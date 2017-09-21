var docid = document.getElementById("fLDetaildocid").value;
$(function() {
	$('#fLDetailList01').datagrid({
		idField : 'docindex',
		title : '详细',
		url : 'functionController.do?fDocDetail&field=id,docid,doctitle,docindex,doctxt&docid='+docid,
		fit : true,
		loadMsg : '数据加载中...',
		pageSize : 10,
		pagination : true,
		sortOrder : 'asc',
		rownumbers : true,
		singleSelect : true,
		fitColumns : true,
		nowrap:false,
		showFooter : true,
		frozenColumns : [ [] ],
		columns : [ [
			{field : 'docindex',title : '序号',hidden:true,sortable:true},
			{field : 'doctitle',title : '文件名',hidden:true,sortable:true},
			{field : 'doctxt', title : '详细', width :510, sortable : true }
		] ],
		onClickRow : function(rowIndex, rowData) {
			rowid = rowData.id;
			gridname = 'fLDetailList01';
		}
	});
	$('#fLDetailList01').datagrid('getPager').pagination({
		beforePageText : '',
		afterPageText : '/{pages}',
		displayMsg : '{from}-{to}共{total}条',
		showPageList : true,
		pageList : [ 10, 20, 30 ],
		showRefresh : true
	});
	$('#fLDetailList01').datagrid('getPager').pagination({
		onBeforeRefresh : function(pageNumber, pageSize) {
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
});
var fLDetail = {
	modify:function(columnName,dataType){
		update('修改','jdbcController.do?colModify&columnName='+columnName+
				'&tableName='+tableName+'&dataType='+dataType,'colDetailList01');
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
	addCol:function () {
		update('新建','jdbcController.do?colAdd&tableName='+tableName,'colDetailList01');
	}
};