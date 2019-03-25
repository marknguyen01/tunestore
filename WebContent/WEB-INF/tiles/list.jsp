<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:forEach var="cd" items="${CDLIST}">
<div class="cd">
<img src="<c:url value="/images/${cd.image}" />" width="115" height="115" /><br />
  <strong>${cd.album}</strong><br />
  ${cd.artist}
<p>
<br />
  <c:url value="/giftsetup.do" var="giftlink">
    <c:param name="cd" value="${cd.id}" />
  </c:url>
  <c:choose>
  <c:when test="${cd.owned}">
    <c:url value="/download.do" var="cdlink">
      <c:param name="cd" value="${cd.bits}" />
    </c:url>
    <a href="${cdlink}">Download</a><br />
    <a href="${giftlink}">Gift</a> (<fmt:formatNumber type="currency" currencySymbol="$" value="${cd.price}" />)
  </c:when>
  <c:otherwise>
    <c:url value="/buy.do" var="cdlink">
      <c:param name="cd" value="${cd.id}" />
    </c:url>
    <a href="${cdlink}">Buy</a>/<a href="${giftlink}">Gift</a> (<fmt:formatNumber type="currency" currencyCode="USD" currencySymbol="$" value="${cd.price}" />)
  </c:otherwise>
  </c:choose>
  <c:url value="/comments.do" var="commentlink">
    <c:param name="cd" value="${cd.id}" />
  </c:url>
  <br /><a href="${commentlink}">Comments</a>
</p>
</div>
</c:forEach>
