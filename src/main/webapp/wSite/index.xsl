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
			<script>
			    function doInit()
			    {
			        
			    }
			</script>
			<body onload="doInit();">
			<xsl:call-template name="HeaderMenu"/>
				<!--main 中間2欄區-->
				<table border="0" class="layout" summary="layout table">
					<tr>
						<td style="vertical-align: top">
							<div class="Title_h1"><a href="Control?function=HyPatternContent&amp;hypattern=list.do&amp;listspec=AnonymousBulletinList.xml&amp;kpsn=1">公告消息</a></div>
							<!--start-->
							<div class="mp_news">
								<div class="lpTb3">
								   <table cellpadding="0" cellspacing="0"  summary="">
									   <caption></caption>
									 <tr>
									   <th width="12%" nowrap="nowrap" class="number" scope="col">發佈日期</th>
									   <th width="72%" scope="col">標題</th>
									 </tr>
									 <xsl:for-each select="//Article">
										<tr>
											<td scope="col" class="number" nowrap="nowrap"><xsl:value-of select="ArticleField[name='startdate']/Value"/></td>
											<td scope="col">
												<span class="span">
													<a>
														<xsl:attribute name="href">Control?function=HyPatternContent&amp;hypattern=UpdateShowForm.do&amp;formType=AnonymousBulletinEdit.xml&amp;pk=<xsl:value-of select="ArticleField[name='bid']/name"/>&amp;pkvalue=<xsl:value-of select="ArticleField[name='bid']/Value"/></xsl:attribute>
														<xsl:value-of select="ArticleField[name='title']/Value"/>
													</a>
												</span>
											</td>
										</tr>
									 </xsl:for-each>
								   </table>
								</div>
	 
							</div>                      
							<!--end--> 
						</td>
					
					<xsl:choose>
					<xsl:when test="/hpMain/isLogin = 'false' or /hpMain/isLogin = false()">
						<td class="right_mp">
							<!--login start -->
							<div class="login">					
								<div class="headline">憑證登入</div>
								<div class="body">                        
									
									<table>
										<form method="post" name="loginform" action="Control?function=LoginPage" >
										<applet id="client" height="0" width="0">
											<param name="code" value="tw.com.hyweb.ca.CAClient"/>
											<param name="codebase" value="."/>
											<param name="archive">
												<xsl:attribute name="value"><xsl:value-of select="/hpMain/contextPath" />/ca/CAClient.jar,<xsl:value-of select="/hpMain/contextPath" />/ca/lib/bcprov-jdk15-1.43.jar,<xsl:value-of select="/hpMain/contextPath" />/ca/lib/commons-codec-1.4.jar,<xsl:value-of select="/hpMain/contextPath" />/ca/lib/P11JNI-2.1.jar,<xsl:value-of select="/hpMain/contextPath" />/ca/lib/native-1.0.0.jar,<xsl:value-of select="/hpMain/contextPath" />/ca/lib/json-20090211.jar</xsl:attribute>
											</param>
											<param name="mayscript" value="true"/>
											<param name="codebase_lookup" value="false"/>
										</applet>
										<input type="hidden" name="loginType" id="loginType" value="ca"/>
										<input type="hidden" name="serialNumber" id="serialNumber" />
										<input type="hidden" name="content" id="content" />
										<input type="hidden" name="p7SignedData" id="p7SignedData" />
										<tr>
											<td colspan="2" class="Txt_bold">請插入憑證, 點選登入後, 輸入憑證PIN碼 </td>
										</tr>
										<tr>
											<td colspan="2" class="Txt_bold"></td>
										</tr>
										<tr>
											<td colspan="2" align="center"><input type="button" name="btnLogin" id="btnLogin" value="登入" class="btn" onclick="doCALogin()"/></td>
										</tr>
										<tr>
											<td colspan="2">
											<table border="0" cellspacing="0" cellpadding="4" width="96%" align="center">
												<tbody>
													<tr>
													<!-- <td>初次使用憑證登入，請安裝<a href="http://www.cp.gov.tw/portal/HiCOS%20Client%20v2.1.2.zip">HICOS元件</a>，安裝元件與登入問題，請詳<a href="http://www7.www.gov.tw/egov/faq/FAQ06.html">常見問題</a></td> -->
													<!-- <td>若尚未註冊憑證，請先到<a href="#" tager="_self" onclick="alert('檢視帳號資訊');">帳號資訊檢視頁面</a>進行註冊</td> -->
													</tr>
												</tbody>
											</table>
											</td>
										</tr>
										</form>
									</table>
									
								</div>
								<br/>
								<div class="headline">一般登入</div>
								<div class="body">
									<table>
										<form method="post" action="Control?function=LoginPage" onsubmit="return doUserLogin()">
											<input type="hidden" name="loginType" id="loginType" value="user"/>
											<tr>
												<td colspan="2" class="Txt_bold">請輸入註冊時的身分證字號/統編作為帳號</td>
											</tr>
											<tr>
												<td><label for="userid">帳號</label></td>
												<td><input type="text" name="userid" id="userid" size="10"/></td>
											</tr>
											<tr>
												<td rowspan="2"><label for="userPwd">密碼</label></td>
												<td><input type="password" name="userPwd" id="userPwd" size="10"/></td>
											</tr>
											<tr>
												<td colspan="2"><input type="submit" name="btnLogin" id="btnLogin" value="登入" class="btn"/></td>
												<!-- <td><input type="button" name="btnRegister" id="btnRegister" value="註冊帳號" class="btn_green_s" onclick="doRegister();"/></td> -->
											</tr>
										</form>
									</table>
								</div>
							</div> 
							<!--login end--> 
						</td>
					</xsl:when>
					</xsl:choose>
				</tr>
				</table>
				
				<xsl:call-template name="Footer"/>
				
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/wSite/js/jquery/jquery-1.6.2.js</xsl:attribute>
				</script>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/ca/deployJava.js</xsl:attribute>
				</script>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="/hpMain/contextPath" />/ca/cert.js</xsl:attribute>
				</script>
				<script src="js/action.js" language="javascript"></script>
				<script type="text/javascript">
				jQ(document).ready(function($){
				
				});
								
				function doCALogin(){
				
					//caClientInit();
					
					//var client = document.getElementById('client');
					//client.loginURL(0); // 0 - CARD, 1 - P12
					
					//var cr = setTimeout("chkLoginURLResult()",2000);
				}

				function doUserLogin(){
					/*
					if(jQ.trim(jQ('#userid').val()) == ''){
						popupMessageAndFocus(jQ('#userid'),'請輸入帳號!');
						return false;
					} else if(jQ.trim(jQ('#userPwd').val()) == ''){
						popupMessageAndFocus(jQ('#userPwd'),'請輸入密碼!');
						return false;
					}
					return true;
					*/
				}
				
				function doRegister(){
					/*
					window.location.href = "Control?function=RegisterPage";
					*/
				}
				</script>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>
