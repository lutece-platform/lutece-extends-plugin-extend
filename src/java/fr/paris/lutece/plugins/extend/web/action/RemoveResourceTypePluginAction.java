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

import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.service.type.ResourceTypeResourceIdService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * RemoveResourceTypePluginAction
 *
 */
public class RemoveResourceTypePluginAction extends AbstractPluginAction<IResourceExtenderSearchFields>
    implements IResourceTypePluginAction
{
    private static final String ACTION_NAME = "Remove resource type";

    // PARAMETERS
    /** the button is an image so the name is .x or .y */
	private static final String PARAMETER_REMOVE_TYPE = "removeResourceType";
    private static final String PARAMETER_RESOURCE_TYPE = "resourceType";

    // MARKS
    private static final String MARK_RESOURCE_TYPES = "resourceTypes";

    // TEMPLATE
    private static final String TEMPLATE_BUTTON = "actions/remove_resource_type.html";

    // ATTRIBUTES
    private static final String ATTRIBUTE_KEY = "key";

    // JSP
    private static final String JSP_URL = "jsp/admin/plugins/socialhub/ConfirmRemoveResourceType.jsp";
    @Inject
    private IExtendableResourceTypeService _resourceTypeService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        model.put( MARK_RESOURCE_TYPES,
            ReferenceList.convert( RBACService.getAuthorizedCollection( _resourceTypeService.findAll(  ),
                    ResourceTypeResourceIdService.PERMISSION_DELETE, adminUser ), ATTRIBUTE_KEY, ATTRIBUTE_KEY, false ) );
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
        return request.getParameter( PARAMETER_REMOVE_TYPE ) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
        IResourceExtenderSearchFields sessionFields ) throws AccessDeniedException
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL );
        url.addParameter( PARAMETER_RESOURCE_TYPE, request.getParameter( PARAMETER_RESOURCE_TYPE ) );

        DefaultPluginActionResult result = new DefaultPluginActionResult(  );
        result.setRedirect( url.getUrl(  ) );

        return result;
    }
}
