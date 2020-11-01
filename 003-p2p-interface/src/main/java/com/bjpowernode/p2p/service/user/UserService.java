package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.User;

/**
 * 张新宇
 * 2020/7/25
 */
public interface UserService {

    /**
     * 获取平台用户数
     * @return
     */
    Integer queryAllUserCount();

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    User register(String phone, String loginPassword) throws Exception;

    /**
     * 根据用户标识更新用户信息
     * @param user
     * @return
     */
    int modifyUserById(User user);

    /**
     * 用户登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User login(String phone, String loginPassword);

}
