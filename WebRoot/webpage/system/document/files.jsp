<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>文件列表</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
	<div id="content">
		<div id="wrapper">
			<div id="steps">
				<form id="formobj" action="null" name="formobj" method="post">
					<input type="hidden" id="btn_sub" class="btn_sub" />
					<fieldset class="step">
						<!-- <div class="form">
							<label class="Validform_label"> 文件标题: </label>
							<input name="documentTitle" id="documentTitle" datatype="s3-50" />
							<span class="Validform_checktip">标题名称在3~50位字符,且不为空</span>
						</div> -->
						<div class="form">
							<link rel="stylesheet" href="plug-in/uploadify/css/uploadify.css" type="text/css"></link>
							<script type="text/javascript" src="plug-in/uploadify/jquery.uploadify-3.1.js"></script>
							<script type="text/javascript" src="plug-in/tools/Map.js"></script>
							<script type="text/javascript">
								var flag = false;
								var fileitem = "";
								var fileKey = "";
								var m = new Map();
								$(function() {
									$('#file_upload').uploadify({
										buttonText : '上传文件',
										auto : false,
										progressData : 'speed',
										multi : true,
										height : 25,
										overrideEvents : [ 'onDialogClose' ],
										fileTypeDesc : '文件格式:',
										queueID : 'filediv',
										fileTypeExts : '*.doc;*.docx;*.txt;*.ppt;*.xls;*.xlsx;*.html;*.htm',
										fileSizeLimit : '3MB',
										simUploadLimit : 999,
										removeCompleted : true, //上传完成后自动删除队列
										swf : 'plug-in/uploadify/uploadify.swf',
										uploader : 'systemController.do?saveFiles',
										onUploadStart : function(file) {
											$('#file_upload').uploadify("settings", "formData", {
											});
										},
										onQueueComplete : function(queueData) {
											var win = frameElement.api.opener;
											parent.fLbasic.listSearch();
											win.tip('上传完成');
											frameElement.api.close();
										},
										onUploadSuccess : function(file, data, response) {
											var d = $.parseJSON(data);
											if (d.msg == "文件添加成功") {
											}
											else {
												var win = frameElement.api.opener;
												win.tip('请勿重复上传');
											}
										},
										onFallback : function() {
											tip("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试")
										},
										onSelectError : function(file, errorCode, errorMsg) {
											switch (errorCode) {
											case -100:
												tip("上传的文件数量已经超出系统限制的" + $('#file_upload').uploadify('settings', 'queueSizeLimit') + "个文件！");
												break;case -110:
												tip("文件 [" + file.name + "] 大小超出系统限制的" + $('#file_upload').uploadify('settings', 'fileSizeLimit') + "大小！");
												break;case -120:
												tip("文件 [" + file.name + "] 大小异常！");
												break;case -130:
												tip("文件 [" + file.name + "] 类型不正确！");
												break;
											}
										},
										onUploadProgress : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {}
									});
								});
								function upload() {
									$('#file_upload').uploadify('upload', '*');
									return flag;
								}
								function cancel() {
									$('#file_upload').uploadify('cancel', '*');
								}
							</script>
							<span id="file_uploadspan"><input type="file" name="fiels" id="file_upload" /></span>
						</div>
						<div class="form" id="filediv" style="height:50px"></div>
					</fieldset>
					<link rel="stylesheet" href="plug-in/Validform/css/divfrom.css" type="text/css" />
					<link rel="stylesheet" href="plug-in/Validform/css/style.css" type="text/css" />
					<link rel="stylesheet" href="plug-in/Validform/css/tablefrom.css" type="text/css" />
					<script type="text/javascript" src="plug-in/Validform/js/Validform_v5.3.1_min.js"></script>
					<script type="text/javascript" src="plug-in/Validform/js/Validform_Datatype.js"></script>
					<script type="text/javascript" src="plug-in/Validform/js/datatype.js"></script>
					<script type="text/javascript">
						$(function() {
							$("#formobj").Validform({
								tiptype : 4,
								btnSubmit : "#btn_sub",
								btnReset : "#btn_reset",
								ajaxPost : true,
								beforeSubmit : function(curform) {
									var tag = false;
									return upload(curform);
								},
								callback : function(data) {
									var win = frameElement.api.opener;
									if (data.success == true) {
										frameElement.api.close();win.tip(data.msg);
									}
									else {
										if (data.responseText == '' || data.responseText == undefined){
											$("#formobj").html(data.msg);
										}else $("#formobj").html(data.responseText);
										return false;
									}
									win.reloadTable();
								}
							});
						});
					</script>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
