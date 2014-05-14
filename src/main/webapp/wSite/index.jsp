<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:user="urn:user-namespace-here"
	xmlns:msxsl="urn:schemas-microsoft-com:xslt">
<head>
<META http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta content="IE=EmulateIE7" http-equiv="X-UA-Compatible">
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
			<link href="css/design.css" type="text/css" rel="stylesheet">
				<script src="js/jquery/jquery-1.6.2.js" type="text/javascript"></script>
				<script src="js/jquery/ui/jquery.ui.all.js"></script>
				<link href="js/jquery/themes/base/jquery.ui.all.css"
					rel="stylesheet">
					<link href="js/jquery/themes/redmond/jquery-ui-1.8.16.custom.css"
						rel="stylesheet">
						<script src="js/jquery/jquery.json-2.3.js"></script>
						<script src="js/dhtmlxMenu/dhtmlxcommon.js"></script>
						<script src="js/dhtmlxMenu/dhtmlxmenu.js"></script>
						<link href="js/dhtmlxMenu/skins/dhtmlxmenu_evta.css"
							type="text/css" rel="stylesheet">
							<link type="text/css" rel="STYLESHEET"
								href="/nii_web/wSite/js/dhtmlxGrid/dhtmlxgrid.css">
								<link href="js/dhtmlxGrid/ext/dhtmlxgrid_pgn_bricks.css"
									type="text/css" rel="STYLESHEET">
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/dhtmlxgrid.js"></script>
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/dhtmlxgridcell.js"></script>
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_cntr.js"></script>
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_link.js"></script>
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/excells/dhtmlxgrid_excell_acheck.js"></script>
									<script language="javaScript"
										src="/nii_web/wSite/js/dhtmlxGrid/ext/dhtmlxgrid_nxml.js"></script>
									<script src="js/dhtmlxGrid/ext/dhtmlxgrid_drag.js"></script>
									<script src="js/dhtmlxGrid/ext/dhtmlxgrid_pgn.js"></script>
									<script src="js/dhtmlxGrid/ext/dhtmlxgrid_splt.js"></script>
									<script language="javaScript">
										var jQ = jQuery.noConflict();

										function alert(msg, func) {
											jQ("#dialog-message p").text(msg);
											jQ("#dialog-message")
													.dialog(
															{
																resizable : false,
																modal : true,
																title : "系統訊息",
																buttons : {
																	Ok : function() {
																		jQ(this)
																				.dialog(
																						"close");
																	}
																},
																close : function(
																		event,
																		ui) {
																	if (func)
																		func
																				.call();
																}
															});
										}

										function redirect() {

										}
									</script>
</head>
<script src="js/action.js"></script>
<script></script>
<body>
	<div class="wrap">
		<div style="display: none;" title="" id="dialog-message">
			<p></p>
		</div>
		<div class="top" id="zone.head">
			<div class="header">
				<h1>
					<a title="" href="#"><img title="" alt=""
						src="images/space.gif"></a>
				</h1>
				<div class="time">
					系統時間:<em>2012/03/20 20:00</em>
				</div>
			</div>
		</div>
		<script>
			function doLogout() {
				window.location.href = "/nii_web/wSite/Control?function=Logout";
			}
		</script>
		<form enctype="multipart/form-data" method="post" id="myform"
			name="myform">
			<input value="RunAction" name="function" type="hidden"><input
				name="_action" id="_action" type="hidden"
				value="businessVisitGroup/applyTypeSelect.xml"><input
					name="_event" id="_event" type="hidden"><input
						name="_toUrl" type="hidden"><input name="_returnCode"
							type="hidden" value="000000">
								<div class="layout_main">
									<table style="table-layout: fixed;" width="100%" border="0">
										<tr>
											<td style="width: 180px;" class="leftbg">
												<div class="menu">
													<ul>
														<li><a class="here" href="#">案件受理</a>
															<ul>
																<li><a
																	href="RunAction.jsp?_action=receive/query_form.xml">來台團聚</a>
																</li>
																<li><a
																	href="RunAction.jsp?_action=businessVisitGroup/applyTypeSelect.xml">團體申請案</a>
																</li>
																<li><a
																	href="RunAction.jsp?_action=businessVisitGroup/groupUpdateWork.xml">團體補登</a>
																</li>
																<li><a
																	href="RunAction.jsp?_action=postpone/form_query.xml">延期</a>
																</li>
																<li><a
																	href="RunAction.jsp?_action=resign/form_query.xml">加簽</a></li>
																<li><a href="#">入出境、居停留申請</a></li>
																<li><a href="#">加簽延期申請</a></li>
																<li><a href="#">一般人民申請</a></li>
																<li><a href="#">其他申請</a></li>
																<li><a href="#">補件作業</a></li>
																<li><a href="#">資料補登作業</a></li>
															</ul></li>
														<li><a href="#">資料維護</a>
															<ul>
																<li><a href="RunAction.jsp?_action=code/list.xml">代碼維護</a>
																</li>
															</ul></li>
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
											</td>
											<td style="width: 100%;" class="center"><script
													xmlns:user="urn:user-namespace-here"
													xmlns:msxsl="urn:schemas-microsoft-com:xslt"
													language="javascript" type="text/javascript"
													src="js/jquery/jquery.form.js"></script> <script
													language="javascript" type="text/javascript"
													src="js/jquery/jquery.MetaData.js"></script> <script
													language="javascript" type="text/javascript"
													src="js/jquery/jquery.MultiFile.js"></script> <script
													language="javascript" type="text/javascript"
													src="js/jquery/jquery.blockUI.js"></script> <script
													language="javascript" type="text/javascript"
													src="js/dhtmlxToolbar/dhtmlxtoolbar.js"></script>
												<link
													href="js/dhtmlxToolbar/skins/dhtmlxtoolbar_dhx_blue.css"
													type="text/css" rel="stylesheet">
													<script src="js/jquery/panel/ui.panel.js"
														type="text/javascript"></script>
													<link rel="stylesheet" href="js/jquery/panel/ui.panel.css"
														type="text/css">
														</div></td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="zone.footer">
													<div class="footer">
														<p>
															內政部入出國及移民署 版權所有 &copy; NATIONAL IMMIGRATION AGENCY<br>地址：100-66台北市廣州街15號
																電話：(02)2388-9393 
														</p>
													</div>
												</div>
											</td>
										</tr>
									</table>
								</div>
		</form>
	</div>
</body>
</html>
