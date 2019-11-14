<!DOCTYPE html>
<html>

	<head>
	 <script src="${ctx}/view/common/js/jquery-1.8.3.min.js"></script>
 <#include "pc/common/base.ftl">
		<meta charset="UTF-8">
		<meta name="viewport" content="viewport-fit=cover, width=750, user-scalable=no">
		<meta http-equiv="X-UA-Compatible" content="ie=edge">
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/common.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/listSearch.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/view/common/assets/h5/css/foot.css" />
		<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
		<title>缺陷列表</title>
	</head>

	<body>
		<div id="app">
			<h2 id="title">缺陷列表</h2>
			<div id="inputsearch">
				<div class="div-active"></div>
				<form action="" onsubmit="searchList();return false;">
					<input type="text" placeholder="请输入编号" id="number"/>
				</form>
				
			</div>
			<div class="list-item clear">
				<!--这个a标签放整个li点击后跳转的地址-->
				
				
				
			</div>

		</div>
		<div id="foot">
			<ul>
				<li>
					<a href="${ctx}/workplace" >
						<div class="icon icon-1 "></div>
						工作台
					</a>

				</li>
				<li>
					<a href="${ctx}/list/search-all" >
						<div class="icon icon-2"></div>
						列表
					</a>

				</li>
				<li>
					<a href="${ctx}/defect/list" class="color-active">
						<div class="icon icon-3 icon-3-active"></div>
						缺陷
					</a>

				</li>
				<li>
					<a href="${ctx}/my">
						<div class="icon icon-4"></div>
						我的
					</a>

				</li>
			</ul>
		</div>

	</body>
	<script src="${ctx}/view/common/assets/apis.js"></script>
	<script src="${ctx}/view/common/assets/jqueryAjaxUtil.js"></script>
		<script type="text/javascript">
			/* $("#inputsearch input").focus(function() {
				$("#inputsearch div").addClass("div-active");
			});
			$("#inputsearch input").blur(function() {
				if($("#inputsearch input").val() == "") {
					$("#inputsearch div").removeClass("div-active");
				}
			}); */
			
			function searchList(){
//				这里放ajax
				ajaxPost(APIS.frmIssueList.queryDefect,{
					eq_issueType:"bug",
					eq_issueId:$("#number").val(),
					sort:"issueId",
					order:"desc"
					},function(data){
						console.log(data);
						render1(data);
					});
			}
			
			 function render1(data) {

			  	 var html="";
				//html+="<ul>"
				for (var i = 0; i < data.data.rows.length; i++) {
					
					html += "\n<a href=\"${ctx}/defect/detail-"+data.data.rows[i].issueId+"\"><div class=\"list-item-title\"><span class=\"lf\">" + data.data.rows[i].title + "</span><span class=\"rt\">" + data.data.rows[i].issueId + "</span></div><div class=\"list-item-content clear\">" + data.data.rows[i].remark + "</div><div class=\"list-item-foot clear\"><span class=\"lf\"><img src=\"${ctx}/view/common/assets/h5/img/solved.png\"/><span>" + data.data.rows[i].issueStatusName + "</span></span><span class=\"lf\"><img src=\"${ctx}/view/common/assets/h5/img/task.png\"/><span>" + data.data.rows[i].issueTypeName + "</span></span></div><div class=\"clear\"></div></a>\n";
				
				}
				//html+="</ul>";
				
				 $("#app>.list-item").html(html);
			  		    
			  		    }
		</script>
<script type="text/javascript">
	//console.log(1);
	$(function(){
		//console.log(APIS.frmCustomerList.queryCustomer);	
		ajaxPost(APIS.frmIssueList.queryDefect,{
			eq_issueType:"bug",
			eq_issueId:$("#number").val(),
			sort:"issueId",
			order:"desc"
  		},function(data){
  			console.log(data);
  			render1(data);
  		});
	})

	
 
	 function render1(data) {

			  	 var html="";
				//html+="<ul>"
				for (var i = 0; i < data.data.rows.length; i++) {
					
					html += "\n<a href=\"${ctx}/defect/detail-"+data.data.rows[i].issueId+"\"><div class=\"list-item-title\"><span class=\"lf\">" + data.data.rows[i].title + "</span><span class=\"rt\">" + data.data.rows[i].issueId + "</span></div><div class=\"list-item-content clear\">" + data.data.rows[i].remark + "</div><div class=\"list-item-foot clear\"><span class=\"lf\"><img src=\"${ctx}/view/common/assets/h5/img/solved.png\"/><span>" + data.data.rows[i].issueStatusName + "</span></span><span class=\"lf\"><img src=\"${ctx}/view/common/assets/h5/img/task.png\"/><span>" + data.data.rows[i].issueTypeName + "</span></span></div><div class=\"clear\"></div></a>\n";
				
				}
				//html+="</ul>";
				
				 $("#app>.list-item").html(html);
			  		    
			  		    }
	
</script>
</html>