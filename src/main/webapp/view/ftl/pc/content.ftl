<html>
<head>
<script type="text/javascript">

	function onload(){
		var inserthtml = '<iframe id="scorm_object" style="width:100%;height:100%" type="text/html" allowfullscreen="allowfullscreen" webkitallowfullscreen="webkitallowfullscreen" mozallowfullscreen="mozallowfullscreen" src="file:///D:/document/work/201807/scorm-demo/task one theory/index_lms.html"></iframe>'
		$("#divId").append(inserthtml);
	}

	function open_win() {
		window.open("file:///D:/document/work/201807/scorm-demo/task one theory/story_html5.html")
	}

	function init(){
		console.log("init");
		API = new API_Calls();
	   
	}

	function API_Calls() {
		console.log("calls-init");
		this.LMSInitialize = LMSInitialize;
		this.LMSSetValue = LMSSetValue;
		this.LMSGetValue = LMSGetValue;
		this.LMSCommit = LMSCommit;
		this.LMSFinish = LMSFinish;
		this.LMSGetLastError = LMSGetLastError;
		this.LMSGetErrorString = LMSGetErrorString;
		console.log("calls-end");
	}

    function LMSInitialize(value){      
		console.log("LMSInitialize:value"+value);
        //alert("LMSInitialize:" + value);
        var reCode = "";
        enterLMSAjax("INIT","DoType=INIT&courseID=<%=courseID%>");
        return true;
    }

    function LMSSetValue(name, value){
        //alert("LMSFrame,LMSSetValue:" + name + ":" + value);
        //enterLMSAjax("SET",value);
        console.log("LMSFrame,LMSSetValue:" + name + ":" + value);
		
        switch(name)
        {
            case "cmi.core.student_id":
                buttonform.studentID.value = value;
                break;
            case "cmi.core.student_name":
                buttonform.studentName.value = value;
                break;
            case "cmi.core.lesson_location":
                buttonform.scoLocation.value = value;
                break;
            case "cmi.core.credit":
                buttonform.Credit.value = value;
                break;
            case "cmi.core.lesson_status":
                if(buttonform.runStatus.value != "completed"){
                  buttonform.runStatus.value = value;
                }
                break;
            case "cmi.core.entry":
                buttonform.Entry.value = value;
                break;
            case "cmi.core.score":
                buttonform.score.value = value;
                break;
            case "cmi.core.score.raw":
                buttonform.score.value = value;
                break;
            case "cmi.core.total_time":
                buttonform.totalTime.value = value;
                break;
            case "cmi.core.lesson_mode":
                buttonform.lessonMode.value = value;
                break;
            case "cmi.core.exit":
                buttonform.LessonExit.value = value;
                break;
            case "cmi.core.session_time":
                buttonform.sessionTime.value = value;
                break;
            case "cmi.suspend_data":
                buttonform.SuspendData.value = value;
                break;
            default:
                break; 
        }  
        LMSCommit(value);
        return "";

    }

    function LMSGetValue(name){
		console.log("LMSGetValue:" + name);
        //alert("LMSGetValue:" + name);        
        var reCode="";
        //reCode = enterLMSAjax("GET","");
        
        switch(name){
            case "cmi.core.student_id":
                return buttonform.studentID.value;
                break;
            case "cmi.core.student_name":
                return buttonform.studentName.value;
                break;
            case "cmi.core.lesson_location":
                return buttonform.scoLocation.value;
                break;
            case "cmi.core.credit":
                return buttonform.Credit.value;
                break;
            case "cmi.core.lesson_status":
                return buttonform.runStatus.value;
                break;
            case "cmi.core.entry":
                return buttonform.Entry.value;
                break;
            case "cmi.core.score":
                return buttonform.score.value;
                break;
            case "cmi.core.score.raw":
                return buttonform.score.value;
                break;
            case "cmi.core.total_time":
                return buttonform.totalTime.value;
                break;
            case "cmi.core.lesson_mode":
                return buttonform.lessonMode.value;
                break;
            case "cmi.core.exit":
                return buttonform.LessonExit.value;
                break;
            case "cmi.core.session_time":
                return buttonform.sessionTime.value;
                break;
            case "cmi.suspend_data":
                return buttonform.SuspendData.value;
                break;
            default:
                break; 
        }  
        return reCode;
    }

    function LMSCommit(value){        
        //alert("LMSCommit:" + value);
		console.log("LMSCommit:" + value);
        var reCode="";
        var param = "DoType=COMM&courseID="+ buttonform.courseID.value;
        param += "&lessonMode=" + buttonform.lessonMode.value;
        param += "&runStatus=" + buttonform.runStatus.value;
        param += "&starttime=" + buttonform.StartTime.value;
        param += "&scoLocation=" + buttonform.scoLocation.value; 
        param += "&score=" + buttonform.score.value;
        param += "&LessonExit=" + buttonform.LessonExit.value;
        param += "&Entry=" + buttonform.Entry.value;
        param += "&sessionTime=" + buttonform.sessionTime.value;
        param += "&SuspendData=" + buttonform.SuspendData.value;
        //param += "&LastSCOID=" + buttonform.LastSCOID.value;

        enterLMSAjax("COMM",param);
        return reCode;
    }

    function LMSFinish(value){        
        //alert("LMSFinish:" + value);
		console.log("LMSFinish:" + value);
        var reCode="";
        reCode = LMSCommit(value);
        //changeSCOContent();
        return reCode;
    }

    //通过ajax获得服务器当前时间
    function GetServerTime() {
		console.log("GetServerTime");
        var start = "";
        start = enterLMSAjax("TIME","");
		console.log("GetServerTime：start"+start);
        return start;
    }
    
    function LMSGetLastError()
    {
		console.log("LMSGetLastError");
        var reCode = "0";
        //reCode = enterLMSAjax("LastError","");
        return reCode;
    }
    
    function LMSGetErrorString(value)
    {
		console.log("LMSGetErrorString");
        var reCode = "";
        //reCode = enterLMSAjax("ErrorString",value);
        return reCode;
    }       

    function enterLMSAjax(p_dotype,p_value)
    {
		console.log("enterLMSAjax：p_dotype："+p_dotype+"p_value:"+p_value);
        //var DataToSend = "TaskName="+p_name+"&TaskValue="+p_value+"&DoType="+p_dotype; 
        var xmlhttp;
        var reCode;
        if (window.XMLHttpRequest)
        {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        }
        else
        {// code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange=function()
        {
            if (xmlhttp.readyState==4 && xmlhttp.status==200)
            {
                reCode = xmlhttp.responseText;
                if(reCode.indexOf("<INIT>")>=0){ accessInfo(reCode); }
            }
        }
        //xmlhttp.open("POST","ajaxScoData.jsp?TaskName="+p_name+"&TaskValue="+p_value+"&DoType="+p_dotype,true);
        xmlhttp.open("POST","ajaxScoData.jsp",true);
        xmlhttp.setRequestHeader( "Content-Type","application/x-www-form-urlencoded"); 
        xmlhttp.send(p_value);
    }

	function doConfirm(){
		console.log("doConfirm");
		if( confirm("现在退出学习记录将不能保存.  确认退出吗?") ){
		   window.parent.frames[4].document.location.href = "LMSMenu.jsp";
		}else{
		}
	}

	function closeWin(){
		console.log("closeWin");
		 //alert("You have closed the window.22222");
		 //parent.opener.location.reload();
	}
</script>
</head>
<body onload="init();" onUnLoad="LMSCommit('')">
<input type=button value="Open Window" onclick="open_win()" />
<div id="disId">
	<form id="scormviewform" method="post" action="${ctx}/content/scorm">
		模式: <input type="radio" id="b" name="mode" value="browse">
			<label for="b" id="yui_3_17_2_1_1533872076665_176">预览</label>
			<input type="radio" id="n" name="mode" value="normal" checked="checked">
			<label for="n">普通</label><input type="hidden" name="display" value="popup"><br>
			<input type="hidden" name="scoid" value="465"><input type="hidden" name="cm" value="2111">
			<input type="hidden" name="currentorg" value="task_two_theory_ORG">
			<input type="submit" value="进入" id="yui_3_17_2_1_1533872076665_177"></form>
</div>

</body>
</html>