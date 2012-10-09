/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * CreateResourceExtenderPluginAction
 *
 */
public class CreateResourceExtenderPluginAction extends AbstractPluginAction<IResourceExtenderSearchFields>
    implements IResourceExtenderPluginAction
{
    private static final String ACTION_NAME = "Create resource extender";

    // PARAMETERS
    private static final String PARAMETER_CREATE_RESOURCE_EXTENDER = "createResourceExtender";
    private static final String PARAMETER_EXTENDER_TYPE = "extenderType";
    private static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    private static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";

    // MARKS
    private static final String MARK_RESOURCE_TYPES = "resourceTypes";
    private static final String MARK_EXTENDER_TYPES = "extenderTypes";

    // TEMPLATE
    private static final String TEMPLATE_BUTTON = "actions/create_resource_extender.html";

    // JSP
    private static final String JSP_URL = "jsp/admin/plugins/extend/DoCreateResourceExtender.jsp";
    @Inject
    private IExtendableResourceTypeService _resourceTypeService;
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        model.put( MARK_RESOURCE_TYPES, _resourceTypeService.findAllAsRef(  ) );
        model.put( MARK_EXTENDER_TYPES, _extenderService.getExtenderTypes( request.getLocale(  ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getButtonTemplate(  )
    {
        return TEMPLATE_BUTTON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(  )
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
        url.addParameter( PARAMETER_EXTENDER_TYPE, request.getParameter( PARAMETER_EXTENDER_TYPE ) );
        url.addParameter( PARAMETER_ID_EXTENDABLE_RESOURCE, request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE ) );
        url.addParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE, request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );

        DefaultPluginActionResult result = new DefaultPluginActionResult(  );
        result.setRedirect( url.getUrl(  ) );

        return result;
    }
}
