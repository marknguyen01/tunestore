<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<h2>Here's What People Say</h2>
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
</p>
</div><br clear="all" />
<div id="commentblock">
<jsp:include page="/WEB-INF/tiles/_commentblock.jsp" />
</div>
<br clear="all" />
<c:if test="${not empty USERNAME}">
<strong>Leave Your Comment:</strong><br />
<script language="javascript" type="text/javascript">
<!--
function submitform() {
  var cd = $('cd').value;
  var comment = $('comment').value;
//.replace(/[^A-Za-z0-9 ,.!?]/g, '');
  new Ajax.Updater('commentblock','<c:url value="/leaveComment.do" />', {
    parameters: {
      cd: cd,
      comment: comment
    }
  });
  $('comment').value = '';
}
// -->
</script>
<form name="lcform" id="lcform" onsubmit="javascript:submitform(); return false;" action="javascript:void(0);">
<input type="hidden" name="cd" id="cd" value="${cd.id}" />
<textarea name="comment" id="comment" cols="60" rows="5"></textarea><br />
<input type="submit" value="Submit" />
</form>
</c:if>
