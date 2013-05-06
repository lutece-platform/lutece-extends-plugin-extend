<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="resourceExtender" scope="session" class="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean" />

<% 
	resourceExtender.init( request, resourceExtender.RIGHT_MANAGE_RESOURCE_EXTENDER );
	IPluginActionResult result = resourceExtender.getCreateDefaultResourceExtender( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../../ErrorPage.jsp" %>
		<jsp:include page="../../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../../AdminFooter.jsp" %>
<%
	}
%>
