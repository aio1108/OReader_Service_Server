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
			    <xsl:if test="/hpMain/returnCode=true() and /hpMain/returnCode!='000000'">
				jQ(function() {
					var msg = "";
					<xsl:for-each select="//returnMessage">
					msg += '<xsl:value-of select="msg"/>\n';
					</xsl:for-each>
					
					popupMessage("<xsl:value-of select="/hpMain/returnCodeDesc"/>",msg);
				});
				</xsl:if>
			    <xsl:if test="/hpMain/returnCode=true() and /hpMain/returnCode='000000' and /hpMain/redirect=true()">
				window.location.href = "<xsl:value-of select="/hpMain/redirect"/>";
				</xsl:if>				
			</script>
			    	
			<body>
				<div class="wrap">
				<xsl:if test="/hpMain/caseRunerMode!='VIEW'">
				<xsl:call-template name="HeaderMenu"/>
				</xsl:if>
				
				<form name="myform" id="myform" method="post" enctype="multipart/form-data">
					<!-- Case Runner 運作所需的隱藏欄位 -->
					<input type="hidden" name="function" value="RunCase" />
					<input type="hidden" id="_event" name="_event"/>
					<input type="hidden" name="_toUrl"/>
					<input type="hidden" name="_returnCode">
						<xsl:attribute name="value">
							<xsl:value-of select="/hpMain/returnCode"/>
						</xsl:attribute>
					</input>
					<input type="hidden" name="wpinNo">
						<xsl:attribute name="value">
							<xsl:value-of select="//wpinno"/>
						</xsl:attribute>
					</input>
					<input type="hidden" name="actionId">
						<xsl:attribute name="value">
							<xsl:value-of select="//actionId"/>
						</xsl:attribute>
					</input>
					<input type="hidden" name="toActionId"/>
					<input type="hidden" name="_casename">
						<xsl:attribute name="value">
							<xsl:value-of select="//caseInfo/wpkind"/><xsl:value-of select="//caseInfo/wptype"/>
						</xsl:attribute>
					</input>
					<input type="hidden" name="_mode">
						<xsl:attribute name="value">
							<xsl:value-of select="/hpMain/caseRunerMode"/>
						</xsl:attribute>
					</input>

				
				<div class="layout_main">
					<table border="0" class="layout" summary="layout table">
					<tr>
						<td class="leftbg">
							<xsl:call-template name="LeftMenu"/>
						</td>
						<td class="center" style="width:760px;">
							<div class="clear">
								<div class="aRight">
									
								</div>
							</div>
							<div class="capturePic" style="text-align:right;">
								<input id="btn1" type="button" value="應檢附項目一覽表" class="btn_com" />
								<input id="btn2" type="button" value="本申請案申辦流程" class="btn_com" />
								<a href="#"><img src="images/btn_capture.gif" alt="影像擷取" /></a>
							</div>
							<script>
							jQ(function(){
								jQ("#btn1").button();
								jQ("#btn2").button();
							});
							</script>
							
							<!-- 
							<xsl:if test="/hpMain/actionHTML=true()">
								<xsl:value-of select="//actionHTML" disable-output-escaping="yes"/>
							</xsl:if>
							 -->
							
							<xsl:if test="//actionOutput/result=true()">
								<xsl:value-of select="//actionOutput/result" disable-output-escaping="yes"/>
							</xsl:if>
						</td>
					</tr>
					<xsl:if test="/hpMain/caseRunerMode!='VIEW'">
					<tr>
						<td colspan="2">
							<xsl:call-template name="Footer"/>
						</td>
					</tr>
					</xsl:if>
					</table>
				</div>
				</form>
				
				
				
				<xsl:if test="/hpMain/caseRunerMode='VIEW'">
				<script>
				jQ(function(){
					jQ(".div_center").hide();
					disableAllChild();
				});
				</script>
				</xsl:if>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
