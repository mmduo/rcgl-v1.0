<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/zzcmjList.js"></script>

<table width="100%" id="zzcmjList" toolbar="#zzcmjListtb"></table>
<div id="zzcmjListtb" style="padding:3px; ">
	<div name="searchColums">
		<span style="display:-moz-inline-box;display:inline-block;">
			<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">单位：</span>
			<select name="zzcdepart" style="width: 135px">
				<c:forEach items="${departList}" var="zzcdepart">
					<option value="${departList}">${departList}</option>
				</c:forEach>
			</select>
		</span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">姓名：</span>
		<input type="text" name="name" style="width: 100px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">职务：</span>
			<select name="zw" style="width: 135px">
				<option value ="" >---请选择---</option>
				<c:forEach items="${zwList}" var="zw">
					<option value="${zw.typename }">${zw.typename}</option>
				</c:forEach>
			</select>
		</span>
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
			onclick="add('录入','userController.do?zzcmjaddorupdate','zzcmjList')">录入</a>
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="update('维护','userController.do?zzcmjaddorupdate','zzcmjList')">维护</a>
		</span>
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcmjList.listSearch()">查询</a>
		<!-- <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="zzcmjListsearch()">查询</a> -->
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload"	onclick="searchReset('zzcmjList')">重置</a>
		</span>
	</div>
</div>