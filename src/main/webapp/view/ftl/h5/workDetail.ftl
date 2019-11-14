<!DOCTYPE html>
<html >
<head>
    <title>工作项详情</title>
    <meta charset="UTF-8">
    <script src="${ctx}/view/common/js/jquery-1.8.3.min.js"></script>
 <#include "pc/common/base.ftl">
    <meta charset="UTF-8">
    <meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/work-detail.css"> 
    <link rel="stylesheet" href="${ctx}/view/common/assets/h5/css/common.css">
    <!-- <script src="./jquery-1.9.1.min.js"></script> -->
</head>
	<body>
       <!-- 工作项详情 -->
	
     	<script src="${ctx}/view/common/assets/h5/js/work-detail.js"></script>
	</body>
	<script src="${ctx}/view/common/assets/apis.js"></script>
	<script src="${ctx}/view/common/assets/jqueryAjaxUtil.js"></script>
	<script type="text/javascript">
	$(function(){
		//console.log(APIS.frmCustomerList.queryCustomer);	

		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.queryDefect,{
			eq_issueId:issueId
			},function(data){
				console.log(data);
				render1(data);
			});
		

	})



	  function render1(data) {

	  	 var html='<div id="bg" class="over"></div><div class="normal-one"><div class="normal-left lf" onclick="back()"><img src="${ctx}/view/common/assets/h5/img/show.png"></div><div class="title">工作项详情</div></div><div id="change" class="over">工作项状态已改变</div>';
		//html+="<ul>"
		for (var i = 0; i < data.data.rows.length; i++) {
			
			html += "<div class=\"normal-one\"><div class=\"normal-left lf\">\u5DE5\u4F5C\u9879\u7F16\u53F7</div><div class=\"normal-right rt\">" + data.data.rows[i].issueId + "</div></div><div class=\"normal-one\"><div class=\"normal-left lf\">\u9879\u76EE</div><div class=\"normal-right rt\">" + data.data.rows[i].projName + "</div>\n</div><div class=\"normal-two\"><div class=\"two-left lf\">\u6807\u9898</div><div class=\"two-right rt\">" + data.data.rows[i].title + "</div></div><div class=\"normal-two\"><div class=\"two-left lf\">\u63CF\u8FF0</div><div class=\"two-right rt\" style=\"width:550px;\">" + data.data.rows[i].remark + "</div></div><div class=\"normal-one\"><div class=\"normal-left lf\">\u7C7B\u578B</div><div class=\"normal-right rt\">" + data.data.rows[i].issueTypeName + "</div></div><div class=\"normal-one\"><div class=\"normal-left lf\">\u91CD\u8981\u7A0B\u5EA6</div><div class=\"normal-right rt\">" + data.data.rows[i].priorityName + "</div></div><div class=\"normal-one\"><div class=\"normal-left lf\">\u521B\u5EFA\u4EBA</div><div class=\"normal-right rt\">" + data.data.rows[i].createByName + "</div></div><div class=\"normal-one\" style=\"margin-bottom:200px;\"><div class=\"normal-left lf\">\u521B\u5EFA\u65F6\u95F4</div><div class=\"normal-right rt\">" + data.data.rows[i].createTime + "</div></div>\n";
		
		}
		
		//权限判断
		
		//html+="</ul>";
		var status = data.data.rows[0].issueStatus;
		var reopen = data.data.rows[0].reopen;
		var cancel = data.data.rows[0].cancel;
		var start = data.data.rows[0].start;
		var resolve = data.data.rows[0].resolve;
		var test = data.data.rows[0].test;
		var close = data.data.rows[0].close;
		console.log(status);
		
		if(status=="test"){

			if(close=="关闭工作"){
				html += '<div id="wkb"><button id="test-close" class="lf" onclick="closework()">关闭工作</button>';
 		 		
			}
			if(reopen=="重新打开"){
  		    	html += '<button id="test-reopen" class="rt" onclick="reopen()">重新打开</button>';
  		    	}
			
			if(cancel=="取消工作"){
  		    	html += '<button id="test-cancel" class="rt" onclick="cancelcheck()">取消工作</button></div>';
  		    	}
			 $("body").html(html);
	  		    
	  		    }else if(status=="open"){
	  		    	if(start=="开始工作"){
	  		    		html += '<div id="wkb"><button id="open-start" class="lf" onclick="startcheck()">开始工作</button>';
	  		    	}
	  		    	if(cancel=="取消工作"){
	  		    	html += '<button id="open-cancel" class="rt" onclick="cancelcheck()">取消工作</button></div>';
	  		    	}$("body").html(html);
	  		    }else if(status=="reopen"){
	  		    	if(start=="开始工作"){
	  		    		html += '<div id="wkb"><button id="open-start" class="lf" onclick="startcheck()">开始工作</button>';
	  		    	}
	  		    	if(cancel=="取消工作"){
	  		    	html += '<button id="open-cancel" class="rt" onclick="cancelcheck()">取消工作</button></div>';
	  		    	}
	  		    	$("body").html(html);
	  		    }else if(status=="workin"){
	  		    	if(resolve=="解决工作"){
	  		    		html += '<div id="wkb"><button id="open-start" class="lf" onclick="resolve()">解决工作</button>';
		  		 		 
	  		    	}
	  		    	
	  		    	if(cancel=="取消工作"){
		  		    	html += '<button id="open-cancel" class="rt" onclick="cancelcheck()">取消工作</button></div>';
		  		    	}
	  		    	$("body").html(html);
	  		    }else if(status=="resolve"){
	  		    	if(test=="开始测试"){
	  		    		html += '<div id="wkb"><button id="open-start" class="lf" onclick="starttest()">开始测试</button>';
		  		 		 
	  		    	}
	  		    	if(cancel=="取消工作"){
		  		    	html += '<button id="open-cancel" class="rt" onclick="cancelcheck()">取消工作</button></div>';
		  		    	}
	  		    	$("body").html(html);
	  		    }else if(status=="close"){
	  		    	if(reopen=="重新打开"){
	  		    	html += '<div id="wkb"><button id="reopen" class="rt" onclick="reopen()">重新打开</button></div>';
	  		    	}
	  		 		 $("body").html(html);
	  		    }else if(status=="cancel"){
	  		    	$("body").html(html);
	  		    }else{
	  		    	$("body").html(html);
	  		    }
	}
	</script>
	<script type="text/javascript">
	function startcheck(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.startWork,{
			Mode:"Edit",
			id:issueId,
			status:"workin"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	<script type="text/javascript">
	function cancelcheck(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.cancelWork,{
			Mode:"Edit",
			id:issueId,
			status:"cancel"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	
		<script type="text/javascript">
	function starttest(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.startTest,{
			Mode:"Edit",
			id:issueId,
			status:"test"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	
		<script type="text/javascript">
	function closework(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.closeWork,{
			Mode:"Edit",
			id:issueId,
			status:"close"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	
	<script type="text/javascript">
	function reopen(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.reopen,{
			Mode:"Edit",
			id:issueId,
			status:"reopen"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	
	<script type="text/javascript">
	function resolve(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		var issueId = ${issueId};
		ajaxPost(APIS.frmIssueList.resolve,{
			Mode:"Edit",
			id:issueId,
			status:"resolve"
			},function(data){
				console.log(data);
				
			});
		$("#bg").removeClass("over");
		$("#change").removeClass("over");
		$("#bg").addClass("show");
		$("#change").addClass("show");
		setTimeout(" window.history.back(-1)", 3000 );
	}



	 
	</script>
	

	
</html>