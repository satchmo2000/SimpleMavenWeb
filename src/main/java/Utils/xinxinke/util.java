package Utils.xinxinke;

import Services.Result;
import Utils.xinxinke.Entityes.info;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {
    /// <summary>
    /// 判断是否是32位的数字+小写字母组成的guid.
    /// </summary>
    /// <param name="str"></param>
    /// <returns></returns>
    public static boolean isGuid(String str){
        if(str.isEmpty())
            return false;

        Pattern pattern = Pattern.compile("^[a-z0-9]{32}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
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


    // 转码
    public static String encode(String input) throws Exception {
        return URLEncoder.encode(input, "UTF-8");
    }

    /// <summary>
    /// 通过正则表达式截取字符串
    /// </summary>
    /// <param name="input">输入字符串</param>
    /// <param name="regexStr">正则</param>
    /// <returns>返回字符串list</returns>
    public static List<String> getStringsByRegex(String input, String regexStr){
        List<String> list = new ArrayList<String>();

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            list.add(matcher.group());
        }

        return list;
    }

    /// <summary>
    /// 是否满足正则
    /// </summary>
    /// <param name="input"></param>
    /// <param name="regexStr"></param>
    /// <returns></returns>
    public static boolean isMatchByRegex(String input, String regexStr){
        if (input.isEmpty() || regexStr.isEmpty())
            return false;

        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    // 发起 POST 请求
    public static String post(String url, String data) throws Exception {

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

    public static info getInfo(String jsonString, Result outValue){
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, new TypeToken<info>() {
            }.getType());
        }catch(Exception e){
            e.printStackTrace();
            outValue.setStrValue(e.toString());
            return null;
        }
    }

}
