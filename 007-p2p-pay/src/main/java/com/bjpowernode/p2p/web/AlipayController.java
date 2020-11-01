package com.bjpowernode.p2p.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.bjpowernode.p2p.config.AlipayConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 张新宇
 * 2020/8/28
 */
@Controller
public class AlipayController {

    @RequestMapping("/api/alipay")
    public void alipay(HttpServletResponse response,
                       @RequestParam(value = "out_trade_no",required = true)String out_trade_no,
                       @RequestParam(value = "total_amount",required = true)Double total_amount,
                       @RequestParam(value = "subject",required = true)String subject) throws AlipayApiException, IOException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //设置同步返回的地址
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        //设置异步通知的地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //输出
        System.out.println("result=" + result);
        response.setContentType( "text/html;charset=UTF-8");
        response.getWriter().write(result); //直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
//        return "";
    }
}
