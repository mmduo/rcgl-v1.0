<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/zzcnljList.js"></script>

<table width="100%" id="zzcnljList" toolbar="#zzcnljListtb"></table>
<div id="zzcnljListtb" style="padding:3px; ">
	<div name="searchColums">
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">日期：</span>
			<input name="zzcnljqdate" class="easyui-datebox"></span>
	</div>
	<div style="height:30px;" class="datagrid-toolbar">
		<span style="float:left;">
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="zzcnljList.expFiles()">导出</a>
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="zzcnljList.expExcelFiles()">导出excel</a>
		</span>
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcnljList.listSearch()">查询</a>
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcnljListsearch()">查询</a> -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload"	onclick="searchReset('zzcnljList')">重置</a>
		</span>
	</div>
</div>