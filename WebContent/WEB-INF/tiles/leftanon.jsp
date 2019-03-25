<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<logic:messagesPresent>
  <html:messages id="msg">
    <span class="message">${msg}</span><br />
  </html:messages>
</logic:messagesPresent>
<html:form action="/login">
<table>
<tr>
  <td class="prompt"><bean:message key="prompt.username" />:</td>
  <td class="ui"><html:text property="username" /></td>
</tr>
<tr>
  <td class="prompt"><bean:message key="prompt.password" />:</td>
  <td class="ui"><html:password property="password" /></td>
</tr>
<tr>
  <td colspan="2" class="ui"><html:checkbox property="stayLogged" value="true" />&nbsp;<bean:message key="prompt.stayLogged" /></td>
</tr>
<tr>
  <td colspan="2" class="ui"><html:submit value="Login" /></td>
</tr>
</table>
</html:form>
<form method="post" action="<c:url value="/login" />">
</form>
<p>Don't have an account?  <html:link action="/registerform">Register here</html:link>