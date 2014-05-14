<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" xmlns:user="urn:user-namespace-here" version="1.0">
    <xsl:output method="html" encoding="utf-8" omit-xml-declaration="yes" indent="yes" standalone="yes"/>
    
    <xsl:template name="Footer">
		<xsl:if test="//showHF = 'true'">
			<!-- footer start-->
			<div id="zone.footer">
				<div class="footer">
				<p>內政部入出國及移民署 版權所有 © NATIONAL IMMIGRATION AGENCY<br/>地址：100-66台北市廣州街15號 電話：(02)2388-9393</p>
				</div>
			</div>
			<!-- footer end-->
		</xsl:if>
    </xsl:template>
</xsl:stylesheet>