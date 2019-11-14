<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">
		 <#assign ctx=request.contextPath>
		<meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
		<meta http-equiv="X-UA-Compatible" content="ie=edge">
		<title>缺陷详情</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/common.css" />
		<link rel="stylesheet" href="https://cdn.bootcss.com/weui/1.1.2/style/weui.min.css">
		<link rel="stylesheet" href="https://cdn.bootcss.com/jquery-weui/1.2.0/css/jquery-weui.min.css">
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/defectdetail.css" />
		
	</head>

	<body>
		<h3 class="title">
		    <b class="return" onclick="back()"></b>
			<a href="javascript:;"></a>
			缺陷详情
		</h3>
		<div id="Defect">
			<form action="" method="post">
				<ul>
					<li id="item1">
						<span class="left-cre">项目</span>
						<div class="right-cre"></div>
					</li>
					<li>
						<span class="left-cre">类型</span>
						<div class="right-cre">缺陷</div>
					</li>
					<li id="item2">
						<span class="left-cre">标题</span>
						<div class="right-cre"></div>
					</li>
					<li class="liM" id="item3">
						<span class="left-cre">描述</span>
						<div class="right-cre" style="text-align: left;"></div>
					</li>
					<li class="add">
						<span class="left-cre">附件</span>
						<div class="img-cre">
							<img src="" alt="">
						</div>
					</li>
				</ul>
				
			</form>
		</div>
		<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
		<script src="https://cdn.bootcss.com/jquery-weui/1.2.0/js/jquery-weui.min.js"></script>
		<script type="text/javascript" language="javascript" src="${ctx}/view/common/assets/apis.js" ></script>
		<script type="text/javascript" language="javascript" src="${ctx}/view/common/assets/jqueryAjaxUtil.js" ></script>
		
		<script type="text/javascript">
			$(".add img").click(function(){
				$(".add input").trigger("click");
			})
		</script>
	</body>
<script>
var issueId = ${issueId};
$(function(){
	ajaxPost(APIS.frmIssueList.queryDefects, {
			eq_id:issueId
 	}, function(data) {
			console.log(data);
			load(data);
 	});

	ajaxPost(APIS.frmIssueView.query, {
		eq_issueId:issueId
	}, function(data) {
		console.log(data);
	//console.log(data.data.rows.length);
		var html="";
		var urlhead='${ctx}';
		console.log(urlhead);
		for(var i=0;i<data.data.rows.length;i++){
			var url =data.data.rows[i].fileIndexId;
			html+="<img src='"+urlhead+"/localDownload?fileId="+url+"'>";
			console.log(html);
		}
		$(".add>.img-cre").html(html);
	});
	
})
//加载数据
function load(da){
	var title = da.data.rows[0].title;
	var remark = da.data.rows[0].remark;
	var projId = da.data.rows[0].projId;
	//console.log(title);
	//console.log(remark);
	//console.log(projId);
	ajaxPost(APIS.frmIssueList.queryDefect,{
		eq_projId:projId,
		order:"desc",
		page:1,
		rows:1,
		sort:"id"
	},function(data){
		//console.log(data);
		var projName = data.data.rows[0].projName; 
		//console.log(projName);
		$("#item1 div").html(projName);
		$("#item2 div").html(title);
		$("#item3 div").html(remark);		
	});
}
//返回
function back(){  
    window.history.back(-1);  
}




</script>
</html>