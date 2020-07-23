/*
 * Copyright (c) 2002-2020, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.extend.web.action;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * CreateResourceExtenderPluginAction
 *
 */
public class CreateResourceExtenderPluginAction extends AbstractPluginAction<IResourceExtenderSearchFields> implements IResourceExtenderPluginAction
{
    private static final String ACTION_NAME = "Create resource extender";

    // PARAMETERS
    private static final String PARAMETER_CREATE_RESOURCE_EXTENDER = "createResourceExtender";
    private static final String PARAMETER_MANAGE_BY_RESOURCE = "manageByResource";
    private static final String PARAMETER_FROM_URL = "from_url";
    private static final String PARAMETER_REFERER = "referer";

    // TEMPLATE
    private static final String TEMPLATE_BUTTON = "actions/create_resource_extender.html";

    // JSP
    private static final String JSP_URL = "jsp/admin/plugins/extend/GetCreateResourceExtender.jsp";

    // CONSTANT
    private static final String CONSTANT_AND = "&";
    private static final String CONSTANT_AND_HTML = "%26";

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getButtonTemplate( )
    {
        return TEMPLATE_BUTTON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName( )
    {
        return ACTION_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_CREATE_RESOURCE_EXTENDER ) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
            IResourceExtenderSearchFields sessionFields ) throws AccessDeniedException
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL );
        url.addParameter( PARAMETER_MANAGE_BY_RESOURCE, request.getParameter( PARAMETER_MANAGE_BY_RESOURCE ) );

        String strFromUrl = request.getParameter( PARAMETER_FROM_URL );

        if ( StringUtils.isBlank( strFromUrl ) )
        {
            strFromUrl = request.getHeader( PARAMETER_REFERER );
        }

        if ( StringUtils.isNotBlank( strFromUrl ) )
        {
            strFromUrl = strFromUrl.replace( CONSTANT_AND, CONSTANT_AND_HTML );
            url.addParameter( PARAMETER_FROM_URL, strFromUrl );
        }

        DefaultPluginActionResult result = new DefaultPluginActionResult( );
        result.setRedirect( url.getUrl( ) );

        return result;
    }
}
