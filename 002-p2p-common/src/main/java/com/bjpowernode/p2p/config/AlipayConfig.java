package com.bjpowernode.p2p.config;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 张新宇
 * 2020/8/28
 */
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


    //商户支付宝用户号UID
    public static String uid = "2088621956680333";

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000118634206";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCk8wHPbNmxgcYauESVCVMLZ9NjDUgWxTuWXLZeJS0mmgnSvqr0QUOaRRj+ZL2DNrHtk3VC5FKr98PpaAhf06sp8lrJlvhM8yHJWvX/D8UrmAQ8Kd0cB/KdTZlJ2AmpAr/tivH2aNNan4cSsMvkeULdQOpQdGBPHB8N4jzIONBwfA4IKrqKYfj6RxHKxWfl4Ttz5xZXztZCuAsNvMo5mDNDXLb+dq2Snzfu85NCvwnTHEZ7pBf53QP6mvIXztodUHXN8GBo4gz7OYBCR0XQ6HFiwnkyF5e4TpnvfuM8exWhztZLxZKQ+3cDwyxSdOVKpZ+iFXCMAP8pLO6youmAZoOHAgMBAAECggEATz054mZdEavQ+icJMztefH0tDElmFyI6pNn29bUQQpn/HBFiWKI9ko+dGSBV9gHBaerLiw3aNSh6IddLJpWthSIawPrYX0bxNSSQRi5SPsXxSuGliYC76RtNBy9UPK/IeZmk8QWces1l+vGZkTE7kMRNi8bMOVS+ClJjdp/sagw2dRdZmYJiFwHg9wAwho5OMOxXYNkmGAjtIKq+6rBrcuWZixe3J3zpjG0Tbu82Lh44GsSA46FFbtoxl0O6JLEWWi7To0fkh7QDHSjLeXEnvCQxV4UXfZvGSXRSxXYavJI09eSqHwsZLtxIiTpqlelIGtQCoB5fLD7ZWadSDDm5QQKBgQDv+TPmpGT2sRPy59a1EI8avJHneIZZDg7cG9CL1/z/zCv6MvBYhS5DHeLZedSvypsaURykViKNSlAggY4yo5ukcCRV/dwakyoIZ8AKVJPiiayvney1pqUglqBzldh0CPK+GJMII2/5btfYYXVJ63NmiO6F9iANoQVVepMaagvzJwKBgQCv9x/istmqRM8a4PBgoqOr15G+PCGKT95HqNji4i9WVqXymDzx0Mnt83bO4KVKf+cEIq9+To0KkJU7wSjCyx2hgMCZ75fCnRKTaubJf4dpAI4tNOnALgvloXoiQh3DVCRYeDQLf+MxlhQHwzYKMyZNZKzeb6czQscTA8WF7MeooQKBgGURngxb3LIRroc1obYsvTjThtfoaR0E0LlfdZwQYdwST8tWvOdrYCqgYbSanTtUp7KTyu5j4LWFQO4P4qXtRlYhbQxSmOsQZDmaCMXRDV/yACsO5m0zPITXK8jCqaFlS2dIzlQ1KKnzRzE5Cr8RTU84CM2e9a0HzklMELylQVctAoGAeUh4prUlR7GDmgAeTkzrSL8dSiSB7aYdDAE59C939ydj2/DIWNfSxnp/khnOc6nynawWj5Uuq3B6G6SIJrL+6lkm5zL3xAClVAxjo9CNkXb7h/cngfl647xHxSMTBpWRM5UuMhrkikHp7mvDbIZSyRcx+2KzuKY3NkjsNOG9v+ECgYA5NhcsOpqhMgHsoySZ1xuFvU7+M9B0sw9W9jMQKJp16wTFAplQ8WFqNYEtEqAjFrEjfzxxPbUgRt4vQapp07GdGDFDf05oq/lT5HaTOO5Bt/qyovxzW8Z+BsQBN12mzkqzO8n07/IAkbpZedb5iqega9InWYaMaz9KXl8IrNzBqA==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqRODcrtYcsi0QTdJqj3Dtl7c+3t3xewUV6EHn4wG+Jmy28/q9vgIpW27NS61V1tXmXuxLLElwa1Kf4sWGtGuISVb9iExnGgSp0apMtZ6gQH7W6EVfKMd0fNMccrDK32aA6agZ7lyJpmja8tB79egMbHyzMcakJ/41KDFr9NaHRNOfXQgwmMJv7hlHW9RQYGqrw2Url+JNqQryKi6b3fwkRFjniE2IypDzxxhNr3obPr/9XJWlslevENJ4+Kbakap6MRCK7aaerZuDMaSrAcvA1Hq1TzTq1Y1WtuSFKozY9UHdQdcf4709ISnTmiVNEca+XN7DQkTbjgrVEN60uagfwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String notify_url = "http://zxy520.natapp1.cc/p2p/loan/alipay/notify";
    public static String notify_url = "http://localhost:8080/p2p/loan/alipay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/p2p/loan/alipay/return";
//    public static String return_url = "http://zxy520.natapp1.cc/p2p/loan/alipay/return";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
