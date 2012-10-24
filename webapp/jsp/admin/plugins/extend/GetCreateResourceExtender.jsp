<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="resourceExtender" scope="session" class="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean" />
<% resourceExtender.init( request, resourceExtender.RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE ) ; %>
<%= resourceExtender.getCreateResourceExtender( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
