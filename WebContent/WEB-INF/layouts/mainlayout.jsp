<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
   <title><tiles:getAsString name="title" /></title>
   <script language="javascript" type="text/javascript" src="<c:url value="/js/prototype.js" />"></script>
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
<h2><tiles:getAsString name="title" /></h2>
<tiles:insert name="body" />
        </div>
      </div>
    </div>
    <div class="yui-b" id="leftpanel">
<tiles:insert name="left" />
    </div>
  </div>
  <div id="ft">Copyright &copy; 2008 The Tune Store</div>
</div>
</body>
</html>
