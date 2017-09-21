<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding:1px;">
		<script type="text/javascript">$(function() {
				$('#jltypeList').datagrid({
					idField : 'id',
					title : '警力维护',
					url : 'systemController.do?jltypeGrid&typegroupid=1&field=id,typecode,typename,value',
					fit : true,
					loadMsg : '数据加载中...',
					pageSize : 10,
					pagination : true,
					sortOrder : 'asc',
					rownumbers : true,
					singleSelect : true,
					fitColumns : true,
					showFooter : true,
					frozenColumns : [ [] ],
					columns : [ [ {
						field : 'typecode',
						title : '编号',
						width : 30,
						hidden : true,
						sortable : true
					}, {
						field : 'typecode',
						title : '单位编码',
						width : 60,
						hidden : true,
						sortable : true
					}, {
						field : 'typename',
						title : '单位名称',
						width : 60,
						sortable : true
					}, {
						field : 'value',
						title : '警力',
						width : 60,
						sortable : true
					} ] ],
					onClickRow : function(rowIndex, rowData) {
						rowid = rowData.id;
						gridname = 'jltypeList';
					}
				});$('#jltypeList').datagrid('getPager').pagination({
					beforePageText : '',
					afterPageText : '/{pages}',
					displayMsg : '{from}-{to}共{total}条',
					showPageList : true,
					pageList : [ 10, 20, 30 ],
					showRefresh : true
				});$('#jltypeList').datagrid('getPager').pagination({
					onBeforeRefresh : function(pageNumber, pageSize) {
						$(this).pagination('loading');$(this).pagination('loaded');
					}
				});
			});
			function reloadTable() {
				$('#' + gridname).datagrid('reload');
			}
			function reloadjltypeList() {
				$('#jltypeList').datagrid('reload');
			}
			function getjltypeListSelected(field) {
				return getSelected(field);
			}
			function getSelected(field) {
				var row = $('#' + gridname).datagrid('getSelected');
				if (row != null) {
					value = row[field];
				}
				else {
					value = '';
				}
				return value;
			}
			function getjltypeListSelections(field) {
				var ids = [];
				var rows = $('#jltypeList').datagrid('getSelections');
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i][field]);
				}
				ids.join(',');return ids
			}
			;
			function jltypeListsearch() {
				var queryParams = $('#jltypeList').datagrid('options').queryParams;
				$('#jltypeListtb').find('*').each(function() {
					queryParams[$(this).attr('name')] = $(this).val();
				});$('#jltypeList').datagrid({
					url : 'systemController.do?jltypeGrid&typegroupid=1&field=id,typename,value,'
				});
			}
			function dosearch(params) {
				var jsonparams = $.parseJSON(params);
				$('#jltypeList').datagrid({
					url : 'systemController.do?jltypeGrid&typegroupid=1&field=id,typename,value,',
					queryParams : jsonparams
				});
			}
			function jltypeListsearchbox(value, name) {
				var queryParams = $('#jltypeList').datagrid('options').queryParams;
				queryParams[name] = value;
				queryParams.searchfield = name;$('#jltypeList').datagrid('reload');
			}
			$('#jltypeListsearchbox').searchbox({
				searcher : function(value, name) {
					jltypeListsearchbox(value, name);
				},
				menu : '#jltypeListmm',
				prompt : '请输入查询关键字'
			});
			function searchReset(name) {
				$("#" + name + "tb").find(":input").val("");jltypeListsearch();
			}
		</script>
		<table width="100%" id="jltypeList" toolbar="#jltypeListtb"></table>
		<div id="jltypeListtb" style="padding:3px; ">
			<div style="height:30px;" class="datagrid-toolbar">
				<span style="float:left;"><a href="#"
					class="easyui-linkbutton" plain="true" icon="icon-add"
					onclick="update('警力维护','userController.do?zzcsyjlupdate&typegroupid=1','jltypeList')">实有警力录入</a>
				</span> 
			</div>
		</div>
		<%-- <t:datagrid name="${typegroup.typegroupcode}List" title="实有警力维护" actionUrl="systemController.do?jltypeGrid&typegroupid=${typegroup.id}" idField="id" queryMode="group">
 <t:dgCol title="编号" field="id" hidden="false"></t:dgCol>
 <t:dgCol title="单位名称" field="typename"></t:dgCol>
 <t:dgCol title="实有警力" field="value"></t:dgCol>
 <t:dgCol title="操作" field="opt"></t:dgCol>
 <t:dgDelOpt url="systemController.do?delType&id={id}" title="删除"></t:dgDelOpt>
 <t:dgToolBar title="${typegroup.typegroupname}录入" icon="icon-add" url="systemController.do?addorupdateType&typegroupid=${typegroup.id}" funname="add"></t:dgToolBar>
 <t:dgToolBar title="类别编辑" icon="icon-edit" url="systemController.do?addorupdateType&typegroupid=${typegroup.id}" funname="update"></t:dgToolBar>
</t:datagrid> --%>
	</div>
</div>

