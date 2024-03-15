package controller;

import Models.JsonObjectEx;
import Models.SessionManager;
import Services.BaseView;
import Services.ClientServicesX;
import Services.Result;
import Utils.DesEncrypt;
import Utils.RSAEncrypt;
import Utils.ServerLoadInfo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/Tools")
public class ToolsController {
    private final int nPageSize = 20;

    @RequestMapping(value="/Index", method = RequestMethod.GET)
    public String Index(String text, String password, HttpServletRequest request, Model model){
        SessionManager sm = new SessionManager(request);
        ClientServicesX client = new ClientServicesX(sm.getTokenId());
        Result outValue = new Result();
        ClientServicesX.enumErrorCode enumRet = client.IsTimeOut(outValue);
        if (enumRet == ClientServicesX.enumErrorCode.enumSuccess && !outValue.getBoolValue()){

            String strConn = IsNull(text, "");

            if (strConn.length() > 0){
                String strTextOut = "";
                try{
                    strTextOut = DesEncrypt.Encrypt(text, password , 0);
                }catch(Exception e){
                    strTextOut = DesEncrypt.Encrypt(text , password, 1);
                }

                model.addAttribute("conn", strConn);
                model.addAttribute("encode", strTextOut);
            }
            model.addAttribute("tokenid", sm.getTokenId());
            model.addAttribute("logindate", sm.getLoginDate());

            model.addAttribute("cpu", ServerLoadInfo.getCpuLoad());
            model.addAttribute("memory", ServerLoadInfo.getMemoryLoad());
            model.addAttribute("disk", ServerLoadInfo.getDiskLoad());

            return new BaseView(request).ReturnView();
        }
        return (String) new BaseView(request, model).ReturnLoginWithAlert(enumRet);
    }

    //<editor-folder desc="Operator">

    @RequestMapping(value="/RSAPassword", method = RequestMethod.GET)
    @ResponseBody
    public Object RSAPassword(HttpServletRequest request, Model model) throws NoSuchAlgorithmException {
        RSAEncrypt rsa = new RSAEncrypt();
        rsa.genKeyPair();
        String strPubKey = rsa.getPubKey();
        String strPriKey = rsa.getPriKey();
        JSONObject json = new JSONObject();
        json.put("code", true);
        json.put("prikey", strPriKey);
        json.put("pubkey", strPubKey);
        return json;
    }

    @RequestMapping(value="/DefineJson", method = RequestMethod.GET)
    @ResponseBody
    public Object DefineJson(String text, HttpServletRequest request, Model model){
        JsonObjectEx json = new JsonObjectEx();
        String strProcedure = IsNull(text, "");

        if (strProcedure.length() > 0){
            SessionManager sm = new SessionManager(request);
            ClientServicesX client = new ClientServicesX(sm.getTokenId());

            Result outValue = new Result();
            boolean bRet = client.GetProcDefine(strProcedure, true, outValue);

            if (bRet){
                json.putAll(true, "读取成功！", outValue.getStrValue());
            }else{
                json.putAll(false, "读取失败，检查存在过程中是否有更新语句！返回值：" + outValue.toErrorCode().toString());
            }
        }
        else {
            json.putAll(false, "请提供存储过程后再尝试！");
        }
        return json;
    }

    @RequestMapping(value="/EncodeText", method = RequestMethod.GET)
    @ResponseBody
    public Object EncodeText(String text, String password, HttpServletRequest request, Model model){
        JsonObjectEx json = new JsonObjectEx();
        String strText = IsNull(text, "");

        if (strText.length() > 0){
            String strTextOut = "";
            try{
                strTextOut = DesEncrypt.Encrypt(text, password , 0);
            }catch(Exception e){
                strTextOut = DesEncrypt.Encrypt(text , password, 1);
            }

            System.out.println(strTextOut);

            json.putAll(true, strTextOut);
        }
        else{
            json.putAll(true, "加密失败，请检查程序！");
        }
        return json;
    }

    private int HttpRequest(String strUrl, String formData, Result outValue){
        try {

            // 创建一个URL对象
            URL url = new URL(strUrl);
            System.out.println("set url");

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("create connection");

            // 设置HTTP请求方法为GET
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            System.out.println("set post");

            // 设置请求头为表单数据格式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 在.Net环境下正常，但在Java下产生403错误，需要添加以下User-Agent属性值
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            //connection.setRequestProperty("Referer", "https://example.com");

            System.out.println("set form");
            System.out.println(formData);

            // 将表单数据写入请求体
            try (OutputStream os = connection.getOutputStream()) {
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(formData);
                writer.flush();
            }
            System.out.println("write formData");

            // 获取响应代码
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 读取响应内容
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // 打印响应内容
                    System.out.println("Response Content: ");
                    System.out.println(response.toString());

                    String strError = response.toString();
                    outValue.setStrValue(strError);
                }
            } else {
                String strError = "HTTP request failed with response code: " + responseCode;
                System.out.println(strError);
                outValue.setStrValue(strError);
            }

            // 关闭连接
            connection.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK ? 0 : -1;
        } catch (Exception e) {
            e.printStackTrace();
            outValue.setStrValue(e.getMessage());
            return -1;
        }
    }

    @RequestMapping(value="/GetNameSigner", method = RequestMethod.GET)
    @ResponseBody
    public Object GetNameSigner(String id, String zhenbi, Integer id1, Integer id2, String id3, String id5, HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        JsonObjectEx json = new JsonObjectEx();
        String strText = IsNull(zhenbi, "");

        String strUrl = "http://www.yishuzi.com/make.php?file=a&page=14";
        String strParam = String.format("id=%s&zhenbi=%s&id1=%s&id2=%d&id3=%s&id5=%s",
                URLEncoder.encode(id, "UTF-8"),
                URLEncoder.encode(strText, "UTF-8"),
                id1, id2, id3, id5);

        System.out.println(strParam);

        Result resultError = new Result();
        int nRet = HttpRequest(strUrl, strParam, resultError);

        if(nRet == 0){
            String strResult = resultError.getStrValue();
            int nPos = strResult.indexOf('<');
            if(nPos > 0){
                strResult = strResult.substring(nPos);
            }
            json.putAll(true, strResult, id);
        }
        else{
            json.putAll(false, resultError.getStrValue(), id);
        }
        return json;
    }

    @RequestMapping(value="/GetCurrentServerLoad", method = RequestMethod.GET)
    @ResponseBody
    public Object GetCurrentServerLoad(HttpServletRequest request){
        JsonObjectEx json = new JsonObjectEx();
        try {
            // 获取服务器负载
            double cpu = ServerLoadInfo.getCpuLoad();
            double memory = ServerLoadInfo.getMemoryLoad();
            double disk = ServerLoadInfo.getDiskLoad();
            json.putAll(true, "获取负载信息成功！");
            json.put("cpu", cpu);
            json.put("memory", memory);
            json.put("disk", disk);
        } catch (Exception e) {
            e.printStackTrace();
            json.putAll(false, e.getMessage());
        }
        return json;
    }
    //</editor-folder>

    @RequestMapping(value="/ServerLoadMonitor", method = RequestMethod.GET)
    public String ServerLoadMonitor(HttpServletRequest request, Model model){
        SessionManager sm = new SessionManager(request);
        ClientServicesX client = new ClientServicesX(sm.getTokenId());
        Result outValue = new Result();
        ClientServicesX.enumErrorCode enumRet = client.IsTimeOut(outValue);
        if(enumRet == ClientServicesX.enumErrorCode.enumSuccess && !outValue.getBoolValue()){
            return new BaseView(request).ReturnView();
        }
        return (String) new BaseView(request, model).ReturnLoginWithAlert(enumRet);
    }

    private void ResponseImage(BufferedImage image, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setDateHeader("Expires", 0L);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");

            response.setContentType("image/png");
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);

            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/IncludeFile")
    public String IncludeFile(HttpServletRequest request){
        return new BaseView(request).ReturnView();
    }

    public<T> T IsNull(T t, T v){
        return t == null ? v : t;
    }

}
