package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000118609550";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCHIKDOUrjE6wHWFvBO0dKOVxTqlWCO6OKFvmb8l3SZTDObPVua4nPdObnKZcGtp/eNJ9vF+rCnqy0b495Fw6MjeqBE4nYH8QINXoWFFvvo8+WiYD/oQ4gz+vxwvnOneXmPKjQhYM6TOnFK7rl7Tw6+1/wpGUw6Xb+Oc79r/6H/+NHVdtbeT3iUTMqowfHxsR6IK3oDu6EyDA5ruYeCRRnr0SFIyFaUJKFiouLSLD8l8Jf5fl1jICMJlRIYlG6PRy+R7mUjlplg9SiJZuZQcLGqRDlDhGHYBH9I/3blrHdsVpGc7Ng+CTV7JwLennhd1llSkfzokCU9yjE9a5ugUj53AgMBAAECggEAfxHaxksZ/+uzTV1C3AwKQdp4C+Uu6RTtN7IGbNAMUMBM41Y+15bOOCI5qbApAqGqdG4gCLwm9XVONSUCd35y4kFFh6GiNvOX53SUgMRNXhJFRn3WexdOdPs3+tnOLpZYDp40aZL8j4zszvdRKzj799FqCkgE2/oe0dndD7VT5T31EOENg6S3qIfSiuhJVdWvU4W2x3QVDNUc/4MWVZwpC3sKQXt0mM94XIxgKmFmh6abXfuY1BshYxE0thjPyVgOC5x0IY9VZi6eHQFnipAeQsC6APlru0XdZKQjjuLXCRuBLiEZAoRO+exRzO1uE7X0iUrqsc4+4KupLFcTAvkPAQKBgQDkFNzavN+koballXc1hGMcVwaxC0m3NIv5rDf0XvyJF0fUIBzWBQrUEVNkpkW7H/1CO24EW8pMIyWRLbwmvw/XfBh6cUgbjg4DeBMJW6jjZwpLN/GF3eFWSYxa0P1tH4gNquinCj0gkWBNXgLS3I5P3+6zLsDoQC7wk8digtTggQKBgQCXqvex6hKlihGKOUJaPKIuqrLiWIzHfyernm2qclFD8of9Z/wVSKE59EXXJQcOwj3X2RwnZjO1hMeCP99uCnqyoQWkUj76FdSODwySYa0UkES9Qk/VVyg0UKoNZp4XutH4YnHCPKkHdTEjDUY51eu/uktxDUULOSeEgQPzwNyi9wKBgEJfz+XYET1X0k9dr9NjM3SL3uam4kzufc2q0J3NBrDelYilz754uS393MbtnF1YcdtUcC/Uhoqhr80QAjFEaNfMxVLcpJ9gN7PKvb97bSrAUb6SCTdr2qfBv5auOxIGmxFfK1VxJVjU9bGOKkQy6zdTc6Oxd3IfO4nHd76zVEABAoGBAJQS5yFcVjgoRFee75kJ+Y12Oo+hxFBBZIm5c3glhBHCh+Jv0z00W7QpOMaD8BAItDg4T7Rno7RkniQcGBdnQ8OCUe/MHJAtIAhRyQRhOIoLEBF23Fp9bVMmLeBHf7HLQ5QtCMOKVbxiPkbqftWYdUIKr/z/55i9hEdpbmJcuQyhAoGBAMOQIXcSMvADRPh/nZytIptBeYU2Gu3HhWSeItD0NPDR5ZJcYmbAUVPnOBIOsSh+I1BHCQB/3kN/ilJH4mjynMxqJkJq2GcPlnygEYKRf+pdQC9o7AuTcnfffPkWQadBhXAt9YJA2Khk7NW3rY1fiKSTARLGOPkf1DK5Vpg519vf";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqckg6GQRlVyjB0hqMnViB8NkpTdlJmXu+y6crRZlq5i+Xb1sH/N7+hYQCljNdRaLXpiU+w7CENDg/1SGo2aMHojRYvdxWbtgcPwQHlURBG7Y3aa1/7/krWEXlKNgoi8KqLUn6AKkrb1lWNcP/6JDz51sinKBc53jcgPzJcaNwNc9pAgALR4SVbEsQyG8nkLNWyPQmz6w6vP9Xa3Fph843HkSt/hHoc1XDiOxh1x+BdrttI2BWDlQ7ilTs1mv1DibLIbxj7jFbFarJUG9FY5QPzInmjVo8cgi6xrBPjIyl68C24UTCz+AbR22TOZXBFiFsB9/c3R7qY8DJCaN42PIHwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/lcb/returnPayStatus";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    //沙盒网关：https://openapi.alipaydev.com/gateway.do
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

