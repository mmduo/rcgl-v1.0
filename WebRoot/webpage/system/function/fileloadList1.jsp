<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools"></t:base>
	<div class="easyui-layout" fit="true">
		<div region="center" style="padding:1px;">
		<script type="text/javascript" src="plug-in/common/fLbasic.js"></script>
			<table width="100%" id="fLbasicList01"></table>
		</div>
		<div region="east" style="width:500px; overflow: hidden;" split="true" border="false">
			<div id="fLbasicpanel" class="easyui-panel" title="明细" style="padding:1px;" fit="true" border="false" ></div>
		</div>
	</div>