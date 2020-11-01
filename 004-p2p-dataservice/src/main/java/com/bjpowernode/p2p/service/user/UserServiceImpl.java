package com.bjpowernode.p2p.service.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.loan.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 张新宇
 * 2020/7/25
 */
@Component
@Service(interfaceClass = UserService.class,version = "1.0.0",timeout = 15000)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Integer queryAllUserCount() {
        //获取到操作指定key的操作对象
        BoundValueOperations<Object, Object> boundValueOperations = redisTemplate.boundValueOps(Constants.ALL_USER_COUNT);
        //从该对象中获取对象总人数
        Integer allUserCount = (Integer) boundValueOperations.get();
        //判断是否有值
        if (!ObjectUtils.allNotNull(allUserCount)){
            synchronized (this){
                //再次从redis缓存中查询
                allUserCount = (Integer) boundValueOperations.get();
                //判断是否有值
                if (!ObjectUtils.allNotNull(allUserCount)){
                    //从数据库查询
                    allUserCount=userMapper.selectAllUserCount();
                    //再存放到redis缓存中
                    boundValueOperations.set(allUserCount,25, TimeUnit.MINUTES);
                }
            }
        }
        return allUserCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Transactional
    @Override
    public User register(String phone, String loginPassword) throws Exception {
        //1.新增用户
        User userRecord=new User();
        userRecord.setPhone(phone);
        userRecord.setLoginPassword(loginPassword);
        userRecord.setAddTime(new Date());
        userRecord.setLastLoginTime(new Date());
        int userInsertCount=userMapper.insertSelective(userRecord);
        if (userInsertCount<0){
            throw new Exception();
        }

        //根据手机号码查询用户信息
//        User user=userMapper.selectUserByPhone(phone);


        //2.新增账户

        FinanceAccount financeAccount=new FinanceAccount();

        financeAccount.setUid(userRecord.getId());
        financeAccount.setAvailableMoney(888.0);
        int insertFinanceAccountCount=financeAccountMapper.insertSelective(financeAccount);
        if (insertFinanceAccountCount<0){
            throw new Exception();
        }

        return userRecord;
    }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {

        User userDetail=new User();
        userDetail.setPhone(phone);
        userDetail.setLoginPassword(loginPassword);

        //根据手机号码和密码查询用户信息
        User user=userMapper.selectUserByPhoneAndLoginPassword(userDetail);

        //判断用户是否存在
        if (ObjectUtils.allNotNull(user)){
            //更新最近登录时间
            User updateUser=new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);
        }

        return user;
    }
}
