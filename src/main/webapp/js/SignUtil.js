//需要支持的js文件
//md5.js
//crypto-js-4.2.0/crypto-js.js

(function () {
    window['SignUtil'] = {};                                              //创建SignUtil命名空间

    //多次MD5后使末位值与第一次MD5后的末位值相等
    function RandomMd5(md5c) {
        var md5e = md5(md5c);
        console.log('First MD5 is', md5e);
        var md5e_last = md5e.substring(31, 32);
        md5e = md5(md5e);
        var index = 1;
        while (md5e.substring(31, 32) != md5e_last) {
            md5e = md5(md5e);
            index++;
            //console.log('Loop', index, md5e);
        }
        console.log('Loop Count=', index, ',md5=', md5e);
        return md5e;
    }

    function MakeSign(publicKey, Password, msSub){
        var curDate = new Date();
        var curDateString0 = curDate.format("yyyyMMddhhmmss");
        var curSecond = curDate.getSeconds();
        if(msSub < 0){
            //当页面时间小于服务器时间时，要把时差补回去
            curDate.setSeconds(curSecond - msSub);
            curSecond = curDate.getSeconds();
        }
        var PasswordMd5 = md5(Password);
        var curDateString = curDate.format("yyyyMMddhhmmss");
        console.log('PasswordMd5=', PasswordMd5, 'Current Date=', curDateString);

        //密码片段越短越安全，建议截取后4位（取决于MD5破解库的查重率）
        var md5c = PasswordMd5.substring(28, 32) + curDateString + publicKey;
        var md5e = RandomMd5(md5c);

        //console.log('last md5 is', md5e);
        var crypt = new JSEncrypt();
        crypt.setPublicKey(publicKey);
        var encryptedPassword = crypt.encrypt(Password);

        var debugInfo = curSecond + ',' + curDateString0 + (msSub > 0 ? '-' : '+') + (Math.abs(msSub)) + '->' + curDateString + ',' + PasswordMd5 + '->' + md5e + ',Key=' + publicKey;

        return {CurrentDate: curDateString, Second: curSecond, Sign: md5e, EncryptedPassword: encryptedPassword, DebugInfo: debugInfo};
    }
    window['SignUtil']['MakeSign'] = MakeSign;

    function encryptDesCbcPkcs7Padding(message, key) {
        message = CryptoJS.enc.Utf8.parse(message);
        var keyWords = CryptoJS.enc.Utf8.parse(key);
        var iv = CryptoJS.lib.WordArray.create(new ArrayBuffer(8)); // 初始化向量，需要 8 个字节

        var encrypted = CryptoJS.DES.encrypt(message, keyWords, {
            iv: iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });
        return CryptoJS.enc.Base64.stringify(encrypted.ciphertext)
    }

    function decryptDesCbcPkcs7Padding(message, key) {
        message = CryptoJS.enc.Base64.parse(message);
        var keyWords = CryptoJS.enc.Utf8.parse(key);
        var iv = CryptoJS.lib.WordArray.create(new ArrayBuffer(8)); // 初始化向量，需要 8 个字节

        var decrypted = CryptoJS.DES.decrypt({ ciphertext: message }, keyWords, {
            iv: iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });

        var result = decrypted.toString(CryptoJS.enc.Utf8);
        result = CryptoJS.enc.Base64.parse(result.toString(CryptoJS.enc.Utf8)).toString(CryptoJS.enc.Utf8);
        return decodeURIComponent(result);
    }

    const iv = "abcdefghabcdefgh"; // 初始化向量，应为16位（16字节）

    //涉及密码为16位，取实际密码的前后8位，若原密码不足8位，则密码被多次复制使总长度大于8位
    function Expand16(data){
        var nDataLength = data.length;
        if(nDataLength == 0)
            return "";
        else{
            while(nDataLength < 8) {
                data += data;
                nDataLength *= 2;
            }
        }
        data = data.substring(0, 8) + data.substring(nDataLength - 8, nDataLength);
        return data;
    }

    function encryptAES(message, key){
        if(key.length == 0)
            return message;

        key = Expand16(key);

        const algorithm = "aes-256-cbc";
        const encrypted = CryptoJS.AES.encrypt(message, CryptoJS.enc.Utf8.parse(key), {
            iv: CryptoJS.enc.Utf8.parse(iv),
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });
        var encryptedtext = encrypted.toString();

        console.log('EncryptTokenId(', message, key, ')=', encryptedtext);

        return encryptedtext;
    }

    function decryptAES(message, key){
        if(key.length == 0)
            return message;

        key = Expand16(key);

        const algorithm = "aes-256-cbc";
        const decrypted = CryptoJS.AES.decrypt(message, CryptoJS.enc.Utf8.parse(key), {
            iv: CryptoJS.enc.Utf8.parse(iv),
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        });
        var decryptedtext = decrypted.toString(CryptoJS.enc.Utf8);
        console.log('DecryptTokenId(', message, key, ')=', decryptedtext);

        return decryptedtext;
    }

    function EncodeTokenIdEx(tokenId, Password, LoginDate){
        var strEncodingPassword = md5(Password).substring(0, 16) + LoginDate;
        var m_strEncodingTokenId = encryptAES(tokenId, strEncodingPassword);
        console.log(tokenId, '+', strEncodingPassword, '=', m_strEncodingTokenId);
        return m_strEncodingTokenId;
    }
    window['SignUtil']['EncodeTokenIdEx'] = EncodeTokenIdEx;

    function EncodeTokenId(tokenId, Password){
        var curDate = new Date();
        var curDateString = curDate.format("yyyyMMddhhmmss");
        var strEncodingPassword = md5(Password).substring(0, 16) + curDateString;
        var m_strEncodingTokenId = encryptAES(tokenId, strEncodingPassword);
        console.log(tokenId, '+', strEncodingPassword, '=', m_strEncodingTokenId);
        return m_strEncodingTokenId;
    }
    window['SignUtil']['EncodeTokenId'] = EncodeTokenId;

    function DecodeTokenIdEx(encodingTokenId, Password, LoginDate){
        var strDecodingPassword = md5(Password).substring(0, 16) + LoginDate;
        var m_strTokenId = decryptAES(encodingTokenId, strDecodingPassword);
        console.log(encodingTokenId, '-', strDecodingPassword, '=', m_strTokenId);
        return m_strTokenId;
    }
    window['SignUtil']['DecodeTokenIdEx'] = DecodeTokenIdEx;

    function DecodeTokenId(encodingTokenId, Password){
        var curDate = new Date();
        var curDateString = curDate.format("yyyyMMddhhmmss");
        var strDecodingPassword = md5(Password).substring(0, 16) + curDateString;
        var m_strTokenId = decryptAES(encodingTokenId, strDecodingPassword);
        console.log(encodingTokenId, '-', strDecodingPassword, '=', m_strTokenId);
        return m_strTokenId;
    }
    window['SignUtil']['DecodeTokenId'] = DecodeTokenId;
})();