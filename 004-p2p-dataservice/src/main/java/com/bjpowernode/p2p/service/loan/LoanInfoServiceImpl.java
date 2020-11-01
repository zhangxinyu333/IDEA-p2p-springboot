package com.bjpowernode.p2p.service.loan;

import com.alibaba.dubbo.config.annotation.Service;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 张新宇
 * 2020/7/25
 */
@Component
@Service(interfaceClass = LoanInfoService.class,version = "1.0.0",timeout = 15000)
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {

        //设置redis Template模板对象的keySerializer属性方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //首先去redis缓存中查询
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        //判断是否有值
        if (!ObjectUtils.allNotNull(historyAverageRate)){
            synchronized (this) {
                historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
                //判断是否有值
                if (!ObjectUtils.allNotNull(historyAverageRate)){
                    //从数据库查询
                    historyAverageRate =loanInfoMapper.selectHistoryAverageRate();
                    //添加到redis缓冲中
                    redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate,20, TimeUnit.SECONDS);
                }
            }
        }
        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {


        return loanInfoMapper.selectLoanInfoListByProductType(paramMap);
    }

    @Override
    public PaginationVo<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap) {

        PaginationVo<LoanInfo> paginationVo = new PaginationVo();

        //查询总记录数
        Long total=loanInfoMapper.selectTotal(paramMap);
        paginationVo.setTotal(total);

        //每页展示的数据集合
        List<LoanInfo> loanInfoList=loanInfoMapper.selectLoanInfoListByProductType(paramMap);
        paginationVo.setDataList(loanInfoList);

        return paginationVo;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }
}
