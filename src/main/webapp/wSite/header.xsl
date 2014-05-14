<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:user="urn:user-namespace-here" version="1.0">
    <xsl:output method="html" encoding="utf-8" omit-xml-declaration="yes" indent="yes" standalone="yes"/>
    
    <xsl:template name="Header">
		
    	<head> 
			<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <xsl:if test="/hpMain/siteName=true()">
                <title><xsl:value-of select="/hpMain/siteName" /></title>
            </xsl:if>
            
            <link rel="stylesheet" type="text/css" href="css/design.css" />
			
			
			<!-- jQuery base -->
			<script type="text/javascript" src="js/jquery/jquery-1.6.2.js"></script>
			<script src="js/jquery/ui/jquery.ui.all.js"></script>
			<link rel="stylesheet" href="js/jquery/themes/base/jquery.ui.all.css"/>
			<link rel="stylesheet" href="js/jquery/themes/redmond/jquery-ui-1.8.16.custom.css"/>
			
			<!-- jQuery plugin -->
			<script src='js/jquery/jquery.json-2.3.js'></script>
			<!-- <script src="js/jquery/jquery.ui.datepicker-tw.js"></script>-->
			
			<!-- DHTMLX Menu -->
			<script src="js/dhtmlxMenu/dhtmlxcommon.js"></script>
			<script src="js/dhtmlxMenu/dhtmlxmenu.js"></script>
			<link rel="stylesheet" type="text/css" href="js/dhtmlxMenu/skins/dhtmlxmenu_evta.css"/>
			
			<!-- DHTMLX GRID -->
			<link rel="STYLESHEET" type="text/css">
				<xsl:attribute name="href"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/dhtmlxgrid.css</xsl:attribute>
			</link>
			<link rel="STYLESHEET" type="text/css" href="js/dhtmlxGrid/ext/dhtmlxgrid_pgn_bricks.css"/>
			<script language="javaScript">
			  <xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/dhtmlxgrid.js</xsl:attribute>
			</script>
			<script language="javaScript">
				<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/dhtmlxgridcell.js</xsl:attribute>
			</script>
			<script language="javaScript">
				<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_cntr.js</xsl:attribute>
			</script>
			<script language="javaScript">
				<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_link.js</xsl:attribute>
			</script>
			<script language="javaScript">
				<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_acheck.js</xsl:attribute>
			</script>
			<script language="javaScript">
				<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/dhtmlxGrid/ext/dhtmlxgrid_nxml.js</xsl:attribute>
			</script>	
			<script src="js/dhtmlxGrid/ext/dhtmlxgrid_drag.js"></script>
			<script src="js/dhtmlxGrid/ext/dhtmlxgrid_pgn.js"></script>
			<script src="js/dhtmlxGrid/ext/dhtmlxgrid_splt.js"></script>

			<script language="javaScript">
				var jQ = jQuery.noConflict();
				
				function alert(msg,func)
				{
					jQ("#dialog-message p").text(msg);
					jQ("#dialog-message").dialog({
						resizable: false,
						modal: true,
						title: "系統訊息",
						buttons: {
							Ok: function() {
								jQ(this).dialog("close");
							}
						},
						close: function(event, ui) { 
							if(func) func.call();
						}
					});
				}
				
				<xsl:if test="//msg != ''">
				jQ(function(){
					alert("<xsl:value-of select="//msg"/>",redirect);
				});
				</xsl:if>
				
				function redirect()
				{
					<xsl:if test="//redirect != ''">
					window.location.href = '<xsl:value-of select="//redirect"/>';
					</xsl:if>
				}
			</script>
			<xsl:apply-templates select="." mode="addScriptFile"/>
		</head>
    </xsl:template>
    
    <xsl:template name="HeaderMenu">
	    <div id="dialog-message" title="" style="display:none;"><p></p></div>
		<xsl:if test="//showHF = 'true'">
			<div id="zone.head" class="top">
				<div class="header">
				  <h1><a href="#" title=""><img src="images/space.gif" alt="" title="" /></a></h1>
				  <div class="time">系統時間:<em>2012/03/20 20:00</em></div>
				  <xsl:if test="//isLogin = 'true'">
				  <div class="user">
				  使用者：<em><xsl:value-of select="//userName"/></em><input name="" type="button" value="登出" class="btnlog" onclick="doLogout();"/>
				  </div>
				  </xsl:if>
				</div>
			</div>
			<script>
				function doLogout(){
					window.location.href = "<xsl:value-of select="//contextPath" />/wSite/Control?function=Logout";
				}
			</script>
		</xsl:if>
    </xsl:template>
    
    <xsl:template name="LeftMenu">
    	<div class="menu">
			<ul>
				<li><a href="#" class="here">案件受理</a>
					<ul>
						<li><a href="RunAction.jsp?_action=receive/query_form.xml">來台團聚</a></li>
						<li><a href="RunAction.jsp?_action=businessVisitGroup/applyTypeSelect.xml">參訪團體受理</a></li>
						<li><a href="RunAction.jsp?_action=businessVisitGroup/groupUpdateWork.xml">參訪團體登錄/補登</a></li>
						<li><a href="RunAction.jsp?_action=immApplyCaseForm/enterApplyCase.xml">大陸來台申請書登錄</a></li>
						<li><a href="RunAction.jsp?_action=postpone/form_query.xml">延期</a></li>
						<li><a href="RunAction.jsp?_action=resign/form_query.xml">加簽</a></li>
						<li><a href="#">入出境、居停留申請</a></li>
						<li><a href="#">加簽延期申請</a></li>
						<li><a href="#">一般人民申請</a></li>
						<li><a href="#">其他申請</a></li>
						<li><a href="#">補件作業</a></li>
						<li><a href="#">資料補登作業</a></li>            
					</ul>
				</li>
				<li><a href="#">資料維護</a>
					<ul>
						<li><a href="RunAction.jsp?_action=code/list.xml">代碼維護</a></li>
					</ul>
				</li>
				<li><a href="#">資料查詢</a></li>
				<li><a href="#">審核管理</a></li>
				<li><a href="#">統計分析</a></li>
				<li><a href="#">面談訪查管理</a></li>
				<li><a href="#">證函管理</a></li>
				<li><a href="#">掃描管理</a></li>
				<li><a href="#">分案管理</a></li>
				<li><a href="#">工作流管理</a></li>
				<li><a href="#">業務分析</a></li>
				<li><a href="#">績效管理統計</a></li>
				<li><a href="#">管制/審驗控管</a></li>
				<li><a href="#">系統管理</a></li>
			</ul>
		</div>   
    </xsl:template>
	
	<xsl:template match="hpMain" mode="addScriptFile">
		<!-- to be overwrite -->
  	</xsl:template>	

</xsl:stylesheet>