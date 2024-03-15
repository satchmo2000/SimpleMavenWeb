package Utils.xinxinke;

import Services.Result;
import Utils.DateTime;
import Utils.xinxinke.Entityes.info;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/// <summary>
/// 接口来源平台地址：http://www.xinxinke.com/
/// 机制：向接口发送“模板+参数”进行短信发送
/// 平台管理账号：用户名satchmo2000，密码262530
/// </summary>
public class xxkMsgSend {
    //开发者参数（调用时使用自己的参数即可）
    private String strDevId;    //=c153cca0d4024399819e4b1df7a12dcc
    private String strDevKey;   //d0ae391e48d24b5b936c29d334c67398
    private boolean bGet = false;

    /// <summary>
    /// 采用指定模板及参数格式
    /// </summary>
    //private const string strTemplateCode = "CHECK";
    //private const string strSmsParam = "{\"code\":\"{0}\"}";

    public xxkMsgSend(String DevId, String DevKey){
        strDevId = DevId;
        strDevKey = DevKey;
    }

    //<editor-folder desc="业务接口">

    /// <summary>
    /// 灌注申请
    /// </summary>
    /// <param name="strRecNum">接收短信的手机号码</param>
    /// <param name="strPoleName">模板中的自定义参数</param>
    /// <param name="strGroupName">模板中的自定义参数</param>
    /// <param name="strTacheName">模板中的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误码</param>
    /// <returns></returns>
    public info SendBetonTaskPublish(String strRecNum, String strPoleName, String strGroupName, double dNum, String strExtNum, Result outValue){

        try{
            PublishBetonTaskItem pbti = new PublishBetonTaskItem(strPoleName, strGroupName, dNum);

            return SendEx("PublishBetonTask", pbti.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 施工环节任务发布
    /// </summary>
    /// <param name="strRecNum">接收短信的手机号码</param>
    /// <param name="strPoleName">模板中的自定义参数</param>
    /// <param name="strGroupName">模板中的自定义参数</param>
    /// <param name="strTacheName">模板中的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误码</param>
    /// <returns></returns>
    public info SendTacheTaskPublish(String strRecNum, String strPoleName, String strGroupName, String strTacheName, String strExtNum, Result outValue){
        try{
            PublishTacheTaskItem ptti = new PublishTacheTaskItem(strPoleName, strGroupName, strTacheName);

            return SendEx("PublishTacheTask", ptti.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 任务发布
    /// </summary>
    /// <param name="strRecNum">接收短信的手机号码</param>
    /// <param name="strUserName">模板中的自定义参数</param>
    /// <param name="strProject">模板中的自定义参数</param>
    /// <param name="strParent">模板中的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误码</param>
    /// <returns></returns>
    public info SendPublish(String strRecNum, String strUserName, String strProject, String strParent, String strExtNum, Result outValue){
        try{
            PublishTaskItem pti = new PublishTaskItem(strUserName, strProject, strParent);

            return SendEx("publishtask", strProject.length() > 0 ? pti.toString() : "", strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    public info SendSubmit(String strRecNum, String strUserName, String strProject, String strParent, String strExtNum, Result outValue){
        try{
            SubmitTaskItem sti = new SubmitTaskItem(strUserName, strProject, strParent);

            return SendEx("submittask", strProject.length() > 0 ? sti.toString() : "", strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 施工环节任务催促
    /// </summary>
    /// <param name="strRecNum">接收短信的手机号码</param>
    /// <param name="strPoleName">模板中的自定义参数</param>
    /// <param name="strGroupName">模板中的自定义参数</param>
    /// <param name="strTacheName">模板中的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误码</param>
    /// <returns></returns>
    public info SendTacheTaskHurry(String strRecNum, String strPoleName, String strGroupName, String strTacheName, String strExtNum, Result outValue){
        try{
            HurryTaskItem hti = new HurryTaskItem(strPoleName, strGroupName, strTacheName);

            return SendEx("HurryTask", hti.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 施工环节任务相关桩号导航
    /// </summary>
    /// <param name="strRecNum">接收短信的手机号码</param>
    /// <param name="strPoleName">模板中的自定义参数</param>
    /// <param name="strGroupName">模板中的自定义参数</param>
    /// <param name="strTacheName">模板中的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误码</param>
    /// <returns></returns>
    public info SendTacheTaskNavigate(String strRecNum, String strPoleName, String strGroupName, String strTacheName, int tkid, int poleid, String strExtNum, Result outValue){
        try{
            NavigateTaskItem nti = new NavigateTaskItem(strPoleName, strGroupName, strTacheName, tkid, poleid);

            return SendEx("NavigateTacheTask", nti.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 发送验证码
    /// </summary>
    /// <param name="strRecNum">接收短信的号码</param>
    /// <param name="strCode">模板中需要的自定义参数</param>
    /// <param name="strExtNum">暂不需要</param>
    /// <param name="strError">返回错误信息</param>
    /// <returns></returns>
    public info SendCheckCode(String strRecNum, String strCode, String strExtNum, Result outValue){
        try{
            CheckCodeItem cci = new CheckCodeItem(strCode);
            return SendEx("CHECK", strCode.length() > 0 ? cci.toString() : "", strRecNum, strExtNum, true, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    public info SendResetPassword(String strRecNum, String strCode, String strExtNum, Result outValue){
        try{
            ResetPasswordItem rpi = new ResetPasswordItem(strCode);

            return SendEx("resetpassword", rpi.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    public info SendWarningMessage(String strRecNum, String strReceiver, String strContent, String strExtNum, Result outValue){
        try{
            WarningMessageItem wmi = new WarningMessageItem(strReceiver, strContent);

            return SendEx("WARNING", strReceiver.length() > 0 ? wmi.toString() : "", strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 传感器数据连续性检测
    /// </summary>
    /// <param name="strRecNum"></param>
    /// <param name="strContent">发送内容</param>
    /// <param name="strProject">项目</param>
    /// <param name="strExtNum"></param>
    /// <param name="strError"></param>
    /// <returns></returns>
    public info SendCheckMessage(String strRecNum, String strContent, String strProject, String strExtNum, Result outValue){
        try{
            CheckMessageItem cmi = new CheckMessageItem(strContent, strProject);

            return SendEx("sensorcheck", cmi.toString(), strRecNum, strExtNum, false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }

    /// <summary>
    /// 广播消息
    /// </summary>
    /// <param name="strRecNum"></param>
    /// <param name="strContent">发送内容</param>
    /// <param name="strExtNum"></param>
    /// <param name="strError"></param>
    /// <returns></returns>
    public info SendBroadCastMessage(String strRecNum, String strContent, Result outValue){
        try{
            BroadCastItem bci = new BroadCastItem(strContent);
            return SendEx("broadcast", bci.toString(), strRecNum, "", false, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }
    }
    
    //</editor-folder>

    //<editor-folder desc="标准发送接口">

    private info SendCommon(String strRecNum, String strTemplateName, String strParamTemplate, String[] Params, Result outValue){
        try{
            Integer i = 0;
            for (String strParam : Params){
                String strSection = "{" + i.toString() + "}";
                strParamTemplate = strParamTemplate.replace(strSection, strParam.trim());
                i++;
            }
            return SendEx(strTemplateName, strParamTemplate, strRecNum, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.getMessage());
            return null;
        }

    }

    public info SendEx(String strTemplate, String strParam, String strRecNum, Result outValue){
        return SendEx(strTemplate, strParam, strRecNum, "", false, outValue);
    }

    public info SendEx(String strTemplate, String strParam, String strRecNum, String strExtNum,boolean bTest , Result outValue){
        try{

            // 参数
            String url = "http://www.xinxinke.com/api/send";
            String sign = util.md5(strDevId + strDevKey + strRecNum);

            // 拼接
            StringBuffer data = new StringBuffer();
            data.append("").append("dev_id").append("=").append(util.encode(strDevId));
            data.append("&").append("sign").append("=").append(util.encode(sign));
            data.append("&").append("sms_template_code").append("=").append(util.encode(strTemplate));
            data.append("&").append("sms_param").append("=").append(util.encode(strParam));
            data.append("&").append("rec_num").append("=").append(util.encode(strRecNum));
            System.out.println(url + ";" + data.toString());

            String strRet = "{\"data\":{\"message\":\"提交成功\",\"mobiles\":[{\"index\":\"146716694473117848\",\"mobile\":\"13651115380\"}]},\"code\":\"25010\"}";
            if(!bTest) {
                // 请求
                strRet = post(url, data.toString());
            }

            return util.getInfo(strRet, outValue);
        }
        catch (Exception ex){
            outValue.setStrValue(ex.toString());
            return null;
        }
    }

    //</editor-folder>

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

    //<editor-folder desc="模板定义"

    public class TemplateContentItem{
        @Override
        public String toString(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }
    
    public class PublishBetonTaskItem extends TemplateContentItem{
        //template=PublishBetonTask
        public String pole;
        public String group;
        public double num;
        public PublishBetonTaskItem(String strPole, String strGroup, double dNum){
            pole = strPole.trim();
            group = strGroup.trim();
            num = dNum;
        }
    }

    public class PublishTacheTaskItem extends TemplateContentItem{
        //template=PublishTacheTask
        public String pole;
        public String group;
        public String tache;
        public PublishTacheTaskItem(String strPole, String strGroup, String strTache){
            pole = strPole;
            group = strGroup;
            tache = strTache;
        }
    }

    public class PublishTaskItem extends TemplateContentItem{
        //template=publishtask
        public String user;
        public String project;
        public String parent;
        public PublishTaskItem(String strUser, String strProject, String strParent){
            user = strUser;
            project = strProject;
            parent = strParent;
        }
    }

    public class SubmitTaskItem extends TemplateContentItem{
        //template=submittask
        public String user;
        public String project;
        public String parent;
        public SubmitTaskItem(String strUser, String strProject, String strParent){
            user = strUser;
            project = strProject;
            parent = strParent;
        }
    }

    public class CheckCodeItem extends TemplateContentItem{
        //template=CHECK
        public String code;
        public CheckCodeItem(String strCode){
            code = strCode.trim();
        }
    }

    public class ResetPasswordItem extends TemplateContentItem{
        //resetpassword
        public String resetpassword;
        public ResetPasswordItem(String strResetPassword){
            resetpassword = strResetPassword;
        }
    }

    public class HurryTaskItem extends TemplateContentItem{
        //template=HurryTask
        public String pole;
        public String group;
        public String tache;
        public HurryTaskItem(String strPole, String strGroup, String strTache){
            pole = strPole.trim();
            group = strGroup.trim();
            tache = strTache.trim();
        }
    }

    public class NavigateTaskItem extends TemplateContentItem{
        //template=NavigateTacheTask
        public String pole;
        public String group;
        public String tache;
        public int tkid;
        public int poleid;
        public NavigateTaskItem(String strPole, String strGroup, String strTache, int nTKId, int nPoleId){
            pole = strPole.trim();
            group = strGroup.trim();
            tache = strTache.trim();
            tkid = nTKId;
            poleid = nPoleId;
        }
    }

    public class WarningMessageItem extends TemplateContentItem{
        //template=WARNING
        public String user;
        public String content;
        public WarningMessageItem(String strUser, String strContent){
            user = strUser.trim();
            content = strContent.trim();
        }
    }

    public class CheckMessageItem extends TemplateContentItem{
        //template=sensorcheck
        public String content;
        public String project;
        public CheckMessageItem(String strContent, String strProject){
            content = strContent.trim();
            project = strProject.trim();
        }
    }

    public class BroadCastItem extends TemplateContentItem{
        //template=broadcast
        public String content;
        public String date;
        public BroadCastItem(String strContent){
            content = strContent.trim();
            DateTime dtNow = new DateTime();
            date = dtNow.toDateString("yyyy-MM-dd");
        }
    }

    //</editor-folder>
}
