<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" autoFlush="false" buffer="100kb"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>${ProjectTitle}</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/boostrap/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/js/boostrap/bootstrap-responsive.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery-ui.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/ui-lightness/jquery-ui-1.8.21.custom.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/application.css">
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/signin.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/index.css" rel="stylesheet">

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/Theme.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/modernizr-2.5.3.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery-ui-1.8.21.custom.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery.ui.touch-punch.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/libs/jquery-ui-1.11.1.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/boostrap/bootstrap.min.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/commonscript.js"></script>

    <script type="text/javascript" src="https://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/md5.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/CryptoJS.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/jsencrypt.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/SignUtil.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/IndexedDB.js"></script>
</head>
<body>
    <div class="signin">
        <div class="signin-head" style="width:300px;">
            <img src="${pageContext.request.contextPath}/Images/Logo/${Logo}" alt="" class="img-circle" style="border-radius:0;width:360px;height:60px; margin-top:30px;margin-bottom:30px;">
        </div>
        <form class="form-signin" enctype_="multipart/form-data" method="post" role="form" action="${pageContext.request.contextPath}/Login">
            <input id="UserName" name="UserName" type="text" class="form-control" placeholder="用户名" required autofocus />
            <input id="Password" name="Password" type="password" class="form-control" placeholder="密码" required />
            <input id="RSAPasswod" name="RSAPassword" type="hidden" />
            <input id="Second" name="Second" type="hidden"  title="秒"/>
            <input id="Sign" name="Sign" type="hidden" title="Md5x(Right(Md5(Password),16) + DateTime + PubKey)" />
            <input id="Url" name="Url" type="hidden" value="${Url}" />
            <div class="slider-container">
                <div class="slider"></div>
            </div>
            <button name="passwordcheck" disabled class="loginBtn btn-lg btn-block" type="submit">登录</button>
            <label class="checkbox">
                <input type="checkbox" value="remember-me" id="showpwd">显示密码，
                <a href="javascript:void(0);" name="resetpassword">重置密码</a>
                <c:if test="${CompanyReg == 1}">
                <a href="${pageContext.request.contextPath}/Home/AppLogin" style="float:right;">APP报表</a>
                </c:if>
            </label>
        </form>
        <input id="PubKey" name="PubKey" type="hidden" value="${PubKey}" />
        <h3 style="text-align:center;font-size:24px;font-weight:500;margin-top:-10px;padding:0;">${ProjectTitle}</h3>
        <p style="text-align:center;margin-top:-10px;">${WebSite}</p>
    </div>
    <p class="red" title="SQL数据库连接检测！">${DBError}</p>
    <div id="DTipBox" class="DTipBox"></div>
    <div id="DTip" class="DTip">
        <img src="${pageContext.request.contextPath}/Images/Other/downloadbg.png" alt="" />
        <!--a class="CommonIcon" href="javascript:void(0);" onclick="javascript:CloseTip();" title="关闭" alt=""><img src="${pageContext.request.contextPath}/Images/CommonIcon/Delete.png"/></a-->
    </div>

    <div id="dialog_reset"></div>

    <div class="f_wechart11" title="微信扫码登录">
        <dl>
            <dt>
                <div id="wx-login-container"></div>
            </dt>
        </dl>
    </div>

    <!--f_wechart 微信浮动条-->
    <c:if test ="${Version1001 != null && ShowQRCode1001 == 1}">
    <div class="f_wechart1" title="中交钢构监测">
        <dl>
            <dd><span>支持浏览器自带扫描</span></dd>
            <dt>
                <a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/zjwork-v${Version1001}.apk">
                    <img src="${pageContext.request.contextPath}/ZJAPP?version=${Version1001}" />
                </a>
                <span>V${Version1001}</span>
            </dt>
            <dd><a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/zjwork-v${Version1001}.apk">（安卓）三维钢构</a></dd>
        </dl>
    </div>
    </c:if>
    <c:if test="${Version1002 != null && ShowQRCode1002 == 1}">
    <div class="f_wechart1" title="调度平台">
        <dl>
            <dt>
                <a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/ddwork-v${Version1002}.apk">
                    <img src="${pageContext.request.contextPath}/DDAPP?version=${Version1002}" />
                </a>
                <span>V${Version1002}</span>
            </dt>
            <dd><a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/ddwork-v${Version1002}.apk">调度平台</a></dd>
        </dl>
    </div>
    </c:if>

    <c:if test="${Version1 != null && ShowQRCode1 == 1}">
    <div class="f_wechart2" title="桩基施工管理">
        <dl>
            <dt>
                <a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/work-v${Version1}.apk">
                    <img src="${pageContext.request.contextPath}/POLEAPP?version=${Version1}" />
                </a>
                <span>V${Version1}</span>
            </dt>
            <dd><a name="nav" href="javascript:void(0);" data-url="${pageContext.request.contextPath}/update/work-v${Version1}.apk">施工平台</a></dd>
        </dl>
    </div>
    </c:if>

    <div id="WeiXinBox" class="f_wechart3" title="网站（微信）入口">
        <dl>
            <dt>
                <a href="${pageContext.request.contextPath}/index" target="_blank">
                    <img src="${pageContext.request.contextPath}/WebQRCode" />
                </a>
            </dt>
            <dd><a href="${pageContext.request.contextPath}/index" target="_blank">网站（微信）入口</a></dd>
        </dl>
    </div>
    <!--f_wechart 微信浮动条-->

    <div style="text-align:center;">
        <p>开发者:<a href="http://www.retainer.com.cn/" target="_blank">瑞腾通软</a></p>
    </div>
</body>
</html>

<script type="text/javascript">
    $(function(){
        baseload();
        //if (IsWeiXin()) {
        //    OpenTip();
        //}
        IndexedDB.CreateTable(onLoadStart);

        var appid = "wxc71e2f9bbede8cb3";
        /*
        //var appsecret = "fa6705e535622b8c9ced7ff570fefffc";
        //var accesstoken = $("#WxAccessToken").val();
        //var url = $("#WxUrl").val();
        //var ticket = $("WxTicket").val();
        var nonce = $("#WxNonce").val();
        var timestamp = $("#WxTimeStamp").val();
        var sign = $("#WxSign").val();

        // 初始化微信JS-SDK
        wx.config({
            // 配置参数，包括 appid、nonceStr、timestamp、signature 等
            debug: false, // 是否开启调试模式
            appId: appid, // 必填，公众号的唯一标识
            timestamp: timestamp, // 必填，生成签名的时间戳
            nonceStr: nonce, // 必填，生成签名的随机串
            signature: sign,// 必填，签名
            jsApiList: [
                'chooseImage',
                'getLocalImgData'
            ] // 需要调用的JS接口列表
        });

        // 调用微信登录接口
        wx.ready(function () {
            wx.login({
                success: function (res) {
                    if (res.code) {
                        // 获取到登录凭证，可以将其发送到服务器进行验证
                        var code = res.code;
                        var url = '/Home/WeChatReturnUrl?state=STATE&code=' & code;
                        alert(url);
                        window.open(url);

                        // 发送code到服务器端进行验证和登录
                        // 服务器端可以通过微信开放平台的接口，根据code获取用户的access_token和openid
                        // 然后根据openid判断用户是否存在，如果存在，则自动登录；如果不存在，则进行注册流程

                        // TODO: 发送code到服务器进行验证和登录

                        // 登录成功后，可以进行其他操作
                    } else {
                        console.log('登录失败：' + res.errMsg);
                    }
                },
                fail: function () {
                    console.log('调用微信登录接口失败！');
                }
            });
        });

        wx.error(function (res) {
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
            alert('wx.error=' + res.errMsg);
        });

        */

        // 使用微信SDK创建登录按钮
        var obj = new WxLogin({
            self_redirect: false,
            id: "wx-login-container",
            appid: appid,
            scope: "snsapi_login",
            redirect_uri: "http://www.help5g.com/Home/WeiXinLogin",
            state: "STATE",
            style: "black",
            href: ""
        });
    });

    function onLoadStart() {
        IndexedDB.updateParam(1, 0);
    }

    function baseload() {
        if (IsWeiXin()) {
            CloseWeiXin();
        }

        var msg = Request.QueryString["msg"];
        if (msg != undefined) {
            alert(decodeURI(msg));
        }
    }

    function IsWeiXin() {
        var ua = navigator.userAgent.toLowerCase(), re = false;
        if (ua.match(/MicroMessenger/i) == "micromessenger") { re = true; }
        return re;
    }

    function CloseWeiXin() {
        $("#WeiXinBox").hide();
    }

    function OpenTip() {
        $("#DTipBox").show();
        $("#DTip").show();
    }

    function CloseTip() {
        $("#DTipBox").hide();
        $("#DTip").hide();
    }

    $("#DTipBox").live('click', function () {
        CloseTip();
    });

    $("#DTip").live('click', function () {
        CloseTip();
    });

    $("[name=nav]").live('click', function () {
        if (IsWeiXin()) {
            OpenTip();
        }
        else {
            location.href = $(this).attr('data-url');
        }
    });

    $("#showpwd").live('click', function () {
        var showPwd = $("#Password");
        if($(this).filter(":checked").length){
            showPwd.prop('type', 'text');
        } else {
            showPwd.prop('type', 'password');
        };
    });

    let isMouseDown = false;
    let startX = 0;
    let scrollLeft = 0;
    let slider = $('.slider')[0];
    let sliderContainer = $('.slider-container')[0];

    $('.slider').on('mousedown', function(e){
        isMouseDown = true;
        startX = e.clientX;
    });

    $('.slider').on('mousemove', function(e){
        if (!isMouseDown) return;
        const x = e.clientX - startX;
        scrollLeft = Math.max(0, Math.min(x, sliderContainer.clientWidth - slider.clientWidth));
        slider.style.left = scrollLeft + 'px';
    });

    $('.slider').on('mouseup', function(e){
        isMouseDown = false;
        if (scrollLeft >= sliderContainer.clientWidth - slider.clientWidth - 5) {
            // 滑动成功，执行相应操作
            $('.loginBtn').addClass('btn-warning');
            $('.loginBtn').prop('disabled', false);
        } else {
            // 滑动失败，恢复初始状态
            slider.style.left = '0px';
        }
    });

    $('.slider-container').on('mouseleave', function(e){
        isMouseDown = false;
        slider.style.left = '0px';
    });

    $("[name=passwordcheck]").click(function(){
        var publicKey = $("#PubKey").val();
        var Password = $("#Password").val();
        if (Password.length == 0) {
            alert('登录密码不能为空！');
            return false;
        }
        if (publicKey.length > 0) {
            //publicKey='MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLMQvXfr+PAPJDGe0C+1DtiCySJHbsJ5cb6+90vKCnJVSWSZit4umJpwS+Sck9NuYmQp6dw5X/xaT35OhWuQJBV8e9ZIxg6Bc9XukMB7kz0u1jyBAnoRXkZ4QwwxO30R9oRBtRN4rwKCJMoBYhACvhe1X6dIqukmAN8fj4N1MXZQIDAQAB';
            //publicKey = 'uLXAZHRT7gMRH/dYun7GY7I1e3TlMhEFr87NS3cPf6Y2Fnh2C/qbTYMQP8cvahQy6Nlwh5BsfIEjSVP6tFV0L1tqd/RsnZxWfYxV7BJmux4XjU1hoA4sxrBfe4GNvKNuymiPlVjsGh3xOC20COfVv0S71msUgL6wxZlp2njJrfs=';

            var param = SignUtil.MakeSign(publicKey, Password);

            $("#Second").val(param.Second);
            $("#Sign").val(param.Sign);
            var encryptedPassword = param.EncryptedPassword;
            if (encryptedPassword == false) {
                alert('RSA公钥无效，当前不支持RSA加密，但系统可以正常登录，但安全性缺乏保障，请与管理员联系！');
                return true;
            }
            else {
                //简单模式：采用公钥加密登录密码
                $("#Password").val("Encrypt.");
                $('#RSAPasswod').val(encryptedPassword);
                return true;
            }
        }
    });

    $("[name=resetpassword]").click(function () {
        var username = $("#UserName").val();
        if (username.length == 0) {
            alert('请输入有效用户名！');
            $("#UserName").focus();
            return;
        }

        $.ajax({
            url: '${pageContext.request.contextPath}/Home/SetCheckCode',
            dataType: 'json',
            data: {
                username: username,
                rnd: Math.random(0 , 100)
            },
            success: function (data) {

                if (data.code == true) {
                    //打开密码设置窗口
                    $("#dialog_reset").dialog({
                        title: "重置密码",
                        width: 600,
                        modal: true,
                        autoOpen: false
                    });

                    var chkcode = data.chkcode;
                    if (chkcode == undefined)
                        chkcode = '';

                    $.get('${pageContext.request.contextPath}/User/UserResetPassword', { rnd: Math.random(0, 100) }, function (data) {
                        $("#dialog_reset").html(data);
                        $("#dialog_reset").dialog("open");
                        $('#hiddenusername').val(username);
                        $('#chkcode').val(chkcode);
                    });

                } else {
                    alert(data.err_msg)
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {//请求失败处理函数
                alert('调用接口失败！');
            }
        });
    });
</script>