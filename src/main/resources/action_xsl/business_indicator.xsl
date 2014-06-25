<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:html="http://www.w3.org/TR/REC-html40"
 exclude-result-prefixes="ss o x html">
    <xsl:output method="xml" encoding="utf-8" indent="yes" />
    <xsl:template match="/">
      <root>
        <xsl:for-each select="ss:Workbook/ss:Worksheet/ss:Table/ss:Row">
          <xsl:if test="position()!=1">
            <records>
              <date><xsl:value-of select="ss:Cell[1]/ss:Data" /></date>
              <leadGeneralIndicator><xsl:value-of select="ss:Cell[2]/ss:Data" /></leadGeneralIndicator>
              <leadGeneralIndicatorWithoutTrend><xsl:value-of select="ss:Cell[3]/ss:Data" /></leadGeneralIndicatorWithoutTrend>
              <sameGeneralIndicator><xsl:value-of select="ss:Cell[4]/ss:Data" /></sameGeneralIndicator>
              <sameGeneralIndicatorWithoutTrend><xsl:value-of select="ss:Cell[5]/ss:Data" /></sameGeneralIndicatorWithoutTrend>
              <behindGeneralIndicator><xsl:value-of select="ss:Cell[6]/ss:Data" /></behindGeneralIndicator>
              <behindGeneralIndicatorWithoutTrend><xsl:value-of select="ss:Cell[7]/ss:Data" /></behindGeneralIndicatorWithoutTrend>
            </records>
          </xsl:if>
        </xsl:for-each>
      </root>
    </xsl:template>
</xsl:stylesheet>