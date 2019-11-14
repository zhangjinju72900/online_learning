<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
		<meta http-equiv="X-UA-Compatible" content="ie=edge">
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/common.css" />
		<link rel="stylesheet" href="https://cdn.bootcss.com/weui/1.1.2/style/weui.min.css">
		<link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.0/css/jquery-weui.min.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/addDefect.css" />
		<style>
			.addimg{
				/*width: 80%;*/
				/*height: 100px;*/
				width: 500px;
				margin: 0 auto;
				display: block;
			}
		</style>
	</head>

	<body>
		<div class="modal-full-screen none">
		   <div>
		    <p>提交成功</p>
		   </div>
  		</div>
		<h3 class="title">
			<a href="javascript:;" onclick="back()">
				
			</a>
			新增缺陷
		</h3>
		<div id="Defect">
			<form  method="post">
				<input id="fileId" value="" type="hidden"/>
				<ul>
					<li>
						<span class="left-cre">项目</span>						
						<select name="" id="select1">
							
						</select>
					</li>
					<li>
						<span class="left-cre">类型</span>
						<input type="text" value="缺陷" disabled/>
					</li>
					<li>
						<span class="left-cre">标题</span>
						<input type="text" id="select2"/>
					</li>
					<li>
						<span class="left-cre">重要程度</span>						
						<select name="" id="selecta">
							<option value="blocker">严重</option>
							<option value="critical">重要</option>
							<option value="major">一般</option>
							<option value="minor">轻微</option>
						</select>
					</li>
					<li class="liM">
						<span class="left-cre">描述</span>
						<textarea id="select3"></textarea>
					</li>
					
					<li class="add">
						<span class="left-cre">附件</span>
						<input type="file" accept="image/*" mutiple="mutiple" capture="camera" id="camera" onchange="readImg(this)" name="camera"/>
						<input type="file" accept="image/*" mutiple="mutiple" id="album" onchange="readImg(this);" name="album"/>
						<img src="${ctx}/view/common/assets/h5/img/camera.png" />
						
					</li>
				</ul>
				<div id="submit">
					<button type="button" onclick="save()">保存</button>
					<button type="reset" onclick="back()">取消</button>
				</div>
			</form>
		</div>
		<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
		<script src="https://cdn.bootcss.com/jquery-weui/1.2.0/js/jquery-weui.min.js"></script>
		<script type="text/javascript" language="javascript" src="${ctx}/view/common/assets/apis.js" ></script>
		<script type="text/javascript" language="javascript" src="${ctx}/view/common/assets/jqueryAjaxUtil.js" ></script>
		<script src="${ctx}/view/common/js/ajaxfileupload.js?${date}"></script>
		<script type="text/javascript">
			$(".add img").click(function() {
				//				$(".add input").trigger("click");
				$.actions({
					actions: [{
						text: "拍照",
						onClick: function() {
							$("#camera").trigger("click");
							
						}
					}, {
						text: "从图库选择",
						onClick: function() {
							$("#album").trigger("click");
						}
					}]
				});
			})	
		</script>
	</body>
<script>
$(function(){
	//console.log(APIS.frmCustomerList.queryCustomer);	
	ajaxPost(APIS.frmIssueList.queryDefect,{
		eq_issueStatus:["open", "workin", "reopen"],
		eq_issueType:"bug"
		},function(data){
			//console.log(data);
			unique1(data);
		});
})
//去重
function unique1(array){ 
	//console.log(array.data.rows.length)
	var n = []; //一个新的临时数组 
	var m = [];
	//遍历当前数组 
	for(var i = 0; i < array.data.rows.length; i++){ 
		//如果当前数组的第i已经保存进了临时数组，那么跳过， 
		//否则把当前项push到临时数组里面 
		if (n.indexOf(array.data.rows[i].projName) == -1) n.push(array.data.rows[i].projName);
		if (m.indexOf(array.data.rows[i].projId) == -1) m.push(array.data.rows[i].projId);
	} 
	//console.log(n);
	//console.log(m);
	disrepeat(n,m);
}
//加载项目名称
function disrepeat(data,num){
	
	var html="";
	for(var i=0;i<data.length;i++){
		
		html+="<option value="+num[i]+">"+data[i]+"</option>";
	}
	$("#select1").html(html);
}

//保存
function save(){
	//console.log($("#select2").val());
	//console.log($("#select3").val());
	//console.log($("#select1>option").val());
	//console.log($("#selecta>option").val());
	 var date = new Date();
        var seperator1 = "-";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
      if (month >= 1 && month <= 9) {
         month = "0" + month;
      }
     if (strDate >= 0 && strDate <= 9) {
         strDate = "0" + strDate;
      }
     var h=date.getHours();       //获取当前小时数(0-23)
     var m=date.getMinutes();     //获取当前分钟数(0-59)
     var s=date.getSeconds();     //获取当前秒数
       //提价时间
     var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate +" "+ h + ":" + m + ":" + s; 
 
	console.log(currentdate);
	
	var title = $("#select2").val();
	var remark = $("#select3").val();
	var projId = $("#select1>option:selected").val();
	var priority = $("#selecta>option:selected").val()
	var empId = ${empId};
	console.log(projId);
	console.log(empId);
	ajaxPost(APIS.frmIssueBugAdd.saveIssueBug, {
		Mode:"Add",
		projId:projId,
		type:"bug",
		title:title,
		remark:remark,
		priority:priority,
		status:"open",
		create_by:empId,
		create_time:currentdate
	 }, function(data) {
	    console.log(data);
	    photo(data);
	    if(data.msg=="保存成功"){
	    	 $(".modal-full-screen p").html("保存成功");
	    	 $(".modal-full-screen").toggleClass("none");
	    	var timer=setTimeout(function(){
		    	$(".modal-full-screen").toggleClass("none");
		    	clearTimeout(timer);
		    	timer=null;
		     location.href="${ctx}/workplace"; 
		    },1000);
	    }else{
	    	 $(".modal-full-screen p").html("请填写完整");
	    	 $(".modal-full-screen").toggleClass("none");
		    	var timer=setTimeout(function(){
			    	$(".modal-full-screen").toggleClass("none");
			    	clearTimeout(timer);
			    	timer=null;
			    /*  location.href="${ctx}/workplace";  */
			    },1000);
	    }
	 });
}
//保存附件
function photo(data){
	var ctlIssueId = data.data.id;	
	var fileIndexId = $("#fileId").val();
	console.log(ctlIssueId);
	console.log(fileIndexId);
	ajaxPost(APIS.issueFileAdd.saveissueFile, {
		Mode:"Add",
		ctlIssueId:ctlIssueId,
		ctlfileId:fileIndexId
	 }, function(data) {
	    
		 
	 });
}

function settime(fileid,token){
	$.ajaxFileUpload({  
	    url:'${ctx}/localUpload?token='+token,  
	    data:{accessType:"public",allowFile:"jpg,jpeg,gif,png,bmp",maxSize:"9999999",uploadUrl:""},
	    secureuri:false,  
	    fileElementId:fileid,//file标签的id  
	    dataType: 'json',//返回数据的类型   
	    success: function (data) {
	    	if(data.status!=0){
				$("#fileId").val(data.status);
		    	alert('上传成功');
	    	}
	    },
	    error: function (data) {  
	        alert('文件上传失败，请重新上传');  
	    }  
	});
}

function uploadFile(file){
	var API_PROXY = '';
	var token = '';
	var tokenKey = 'OnClick_pnlGroup_fileIndexId_issueFileAdd_fileUpload';
	var uiName = "issueFileAdd";
	var fileid = file.id;
	console.log(file.id);
	$.ajax({
	      type: 'GET',
	      url:  API_PROXY + '/ui/' + uiName + '/app',
	      success: function(data) {;
	        if (data && data.token && data.token.items) {
	          var token = data.token.items[tokenKey];
	          if (token) {
	            console.log(token);
	            settime(fileid,token);
	          } else {
	            console.log('------------------------token的UiName配置错误-------------------');
	          }
	        } else {
	          console.log('------------------------token列表获取失败-----------------------');
	        }
	      }
	    });
	console.log(token);
}


function readImg(file) {
	if(file.files && file.files[0]) {
		var reader = new FileReader();
		reader.onload = function(evt) {
			$("#Defect>form>ul").append("<img class='addimg' src="+evt.target.result+">");
		}
		reader.readAsDataURL(file.files[0]);
		uploadFile(file);
	} else {
		
	}
}
//返回
function back(){  
	 window.location.replace("${ctx}/workplace"); 
}
</script>
</html>