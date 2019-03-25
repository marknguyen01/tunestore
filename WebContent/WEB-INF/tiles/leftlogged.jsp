<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
Welcome ${USERNAME}!<br />
<logic:messagesPresent message="false">
  <html:messages id="msg">
    <span class="error"><bean:write name="msg" /></span><br />
  </html:messages>
</logic:messagesPresent>
<logic:messagesPresent message="true">
  <html:messages id="msg" message="true">
    <strong><bean:write name="msg" /></strong><br />
  </html:messages>
</logic:messagesPresent>
Your account balance is <fmt:formatNumber pattern="\$#,##0.00" value="${BALANCE}" />
<br />
<br />
Add Balance:<br />
<html:form method="POST" action="/addbalance">
<table border="0" width="100%">
<tr>
<td class="prompt">Type:</td>
<td class="ui"><html:select property="vendor">
<html:option value="">-- SELECT</html:option>
<html:option value="VISA">VISA</html:option>
<html:option value="MASTERCARD">MASTERCARD</html:option>
</html:select>
</td>
</tr>
<tr>
  <td class="prompt">Number:</td>
  <td class="ui"><html:password property="cc" /></td>
</tr>
<tr>
  <td class="prompt">Amount:</td>
  <td class="ui"><html:text property="amount" /></td>
</tr>
<tr>
  <td colspan="2" class="ui" style="text-align: center"><input type="submit" value="Add" /></td>
</tr>
</table>
</html:form>
<br />
<br />
<a href="<c:url value="/friends.do" />">Friends</a><br />
<a href="<c:url value="/profile.do" />">Profile</a><br />
<a href="<c:url value="/index.jsp" />">CD's</a>
<br />
<br />
<a href="<c:url value="/logout.do" />">Log Out</a>