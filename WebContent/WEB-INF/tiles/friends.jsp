<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2>Friend Requests</h2>
<c:forEach items="${requests}" var="f">
<strong>${f.username}</strong><br />
<c:url var="friendurl" value="/addfriend.do">
<c:param name="friend" value="${f.username}" />
</c:url>
<a href="${friendurl}">Approve</a><br />
</c:forEach>
<br />

<h2>My Friends</h2>
<c:forEach items="${myfriends}" var="f">
<strong>${f.username}</strong><br />
<c:choose>
<c:when test="${f.approved}">
Approved<br />
<c:url var="cdlink" value="/viewcds.do">
<c:param name="friend" value="${f.username}" />
</c:url>
<a href="${cdlink}">View ${f.username}'s CD's</a><br />
</c:when>
<c:otherwise>
Waiting<br />
</c:otherwise>
</c:choose><br />
</c:forEach>
<br />
<h2>Add a Friend</h2>
<html:form action="/addfriend" method="POST">
Friend name:  <html:text property="friend" /><br />
<html:submit value="Submit" />
</html:form>