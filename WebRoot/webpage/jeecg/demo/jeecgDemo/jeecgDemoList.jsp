<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="jeecgDemoList" title="日常工作记录汇总" autoLoadData="true" actionUrl="jeecgDemoController.do?datagrid" sortName="attachmenttitle"  fitColumns="true" idField="id" fit="true" queryMode="group">
   <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
   <t:dgCol title="名称" field="attachmenttitle" query="true" width="60"></t:dgCol>
   <t:dgCol title="创建日期" field="note" query="true" queryMode="group" width="30"></t:dgCol>
   <t:dgCol title="总队领导" sortable="false" field="docBigleader" width="40"></t:dgCol>
   <t:dgCol title="代班领导" field="docClassleader" query="true" width="40"></t:dgCol>
   <t:dgCol title="值班警力" field="docClassnum" width="20"></t:dgCol>
   <t:dgCol title="系统维护" field="docXtwh" width="20"></t:dgCol>
   <t:dgCol title="信通报障" field="docXtbz" width="20"></t:dgCol>
   <t:dgCol title="故障处理" field="docGzcl" width="20"></t:dgCol>
   
   <t:dgToolBar operationCode="edit" title="编辑" icon="icon-edit" url="jeecgDemoController.do?addorupdate" funname="update"></t:dgToolBar>
   <%--   update-start--Author:sun  Date:20130503 for 增加页面详细查看功能--%>
   <%-- <t:dgToolBar operationCode="detail" title="查看" icon="icon-search" url="jeecgDemoController.do?addorupdate" funname="detail"></t:dgToolBar> --%>
   <%--   update-end--Author:sun  Date:20130503 for 增加页面详细查看功能--%>
  </t:datagrid>
  </div>
  </div>
 <script type="text/javascript">
	$(document).ready(function(){
		$("input[name='note_begin']").attr("class","easyui-datebox");
		$("input[name='note_end']").attr("class","easyui-datebox");
	});
</script>