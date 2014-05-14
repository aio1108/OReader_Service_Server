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
					msg += '<xsl:value-of select="message"/>\n';
					</xsl:for-each>
					
					popupMessage("<xsl:value-of select="/hpMain/returnCodeDesc"/>",msg);
				});
				</xsl:if>
			    <xsl:if test="/hpMain/returnCode=true() and /hpMain/returnCode='000000' and /hpMain/redirectUrl!=''">
				window.location.href = "<xsl:value-of select="/hpMain/redirectUrl"/>";
				</xsl:if>				
			</script>
			    	
			<body>
				<div>
					<xsl:if test="//showHF = 'true'">
						<xsl:attribute name="class">wrap</xsl:attribute>
					</xsl:if>
				<xsl:call-template name="HeaderMenu"/>				
				<form name="myform" id="myform" method="post" enctype="multipart/form-data">
					<!-- Case Runner 運作所需的隱藏欄位 -->
					<input type="hidden" name="function" value="RunAction" />
					<input type="hidden" id="_action" name="_action">
						<xsl:attribute name="value">
							<xsl:value-of select="/hpMain/actionXml"/>
						</xsl:attribute>
					</input>
					<input type="hidden" id="_event" name="_event"/>
					<input type="hidden" name="_toUrl"/>
					<input type="hidden" name="_returnCode">
						<xsl:attribute name="value">
							<xsl:value-of select="/hpMain/returnCode"/>
						</xsl:attribute>
					</input>

					
					<div class="layout_main">
						<table border="0" width="100%" style="table-layout:fixed;">
							<tr>
							<xsl:if test="//showHF = 'true'">
							<td class="leftbg" style="width:180px;">
								<xsl:call-template name="LeftMenu"/>
							</td>
							</xsl:if>
							<td class="center" style="width:100%;">
								<xsl:if test="//actionOutput/result=true()">
									<xsl:value-of select="//actionOutput/result" disable-output-escaping="yes"/>
								</xsl:if>
							</td>				
						</tr>
						<tr>
							<td colspan="2">
								<xsl:call-template name="Footer"/>
							</td>
						</tr>
						</table>
					</div>
				</form>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
