$(function() {
		$('#zzcmjList').datagrid({
			idField : 'id',
			title : '民警不在岗管理',
			url : 'userController.do?zzcmjdatagrid&field=id,zzcdepart,name,zw,bzgzl,ljdate,fjdate,spdate,cxtype,qwaddress,',
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
			columns : [ [ {field : 'id',title : '编号',hidden : true,}, 
			{field : 'zzcdepart',title : '单位',width : 50,sortable : true}, 
			{field : 'name',title : '姓名',width : 40},
			{field : 'zw',title : '职务',width : 50,sortable : true}, 
			{field : 'bzgzl',title : '不在岗种类',width : 55,sortable : true}, 
			{field : 'ljdate',title : '离京日期',width : 60,sortable : true}, 
			{field : 'fjdate',title : '返京日期',width : 60,sortable : true}, 
			{field : 'spdate',title : '审批日期',width : 60,sortable : true}, 
			{field : 'cxtype',title : '出行方式',width : 60,sortable : true}, 
			{field : 'qwaddress',title : '前往地点',width : 60,sortable : true} ,
			{field : 'null',title : '操作',width : 50,
				formatter : function(value, rec, index) {
					if (!rec.id) {
						return '';
					}
					var href = '';
					href += "[<a href='#' onclick=zzcmjList.del('"+rec.id+"')>删除</a>]";
					return href;
			}}] ],
			onClickRow : function(rowIndex, rowData) {
				rowid = rowData.id;
				gridname = 'zzcmjList';
			}
		});$('#zzcmjList').datagrid('getPager').pagination({
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : '{from}-{to}共{total}条',
			showPageList : true,
			pageList : [ 10, 20, 30 ],
			showRefresh : true
		});$('#zzcmjList').datagrid('getPager').pagination({
			onBeforeRefresh : function(pageNumber, pageSize) {
				$(this).pagination('loading');$(this).pagination('loaded');
			}
		});
	});
var zzcmjList = {
		listSearch:function () {
			var queryParams = $('#zzcmjList').datagrid('options').queryParams;
			$('#zzcmjListtb').find('*').each(function() {
				queryParams[$(this).attr('name')] = $(this).val();
			});
			$('#zzcmjList').datagrid({
				url : "userController.do?zzcmjdatagrid&field=id,zzcdepart,name,zw,bzgzl,ljdate,fjdate,spdate,cxtype,qwaddress"
			});
		},
		del:function (id) {
			$.messager.confirm('提示','是否删除', function(r){
				if (r){
					$.ajax({
						async:false,
						type:"POST",
						url:"userController.do?delzzc&zzcid="+id,//要访问的后台地址
						dataType : 'json',
						error:function(d){
							$.messager.alert('提示',d.msg,'info');
						},
						success: function(d){
							$.messager.alert('提示',d.msg,'info');
							$('#zzcmjList').datagrid('reload');
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
			moveCompany.listSearch();
		},
		expFiles:function(){
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : 'jeecgJdbcController.do?zzcexpFiles',// 请求的action路径
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
						$('#zzcmjList').datagrid('reload');
					}
				}
			})
		}
	};
	function reloadTable() {
		$('#' + gridname).datagrid('reload');
	}
	function reloadzzcmjList() {
		$('#zzcmjList').datagrid('reload');
	}
	function getzzcmjListSelected(field) {
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
	function getzzcmjListSelections(field) {
		var ids = [];
		var rows = $('#zzcmjList').datagrid('getSelections');
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i][field]);
		}
		ids.join(',');return ids
	};
	function zzcmjListsearchbox(value, name) {
		var queryParams = $('#zzcmjList').datagrid('options').queryParams;
		queryParams[name] = value;
		queryParams.searchfield = name;$('#zzcmjList').datagrid('reload');
	}
	$('#zzcmjListsearchbox').searchbox({
		searcher : function(value, name) {
			zzcmjListsearchbox(value, name);
		},
		menu : '#zzcmjListmm',
		prompt : '请输入查询关键字'
	});
	function searchReset(name) {
		$("#" + name + "tb").find(":input").val("");zzcmjListsearch();
	}