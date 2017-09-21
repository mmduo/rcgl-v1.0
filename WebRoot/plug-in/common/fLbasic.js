$(function() {
	$('#fLbasicList01').datagrid({
		//idField : 'serverIp',
		title : '文件列表',
		url : "functionController.do?fileloadGrid&field=id,attachmenttitle,createdate",
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
		columns : [ [
			{field : 'id', title : '编号', hidden:true, sortable : true },			
			{field : 'attachmenttitle',title : '文件名称',width : 80,sortable:true},
			{field : 'createdate', title : '上传时间', width : 40, sortable : true },
			{field : 'null',title : '操作',width : 50,formatter : function(value, rec, index) {
				if (!rec.id) {
					return '';
				}
				var href = '';
				href += "[<a href='#' onclick=fLbasic.fLDetail('"+rec.id+"')>明细</a>]";
				href += "[<a href='#' onclick=fLbasic.fLExp('"+rec.id+"')>导出</a>]";
				return href;
			}}
		] ],
		onClickRow : function(rowIndex, rowData) {
			rowid = rowData.id;
			gridname = 'fLbasicList01';
		}
	});
	$('#fLbasicList01').datagrid('getPager').pagination({
		beforePageText : '',
		afterPageText : '/{pages}',
		displayMsg : '{from}-{to}共{total}条',
		showPageList : true,
		pageList : [ 10, 20, 30 ],
		showRefresh : true
	});
	$('#fLbasicList01').datagrid('getPager').pagination({
		onBeforeRefresh : function(pageNumber, pageSize) {
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
});
var fLbasic = {
	fLDetail:function (id) {
		$('#fLbasicpanel').panel("refresh", "functionController.do?fLDetail&docid=" + id);
	},
	fLExp:function(id){
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : 'systemController.do?expFiles&docid='+id,// 请求的action路径
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					$.messager.show({
						title : '提示消息',
						msg : '导出成功',
						timeout : 2000,
						showType : 'slide'
					});
					reloadTable();
				}
			}
		});
		//update('导出','systemController.do?expFiles&docid='+id,'0');
	},
	getBack:function () {
		var currtab_title = $('#mainTabs').tabs('getSelected').panel('options').title;
		$('#mainTabs').tabs('close', currtab_title);
		// 释放内存
		$.fn.panel.defaults = $.extend({}, $.fn.panel.defaults, {
			onBeforeDestroy : function() {
				var frame = $('iframe', this);
				if (frame.length > 0) {
					frame[0].contentWindow.document.write('');
					frame[0].contentWindow.close();
					frame.remove();
				}
				if ($.browser.msie) {
					CollectGarbage();
				}
			}
		});
		
		$('#mainTabs').tabs({ onSelect : function(title) {rowid = "";}});
	},
	searchReset:function (name) {
		$("#" + name + "Form01").find(":input").val("");
		moveCompany.listSearch();
	},
	listSearch:function () {
		var queryParams = $('#fLbasicList01').datagrid('options').queryParams;
		$('#fLbasicList01').find('*').each(function() {
			queryParams[$(this).attr('name')] = $(this).val();
		});
		$('#fLbasicList01').datagrid({
			url : "functionController.do?fileloadGrid&field=id,attachmenttitle,createdate"
		});
	},
};