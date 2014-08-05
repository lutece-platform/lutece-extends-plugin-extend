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

import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.service.ExtendableResourceResourceIdService;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.pluginaction.AbstractPluginAction;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * CreateResourceTypePluginAction
 *
 */
public class ModifyDefaultConfigPluginAction extends AbstractPluginAction<IResourceExtenderSearchFields>
    implements IResourceExtenderPluginAction
{
    private static final String ACTION_NAME = "Modify default configuration";

    // PARAMETERS
    private static final String PARAMETER_MODIFY_DEFAULT_CONFIG = "modifyDefaultConfig";
    private static final String PARAMETER_FROM_URL = "from_url";
    private static final String PARAMETER_REFERER = "referer";
    private static final String PARAM_EXTENDER_TYPE = "extenderTypeModifyConfig";

    // MARKS
    private static final String MARK_PERMISSION_MODIFY_CONFIG = "permissionModifyConfig";
    private static final String MARK_RESOURCE_EXTENDER_CONFIGURABLE = "configurableExtenderTypes";

    // TEMPLATE
    private static final String TEMPLATE_BUTTON = "actions/modify_default_config.html";

    // MESSAGE
    private static final String MESSAGE_NO_EXTENDER_TYPE_SELECTED = "extend.modify_default_config.noExtenderTypeSelected";

    // JSP
    private static final String JSP_URL = "jsp/admin/plugins/extend/GetModifyDefaultConfig.jsp";

    // CONSTANT
    private static final String CONSTANT_AND = "&";
    private static final String CONSTANT_AND_HTML = "%26";
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( HttpServletRequest request, AdminUser adminUser, Map<String, Object> model )
    {
        model.put( MARK_PERMISSION_MODIFY_CONFIG,
            RBACService.isAuthorized( ExtendableResourceType.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, adminUser ) );

        List<IResourceExtender> listExtenders = _extenderService.getResourceExtenders(  );
        ReferenceList refListExtenderTypes = new ReferenceList(  );

        for ( IResourceExtender resourceExtender : listExtenders )
        {
            if ( resourceExtender.isConfigRequired(  ) )
            {
                ReferenceItem refItem = new ReferenceItem(  );
                refItem.setCode( resourceExtender.getKey(  ) );
                refItem.setName( resourceExtender.getTitle( AdminUserService.getLocale( request ) ) );
                refListExtenderTypes.add( refItem );
            }
        }

        model.put( MARK_RESOURCE_EXTENDER_CONFIGURABLE, refListExtenderTypes );
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
        return request.getParameter( PARAMETER_MODIFY_DEFAULT_CONFIG ) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPluginActionResult process( HttpServletRequest request, HttpServletResponse response, AdminUser adminUser,
        IResourceExtenderSearchFields sessionFields ) throws AccessDeniedException
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL );
        String strExtenderType = request.getParameter( PARAM_EXTENDER_TYPE );
        DefaultPluginActionResult result = new DefaultPluginActionResult(  );

        if ( StringUtils.isBlank( strExtenderType ) )
        {
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_NO_EXTENDER_TYPE_SELECTED,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }

        url.addParameter( PARAM_EXTENDER_TYPE, strExtenderType );

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

        result.setRedirect( url.getUrl(  ) );

        return result;
    }
}
