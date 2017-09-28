$(function() {
		$('#zzchzList').datagrid({
			idField : 'id',
			title : '十二总队民警不在岗情况汇总表',
			url : 'userController.do?zzchzdatagrid&field=typename,jl,nlj,bzghj,wlj,ylj',
			fit : true,
			loadMsg : '数据加载中...',
			pageSize : 10,
			pagination : true,
			sortOrder : 'asc',
			rownumbers : true,
			singleSelect : true,
			fitColumns : true,
			showFooter : true,
			nowrap: true,
			striped: true,
			collapsible:true,
			frozenColumns : [ [
			  {field : 'typename',title : '单位',width : 110},
			  {field : 'jl',title : '实有警力数',width : 110},
			  {field : 'nlj',title : '已审批拟离京人数',width : 140,sortable : true}
			] ],
			columns : [ [ {title:'不在岗人数',width : 140,colspan:3}],[{
				field : 'bzghj',
				title : '小计',
				width : 40,
				sortable : true
			}, {
				field : 'wlj',
				title : '不在岗未离京人数',
				width : 50,
				sortable : true
			}, {
				field : 'ylj',
				title : '不在岗已离京人数',
				width : 50,
				sortable : true
			} ] ],
			onClickRow : function(rowIndex, rowData) {
				rowid = rowData.id;
				gridname = 'zzchzList';
			}
		});$('#zzchzList').datagrid('getPager').pagination({
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : '{from}-{to}共{total}条',
			showPageList : true,
			pageList : [ 10, 20, 30 ],
			showRefresh : true
		});$('#zzchzList').datagrid('getPager').pagination({
			onBeforeRefresh : function(pageNumber, pageSize) {
				$(this).pagination('loading');$(this).pagination('loaded');
			}
		});
	});
var zzchzList = {
		listSearch:function () {
			var queryParams = $('#zzchzList').datagrid('options').queryParams;
			$('#zzchzListtb').find('*').each(function() {
				queryParams[$(this).attr('name')] = $(this).val();
			});
			$('#zzchzList').datagrid({
				url : "userController.do?zzchzdatagrid&field=typename,value,nlj,bzghj,wlj,ylj"
			});
		},
		expFiles:function(){
			var queryParams = $('#zzchzList').datagrid('options').queryParams;
			var zzchzqdate = $("[name=zzchzqdate]").val();
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : 'jeecgJdbcController.do?zzchzexpFiles&zzchzqdate='+zzchzqdate,// 请求的action路径
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
						$('#zzchzList').datagrid('reload');
					}
				}
			})
		},
		expExcelFiles:function(){
			var queryParams = $('#zzchzList').datagrid('options').queryParams;
			var zzchzqdate = $("[name=zzchzqdate]").val();
			window.location.href="jeecgJdbcController.do?zzchzexpExcelFiles&zzchzqdate="+zzchzqdate;// 请求的action路径;
		}
	};
function searchReset(name) {
	$("#" + name + "tb").find(":input").val("");
}