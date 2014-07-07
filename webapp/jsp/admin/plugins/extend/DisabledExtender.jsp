<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="resourceExtender" scope="session" class="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean" />

<% 
	resourceExtender.init( request, resourceExtender.RIGHT_MANAGE_RESOURCE_EXTENDER );
 	response.sendRedirect( resourceExtender.getDisabledExtender( request) );
%>
