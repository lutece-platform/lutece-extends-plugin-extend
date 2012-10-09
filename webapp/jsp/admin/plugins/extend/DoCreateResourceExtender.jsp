<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="resourceExtender" scope="session" class="fr.paris.lutece.plugins.socialhub.web.ResourceExtenderJspBean" />
<% 
	resourceExtender.init( request, resourceExtender.RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE );
 	response.sendRedirect( resourceExtender.doCreateResourceExtender( request) );
%>
