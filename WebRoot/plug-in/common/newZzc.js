$(function() {
	$('#newZzcList01').datagrid({
		idField : 'companyCode',
		title : '民警不在岗情况摸排列表',
		url : "jeecgJdbcService.do?zzcdatagrid&field=id,depart,name,zw,bzgzl,spld,spdate,ksdate," +
				"ljdate,fjdate,jsdate,cxtype,qwaddress,note",
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
			{field : 'ck',checkbox : true },
			{field : 'id', title : 'id', width : 60, sortable : true, hidden:true },
			{field : 'depart', title : '单位', width : 30, sortable : true },	
			{field : 'zw',title : '职务',width : 30,sortable:true,hidden:true},
			{field : 'bzgzl', title : '不在岗种类', width : 30, sortable : true },
			{field : 'spld', title : '审批领导', width : 30, sortable : true },			
			{field : 'spdate',title : '审批日期',width : 30,sortable:true},
			{field : 'ksdate',title : '开始日期',width : 30,sortable:true,hidden:true},
			{field : 'ljdate', title : '离京日期', width : 30, sortable : true },
			{field : 'fjdate', title : '返京日期', width : 30, sortable : true },			
			{field : 'jsdate',title : '结束日期',width : 30,sortable:true},
			{field : 'cxtype',title : '出行方式',width : 40,sortable:true,hidden:true},
			{field : 'qwaddress', title : '前往地点', width : 40, sortable : true },
			{field : 'note', title : '备注', width : 60, sortable : true }
		] ],
		onClickRow : function(rowIndex, rowData) {
			rowid = rowData.id;
			gridname = 'newZzcList01';
		}
	});
	$('#newZzcList01').datagrid('getPager').pagination({
		beforePageText : '',
		afterPageText : '/{pages}',
		displayMsg : '{from}-{to}共{total}条',
		showPageList : true,
		pageList : [ 10, 20, 30 ],
		showRefresh : true
	});
	$('#newZzcList01').datagrid('getPager').pagination({
		onBeforeRefresh : function(pageNumber, pageSize) {
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
});
var newZzc = {
	listSearch:function () {
		var queryParams = $('#newZzcList01').datagrid('options').queryParams;
		$('#newZzcForm01').find('*').each(function() {
			queryParams[$(this).attr('name')] = $(this).val();
		});
		$('#newZzcList01').datagrid({
			url : "jeecgJdbcService.do?zzcdatagrid&field=id,depart,name,zw,bzgzl,spld,spdate,ksdate," +
				"ljdate,fjdate,jsdate,cxtype,qwaddress,note"
		});
	},
	save:function () {
		//MsgAlertInfo('保存成功');
		$.messager.confirm('提示','是否执行此操作', function(r){
			if (r){
				newZzc.setValue();
				/*document.getElementById("newCompanyrcompanyName").value = 
					document.getElementById("newCompanycompanyName").value;*/
				$.ajax({
					async:false,
					type:"POST",
					url:"jeecgJdbcService.do?saveNew",//要访问的后台地址
					dataType : 'json',
					data:$('#newZzcForm01').serialize(),
					error:function(d){
						$.messager.alert('提示',d.msg,'info');
					},
					success: function(d){
						$.messager.alert('提示',d.msg,'info');
					}
				});
			}
		});
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
		newCompany.listSearch();
	},
	//隐藏值
	setValue:function(){
		var selected = $('#newZzcList01').datagrid('getSelected');
		document.getElementById("newCompanyserverIp").value = selected.serverIp;
		document.getElementById("newCompanydbInstance").value = selected.dbInstance;
		document.getElementById("newCompanyfilesPath").value = selected.filesPath;
	}
};