<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/zzcList.js"></script>

<table width="100%" id="zzcList" toolbar="#zzcListtb"></table>
<div id="zzcListtb" style="padding:3px; ">
	<div name="searchColums">
		<span style="display:-moz-inline-box;display:inline-block;">
			<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">单位：</span>
			<!-- <select name="depart" WIDTH="100" style="width: 104px"> -->
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
		</span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">姓名：</span>
		<input type="text" name="name" style="width: 100px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">职务：</span>
		<input type="text" name="zw" style="width: 100px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">不在岗种类：</span>
		<input type="text" name="bzgzl" style="width: 100px" /></span>
		
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">离京日期：</span>
			<input name="ljdate" class="easyui-datebox"></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">返京日期：</span>
			<input name="fjdate" class="easyui-datebox"></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">审批日期：</span>
			<input name="spdate" class="easyui-datebox"></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">出行方式：</span>
		<input type="text" name="cxtype" style="width: 100px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">前往地点：</span>
		<input type="text" name="qwaddress" style="width: 100px" /></span>
	</div>
	<div style="height:30px;" class="datagrid-toolbar">
		<span style="float:left;">
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-add"
			onclick="add('录入','userController.do?zzcaddorupdate','zzcList')">录入</a>
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="update('维护','userController.do?zzcaddorupdate','zzcList')">维护</a>
		<!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-add"
			onclick="update('实有警力维护','userController.do?zzcsyjlupdate','zzcList')">实有警力维护</a> -->
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="zzcList.expFiles()">快捷导出</a>
		</span>
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcList.listSearch()">查询</a>
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcListsearch()">查询</a> -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload"	onclick="searchReset('zzcList')">重置</a>
		</span>
	</div>
</div>