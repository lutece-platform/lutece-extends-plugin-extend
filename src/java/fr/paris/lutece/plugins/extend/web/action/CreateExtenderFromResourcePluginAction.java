/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.resource.ExtendableResourcePluginActionManager;
import fr.paris.lutece.portal.web.resource.IExtendableResourcePluginAction;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * CreateExtenderFromResourcePluginAction
 *
 */
public class CreateExtenderFromResourcePluginAction extends AbstractPluginAction<IExtendableResource>
    implements IExtendableResourcePluginAction
{
    private static final String ACTION_NAME = "Create extender from the resource";

    // PARAMETERS
    private static final String PARAMETER_CREATE_EXTENDER_FROM_RESOURCE = "createExtenderFromResource";
    private static final String PARAMETER_EXTENDER_TYPE = "extenderType";
    private static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    private static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
    private static final String PARAMETER_FILTER_EXTENDER_TYPE = "filterExtenderType";
    private static final String PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE = "filterIdExtendableResource";
    private static final String PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE = "filterExtendableResourceType";
    private static final String PARAMETER_SEARCH = "search";
    private static final String PARAMETER_REFERER = "referer";
    private static final String PARAMETER_FROM_URL = "from_url";
    private static final String PARAMETER_REMOVE = "remove";
    private static final String PARAMETER_ID_EXTENDER = "idExtender";

    // MARKS
    private static final String MARK_EXTENDER_TYPES = "extenderTypes";
    private static final String MARK_EXTENDER_TYPES_INSTALLED = "extenderTypesInstalled";

    // JSP
    private static final String JSP_URL = "jsp/admin/plugins/extend/DoCreateResourceExtender.jsp";
    private static final String JSP_URL_REMOVE = "jsp/admin/plugins/extend/ConfirmRemoveResourceExtender.jsp";

    // CONSTANT
    private static final String CONSTANT_AND = "&";
    private static final String CONSTANT_AND_HTML = "%26";
    @Inject
    private IResourceExtenderService _resourceExtenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( HttpServletRequest request )
    {
        return request.getParameter( PARAMETER_CREATE_EXTENDER_FROM_RESOURCE ) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        String strIdExtendableResource = (String) model.get( ExtendableResourcePluginActionManager.MARK_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = (String) model.get( ExtendableResourcePluginActionManager.MARK_EXTENDABLE_RESOURCE_TYPE );

        // TODO : Display the action only if the user has the permission
        model.put( MARK_EXTENDER_TYPES, _resourceExtenderService.getExtenderTypes( request.getLocale(  ) ) );
        model.put( MARK_EXTENDER_TYPES_INSTALLED,
            _resourceExtenderService.getExtenderTypesInstalled( strIdExtendableResource, strExtendableResourceType,
                PluginService.getPlugin( ExtendPlugin.PLUGIN_NAME ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getButtonTemplate(  )
    {
        return null;
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
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
        IExtendableResource sessionFields ) throws AccessDeniedException
    {
        // TODO : Check if the current user has the permission to do the action
        UrlItem url;

        if ( Boolean.valueOf( request.getParameter( PARAMETER_REMOVE ) ) )
        {
            url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_REMOVE );

            ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter(  );
            filter.setFilterIdExtendableResource( request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE ) );
            filter.setFilterExtendableResourceType( request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );
            filter.setFilterExtenderType( request.getParameter( PARAMETER_EXTENDER_TYPE ) );

            List<Integer> listIdExtender = _resourceExtenderService.findIdsByFilter( filter );

            if ( ( listIdExtender != null ) && ( listIdExtender.size(  ) == 1 ) )
            {
                url.addParameter( PARAMETER_ID_EXTENDER, listIdExtender.get( 0 ) );
            }
        }
        else
        {
            url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL );
            url.addParameter( PARAMETER_EXTENDER_TYPE, request.getParameter( PARAMETER_EXTENDER_TYPE ) );
            url.addParameter( PARAMETER_ID_EXTENDABLE_RESOURCE, request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE ) );
            url.addParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE,
                request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );
            // Add filter parameters in order
            url.addParameter( PARAMETER_FILTER_EXTENDER_TYPE, request.getParameter( PARAMETER_EXTENDER_TYPE ) );
            url.addParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE,
                request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE ) );
            url.addParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE,
                request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE ) );
            url.addParameter( PARAMETER_SEARCH, PARAMETER_SEARCH );
        }

        url.addParameter( PARAMETER_FROM_URL,
            StringUtils.replace( request.getHeader( PARAMETER_REFERER ), CONSTANT_AND, CONSTANT_AND_HTML ) );

        DefaultPluginActionResult result = new DefaultPluginActionResult(  );
        result.setRedirect( url.getUrl(  ) );

        return result;
    }
}
