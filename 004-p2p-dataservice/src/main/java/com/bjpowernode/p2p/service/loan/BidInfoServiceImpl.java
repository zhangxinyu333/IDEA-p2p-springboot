package com.bjpowernode.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.mapper.user.UserMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidInfoExtUser;
import com.bjpowernode.p2p.model.vo.BidInfoLoanVO;
import com.bjpowernode.p2p.model.vo.BidUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 张新宇
 * 2020/7/25
 */
@Component
@Service(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
public class BidInfoServiceImpl implements BidInfoService {

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public Double selectAllBidMoney() {
        //获取操作对象key的操作对象
        BoundValueOperations<Object, Object> boundValueOperations = redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);
        //从redis中获取该值
        Double allBidMoney= (Double) boundValueOperations.get();
        //判断是否为空
        if (!ObjectUtils.allNotNull(allBidMoney)) {
            synchronized (this){
                //从redis中获取该值
                allBidMoney= (Double) boundValueOperations.get();
                //判断是否为空
                if (!ObjectUtils.allNotNull(allBidMoney)) {
                    //没有，从数据库查询
                    allBidMoney=bidInfoMapper.selectAllBidMoney();
                    //在存入到redis缓存中
                    boundValueOperations.set(allBidMoney,20, TimeUnit.MINUTES);
                }
            }
        }
        return allBidMoney;
    }

    @Override
    public List<BidInfoExtUser> queryRecentlyBidInfoListByLoanId(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoByLoanId(paramMap);
    }

    @Override
    public List<BidInfoLoanVO> queryRecentlyBidInfoListByUid(Map<String, Object> paramMap) {
        return bidInfoMapper.selectRecentlyBidInfoByUid(paramMap);
    }

    @Transactional
    @Override
    public void invest(Map<String, Object> paramMap) throws Exception {
        Integer uid= (Integer) paramMap.get("uid");
        Integer loanId= (Integer) paramMap.get("loanId");
        Double bidMoney= (Double) paramMap.get("bidMoney");

        //更新产品剩余可投金额
        //再多线程高并发的时候可能引发“超卖现象”，使用数据库乐观锁机制来解决这个问题
        LoanInfo loanInfoDetail = loanInfoMapper.selectByPrimaryKey(loanId);
        paramMap.put("version", loanInfoDetail.getVersion());

        int updateLeftProductMoneyCount=loanInfoMapper.updateLeftProductMoneyById(paramMap);
        if (updateLeftProductMoneyCount<=0){
            throw new Exception();
        }


        //更新账户可用余额
        int updateFinanceAccountByBid=financeAccountMapper.updateFinanceAccountByUid(paramMap);
        if (updateFinanceAccountByBid<=0){
            throw new Exception();
        }
        //新增投资记录
        BidInfo bidInfo=new BidInfo();
        bidInfo.setUid(uid);
        bidInfo.setLoanId(loanId);
        bidInfo.setBidMoney(bidMoney);
        bidInfo.setBidTime(new Date());
        bidInfo.setBidStatus(1);
        int insertSelectiveCount = bidInfoMapper.insertSelective(bidInfo);
        if (insertSelectiveCount<=0){
            throw new Exception();
        }

        //再次获取产品详情
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        //判断产品是否满标
        if (0 == loanInfo.getLeftProductMoney()){
            //产品已满标，更新产品满标时间和满标状态

            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanId);
            updateLoanInfo.setProductFullTime(new Date());
            updateLoanInfo.setProductStatus(1);
            int updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
            if (updateLoanInfoCount<=0){
                throw new Exception();
            }
        }

        User user = userMapper.selectByPrimaryKey(uid);
        //将用户的投资金额存放到redis缓存中
        redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,user.getPhone() , bidMoney);


    }

    @Override
    public List<BidUser> queryBidUserTop() {

        List<BidUser> bidUserList=new ArrayList<BidUser>();

        //从redis缓存中获取用户排行
        Set<ZSetOperations.TypedTuple<Object>> set = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 5);
        //获取set集合的迭代器
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = set.iterator();
        //循环遍历
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();

            BidUser bidUser = new BidUser();
            bidUser.setPhone(phone);
            bidUser.setScore(score);
            bidUserList.add(bidUser);

        }



        return bidUserList;
    }
}
