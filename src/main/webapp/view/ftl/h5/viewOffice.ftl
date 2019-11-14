
<html>
<head>
<script src="https://cdn.bootcss.com/jquery/1.11.0/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		var encodeUrl = "https://view.officeapps.live.com/op/view.aspx?src="+encodeURIComponent("${url}");
		$("#frame").attr('src',encodeUrl);
		
		$("#frame").load(function(){
	        $(this).contents().find("div.WACRibbonPanel").css('display','none');
	    });
	})
	
</script>
</head>
<body>

<iframe id="frame" width='100%' height='97%' frameborder='1'>
</iframe>

</body>
</html>
