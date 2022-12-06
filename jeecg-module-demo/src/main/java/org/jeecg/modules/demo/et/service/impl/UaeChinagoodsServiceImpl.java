package org.jeecg.modules.demo.et.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeecg.common.constant.enums.EtEnvEnum;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.demo.et.entity.EventTracking;
import org.jeecg.modules.demo.et.entity.UaeChinagoods;
import org.jeecg.modules.demo.et.mapper.UaeChinagoodsMapper;
import org.jeecg.modules.demo.et.service.IUaeChinagoodsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: uae_chinagoods
 * @Author: jeecg-boot
 * @Date:   2022-11-22
 * @Version: V1.0
 */
@Service
@DS("ghost_sa")
@Slf4j
public class UaeChinagoodsServiceImpl extends ServiceImpl<UaeChinagoodsMapper, UaeChinagoods> implements IUaeChinagoodsService {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${cg.et.topic}")
    private String sourceTopic;

    public static final List<String> NOT_NEED_EVENT = Arrays.asList("$pageview",
            "$WebStay", "$WebClick", "$AppClick", "$AppViewScreen", "profile_set_once",
            "$AppStart", "$AppEnd"
    );

    public KafkaConsumer<String, String> initKafkaConsumer(int batchSize) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // group.id，指定了消费者所属群组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, batchSize);
        // 100MB
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 50 * 1024 * 1024);

        KafkaConsumer<String, String> kafkaConsumer =
                new KafkaConsumer<>(props);
//        kafkaConsumer.subscribe(Collections.singletonList(sourceTopic));
        // 指定每个分区消费1000条数据
        // 然后马上调用 seek() 方法定位分区的偏移量。
        // seek() 方法只更新我们正在使用的位置，在下一次调用 poll() 时就可以获得正确的消息。
        // 如果 seek() 发生错误, poll() 就会抛出异常。
        List<PartitionInfo> partitionInfoList = kafkaConsumer.partitionsFor(sourceTopic);
        List<TopicPartition> topicPartitionList = new ArrayList<>(partitionInfoList.size());
        TopicPartition topicPartition;

        for (PartitionInfo partitionInfo : partitionInfoList) {
            topicPartition = new TopicPartition(sourceTopic, partitionInfo.partition());
            topicPartitionList.add(topicPartition);
        }

        int partitionBatchSize = batchSize / partitionInfoList.size() + 1;
        kafkaConsumer.assign(topicPartitionList);
        Map<TopicPartition, Long> partitionOffsetM = kafkaConsumer.endOffsets(topicPartitionList);
        for (TopicPartition partition: topicPartitionList) {
            log.debug("分区: {}, 当前最后offset为: {}", partition, partitionOffsetM.get(partition));
            kafkaConsumer.seek(partition, Math.max(partitionOffsetM.get(partition) - partitionBatchSize, 0));
        }
        return kafkaConsumer;
    }

    /**
     * 批量消费kafka消息
     **/
    public List<UaeChinagoods> pollMessage(KafkaConsumer<String, String> consumer, UaeChinagoods uaeChinagoods, Integer pageNo, Integer pageSize, HttpServletRequest req) throws ParseException {
        String env = req.getParameter("env");
        String remark = env.equals(EtEnvEnum.PROD.etEnv) ? "online" : "stg";

        String distinctId = uaeChinagoods.getDistinctId();
        String event = uaeChinagoods.getEvent();
        String project = req.getParameter("project");
        String platformType = req.getParameter("platformType");
        int batchSize = Integer.parseInt(req.getParameter("batchSize"));
        long beginCreatedAt = 0L, endCreatedAt = 0L;
        // 基于时间查询
        String[] createdAtArr = req.getParameterValues("createdAtArr[]");
        if (createdAtArr != null && createdAtArr.length == 2) {
            beginCreatedAt = DateUtils.parseTimestamp(createdAtArr[0], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
            endCreatedAt = DateUtils.parseTimestamp(createdAtArr[1], "yyyy-MM-dd HH:mm:ss").toInstant().getEpochSecond() * 1000;
        }
        long finalBeginCreatedAt = beginCreatedAt;
        long finalEndCreatedAt = endCreatedAt;

        int consumerCount = 0;
        List<UaeChinagoods> resultList = new ArrayList<>(batchSize);
        while (consumerCount <= batchSize ) {
            // 100 是超时时间（ms），在该时间内 poll 会等待服务器返回数据
            ConsumerRecords<String, String> records = consumer.poll(3000);
            log.info("批量获取消息，当前获取消息大小为： {}", records.count());
            consumerCount += records.count();

            // poll 返回一个记录列表。
            // 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对。
            List<UaeChinagoods> tmpResultList = Streams.stream(records)
                    .map(record -> EventTracking.of(record.value()))
                    .filter((et) -> !NOT_NEED_EVENT.contains(et.getEvent()) && StringUtils.isNotBlank(et.getEvent()))
                    .map(EventTracking::toChinagoods)
                    .filter(et -> {
                        boolean ret = true;
                        if (StringUtils.isNotBlank(distinctId)) {
                            ret = StringUtils.equals(distinctId, et.getDistinctId());
                            if (!ret) {
                                return false;
                            }
                        }

                        if (StringUtils.isNotBlank(event)) {
                            ret = StringUtils.equals(event, et.getEvent());
                            if (!ret) {
                                return false;
                            }
                        }

                        // remark
                        if (StringUtils.isNotBlank(remark)) {
                            ret = StringUtils.equals(remark, et.getRemark());
                            if (!ret) {
                                return false;
                            }
                        }

                        if (StringUtils.isNotBlank(project)) {
                            ret = StringUtils.equals(project, et.getProject());
                            if (!ret) {
                                return false;
                            }
                        }

                        // platformType
                        if (StringUtils.isNotBlank(platformType)) {
                            ret = StringUtils.equals(platformType, et.getPlatformType());
                            if (!ret) {
                                return false;
                            }
                        }
                        // createdAt
                        if (finalBeginCreatedAt != 0 && finalEndCreatedAt != 0) {
                            ret = false;
                            long createdAt = Long.parseLong(et.getCreatedAt());
                            if (createdAt >= finalBeginCreatedAt && createdAt <= finalEndCreatedAt) {
                                ret = true;
                            }
                        }
                        return ret;
                    }).collect(Collectors.toList());
            resultList.addAll(tmpResultList);
        }
        return resultList;
    }


    /**
     * 查询kafka消息
     * @param uaeChinagoods 查询uae实例
     * @param pageNo        页吗
     * @param pageSize      页面尺寸
     * @param req           请求实例
     * @return 页面
     * @throws ParseException
     */
    @Override
    public IPage<UaeChinagoods> queryKafkaMessage(UaeChinagoods uaeChinagoods, Integer pageNo, Integer pageSize, HttpServletRequest req) throws ParseException {
        int batchSize = Integer.parseInt(req.getParameter("batchSize"));
        KafkaConsumer<String, String> kafkaConsumer = initKafkaConsumer(batchSize);
        // 查询每个分区1000条消息
        List<UaeChinagoods> resultList = pollMessage(kafkaConsumer, uaeChinagoods, pageNo, pageSize, req);
        // 关闭kafka连接，避免重新加入
        kafkaConsumer.close();

        Page<UaeChinagoods> page = new Page<UaeChinagoods>(pageNo, pageSize);
        page.setTotal(resultList.size())
                .setRecords(resultList)
                .setPages(1)
                .setCurrent(1)
                .setSize(resultList.size())
        ;
        return page;
    }
}
