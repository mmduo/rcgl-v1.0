<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>不在岗民警信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
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
       <c:forEach items="${departList}" var="zzcdepart">
        <option value="${zzcdepart.typename }" 
        <c:if test="${zzcdepart.typename==zsZzc.zzcdepart}">selected="selected"</c:if>>
         ${zzcdepart.typename}
        </option>
       </c:forEach>
      </select>
      <span class="Validform_checktip">请选择单位</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       姓名:
      </label>
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
     	<select id="zw"  name="zw"  datatype="*">
       <c:forEach items="${zwList}" var="zw">
        <option value="${zw.typename }" 
        <c:if test="${zw.typename==zsZzc.zw}">selected="selected"</c:if>>
         ${zw.typename}
        </option>
       </c:forEach>
      </select>
      <span  class="Validform_checktip">请选择职务</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       	不在岗种类:
      </label>
     </td>
     <!-- Upd By ZM 20170918 input改为 checkbox Start-->
     <td class="value" width="85%">
       <c:set var="sources" value="${zsZzc.bzgzl}"/>
       <c:forEach items="${bzgzlList}" var="bzgzl">
       <c:set var="itemStr" value="${bzgzl.typename}"/>
       <input datatype="*" name="bzgzl" class="rt2" id="bzgzl+${bzgzl.typecode}" type="checkbox"  value="${bzgzl.typename}"
       <c:if test="${fn:contains(sources,itemStr)}">checked="checked"</c:if>/>  
       <label for="bzgzl+${bzgzl.typecode}">${bzgzl.typename}</label>
       </c:forEach>
       <span class="Validform_checktip">必填</span>
     </td>
     <!-- Upd By ZM 20170918 input改为 checkbox End-->
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
    <!-- Upd By ZM 20170918 必填项验证  Start-->
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">审批日期:</label>
     </td>
     <td class="value" width="85%">
	   <input class="Wdate" onClick="WdatePicker()" style="width: 150px" id="spdate" 
	 		name="spdate" value="${zsZzc.spdate}" datatype="*" readonly="readonly">
       <span class="Validform_checktip">必填</span>
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">起止日期:</label>
     </td>
     <td class="value" width="85%">
	   <input class="Wdate" onClick="WdatePicker()" style="width: 150px" id="ksdate" 
	 		name="ksdate" value="${zsZzc.ksdate}" readonly="readonly">
	   <label class="Validform_label"> - </label>
	   <input class="Wdate" onClick="WdatePicker()" style="width: 150px" id="jsdate" 
	 		name="jsdate" value="${zsZzc.jsdate}" readonly="readonly">
     </td>
    </tr>
    <!-- Upd By ZM 20170918 必填项验证 End-->
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">备注:</label>
     </td>
     <td class="value" width="85%">
       <input id="note" class="inputxt" name="note" value="${zsZzc.note }">
     </td>
    </tr>
   </table>
  <div id="div-dg">
    <table style="width:600px;" cellpadding="0" cellspacing="1" class="formtable">
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
       离京日期:
      </label>
     </td>
     <td class="value" width="85%">
	   <input class="Wdate" onClick="WdatePicker()" style="width: 150px" id="ljdate" 
	 		name="ljdate" value="${zsZzc.ljdate}" readonly="readonly">
	   <label class="Validform_label"> - </label>
	   <input class="Wdate" onClick="WdatePicker()" style="width: 150px" id="fjdate" 
	 		name="fjdate" value="${zsZzc.fjdate}" readonly="readonly">
     </td>
    </tr>
    <tr>
     <td align="right" width="15%" nowrap>
      <label class="Validform_label">
     出行方式:
      </label>
     </td>
     <!-- Upd By ZM 20170918 input改为 checkbox Start-->
     <td class="value" width="85%">
       <c:set var="sources" value="${zsZzc.cxtype}"/>
       <c:forEach items="${cxtypeList}" var="cxtype">
       <c:set var="itemStr" value="${cxtype.typename}"/>
       <input name="cxtype" class="rt2" id="cxtype+${cxtype.typecode}" type="checkbox"  value="${cxtype.typename}"
       <c:if test="${fn:contains(sources,itemStr)}">checked="checked"</c:if>/>  
       <label for="cxtype+${cxtype.typecode}">${cxtype.typename}</label>
       </c:forEach>
     </td>
     <!-- Upd By ZM 20170918 input改为 checkbox End-->
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
    </table><br/><br/><br/>
   </div>
  </t:formvalid>
 </body>