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
package fr.paris.lutece.plugins.extend.web;

import fr.paris.lutece.plugins.extend.business.DefaultExtendableResource;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.service.DefaultExtendableResourceService;
import fr.paris.lutece.plugins.extend.service.ExtendableResourceManager;
import fr.paris.lutece.plugins.extend.service.ExtendableResourceResourceIdService;
import fr.paris.lutece.plugins.extend.service.IDefaultExtendableResourceService;
import fr.paris.lutece.plugins.extend.service.IExtendableResourceManager;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.type.ExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.service.type.ResourceTypeResourceIdService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.util.ExtendUtils;
import fr.paris.lutece.plugins.extend.web.action.IResourceExtenderPluginAction;
import fr.paris.lutece.plugins.extend.web.action.IResourceExtenderSearchFields;
import fr.paris.lutece.plugins.extend.web.action.IResourceTypePluginAction;
import fr.paris.lutece.plugins.extend.web.action.ResourceExtenderSearchFields;
import fr.paris.lutece.plugins.extend.web.component.IResourceExtenderComponentManager;
import fr.paris.lutece.plugins.extend.web.component.ResourceExtenderComponentManager;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginAction;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


/**
 *
 * ResourceExtenderJspBean
 *
 */
public class ResourceExtenderJspBean extends PluginAdminPageJspBean
{
    /** The Constant RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE_TYPE. */
    public static final String RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE_TYPE = "RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE_TYPE";

    /** The Constant RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE. */
    public static final String RIGHT_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE = "RESOURCE_EXTENDER_MANAGEMENT_BY_RESOURCE";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE_PAGE_TITLE = "extend.manage_resource_extenders_by_resource_type.pageTitle";
    private static final String PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_PAGE_TITLE = "extend.manage_resource_extenders_by_resource.pageTitle";
    private static final String PROPERTY_CREATE_RESOURCE_TYPE_PAGE_TITLE = "extend.create_resource_type.pageTitle";
    private static final String PROPERTY_MODIFY_RESOURCE_TYPE_PAGE_TITLE = "extend.modify_resource_type.pageTitle";
    private static final String PROPERTY_EXTENDER_CONFIG_PAGE_TITLE = "extend.resource_extender_config.pageTitle";
    private static final String PROPERTY_EXTENDER_INFO_PAGE_TITLE = "extend.resource_extender_info.pageTitle";
    private static final String PROPERTY_EXTENDER_HISTORY_PAGE_TITLE = "extend.resource_extender_history.pageTitle";
    private static final String PROPERTY_CREATE_DEFAULT_RESOURCE_PAGE_TITLE = "extend.create_default_resource_extender.pageTitle";

    // MESSAGES
    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "extend.message.error.genericMessage";
    private static final String MESSAGE_CONFIRM_REMOVE_RESOURCE_EXTENDER = "extend.message.confirm.removeResourceExtender";
    private static final String MESSAGE_CONFIRM_REMOVE_RESOURCE_TYPE = "extend.message.confirm.removeResourceType";
    private static final String MESSAGE_STOP_GENERIC_MESSAGE = "extend.message.stop.genericMessage";
    private static final String MESSAGE_CANNOT_DUPLICATE_RESOURCE_TYPE = "extend.message.cannotDuplicateResourceType";
    private static final String MESSAGE_EXTENDER_TO_ALL_RESOURCES_ALREADY_EXISTS = "extend.message.extenderToAllResourcesAlreadyExists";
    private static final String MESSAGE_EXTENDER_WITH_ID_RESOURCES_ALREADY_EXISTS = "extend.message.extenderWithIdAlreadyExists";
    private static final String MESSAGE_UNAUTHORIZED_ACTION = "extend.message.unauthorizedAction";

    // PARAMETERS
    private static final String PARAMETER_RESOURCE_TYPE = "resourceType";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_ID_EXTENDER = "idExtender";
    private static final String PARAMETER_SESSION = "session";
    private static final String PARAMETER_SEARCH = "search";
    private static final String PARAMETER_FILTER_EXTENDER_TYPE = "filterExtenderType";
    private static final String PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE = "filterIdExtendableResource";
    private static final String PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE = "filterExtendableResourceType";
    private static final String PARAMETER_EXTENDER_TYPE = "extenderType";
    private static final String PARAMETER_ID_EXTENDABLE_RESOURCE = "idExtendableResource";
    private static final String PARAMETER_EXTENDABLE_RESOURCE_TYPE = "extendableResourceType";
	private static final String PARAMETER_FROM_URL = "from_url";
    private static final String PARAMETER_EXTENDER_TYPE_DEFAULT_CONFIG = "extenderTypeModifyConfig";

    // MARKS
    private static final String MARK_RESOURCE_TYPE_ACTIONS = "resourceTypeActions";
    private static final String MARK_RESOURCE_EXTENDER_ACTIONS = "resourceExtenderActions";
    private static final String MARK_RESOURCE_TYPE = "resourceType";
    private static final String MARK_MANAGE_BY_RESOURCE = "manageByResource";
    private static final String MARK_RESOURCE_EXTENDER = "resourceExtender";

    // JSP
    private static final String JSP_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE = "ManageResourceExtendersByResource.jsp";
    private static final String JSP_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE_TYPE = "ManageResourceExtendersByResourceType.jsp";
    private static final String JSP_URL_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE = "jsp/admin/plugins/extend/" +
        JSP_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE_TYPE;
    private static final String JSP_URL_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE = "jsp/admin/plugins/extend/" +
        JSP_MANAGE_RESOURCE_EXTENDER_BY_RESOURCE;
    private static final String JSP_URL_DO_REMOVE_RESOURCE_EXTENDER = "jsp/admin/plugins/extend/DoRemoveResourceExtender.jsp";
    private static final String JSP_URL_DO_REMOVE_RESOURCE_TYPE = "jsp/admin/plugins/extend/DoRemoveResourceType.jsp";
    private static final String JSP_URL_CREATE_DEFAULT_RESOURCE_EXTENDER = "jsp/admin/plugins/extend/CreateDefaultResourceExtender.jsp";
    private static final String JSP_URL_MODIFY_RESOURCE_EXTENDER_CONFIG = "jsp/admin/plugins/extend/ModifyExtenderConfig.jsp";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE = "admin/plugins/extend/manage_resource_extenders_by_resource_type.html";
    private static final String TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE = "admin/plugins/extend/manage_resource_extenders_by_resource.html";
    private static final String TEMPLATE_CREATE_RESOURCE_TYPE = "admin/plugins/extend/create_resource_type.html";
    private static final String TEMPLATE_MODIFY_RESOURCE_TYPE = "admin/plugins/extend/modify_resource_type.html";
    private static final String TEMPLATE_CREATE_DEFAULT_RESOURCE_EXTENDER = "admin/plugins/extend/create_default_resource_extender.html";

	// CONSTANT
	private static final String CONSTANT_AND = "&";
	private static final String CONSTANT_AND_HTML = "%26";
    private static final String CONSTANT_NULL = "null";

    // VARIABLES
    private IResourceExtenderSearchFields _resourceExtenderSearchFields = new ResourceExtenderSearchFields(  );
    private IExtendableResourceTypeService _resourceTypeService = SpringContextService.getBean( ExtendableResourceTypeService.BEAN_SERVICE );
    private IResourceExtenderService _extenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
    private IResourceExtenderComponentManager _extenderComponentManager = SpringContextService.getBean( ResourceExtenderComponentManager.BEAN_MANAGER );
    private IExtendableResourceManager _resourceManager = SpringContextService.getBean( ExtendableResourceManager.BEAN_MANAGER );
    private IDefaultExtendableResourceService _defaultResourceService = SpringContextService.getBean( DefaultExtendableResourceService.BEAN_SERVICE );
    private IResourceExtenderHistoryService _resourceExtenderHistoryService = SpringContextService
            .getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
    private UrlItem _lastUrl;

    /**
     * Gets the manage resource extenders by resource type.
     *
     * @param request the request
     * @param response the response
     * @return the manage resource extenders by resource type
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult getManageResourceExtendersByResourceType( HttpServletRequest request,
        HttpServletResponse response ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE_PAGE_TITLE );

        // Set the last URL
        _lastUrl = getUrlManageResourceExtendersByResourceType( request, true );

        // first - see if there is an invoked action
        IPluginAction<IResourceExtenderSearchFields> action = getAction( request );

        if ( action != null )
        {
            AppLogService.debug( "Processing resource action " + action.getName(  ) );

            return action.process( request, response, getUser(  ), _resourceExtenderSearchFields );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        // This parameter is used to differentiate the page to manage the extender by type or by resource
        model.put( MARK_MANAGE_BY_RESOURCE, false );
        _resourceExtenderSearchFields.fillModel( getLastUrl( request ).getUrl(  ), request, model,
            ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE, getUser(  ) );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), model,
            IResourceExtenderPluginAction.class, MARK_RESOURCE_EXTENDER_ACTIONS );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), model,
            IResourceTypePluginAction.class, MARK_RESOURCE_TYPE_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE,
                request.getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Gets the manage resource extenders by resource.
     *
     * @param request the request
     * @param response the response
     * @return the manage resource extenders by resource
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult getManageResourceExtendersByResource( HttpServletRequest request,
        HttpServletResponse response ) throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_PAGE_TITLE );

        // Set the last URL
        _lastUrl = getUrlManageResourceExtendersByResource( request, true );

        // first - see if there is an invoked action
        IPluginAction<IResourceExtenderSearchFields> action = getAction( request );

        if ( action != null )
        {
            AppLogService.debug( "Processing resource action " + action.getName(  ) );

            return action.process( request, response, getUser(  ), _resourceExtenderSearchFields );
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        // This parameter is used to differentiate the page to manage the extender by type or by resource
        model.put( MARK_MANAGE_BY_RESOURCE, true );
        _resourceExtenderSearchFields.fillModel( getLastUrl( request ).getUrl(  ), request, model, getUser(  ) );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), model,
            IResourceExtenderPluginAction.class, MARK_RESOURCE_EXTENDER_ACTIONS );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), model,
            IResourceTypePluginAction.class, MARK_RESOURCE_TYPE_ACTIONS );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE,
                request.getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Gets the creates the resource type.
     *
     * @param request the request
     * @param response the response
     * @return the creates the resource type
     */
    public IPluginActionResult getCreateResourceType( HttpServletRequest request, HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_CREATE_RESOURCE_TYPE_PAGE_TITLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RESOURCE_TYPE, request.getLocale(  ) );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Gets the modify resource type.
     *
     * @param request the request
     * @param response the response
     * @return the modify resource type
     */
    public IPluginActionResult getModifyResourceType( HttpServletRequest request, HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_MODIFY_RESOURCE_TYPE_PAGE_TITLE );

        String strResourceType = request.getParameter( PARAMETER_RESOURCE_TYPE );

        String strHtml = StringUtils.EMPTY;

        if ( StringUtils.isNotBlank( strResourceType ) )
        {
            ExtendableResourceType resourceType = _resourceTypeService.findByPrimaryKey( strResourceType );

            if ( resourceType != null )
            {
                Map<String, Object> model = new HashMap<String, Object>(  );
                model.put( MARK_RESOURCE_TYPE, resourceType );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RESOURCE_TYPE,
                        request.getLocale(  ), model );
                strHtml = template.getHtml(  );
            }
        }

        IPluginActionResult result = new DefaultPluginActionResult(  );

        if ( StringUtils.isNotBlank( strHtml ) )
        {
            result.setHtmlContent( strHtml );
        }
        else
        {
            result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                    AdminMessage.TYPE_STOP ) );
        }

        return result;
    }

    /**
     * Gets the confirm remove resource type.
     *
     * @param request the request
     * @return the confirm remove resource type
     */
    public String getConfirmRemoveResourceType( HttpServletRequest request )
    {
        String strResourceType = request.getParameter( PARAMETER_RESOURCE_TYPE );

        if ( StringUtils.isBlank( strResourceType ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_RESOURCE_TYPE );
        url.addParameter( PARAMETER_RESOURCE_TYPE, strResourceType );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_RESOURCE_TYPE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the confirm remove resource extender.
     *
     * @param request the request
     * @return the confirm remove resource extender
     */
    public String getConfirmRemoveResourceExtender( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( StringUtils.isBlank( strIdExtender ) || !StringUtils.isNumeric( strIdExtender ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_REMOVE_RESOURCE_EXTENDER );
        url.addParameter( PARAMETER_ID_EXTENDER, strIdExtender );
		url.addParameter( PARAMETER_FROM_URL, StringUtils.replace( request.getParameter( PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_RESOURCE_EXTENDER, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the modify extender config.
     *
     * @param request the request
     * @param response the response
     * @return the modify extender config
     */
    public IPluginActionResult getModifyExtenderConfig( HttpServletRequest request, HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_EXTENDER_CONFIG_PAGE_TITLE );

        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( StringUtils.isNotBlank( strIdExtender ) && StringUtils.isNumeric( strIdExtender ) )
        {
            // Check permission
            if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender,
                        ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser(  ) ) )
            {
                IPluginActionResult result = new DefaultPluginActionResult(  );
                result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION,
                        AdminMessage.TYPE_STOP ) );

                return result;
            }

            int nIdExtender = Integer.parseInt( strIdExtender );
            ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );

            if ( resourceExtender != null )
            {
                IPluginActionResult result = new DefaultPluginActionResult(  );

                result.setHtmlContent( getAdminPage( _extenderComponentManager.getConfigHtml( resourceExtender,
                            getLocale(  ), request ) ) );

                return result;
            }
        }

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                AdminMessage.TYPE_STOP ) );

        return result;
    }

    public IPluginActionResult getModifyDefaultConfig( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_EXTENDER_CONFIG_PAGE_TITLE );
        String strExtenderType = request.getParameter( PARAMETER_EXTENDER_TYPE_DEFAULT_CONFIG );

        // Check permission
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null,
                ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser( ) ) )
        {
            IPluginActionResult result = new DefaultPluginActionResult( );
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }
        if ( StringUtils.isEmpty( strExtenderType ) )
        {
            IPluginActionResult result = new DefaultPluginActionResult( );

            result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }

        IPluginActionResult result = new DefaultPluginActionResult( );

        result.setHtmlContent( getAdminPage( _extenderComponentManager.getDefaultConfigHtml( strExtenderType,
                getLocale( ),
                request ) ) );

        return result;
    }

    /**
     * Gets the view extender info.
     *
     * @param request the request
     * @param response the response
     * @return the view extender info
     */
    public IPluginActionResult getViewExtenderInfo( HttpServletRequest request, HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_EXTENDER_INFO_PAGE_TITLE );

        ResourceExtenderDTO resourceExtender = null;
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        // Find by id extender
        if ( StringUtils.isNotBlank( strIdExtender ) && StringUtils.isNumeric( strIdExtender ) )
        {
            int nIdExtender = Integer.parseInt( strIdExtender );
            resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );
        }

        // Check permission
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender,
                    ExtendableResourceResourceIdService.PERMISSION_VIEW_INFO, getUser(  ) ) )
        {
            IPluginActionResult result = new DefaultPluginActionResult(  );
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }

        // Find by request
        if ( resourceExtender == null )
        {
            resourceExtender = new ResourceExtenderDTO(  );
            populate( resourceExtender, request );

            String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

            if ( StringUtils.isNotBlank( strJspError ) )
            {
                IPluginActionResult result = new DefaultPluginActionResult(  );

                result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                        AdminMessage.TYPE_STOP ) );

                return result;
            }
        }

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( _extenderComponentManager.getInfoHtml( resourceExtender, getLocale(  ),
                    request ) ) );

        return result;
    }

    /**
     * Gets the view extender history.
     *
     * @param request the request
     * @param response the response
     * @return the view extender history
     */
    public IPluginActionResult getViewExtenderHistory( HttpServletRequest request, HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_EXTENDER_HISTORY_PAGE_TITLE );

        ResourceExtenderDTO resourceExtender = null;
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        // Find by id extender
        if ( StringUtils.isNotBlank( strIdExtender ) && StringUtils.isNumeric( strIdExtender ) )
        {
            int nIdExtender = Integer.parseInt( strIdExtender );
            resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );
        }

        // Check permission
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender,
                    ExtendableResourceResourceIdService.PERMISSION_VIEW_HISTORY, getUser(  ) ) )
        {
            IPluginActionResult result = new DefaultPluginActionResult(  );
            result.setRedirect( AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION,
                    AdminMessage.TYPE_STOP ) );

            return result;
        }

        // Find by request
        if ( resourceExtender == null )
        {
            resourceExtender = new ResourceExtenderDTO(  );
            populate( resourceExtender, request );

            String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

            if ( StringUtils.isNotBlank( strJspError ) )
            {
                IPluginActionResult result = new DefaultPluginActionResult(  );

                result.setRedirect( AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS,
                        AdminMessage.TYPE_STOP ) );

                return result;
            }
        }

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( _extenderComponentManager.getHistoryHtml( resourceExtender, getLocale(  ),
                    request ) ) );

        return result;
    }

    /**
     * Gets the creates the default extender resource.
     *
     * @param request the request
     * @param response the response
     * @return the creates the default extender resource
     */
    public IPluginActionResult getCreateDefaultResourceExtender( HttpServletRequest request,
        HttpServletResponse response )
    {
        setPageTitleProperty( PROPERTY_CREATE_DEFAULT_RESOURCE_PAGE_TITLE );

        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO(  );

        // Populate the bean
        populate( resourceExtender, request );

        // Validate the form
        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            IPluginActionResult result = new DefaultPluginActionResult(  );

            result.setRedirect( strJspError );

            return result;
        }

        Map<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_RESOURCE_EXTENDER, resourceExtender );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_DEFAULT_RESOURCE_EXTENDER,
                request.getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    // DO

    /**
     * Do create resource type.
     *
     * @param request the request
     * @return the string
     */
    public String doCreateResourceType( HttpServletRequest request )
    {
        // Check permission
        if ( !RBACService.isAuthorized( ExtendableResourceType.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                    ResourceTypeResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP );
        }

        String strCancel = request.getParameter( PARAMETER_CANCEL );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return getLastUrl( request ).getUrl(  );
        }

        ExtendableResourceType resourceType = new ExtendableResourceType(  );

        // Populate the bean
        populate( resourceType, request );

        // Validate the form
        String strJspError = ExtendUtils.validateResourceType( request, resourceType );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        // Checks that the parameters are unique
        if ( _resourceTypeService.isDuplicate( resourceType.getKey(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DUPLICATE_RESOURCE_TYPE,
                AdminMessage.TYPE_STOP );
        }

        try
        {
            _resourceTypeService.create( resourceType );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when creating a resource type", ex );
            // Revert
            _resourceTypeService.remove( resourceType.getKey(  ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        return getLastUrl( request ).getUrl(  );
    }

    /**
     * Do create resource extender.
     *
     * @param request the request
     * @return the string
     */
    public String doCreateResourceExtender( HttpServletRequest request )
    {
        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO(  );

        // Populate the bean
        populate( resourceExtender, request );

        // Validate the form
        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        // Checks that the parameters are unique
        if ( _extenderService.isAuthorizedToAllResources( resourceExtender.getExtendableResourceType(  ),
                    resourceExtender.getExtenderType(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_TO_ALL_RESOURCES_ALREADY_EXISTS,
                AdminMessage.TYPE_INFO );
        }

        if ( _extenderService.isAuthorized( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ), resourceExtender.getExtenderType(  ) ) )
        {
            Object[] params = 
                {
                    resourceExtender.getIdExtendableResource(  ), resourceExtender.getExtendableResourceType(  ),
                };

            return AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_WITH_ID_RESOURCES_ALREADY_EXISTS,
                params, AdminMessage.TYPE_INFO );
        }

        // Check if it must use the default resource service or not
        // If so, then redirect the user to the creation resource extender page
        if ( _resourceManager.useDefaultExtendableResourceService( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ) ) )
        {
            return getUrlCreateDefaultResourceExcenter( request, resourceExtender ).getUrl(  );
        }

        strJspError = doCreateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        // If the extender needs a config, then redirect the user to the config modification page if he is authorized to access it
        boolean bAuthorizedUser = RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null,
                ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser( ) );
        if ( bAuthorizedUser )
        {
            IResourceExtender extender = _extenderService.getResourceExtender( resourceExtender.getExtenderType( ) );
            if ( ( extender != null ) && extender.isConfigRequired( ) )
            {
                return getUrlModifyResourceExtenderConfig( request, resourceExtender ).getUrl( );
            }
        }

        return getLastUrl( request ).getUrl(  );
    }

    /**
     * Do create default resource extender.
     *
     * @param request the request
     * @return the string
     */
    public String doCreateDefaultResourceExtender( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return getLastUrl( request ).getUrl(  );
        }

        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO(  );

        // Populate the bean
        populate( resourceExtender, request );

        // Validate the form
        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        if ( StringUtils.isBlank( resourceExtender.getName(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        DefaultExtendableResource resource = new DefaultExtendableResource( resourceExtender );

        try
        {
            _defaultResourceService.create( resource );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when creating a default resource extender", ex );
            // Revert
            _defaultResourceService.remove( resource.getIdExtendableResource(  ), resource.getExtendableResourceType(  ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        strJspError = doCreateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        // If the extender needs a config, then redirect the user to the config modification page if he is authorized to access it
        boolean bAuthorizedUser = RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null,
                ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser( ) );
        if ( bAuthorizedUser )
        {
            IResourceExtender extender = _extenderService.getResourceExtender( resourceExtender.getExtenderType( ) );
            if ( ( extender != null ) && extender.isConfigRequired( ) )
            {
                return getUrlModifyResourceExtenderConfig( request, resourceExtender ).getUrl( );
            }
        }

        return getLastUrl( request ).getUrl(  );
    }

    /**
     * Do modify resource type.
     *
     * @param request the request
     * @return the string
     */
    public String doModifyResourceType( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );

        if ( StringUtils.isNotBlank( strCancel ) )
        {
            return getLastUrl( request ).getUrl(  );
        }

        String strResourceType = request.getParameter( PARAMETER_RESOURCE_TYPE );

        if ( StringUtils.isNotBlank( strResourceType ) )
        {
            ExtendableResourceType resourceType = _resourceTypeService.findByPrimaryKey( strResourceType );

            if ( resourceType != null )
            {
                // Populate the bean
                populate( resourceType, request );

                // Validate the form
                String strJspError = ExtendUtils.validateResourceType( request, resourceType );

                if ( StringUtils.isNotBlank( strJspError ) )
                {
                    return strJspError;
                }

                // Checks that the parameters are unique
                if ( _resourceTypeService.isDuplicate( resourceType.getKey(  ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DUPLICATE_RESOURCE_TYPE,
                        AdminMessage.TYPE_STOP );
                }

                try
                {
                    _resourceTypeService.update( resourceType );
                }
                catch ( Exception ex )
                {
                    // Something wrong happened... a database check might be needed
                    AppLogService.error( ex.getMessage(  ) + " when updating a resource type", ex );

                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
                }

                return getLastUrl( request ).getUrl(  );
            }
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Do remove resource type.
     *
     * @param request the request
     * @return the string
     */
    public String doRemoveResourceType( HttpServletRequest request )
    {
        String strResourceType = request.getParameter( PARAMETER_RESOURCE_TYPE );

        if ( StringUtils.isBlank( strResourceType ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permission
        if ( !RBACService.isAuthorized( ExtendableResourceType.RESOURCE_TYPE, strResourceType,
                    ResourceTypeResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP );
        }

        try
        {
            _resourceTypeService.remove( strResourceType );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when deleting a resource type", ex );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        return getLastUrl( request ).getUrl(  );
    }

    /**
     * Do remove resource extender.
     *
     * @param request the request
     * @return the string
     */
    public String doRemoveResourceExtender( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( StringUtils.isBlank( strIdExtender ) || !StringUtils.isNumeric( strIdExtender ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permission
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender,
                    ExtendableResourceResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP );
        }

        int nIdExtender = Integer.parseInt( strIdExtender );
        ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );

        if ( resourceExtender != null )
        {
            try
            {
                _extenderService.remove( nIdExtender );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when deleting a resource extender", ex );

                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                    AdminMessage.TYPE_ERROR );
            }

            _extenderService.doDeleteResourceAddOn( resourceExtender );
            _resourceExtenderHistoryService.removeByResource( resourceExtender.getExtenderType( ),
                    resourceExtender.getIdExtendableResource( ), resourceExtender.getExtendableResourceType( ) );

            // Remove the default resource if there are no resource extender associated to the given id resource and resource type
            ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter(  );
            filter.setFilterIdExtendableResource( resourceExtender.getIdExtendableResource(  ) );
            filter.setFilterExtendableResourceType( resourceExtender.getExtendableResourceType(  ) );

            List<Integer> listIds = _extenderService.findIdsByFilter( filter );

            if ( ( listIds == null ) || listIds.isEmpty(  ) )
            {
                try
                {
                    _defaultResourceService.remove( resourceExtender.getIdExtendableResource(  ),
                        resourceExtender.getExtendableResourceType(  ) );
                }
                catch ( Exception ex )
                {
                    // Something wrong happened... a database check might be needed
                    AppLogService.error( ex.getMessage(  ) + " when deleting a default resource extender", ex );

                    return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE,
                        AdminMessage.TYPE_ERROR );
                }
            }
        }

        return getLastUrl( request ).getUrl(  );
    }

    /**
     * Do modify extender config.
     *
     * @param request the request
     * @return the string
     */
    public String doModifyExtenderConfig( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( StringUtils.isBlank( strIdExtender ) || !StringUtils.isNumeric( strIdExtender ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check permission
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender,
                    ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP );
        }

        int nIdExtender = Integer.parseInt( strIdExtender );
        ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );

        if ( resourceExtender != null )
        {
            String strCancel = request.getParameter( PARAMETER_CANCEL );

            if ( StringUtils.isBlank( strCancel ) )
            {
                try
                {
                    _extenderComponentManager.doSaveConfig( resourceExtender, request );
                }
                catch ( ExtendErrorException e )
                {
                    AppLogService.debug( e.getErrorMessage(  ), e );

                    Object[] params = { e.getErrorMessage(  ) };

                    return AdminMessageService.getMessageUrl( request, MESSAGE_STOP_GENERIC_MESSAGE, params,
                        AdminMessage.TYPE_STOP );
                }
            }

            return getLastUrl( request ).getUrl(  );
        }

        return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_STOP );
    }

    /**
     * Do modify default extender config.
     * 
     * @param request the request
     * @return The URL of the result page
     */
    public String doModifyDefaultConfig( HttpServletRequest request )
    {
        String strCancel = request.getParameter( PARAMETER_CANCEL );
        if ( StringUtils.isBlank( strCancel ) )
        {
            String strExtenderType = request.getParameter( PARAMETER_EXTENDER_TYPE );

            if ( StringUtils.isBlank( strExtenderType ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // Check permission
            if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null,
                    ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, getUser( ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP );
            }

            ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
            resourceExtender.setIdExtender( -1 );
            resourceExtender.setExtenderType( strExtenderType );

            try
            {
                _extenderComponentManager.doSaveConfig( resourceExtender, request );
            }
            catch ( ExtendErrorException e )
            {
                AppLogService.debug( e.getErrorMessage( ), e );

                Object[] params = { e.getErrorMessage( ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_STOP_GENERIC_MESSAGE, params,
                        AdminMessage.TYPE_STOP );
            }
        }

        return getLastUrl( request ).getUrl( );
    }

    // PRIVATE METHODS

    /**
     * Gets the url manage resource extenders by resource type.
     *
     * @param request the request
     * @param bSession the b session
     * @return the url manage resource extenders by resource type
     */
    private static UrlItem getUrlManageResourceExtendersByResourceType( HttpServletRequest request, boolean bSession )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) +
                JSP_URL_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE );

        if ( bSession )
        {
            url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );
        }

        return url;
    }

    /**
     * Gets the url manage resource extenders by resource.
     *
     * @param request the request
     * @param bSession the b session
     * @return the url manage resource extenders by resource
     */
    private static UrlItem getUrlManageResourceExtendersByResource( HttpServletRequest request, boolean bSession )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) +
                JSP_URL_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE );

        if ( bSession )
        {
            url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );
        }

        return url;
    }

    /**
     * Gets the url create default resource excenter.
     *
     * @param request the request
     * @param resourceExtender the resource extender
     * @return the url create default resource excenter
     */
    private static UrlItem getUrlCreateDefaultResourceExcenter( HttpServletRequest request,
        ResourceExtenderDTO resourceExtender )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_CREATE_DEFAULT_RESOURCE_EXTENDER );
        url.addParameter( PARAMETER_EXTENDER_TYPE, resourceExtender.getExtenderType(  ) );
        url.addParameter( PARAMETER_ID_EXTENDABLE_RESOURCE, resourceExtender.getIdExtendableResource(  ) );
        url.addParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE, resourceExtender.getExtendableResourceType(  ) );

        return url;
    }

    /**
     * Gets the url modify resource extender config.
     *
     * @param request the request
     * @param resourceExtender the resource extender
     * @return the url modify resource extender config
     */
    private static UrlItem getUrlModifyResourceExtenderConfig( HttpServletRequest request,
        ResourceExtenderDTO resourceExtender )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MODIFY_RESOURCE_EXTENDER_CONFIG );
        url.addParameter( PARAMETER_ID_EXTENDER, resourceExtender.getIdExtender(  ) );
		String strUrl = StringUtils.replace( request.getParameter( PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML );
		if ( StringUtils.isNotEmpty( strUrl ) )
		{
			url.addParameter( PARAMETER_FROM_URL, strUrl );
		}

        return url;
    }

    /**
     * Gets the action.
     *
     * @param request the request
     * @return the action
     */
    private static IPluginAction<IResourceExtenderSearchFields> getAction( HttpServletRequest request )
    {
        IPluginAction<IResourceExtenderSearchFields> action = PluginActionManager.getPluginAction( request,
                IResourceExtenderPluginAction.class );

        if ( action != null )
        {
            return action;
        }

        return PluginActionManager.getPluginAction( request, IResourceTypePluginAction.class );
    }

    /**
     * Gets the last url.
     *
     * @param request the request
     * @return the last url
     */
    private UrlItem getLastUrl( HttpServletRequest request )
    {
		String strUrl = StringUtils.replace( request.getParameter( PARAMETER_FROM_URL ), CONSTANT_AND_HTML, CONSTANT_AND );
        if ( StringUtils.isNotEmpty( strUrl ) && !StringUtils.equalsIgnoreCase( strUrl, CONSTANT_NULL ) )
		{
			return new UrlItem( strUrl );
		}
        // if parameter search is not blank => force search
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_SEARCH ) ) )
        {
            _lastUrl = getUrlManageResourceExtendersByResource( request, false );
            _lastUrl.addParameter( PARAMETER_FILTER_EXTENDER_TYPE,
                request.getParameter( PARAMETER_FILTER_EXTENDER_TYPE ) );
            _lastUrl.addParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE,
                request.getParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE ) );
            _lastUrl.addParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE,
                request.getParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE ) );
        }

        if ( _lastUrl != null )
        {
            return _lastUrl;
        }

        return getUrlManageResourceExtendersByResource( request, true );
    }

    /**
     * Do create resource extender.
     *
     * @param request the request
     * @param resourceExtender the resource extender
     * @return the string
     */
    private String doCreateResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        // If the resource type is not found, then create it
        ExtendableResourceType resourceType = _resourceTypeService.findByPrimaryKey( resourceExtender.getExtendableResourceType(  ) );

        if ( resourceType == null )
        {
            resourceType = new ExtendableResourceType(  );
            resourceType.setKey( resourceExtender.getExtendableResourceType(  ) );
			resourceType.setDescription( resourceExtender.getExtendableResourceType( ) );
            _resourceTypeService.create( resourceType );
        }

        try
        {
            _extenderService.create( resourceExtender );
        }
        catch ( Exception ex )
        {
            // Something wrong happened... a database check might be needed
            AppLogService.error( ex.getMessage(  ) + " when creating a resource extender", ex );
            // Revert
            _extenderService.remove( resourceExtender.getIdExtender(  ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        _extenderService.doCreateResourceAddOn( resourceExtender );

        return StringUtils.EMPTY;
    }
}
