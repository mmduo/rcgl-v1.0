<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:1px;">
	<script type="text/javascript" src="plug-in/common/fLbasic.js"></script>
		<table width="100%" id="fLbasicList01" toolbar="#flListtb"></table>
		<div id="flListtb" style="padding:3px; ">
			<div style="height:30px;" class="datagrid-toolbar">
				<span style="float:left;"><a href="#"
					class="easyui-linkbutton" plain="true" icon="icon-add"
					onclick="add('文件上传','systemController.do?addFiles','functionList')">文件上传</a></span>
			</div>
		</div>
</div>
<div region="east" style="width:550px; overflow: hidden;" split="true" border="false">
	<div id="fLbasicpanel" class="easyui-panel" title="明细" style="padding:1px;" fit="true" border="false" ></div>
</div>
</div>