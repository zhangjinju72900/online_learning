<!DOCTYPE html>
<html>
<head>
	<title></title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<meta name="viewport" id="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui" />
	<link href="${ctx}/view/common/mobile/player.css" rel="stylesheet" type="text/css" />

	<script src="${ctx}/view/common/lms/APIConstants.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/Configuration.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/UtilityFunctions.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/SCORM2004Functions.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/SCORMFunctions.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/AICCFunctions.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/NONEFunctions.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/LMSAPI.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctx}/view/common/lms/API.js" type="text/javascript" charset="utf-8"></script>

	<script language="JavaScript1.2">

	strContentLocation = "story_html5.html";  //Put the link to the start of the content here.

	function LoadContent(){
	}
	
	</script>
</head>
<body onbeforeunload="Unload()" onunload="Unload()">

	<div class="interstitial white"></div>
	<span id="loadingSpinner" class="loading"></span>

	<div class="framewrap">
		<div class="landscape_menu no_logo">
			<div class="logo_background">
				<div class="logo_section"></div>
				<div class="presenter_video_section"></div>
				<div class="presenter_section"></div>
			</div>
			<ul id="menu_tabs" class="menu_nav">
			</ul>
			<div id="menu_container" class="menu_container scrollarea">
				<ul id="slide_list" class="menu_list">
				</ul>
			</div>
		</div>
		
		<div class="contentpane">
			<div id="slidebg"></div>
			<div id="topbar">
				<div id="storytitle">
					<div id="storytitle_section"></div>
					<div id="timer_section"></div>
				</div>
				<div id="toplinks_left"></div>
				<div id="toplinks_right"></div>
			</div>
			<div id="slideframe">
				<div id="slidecontainer">
				</div>
			</div>
			<div id="controls">
				<div id="control-finish" class="controlbar-button right" style="display:none;"><a class="icon finish-slide"></a><div class="label finish">FINISH</div></div>
				<div id="control-submit" class="controlbar-button right" style="display:none;"><a class="icon submit-slide"></a><div class="label submit">SUBMIT</div></div>
				<div id="control-next" class="controlbar-button right"><div class="label next">NEXT</div><a class="icon next-slide"></a></div>
				<div id="control-previous" class="controlbar-button right"><a class="icon previous-slide"></a><div class="label prev">PREV</div></div>
				<div id="control-submitall" class="controlbar-button" style="display:none;"><div class="label submitall">Submit All</div></div>
				<div id="control-volume" class="controlbar-button compact"><a class="icon volume half"></a></div>

			
				<span id="indicator"></span>
			</div>
		</div>
	</div>
	<iframe name="AICCComm" src="${ctx}/view/common/lms/AICCComm.html" style="display:none;"></iframe>
	<iframe name="rusticisoftware_aicc_results" src="${ctx}/view/common/lms/blank.html" style="display:none;"></iframe>
	<iframe name="NothingFrame" src="${ctx}/view/common/lms/blank.html" style="display:none;"></iframe>

</body>


<script src="../view/common/mobile/player_compiled.js" type="text/javascript"></script>

<script>

	player.autoplay = true;
	trace.suppress = true;
	console.suppress = true;
	player.loopable = false;
	player.allowRightClick = false;
	player.isHTML5 = true;

	var globals = {};
	try
	{
		globals.strTitle = "task one theory";
		globals.bHtml5Supported = true;
		globals.bLMS = true;
		globals.bAOSupport = false;
		globals.strContentFolder = "${ctx}/view/common/story_content";
		globals.strContentLocation = "${ctx}/view/common/story_content/story_html5.html";
		globals.bProjector = false;
		globals.strSwfFile = "${ctx}/view/common/story_content/story.swf";
		globals.nWidth = 740;
		globals.nHeight = 634;
		globals.strScale = "noscale";   // noscale | show all
		globals.strBrowserSize = "default";      // default, fullscreen, optimal
		globals.strBgColor = "#FAC090";
		globals.strAlign = "middle";
		globals.strQuality = "best";
		globals.bCaptureRC = false;
		globals.strFlashVars = "";
		globals.bScrollbars = true;
		globals.bWarnOnCommitFail = false;
		globals.strWMode = "window"; // opaque | window (use "window" for optimal performance, opaque for webobject support)
		globals.bUseHtml5 = true && globals.bHtml5Supported;
		globals.bUseMobilePlayer = true;
		globals.biOS = (navigator.userAgent.indexOf("AppleWebKit/") > -1 && navigator.userAgent.indexOf("Mobile/") > -1);
		globals.biPad = (navigator.userAgent.indexOf("iPad") > -1);
	}
	catch (e) { 
		console.log("error");	
	}

	if (globals.biOS)
	{
		if (globals.bUseMobilePlayer && globals.biPad)
		{
			location.replace("amplaunch.html");
		}
	}

	player.initGlobals();


	var strParams = document.location.search.substr(1);
	var arrParams = strParams.split("&");
	var arrCmdValue;

	for (var i = 0; i < arrParams.length; i++)
	{
		arrCmdValue = arrParams[i].split("=");
		switch (arrCmdValue[0])
		{
			case "lms":
				globals.bLMS = true;
				break;
			case "warncommit":
				globals.bWarnOnCommitFail = (arrCmdValue[1] == "1");
				break;
		}
	}


	if (globals.bLMS)
	{
		document.write("<scr" + "ipt src='${ctx}/view/common/lms/lms.js' type='text/javascript' charset='utf-8'><\/scr" + "ipt>");
	}

	document.write("<scr" + "ipt src='${ctx}/view/common/mobile/data.js' type='text/javascript' charset='utf-8'><\/scr" + "ipt>");
	document.write("<scr" + "ipt src='" + globals.strContentFolder + "/frame.js' type='text/javascript' charset='utf-8'><\/scr" + "ipt>");
	document.write("<scr" + "ipt src='" + globals.strContentFolder + "/user.js' type='text/javascript' charset='utf-8'><\/scr" + "ipt>");
//	if (window.location.protocol == "file:")
//	{
		//document.write("<scr" + "ipt src='${ctx}/view/common/mobile/masks.js' type='text/javascript' charset='utf-8'><\/scr" + "ipt>");
//	}
</script>

</html>


<script>
	window.scormdriver_content = window;
</script>