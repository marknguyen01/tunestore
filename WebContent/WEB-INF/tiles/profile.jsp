<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2>Profile</h2>
Username:  ${USERNAME}<br />
Balance: <fmt:formatNumber currencySymbol="$" type="currency" value="${BALANCE}" /><br />

<h2>Password</h2>
<logic:messagesPresent name="com.tunestore.action.PasswordAction.ERROR_KEY">
<html:messages id="msg" name="com.tunestore.action.PasswordAction.ERROR_KEY">
<span class="error"><bean:write name="msg" /></span><br />
</html:messages>
</logic:messagesPresent>
<logic:messagesPresent name="com.tunestore.action.PasswordAction.MESSAGE_KEY">
<html:messages id="msg" name="com.tunestore.action.PasswordAction.MESSAGE_KEY">
<strong><bean:write name="msg" /></strong><br />
</html:messages>
</logic:messagesPresent>
<html:form action="/password" method="post">
New Password:  <html:password property="password" size="20" /><br />
Repeat New Password:  <html:password property="rptpass" size="20" /><br />
<input type="submit" value="Change Password" />
</html:form>
