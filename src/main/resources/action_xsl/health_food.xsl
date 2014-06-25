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
        <xsl:for-each select="Body/Data">
          <records>
            <id><xsl:value-of select="ID" /></id>
            <name><xsl:value-of select="c_name" /></name>
            <ingredient><xsl:value-of select="basic" /></ingredient>
            <effect><xsl:value-of select="effect" /></effect>
            <comment><xsl:value-of select="comment" /></comment>
            <checkDate><xsl:value-of select="chk_date" /></checkDate>
            <company><xsl:value-of select="company" /></company>
            <factory><xsl:value-of select="factory" /></factory>
            <checkType><xsl:value-of select="chk_type" /></checkType>
            <relateURL1><xsl:value-of select="url2" /></relateURL1>
            <relateURL2><xsl:value-of select="url3" /></relateURL2>
          </records>
        </xsl:for-each>
      </root>
    </xsl:template>
</xsl:stylesheet>