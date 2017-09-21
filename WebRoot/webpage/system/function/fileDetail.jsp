<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/fLDetail.js"></script>
<input type="hidden" id="fLDetaildocid" value='${docid}'>
<table width="100%" id="fLDetailList01" toolbar="#fLDetailListtb"></table>
<div id="fLDetailListtb" style="padding: 3px;">
	<div style="height: 30px;" class="datagrid-toolbar">
		<!-- <span style="float: left;">
			<a href="#"	class="easyui-linkbutton" plain="true" icon="icon-add"
			onclick="colDetail.addCol()">新建列字段</a>
		</span> -->
	</div>
</div>