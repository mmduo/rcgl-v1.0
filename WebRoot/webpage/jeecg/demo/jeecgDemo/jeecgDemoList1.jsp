<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
 <div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <script type="text/javascript" src="plug-in/common/fSlist.js"></script>
<table width="100%" id="jeecgDemoList1" toolbar="#jeecgDemoList1tb"></table>
<div id="jeecgDemoList1tb" style="padding:3px; ">
	<div name="searchColums">
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">文档标题：</span>
		<input type="text" name="docTitle" style="width: 100px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">内容：</span>
		<input type="text" name="docTxt" style="width: 200px" /></span>
		<span style="display:-moz-inline-box;display:inline-block;">
		<span style="display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;">处理日期：</span>
		<input type="text" name="note_begin" style="width: 94px" />
		<span style="display:-moz-inline-box;display:inline-block;width: 8px;text-align:right;">~</span>
		<input type="text" name="note_end" style="width: 94px" /></span>
	</div>
	<div style="height:30px;" class="datagrid-toolbar">
		<span style="float:right">
		<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="jeecgDemoList1search()">查询</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" onclick="searchReset('jeecgDemoList1')">重置</a>
		</span>
	</div>
</div>
  
  </div>
  </div>
 <script type="text/javascript">
	$(document).ready(function(){
		$("input[name='note_begin']").attr("class","easyui-datebox");
		$("input[name='note_end']").attr("class","easyui-datebox");
	});
</script>