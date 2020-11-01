package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.user.RechargeRecord;

/**
 * 张新宇
 * 2020/8/7
 */
public interface RedisService {

    /**
     * 将只存放到Redis中String数据类型
     * @param key
     * @param value
     */
    void put(String key, String value);

    /**
     * 获取指定key的值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取唯一数字
     * @return
     */
    Long getOnlyNumber();


}
