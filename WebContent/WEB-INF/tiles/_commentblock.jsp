<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty comments}">
Peope haven't said anything
</c:if>
<c:forEach items="${comments}" var="comment">
<div class="comment">
<strong><c:out value="${comment.leftby}" escapeXml="false" /></strong> says:
<blockquote>
<c:out value="${comment.comment}" escapeXml="false" />
</blockquote>
</div>
</c:forEach>
