/**
 * base on easyUI 
 */
//用于客户端function根据服务端解析时定义的functionId获取指定ui下对应的token

var getFnName = function(callee){
    var _callee = callee.toString().replace(/[\s\?]*/g,""),
    comb = _callee.length >= 50 ? 50 :_callee.length;
    _callee = _callee.substring(0,comb);
    var name = _callee.match(/^function([^\(]+?)\(/);
    if(name && name[1]){
      return name[1];
    }
    var caller = callee.caller,
    _caller = caller.toString().replace(/[\s\?]*/g,"");
    var last = _caller.indexOf(_callee),
    str = _caller.substring(last-30,last);
    name = str.match(/var([^\=]+?)\=/);
    if(name && name[1]){
      return name[1];
    }
    return "anonymous"
  };

  
function getStorage(key){
	if(parent.gloableStorage){
		return parent.gloableStorage[key];
	} else if(parent.parent.gloableStorage){
		return parent.parent.gloableStorage[key];
	}else if(parent.parent.parent.gloableStorage){
		return parent.parent.parent.gloableStorage[key];
	} else{
		return null;
	}
}
function delStorage(key){
	if(parent.gloableStorage){
		delete parent.gloableStorage[key];
	} else if(parent.parent.gloableStorage){
		delete parent.parent.gloableStorage[key];
	}else if(parent.parent.parent.gloableStorage){
		delete parent.parent.parent.gloableStorage[key];
	} 
}

function setStorage(key, val){
	try{
		if(parent.gloableStorage){
			parent.gloableStorage[key]=val;
		}else if(parent.parent.gloableStorage){
			parent.parent.gloableStorage[key]=val;
		}else if(parent.parent.parent.gloableStorage){
			parent.parent.parent.gloableStorage[key]=val;
		}
	}catch(e){
		console.log("cross domain");
		if(!window.gloableStorage){
			window.gloableStorage = {};
		}
		window.gloableStorage[key]=val;
	}
}

function getUiStorage(key){
	if(uiStorage){
		return uiStorage[key];
	} else if(parent.uiStorage){
		return parent.uiStorage[key];
	}else if(parent.uiStorage){
		return parent.uiStorage[key];
	} else{
		return null;
	}
}
function setUiStorage(key, val){
	if(uiStorage){
		uiStorage[key]=val;
	}else if(parent.uiStorage){
		parent.uiStorage[key]=val;
	}else if(parent.uiStorage){
		parent.parent.uiStorage[key]=val;
	}
}
/**
 * 分组存放数据
 * @param group
 * @param key
 * @returns
 */
function getGroupStorage(group,key){
	if(parent.gloableStorage[group]==undefined){
		return "";
	}else{
		return eval("parent.gloableStorage." + group + "." + key);	
	}
}
		//easyui common formater
		var Common = {

			    //EasyUI用DataGrid用日期格式化
			    TimeFormatter: function (value, rec, index) {
			        if (value == undefined) {
			            return "";
			        }
			        /*json格式时间转js时间格式*/
			        value = value.substr(1, value.length - 2);
			        var obj = eval('(' + "{Date: new " + value + "}" + ')');
			        var dateValue = obj["Date"];
			        if (dateValue.getFullYear() < 1900) {
			            return "";
			        }
			        var val = dateValue.format("yyyy-mm-dd HH:MM");
			        return val.substr(11, 5);
			    },
			    DateTimeFormatter: function (value, rec, index) {
			        if (value == undefined) {
			            return "";
			        }
			        /*json格式时间转js时间格式*/
			        value = value.substr(1, value.length - 2);
			        var obj = eval('(' + "{Date: new " + value + "}" + ')');
			        var dateValue = obj["Date"];
			        if (dateValue.getFullYear() < 1900) {
			            return "";
			        }

			        return dateValue.format("yyyy-mm-dd HH:MM");
			    },

			    //EasyUI用DataGrid用日期格式化
			    DateFormatter: function (value, rec, index) {
			        if (value == undefined || value.length == 0) {
			            return "";
			        }
			       
			        if (value.length > 10){
			        	return value.substr(0, 10);
			        }else{
			        	return value;
			        }
			        
			    },
			    TitleFormatter : function (value, rec, index) {
			        if (value.length > 10) value = value.substr(0, 8) + "...";
			        return value;
			    },
			    LongTitleFormatter: function (value, rec, index) {
			        if (value.length > 15) value = value.substr(0, 12) + "...";
			        return value;
			    }
			};

/**
 * 根据传入URL生成一个基于当前窗体的dialog
 */
function createModalDialog(id, _url, _title, _width, _height, _icon){
    $("body").append("<div id='"+id+"' class='easyui-dialog'></div>");
    if (_width == null)
        _width = 800;
    if (_height == null)
        _height = 400;

    $("#"+id).dialog({
        href: _url
    });
    $("#"+id).dialog('open');
}

/**
 * 信息已保存时右下角滑动自动消失的提示信息
 * 统一
 */
function showSaved(){
	$.messager.show({
		title:'提示信息',
		msg:'信息已保存.',
		timeout:3000,
		showType:'slide'
	});
}
function showSlide(title,message){
	$.messager.show({
		title:title,
		msg:message,
		timeout:3000,
		showType:'slide'
	});
}
function showSaveb(){
	$.messager.show({
		title:'提示信息',
		msg:'请先保存.',
		timeout:3000,
		showType:'slide'
	});
}
/**
 * datagrid没有返回结果时显示
 */
function showEmptyResultMsg(){
$.messager.show({
	title:'提示信息',
	msg:'没有查询结果',
	timeout:3000,
	showType:'slide'
});
}




/**
 * 显示异常
 * 内容为配置的异常拦截器对应的view
 * @param content
 */
function showError(content){
	$.messager.show({
		title:'提示信息',
		msg:content,
		width:350,
		height:260,
		showType:'slide'
	});
}

function showPageDialog(url, title, width, height,callback, shadow) {  //
    var content = '<iframe src="' + url + '" width="100%" height="100%" style="padding: 0px;overflow:hidden;" frameborder="0" scrolling="no"></iframe>';  
    var boarddiv = '<div id="tmpDlg" title="' + title + '"></div>'//style="overflow:hidden;"可以去掉滚动条  
    $(document.body).append(boarddiv);  
    var win = $('#tmpDlg').dialog({  
        content: content,  
        width: width,  
        resizable:true,
        height: height,  
        modal: arguments[5]?arguments[5]:true,   //默认为模式对话框
        title: title,  
//        buttons:[], //按钮在这里不设置否则会产生一个窄的灰色div
        onClose:callback
    });  
    win.dialog('open');  
    return win;
} 
//和show是一对
function closePageDialog(){
	 parent.$('#tmpDlg').dialog('close');
}


$.fn.serializeJson = function(noIncludeEmpty)    
{    
   var o = {};    
   // 添加filebox value
   $(".easyui-filebox").each(function() {
	   o[$(this).attr('textboxname')]=$(this).filebox("getText");
   })
   var a = this.serializeArray();    
   $.each(a, function() {    
       if (o[this.name]) {    
           if (!o[this.name].push) {    
               o[this.name] = [o[this.name]];    
           }    
           o[this.name].push(this.value || '');    
       } else {  
    	   if(noIncludeEmpty){
    		   if(this.value){
    			   o[this.name] = this.value || '';
    		   }
    	   } else {
    		   o[this.name] = this.value || '';    
    	   }
       }    
   });    
   return o;    
};  

function goback(){
	if(document.referrer=='${ctx}'){
		alert('top');
	}else{
		location.href=document.referrer;
	}
}
function setCookie(cname, cvalue, exdays) {  
    var d = new Date();  
    d.setTime(d.getTime() + (exdays*24*60*60*1000));  
    var expires = "expires="+d.toUTCString();  
    document.cookie = cname + "=" + cvalue + "; path=/;" + expires;  
}
//获取cookie  
function getCookie(cname) {  
    var name = cname + "=";  
    var ca = document.cookie.split(';'); 
    for(var i=0; i<ca.length; i++) {  
        var c = ca[i];  
        while (c.charAt(0)==' ') c = c.substring(1);  
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);  
    }  
    return "";  
}  
//清除cookie    
function clearCookie(name) {    
    setCookie(name, "", -1);    
}
//datagrid保持数据封箱拆箱操作
function packageDataGridOption(grid){
	var dataGridOption={};
	dataGridOption.pageSize=grid.datagrid('getPager').data("pagination").options.pageSize;
	dataGridOption.pageNumber=grid.datagrid('getPager').data("pagination").options.pageNumber;
	dataGridOption.selected=grid.datagrid('getRowIndex',grid.datagrid('getSelected'));
	return dataGridOption;
}
//out参数通常配置方式为panelName.controlName
//对组件赋值等操作时需要使用组件id
function getControlId(out){
	var arr = out.split(".");
	return "#"+arr[0] + "_" + arr[1];
}

function getControlName(panelControlName){
	var arr = panelControlName.split(".");
	if(arr!=null && arr.length>1){
		return arr[1];
	}
	return null;
}
//取指定panel的指定control值。
//TODO未来扩展组件支持。
function getPanelControlValue(panelControlName,single){
	if(panelControlName==undefined) return null;
	var arr = panelControlName.split(".");
	var panelId = "#"+arr[0];
	var controlName = arr[1];
	var className = $(panelId).attr("class");
	var currentId = undefined;
	if(!className){
		currentId = $(getControlId(panelControlName)).val();
	}else if(className.indexOf('datagrid')>=0){
		if($(panelId).datagrid("getColumnOption", 'ck')){
			var selectedRows = $(panelId).datagrid('getChecked');
		} else {
			var selectedRows = $(panelId).datagrid('getSelections');
		}
		var ids = [];
		for(var i=0; i<selectedRows.length; i++){
			ids.push(selectedRows[i][controlName]);
		}
		currentId = ids.join(',');
	}else if(className&&className.indexOf('tree')>=0){
		var selectTree = getStorage('selectTree');
		if(selectTree&&$(panelId).tree('getSelected')){
			currentId = selectTree[controlName];
		}
	}else if(className&&className.indexOf('barChart')>=0||className.indexOf('columnChart')>=0||className.indexOf('pieChart')>=0||className.indexOf('lineChart')>=0){
		var selectChartData = getStorage('selectChartDate');
		var type =selectChartData.componentSubType;
        var dataIndex = selectChartData.dataIndex;
        //panel取Echart
        var encodeCharts = eval(arr[0]);
        var seri = encodeCharts.getOption().series[selectChartData.seriesIndex].data;
        var controlIndex = $(getControlId(panelControlName)).index();
		if(type!="pie") {
            //controlName去PropertyIndex
            currentId = seri[seri.length - 1][0][controlIndex][dataIndex];
        }else{
            currentId = seri[seri.length-1].key[0][controlIndex][dataIndex];
		}

	}else{
		if($(panelId+'_'+controlName) && $(panelId+'_'+controlName).attr("class") && $(panelId+'_'+controlName).attr("class").indexOf('combo')>0){
			currentId = $(panelId+'_'+controlName).combobox("getValues");
		}else{
			currentId = $(panelId+" [name='"+controlName+"']").val();
		}
    }
	if(!currentId){
		console.warn("panelControlName.value" + panelControlName + " is undefined...可能是错误的配置值造成无法获取值");
	}
	return currentId;
}
function getCurrentTabIndex(){
	var index = $("#center_tab").tabs('getTab',$("#center_tab").tabs('getSelected'));
	return index;
}

//密码内容加密
function getMD5PassWord(obj,newValue,oldValue){
//	var pswd = $(".js-password").val();
	var pswd = newValue;
	alert("输入密码框原值："+pswd);
	if(pswd != ""){
		$('.js-MD5password').val(MD5(pswd));
	}
	alert("密码框加密值："+$('.js-MD5password').val());
}
//本地变量存储路径获得
function getPath(t){
	return $(".js-ui").attr('id')+"_"+t.attr("id")+"_"+$("#code").val();
}

function getUIMode(uiName){
	return getStorage(uiName + '_EditMode')
}
function setUIMode(uiName, mode){
	return setStorage(uiName + '_EditMode', mode)
}

function setEncodeParam(out,paramObj){
	setStorage('EncodeParam' + out,paramObj);
}

function getEncodeParam(out){
	return getStorage('EncodeParam' + out);
}

function delEncodeParam(out){
	delStorage('EncodeParam' + out);
}
//用逗号分隔符将数组中指定属性合并成一个值返回
function getPropertyValues(arr,propertyName){
	var len = arr.length;
	if(len){
		var arrResult =  new Array(len);
		for(i=0;i<len;i++){
			arrResult[i] = arr[i][propertyName];
		}
		return arrResult.join(",");
	}
	return "";
}
//grid向上移动一列
function moveUp (dg) {
	var selectrow=$('#'+dg).datagrid('getSelected');
	if(!selectrow){
		$.messager.alert('提示', '必须选中一列移动!', 'warning');  
	}
	var rowIndex=$('#'+dg).datagrid('getRowIndex', selectrow);  
	if(rowIndex==0){  
        $.messager.alert('提示', '顶行无法上移!', 'warning');  
    }else{
    	//判断当前行是否被选中
	    var allRows=$('#'+dg).datagrid('getChecked');   //获取所有被选中的行
	    var flg=false;
	    $.each(allRows,function(i,n){
    	    if(selectrow.field==n.field){
    	    	flg=true;
    	    	return false;
    	    }
	    })
        $('#'+dg).datagrid('deleteRow', rowIndex);//删除一行  
        rowIndex--;  
        $('#'+dg).datagrid('insertRow', {  
            index:rowIndex,  
            row:selectrow  
        });  
        $('#'+dg).datagrid('selectRow', rowIndex);
        if(flg)
        	$('#'+dg).datagrid("checkRow",rowIndex);
    } 
}
function moveDown (dg) {
	var rows=$('#'+dg).datagrid('getRows');  
	var rowlength=rows.length; 
	var selectrow=$('#'+dg).datagrid('getSelected');
	if(!selectrow){
		$.messager.alert('提示', '必须选中一列移动!', 'warning');  
	}
	var rowIndex=$('#'+dg).datagrid('getRowIndex', selectrow);  
	if(rowIndex==rowlength-1){  
        $.messager.alert('提示', '底行无法下移!', 'warning');  
    }else{
    	//判断当前行是否被选中
	    var allRows=$('#'+dg).datagrid('getChecked');   //获取所有被选中的行
	    var flg=false;
	    $.each(allRows,function(i,n){
    	    if(selectrow.field==n.field){
    	    	flg=true;
    	    	return false;
    	    }
	    })
        $('#'+dg).datagrid('deleteRow', rowIndex);//删除一行  
        rowIndex++;  
        $('#'+dg).datagrid('insertRow', {  
            index:rowIndex,  
            row:selectrow  
        });  
        $('#'+dg).datagrid('selectRow', rowIndex);  
        if(flg)
        	$('#'+dg).datagrid("checkRow",rowIndex);
    } 
}

function setNav(ToTitle, To){
	var path=$("#from").val()+$("#code").val();
	if(getStorage(path)){
		var pathValue = JSON.parse(JSON.stringify(getStorage(path)));
	}else{
		var pathValue=[];
	}
	pathValue.push(To);
	setStorage(path, pathValue);
	
	if(getStorage(path + "_name")){
		var pathNameValue = JSON.parse(JSON.stringify(getStorage(path + "_name")));
	}else{
		var pathNameValue=[];
	}
	pathNameValue.push(ToTitle)
	setStorage(path + "_name", pathNameValue);
}
function getTransitionUrl(token, type, puid,EditMode, from, code, ToTitle, To, url){
	setNav(ToTitle, To);
	return url+To+"?token="+token+"&type="+type+"&puid="+puid+"&EditMode="+EditMode+"&from="+from+"&code="+code;
}

//选中行的id
function checkedId(){
	var checkedItems = $('#addGroup1').datagrid('getChecked');  
    var names = [];
    for(var i=0;i<checkedItems.length;i++){
    	names.push(checkedItems[i].id);
    }             
    var ids = names.join(",");
    $("#addGroup_flag").val(ids);
}

//获取表格中指定字段的值,进行判断是否置顶操作
function topRowDatagrid(panel){
debugger
	 var name ="#"+panel; 
	 var row = $(name).datagrid('getChecked');
	 var flag = false;
	// var toptext = row[0].topFlag;
	 for(i=0;i<row.length;i++){
		 var topFlag = row[i].topFlag;
		 if(topFlag=="是"){   //如果是已经置顶
			 flag = true; 
		 }
	 }
	return flag;	
}
//判断当前数据能不能推荐
function checkRecommend(panel){
debugger
	 var name ="#"+panel; 
	 var row = $(name).datagrid('getChecked');
	 var flag = false;
	 for(i=0;i<row.length;i++){
		 var ableRecommend = row[i].ableRecommend;
		 if(ableRecommend=="能"){   //如果能就可以推荐
			 flag = true;
		 }else{
			 flag = false;
			 break;
		 }
	 }
	return flag;	
}
//判断所选数据是否已经推荐
function alreadyRecommend(panel){
debugger
	 var name ="#"+panel; 
	 var row = $(name).datagrid('getChecked');
	 var flag = false;
	 for(i=0;i<row.length;i++){
		 var recommendFlag = row[i].recommendFlag;
		 var ableRecommend = row[i].ableRecommend;
		 if(recommendFlag=="是"){//如果已经推荐
				 flag = true; 
		 }
	 }
	return flag;	
}

//判断审核是否通过checkStatus
function checkStatus(panel){
	 debugger
	 var name ="#"+panel; 
	 var row = $(name).datagrid('getChecked');
	 var flag = false;
	 for(i=0;i<row.length;i++){
		 var status = row[i].status;
		 if(status=="审核通过"){   //如果已经推荐
			 flag = true; 
		 }
	 }
	return flag;	
	
}
	
	
	//导出
	function toController(url,param,tipMsg,errorMsg){
		debugger 
		var flag = true;
		 var params = param;
		 var pars = "";
		/* var projectId=getPanelControlValue(params[0].split(".")[0]+"."+params[0].split(".")[1]);
		 for(i=0;i<params.length;i++){
		  var temps = params[i].split(".");
		  
		  var value = getPanelControlValue(temps[0]+"."+temps[1]);
		  if(pars==""&&value!=""&&value!=null){
		   pars = "?"+temps[2]+"="+value;
		  }else if(value!=null&&value!=""){
		   pars += "&"+temps[2]+"="+value;
		  }
		 }*/
		 
		 var id=getPanelControlValue(params.split(".")[0]+"."+params.split(".")[1]);
		 pars = "?"+params.split(".")[2]+"="+id;
		 
		 var defer = $.Deferred();
		    var _r = function (){
		     $.ajax({
		      type: "post",
		      url: getRootPath_web()+"/"+url+encodeURI(encodeURI(pars)),
		      dataType: 'text',
		      async: true,
		      beforeSend :function(){
		      if(tipMsg==null||tipMsg==""){
		    	 //showWaitMsg("处理中请稍后...");
		       }else{
		        //showWaitMsg(tipMsg);
		       }
		      },
		      complete :function(){
		       // hideWaitMsg();
		      },
		      success: function(result) {
		        defer.resolve(result); 
		      }
		     })
		        // 这步也很重要,将defer返回给变量  _r
		        return defer.promise();
		    };

		    //  我们用 _r 的结果,紧接着执行第二个方法
		    $.when(_r()).done(function(msg){
		            // 根据实际需求更改，以下只是随便写个例子
		     if(msg.search('error')==-1){
		      location.href = getRootPath_web()+"/lesson/downLoad?path="+encodeURI(encodeURI(msg));
		  }else{
		   if(tipMsg!=null&&tipMsg!=""){
		    $.messager.alert('失败提示',msg);
		   }else{
		    $.messager.alert(errorMsg);
		   }
		   
		  } 
		     });
		}

	function getRootPath_web() {
	    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	    var curWwwPath = window.document.location.href;
	    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	    var pathName = window.document.location.pathname;
	    var pos = curWwwPath.indexOf(pathName);
	    //获取主机b地址，如： http://localhost:8083
	    var localhostPath = curWwwPath.substring(0, pos);
	    //获取带"/"的项目名，如：/uimcardprj
	    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	    
	    var rootPath = localhostPath + projectName;
	    if(rootPath.search("ui") == -1){
	     return (rootPath);
	    }else{
	     return localhostPath;
	    }
	}
function ossFileDown(filePath){
	filePath = getPanelControlValue(filePath);
	 location.href = getRootPath_web()+"/ossFileDownLoad?path="+encodeURI(encodeURI(filePath));
}
function openMp4View(key){
	 var vid = getPanelControlValue(key);
	 window.open(getRootPath_web()+"/viewMp4?key="+vid);
}
function clearVal(val){
	debugger
	var id = val.split(",");
	for(i=0;i<id.length;i++){
		
		$("input[name='"+id[i]+"']").val('');
		$("input[name='"+id[i]+"']").prev().val('');
	}
}

function custom_close(){
	 parent.flg=true;
	 parent.$('#tmpDlg').dialog('close');
	}

	function treeSelectOk(id,name){
	 debugger
	 var selectId = getPanelControlValue(id.split("|")[0]);
	 var selectName = getPanelControlValue(name.split("|")[0]);
	 parent.$("input[name='"+id.split("|")[1]+"']").val(selectId);
	 parent.$("input[name='"+name.split("|")[1]+"']").prev().val(selectName);
	 custom_close();
	}
	//检测图片类型
	function checkPictype(tabId){
	     var rows = $("#"+tabId).datagrid("getRows");
	     var flag = true;
	     debugger
	     if(rows.length>0){
	      for(var i=0;i<rows.length;i++){
	       var picId = rows[i].picId;
	       var delet=rows[i].delete;
	       if((picId==null||picId==undefined||picId=='undefined'||picId=='') && picId!=0 && delet!=1){
	        flag = false;
	       }
	      }
	     }
	     return flag;
	 } 
	
	//商品维护中判断单价和积分判断
	function checkIntegralAndAmount(){
		debugger
		 $(".datagrid-cell-rownumber").trigger("click");
		 var rows = $("#meetFile").datagrid("getRows");
		 var flag=true;
		 debugger
		 if(rows.length>0){
			 for(var i=0;i<rows.length;i++){
				 var integral=rows[i].integral;
				 var amount=rows[i].amount;
				 var del=rows[i].delete;
				 if((integral<0||amount<0)&&del!=1){
					 flag=false;
				 }
			 }
		 }
		 return flag;
	}
	
	//判断学校是否重复
	  function funName(){
	    var rows =$("#school").datagrid("getRows");
	     var str1="";
	     var str2="";
	     for(i=0;i<rows.length;i++){
	      var schoolId = rows[i].schoolId;
	      var del = rows[i].delete;
	      if(del!=1){
	       str1=str1+","+schoolId;
	      }
	     }
	     for(i=0;i<rows.length;i++){
	      var schoolId = rows[i].schoolId;
	      var del = rows[i].delete;
	      if(del!=1&&str2.search(schoolId)==-1){
	       str2=str2+","+schoolId;
	      }
	     }
	     if(str1==str2){
	      return true;
	     }else{
	      return false;
	     }
	    }
	  //判断班级是否重复
	  function funName1(){
		    var rows =$("#pClass").datagrid("getRows");
		     var str1="";
		     var str2="";
		     for(i=0;i<rows.length;i++){
		      var classId = rows[i].classId;
		      var del = rows[i].delete;
		      if(del!=1){
		       str1=str1+","+classId;
		      }
		     }
		     for(i=0;i<rows.length;i++){
		      var classId = rows[i].classId;
		      var del = rows[i].delete;
		      if(del!=1&&str2.search(classId)==-1){
		       str2=str2+","+classId;
		      }
		     }
		     if(str1==str2){
		      return true;
		     }else{
		      return false;
		     }
		    }
	  
	  
	  //判断活动送分是否小于0
	  function ifIntegralError(panelControlName){
		  if(panelControlName<0)return true;
		  else return false;
	  }
	  function viewMp4(sss){
		  var id=getPanelControlValue(sss);
		  //controller的地址+id
		  window.open(getRootPath_web()+"/viewMp4?id="+id);
	}

	  
	  //判断活动积分是否小于0
	  function ifJionintegralError(panelControlName){
		  if(panelControlName<0)return true;
		  else return false;
	  }
	  
	  function getRoot(){
			var root = window;
			try {
		        while (root.parent.document !== root.document) {
		            if (root.parent.document) {
		            	root = root.parent;
		            } else {
		                //chrome/ff 
		                break;
		            }
		        }
		    } catch(e){
		        // Safari needs try/catch so sets exception here
		    	console.log("getRoot " + e.name);
		    }
		    return root;
		}
	  
	  function openPic(conId){
		  var url = getPanelControlValue(conId);
		  if(url!=null){
			 window.open(url);
		  }
	  }
	  
	  function downloadModule(name,exportName){
		  window.open(getRootPath_web()+"/templeteDownload?fileName="+name+"&exportName="+exportName);
	  }
	  function okSelect(id,name){
			debugger
			var selectId = getPanelControlValue(id.split("|")[0]);
			var selectName = getPanelControlValue(name.split("|")[0]);
			parent.$("input[name='"+id.split("|")[1]+"']").val(selectId);
			parent.$("input[name='"+name.split("|")[1]+"']").prev().val(selectName);
			parent.$("input[name='"+name.split("|")[1]+"']").val(selectName);
			//window.parent.document.getElementsByName("ctlid").value=selectId;
			//window.parent.document.getElementsByName("ctlOrg").value=selectName;
			custom_close();
		}
