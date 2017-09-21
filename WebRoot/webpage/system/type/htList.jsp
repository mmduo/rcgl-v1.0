<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
  <t:base type="jquery,easyui,tools"></t:base>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:1px;">
<t:datagrid name="${typegroup.typegroupcode}List" title="类型列表" actionUrl="systemController.do?htGrid&htgroupid=${typegroup.id}" idField="id" queryMode="group">
 <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
 <t:dgCol title="合同名称" field="typename"></t:dgCol>
 <t:dgCol title="详情" field="value"></t:dgCol>
 <%-- <t:dgCol title="操作" field="opt"></t:dgCol>
 <t:dgDelOpt url="systemController.do?delType&id={id}" title="删除"></t:dgDelOpt> --%>
 <t:dgToolBar title="编辑" icon="icon-edit" url="systemController.do?addorupdateHt&typegroupid=${typegroup.id}" funname="update"></t:dgToolBar>
</t:datagrid>
</div>
</div>
