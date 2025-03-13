<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />


<%@page import="fr.paris.lutece.plugins.extend.web.ResourceExtenderJspBean"%>

${ resourceExtenderJspBean.init( pageContext.request, ResourceExtenderJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER ) }
${ pageContext.response.sendRedirect( resourceExtenderJspBean.getCreateResourceExtender( pageContext.request )) }

<%@ include file="../../AdminFooter.jsp" %>