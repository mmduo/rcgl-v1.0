<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript" src="plug-in/common/userList.js"></script>

<table width="100%" id="userList" toolbar="#userListtb"></table>
<div id="userListtb" style="padding:3px; ">
	<div name="searchColums">
		<span style="display:-moz-inline-box;display:inline-block;">
			<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">单位：</span>
			<select name="browser" style="width: 135px">
				<option value ="" >---请选择---</option>
				<c:forEach items="${departList}" var="zzcdepart">
					<option value="${zzcdepart.typename }">${zzcdepart.typename}</option>
				</c:forEach>
			</select>
		</span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">姓名：</span>
		<input type="text" name="realName" style="width: 100px" /></span>
	</div>
	<div style="height:30px;" class="datagrid-toolbar">
		<span style="float:left;">
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-add"
			onclick="add('录入','userController.do?addorupdate','userList')">录入</a>
		<a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="update('修改','userController.do?addorupdate','userList')">修改</a>
		<!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
			onclick="userList.expFiles()">当日导出</a> -->
		<!-- <a href="#" class="easyui-linkbutton" plain="true" icon="icon-add"
			onclick="add('批量录入','userController.do?goImplXls','userList')">批量录入</a> -->
		</span>
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="userList.listSearch()">查询</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload"	onclick="searchReset('userList')">重置</a>
		</span>
	</div>
</div>