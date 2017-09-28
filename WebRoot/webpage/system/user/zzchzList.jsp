<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/zzchzList.js"></script>

<table width="100%" id="zzchzList" toolbar="#zzchzListtb"></table>
<div id="zzchzListtb" style="padding:3px; ">
	<div name="searchColums">
		<!-- <span style="display:-moz-inline-box;display:inline-block;">
			<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">单位：</span>
			<select name="zzcdepart" style="width: 104px">
				<option value ="" >---请选择---</option>
				<option value ="办公室">办公室</option>
				<option value ="政治处">政治处</option>
				<option value ="总队纪委">总队纪委</option>
				<option value ="审计室">审计室</option>
				<option value ="警务保障处">警务保障处</option>
				<option value ="法制处">法制处</option>
				<option value ="情报信息中心" >情报信息中心</option>
				<option value ="一大队">一大队</option>
				<option value ="二大队">二大队</option>
				<option value ="三大队">三大队</option>
				<option value ="四大队">四大队</option>
				<option value ="五大队">五大队</option>
				<option value ="六大队">六大队</option>
				<option value ="七大队" >七大队</option>
				<option value ="八大队">八大队</option>
				<option value ="九大队">九大队</option>
				<option value ="十大队">十大队</option>
				<option value ="十一大队">十一大队</option>
				<option value ="十二大队">十二大队</option>
				<option value ="朝阳技术侦察中队">朝阳技术侦察中队</option>
				<option value ="海淀技术侦察中队" >海淀技术侦察中队</option>
				<option value ="通州技术侦察中队">通州技术侦察中队</option>
				<option value ="丰台技术侦察中队">丰台技术侦察中队</option>
				<option value ="顺义技术侦察中队">顺义技术侦察中队</option>
				<option value ="西城技术侦察中队">西城技术侦察中队</option>
				<option value ="怀柔技术侦察中队">怀柔技术侦察中队</option>
			</select>
		</span> -->
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">日期：</span>
			<input name="zzchzqdate" class="easyui-datebox"></span>
	</div>
	<div style="height:30px;" class="datagrid-toolbar">
		<span style="float:left;">
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="zzchzList.expFiles()">导出</a>
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="zzchzList.expExcelFiles()">导出excel</a>
		</span>
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzchzList.listSearch()">查询</a>
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzchzListsearch()">查询</a> -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload"	onclick="searchReset('zzchzList')">重置</a>
		</span>
	</div>
</div>