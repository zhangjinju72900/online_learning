<!DOCTYPE html>
<html >
<head>
	 
	
    <title>登录页</title>
    <meta charset="UTF-8">
    <#assign ctx=request.contextPath>
    <meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/common.css">
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/login.css">
    <script src="${ctx}/view/common/js/jquery-1.8.3.min.js"></script>
 	<#include "pc/common/base.ftl">
    
    <!-- <script src="${ctx}/view/common/assets/h5/jsjquery-1.9.1.min.js"></script> -->
</head>
   <body>
     <div id="user-name">
         <img src="${ctx}/view/common/assets/h5/img/person.png">
         <input type="text" id="txt_username" placeholder="请输入用户名" placeholder="6-20位英文字母或者数字">
     </div>
     <div id="pwd">
        <img src="${ctx}/view/common/assets/h5/img/lock.png">
        <input type="password" id="txt_password" placeholder="请输入密码" placeholder="密码6-18位字母、数字">
    </div>

    <div id="forget">
        <b id="people" class="over">用户名或密码错误</b>
        <a>忘记密码?</a>
    </div>
	<button onclick="doLogin()">登 录</button>
	<script src="${ctx}/view/common/assets/h5/js/login.js"></script>
	<script src="${ctx}/view/common/js/store/myStorage.js?${date}" charset="utf-8"></script>
    <script src="${ctx}/view/common/js/store/json2.js?${date}" charset="utf-8"></script>
    <script src="${ctx}/view/common/js/store/localDB.js?${date}" charset="utf-8"></script>
</body>
<script type="text/javascript">
$(document).ready(function () {
    console.log(localDB.select("appStart"));
    if (localDB.select("appStart").length == 0) {
        localDB.createSpace("appStart");
        localDB.insert("appStart", {app: '${app}', ver: '${ver}', cid: '${cid}', uid: ''});
    }

    var notice = '${notice}';
    if (notice != '') {
        $(".mBox-x").show();
    }

    $('.mBox-close-x').click(function () {
        mq_stopLoop();
        $(this).parent().hide();
    });

    var errorMsg = '${errorMsg}';
    if (null != errorMsg && '' != errorMsg) {
        $("#tool-mes > div").show();
    }

});
	var siteVerify = true;
	function doLogin() {
        var userName = $('#txt_username').val();
        var userPwd = $('#txt_password').val();
        if (userName == '') {
			console.log('用户名不能为空！');
			$("#people").removeClass("over");
			$("#people").addClass("show");
			setTimeout(" window.location.reload()", 3000 );
            return false;
        }
        if (userPwd == '') {
        	console.log('密码不能为空！');
			$("#people").removeClass("over");
			$("#people").addClass("show");
			setTimeout(" window.location.reload()", 3000 );
            return false;
        }
        
        //人机验证
        if (siteVerify) {
        	console.log(userName);
            console.log(userPwd);
            //判断此用户是否存在于数据库中
            ajaxPost(APIS.login, {
                //值
                userName: userName,
                password: userPwd
            }, function (result) {
            	console.log(result);
                if (result.data!=null&&result.data.userId!=undefined) {
                	localDB.update("appStart", {app: '${app}', ver: '${ver}', cid: '${cid}', uid: ''}, {
                        app: '${app}',
                        ver: '${ver}',
                        cid: '${cid}',
                        uid: result.data.userId
                    });
                	window.location.href = "${ctx}/";
                    window.location.href = "${ctx}/my";
                } else {
                	console.log('登录失败');
                	$("#people").removeClass("over");
            		$("#people").addClass("show");
            		setTimeout(" window.location.reload()", 3000 );
                }

            });
        }
		
	} 
	
</script>
</html>