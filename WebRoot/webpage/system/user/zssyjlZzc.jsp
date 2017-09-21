<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>警力维护</title>
  <t:base type="jquery,easyui,tools"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="userController.do?zzcsyjlsaveUser">
   <input id="id" name="id" type="hidden" value="${zsType.id }">
   <table style="width:600px;" cellpadding="0" cellspacing="1" class="formtable">
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">单位:</label>
     </td>
     <td class="value" width="85%">
      <c:if test="${zsType.id!=null }">
     ${zsType.typename }
     </c:if>
      <c:if test="${zsType.id==null }">
       <input id="typename" class="inputxt" name="typename" value="${zsType.typename }" >
        </c:if>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">警力:</label>
     </td>
     <td class="value" width="85%">
       <input id="value" class="inputxt" name="value" value="${zsType.value }" >
     </td>
    </tr>
   </table>
  </t:formvalid>
 </body>