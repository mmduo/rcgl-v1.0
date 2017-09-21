<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
 <head>
  
 </head>
 <body style="overflow-y: hidden" scroll="no">
 <script type="text/javascript">
 openDocObj = new ActiveXObject("SharePoint.OpenDocuments.1"); // 为了兼容Office XP，可以创建“SharePoint.OpenDocuments.1”
 openDocObj.EditDocument("D:/sdd.xml");
 </script>
  <%-- <%@include file="/webpage/system/type/test1.html"%> --%>
 </body>
</html>
