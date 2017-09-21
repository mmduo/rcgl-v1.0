<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:datagrid name="zzcList" title="不在岗民警管理" actionUrl="userController.do?zzcdatagrid" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="编号" field="id" hidden="false" ></t:dgCol>
	<t:dgCol title="单位" sortable="false" field="depart" query="true" width="20"></t:dgCol>
	<t:dgCol title="姓名" sortable="false" field="name" query="true" width="20"></t:dgCol>
	<%-- <t:dgCol title="部门" field="TSDepart_departname" query="true" queryMode="single" replace="${departsReplace}"></t:dgCol> --%>
	<t:dgCol title="职务" field="zw" query="true" ></t:dgCol>
	<t:dgCol title="不在岗种类" field="bzgzl" query="true" ></t:dgCol>
	<t:dgCol title="离京日期" field="ljdate" query="true" ></t:dgCol>
	<t:dgCol title="返京日期" field="fjdate" query="true" ></t:dgCol>
	<t:dgCol title="审批日期" field="spdate" query="true" ></t:dgCol>
	<t:dgCol title="出行方式" field="cxtype" query="true" ></t:dgCol>
	<t:dgCol title="前往地点" field="qwaddress" query="true" ></t:dgCol>
	<%-- <t:dgCol title="状态" sortable="true" field="status" replace="正常_1,禁用_0,超级管理员_-1"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="userController.do?del&id={id}&userName={userName}" /> --%>
	<t:dgToolBar title="录入" icon="icon-add" url="userController.do?zzcaddorupdate" funname="add"></t:dgToolBar>
   <t:dgToolBar title="维护" icon="icon-edit" url="userController.do?zzcaddorupdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-edit" url="jeecgJdbcController.do?zzcexpFiles" funname="add"></t:dgToolBar>
</t:datagrid>
