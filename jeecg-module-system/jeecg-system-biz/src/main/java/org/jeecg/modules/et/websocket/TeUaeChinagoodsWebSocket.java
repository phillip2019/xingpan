package org.jeecg.modules.et.websocket;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.shiro.util.CollectionUtils;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.modules.redis.client.JeecgRedisClient;
import org.jeecg.modules.et.entity.EtChinagoods;
import org.jeecg.modules.et.entity.EventTracking;
import org.jeecg.modules.et.entity.UaeWSParamChinagoods;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/et/ws/{userId}")
public class TeUaeChinagoodsWebSocket {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers = "172.18.5.15:9092,172.18.5.16:9092,172.18.5.17:9092,172.18.5.6:9092,172.18.5.9:9092";

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId = "xingpang";

    @Value("${cg.et.topic}")
    private String sourceTopic = "events-tracking";
    
    /**线程安全Map*/
    private static final ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Boolean> SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP = new ConcurrentHashMap<>();


    //==========【websocket接受、推送消息等方法 —— 具体服务节点推送ws消息】========================================================================================
    @OnOpen
    public void onOpen(Session session,
                       @PathParam(value = "userId") String userId
                       ) throws ParseException {
        try {
            SESSION_POOL.put(userId, session);
            SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.put(userId, Boolean.FALSE);
            log.info("【埋点系统 WebSocket】有新的连接，总数为:" + SESSION_POOL.size());
        } catch (Exception e) {
            log.error("【埋点系统 WebSocket】有新的连接失败, userId: {}", userId, e);
        }

        String queryString = session.getQueryString();
        String ip = StringUtils.substringBetween(queryString, "ip=", "&");
        String distinctId = StringUtils.substringBetween(queryString, "distinctId=", "&");
        String anonymousId = StringUtils.substringBetween(queryString, "anonymousId=", "&");

        UaeWSParamChinagoods wsParamChinagoods = new UaeWSParamChinagoods();
        wsParamChinagoods.setDistinctId(distinctId)
                .setAnonymousId(anonymousId)
                .setIp(ip);

        log.info("开始接受");
        //------------------------------------------------------------------------------
        this.pollMessage(userId, wsParamChinagoods);
        //------------------------------------------------------------------------------
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        if (StringUtils.isNotBlank(userId)) {
            SESSION_POOL.remove(userId);
            SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.remove(userId);
        }
        log.info("【系统 WebSocket】连接断开，总数为:" + SESSION_POOL.size());
    }

    /**
     * ws推送消息
     *
     * @param userId 用户编号
     * @param etChinagoodsList 埋点事件列表
     */
    public void pushMessage(String userId, List<EtChinagoods> etChinagoodsList) {
        // 若session_pool中存在此userId可以，则获取此session，然后发送消息
        if (SESSION_POOL.containsKey(userId)) {
            Session session = SESSION_POOL.get(userId);
            if (etChinagoodsList != null && !etChinagoodsList.isEmpty()) {
                try {
                    //update-begin-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                    synchronized (session){
                        String message = JSONObject.toJSONString(etChinagoodsList);
                        log.debug("【系统 WebSocket】推送单人消息:" + message);
                        session.getBasicRemote().sendText(message);
                    }
                    //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                } catch (Exception e) {
                    log.error("【埋点验证】消息推送单人: {}, 消息失败", userId, e);
                }
            } else {
                log.error("【埋点验证】消息推送单人: {}, 消息为空", userId);
            }
        }
    }

    /**
     * ws推送消息
     *
     * @param userId
     * @param message
     */
    public void pushMessageString(String userId, String message) {
        for (Map.Entry<String, Session> item : SESSION_POOL.entrySet()) {
            //userId key值= {用户id + "_"+ 登录token的md5串}
            //TODO vue2未改key新规则，暂时不影响逻辑
            if (item.getKey().contains(userId)) {
                Session session = item.getValue();
                try {
                    //update-begin-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                    synchronized (session){
                        log.debug("【系统 WebSocket】推送单人消息:" + message);
                        session.getBasicRemote().sendText(message);
                    }
                    //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * ws接受客户端消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "userId") String userId) throws ParseException {
        if(!"ping".equals(message) && !WebsocketConst.CMD_CHECK.equals(message)){
            log.info("【系统 WebSocket】收到客户端消息:" + message);
        } else {
            log.debug("【系统 WebSocket】收到客户端消息:" + message);
        }
        // 若kafka实例已经初始化，则后续代码不需要执行
        if (SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.get(userId)) {
            JSONObject obj = new JSONObject();
            //业务类型
            obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_CHECK);
            //消息内容
            obj.put(WebsocketConst.MSG_TXT, "心跳响应");
            this.pushMessageString(userId, obj.toJSONString());
        }
    }

    public KafkaConsumer<String, String> initKafkaConsumer(String userId, int batchSize) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // group.id，指定了消费者所属群组
        final String groupId = String.format("%s_%s", consumerGroupId, userId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, batchSize);
        // 100MB
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 50 * 1024 * 1024);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer =
                new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(sourceTopic));
        return kafkaConsumer;
    }

    /**
     * 批量消费kafka消息
     **/
    public void pollMessage(String userId, UaeWSParamChinagoods wsParamChinagoods) throws ParseException {

        String distinctId = wsParamChinagoods.getDistinctId();
        String anonymousId = wsParamChinagoods.getAnonymousId();
        String ip = wsParamChinagoods.getIp();
        try (KafkaConsumer<String, String> kafkaConsumer = initKafkaConsumer(userId,500)) {

            SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.put(userId, Boolean.TRUE);

            // 判断当前用户会话是否存在，若还存在则继续循环
            while (SESSION_POOL.containsKey(userId)) {
                // 100 是超时时间（ms），在该时间内 poll 会等待服务器返回数据
                ConsumerRecords<String, String> records = kafkaConsumer.poll(3000);
                log.debug("批量获取消息，当前获取消息大小为： {}", records.count());

                // poll 返回一个记录列表。
                // 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对。
                List<EtChinagoods> tmpResultList = Streams.stream(records)
                        .map(record -> EventTracking.of(record.value()))
                        .map(EventTracking::toChinagoods)
                        .filter(et -> {
                            boolean ret = false;
                            if (StringUtils.isNotBlank(distinctId)) {
                                ret = StringUtils.equals(distinctId, et.getDistinctId());
                                if (!ret) {
                                    return false;
                                }
                            }

                            if (StringUtils.isNotBlank(anonymousId)) {
                                ret = StringUtils.equals(anonymousId, et.getAnonymousId());
                                if (!ret) {
                                    return false;
                                }
                            }

                            if (StringUtils.isNotBlank(ip)) {
                                ret = StringUtils.equals(ip, et.getIp());
                                if (!ret) {
                                    return false;
                                }
                            }
                            return ret;
                        }).collect(Collectors.toList());

                this.pushMessage(userId, tmpResultList);
            }
        } catch (Exception e) {
            log.error("【系统 WebSocket】批量获取消息失败，发生异常为: ", e);
            // 若无数据，则传递null，表明发生异常
            this.pushMessage(userId, null);
        }
    }

    /**
     * 配置错误信息处理
     *
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
        log.warn("【系统 WebSocket】消息出现错误");
        // 开始移除此session， 遍历SESSION_POOL，比对session若此session存在，则移除
        for (Map.Entry<String, Session> entry : SESSION_POOL.entrySet()) {
            String k = entry.getKey();
            Session v = entry.getValue();
            if (v == session) {
                SESSION_POOL.remove(k);
                break;
            }
        }
    }
    //==========【系统 WebSocket接受、推送消息等方法 —— 具体服务节点推送ws消息】========================================================================================
}