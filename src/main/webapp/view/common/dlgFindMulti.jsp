<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML >
<head>
<meta http-equiv="pragma" content="no-cache">  
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="no-cache">    
<link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css" rel="stylesheet" media="screen"/>
<link href="${ctx}/view/common/css/style.css" rel="stylesheet" media="screen"/>
<link href="${ctx}/view/common/css/plugins/easyui-1.5.2/themes/icon.css" rel="stylesheet" media="screen"/>
<script  src="${ctx}/view/common/css/plugins/easyui-1.5.2/jquery.min.js"></script>
<script  src="${ctx}/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js"></script>
<script  src="${ctx}/view/common/css/plugins/easyui-1.5.2/easyui.plugins.js"></script>
<!-- 引用EasyUI的国际化文件,让它显示中文 -->
<script src="${ctx}/view/common/css/plugins/easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script  src="${ctx}/view/common/js/base.js"></script>
   <!-- js本地存储-->
<script src="${ctx }/view/common/js/store/myStorage.js" charset="utf-8"></script>
<script src="${ctx }/view/common/js/store/json2.js" charset="utf-8"></script>
<script src="${ctx }/view/common/js/store/localDB.js" charset="utf-8"></script>
<script  src="${ctx}/view/common/js/ajaxUtil.js"></script>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<body class="easyui-layout" fit="true" style="width:100%;height:100%" >
<div id='Loading'
     style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#FFFFFF ;text-align:center;padding-top: 10%;">
    <h1 style="display: inline-block;border: 1px solid #95b9e7;font-size: 16px;padding: 5px;">
        <image src='${ctx}/view/common/css/plugins/easyui-1.5.2/themes/default/images/loading.gif'/>
        <font color="#15428B" size="2">正在处理，请稍等···</font></h1>
</div>
<style> 
.input-group-inline .textbox-text {
padding-top:0px;padding-bottom:10px;
}
</style>
<script>
    function closes() {
        $("#Loading").fadeOut("normal", function () {
            $(this).remove();
        });
    }
    var pc;
    function loaded() {
        if (pc) clearTimeout(pc);
        pc = setTimeout(closes, 0);
    }
    $(document).ready(function () {
    	loaded();
    	doQuery();
    	$('#dlgName').textbox('textbox').keydown(function (e) {
            if (e.keyCode == 13) {
            	doQuery();
            }
        });	    	
        getQueryParam();
     });
	function updateClientContext(c) {
	    if (c == undefined) {
	        return;
	    }
	    clientContext = c;
	    if (clientContext.filterStatement) {
	        console.log("更新组件状态");
	        eval(clientContext.filterStatement);
	    }
	}
</script>

<div data-options="region:'north'" style="" >
		<div class="easyui-layout" data-options="fit:true" >
		<div data-options="border:false,region:'north'" style="overflow:hidden;" class="js-panel">
		<div class="btn-group" style="text-align:center;" id="group" >
		<input id="switch" class="easyui-switchbutton" data-options="onText:'精确',offText:'模糊',onChange:function (a,b){doQuery();}" style="width:60px;height:25px"  ><!--  -->
			<label for="${filterName}">检索:</label>
        	<input class="easyui-textbox" type="text"  name="${filterName}" id="dlgName"/>
        	<input type="hidden" name="id" value="${id}" />
        	<input type="hidden" name="single" value="1" /><!-- 单选 -->
        	<a href="javascript:void(0)" flat="true" class="easyui-linkbutton" style="width:60px" id="btnQuery" onclick="doQuery()">查询</a>
		</div>
  		<span class="radioSpan">
		<input type='radio' name='op' id='op1' value='1' checked="checked" style="width:30px;" class='easyui-validatebox' required='true'><span>全部</span>
		<input type='radio' name='op' id='op2' value='2' style='width:30px;display:${displayOp2}' onClick="clearRow()"  class='easyui-validatebox' required='true'>
		<span style="display:${displayOp2}">未设置</span>
		<input type='radio' name='op' id='op3' value='3' style='width:30px;display:${displayOp3}' onClick="clearRow()" class='easyui-validatebox' required='true'>
		<span style="display:${displayOp3}">已设置</span>
		<input type='radio' name='op' id='op4' value='4' style='width:30px;display:${displayOp4}' onClick="clearRow()" class='easyui-validatebox' required='true'>
		<span style="display:${displayOp4}">重置</span>
		</span>		
  		</div>
</div>
</div>
<div data-options="region:'center'" >
 <table  class="easyui-datagrid"  checkbox="true" rownumbers="true" 
 				region="center" 
				name="pnlDg" id="pnlDg" 
				data-options="
				fit:true,
				toolbar:'#toolbar',
				fitColumns:true,
				singleSelect:true,
				pagination:true,selectOnCheck:false,checkOnSelect:false,
				columns:[${columns}],
				nowrap:false"> 
		<thead>
			<tr>
				<th data-options="field:'id',sortable:true,halign:'center',width:120" >编码</th>
				<th data-options="field:'name',sortable:true,halign:'center',width:120" >名称</th>
			</tr>
		</thead>
</table> 
</div>
<div data-options="region:'south'" >
	<div class="easyui-layout" data-options="fit:true" >
	<div data-options="border:false,region:'south'" style="overflow:hidden;" class="js-panel">
	<div class="btn-group" style="text-align:right;">
	<a class="easyui-linkbutton" href="javascript:void(0)" 
		onClick="doConfirm()"  
		id="pButton_save" name="save"
		style="margin-right:10px"
		>确认</a>
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		onClick="javascript:closeDialog();"
		id="pButton_cancel" name="cancel"
		style="margin-right:10px"
		>取消</a>	
	
<!--     <a href="javascript:void(0)" class="easyui-linkbutton  l-btn l-btn-small" style="width:60px" id="btnConfirm" onClick="doConfirm()" >确认</a> -->
<!--     <a href="javascript:void(0)" class="easyui-linkbutton  l-btn l-btn-small" style="width:60px" id="btnCancel" onClick="javascript:closeDialog();">取消</a> -->
	</div>
	</div>
	</div>
</div>	
</body>
<script> 
var retRow;//选中行
var selected = false;
var selectType = 1;//选项类型,1为表格中的项;2未设置;3重置

var realMerge = function (to, from) {
    for (n in from) {
        if (typeof to[n] != 'object') {
            to[n] = from[n];
        } else if (typeof from[n] == 'object') {
            to[n] = realMerge(to[n], from[n]);
        }
    }
    return to;
};

$(function(){
	doQuery();
});
//清除表格选中行
function clearRow(){
	$('#pnlDg').datagrid('clearSelections');   
}
var paramIn = {};
function getQueryParam(name) {
	var root = getRoot();
	var obj =  root.$('#tmpSearcher').dialog('options');
	var queryParams = obj["queryParams"];
	console.log( JSON.stringify(queryParams));
	paramIn = queryParams;
}

$(window).resize(function(){  
    $("#pnlDg").datagrid('resize',{width:parent.$('.layout-panel-center').width()-mwidth}); //收缩引起window resize,重新计算值，并调用resize方法。  
});  

function doQuery(){
	var url = '${ctx}/api/commonquery';
//	var p = parent.$('#tmpSearcher');
//	var paramIn = {};
//	if("${In}"){
//		paramIn = parent.$('#${In} :input,hidden').serializeJson();	
//	}
	
	var isFuzzy = !($('#switch').switchbutton("options").checked);
	
	var params = $("#pnlDg").datagrid('options').queryParams;
	params['token'] = "${token}";
	params['id'] = "${id}";//Find组件当前值(常用于在筛选结果中排除)
	params['${filterName}'] = $('#dlgName').textbox("getValue");
	if(isFuzzy){
		params['filterName'] = "${filterName}";
	}else{
		params['filterName'] = "${filterNameAcurate}";
		params['${filterName}'] = '';
		params['${filterNameAcurate}'] = $('#dlgName').textbox("getValue");
	}

	$.extend(params,paramIn);
	$("#pnlDg").datagrid({
		loader:function(params,success,error){
			ajaxPost(url,params,function(res){
				$('#dlgName').textbox('textbox').focus(); 
				var dgData = res.data;
				dgData['columns'] = [${columns}];
				success(dgData);
			});
		}
	});
	 var p = $('#pnlDg').datagrid('getPager');  
	    if (p){  
	           $(p).pagination({  
	             beforePageText: '',    
	             afterPageText: '/ {pages}',    
	             displayMsg: '共 {total}条',    
	           });  
	     }  
}
if('${op2}'){
	var op2 = '${op2}';
}
if('${op3}'){
	var op3 = '${op3}';
}
if('${op4}'){
	var op4 = '${op4}';
}
function closeDialog(){
	parent.$('#tmpSearcher').dialog('close');
}

function doConfirm(){
	var opVal = $('input[name=op]:checked').val();
	if(opVal == undefined) return;
	selectType = opVal;
	if(opVal=="1"){
		var row = $('#pnlDg').datagrid('getChecked');
		if(row){
			retRow = row;
			selected = true;
			closeDialog();
		}
	}else{//未设置,已设,重置
		retRow = eval("("+eval("op"+opVal)+")");//取选项值对应的JSON对象
		retValue = opVal;
		selected = true;
		closeDialog();
	}
};

</script>
</html>