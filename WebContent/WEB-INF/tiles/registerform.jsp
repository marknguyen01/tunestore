<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2>Register</h2>
<body>
<logic:messagesPresent name="ERRORS.REGISTER">
<ul>
  <html:messages name="ERRORS.REGISTER" id="msg">
<li class="error"><bean:write name="msg" /></li>
  </html:messages>
</ul>
</logic:messagesPresent>
<html:form action="/register">
<table border="0" cellpadding="2" cellspacing="0">
<tr>
<td><strong><bean:message key="prompt.username" /></strong></td>
<td><html:text property="username" /></td>
</tr>
<tr>
<td><strong><bean:message key="prompt.password" /></strong></td>
<td><html:password property="password" /></td>
</tr>
<tr>
<td><strong><bean:message key="prompt.rptpass" /></strong></td>
<td><html:password property="rptpass" /></td>
</tr>
<tr>
<td colspan="2"><html:submit /></td>
</tr>
</table>
</html:form>
</body>