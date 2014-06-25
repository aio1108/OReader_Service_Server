<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:html="http://www.w3.org/TR/REC-html40">
    <xsl:output method="xml" encoding="utf-8" indent="yes" />
    <xsl:template match="/">
      <root>
        <xsl:for-each select="GenericData/DataSet/Series[2]/SeriesProperty/Obs">
          <records>
            <date><xsl:value-of select="@TIME_PERIOD" /></date>
            <GDP><xsl:value-of select="@OBS_VALUE" /></GDP>
          </records>
        </xsl:for-each>
      </root>
    </xsl:template>
</xsl:stylesheet>