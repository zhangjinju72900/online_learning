
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
			   <link href="/view/common/css/plugins/easyui-1.5.2/themes/default/easyui.css?20190130" rel="stylesheet" media="screen"/>
			   <link href="/view/common/css/style.css?20190130" rel="stylesheet" media="screen"/>
    <link href="/view/common/css/imageView.css?20190130" rel="stylesheet" media="screen"/>
    <link href="/view/common/css/plugins/easyui-1.5.2/themes/icon.css?20190130" rel="stylesheet" media="screen"/>
    <script type="text/javascript">var ctx = "";</script>
    <script src="/view/common/js/jquery-1.8.3.min.js?20190130"></script>
    <script src="/view/common/css/plugins/easyui-1.5.2/jquery.easyui.min.js?20190130"></script>
    <script src="/view/common/css/plugins/easyui-1.5.2/easyui.plugins.js?20190130"></script>
    <script src="/view/common/js/Common.js?20190130"></script>
    <!-- 引用EasyUI的国际化文件,让它显示中文 -->
    <script src="/view/common/css/plugins/easyui-1.5.2/locale/easyui-lang-zh_CN.js?20190130" type="text/javascript"></script>
    <script src="/view/common/js/base.js?20190130"></script>
    <script src="/view/common/js/MD5.js?20190130"></script>
    <script src="/view/common/js/jsExpression.js?20190130"></script>

    <!-- js本地存储-->
    <script src="/view/common/js/store/myStorage.js?20190130" charset="utf-8"></script>
    <script src="/view/common/js/store/json2.js?20190130" charset="utf-8"></script>
    <script src="/view/common/js/store/localDB.js?20190130" charset="utf-8"></script>
    <!-- ajax请求-->
    <script src="/view/common/js/ajaxUtil.js?20190130"></script>
	
	
         
		      <link href="/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?20190130" rel="stylesheet" media="screen"/>
		    <script src="/view/common/css/plugins/easyui-1.5.2/plugins/jquery.Jcrop.min.js?20190130"></script>
		    <script src="/view/common/css/plugins/easyui-1.5.2/plugins/imgCropUpload.js?20190130"></script>
	     	<script src="/view/common/css/plugins/easyui-1.5.2/plugins/jquery.filebox.js?20190130"></script>
		 	<script src="/view/common/js/ajaxfileupload.js?20190130"></script>
         <script src="/view/common/js/group.js?20190130"></script>
		      <link href="/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?20190130" rel="stylesheet" media="screen"/>
		    <script src="/view/common/css/plugins/easyui-1.5.2/plugins/jquery.Jcrop.min.js?20190130"></script>
		    <script src="/view/common/css/plugins/easyui-1.5.2/plugins/imgCropUpload.js?20190130"></script>
	     	<script src="/view/common/css/plugins/easyui-1.5.2/plugins/jquery.filebox.js?20190130"></script>
		 	<script src="/view/common/js/ajaxfileupload.js?20190130"></script>
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
	          	OnClick_pformDown_ctlSave_frmMaintainManualPPTEdit();
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


function OnClickView(){
	if($("#addGroup_resourcesType").val() == "14" || $("#addGroup_resourcesType").val() == "15"){
		var id = $("#addGroup_id").val();
		var temp = document.createElement("form");        
		temp.action = "${ctx}/runCode";        
		temp.method = "post";    
		temp.target = "_blank";			
		temp.style.display = "none";   
		
		var input = document.createElement("input");
	    input.type = "hidden";
	    input.name = "id";
	    input.value = id;
	    temp.appendChild(input);
	    
	    var input1 = document.createElement("input");
	    input1.type = "hidden";
	    input1.name = "resourcesType";
	    input1.value = $("#addGroup_resourcesType").val();
	    temp.appendChild(input1); 
	    
	    var input2 = document.createElement("input");
	    input2.type = "hidden";
	    input2.name = "module";
	    input2.value = "2";
	    temp.appendChild(input2);       
		
		$(document.body).append(temp);
		
		temp.submit();        
		return temp;		
	}else if($("#addGroup_fileType").val() == 'pdf'){
		window.open($("#addGroup_ossUrl").val());
	}else if($("#addGroup_fileType").val() == 'mp4'){
		var url = "${ctx}/viewMp4";
	    var temp = document.createElement("form");        
		temp.action = url;        
		temp.method = "post";    
		temp.target = "_blank";			
		temp.style.display = "none";
		
		var input = document.createElement("input");
	    input.type = "hidden";
	    input.name = "key";
	    input.value = $("#addGroup_ossKey").val();
	    temp.appendChild(input);        
		     
		$(document.body).append(temp);     
		temp.submit();        
		return temp; 
	}else if($("#addGroup_fileType").val() == 'xls' || $("#addGroup_fileType").val() == 'xlsx' ||
		$("#addGroup_fileType").val() == 'doc' || $("#addGroup_fileType").val() == 'docx' ||
		$("#addGroup_fileType").val() == 'ppt' || $("#addGroup_fileType").val() == 'pptx'){
		var url = "${ctx}/viewOffice";
	    var temp = document.createElement("form");        
		temp.action = url;        
		temp.method = "post";    
		temp.target = "_blank";			
		temp.style.display = "none";
		
		var input = document.createElement("input");
	    input.type = "hidden";
	    input.name = "key";
	    input.value = $("#addGroup_ossKey").val();
	    temp.appendChild(input);
		     
		$(document.body).append(temp);     
		temp.submit();        
		return temp; 
	}else{
		alert("当前仅支持预览scorm/mp4/pdf/word/excel/ppt文件");
	}
}
</script>
    
    
</head>
<body class="easyui-layout js-ui" title="编辑PPT资料" id="frmMaintainManualPPTEdit" style="width:100%;height:100%;background-color:white;">

<div id='Loading'
     style="position:absolute;z-index:1000;top:0px;left:0px;width:100%;height:100%;background:#FFFFFF ;text-align:center;padding-top: 10%;">
    <h1 style="display: inline-block;border: 1px solid #95b9e7;font-size: 16px;padding: 5px;">
        <image src='/view/common/css/plugins/easyui-1.5.2/themes/default/images/loading.gif'/>
        <font color="#15428B" size="2">正在处理，请稍等···</font></h1>
</div>

<input type="hidden" id="from" value="menu">
<input type="hidden" id="code" value="308">
<input type="hidden" id="uiid" value="bbc0e93eeef34c528a59b219188ea751">
<input type="hidden" id="puid" value="a4286aa88d7f4ada919e44e3d5c9a731">
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
<!--Center begin-->
<div data-options="region:'center'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
        
                    <div data-options="border:false,region:'center'" style="
                        
                                                                        
                        
                            " class="js-panel" trigger="">
<div name="addGroup" id="addGroup" style="padding-top:15px;"
     class="js-group">
	<input type="hidden" name="id" id="addGroup_id">
	<input type="hidden" name="parentId" id="addGroup_parentId">
<div class="input-group-inline" >
<label style='margin-left :3px;vertical-align:top;'>支持格式</label>
<label class='js-label'style="width:10px;height:22px;text-align:left" width-data="1" id="addGroup_ctlName1" name="ctlName1"/>当前支持单个ppt文件上传(zip)</label>
</div> 	<br>
	<div class="input-group-inline js-imagefileName" >
	    <link href="/view/common/css/plugins/easyui-1.5.2/themes/default/jquery.Jcrop.css?20190130" rel="stylesheet" media="screen"/>
	    <label style="margin-left :3px;
"
	    >上传文件 </label>
	    	<input class="easyui-filebox js-input"  width-data="1"
	    	id="fileUploadId_1" name="fileName" data-options="buttonText:'...',prompt:'',
	    	     
	onClickButton:function(){eval('OnClick_addGroup_fileName_frmMaintainManualPPTEdit()');},
onChange: function(a,b){
				var out = outParam.split('.');
				var path = pathParam == ''?'':pathParam.split('.');
				var module=moduleParam;
					if(out == null || out.length != 2 ){
						console.log('Out参数有误，请检查配置');
					}else if(path != null && path!='' && path.length != 2){
						console.log('Url参数有误，请检查配置');
					}else{
					if($(this).attr('type')=='grid'){
				    	var fileIdx=$(this).attr('index');
					} else {
						var fileIdx='1';
					}
		    		var upfile = document.getElementById('filebox_file_id_'+fileIdx).files[0];
		    		var size = ''; size = fileSize;
		    		var type = ''; type = fileType;
		    		var accessType = ''; accessType = fileAccessType;
		    		var uploadUrl = ''; uploadUrl = fileUploadUrl;
		    		var token = ''; token = upToken;
		    		
		    		var pointPos = upfile.name.lastIndexOf('.');
					var fileSuffix=upfile.name.substring(pointPos+1,upfile.name.length);//后缀名
		    		 
		    		if(size == ''){
		    			$.messager.alert('文件上传','请配置文件上传大小参数(单位:M)');
		    		}else if(parseInt(size)*1024<=upfile.size){//单位M
		    			var msg = '上传文件不能超过'+size+'KB，请重新上传';
		    			$.messager.alert('文件上传',msg);
		    		}else if(type!='' && type.toLowerCase().indexOf(fileSuffix.toLowerCase())<0){
		    			$.messager.alert('文件上传','文件类型不在允许范围内(' + type + ')');
		    		}else{
						$.messager.confirm('文件上传', '确定要上传吗?', function(r){
							if (r){
							  	 $.messager.progress({ 
									           title: '提示', 
									           msg: '正在上传，请稍后……', 
									           text: '' 
									        });
					    		$.ajaxFileUpload({  
					                 url:'/localUpload?token='+token,  
					                 data:{accessType:accessType,allowFile:type,maxSize:size,uploadUrl:uploadUrl,module:module},
					                 secureuri:false,  
					                 fileElementId:'filebox_file_id_'+fileIdx,//file标签的id  
					                 dataType: 'json',//返回数据的类型
					                 contentType:'application/json',
					                 success: function (data) {
					                 	$.messager.progress('close'); 
					                 	if(data.status!=0){
	         			    				if($('#'+out[0]).attr('class').indexOf('js-group')>-1){
							    		    	//赋值给指定input
							    		    	$('#'+out[0]+'_'+out[1]).val(data.status);
							    		    	if(path != null){
							    		    		$('#'+path[0]+'_'+path[1]).val(data.data.url);
							    		    	}
						    				} else if($('#'+out[0]).attr('class').indexOf('easyui-datagrid')>-1){
						    					//赋值给指定列
						    					$('#'+out[0]).datagrid('getSelected')[out[1]]=data.status;
						    					if(path != null){
							    		    		$('#'+path[0]).datagrid('getSelected')[path[1]]=data.data.url;
							    		    	}
						    				}
		    								  try {
												eval('addGroup_fileName_OnSelect()');
											  } catch(e) {}
					                 	}
				                     	$.messager.alert('文件上传',data.msg);
					                 },
					                 error: function (data) {  
					                     $.messager.alert('文件上传失败，请重新上传');  
					                 }  
			             		});
							}
						});
		    		}
	    		}
	    	}"  />
	</div>
	
		<br>
	<div class="input-group-inline" >
	    <label style="margin-left :3px; color:red;font-weight:bold">编码格式</label>
    	<select class="easyui-combobox js-input"  width-data="1" style="width:2px;height:22px" tipPosition="bottom"
    	 required="true"  
    	id="addGroup_ctlCoding" name="ctlCoding" data-options="editable:false, 
    	">
		</select>
    	<script>
	    	$('#addGroup_ctlCoding').combobox({
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
					eval('addGroup_ctlCoding_OnSelect()');
				  } catch(e) {}
				},
				
	    	}); 
		</script>   
	</div>
	
		<br>
	<input type="hidden" name="fileId" id="addGroup_fileId">
	<input type="hidden" name="fileType" id="addGroup_fileType">
	<input type="hidden" name="resourcesType" id="addGroup_resourcesType">
	<input type="hidden" name="filePath" id="addGroup_filePath">
	<input type="hidden" name="ossKey" id="addGroup_ossKey">
	<input type="hidden" name="ossUrl" id="addGroup_ossUrl">
	<input type="hidden" name="dataFlag" id="addGroup_dataFlag">
	<input type="hidden" name="updateTime" id="addGroup_updateTime">
	<input type="hidden" name="updateBy" id="addGroup_updateBy">
	<input type="hidden" name="createTime" id="addGroup_createTime">
	<input type="hidden" name="createBy" id="addGroup_createBy">
</div>

                    </div>
    </div>
</div>
<!--Center end-->
<!--South begin-->

<div data-options="region:'south'"
     style="">
    <div class="easyui-layout" data-options="fit:true">
        
                    <div data-options="border:false,region:'south'" style="
                        overflow:hidden;
                                                                        
                        
                            " class="js-panel" trigger="">
<div class="btn-group" style="text-align:right;">
<div style="float: left;line-height: 29px;" class="js-path"></div>
	
		
		
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pformDown_ctlSave_frmMaintainManualPPTEdit();antiClick('pformDown_ctlSave');" 
		id="pformDown_ctlSave" name="ctlSave"
		style="margin-right:10px;

		width:40px;
		
"
		>确认</a>
	
		
		
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		id="pformDown_ctlView" name="ctlView" onclick="OnClickView()"
		style="margin-right:10px;

		width:40px;
		
"
		>预览</a>
	
		
		
		
		<a class="easyui-linkbutton" href="javascript:void(0)" 
		OnClick="OnClick_pformDown_ctlCancel_frmMaintainManualPPTEdit();antiClick('pformDown_ctlCancel');" 
		id="pformDown_ctlCancel" name="ctlCancel"
		style="margin-right:10px;

		width:40px;
		
"
		>取消</a>
</div>

                    </div>
    </div>
</div>


<!--South end-->
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
    
    var clientContext = ${clientContext};
    //auth control
    $(document).ready(function () {
		${initControl}
		
		cus_resize_init_input();//comment out this line if your wanna use a special style
    
        setReadonly();
   		
   		${validatorRules}
   		
        changeTab();
        // 记录路径 如果从菜单 快捷菜单 或者首页跳转记录路径
        if(!'Popup'){
	        var path='menu'+$("#code").val();
	        var arr = new Array();
	        var arrTitle = new Array();
			arr.push($(".js-ui").attr("id"));
			arrTitle.push($(".js-ui").attr("title"));
			setStorage(path, arr);
			setStorage(path+"_name", arrTitle);
        }
        if('Popup'!='Popup'){
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
    

${js}


    var uiStorage = {};
    var panelProperty = ${panelPropery};//界面绘制时，计算每个panel对应的主键controlName
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
      return getUIMode('frmMaintainManualPPTEdit');
     }
    
</script>
</html>
