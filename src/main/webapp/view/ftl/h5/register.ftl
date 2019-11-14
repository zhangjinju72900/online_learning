<!doctype html>
<html lang="zh-CN">
<head>
  <script  src="/view/common/css/plugins/easyui-1.5.2/jquery.min.js"></script>
	 <script  src="/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js"></script>
	 <script src="/view/common/assets/jqueryAjaxUtil.js" charset="utf-8"></script>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=750, user-scalable=no">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>中德诺浩注册</title>
  <style>

    body {
      margin: 0;
      font-family: PingFangSC-Regular, "Microsoft YaHei", Arial, sans-serif;
      font-size: 32px;
      line-height: 1;
    }

    ul {
      margin: 0;
      padding: 0;
    }

    [hidden] {
      display: none;
    }

    .wrap {
      position: relative;
      width: 750px;
      margin: 0 auto;
    }

    .close {
      display: block;
      width: 27px;
      height: 26px;
      margin-top: 50px;
      margin-left: 30px;
      background: url('/view/common/img/close.png') no-repeat center center;
    }

    form {
      padding: 0 50px;
    }

    .logo {
      width: 156px;
      height: 77px;
      margin-top: 60px;
    }

    h1 {
      margin-top: 40px;
      margin-bottom: 60px;
      font-size: 48px;
    }

    .item {
      position: relative;
      margin-bottom: 30px;
    }

    .title {
      display: flex;
      align-items: center;
    }

    .title span {
      font-size: 32px;
      font-weight: bold;
    }

    .title img {
      margin-right: 15px;
    }

    .input {
      display: flex;
      align-items: center;
      overflow: hidden;
      height: 100px;
      position: relative;
      padding-left: 60px;
      border-bottom: 1px solid #dcdcdc;
    }

    .input label {
      display: flex;
      align-items: center;
    }

    input[type=radio] {
      display: none;
    }

    .input label.woman i {
      width: 28px;
      height: 44px;
      background: url("/view/common/img/woman-outline.png") no-repeat center center;
    }

    .input label.man i {
      width: 28px;
      height: 44px;
      background: url("/view/common/img/man-outline.png") no-repeat center center;
    }

    .woman input[type=radio]:checked + i {
      background-image: url("/view/common/img/woman.png");
    }

    .man input[type=radio]:checked + i {
      background-image: url("/view/common/img/man.png");
    }

    .input i {
      margin-right: 10px;
    }

    .input span {
      margin-right: 70px;
    }

    input, select {
      display: block;
      width: 100%;
      height: 100px;
      border: none;
      font-size: 32px;
      outline: 0;
    }

    .msg {
      display: none;
      position: absolute;
      top: 50%;
      right: 0;
      transform: translateY(-50%);
      color: #ff0101;
      font-size: 28px;
    }

    .error .msg {
      display: block;
    }

    .error.input {
      border-bottom: 1px solid #ff0101;
    }

    .list {
      display: none;
      position: absolute;
      top: 160px;
      left: 0;
      z-index: 1;
      width: 100%;
      padding: 20px 0;
      border-radius: 4px;
      background-color: #ffffff;
      font-size: 26px;
      box-shadow: 0px 0px 21px 0px rgba(2, 7, 20, 0.2);
    }

    .list li {
      display: flex;
      align-items: center;
      height: 80px;
      padding: 0 30px;
    }

    .list li.active {
      background-color: #eeeeee;
    }

    button {
      display: block;
      width: 442px;
      height: 88px;
      margin: 100px auto;
      border: none;
      border-radius: 44px;
      background-image: linear-gradient(90deg, rgb(0, 132, 253) 0%, rgb(97, 179, 255) 100%);
      font-size: 36px;
      color: #ffffff;
    }

  </style>
</head>
<body>
  <div class="wrap">
    <form >
      <img src="/view/common/img/logo.png" class="logo">
      <h1>请填写以下注册信息</h1>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/school.png">
          <span>学校</span>
        </div>
        <div class="input">
         <input id="schoolInp" type="text" placeholder="请选择学校" onfocus="onSchoolInp(event)" oninput="onSchoolInp(event)" onblur="checkValue(this)">
         <div class='msg' id="schoolMsg" style="display:none">*请选择所在学校</div>
         <input id="schoolId" type="hidden">
        </div>
        <ul class="list" id="schoolList">
        </ul>
      </div>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/name.png">
          <span>姓名</span>
        </div>
        <div class="input">
          <input type="text" placeholder="输入真实姓名" name="name" id ="name" onblur="checkValue(this)">
            <div class="msg" style="display:none" id="nameMsg">*姓名不能为空</div>
        </div>
      </div>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/school.png">
          <span>性别</span>
        </div>
        <div class="input">
          <label class="woman">
            <input type="radio" value="1" name="sex" checked> <i></i>
            <span>女</span>
          </label>
          <label class="man">
            <input type="radio" value="0" name="sex"> <i></i>
            <span>男</span>
          </label>
        </div>
      </div>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/identity.png">
          <span>身份证号</span>
        </div>
        <div class="input error">
          <input type="text" placeholder="输入身份证" name="cardNum" id="cardNum" onblur="checkValue(this)">
          <div class="msg" style="display:none" id="cardMsg">*身份证输入错误</div>
        </div>
      </div>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/password.png">
          <span>密码</span>
        </div>
        <div class="input">
          <input type="password" placeholder="输入密码" name="ctlNewPassword" id="ctlNewPassword" onblur="checkValue(this)">
           <div class="msg" style="display:none" id="newMsg">*密码不能为空</div>
        </div>
      </div>
      <div class="item">
        <div class="title">
          <img src="/view/common/img/password.png">
          <span>确认密码</span>
        </div>
        <div class="input">
          <input type="password" placeholder="确认密码" name="ctlReNewPassword" id="ctlReNewPassword" onblur="checkValue(this)">
            <div class="msg" style="display:none" id="reNewMsg">*确认密码不能为空</div>
        </div>
      </div>

      <input id="schoolValue" type="text" name="schoolValue" hidden>

      <button type="button" onclick="register()" id="btn_submit">注册</button>
    </form>
  </div>

<script>
function ajaxPost1(url, data, callback,error, isasync) {
    ajaxRequest1('post', url, data, callback,error,false,false, isasync);
}
function ajaxRequest1(type, url, data, callback, error,loading, cache, isasync) {
	if(typeof(isasync)=="undefined"){
		isasync=true;
	} else {
		isasync=isasync;
	}
    //loading, cache,
    var ajaxConfig = {
        url: '',
        data: {},
        callback: null,
        error: null,//错误回调
        loading: true,
        cache: true,
        async: isasync
    };
    // 判断每一个参数url的类型
    // 如果是对象则是请求参数对象
    // 如果是字符串则是请求URL，参数和回调要继续检测后面的参数
    var token = data['token'];
    if((token==undefined || token=="") && url && 
    		(url.indexOf("login")<0 && url.indexOf("/api/close")<0 && url.indexOf("getValidateStatus")<0)&& url.indexOf("getVideoPlayAuth")<0&&url.indexOf("checkCardNum")<0){
//		showError("已拒绝未携带token的请求" + url);
    	console.log("已拒绝未携带token的请求" + url);
    	return;
    }
    delete data['token'];
    if (typeof url === 'string') {
    	//data code msg
    	var param = {
    			app:'',
    			ver:'',
    			cid:'',
    			uid:'',
    			wid:'',
    			pid:'',
    			time:getNowFormatDate(),
    			data:data,
    			hmac:''};

		//var defaultRequest = getStorage("defaultRequest");
    	try {

            var appStartData = getStorage("appStartData");
    		if(appStartData!=null&&appStartData.app!=""){
    			param['app'] = appStartData.app;
    		}
    		if(appStartData!=null&&appStartData.ver!=""){
    			param['ver'] = appStartData.ver;
    		}
    		if(appStartData!=null&&appStartData.cid!=""){
    			param['cid'] = appStartData.cid;
    		}
    		if(appStartData!=null&&appStartData.uid!=""){
    			param['uid'] = appStartData.uid;
    		}
    		if($("#uiid").val()){
    			param['wid'] = $("#uiid").val();
    		}
    		if($("#puid").val()){//parent
    			param['pid'] = $("#puid").val();
    		}    		
		} catch (e) {
			console.log(e.name + ": " + e.message);
		}
        ajaxConfig.url = url;
        ajaxConfig.data = param;
        ajaxConfig.callback = callback;
        ajaxConfig.error = error;
        ajaxConfig.loading = typeof(loading) === 'undefined' ? true : loading;
        ajaxConfig.cache = cache;
    } else {
        ajaxConfig = $.extend({}, ajaxConfig, url);
    }
    $.ajax({
        type: type,
        url: ajaxConfig.url,
        data: JSON.stringify(ajaxConfig.data),
        beforeSend: function(xhr) {
        	xhr.setRequestHeader("token",token);
        },
        dataType: 'json',
        cache: ajaxConfig.cache,
        async: ajaxConfig.async,
        contentType:"application/json",
        success: function(response) {
        	var code = response.code;
        	if(code==199){//session timeout
        		parent.location.reload();
        	}else if(code>=0){
	    		if(response.token){
	    			updateClientContext(response.token);
	    		}
	    		ajaxConfig.callback(response);
        	}else{
        		console.log("code="+code + " ,do nothing");
        	}
        },
        error:function (res) 
        { 
        	if (ajaxConfig.error){
        		ajaxConfig.error(res);
    		}else{
    			showError("未知异常,详情请查看错误日志");
    		}
        }
    })
}
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var hour = date.getHours();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if (hour <= 9) {
        strDate = "0" + hour;
    }    
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + hour + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

	ajaxPost1(
		"${ctx}loginAn",{
			username:1
		},function(result){
			
		}	
	);

function checkValue(obj){
	if(obj.value!=null&&obj.value!=""){
		obj.nextElementSibling.style.display="none";
	}
}
//后台的接口地址
var API_PROXY = '';
//后台的接口列表
var APIS = {	    
		
		/**
	     * 匿名登录
	     */
		
	    loginAn: {
	        url: '/loginAn'
	    },
	    login: {
	        url: '/login'
	    },
	    /**
	     * 注册获取学校信息
	     * **/
	    frmPersonalInfoList:{
	    	query:{
  		        url: '/api/query',
	            needLogin: true,
	            tokenKey: 'OnClick_pToolbar_btnQueryBySchool_frmPersonalInfoList_jsquery'
	    	}
	    },
	    /**
	     * 注册获取学校信息
	     * **/
	     frmUserAdd:{
	    	 savecustom:{
  		        url: '/api/savecustom',
	            needLogin: true,
	            tokenKey: 'OnClick_pToolbar_btnSave_frmUserAdd_jssave'
	    	}
	    },
		
	    };
     const schoolList = document.querySelector('#schoolList');
    const schoolValue = document.querySelector('#schoolValue');
    const schoolInp = document.querySelector('#schoolInp'); 
    
    function getSchoolList(text, callback) {
        console.log('学校框里的值:', text);
        ajaxPost(APIS.frmPersonalInfoList.query, 
			    {lk_schoolName : text}, 
			function(result) {
				    	var rows =  result.data.rows;
				    	 callback(rows);
			});

       
      }
 
    // 监听学校输入框
    function onSchoolInp(e) {
    	 var schoolId = $("#schoolId").val();
    	 if(schoolId!=null&&schoolId!=""){
    		 $("#schoolMsg").hide();
    	 }
    	 const text = e.target.value;
         if (!text) {
       	   $("#schoolId").val(""); 
           return;
         }
         $("#schoolList").find("li").each(function(){
     		   $(this).remove();
     	   });
         getSchoolList(text, function(rows) {
             let lis = [];
             for (i=0;i<rows.length;i++) {
		          	let li = "<li value='"+rows[i].id+"'>"+rows[i].schoolName+"</li>";
		            lis.push(li);
		          } 

             schoolList.innerHTML = lis.join('');
             schoolList.style.display = 'block';
           });
    }

    // 监听选择学校事件
    schoolList.addEventListener('click', function(e) {
      const target = e.target;
      $("#schoolId").val(target.value);
      schoolInp.value = target.innerText;
      schoolValue.value = target.getAttribute('value');
      schoolList.style.display = 'none';
    });

    function register() {
    	 var checkCode = function (val) {
        	 var p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
        	 var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
        	 var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
        	 var code = val.substring(17);
        	 if(p.test(val)) {
        	  var sum = 0;
        	  for(var i=0;i<17;i++) {
        	   sum += val[i]*factor[i];
        	  }
        	  if(parity[sum % 11] == code.toUpperCase()) {
        	   return true;
        	  }
        	 }
        	 return false;
       }
    	var schoolId = $("#schoolId").val();
    	var name = $("#name").val();
    	var cardNum = $("#cardNum").val();
    	var ctlNewPassword = $("#ctlNewPassword").val();
    	var ctlReNewPassword = $("#ctlReNewPassword").val();
    	var sex = $(":radio[name='sex']:checked").val();
    	var msg = 0;
    	if(schoolId==null||schoolId==""){
    		$("#schoolMsg").show();
    		msg++;
    	}
    	if(name==null||name==""){
    		$("#nameMsg").show();
    		msg++;
    	}
     	if(ctlNewPassword==null||ctlNewPassword==""){
    		$("#newMsg").show();
    		msg++;
    	}
    	
    	if(ctlReNewPassword==null||ctlReNewPassword==""){
    		$("#reNewMsg").text("确认密码不能为空");
    		$("#reNewMsg").show();
    		msg++;
    	}else if(ctlNewPassword!=ctlReNewPassword){
    		$("#reNewMsg").text("确认密码不一致");
    		$("#reNewMsg").show();
    		msg++;
    	}
    	if(cardNum==null||cardNum==""){
    		$("#cardMsg").text("身份证不能为空");
    		$("#cardMsg").show();
    		msg++;
    	}else if(!checkCode(cardNum)){
    		$("#cardMsg").text("*身份证号码有误");
    		$("#cardMsg").show();
    		msg++;
    	}else{
    		ajaxPost1(
    				"${ctx}checkCardNum",{
    					cardNum:cardNum
    				},function(result){
    					if(result.code==1){
    						$("#cardMsg").text("*身份证号已被注册");
    			    		$("#cardMsg").show();
    			    		msg++;
    					}else{
    						if(msg>0){
    				    		return false;
    				    	}else{
    				    		$("#btn_submit").attr("style","display:none");
    				    		 ajaxPost(APIS.frmUserAdd.savecustom, 
    				    				 {schoolId : schoolId,
    				    	    	      name :name,
    				    	    	      cardNum :cardNum,
    				    	    	      ctlNewPassword :ctlNewPassword,
    				    	    	      ctlReNewPassword :ctlReNewPassword,
    				    	    	      sex : sex
    				    	    	      },function(result) {
    				  					    	if(result.code==0){
    				  					    		window.location.href="${ctx}/view/common/success.html";	
    				  					    	}else{
    				  					    		alert("注册失败");
    				  					    	}
    				    				});

    				    	}
    					}
    				}	
    			);
    	}
    	
   
    	
    	
    /*   const form = document.querySelector('form');
      const inputs = form.querySelectorAll('input');
      for (const input of inputs) {
        console.log(input.value);
      }
      return false; */
    }
   
  </script>
</body>
</html>
