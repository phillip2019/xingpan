package org.jeecg.modules.et.websocket;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Streams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.et.entity.*;
import org.jeecg.modules.et.service.IEtBuProjectEventService;
import org.jeecg.modules.et.service.IEtEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private static String bootstrapServers;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    public void setBootstrapServers(String bootstrapServers) {
        TeUaeChinagoodsWebSocket.bootstrapServers = bootstrapServers;
    }

    private static String consumerGroupId;
    @Value("${spring.kafka.consumer.group-id}")
    public void setConsumerGroupId(String consumerGroupId) {
        TeUaeChinagoodsWebSocket.consumerGroupId = consumerGroupId;
    }

    private static String sourceTopic;
    @Value("${cg.et.topic}")
    public void setSourceTopic(String sourceTopic) {
        TeUaeChinagoodsWebSocket.sourceTopic = sourceTopic;
    }

    private static IEtBuProjectEventService etBuProjectEventService;
    @Autowired
    public void setEtBuProjectEventService(IEtBuProjectEventService etBuProjectEventService) {
        TeUaeChinagoodsWebSocket.etBuProjectEventService = etBuProjectEventService;
    }

    private static IEtEventService etEventService;
    @Autowired
    public void setEtEventService(IEtEventService etEventService) {
        TeUaeChinagoodsWebSocket.etEventService = etEventService;
    }

    /**
     * 线程安全Map
     */
    private static final ConcurrentHashMap<String, Session> SESSION_POOL = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicBoolean> SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP = new ConcurrentHashMap<>();

    private static ExecutorService THREAD_POOL = Executors.newCachedThreadPool();


    //==========【websocket接受、推送消息等方法 —— 具体服务节点推送ws消息】========================================================================================
    @OnOpen
    public void onOpen(Session session,
                       @PathParam(value = "userId") String userId
    ) throws ParseException {
        try {
            SESSION_POOL.put(userId, session);
            SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.put(userId, new AtomicBoolean(false));
            log.info("【埋点系统 WebSocket】有新的连接，总数为:" + SESSION_POOL.size());
        } catch (Exception e) {
            log.error("【埋点系统 WebSocket】有新的连接失败, userId: {}", userId, e);
        }

        String queryString = session.getQueryString() + "&";
        String ip = StringUtils.substringBetween(queryString, "ip=", "&");
        String distinctId = StringUtils.substringBetween(queryString, "distinctId=", "&");
        String anonymousId = StringUtils.substringBetween(queryString, "anonymousId=", "&");
        String buProjectNameId = StringUtils.substringBetween(queryString, "buProjectNameId=", "&");
        String scene = StringUtils.substringBetween(queryString, "scene=", "&");

        UaeWSParamChinagoods wsParamChinagoods = new UaeWSParamChinagoods();
        wsParamChinagoods.setDistinctId(distinctId)
                .setAnonymousId(anonymousId)
                .setIp(ip)
                .setScene(scene)
                .setBuProjectNameId(buProjectNameId)
        ;

        log.info("开始接受");
        //------------------------------------------------------------------------------
        this.pollMessage(userId, wsParamChinagoods);
        //------------------------------------------------------------------------------
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId) throws InterruptedException {
        log.info("【系统 WebSocket】连接断开，用户编号为: {}", userId);

        if (StringUtils.isNotBlank(userId)) {
            // 关闭kafka消费
            if (SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.containsKey(userId)) {
                SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.get(userId).set(false);
            }
            // 阻塞300ms，等待kafka消费线程结束
            Thread.sleep(300);

            SESSION_POOL.remove(userId);
            SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.remove(userId);
        }
        log.info("【系统 WebSocket】连接断开，总数为:" + SESSION_POOL.size());
    }

    /**
     * ws推送消息
     *
     * @param userId           用户编号
     * @param etChinagoodsList 埋点事件列表
     */
    public void pushMessage(String userId, List<EtChinagoods> etChinagoodsList) {
        // 若session_pool中存在此userId可以，则获取此session，然后发送消息
        if (SESSION_POOL.containsKey(userId)) {
            Session session = SESSION_POOL.get(userId);
            if (etChinagoodsList != null && !etChinagoodsList.isEmpty()) {
                try {
                    //update-begin-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                    synchronized (session) {
                        String message = JSONObject.toJSONString(etChinagoodsList);
                        log.debug("【系统 WebSocket】推送单人消息:" + message);
                        if (session.isOpen()) {
                            session.getBasicRemote().sendText(message);
                        }
                    }
                    //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                } catch (Exception e) {
                    log.error("【埋点验证】消息推送单人: {}, 消息失败", userId, e);
                }
            } else {
                log.debug("【埋点验证】消息推送单人: {}, 消息为空", userId);
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
                    synchronized (session) {
                        log.debug("【系统 WebSocket】推送单人消息:" + message);
                        session.getBasicRemote().sendText(message);
                    }
                    //update-end-author:taoyan date:20211012 for: websocket报错 https://gitee.com/jeecg/jeecg-boot/issues/I4C0MU
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * ws接受客户端消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam(value = "userId") String userId) throws ParseException, InterruptedException {
        // 消息体为json字符串，解析消息体，若消息体对象中type等于close_connection，则执行close方法，做清理操作
        JSONObject jn = JSONObject.parseObject(message);
        if ("close_connection".equals(jn.getString("type"))) {
            this.onClose(userId);
        }
        log.info("【系统 WebSocket】收到客户端消息: {}", message);
        // 若kafka实例已经初始化，则后续代码不需要执行
        if (SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.get(userId).get()) {
            JSONObject obj = new JSONObject();
            //业务类型
            obj.put("code", 200);
            //消息内容
            obj.put("msg", "success");
            this.pushMessageString(userId, obj.toJSONString());
        }
    }

    public KafkaConsumer<String, String> initKafkaConsumer(String userId, int batchSize) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // group.id，指定了消费者所属群组
        final String groupId = String.format("%s_%s", consumerGroupId, userId);
        final String shortGroupId = shortenGroupId(groupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, shortGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, batchSize);
        // 100MB
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 50 * 1024 * 1024);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // 禁用自动提交偏移量
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        KafkaConsumer<String, String> kafkaConsumer =
                new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(sourceTopic));
        return kafkaConsumer;
    }

    private static String shortenGroupId(String longGroupId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longGroupId.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString().substring(0, 10);  // 取前10个字符作为缩短的 group.id
        } catch (NoSuchAlgorithmException e) {
            log.error("Hash算法不可用", e);
            return longGroupId;  // 如果哈希算法不可用，使用原始 group.id
        }
    }

    /**
     * 批量消费kafka消息
     **/
    public void pollMessage(String userId, UaeWSParamChinagoods wsParamChinagoods) throws ParseException {

        // 创建一个新线程来处理消息消费
        THREAD_POOL.execute(() -> {
            String distinctId = wsParamChinagoods.getDistinctId();
            String anonymousId = wsParamChinagoods.getAnonymousId();
            String ip = wsParamChinagoods.getIp();
            String event = wsParamChinagoods.getEvent();
            String buProjectNameId = wsParamChinagoods.getBuProjectNameId();
            String scene = wsParamChinagoods.getScene();
            List<String> eventNameList = new ArrayList<>();
            if (StringUtils.isBlank(event)) {
                LambdaQueryWrapper<EtEvent> etQueryWrapper = new LambdaQueryWrapper<>();
                if (StringUtils.isBlank(scene)) {
                    // 查询buProjectNameId对应的event列表
                    LambdaQueryWrapper<EtBuProjectEvent> etBuProjectEventQueryWrapper = new LambdaQueryWrapper<>();
                    etBuProjectEventQueryWrapper.eq(EtBuProjectEvent::getBuProjectId, buProjectNameId);
                    List<EtBuProjectEvent> buProjectEventList = etBuProjectEventService.list(etBuProjectEventQueryWrapper);
                    List<String> eventIds = buProjectEventList.stream().map(EtBuProjectEvent::getEventId).collect(Collectors.toList());
                    if (!eventIds.isEmpty()) {
                        etQueryWrapper.in(EtEvent::getId, eventIds);
                    }
                } else {
                    // 查询scene非空场景
                    etQueryWrapper.eq(EtEvent::getScene, scene);
                }
                List<EtEvent> eventList = etEventService.list(etQueryWrapper);
                eventNameList.addAll(eventList.stream().map(EtEvent::getName).collect(Collectors.toList()));
            } else {
                eventNameList.addAll(Arrays.asList(StringUtils.split(event, ",")));
            }



            try (KafkaConsumer<String, String> kafkaConsumer = initKafkaConsumer(userId, 100)) {
                SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.get(userId).set(true);
                // 判断当前用户会话是否存在，若还存在则继续循环
                while (SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.containsKey(userId) && SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.get(userId).get()) {
                    // 100 是超时时间（ms），在该时间内 poll 会等待服务器返回数据
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(3000);
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

                                if (!eventNameList.isEmpty()) {
                                    ret = eventNameList.contains(et.getEvent());
                                    if (!ret) {
                                        return false;
                                    }
                                }
                                log.debug("UserId: {}，过滤参数为，distinctId: {}，anonymousId: {}， ip: {}, event: {}", userId, distinctId, anonymousId, ip, event);
                                return ret;
                            })
                            // 升序
                            .sorted(Comparator.comparing(EtChinagoods::getCreatedAt)).collect(Collectors.toList());

                    if (!tmpResultList.isEmpty()) {
                        log.debug("埋点事件，当前获取埋点事件条数： {}", tmpResultList.size());
                        this.pushMessage(userId, tmpResultList);
                    }
                }
            } catch (Exception e) {
                SESSION_POOL.remove(userId);
                SESSION_POOL_KAFKA_CONSUMER_FLAG_MAP.remove(userId);
                // 若异常为InterruptedException，则直接关闭，不打印异常信息和推送异常信息，若为其它，则打印异常和推送异常消息
                if (e instanceof org.apache.kafka.common.errors.InterruptException) {
                    return;
                }

                log.error("【系统 WebSocket】批量获取消息失败，发生异常为: ", e);
                // 若无数据，则传递null，表明发生异常
                this.pushMessage(userId, null);
            }
        });
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