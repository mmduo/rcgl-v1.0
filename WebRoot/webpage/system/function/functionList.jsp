<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<div class="easyui-layout" fit="true">
<div region="center" style="padding:1px;">
		<script type="text/javascript">
		$(function() {
				$('#functionList').treegrid({
					idField : 'id',
					treeField : 'text',
					title : '菜单管理',
					url : 'functionController.do?functionGrid&field=id,functionName,TSIcon_iconPath,functionUrl,functionOrder,',
					fit : true,
					loadMsg : '数据加载中...',
					pageSize : 10,
					pagination : false,
					sortOrder : 'asc',
					rownumbers : true,
					singleSelect : true,
					fitColumns : true,
					showFooter : true,
					frozenColumns : [ [] ],
					columns : [ [ {
						field : 'id',
						title : '编号',
						width : 30,
						hidden : true
					}, {
						field : 'text',
						title : '菜单名称',
						width : 100
					}, {
						field : 'code',
						title : '图标',
						width : 30,
						formatter : function(value, rec, index) {
							return '<img border="0" src="' + value + '"/>'
						}
					}, {
						field : 'src',
						title : '菜单地址',
						width : 60
					}, {
						field : 'order',
						title : '菜单顺序',
						width : 60
					}, {
						field : 'null',
						title : '操作',
						width : 100,
						formatter : function(value, rec, index) {
							if (!rec.id) {
								return '';
							}
							var href = '';
							href += "[<a href='#' onclick=delObj('functionController.do?del&id=" + rec.id + "','functionList')>";
							href += "删除</a>]";
							href += "[<a href='#' onclick=operationDetail('" + rec.id + "','" + index + "')>";
							href += "按钮设置</a>]";return href;
						}
					} ] ],
					onClickRow : function(rowData) {
						rowid = rowData.id;
						gridname = 'functionList';
					}
				});$('#functionList').treegrid('getPager').pagination({
					beforePageText : '',
					afterPageText : '/{pages}',
					displayMsg : '{from}-{to}共{total}条',
					showPageList : true,
					pageList : [ 10, 20, 30 ],
					showRefresh : true
				});$('#functionList').treegrid('getPager').pagination({
					onBeforeRefresh : function(pageNumber, pageSize) {
						$(this).pagination('loading');$(this).pagination('loaded');
					}
				});
			});
			function reloadTable() {
				$('#' + gridname).treegrid('reload');
			}
			function reloadfunctionList() {
				$('#functionList').treegrid('reload');
			}
			function getfunctionListSelected(field) {
				return getSelected(field);
			}
			function getSelected(field) {
				var row = $('#' + gridname).treegrid('getSelected');
				if (row != null) {
					value = row[field];
				}
				else {
					value = '';
				}
				return value;
			}
			function getfunctionListSelections(field) {
				var ids = [];
				var rows = $('#functionList').treegrid('getSelections');
				for (var i = 0; i < rows.length; i++) {
					ids.push(rows[i][field]);
				}
				ids.join(',');return ids
			}
			;
			function functionListsearch() {
				var queryParams = $('#functionList').datagrid('options').queryParams;
				$('#functionListtb').find('*').each(function() {
					queryParams[$(this).attr('name')] = $(this).val();
				});$('#functionList').treegrid({
					url : 'functionController.do?functionGrid&field=id,functionName,TSIcon_iconPath,functionUrl,functionOrder,'
				});
			}
			function dosearch(params) {
				var jsonparams = $.parseJSON(params);
				$('#functionList').treegrid({
					url : 'functionController.do?functionGrid&field=id,functionName,TSIcon_iconPath,functionUrl,functionOrder,',
					queryParams : jsonparams
				});
			}
			function functionListsearchbox(value, name) {
				var queryParams = $('#functionList').datagrid('options').queryParams;
				queryParams[name] = value;
				queryParams.searchfield = name;$('#functionList').treegrid('reload');
			}
			$('#functionListsearchbox').searchbox({
				searcher : function(value, name) {
					functionListsearchbox(value, name);
				},
				menu : '#functionListmm',
				prompt : '请输入查询关键字'
			});
			function searchReset(name) {
				$("#" + name + "tb").find(":input").val("");functionListsearch();
			}
		</script>
		<table width="100%" id="functionList" toolbar="#functionListtb"></table>
		<div id="functionListtb" style="padding:3px; ">
			<div style="height:30px;" class="datagrid-toolbar">
				<span style="float:left;"><a href="#"
					class="easyui-linkbutton" plain="true" icon="icon-add"
					onclick="add('菜单录入','functionController.do?addorupdate','functionList')">菜单录入</a><a
					href="#" class="easyui-linkbutton" plain="true" icon="icon-edit"
					onclick="update('菜单编辑','functionController.do?addorupdate','functionList')">菜单编辑</a></span>
			</div>
		</div>
	</div>
<div region="east" style="width:500px; overflow: hidden;" split="true" border="false">
<div class="easyui-panel" title="操作按钮" style="padding:1px;" fit="true" border="false" id="operationDetailpanel">
  </div>
</div>
</div>
<%--   update-start--Author:anchao  Date:20130415 for：按钮权限控制--%>
<script type="text/javascript">
function operationDetail(functionId)
{
	$('#operationDetailpanel').panel("refresh", "functionController.do?operation&functionId=" +functionId);
}
</script>
<%--   update-end--Author:anchao  Date:20130415 for：按钮权限控制--%>
