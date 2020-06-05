package com.atomscat.consumer.serviceImpl;

import com.atomscat.consumer.config.redis.RedisClient;
import com.atomscat.consumer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 秒杀系统的主要基于以下的原则去实现
 *
 * 1. 系统初始化时，把商品存库数量加载到redis中
 * 2. 当收到秒杀请求后，redis预减库存，库存不足则直接返回
 * 3. 秒杀成功的请求入rabbitMQ，立即返回“正在抢购页面…”，当异步下单成功后才返回订单。
 * 4. 客户端轮询是否秒杀成功，服务器请求出队，生成订单，减少库存。
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    public static final String PRODUCT_ID_KEY = "PID001_";
    private static final Integer PRODUCT_COUNT = 5000;
    private static final String HAS_BUY_USER_KEY = "HAS_BUY_USER_KEY_";
    private static final String LOCK_KEY = "LOCK_KEY_";
    private static final String FAIL_BUYED = "已经买过了";
    private static final String BUYE_SUCCESS = "抢到了，订单生成中";
    private static final String FAIL_SOLD_OUT = "没货了";
    private static final String FAIL_BUSY = "排队中，请重试！";
    @Resource
    private RedisClient redisClient;

    //@Resource
    //private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initOrder() {
        // TODO: 后台可以配置
        redisClient.set(PRODUCT_ID_KEY, PRODUCT_COUNT);
        log.info("商品已经初始化完成：数量：" + PRODUCT_COUNT);
    }

    /**
     * 下单
     *
     * @param userId
     */
    @Override
    public String rushToBuy(String userId) {

        //判断用户是否已买
        Object hasBuy = redisClient.get(HAS_BUY_USER_KEY, userId);
        if (hasBuy != null) {
            return FAIL_BUYED;
        }
        //10s自动过期
        int redisExpireTime = 10 * 1000;
        long lockValue = System.currentTimeMillis() + redisExpireTime;
        //后去redis锁，只有获取成功才能继续操作
        boolean getLock = redisClient.lock(LOCK_KEY, String.valueOf(lockValue));
        log.info(userId + " getLock:" + getLock);
        if (getLock) {
            Integer productCount = (Integer) redisClient.get(PRODUCT_ID_KEY);
            log.info("productCount:" + productCount);
            //库存大于0才能继续下单
            if (productCount > 0) {
                //rabbitTemplate.convertAndSend(RabbitConstants.TOPIC_MODE_QUEUE, "topic.queue", userId);
                //减库存
                redisClient.set(PRODUCT_ID_KEY, (productCount - 1));
                //记录用户已买
                redisClient.set(HAS_BUY_USER_KEY, userId, "1");
                //手动释放锁
                redisClient.unlock(LOCK_KEY, String.valueOf(lockValue));
                return BUYE_SUCCESS;
            } else {
                log.info("亲，" + FAIL_SOLD_OUT);
                //手动释放锁
                redisClient.unlock(LOCK_KEY, String.valueOf(lockValue));
                return FAIL_SOLD_OUT;
            }
        } else {
            return FAIL_BUSY;
        }
    }


}
