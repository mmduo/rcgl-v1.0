	$(function() {
		$('#jeecgDemoList1').datagrid({
			idField : 'id',
			title : '故障处理知识库',
			url : 'jeecgDemoController.do?datagrid1&field=id,docTitle,docTxt,ZSDoc_note,',
			fit : true,
			loadMsg : '数据加载中...',
			pageSize : 10,
			pagination : true,
			sortName : 'docTitle',
			sortOrder : 'asc',
			rownumbers : true,
			nowrap:false,
			singleSelect : true,
			fitColumns : true,
			showFooter : true,
			frozenColumns : [ [] ],
			columns : [ [ {
				field : 'id',
				title : '编号',
				width : 30,
				hidden : true,
				sortable : true
			}, {
				field : 'docTitle',
				title : '文档标题',
				width : 40,
				sortable : true
			}, {
				field : 'docTxt',
				title : '内容',
				width : 150,
				sortable : true
			}, {
				field : 'ZSDoc_note',
				title : '处理日期',
				width : 20,
				sortable : true
			} ] ],
			onClickRow : function(rowIndex, rowData) {
				rowid = rowData.id;
				gridname = 'jeecgDemoList1';
			}
		});$('#jeecgDemoList1').datagrid('getPager').pagination({
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : '{from}-{to}共{total}条',
			showPageList : true,
			pageList : [ 10, 20, 30 ],
			showRefresh : true
		});$('#jeecgDemoList1').datagrid('getPager').pagination({
			onBeforeRefresh : function(pageNumber, pageSize) {
				$(this).pagination('loading');$(this).pagination('loaded');
			}
		});
	});
	function reloadTable() {
		$('#' + gridname).datagrid('reload');
	}
	function reloadjeecgDemoList1() {
		$('#jeecgDemoList1').datagrid('reload');
	}
	function getjeecgDemoList1Selected(field) {
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
	function getjeecgDemoList1Selections(field) {
		var ids = [];
		var rows = $('#jeecgDemoList1').datagrid('getSelections');
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i][field]);
		}
		ids.join(',');return ids
	}
	;
	function jeecgDemoList1search() {
		var queryParams = $('#jeecgDemoList1').datagrid('options').queryParams;
		$('#jeecgDemoList1tb').find('*').each(function() {
			queryParams[$(this).attr('name')] = $(this).val();
		});$('#jeecgDemoList1').datagrid({
			url : 'jeecgDemoController.do?datagrid1&field=id,docTitle,docTxt,note,'
		});
	}
	function dosearch(params) {
		var jsonparams = $.parseJSON(params);
		$('#jeecgDemoList1').datagrid({
			url : 'jeecgDemoController.do?datagrid1&field=id,docTitle,docTxt,note,',
			queryParams : jsonparams
		});
	}
	function jeecgDemoList1searchbox(value, name) {
		var queryParams = $('#jeecgDemoList1').datagrid('options').queryParams;
		queryParams[name] = value;
		queryParams.searchfield = name;$('#jeecgDemoList1').datagrid('reload');
	}
	$('#jeecgDemoList1searchbox').searchbox({
		searcher : function(value, name) {
			jeecgDemoList1searchbox(value, name);
		},
		menu : '#jeecgDemoList1mm',
		prompt : '请输入查询关键字'
	});
	function searchReset(name) {
		$("#" + name + "tb").find(":input").val("");jeecgDemoList1search();
	}