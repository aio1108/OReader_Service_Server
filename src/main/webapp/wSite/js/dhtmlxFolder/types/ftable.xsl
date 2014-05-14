<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="icons_src_dir">.</xsl:variable>
	<xsl:template match="item">
		<div>
		<div style="float:left;width:17px;">
			<!-- icon -->
			<img border='0'>
				<xsl:if test="./@type='dir'">
					<xsl:attribute name="src"><xsl:value-of select="$icons_src_dir"/>/ico_fldr_18.gif</xsl:attribute>
				</xsl:if>
				<xsl:if test="./@type!='dir'">
					<xsl:attribute name="src"><xsl:value-of select="$icons_src_dir"/>/ico_<xsl:value-of select="substring-after(./@name,'.')"/>_18.gif</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="onerror">this.onerror = null;this.src='<xsl:value-of select="$icons_src_dir"/>/ico_unknown_18.gif';</xsl:attribute>
			</img>
		</div>
		<!-- label -->
		<div class="dhx_folders_FTABLE_item_text" style="float:left;width:400px;"><span style="padding-left:2px;padding-right:2px;">
			<xsl:if test="./@type='dir'">
				<xsl:value-of select="./@name"/>
			</xsl:if>
			<xsl:if test="./@type!='dir'">
				<xsl:value-of select="substring-before(./@name,'.')"/>
			</xsl:if>
		</span></div>

		
		</div>
	</xsl:template>
</xsl:stylesheet>