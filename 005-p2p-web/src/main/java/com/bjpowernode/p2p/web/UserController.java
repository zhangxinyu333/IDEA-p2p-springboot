package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.loan.FinanceAccount;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.user.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidInfoLoanVO;
import com.bjpowernode.p2p.model.vo.IncomeRecordExtLoanInfo;
import com.bjpowernode.p2p.service.loan.*;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;
import com.bjpowernode.p2p.util.HttpClientUtils;
import com.bjpowernode.p2p.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 张新宇
 * 2020/7/28
 */
@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false)
    private UserService userService;

    @Reference(interfaceClass = RedisService.class, version = "1.0.0", check = false)
    private RedisService redisService;

    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false)
    private FinanceAccountService financeAccountService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = RechargeRecordService.class, version = "1.0.0", check = false)
    private RechargeRecordService rechargeRecordService;

    @Reference(interfaceClass = IncomeRecordService.class, version = "1.0.0", check = false)
    private IncomeRecordService incomeRecordService;

    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;


    @RequestMapping(value = "/loan/page/register")
    public String pageRegister() {
        return "register";
    }

    @RequestMapping(value = "/loan/page/realName")
    public String pageRealName() {
        return "realName";
    }

    @RequestMapping("/loan/checkPhone")
    public @ResponseBody
    Object checkPhone(@RequestParam(value = "phone", required = true) String phone) {
        Map<String, Object> retMap = new HashMap<String, Object>();

        //验证手机号码是否重复(手机号码) --》返回int|boolean|User|String
        //根据手机号码查询用户信息（手机号码）--》返回USer
        User user = userService.queryUserByPhone(phone);

        //判断是否否为空
        if (ObjectUtils.allNotNull(user)) {
            //该手机号已被注册
          /*  retMap.put("code", -1);
            retMap.put("message", "该手机号码已被注册，请更换手机号码");
            retMap.put("success", false);
            return retMap;*/

            return Result.error("该手机号码已被注册，请更换手机号码");
        }
        /*retMap.put("code", 1);
        retMap.put("message", "该手机号码可以注册");
        retMap.put("success", true);
          return retMap;*/
        return Result.success();

    }

    @RequestMapping("/loan/register")
    public @ResponseBody
    Result registry(HttpServletRequest request,
                    @RequestParam(value = "phone", required = true) String phone,
                    @RequestParam(value = "loginPassword", required = true) String loginPassword,
                    @RequestParam(value = "messageCode", required = true) String messageCode) {

        try {
            //从redis中获取短信验证码
            String redisMessageCode = redisService.get(phone);
            //判断短信验证码是否正确
            if (!StringUtils.equals(messageCode, redisMessageCode)) {
                return Result.error("请输入正确的的短信验证码");
            }

            //用户注册,【1.新增用户，2.新增账户】（手机号码，登录密码）--》User
            User user = userService.register(phone, loginPassword);
            //将用户的信息存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER, user);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("注册失败，请重试");
        }


        return Result.success();
    }


    @RequestMapping("/loan/messageCode")
    public @ResponseBody
    Result messageCode(HttpServletRequest request,
                       @RequestParam(value = "phone", required = true) String phone) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String randomCode = "";
        try {
            //调用京东万象平台106短信接口，发送短信验证码内容
            paramMap.put("appkey", "");
            paramMap.put("mobile", phone);

            randomCode = this.getRandomCode(4);
            //短信内容
            String content = "【凯信通】您的验证码是：" + randomCode;
            paramMap.put("content", content);
            String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);
            //模拟报文
            String jsonResult = "{\n" +
                    "                \"code\": \"10000\",\n" +
                    "                \"charge\": false,\n" +
                    "                \"remain\": 0,\n" +
                    "                \"msg\": \"查询成功\",\n" +
                    "                \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-1111611</remainpoint>\\n <taskID>101609164</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                    "            }";
            /*
            {
                "code": "10000",
                "charge": false,
                "remain": 0,
                "msg": "查询成功",
                "result": "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms>\n <returnstatus>Success</returnstatus>\n <message>ok</message>\n <remainpoint>-1111611</remainpoint>\n <taskID>101609164</taskID>\n <successCounts>1</successCounts></returnsms>"
            }
             */
            //使用fastjosn解析json格式的字符串
            //将json格式的字符串转换为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(jsonResult);
            //从json对象中获取code的值
            String code = jsonObject.getString("code");
            //判断通信是否成功
            if (!StringUtils.equals(code, "10000")) {
                return Result.error("短信平台通信异常");
            }

            //获取result所对应xml格式的字符串
            String resultXmlString = jsonObject.getString("result");

            //使用dom4j+xpath来解析xml格式的字符串
            //添加dom4j依赖
            //将xml格式的字符串转换为Document对象
            Document document = DocumentHelper.parseText(resultXmlString);

            //获取returnstatus节点的xpath路径表达式
            Node returnstatusNode = document.selectSingleNode("//returnstatus");
            //获取文本内容
            String returnstatusNodeText = returnstatusNode.getText();

            //判断业务处理结果
            if (!StringUtils.equals("Success", returnstatusNodeText)) {
                return Result.error("短信平台发送失败");
            }

            //将生成的随机数字存放到redis缓存中
            redisService.put(phone, randomCode);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("短信平台异常");
        }


        return Result.success(randomCode);
    }

    private String getRandomCode(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int index = (int) Math.round(Math.random() * 9);
            stringBuilder.append(index);
        }
        return stringBuilder.toString();
    }

    @RequestMapping("/loan/myFinanceAccount")
    public @ResponseBody
    FinanceAccount financeAccount(HttpServletRequest request) {
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //根据用户标识获取用户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
        return financeAccount;
    }

    @RequestMapping("/loan/realName")
    public @ResponseBody
    Result realName(HttpServletRequest request,
                    @RequestParam(value = "realName", required = true) String realName,
                    @RequestParam(value = "idCard", required = true) String idCard,
                    @RequestParam(value = "messageCode", required = true) String messageCode) {

        try {
            //从session中获取用户信息
            User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
            //从redis中获取短信验证码
            String redisMessageCode = redisService.get(sessionUser.getPhone());
            //判断用户输入的验证码和redis中的短信验证码是否一致
            if (!StringUtils.equals(messageCode, redisMessageCode)) {
                return Result.error("请输入正确的短信验证码");
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("appkey", "");
            paramMap.put("cardNo", idCard);
            paramMap.put("realName", realName);
            //调用京东万象平台的“实名认证二要素接口”来验证是否匹配
//            String jsonString=HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test",paramMap);

            //模拟报文
            String jsonString = "{\n" +
                    "    \"code\": \"10000\",\n" +
                    "    \"charge\": false,\n" +
                    "    \"remain\": 1305,\n" +
                    "    \"msg\": \"查询成功\",\n" +
                    "    \"result\": {\n" +
                    "        \"error_code\": 0,\n" +
                    "        \"reason\": \"成功\",\n" +
                    "        \"result\": {\n" +
                    "            \"realname\": \"乐天磊\",\n" +
                    "            \"idcard\": \"350721197702134399\",\n" +
                    "            \"isok\": true\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            //使用fastjosn解析json格式的字符串
            //将json格式的字符串解析为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            //先获取通信标识
            String code = jsonObject.getString("code");
            //判断通信标识是否成功
            if (!StringUtils.equals("10000", code)) {
                return Result.error("实名认证平台，通信异常");
            }
            //获取是否匹配的结果isok
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            //判断是否匹配
            if (!isok) {
                return Result.error("真实姓名与身份证号码不匹配，请重新输入");
            }

            //更新用户信息
            User updateUser = new User();
            updateUser.setId(sessionUser.getId());
            updateUser.setName(realName);
            updateUser.setIdCard(idCard);
            int modifyUserCount = userService.modifyUserById(updateUser);
            if (modifyUserCount < 0) {
                return Result.error("实名认证失败");
            }
            //更新session中用户的信息
            sessionUser.setName(realName);
            sessionUser.setIdCard(idCard);
            request.getSession().setAttribute(Constants.SESSION_USER, sessionUser);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统繁忙，请稍后重试");
        }


        return Result.success();
    }



    @RequestMapping("/loan/page/login")
    public String pageLogin(Model model,
                            @RequestParam(value = "localPageUrl",required = false)String localPageUrl) {

        model.addAttribute("localPageUrl", localPageUrl);
        return "login";
    }

    @RequestMapping("/loan/loadStat")
    public @ResponseBody Object loadStat() {

        Map<String, Object> retMap = new HashMap<String, Object>();
        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();

        //获取平台注册总人数
        Integer allUserCount = userService.queryAllUserCount();

        //获取平台投资总金额
        Double allBidMoney = bidInfoService.selectAllBidMoney();

        retMap.put(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);
        retMap.put(Constants.ALL_USER_COUNT,allUserCount);
        retMap.put(Constants.ALL_BID_MONEY,allBidMoney);

        return retMap;
    }

    @PostMapping("/loan/login")
    public @ResponseBody Result login(HttpServletRequest request,
                                      @RequestParam(value = "phone",required = true)String phone,
                                      @RequestParam(value = "loginPassword",required = true)String loginPassword,
                                      @RequestParam(value = "messageCode",required = true)String messageCode){
        try {
            //从redis缓存中获取短信验证码
            String redisMessageCode = redisService.get(phone);

            //判断用户输入的是否正确
            if (!StringUtils.equals(messageCode,redisMessageCode)){
                return Result.error("请输入正确的短信验证码");
            }

            //用户登录【根据手机号和密码查询用户】【更新最近登录时间（手机号，密码）】返回User
            User user=userService.login(phone,loginPassword);


            //判断用户是否为空
            if (!ObjectUtils.allNotNull(user)){
                return Result.error("手机号或密码有误");
            }

            //将用户的信息存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER, user);


        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录失败");
        }


        return Result.success();
    }

    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request){
        //清除指定session中的key对应值
        request.getSession().removeAttribute(Constants.SESSION_USER);

        //让session失效
//        request.getSession().invalidate();

        return "redirect:/index";
    }

    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model) {

        //从session获取用户标识
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //根据用户标识获取用户信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
        model.addAttribute("financeAccount", financeAccount);

        //将以下查询看做是一个分页
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uid", sessionUser.getId());
//        paramMap.put("uid", 1);
        paramMap.put("currentPage", 0);
        paramMap.put("pageSize", 5);

        //根据用用户标识获取最近投资信息：用户标识，显示第1页，每页显示5条
        List<BidInfoLoanVO> bidInfoLoanVOList = bidInfoService.queryRecentlyBidInfoListByUid(paramMap);
        model.addAttribute("bidInfoLoanVOList", bidInfoLoanVOList);

        //根据用用户标识获取最近充值信息：用户标识，显示第1页，每页显示5条
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRecentlyRechargeRecordListByUid(paramMap);
        model.addAttribute("rechargeRecordList", rechargeRecordList);

        //根据用用户标识获取最近收益信息：用户标识，显示第1页，每页显示5条
        List<IncomeRecordExtLoanInfo> incomeRecordExtLoanInfoList = incomeRecordService.queryRecentlyIncomeRecordListByUid(paramMap);
        model.addAttribute("incomeRecordExtLoanInfoList", incomeRecordExtLoanInfoList);
        return "myCenter";
    }

    @RequestMapping("/loan/myInvest")
    public String allBidInfo(){
        return "myIncome";
    }

    @RequestMapping("/loan/myRecharge")
    public String allRechargeInfo(){
        return "myRecharge";
    }

    @RequestMapping("/loan/myIncome")
    public String allIncomeInfo(){
        return "myIncome";
    }
}
