<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean"%>

${ resourceExtenderJspBean.init( pageContext.request, ResourceExtenderJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER ) }

${ pageContext.setAttribute( 'pluginActionResult', resourceExtenderJspBean.getCreateDefaultResourceExtender( pageContext.request, pageContext.response ) ) }
${ not empty pageContext.getAttribute( 'pluginActionResult' ).redirect ? pageContext.response.sendRedirect( pageContext.getAttribute( 'pluginActionResult' ).redirect ) : '' }

${ not empty pageContext.getAttribute( 'pluginActionResult' ).htmlContent ? pageContext.getAttribute( 'pluginActionResult' ).htmlContent : '' }

<%@ include file="../../AdminFooter.jsp" %>