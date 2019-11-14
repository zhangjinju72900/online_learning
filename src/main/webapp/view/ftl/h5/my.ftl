<!DOCTYPE html>
<html lang="en">
<head>
	<#assign ctx=request.contextPath>
    <meta charset="UTF-8">
    <meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>我的</title>
    <script src="${ctx}/view/common/assets/h5/js/jquery-1.9.1.min.js"></script>
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/common.css">
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/my.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/foot.css" />
	<script src="${ctx}/view/common/js/jquery-1.8.3.min.js"></script>
 	<#include "pc/common/base.ftl">
</head>
<body>
    <div class="outermost outermost-title">
        <span class="title">我的</span>
    </div>
    <div class="outermost">
        <span>姓名</span>
        <div class="content  rt">null</div>
    </div>
    <div class="outermost">
        <span>电话</span>
        <div class="content  rt">null</div>
    </div>
    <div class="outermost">
        <span>电子邮件</span>
        <div class="content  rt">null</div>
    </div>
    <div class="outermost " id="change-psd">
        <span>修改密码</span>
        <div class="content  rt change-icon"></div>
    </div>
    <div id="foot">
        <ul>
            <li>
                <a href="${ctx}/workplace">
                    <div class="icon icon-1"></div>
                    工作台
                </a>

            </li>
            <li>
                <a href="${ctx}/list/search-all">
                    <div class="icon icon-2"></div>
                    列表
                </a>

            </li>
            <li>
                <a href="${ctx}/defect/list">
                    <div class="icon icon-3"></div>
                    缺陷
                </a>

            </li>
            <li>
                <a href="${ctx}/my"  class="color-active">
                    <div class="icon icon-4 icon-4-active"></div>
                    我的
                </a>

            </li>
        </ul>
    </div>
    <div class="change-psd none">
        <div class="psd-freek">
            <div class="psd-text">
            	<input id="psd1" type="password" class="psd-text-input" placeholder="请输入新密码" maxlength="8" maxlength="16" onkeyup="check()">
                <input id="psd2" type="password" class="psd-text-input" placeholder="确认密码" maxlength="8" maxlength="16" onkeyup="check()"><br>
                <span class="psd-check none">密码不一致</span>
            </div>
            <div  class="psd-text">
                <button class="psd-canel" ><b style="color:#fff">取消</b></button>
                <button class="psd-ok" onclick="save()"><b style="color:#fff">确定</b></button>
            </div>
        </div>
    </div>
    <div class="submit-psd none">
        <div class="submit-freek">
            <div class="submit-img none">更新成功</div>
            <div class="submit-img1 none">更新失败</div>
        </div>
    </div>
    <script src="${ctx}/view/common/assets/h5/js/my.js"></script>
    <script src="${ctx}/view/common/assets/apis.js"></script>
	<script src="${ctx}/view/common/assets/jqueryAjaxUtil.js"></script>
</body>
<script type="text/javascript">
    $(function(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var empId = ${empId};
		console.log(empId);
		ajaxPost(APIS.frmUserList.queryUser,{
			eq_empId:empId
  		},function(data){
  			console.log(data);
  			render1(data);
  		});
	})
	function render1(data) {
		var html="";  

		html += "\n<div class=\"outermost outermost-title\">\n        <span class=\"title\">\u6211\u7684</span>\n    </div>\n    <div class=\"outermost\">\n        <span>\u59D3\u540D</span>\n        <div class=\"content  rt\">" + data.data.rows[0].empName + "</div>\n    </div>\n    <div class=\"outermost\">\n        <span>\u7535\u8BDD</span>\n        <div class=\"content  rt\">" + data.data.rows[0].mobile + "</div>\n    </div>\n    <div class=\"outermost\">\n        <span>\u7535\u5B50\u90AE\u4EF6</span>\n        <div class=\"content  rt\">" + data.data.rows[0].email + "</div>\n    </div>\n    <div class=\"outermost \" id=\"change-psd\">\n        <span>\u4FEE\u6539\u5BC6\u7801</span>\n        <div class=\"content  rt change-icon\"></div>\n    </div>\n    <div id=\"foot\">\n        <ul>\n            <li>\n                <a href=\"${ctx}/workplace\">\n                    <div class=\"icon icon-1\"></div>\n                    \u5DE5\u4F5C\u53F0\n                </a>\n\n            </li>\n            <li>\n                <a href=\"${ctx}/list/search-all\">\n                    <div class=\"icon icon-2\"></div>\n                    \u5217\u8868\n                </a>\n\n            </li>\n            <li>\n                <a href=\"${ctx}/defect/list\">\n                    <div class=\"icon icon-3\"></div>\n                    \u7F3A\u9677\n                </a>\n\n            </li>\n            <li>\n                <a href=\"${ctx}/my\"  class=\"color-active\">\n                    <div class=\"icon icon-4 icon-4-active\"></div>\n                    \u6211\u7684\n                </a>\n\n            </li>\n        </ul>\n    </div>\n    <div class=\"change-psd none\">\n        <div class=\"psd-freek\">\n            <div class=\"psd-text\">\n            \t<input id=\"psd1\" type=\"password\" class=\"psd-text-input\" placeholder=\"\u8BF7\u8F93\u5165\u65B0\u5BC6\u7801\" maxlength=\"8\" maxlength=\"16\" onkeyup=\"check()\">\n                <input id=\"psd2\" type=\"password\" class=\"psd-text-input\" placeholder=\"\u786E\u8BA4\u5BC6\u7801\" maxlength=\"8\" maxlength=\"16\" onkeyup=\"check()\"><br>\n                <span class=\"psd-check none\">\u5BC6\u7801\u4E0D\u4E00\u81F4</span>\n            </div>\n            <div  class=\"psd-text\">\n                <button class=\"psd-canel\" ><b style=\"color:#fff\">\u53D6\u6D88</b></button>\n                <button class=\"psd-ok\" onclick=\"save()\"><b style=\"color:#fff\">\u786E\u5B9A</b></button>\n            </div>\n        </div>\n    </div>\n    <div class=\"submit-psd none\">\n        <div class=\"submit-freek\">\n            <div class=\"submit-img none\">\u66F4\u65B0\u6210\u529F</div>\n            <div class=\"submit-img1 none\">\u66F4\u65B0\u5931\u8D25</div>\n        </div>\n    </div>\t\n";
		
		//console.log(data.data.rows[0].mobile);
		$("body").html(html); 
	}
</script>
<script type="text/javascript">
	function save(){
		if($("#psd1").val()==$("#psd2").val()&&$("#psd1").val()!=""&&$("#psd2").val()!=""){
			checkmm();
		}
	}
</script>
<script type="text/javascript">
	function checkmm(){
		console.log($("#psd1").val());
		console.log($("#psd2").val());
		var empId = ${empId};
		var id = ${id};
		var name = "${name}";
		var empName = "${empName}";
		var ctlNewPassword = $("#psd1").val();
		var ctlReNewPassword = $("#psd2").val();
		var authType = "${authType}";
		var status = "${status}";
		ajaxPost(APIS.frmUserEdit.updatepassword,{
			Mode:"Edit",
			empId:empId,
			id:id,
			name:name,
			empName:empName,
			ctlNewPassword:ctlNewPassword,
			ctlReNewPassword:ctlReNewPassword,
			authType:authType,
			status:status
  		},function(data){
  			console.log(data);
  			if(data.code == 0){
  	        	console.log(data.code);
  	            $(".submit-img").removeClass("none");
  	            setTimeout(close, 3000);
  	          	window.location.replace("${ctx}/login/h5");
  	      	}
  		});
	}
</script>
</html>