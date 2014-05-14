<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:user="urn:user-namespace-here" version="1.0">
    <xsl:output method="html" encoding="utf-8" omit-xml-declaration="yes" indent="yes" standalone="yes"/>
    <xsl:include href="header.xsl"/>
    <xsl:include href="footer.xsl"/>
    <xsl:template match="/">
        <xsl:text disable-output-escaping="yes">
            <![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">]]>
        </xsl:text>
        
        <html>
        	<xsl:call-template name="Header"/>
        	
			<script src="js/action.js"></script>
			
			<script>		       
			    <xsl:if test="/hpMain/msgInfo=true()">
				jQ(function() {
					var msg = "<xsl:value-of select="/hpMain/msgInfo"/>";	
					popupMessage("",msg);
				});
				</xsl:if>
				jQ(function(){
					initTabHeight();
				});
				
				jQ(window).resize(function() {
					initTabHeight();
				});
				
				function initTabHeight()
				{
					var h = jQ(document).height();
					jQ("#tabs").css({"height": h-40+"px"});
					jQ("iframe").css({"height": h-90+"px"});
				}	
			</script>
			    	
			<body leftmargin="0" topmargin="0" scroll="no">
				<xsl:variable name="url">Control?function=RunCase&amp;wpinNo=<xsl:value-of select="/hpMain/caseNo"/>&amp;_mode=view</xsl:variable>
				
				
				<div id="tabs" style="margin:15px; font-size:80%;">
					<ul>
						<li><a href="#tabs-1">初審</a></li>
						<li><a href="#tabs-2">複審</a></li>
						<li><a href="#tabs-3">領隊決行</a></li>
					</ul>
					<div id="tabs-1">
						<iframe name="frame1" frameborder="0" border="0" cellspacing="0" style="width:100%;">
							<xsl:attribute name="src"><xsl:value-of select="$url"/>&amp;_flowStatus=C1</xsl:attribute>
						</iframe>
					</div>
					
					<div id="tabs-2">
						<iframe name="frame2" frameborder="0" border="0" cellspacing="0" style="width:100%;">
							<xsl:attribute name="src"><xsl:value-of select="$url"/>&amp;_flowStatus=D1</xsl:attribute>
						</iframe>
					</div>
					
					<div id="tabs-3">
						<iframe name="frame3" frameborder="0" border="0" cellspacing="0" style="width:100%;">
							<xsl:attribute name="src"><xsl:value-of select="$url"/>&amp;_flowStatus=E1</xsl:attribute>
						</iframe>
					</div>
				</div>
				
				
				<script>
				jQ(function() {
					jQ("#tabs").tabs();
				});
				</script>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
