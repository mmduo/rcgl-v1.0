<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>不在岗民警信息</title>
  <t:base type="jquery,easyui,tools"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userController.do?zzcsaveUser">
   <input id="id" name="id" type="hidden" value="${zsZzc.id }">
   <table style="width:600px;" cellpadding="0" cellspacing="1" class="formtable">
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">单位:</label>
     </td>
     <td class="value" width="85%">
	<select id="zzcdepart"  name="zzcdepart"  datatype="*">
        <option value="${departList}" selected="selected">${departList}</option>
      </select>
      <span class="Validform_checktip">请选择单位</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">姓名:</label>
     </td>
     <td class="value" width="85%">
       <input id="name" class="inputxt" name="name" value="${zsZzc.name }" datatype="s2-30" >
      <span class="Validform_checktip">请输入姓名</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">职务:</label>
     </td>
     <td class="value" width="85%">
     	<select id="zw"  name="zw"  datatype="*" >
       <c:forEach items="${zwList}" var="zw">
        <option value="${zw.typename }" 
        <c:if test="${zw.typename==zsZzc.zw}">selected="selected"</c:if>>
         ${zw.typename}
        </option>
       </c:forEach>
      </select>
      <span >请选择职务</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">不在岗种类:</label>
     </td>
     <td class="value" width="85%">
       <input id="bzgzl" class="inputxt" name="bzgzl" value="${zsZzc.bzgzl }" datatype="s2-200" >
       <span class="Validform_checktip">必填</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
      审批领导:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="spld" class="inputxt" name="spld" value="${zsZzc.spld }" datatype="s2-10" >
      <span class="Validform_checktip">必填</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">审批日期:</label>
     </td>
     <td class="value" width="85%">
       <input id="spdate" name="spdate" class="easyui-datebox" value="${zsZzc.spdate }">
       <span class="Validform_checktip">必填</span>
       <%-- <input id="spdate" class="inputxt" name="spdate" value="${zsZzc.spdate }" > --%>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">开始日期:</label>
     </td>
     <td class="value" width="85%">
       <input id="ksdate" name="ksdate" class="easyui-datebox" value="${zsZzc.ksdate }" >
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       离京日期:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="ljdate" name="ljdate" class="easyui-datebox" value="${zsZzc.ljdate }" >
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
      返京日期:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="fjdate" name="fjdate" class="easyui-datebox" value="${zsZzc.fjdate }" >
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       结束日期:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="jsdate" name="jsdate" class="easyui-datebox" value="${zsZzc.jsdate }">
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
     出行方式:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="cxtype" class="inputxt" name="cxtype" value="${zsZzc.cxtype }">
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       前往地点:
      </label>
     </td>
     <td class="value" width="85%">
       <input id="qwaddress" class="inputxt" name="qwaddress" value="${zsZzc.qwaddress }">
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label>备注:</label>
     </td>
     <td class="value" width="85%">
       <input id="note" class="inputxt" name="note" value="${zsZzc.note }">
     </td>
    </tr>
   </table>
  </t:formvalid>
 </body>