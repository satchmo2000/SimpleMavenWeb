function getContextPath() {
    const url = window.location.pathname;
    const contextPath = url.split('/')[1];
    return '/' + contextPath;
}

function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    //exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    //Cookie超时时间：60秒
    exp.setTime(exp.getTime() + 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function setCookie365(name, value) {
    var Days = 365;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);

    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

function GetQueryString(name) {

    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");

    var r = decodeURI(window.location.search).substr(1).match(reg);

    if (r != null) return unescape(r[2]); return null;

}

//公共脚本调用
var Request = new function() {
    this.search = decodeURI(window.location.search);
    this.QueryString = new Array();
    var tmparray = this.search.substr(1, this.search.length).split("&")
    for (var i = 0; i < tmparray.length; i++) {
        var name = tmparray[i].substr(0, tmparray[i].indexOf("=")); //alert(name);
        var value = tmparray[i].substr(tmparray[i].indexOf("=") + 1, tmparray[i].length); //alert(value);
        this.QueryString[name] = value;
    }
}

function ReplaceUrlSection(pathname, index , newvalue) {
    //  /Tasks/TaskInfo/11007?tkid=1
    var tmparray = pathname.split("/")
    var arrayLength = tmparray.length;
    if(index < 0)index = index + arrayLength;
    var newpathname = ''
    for (var i = 0; i < arrayLength; i++) {
        if (i == index)
            newpathname = newpathname + newvalue;
        else
            newpathname = newpathname + tmparray[i];

        if (i < arrayLength - 1)
            newpathname = newpathname + '/';
    }

    return newpathname;
}

//导航性功能切换
function LocationFuncUrl(index, newvalue) {
    location.href = (location.origin == undefined ? "" : location.origin) + ReplaceUrlSection(location.pathname, index, newvalue) + location.search;
}

//单条件切换
function toLocationX(field ,newvalue) {
    this.search = decodeURI(window.location.search);
    this.searchNoPage = "";

    if (this.search.length > 1) {
        var tmparray = this.search.substr(1, this.search.length).split("&")
        for (var i = 0; i < tmparray.length; i++) {
            var name = tmparray[i].substr(0, tmparray[i].indexOf("=")); //alert(name);
            if (name.length > 0) {

                var value = tmparray[i].substr(tmparray[i].indexOf("=") + 1, tmparray[i].length); //alert(value);
                if (name.toLowerCase() != field) {
                    if (this.searchNoPage != "")
                        this.searchNoPage = this.searchNoPage + '&';
                    this.searchNoPage = this.searchNoPage + name + '=' + value;
                }
            }
        }
    }

    if (this.searchNoPage.length > 0)
        return location.pathname + '?' + encodeURI(this.searchNoPage) + '&' + field + '=' + encodeURI(newvalue);
    else
        return location.pathname + '?' + field + '=' + encodeURI(newvalue);

    return false;
}


//单条件切换
function toReplaceX(field ,newvalue) {
    this.search = decodeURI(window.location.search);
    this.searchNoPage = "";

    if (this.search.length > 1) {
        var tmparray = this.search.substr(1, this.search.length).split("&")
        for (var i = 0; i < tmparray.length; i++) {
            var name = tmparray[i].substr(0, tmparray[i].indexOf("=")); //alert(name);
            if (name.length > 0) {

                var value = tmparray[i].substr(tmparray[i].indexOf("=") + 1, tmparray[i].length); //alert(value);
                if (this.searchNoPage != "")
                    this.searchNoPage = this.searchNoPage + '&';
                if (name.toLowerCase() != field) {
                    this.searchNoPage = this.searchNoPage + name + '=' + value;
                }
                else {
                    this.searchNoPage = this.searchNoPage + name + '=' + newvalue;
                }
            }
        }
    }

    if (this.searchNoPage.length > 0)
        return location.pathname + '?' + encodeURI(this.searchNoPage);
    else
        return location.pathname;
}

//修改location.search中的多个字段值
function ReplaceLocationSearch(fields, newvalues) {
    return ReplaceLocation(decodeURI(location.search), fields, newvalues);
}

//修改检索条件中多个字段的值
function ReplaceLocation(search, fields, newvalues) {
    this.search = search;
    this.searchNoPage = "";

    //先删除旧条件
    if (this.search.length > 1) {
        var tmparray = this.search.substr(1, this.search.length).split("&")
        for (var i = 0; i < tmparray.length; i++) {
            var name = tmparray[i].substr(0, tmparray[i].indexOf("=")); //alert(name);
            if (name.length > 0) {

                var value = tmparray[i].substr(tmparray[i].indexOf("=") + 1, tmparray[i].length); //alert(value);

                var nFieldIndex = ExistField(fields, name);
                if (nFieldIndex < 0) {
                    if (this.searchNoPage != "")
                        this.searchNoPage = this.searchNoPage + '&';
                    this.searchNoPage = this.searchNoPage + name + '=' + value;
                }
            }
        }
    }

    //再增加新条件
    for (var j = 0; j < fields.length; j++) {
        if (newvalues[j] == "")
            continue;
        if (this.searchNoPage != "")
            this.searchNoPage = this.searchNoPage + '&';
        this.searchNoPage = this.searchNoPage + fields[j] + '=' + newvalues[j];
    }

    if (this.searchNoPage.length > 0)
        return location.pathname + '?' + encodeURI(this.searchNoPage);
    else
        return location.pathname + search;
}

//判断字段是否存在
function ExistField(fields, field) {
    for (var i = 0; i < fields.length; i++) {
        if (field.toLowerCase() == fields[i].toLowerCase())
            return i;
    }
    return -1;
}

function baseload() {
    var msg = Request.QueryString["msg"];
    if (msg != undefined) {
        alert(decodeURI(msg));
    }
}

$("#checkall").click(function () {
    $("input[name='checkone']").prop("checked", this.checked);
});

$("input[name='checkone']").click(function () {
    var $subs = $("input[name='checkone']");
    $("#checkall").prop("checked", $subs.length == $subs.filter(":checked").length ? true : false);
});

$("#checkall1").click(function () {
    $("input[name='checkone1']").prop("checked", this.checked);
});

$("input[name='checkone1']").click(function () {
    var $subs = $("input[name='checkone1']");
    $("#checkall1").prop("checked", $subs.length == $subs.filter(":checked").length ? true : false);
});

$("#checkall2").click(function () {
    $("input[name='checkone2']").prop("checked", this.checked);
});

$("input[name='checkone2']").click(function () {
    var $subs = $("input[name='checkone2']");
    $("#checkall2").prop("checked", $subs.length == $subs.filter(":checked").length ? true : false);
});

$("#checkall3").click(function () {
    $("input[name='checkone3']").prop("checked", this.checked);
});

$("input[name='checkone3']").click(function () {
    var $subs = $("input[name='checkone3']");
    $("#checkall3").prop("checked", $subs.length == $subs.filter(":checked").length ? true : false);
});


$('[name=select]').on('click', function () {
    var part = $(this).attr('data-part');
    LocationFuncUrl(-1, part);
    return false;
});

//////////////////////////时间计算////////////////////////////
/*
 *   功能:实现VBScript的DateAdd功能.
 *   参数:interval,字符串表达式，表示要添加的时间间隔.
 *   参数:number,数值表达式，表示要添加的时间间隔的个数.
 *   参数:date,时间对象.
 *   返回:新的时间对象.
 *   var now = new Date();
 *   var newDate = DateAdd( "d", 5, now);
 *---------------   DateAdd(interval,number,date)   -----------------
 */
function DateAdd(interval, number, date) {
    switch (interval) {
        case "y": {
            date.setFullYear(date.getFullYear() + number);
            return date;
            break;
        }
        case "q": {
            date.setMonth(date.getMonth() + number * 3);
            return date;
            break;
        }
        case "m": {
            date.setMonth(date.getMonth() + number);
            return date;
            break;
        }
        case "w": {
            date.setDate(date.getDate() + number * 7);
            return date;
            break;
        }
        case "d": {
            date.setDate(date.getDate() + number);
            return date;
            break;
        }
        case "h": {
            date.setHours(date.getHours() + number);
            return date;
            break;
        }
        case "m": {
            date.setMinutes(date.getMinutes() + number);
            return date;
            break;
        }
        case "s": {
            date.setSeconds(date.getSeconds() + number);
            return date;
            break;
        }
        default: {
            date.setDate(d.getDate() + number);
            return date;
            break;
        }
    }
}

function addMonth(date, months) {
    var d = new Date(date);
    var year = d.getFullYear();
    var month = d.getMonth() + months;
    while (month < 0) {
        year--;
        month += 12;
    }
    while (month > 11) {
        year++;
        month -= 12;
    }
    month++;

    var day = d.getDate();

    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var val = year + "-" + month + "-" + day;
    return val;
}

function addYear(date, years) {
    var d = new Date(date);
    var year = d.getFullYear() + years;
    var month = d.getMonth() + 1;
    var day = d.getDate();

    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var val = year + "-" + month + "-" + day;
    return val;
}

function addDate(date, days) {
    var d = new Date(date);
    d.setDate(d.getDate() + days);
    var month = d.getMonth() + 1;
    var day = d.getDate();
    if (month < 10) {
        month = "0" + month;
    }
    if (day < 10) {
        day = "0" + day;
    }
    var val = d.getFullYear() + "-" + month + "-" + day;
    return val;
}

//Json序列化的日期转换（默认为1900-1-1）
function JsonDate(jd) {
    var dtstr = jd.replace(/\/Date\((\d+)\)\//gi, "$1");
    var dtint = eval(dtstr);
    if (dtint > 0) {
        var dt = new Date(dtint);
        return (dt);
    }
    else {
        return new Date(1900, 0, 1);
    }
}

/**
 * 原型：字符串格式化
 * param args 格式化参数值
*/

String.prototype.format = function (args) {
    var result = this;
    if (arguments.length < 1) {
        return result;
    }

    var data = arguments; // 如果模板参数是数组
    if (arguments.length == 1 && typeof (args) == "object") {
        // 如果模板参数是对象
        data = args;
    }
    for (var key in data) {
        var value = data[key];
        if (undefined != value) {
            result = result.replaceAll("\\{" + key + "\\}", value);
        }
    }
    return result;
}

String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

String.prototype.stringToDate = function () {

    return new Date(Date.parse(this.replace(/-/g, "/")));

}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

function Date2String(dt) {
    var year = dt.getFullYear();
    var month = dt.getMonth() + 1;
    var day = dt.getDate();
    var hour = dt.getHours();
    var minute = dt.getMinutes();
    var second = dt.getSeconds();

    if (month < 10) {
        month = "0" + month;
    }

    if (day < 10) {
        day = "0" + day;
    }

    if(hour < 10){
        hour = "0" + hour;
    }

    if(minute < 10){
        minute = "0" + minute;
    }

    if(second < 10){
        second = "0" + second;
    }

    var val = String(year) + String(month) + String(day) + String(hour) + String(minute) + String(second);
    return val;
}
