package com.bjpowernode.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.user.RechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 张新宇
 * 2020/8/7
 */
@Component
@Service(interfaceClass = RedisService.class,version = "1.0.0",timeout = 15000)
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key,value,60, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long getOnlyNumber() {
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER,1);
    }


}
