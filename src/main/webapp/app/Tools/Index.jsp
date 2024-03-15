<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>应用工具</title>
    <jsp:include page="/Tools/IncludeFile"></jsp:include>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/md5.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/crypto-js-4.2.0/crypto-js.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/plugins/jsencrypt.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/SignUtil.js"></script>

    <style type="text/css">
        .rowtitle {
            height: 50px;
            width: 100%;
        }

        .coltitle {
            width: 150px;
        }

        .title {
            height: 30px;
            width: 200px;
        }

        .input {
            width: 400px;
        }

        textarea {
            width: 100%;
        }
    </style>
</head>
<body>
<div id="wrapper">
    <div id="content">
        <div class="container">
            <!-- @RenderBody()  -->
            <h2>内容加解密，RSA密钥对生成，实体代码</h2>
            <p>
                输入需要加解密的字符串（DES），或需要生成实体代码的存储过程。
            </p>
            <div class="rowtitle">
                <div class="coltitle">&nbsp;</div>
                <input id="conn" type="text" style="width: 800px;" value='${conn}'/>
                <input id="tokenid" type="text" class="hidden" value="${tokenid}" style="width:300px;"/>
                <input id="logindate" type="text" class="hidden" value="${logindate}" />
            </div>
            <div class="rowtitle">
                <div class="coltitle">&nbsp;</div>
                <input type="submit" value="Des加/解密" title="密钥：http://www.ebspace.com" onclick="javascript:EncodeText();"/>
                <input type="button" value="RSA密码对" onclick="javascript:RSAPassword();"/>
                <input type="button" value="实体代码" onclick="javascript:DefineJson();"/>
                <input type="button" value="签名设计" onclick="javascript:NameSign();" />
                <input type="button" value="服务负载" onclick="javascript:UploadServerLoad()" />
                <input type="button" value="Sign Encode" onclick="javascript:SignEncode();" title="输入登录密码，获取加密TokenId"/>
                <input type="button" value="Sign Decode" onclick="javascript:SignDecode();" title="输入加密TokenId，密码为用户输入的未加密的密码（固定为123456，仅用于测试），获取真实的TokenId"/>
            </div>
            <div>
                <div id="imgSign" class="coltitle">&nbsp;</div>

                <textarea id="encode"
                          cols="100" rows="20">${encode}</textarea>
            </div>
            <div>
                <p id="serverload">CPU:${cpu}%，MEMORY:${memory}%，DISK:${disk}%</p>
            </div>
            <!-- /@RenderBody()  -->
        </div>
        <!-- /.container -->
    </div>
    <!-- /#content -->

</div>
<!-- /#wrapper -->
</body>
</html>
<script type="text/javascript">
    $(function(){
        setInterval(GetCurrentServerLoad, 5000);
    });

    function SignEncode(){
        var vTokenId = $("#tokenid").val();
        var vLoginDate = $("#logindate").val();
        var vPassword = $("#conn").val();
        var vEncodeTokenId = SignUtil.EncodeTokenIdEx(vTokenId, vPassword, vLoginDate);
        $("#encode").val(vEncodeTokenId);
    }

    function SignDecode(){
        var vEncodeTokenId = $("#conn").val();
        var vLoginDate = $("#logindate").val();
        var vPassword = "123456";
        var vTokenId = SignUtil.DecodeTokenIdEx(vEncodeTokenId, vPassword, vLoginDate);
        $("#encode").val(vTokenId);
    }

    function EncodeText() {
        var vText = $("#conn").val();
        var vPassword = "http://www.ebspace.com";

        if (vText.length == 0) {
            alert('请输入要加密的字符串！');
            return false;
        }

        var currentTime = new Date();
        $.getJSON(
            '${pageContext.request.contextPath}/Tools/EncodeText.do?curTime=' + currentTime,
            {
                text: vText,
                password: vPassword
            }
            , function (json) {
                if (json != undefined) {
                    if (json.code) {
                        $("#encode").val(json.err_msg);
                    } else {
                        alert(json.err_msg);
                    }
                } else {
                    alert('非法操作，请不要试验!');
                }
            }
        );
    }

    function RSAPassword() {
        var currentTime = new Date();
        $.getJSON(
            '${pageContext.request.contextPath}/Tools/RSAPassword.do?curTime=' + currentTime,
            {}
            , function (json) {
                if (json != undefined) {
                    if (json.code) {
                        $("#encode").val('prikey=' + json.prikey + '\n\npubkey=' + json.pubkey);
                    } else {
                        alert(json.code);
                    }
                } else {
                    alert('非法操作，请不要试验!');
                }
            }
        );
    }

    function DefineJson() {
        var vConn = $("#conn").val();
        if (vConn.length == 0) {
            alert('请输入数据库存储过程！');
            return false;
        }

        var currentTime = new Date();
        $.getJSON(
            '${pageContext.request.contextPath}/Tools/DefineJson.do?curTime=' + currentTime,
            {text: vConn}
            , function (json) {
                if (json != undefined) {
                    if (json.code) {
                        $("#encode").val(json.data);
                        alert(json.err_msg);
                    } else {
                        alert(json.err_msg);
                    }
                } else {
                    alert('非法操作，请不要试验!');
                }
            }
        );
    }

    function UploadServerLoad(){
        $.ajax({
            url: '${pageContext.request.contextPath}/Phone/uploadCurrentServerLoad',
            dataType: 'json',
            data: {
                rnd: Math.random()
            },
            success: function (json) {
                alert(json.err_msg);
            }
        });
    }

    function GetCurrentServerLoad(){
        $.ajax({
            url: '${pageContext.request.contextPath}/Tools/GetCurrentServerLoad',
            dataType: 'json',
            data: {
                rnd: Math.random()
            },
            success: function (json) {
                if(json.code){
                    var html = 'CPU:' + json.cpu + '%,MEMORY:' + json.memory + '%,DISK:' + json.disk + '%';
                    $("#serverload").html(html);
                }
                else {
                    $("#serverload").html(json.err_msg);
                }
            }
        });
    }

    function NameSign() {
        var vConn = $("#conn").val();
        if (vConn.length == 0) {
            alert('请输入数据库连接串！');
            return false;
        }

        $.ajax({
            url: '${pageContext.request.contextPath}/Tools/GetNameSigner',
            dataType: 'json',
            data: {
                id: vConn,
                zhenbi: 20191123,
                id1: 905,
                id2: 16,
                id3: '16711680',    //#FF0000
                id5: '0'            //#000000
            },
            success: function (json) {
                if (json.code) {
                    $("#imgSign").html(json.err_msg);
                }
                else {
                    alert(json.err_msg);
                }
            }
        });

        return;

        var url = "http://www.yishuzi.com/make.php?file=a&page=14";
        var formData = new FormData();
        formData.append("id", vConn);
        formData.append("zhenbi", 20191123);
        formData.append("id1", 905);
        formData.append("id2", 16);
        formData.append("id3", '#FF0000');
        formData.append("id5", '#000000');
        $.ajax({
            type: "POST", // 数据提交类型
            url: url, // 发送地址
            data: formData, //发送数据
            async: true, // 是否异步
            processData: false, //processData 默认为false，当设置为true的时候,jquery ajax 提交的时候不会序列化 data，而是直接使用data
            contentType: false,
            header: {
                'Access-Control-Allow-Origin': '*'
            },
            success: function (data) {
                alert(data);
                var json = JSON.parse(data);

            },
            error: function (e) {
                alert(e);
                console.log("不成功" + e);
            }
        });
    }
</script>
