
<!DOCTYPE HTML >
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="no-cache">
    <meta http-equiv="expires" content="-1">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
	<!-- 动态引用easyui样式 -->
          	           <!-- 标准 easyui / style 样式1 -->
			   <link href="/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css?20190125" rel="stylesheet" media="screen"/>
			   <link href="/view/common/css/style.css?20190125" rel="stylesheet" media="screen"/>
    <link href="/view/common/css/imageView.css?20190125" rel="stylesheet" media="screen"/>
    <link href="/view/common/css/plugins/easyui-1.5.2/themes/icon.css?20190125" rel="stylesheet" media="screen"/>
    <script type="text/javascript">var ctx = "";</script>
    <script src="/view/common/js/jquery-1.8.3.min.js?20190125"></script>
    <script src="/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js?20190125"></script>
    <script src="/view/common/css/plugins/easyui-1.5.2/easyui.plugins.js?20190125"></script>
    <script src="/view/common/js/Common.js?20190125"></script>
    <!-- 引用EasyUI的国际化文件,让它显示中文 -->
    <script src="/view/common/css/plugins/easyui-1.5.2/locale/easyui-lang-zh_CN.js?20190125" type="text/javascript"></script>
    <script src="/view/common/js/base.js?20190125"></script>
    <script src="/view/common/js/MD5.js?20190125"></script>
    <script src="/view/common/js/jsExpression.js?20190125"></script>

    <!-- js本地存储-->
    <script src="/view/common/js/store/myStorage.js?20190125" charset="utf-8"></script>
    <script src="/view/common/js/store/json2.js?20190125" charset="utf-8"></script>
    <script src="/view/common/js/store/localDB.js?20190125" charset="utf-8"></script>
    <!-- ajax请求-->
    <script src="/view/common/js/ajaxUtil.js?20190125"></script>
	
	
         
         <script src="/view/common/js/group.js?20190125"></script>
         <script src="/view/common/js/datagrid.js?20190125"></script>
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
    </script>
    
    <script>
    //禁止浏览器后退
(function (global) {
	if(typeof (global) === "undefined")
	{
		throw new Error("window is undefined");
	}

    var _hash = "!";
    var noBackPlease = function () {
        global.location.href += "#";

		// making sure we have the fruit available for juice....
		// 50 milliseconds for just once do not cost much (^__^)
        global.setTimeout(function () {
            global.location.href += "!";
        }, 50);
    };
	
	// Earlier we had setInerval here....
    global.onhashchange = function () {
        if (global.location.hash !== _hash) {
            global.location.hash = _hash;
        }
    };

    global.onload = function () {
    	foo();
		noBackPlease();

		// disables backspace on page except on input fields and textarea..
		document.body.onkeydown = function (e) {
            var elm = e.target.nodeName.toLowerCase();
            if (e.which === 8 && (elm !== 'input' && elm  !== 'textarea')) {
                e.preventDefault();
            }

        	var theEvent = e || window.event;    
	        var code = theEvent.keyCode || theEvent.which || theEvent.charCode;    
	        if (code == 13) {    
	          	
	        }    
            
            // stopping event bubbling up the DOM tree..
            e.stopPropagation();
        };
		
    };

})(window);

    //禁止浏览器后退
    function foo(){  
    var items=$('.textbox-text');  
    var item=null;  
    for(var i=0;i<items.length;i++){  
         item=items[i];  
        (function () {
           var next=(i+1) < items.length ? i+1 : 0 ;  
           item.onkeydown=function(event){
             var eve=event ? event : window.event;
             if((eve.keyCode==13) && (event.ctrlKey)){  //ctrl+ enter 换行
				event.srcElement.value += "\n";
             }else if(eve.keyCode==13){  //enter 回车
				//$(items[next]).focus(); 
             } 
         }  
         })();  
    }     
}  
</script>
    
    
</head>
<body class="easyui-layout js-ui" title="班级scorm成绩" id="frmClassScorm" style="width:100%;height:100%;background-color:white;">

<div id='Loading'
     style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#FFFFFF ;text-align:center;padding-top: 10%;">
    <h1 style="display: inline-block;border: 1px solid #95b9e7;font-size: 16px;padding: 5px;">
        <image src='/view/common/css/plugins/easyui-1.5.2/themes/default/images/loading.gif'/>
        <font color="#15428B" size="2">正在处理，请稍等···</font></h1>
</div>

<input type="hidden" id="from" value="menu">
<input type="hidden" id="code" value="265">
<input type="hidden" id="uiid" value="b2d469abad204ecbb4087f4485e43d36">
<input type="hidden" id="puid" value="54cc349973f84126b41be9b11737ffbe">
<style>
    .input-group-inline .textbox-text {
        padding-top: 0px;
        padding-bottom: 10px;
    }
    .input-group-inline textarea {
            font-family: Arial;
        }
    .readgrey{border:1px solid #eaeaea;background:#f9f9f9}
</style>
<!--begin-->
<!--North begin-->
<div data-options="region:'north'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
        
                    <div data-options="border:false,region:'north'" style="
                        overflow:hidden;
                                                                        
                        
                            " class="js-panel" trigger="">
<div class="btn-group" style="text-align:right;">
<div style="float: left;line-height: 29px;" class="js-path"></div>
	
		
		
		
		
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnSearch_frmClassScorm()" 
		id="pToolbar_btnSearch" name="btnSearch"
		style="margin-right:10px;

		


		width:40px;
		
"
		>搜索</a>
		
		
	
		
		
		
		
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pToolbar_btnBack_frmClassScorm()" 
		id="pToolbar_btnBack" name="btnBack"
		style="margin-right:10px;

		


		width:40px;
		
"
		>返回</a>
		
		
</div>

                    </div>
    </div>
</div>
<!--North end-->
<!--Center begin-->
<div data-options="region:'center'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
        
                    <div data-options="border:false,region:'north'" style="
                        overflow:hidden;
                                                                        
                        
                            " class="js-panel" trigger="">
<div name="pCondition" id="pCondition" style="padding-top:15px;"
     class="js-group">
	<input type="hidden" name="classId" id="pCondition_classId">
	<div class="input-group-inline" >
	    <label style="margin-left :3px;">请选择课程：</label>
    	<select class="easyui-combobox js-input"  width-data="0.333" style="width:2px;height:22px" tipPosition="bottom"
    	 
    	id="pCondition_courseId" name="courseId" data-options="editable:false, 
    	">
		</select>
    	<script>
	    	$('#pCondition_courseId').combobox({
				onChange: function changePercent(value)  {
				//修正comboBox多选时清空所选内容会保留最后一次所选bug
				if(value!=null){
					if(value==''){
						$(this).combobox('setValue', []);
					}else if(Object.prototype.toString.call(value) == '[object Array]' ){
						if(value == 'YorN'){	
							$(this).combobox('setValues',[]);
				        	var datas = $(this).combobox('getData');
				        	var values = new Array();
				        	for (var i=1;i<datas.length;i++){
								values[i-1] = datas[i].value;
							}
							if($(this).combobox('getValues') == values){
								$(this).combobox('setValue', values);
							}else{
								$(this).combobox('setValue', []);
							}
							$(this).combobox('setValues',values);
						}else if(value.indexOf('YorN') != -1){
							$(this).combobox('setValue', []);
						}
					}
				}
				  try {
					eval('pCondition_courseId_OnSelect()');
				  } catch(e) {}
				},
				
	    	}); 
		</script>   
	</div>
	
		<br>
</div>

                    </div>
        
                    <div data-options="border:false,region:'center'" style="
                        
                                                                        
                        
                            " class="js-panel" trigger="">
<script>
	function opFormatter_pTable(val,row,index){
		GridButtonContent = "";
		var html = "";
		if(!"id"){
			console.log("grid可能没有配置Object属性或者Object没有主键？");
			var id='';
		} else {
			var id=row['id'];
		}
		
		return html;
	}
	//用于拼接操作列链接时简化调用 。否则容易引起运行时刻的easyui解析错误（多空格等）造成的语句无法成功执行
	function setId(index,id){
		setStorage('EditIndex', index);
		setPanelId("pTable", id);
	}
	
	
		var editIndex_pTable = undefined;
		function endEditing_pTable(dg){
			if (editIndex_pTable == undefined){return true}
			if (dg.datagrid('validateRow', editIndex_pTable)){
					dg.datagrid('endEdit', editIndex_pTable);
				editIndex_pTable = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickRow_pTable(index, dg){
			//if (editIndex_pTable != index){
				if (endEditing_pTable(dg)){
					dg.datagrid('selectRow', index);
							dg.datagrid('beginEdit', index);
					editIndex_pTable = index;
				//} else {
					//dg.datagrid('selectRow', editIndex_pTable);
				//}
			}
		}
</script>
 

 <table  class="easyui-datagrid"  
				name="pTable" id="pTable" style="width:99.9%;height:90.8%" rownumbers="true" pagination="true"
				> 
						
</table> 
<div id="tb_pTable" style="padding:5px;height:auto">
	<div style="margin-bottom:0px">
	</div>
</div>


                    </div>
    </div>
</div>
<!--Center end-->
<!--end-->
</body>
<script>
    var frmDlg = parent.document.getElementById('frmDlg');
    if (frmDlg && '' == 'true') {
        var height = 0;
        $(frmDlg.contentWindow.document.body).find(".js-panel").each(function () {
            height = height + $(this).height();
        })
        parent.$("#tmpDlg").height(height);
        parent.$(".window-shadow").remove();
    }    
    var clientContext = {"uiName":"frmClassScorm","items":{"OnClick_pToolbar_btnBack_frmClassScorm_proTransition":"cf2f12d74db849748c130a1e692dafe4","OnLoad__frmClassScorm_jQuery":"44b29370541d474f8a26e5f549226ae6","OnClick_pToolbar_btnSearch_frmClassScorm_Query":"1f2c7ebb3bc041be90d94194d23552a9"}};
    //auth control
    $(document).ready(function () {
		$('#pToolbar_btnSearch').linkbutton('enable') ;
 $('#pToolbar_btnBack').linkbutton('enable');
		cus_resize_init_input();//comment out this line if your wanna use a special style
    
        setReadonly();
   		
        changeTab();
        // 记录路径 如果从菜单 快捷菜单 或者首页跳转记录路径
        if(!'Transition'){
	        var path='menu'+$("#code").val();
	        var arr = new Array();
	        var arrTitle = new Array();
			arr.push($(".js-ui").attr("id"));
			arrTitle.push($(".js-ui").attr("title"));
			setStorage(path, arr);
			setStorage(path+"_name", arrTitle);
        }
        if('Transition'!='Popup'){
	        var path='menu'+$("#code").val();
	        var text='<img src="/view/common/images/hrefLogo.jpg" height="13" width="13" style="float:left;margin-top:8px" > </img>';
			for (var i=0;i<getStorage(path+"_name").length;i++){
				if(i==getStorage(path+"_name").length-1){
				  text+='<a>'+getStorage(path+"_name")[i]+'</a>';
				} else {
				  text+='<a>'+getStorage(path+"_name")[i]+'</a> &gt;';
				}
		  	}
	        $(".js-path").html(text);
        }
        
    });

    function changeTab(){

        if($('#tab').tabs(0).find("[class$='Chart']")) {
            $('#tab').tabs(0).find("[class$='Chart']").each(function(i,obj){
                var c = eval($(this).attr("id"));
                c.resize();
            });
        }
        $('#tab').tabs({
            onSelect: function (title, index) {
                $("[class$='Chart']").each(function(i,obj){
                   var chart = eval($(this).attr("id"));
                    chart.resize();
                });
            }

        });
    }

    function updateClientContext(c) {
        if (c == undefined) {
            return;
        }
        if(c.items){
       	 	clientContext = c;
        }else if(c.token && c.functionId){
        	clientContext.items[c.functionId] = c.token;
        	console.log("已更新"+c.functionId+"的token值");
        }
        if (c.filterStatement) {
            eval(c.filterStatement);
        }
    }

    //onLoad事件中调用方法时不加token
    function getToken(key) {
        var token = clientContext.items[key];
        if (token == undefined) {
        	token = "";
		}else{
        	delete clientContext.items[key];
        }
        return token;
    }

	//validateException...
    function restoreToken(key,value) {
        clientContext.items[key] = value;
    }
    
    //初始化生成input宽度
    function cus_resize_init_input() {
        $('.js-panel').each(function () {
        if($(this).children().eq(0).attr('class')=='js-group'){
            var this_width = ($(this).width() - 20);
            $(this).find(".js-input").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
                    $(n).textbox('resize',new_width);
            });
            $(this).find(".js-label").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
                    $(n).width(new_width);
            });
           $(this).find("input:text, textarea").each(function (i, n) {
                var new_width = Math.floor(this_width * $(n).attr("width-data"))-121 ;
                    $(n).width(new_width);
            });
        }
        });
    }

    // 初始化只读状态
    function setReadonly() {
        $('.js-panel').each(function () {
            $(this).find(".js-input").each(function (i, n) {
                if ($(this).attr("readonly") != "readonly") {
                	$(this).textbox('textbox').css('background','#fff');
                } else {
                	$(this).parent().find(".searchbox-button").remove();
                    $(this).parent().find(".combo-arrow").remove();
                    $(this).parent().find(".textbox-addon").remove();
                    $(this).next("span").unbind("click");
                }
                if ($(this).attr("class").indexOf("easyui-searchbox") > -1) {
                    var box = $(this).searchbox('textbox');//获取控件文本框对象
                    box.attr('readonly', true);//禁用输入
                }
            });
        });
    }
    //111111111111111111111111111111111111111111
    
$(function(){
OnLoad__frmClassScorm();

});
//OnLoad开始
function OnLoad__frmClassScorm(){
// 取得当前dg选中行id
var j1=function(){
	console.log('DecodeId 逻辑开始');
	var EditId=getStorage('EditId');
	setStorage('EditId',undefined);
	if(EditId&&EditId!='-1'){
		var arr = "pCondition.classId".split(".");
		var panelId = arr[0];
		var controlName = arr[1];
		var data = {controlName:EditId};
		var data = eval("({" + controlName + ":'" + EditId + "'})");
		$('#'+panelId).form('load',data);
		jQuery();
	} else{
		loaded(); //20170929 取不到ID时走no分支
	}
 }


/**
 * 为下拉组件设置数据源
 * list忽略token
 */
var jQuery=function(){
	console.log('List 逻辑开始');
	var token = getToken('OnLoad__frmClassScorm_jQuery');
	restoreToken("OnLoad__frmClassScorm_jQuery",token);
	var param1 = {'token':token};
	var param2 = {};
	if("pCondition"){
		param2 = $("#pCondition :input,hidden").serializeJson();
	}
	var queryParams = $.extend(param1, param2);
	var url = '/courseList';
	debugger;
	ajaxPost(url,queryParams,function(res){
		var arr = 'pCondition.courseId'.split(".");
		var panelId = "#"+arr[0];
		var val=$(getControlId("pCondition.courseId")).combobox('getValue');
		var flg=false;
		for(var i=0;i<res.data.length;i++){
			if(res.data[i].value==val){
				flg=true;
				break;
			}
		} 
		if(!flg){
			$(getControlId("pCondition.courseId")).combobox('clear');
		}
		$(getControlId("pCondition.courseId")).combobox('loadData',res.data);
		loaded();
	})	
}

j1();
}
//OnLoad结束
//pToolbar.btnSearchOnClick开始
function OnClick_pToolbar_btnSearch_frmClassScorm(){
var Query =function () {
	console.log('Query 逻辑开始');
    var token = getToken('OnClick_pToolbar_btnSearch_frmClassScorm_Query');
	restoreToken("OnClick_pToolbar_btnSearch_frmClassScorm_Query",token);
    
    
    var param1 = {'token':token};
	var param2 = {};
    param2 = $("#pCondition :input,hidden").serializeJson();
    
    var courseId = $('#pCondition_courseId').combobox('getValue'); 
    
    param2.courseId = courseId;
    var queryParams = $.extend(param1, param2);
    
    var url = '/getScormResourceColumn';
    
    ajaxPost(url,queryParams,function(res){
            //获取表头数据成功后，使用easyUi的datagrid去生成表格
            
            $('#pTable').datagrid({
                url: "/getScormResourceData", //获取数据后台接口
                method:"get",
                columns:res.data,//外层ajax请求的表头json
                //columns:[[{"field":"scorm-223","width":"100","title":"task one theory_scormtask one theory_scormtask on"},
                //	{"field":"scorm-340","width":"100","title":"Task five theoryTask five theoryTask five theoryTa"},
                //	{"field":"scorm-343","width":"100","title":"task four theory2"},
                //	{"field":"scorm-368","width":"100","title":"task twelve theory"},
                //	{"field":"scorm-396","width":"100","title":"task one theory no ftask one theory no ftaone the"}]],
                queryParams:{"courseId":courseId,"classId":$("#pCondition_classId").val()},
                rownumbers: true, //行号
                pagination: false, //分页控件
                striped:true,
                loadMsg:"正在努力加载数据,表格渲染中...",
                onLoadSuccess: function (data) {
                	if(data.total == 0){
                		//$(".datagrid-btable:eq(1)").html("<div  style='padding:5px 0px;width:100%;color:red;font-size:12px;text-align:center;'>查无数据</div>");
                		$(".datagrid-btable:eq(1)").html("<div class='datagrid-empty' style='position: absolute; left: 0px; top: 25px; width: 100%; text-align: center;'>没有查询结果</div>");
                	}
                },
            });
      })  
    
    
};


Query();
}
//pToolbar.btnSearchOnClick结束
//pToolbar.btnBackOnClick开始
function OnClick_pToolbar_btnBack_frmClassScorm(){
var proTransition=function() {
	console.log('Back 逻辑开始');
	var token = getToken('OnClick_pToolbar_btnBack_frmClassScorm_proTransition');
	if(token){
		//返回需要记录跳转路径 做成数组 没跳转一次push back一次pop tab关闭时候清除storage 唯一标识为页面code和from
		var path=$("#from").val()+$("#code").val();
		var pathValue = JSON.parse(JSON.stringify(getStorage(path)));
		pathValue.pop();
		setStorage(path, pathValue);
		var pathNameValue = JSON.parse(JSON.stringify(getStorage(path + "_name")));
		pathNameValue.pop();
		setStorage(path + "_name", pathNameValue);
		//window.location.href = "/ui/"+getStorage(path)[getStorage(path).length-1]+"?EditMode=Edit&token=" + token + "&type=Back&puid=" + $("#uiid").val() + "&back=Back&code="+$("#code").val()+"&from="+$("#from").val();
		if(pathValue==null||pathValue==''){
			console.log("跳转到首页！");
			window.location.href = "/content/index";
		}else{
			console.log("正常跳转！");
			window.location.href = "/ui/"+getStorage(path)[getStorage(path).length-1]+"?token=" + token + "&type=Back&puid=" + $("#uiid").val() + "&back=Back&code="+$("#code").val()+"&from="+$("#from").val();
		}
	}
}

proTransition();
}
//pToolbar.btnBackOnClick结束

	//222222222222222222222222222222222222222222
	
	
    var uiStorage = {};
    var panelProperty = {"pTable":"id","pCondition":"","pToolbar":""};//界面绘制时，计算每个panel对应的主键controlName
    //每个panel实现唯一标识值的setter getter
    function setPanelId(panelName, idVal) {
        setUiStorage(panelName + '_EditId', idVal);
    }

    function getPanelId(panelName) {
        var primaryControl = panelProperty[panelName];
        if (primaryControl) {
            return getPanelControlValue(panelName + "." + primaryControl,true);
        }
        return null;
    }
     function getEditMode(){
      return getUIMode('frmClassScorm');
     }
    
</script>
</html>
