<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Photo Viewer</title>
<script type="text/javascript" src="js/jquery/jquery-1.6.2.js"></script>
<script>
var jQ = jQuery.noConflict();
</script>
<script src="js/jquery/ui/jquery.ui.all.js"></script>
<link rel="stylesheet" href="js/jquery/themes/base/jquery.ui.all.css">
<link rel="stylesheet" href="js/jquery/themes/redmond/jquery-ui-1.8.16.custom.css">
<script src="js/jquery/jquery.json-2.3.js"></script>
<link rel="stylesheet" href="js/jquery/iviewer/jquery.iviewer.css">
<script type="text/javascript" src="js/jquery/iviewer/jquery.mousewheel.min.js"></script>
<script type="text/javascript" src="js/jquery/iviewer/jquery.iviewer.js"></script>
<script type="text/javascript" src="js/jquery/iviewer/jQueryRotate3.js"></script>
<script type="text/javascript" src="js/action.js"></script>
<script>
var imgViewer;
jQ(document).ready(function () {
	self.resizeTo(screen.availWidth,screen.availHeight);
	resizeViewArea();
	
	imgViewer = jQ("#imgViewerArea").iviewer({
		update_on_resize: true,
		onSaveFile: saveFile
	});
	imgViewer.iviewer("loadImage", "<%= request.getParameter("img") %>");
   
});

jQ(window).resize(resizeViewArea);

function resizeViewArea()
{
	var w = jQ(window).width();
	var h = jQ(window).height();
	jQ("#imgViewerArea").css({width:w+"px", height:h+"px"});
	
	if(imgViewer)
	{
		imgViewer.iviewer("fit");
	}
}
</script>
</head>
<body style="margin:0px;">
<div id="imgViewerArea"></div>
</body>
</html>