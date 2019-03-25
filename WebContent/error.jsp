<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.io.PrintWriter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
   <title>The Tunestore</title>
   <link rel="stylesheet" href="<c:url value="/css/reset.css" />" type="text/css">
   <link rel="stylesheet" href="<c:url value="/css/fonts.css" />" type="text/css">
   <link rel="stylesheet" href="<c:url value="/css/base.css" />" type="text/css">
   <link rel="stylesheet" href="<c:url value="/css/grids.css" />" type="text/css">
   <link rel="stylesheet" href="<c:url value="/css/tunestore.css" />" type="text/css">
</head>
<body>
<div id="doc3" class="yui-t3">
  <div id="hd"><img src="<c:url value="/images/tunestore.gif" />" width="468" height="60" alt="the tunestore - buy some tunes, give some tunes" /></div>
  <div id="bd">
    <div id="yui-main">
      <div class="yui-b">
        <div class="yui-g">
<h2>KABOOM!</h2>
<p>We're really sorry - the Tunestore fell over.  Please call support and
read the garbage below to them:</p>
<textarea cols="120" rows="20" style="font-family:monospace; width: 400px; white-space: nowrap;">
<% exception.printStackTrace(new PrintWriter(out)); %>
<%
Throwable t = exception.getCause();
while (t != null) {
  %>

ROOT CAUSE
<% t.printStackTrace(new PrintWriter(out)); %>
<%
  t = t.getCause();
}
%></textarea>
        </div>
      </div>
    </div>
    <div class="yui-b" id="leftpanel">
    </div>
  </div>
  <div id="ft">Copyright &copy; 2008 The Tune Store</div>
</div>
</body>
</html>
