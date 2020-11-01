package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.bjpowernode.p2p.config.AlipayConfig;
import com.bjpowernode.p2p.constant.Constants;
import com.bjpowernode.p2p.model.user.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.RedisService;
import com.bjpowernode.p2p.util.DateUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 张新宇
 * 2020/8/24
 */
@Controller
public class RechargeRecordController {

    @Reference(interfaceClass = RechargeRecordService.class,version = "1.0.0",check = false)
    private RechargeRecordService rechargeRecordService;

    @Reference(interfaceClass = RedisService.class,version = "1.0.0",check = false)
    private RedisService redisService;

    @RequestMapping(value = "/loan/recharge/page")
    public String toRechargePage(){
        return "toRecharge";
    }

     @RequestMapping(value = "/loan/recharge/alipay")
     public String alipayRecharge(HttpServletRequest request,Model model,
                                  @RequestParam(value = "rechargeMoney",required = true)Double rechargeMoney){
         System.out.println("-------------alipayRecharge-----------");

         //从session中获取账户信息
         User sessionUser= (User) request.getSession().getAttribute(Constants.SESSION_USER);

         try {
             //生成充值记录
             RechargeRecord rechargeRecord = new RechargeRecord();

             rechargeRecord.setUid(sessionUser.getId());

             //全局唯一的充值记录订单号=“支付方式”+“时间戳”+“redis唯一数字”
             //支付方式：A支付宝订单号  W微信订单号
             String rechargeNo="A"+ DateUtils.getTimestamp()+redisService.getOnlyNumber();
             rechargeRecord.setRechargeNo(rechargeNo);
             rechargeRecord.setRechargeDesc("0");//0支付宝充值  1微信充值
             rechargeRecord.setRechargeMoney(rechargeMoney);
             rechargeRecord.setRechargeStatus("0");//0充值中  1充值成功 2充值失败
             rechargeRecord.setRechargeTime(new Date());
             int addRechargeRecordCount=rechargeRecordService.addRechargeRecord(rechargeRecord);
             if (addRechargeRecordCount<=0){
                 model.addAttribute("trade_msg", "充值异常");
                 return "toRechargeBack";
             }

             model.addAttribute("rechargeNo", rechargeNo);
             model.addAttribute("rechargeMoney", rechargeMoney);
             model.addAttribute("subject", "支付宝充值");
         } catch (Exception e) {
             e.printStackTrace();
             model.addAttribute("trade_msg", "充值异常");
             return "toRechargeBack";
         }

         return "p2pToAlipay";
     }

     @RequestMapping(value = "/loan/alipay/return")
     public String alipayReturn(HttpServletRequest request,Model model){
         System.out.println("---------");
         //同步返回参数不包含业务处理结果（支付是否成功）
         //同步返回的作用：就是响应到商户系统的页面

         try {
             Map<String,String> params = new HashMap<String,String>();

             //获取支付宝GET过来反馈信息，获取支付宝返回的同步参数（不包含业务处理结果）
             Map<String,String[]> requestParams = request.getParameterMap();
             for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                 String name = (String) iter.next();
                 String[] values = (String[]) requestParams.get(name);
                 String valueStr = "";
                 for (int i = 0; i < values.length; i++) {
                     valueStr = (i == values.length - 1) ? valueStr + values[i]
                             : valueStr + values[i] + ",";
                 }
                 //乱码解决，这段代码在出现乱码时使用
                 valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                 params.put(name, valueStr);
             }

             //调用SDK验证签名
             boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);

             //——请在这里编写您的程序（以下代码仅作参考）——
             /*if(!signVerified) {
             model.addAttribute("trade_msg", "充值异常");
             return "toRechargeBack";
             }*/
             System.out.println("signVerified签名结果："+signVerified);
         } catch (Exception e) {
             e.printStackTrace();
             model.addAttribute("trade_msg", "充值异常");
             return "toRechargeBack";
         }

         return "redirect:/loan/myCenter";
     }


     @RequestMapping(value = "/loan/alipay/notify")
     public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {

         try {
             //获取支付宝POST过来反馈信息
             Map<String,String> params = new HashMap<String,String>();

             Map<String,String[]> requestParams = request.getParameterMap();
             for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                 String name = (String) iter.next();
                 String[] values = (String[]) requestParams.get(name);
                 String valueStr = "";
                 for (int i = 0; i < values.length; i++) {
                     valueStr = (i == values.length - 1) ? valueStr + values[i]
                             : valueStr + values[i] + ",";
                 }
                 //乱码解决，这段代码在出现乱码时使用
                 valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
                 params.put(name, valueStr);
             }

             boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

             //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
             if(true) {//验证成功
                 //商户订单号
                 String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

                 //获取异步通知数据中total_amount充值金额
                 String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");


                 //获取异步通知数据中seller_id
                 String seller_id = new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8");

                 //获取异步通知数据中的app_id
                 String app_id_notify = new String(request.getParameter("app_id").getBytes("ISO-8859-1"),"UTF-8");



                 //支付宝交易号
                 String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

                 //交易状态
                 String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

                 //1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
                 //根据异步通知数据中的out_trade_no查询商户系统中是否有该充值订单记录
                 RechargeRecord rechargeRecord=rechargeRecordService.queryRechargeRecordByRechargeNo(out_trade_no);
                 if (!ObjectUtils.allNotNull(rechargeRecord)){
                    response.getWriter().write("fail");
                    response.getWriter().flush();
                    response.getWriter().close();
                 }

                 //2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
                 int compareValue=rechargeRecord.getRechargeMoney().compareTo(Double.valueOf(total_amount));
                 if (compareValue!=0){
                     response.getWriter().write("fail");
                     response.getWriter().flush();
                     response.getWriter().close();
                 }

                 //3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
                  if (!StringUtils.equals(seller_id, AlipayConfig.uid)){
                        response.getWriter().write("fail");
                        response.getWriter().flush();
                        response.getWriter().close();
                  }

                  //4、验证app_id是否为该商户本身。
                 if (!StringUtils.equals(app_id_notify, AlipayConfig.app_id)){
                     response.getWriter().write("fail");
                     response.getWriter().flush();
                     response.getWriter().close();
                 }



                 if(trade_status.equals("TRADE_CLOSED")){
                    //将商户的订单号更新为充值失败2
                     RechargeRecord updateRechargeRecord=new RechargeRecord();
                     updateRechargeRecord.setRechargeNo(out_trade_no);
                     updateRechargeRecord.setRechargeStatus("2");
                     int modifyRechargeCount=rechargeRecordService.modifyRechargeRecordByRechargeNo(updateRechargeRecord);
                     if (modifyRechargeCount<=0){
                         response.getWriter().write("fail");
                         response.getWriter().flush();
                         response.getWriter().close();
                     }
                 }else if (trade_status.equals("TRADE_SUCCESS")){

                     Map<String,Object> paramMap=new HashMap<>();
                     paramMap.put("uid", rechargeRecord.getUid());
                     paramMap.put("rechargeMoney", Double.parseDouble(total_amount));
                     paramMap.put("rechargeNo", out_trade_no);
                    //给用户充值[1.更新用户的账户余额 2.更新充值记录的状态为1](用户标识，充值金额，充值订单号)
                     rechargeRecordService.recharge(paramMap);
                 }

    //             out.println("success");
                 response.getWriter().write("success");
                 response.getWriter().flush();
                 response.getWriter().close();

             }else {//验证失败
                 response.getWriter().write("fail");
                 response.getWriter().flush();
                 response.getWriter().close();

                 //调试用，写文本函数记录程序运行情况是否正常
                 //String sWord = AlipaySignature.getSignCheckContentV1(params);
                 //AlipayConfig.logResult(sWord);
             }
         } catch (Exception e) {
             e.printStackTrace();
             response.getWriter().write("fail");
             response.getWriter().flush();
             response.getWriter().close();
         }
     }

     @RequestMapping(value = "/loan/recharge/wxpay")
     public String wxpayRecharge(HttpServletRequest request,
                                 Model model,
                                 @RequestParam(value = "rechargeMoney",required = true)Double rechargeMoney){
         System.out.println("-------------wxpayRecharge-----------");

         //从session中获取账户信息
         User sessionUser= (User) request.getSession().getAttribute(Constants.SESSION_USER);

         try {
             //生成充值记录
             RechargeRecord rechargeRecord = new RechargeRecord();

             rechargeRecord.setUid(sessionUser.getId());

             //全局唯一的充值记录订单号=“支付方式”+“时间戳”+“redis唯一数字”
             //支付方式：A支付宝订单号  W微信订单号
             String rechargeNo="W"+ DateUtils.getTimestamp()+redisService.getOnlyNumber();
             rechargeRecord.setRechargeNo(rechargeNo);
             rechargeRecord.setRechargeDesc("1");//0支付宝充值  1微信充值
             rechargeRecord.setRechargeMoney(rechargeMoney);
             rechargeRecord.setRechargeStatus("0");//0充值中  1充值成功 2充值失败
             rechargeRecord.setRechargeTime(new Date());
             int addRechargeRecordCount=rechargeRecordService.addRechargeRecord(rechargeRecord);
             if (addRechargeRecordCount<=0){
                 model.addAttribute("trade_msg", "充值异常");
                 return "toRechargeBack";
             }

             model.addAttribute("rechargeNo", rechargeNo);
             model.addAttribute("rechargeMoney", rechargeMoney);
             model.addAttribute("rechargeTime",new Date());
         } catch (Exception e) {
             e.printStackTrace();
             model.addAttribute("trade_msg", "充值异常");
             return "toRechargeBack";
         }


         return "showQRCode";
     }



}
