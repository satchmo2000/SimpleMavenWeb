package Utils.xinxinke;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 *
 * 接口测试
 *
 * 以下代码只是为了方便开发者测试而提供的样例代码，开发者可以根据自己的业务需要，按照技术文档编写，并非一定要使用该代码。
 * 该代码仅供学习和研究信信客接口使用，只是提供一个参考。
 *
 * @author chenfan
 * @version 1.0, 2015/10/07
 *
 */
public class HTTPTest {
    // 开发者参数
    public String devId = "c153cca0d4024399819e4b1df7a12dcc";
    public String devKey = "d0ae391e48d24b5b936c29d334c67398";

    // 转码
    public String encode(String input) throws Exception {
        return URLEncoder.encode(input, "UTF-8");
    }

    // 计算 MD5 值
    public static String md5(String input) throws Exception {
        byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            sb.append(String.format("%02x", Integer.valueOf(digest[i] & 0xFF)));
        }
        return sb.toString();
    }

    // 发起 POST 请求
    public String post(String url, String data) throws Exception {

        // 打开连接
        HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.connect();

        // 输出参数
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(data);
        dos.flush();
        dos.close();

        // 读取响应
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line = br.readLine();
        br.close();

        // 关闭连接
        conn.disconnect();

        return line;

    }

    // 短信发送接口
    public void send() throws Exception {

        // 参数
        String url = "http://www.xinxinke.com/api/send";
        String smsTemplateCode = "T_CODE_20160629";
        String smsParam = "{\"code\":\"123456\"}";
        String recNum = "13800138000";
        String sign = md5(devId + devKey + recNum);

        // 拼接
        StringBuffer data = new StringBuffer();
        data.append("").append("dev_id").append("=").append(encode(devId));
        data.append("&").append("sign").append("=").append(encode(sign));
        data.append("&").append("sms_template_code").append("=").append(encode(smsTemplateCode));
        data.append("&").append("sms_param").append("=").append(encode(smsParam));
        data.append("&").append("rec_num").append("=").append(encode(recNum));
        System.out.println(url + ";" + data.toString());

        // 请求
        System.out.println(post(url, data.toString()));

    }

    // 短信发送状态报告接口
    public void report() throws Exception {

        // 参数
        String url = "http://www.xinxinke.com/api/report";
        String sign = md5(devId + devKey);

        // 拼接
        StringBuffer data = new StringBuffer();
        data.append("").append("dev_id").append("=").append(encode(devId));
        data.append("&").append("sign").append("=").append(encode(sign));

        // 请求
        System.out.println(post(url, data.toString()));

    }

    // 短信接收接口
    public void receive() throws Exception {

        // 参数
        String url = "http://www.xinxinke.com/api/receive";
        String sign = md5(devId + devKey);

        // 拼接
        StringBuffer data = new StringBuffer();
        data.append("").append("dev_id").append("=").append(encode(devId));
        data.append("&").append("sign").append("=").append(encode(sign));

        // 请求
        System.out.println(post(url, data.toString()));

    }

    // 短信模板接口
    public void template() throws Exception {

        // 参数
        String url = "http://www.xinxinke.com/api/template";
        String sign = md5(devId + devKey);
        String action = "query";

        // 拼接
        StringBuffer data = new StringBuffer();
        data.append("").append("dev_id").append("=").append(encode(devId));
        data.append("&").append("sign").append("=").append(encode(sign));
        data.append("&").append("action").append("=").append(encode(action));

        // 请求
        System.out.println(post(url, data.toString()));

    }

    // 帐户信息查询接口
    public void account() throws Exception {

        // 参数
        String url = "http://www.xinxinke.com/api/account";
        String sign = md5(devId + devKey);

        // 拼接
        StringBuffer data = new StringBuffer();
        data.append("").append("dev_id").append("=").append(encode(devId));
        data.append("&").append("sign").append("=").append(encode(sign));

        // 请求
        System.out.println(post(url, data.toString()));

    }

    public static void main(String[] args) throws Exception {
        HTTPTest t = new HTTPTest();
        t.send();
        // t.report();
        // t.receive();
        // t.template();
        // t.account();
    }
}