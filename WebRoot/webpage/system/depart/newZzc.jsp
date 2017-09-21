<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>新增民警不在岗情况数据</title>
	<script type="text/javascript" src="plug-in/common/newZzc.js"></script>
</head>
<body style="overflow-y: hidden" scroll="no">
<div>
	<form id="newZzcForm01" action="companyController.do?saveNew" name="newZzcForm01" method="post">
		<input type="hidden" name="companyType" value="1"></input>
		<input type="hidden" name="rsuperCompanyCode" value="000000"></input>

		<table cellpadding="0" width="100%" cellspacing="1" class="formtable">
			<tr>
				<td align="right" height="40" width="10%"><span class="filedzt">企业名称：</span></td>
				<td class="value" width="90%">
					<input name="companyName" id="newCompanycompanyName" >
				</td>
			</tr>
		</table>
	</form>
	<table cellpadding="0" width="100%" cellspacing="1" class="formtable">
		<tr>
			<td align="right" height="300">
				<table width="100%" id="newCompanyList01"></table>
			</td>
		</tr>
	</table>
	<table cellpadding="0" width="100%" cellspacing="1" class="formtable">
		<tr>
			<td align="right" height="40" width="30%"></td>
			<td class="value" width="70%">
				<a href="#" class="easyui-linkbutton" onclick="newZzc.save()">保存</a>
				<a href="#" class="easyui-linkbutton" onclick="newZzc.searchReset('newCompany')">重置</a>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" onclick="newZzc.getBack()">返回</a>
			</td>
		</tr>
	</table>
</div>
</body>
</html>