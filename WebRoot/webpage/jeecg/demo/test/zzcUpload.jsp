<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>Excel导入</title>
  <t:base type="jquery,easyui,tools"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" layout="div" dialog="true" beforeSubmit="upload">
   <fieldset class="step">
    <div class="form">
				<link rel="stylesheet" href="plug-in/uploadify/css/uploadify.css"
					type="text/css"></link>
				<script type="text/javascript"
					src="plug-in/uploadify/jquery.uploadify-3.1.js"></script>
				<script type="text/javascript" src="plug-in/tools/Map.js"></script>
				<script type="text/javascript">
					var flag = false;
					var fileitem = "";
					var fileKey = "";
					var m = new Map();
					$(function() {
						$('#file_upload').uploadify({
							buttonText : '选择要导入的文件',
							auto : false,
							progressData : 'speed',
							multi : true,
							height : 25,
							overrideEvents : [ 'onDialogClose' ],
							fileTypeDesc : '文件格式:',
							queueID : 'filediv',
							fileTypeExts : '*.xls',
							fileSizeLimit : '3MB',
							swf : 'plug-in/uploadify/uploadify.swf',
							//uploader : 'userController.do?zzcimportExcel',
							uploader : 'jpPersonController.do?importExcel',
							onUploadStart : function(file) {
								/* alert('1');
								var documentTitle = $('#documentTitle').val();
								alert(documentTitle);
								$('#file_upload').uploadify("settings", "formData", {
									'documentTitle' : documentTitle
								}); */
							},
							onQueueComplete : function(queueData) {
								var win = frameElement.api.opener;
								win.reloadTable();
								win.tip('上传完成');
								frameElement.api.close();
							},
							onUploadSuccess : function(file, data, response) {
								var d = $.parseJSON(data);
								if (d.success) {
									var win = frameElement.api.openerwin.tip(d.msg);
								}
							},
							onFallback : function() {
								tip("您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试")
							},
							onSelectError : function(file, errorCode, errorMsg) {
								switch (errorCode) {
								case -100:
									tip("上传的文件数量已经超出系统限制的" + $('#file_upload').uploadify('settings', 'queueSizeLimit') + "个文件！");
									break;
								case -110:
									tip("文件 [" + file.name + "] 大小超出系统限制的" + $('#file_upload').uploadify('settings', 'fileSizeLimit') + "大小！");
									break;
								case -120:
									tip("文件 [" + file.name + "] 大小异常！");
									break;
								case -130:
									tip("文件 [" + file.name + "] 类型不正确！");
									break;
								}
							},
							onUploadProgress : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {}
						});
					});
					function upload() {
						$('#file_upload').uploadify('upload', '*');		return flag;
					}
					function cancel() {
						$('#file_upload').uploadify('cancel', '*');
					}
				</script>
				<span id="file_uploadspan"><input type="file" name="fiels"
					id="file_upload" /></span>
				<%-- <t:upload name="fiels" buttonText="选择要导入的文件" uploader="userController.do?zzcimportExcel" extend="*.xls" id="file_upload" formData="documentTitle"></t:upload> --%>
    </div>
    <div class="form" id="filediv" style="height:50px">
    </div>
   </fieldset>
  </t:formvalid>
 </body>
</html>
