/*
 * Copyright (c) 2002-2021, City of Paris
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

import fr.paris.lutece.api.user.User;
import org.apache.commons.lang3.Strings;
import fr.paris.lutece.plugins.extend.business.DefaultExtendableResource;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.ExtendableResourceResourceIdService;
import fr.paris.lutece.plugins.extend.service.IDefaultExtendableResourceService;
import fr.paris.lutece.plugins.extend.service.IExtendableResourceManager;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.util.ExtendUtils;
import fr.paris.lutece.plugins.extend.web.action.IResourceExtenderPluginAction;
import fr.paris.lutece.plugins.extend.web.action.IResourceExtenderSearchFields;
import fr.paris.lutece.plugins.extend.web.action.ResourceExtenderSearchFields;
import fr.paris.lutece.plugins.extend.web.component.IResourceExtenderComponentManager;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.IPluginAction;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.PluginActionManager;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Back office controller of the resource extenders.
 */
@SessionScoped
@Named
@Controller( controllerJsp = ResourceExtenderJspBean.JSP_MANAGE_RESOURCE_EXTENDERS, controllerPath = ResourceExtenderJspBean.CONTROLLER_PATH, right = ResourceExtenderJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER, securityTokenEnabled = true )
public class ResourceExtenderJspBean extends MVCAdminJspBean
{
    /** The right to manage the resource extenders. */
    public static final String RIGHT_MANAGE_RESOURCE_EXTENDER = "RESOURCE_EXTENDER_MANAGEMENT";

    /** The JSP of this controller. */
    public static final String JSP_MANAGE_RESOURCE_EXTENDERS = "ManageResourceExtendersByResource.jsp";

    /** The path of the JSP of this controller. */
    public static final String CONTROLLER_PATH = "jsp/admin/plugins/extend/";

    /** The view listing the extenders by resource. */
    public static final String VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE = "manageResourceExtendersByResource";

    /** The view listing the extenders by resource type. */
    public static final String VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE = "manageResourceExtendersByResourceType";

    /** The view and action creating an extender. */
    public static final String VIEW_CREATE_RESOURCE_EXTENDER = "createResourceExtender";

    /** The view confirming the creation of an extender requested from a resource. */
    public static final String VIEW_CONFIRM_CREATE_RESOURCE_EXTENDER = "confirmCreateResourceExtender";

    /** The view and action creating an extender on a default resource. */
    public static final String VIEW_CREATE_DEFAULT_RESOURCE_EXTENDER = "createDefaultResourceExtender";

    /** The view confirming the removal of an extender. */
    public static final String VIEW_CONFIRM_REMOVE_RESOURCE_EXTENDER = "confirmRemoveResourceExtender";

    /** The view and action modifying the configuration of an extender. */
    public static final String VIEW_MODIFY_EXTENDER_CONFIG = "modifyExtenderConfig";

    /** The view and action modifying the default configuration of an extender type. */
    public static final String VIEW_MODIFY_DEFAULT_CONFIG = "modifyDefaultConfig";

    /** The view confirming the deactivation of an extender. */
    public static final String VIEW_CONFIRM_DISABLE_EXTENDER = "confirmDisableExtender";

    /** The view confirming the activation of an extender. */
    public static final String VIEW_CONFIRM_ENABLE_EXTENDER = "confirmEnableExtender";

    /** The view showing the information of an extender. */
    public static final String VIEW_EXTENDER_INFO = "viewExtenderInfo";

    /** The view showing the history of an extender. */
    public static final String VIEW_EXTENDER_HISTORY = "viewExtenderHistory";

    /** The action creating an extender. */
    public static final String ACTION_CREATE_RESOURCE_EXTENDER = VIEW_CREATE_RESOURCE_EXTENDER;

    /** The action creating an extender on a default resource. */
    public static final String ACTION_CREATE_DEFAULT_RESOURCE_EXTENDER = VIEW_CREATE_DEFAULT_RESOURCE_EXTENDER;

    /** The action removing an extender. */
    public static final String ACTION_REMOVE_RESOURCE_EXTENDER = "removeResourceExtender";

    /** The action modifying the configuration of an extender. */
    public static final String ACTION_MODIFY_EXTENDER_CONFIG = VIEW_MODIFY_EXTENDER_CONFIG;

    /** The action modifying the default configuration of an extender type. */
    public static final String ACTION_MODIFY_DEFAULT_CONFIG = VIEW_MODIFY_DEFAULT_CONFIG;

    /** The action disabling an extender. */
    public static final String ACTION_DISABLE_EXTENDER = "disableExtender";

    /** The action enabling an extender. */
    public static final String ACTION_ENABLE_EXTENDER = "enableExtender";

    private static final long serialVersionUID = -4886842745353434350L;

    private static final String PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE_PAGE_TITLE = "extend.manage_resource_extenders_by_resource_type.pageTitle";
    private static final String PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_PAGE_TITLE = "extend.manage_resource_extenders_by_resource.pageTitle";
    private static final String PROPERTY_EXTENDER_CONFIG_PAGE_TITLE = "extend.resource_extender_config.pageTitle";
    private static final String PROPERTY_EXTENDER_INFO_PAGE_TITLE = "extend.resource_extender_info.pageTitle";
    private static final String PROPERTY_EXTENDER_HISTORY_PAGE_TITLE = "extend.resource_extender_history.pageTitle";
    private static final String PROPERTY_CREATE_DEFAULT_RESOURCE_PAGE_TITLE = "extend.create_default_resource_extender.pageTitle";
    private static final String PROPERTY_CREATE_RESOURCE_EXTENDER_PAGE_TITLE = "extend.create_resource_extender.labelCreateResourceExtender";

    private static final String MESSAGE_ERROR_GENERIC_MESSAGE = "extend.message.error.genericMessage";
    private static final String MESSAGE_CONFIRM_CREATE_RESOURCE_EXTENDER = "extend.message.confirm.createResourceExtender";
    private static final String MESSAGE_CONFIRM_REMOVE_RESOURCE_EXTENDER = "extend.message.confirm.removeResourceExtender";
    private static final String MESSAGE_CONFIRM_DISABLE_RESOURCE_EXTENDER = "extend.message.confirm.disableResourceExtender";
    private static final String MESSAGE_CONFIRM_ENABLE_RESOURCE_EXTENDER = "extend.message.confirm.enableResourceExtender";
    private static final String MESSAGE_STOP_GENERIC_MESSAGE = "extend.message.stop.genericMessage";
    private static final String MESSAGE_EXTENDER_TO_ALL_RESOURCES_ALREADY_EXISTS = "extend.message.extenderToAllResourcesAlreadyExists";
    private static final String MESSAGE_EXTENDER_WITH_ID_RESOURCES_ALREADY_EXISTS = "extend.message.extenderWithIdAlreadyExists";
    private static final String MESSAGE_UNAUTHORIZED_ACTION = "extend.message.unauthorizedAction";
    private static final String MESSAGE_EXTENDER_NOT_FOUND = "extend.message.extenderNotFound";

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

    private static final String MARK_RESOURCE_EXTENDER_ACTIONS = "resourceExtenderActions";
    private static final String MARK_MANAGE_BY_RESOURCE = "manageByResource";
    private static final String MARK_RESOURCE_EXTENDER = "resourceExtender";
    private static final String MARK_RESOURCE_TYPES = "resourceTypes";
    private static final String MARK_EXTENDER_TYPES = "extenderTypes";
    private static final String MARK_FROM_URL = "from_url";
    private static final String MARK_EXTENDER_CONTENT = "resourceExtenderContent";

    private static final String TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE = "admin/plugins/extend/manage_resource_extenders_by_resource_type.html";
    private static final String TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE = "admin/plugins/extend/manage_resource_extenders_by_resource.html";
    private static final String TEMPLATE_CREATE_DEFAULT_RESOURCE_EXTENDER = "admin/plugins/extend/create_default_resource_extender.html";
    private static final String TEMPLATE_CREATE_RESOURCE_EXTENDER = "admin/plugins/extend/create_resource_extender.html";
    private static final String TEMPLATE_RESOURCE_EXTENDER_CONTENT = "admin/plugins/extend/resource_extender_content.html";

    private static final String CONSTANT_AND = "&";
    private static final String CONSTANT_AND_HTML = "%26";
    private static final String CONSTANT_NULL = "null";
    private static final Pattern PATTERN_ABSOLUTE_URL = Pattern.compile( "^(\\s*[a-zA-Z][a-zA-Z0-9+.-]*:|\\s*[/\\\\]{2})" );

    private IResourceExtenderSearchFields _resourceExtenderSearchFields = new ResourceExtenderSearchFields( );

    @Inject
    private IExtendableResourceTypeService _resourceTypeService;

    @Inject
    private IResourceExtenderService _extenderService;

    @Inject
    private IResourceExtenderComponentManager _extenderComponentManager;

    @Inject
    private IExtendableResourceManager _resourceManager;

    @Inject
    private IDefaultExtendableResourceService _defaultResourceService;

    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    private UrlItem _lastUrl;

    private transient HttpServletResponse _response;

    /**
     * {@inheritDoc}
     */
    @Override
    public String processController( HttpServletRequest request, HttpServletResponse response ) throws AccessDeniedException
    {
        _response = response;

        return super.processController( request, response );
    }

    /**
     * Displays the extenders by resource, or runs the plugin action the request invokes.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     * @throws AccessDeniedException
     *             if the plugin action refuses the user
     */
    @View( value = VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE, defaultView = true )
    public String getManageResourceExtendersByResource( Models model, HttpServletRequest request ) throws AccessDeniedException
    {
        _lastUrl = getManageUrl( request, VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE, true );

        IPluginAction<IResourceExtenderSearchFields> action = PluginActionManager.getPluginAction( request, IResourceExtenderPluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing resource action {}", action.getName( ) );

            return getPluginActionContent( request, action.process( request, _response, getUser( ), _resourceExtenderSearchFields ) );
        }

        Map<String, Object> mapModel = new HashMap<>( );
        _resourceExtenderSearchFields.fillModel( getLastUrl( request ).getUrl( ), request, mapModel, getUser( ) );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), mapModel, IResourceExtenderPluginAction.class,
                MARK_RESOURCE_EXTENDER_ACTIONS );
        mapModel.forEach( model::put );

        return getPage( PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_PAGE_TITLE, TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE );
    }

    /**
     * Displays the extenders by resource type, or runs the plugin action the request invokes.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     * @throws AccessDeniedException
     *             if the plugin action refuses the user
     */
    @View( VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE )
    public String getManageResourceExtendersByResourceType( Models model, HttpServletRequest request ) throws AccessDeniedException
    {
        _lastUrl = getManageUrl( request, VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE, true );

        IPluginAction<IResourceExtenderSearchFields> action = PluginActionManager.getPluginAction( request, IResourceExtenderPluginAction.class );

        if ( action != null )
        {
            AppLogService.debug( "Processing resource action {}", action.getName( ) );

            return getPluginActionContent( request, action.process( request, _response, getUser( ), _resourceExtenderSearchFields ) );
        }

        Map<String, Object> mapModel = new HashMap<>( );
        _resourceExtenderSearchFields.fillModel( getLastUrl( request ).getUrl( ), request, mapModel, ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE,
                getUser( ) );
        PluginActionManager.fillModel( request, AdminUserService.getAdminUser( request ), mapModel, IResourceExtenderPluginAction.class,
                MARK_RESOURCE_EXTENDER_ACTIONS );
        mapModel.forEach( model::put );

        return getPage( PROPERTY_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE_PAGE_TITLE, TEMPLATE_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE_TYPE );
    }

    /**
     * Displays the form creating an extender.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_CREATE_RESOURCE_EXTENDER )
    public String getCreateResourceExtender( Models model, HttpServletRequest request )
    {
        model.put( MARK_RESOURCE_TYPES, _resourceTypeService.findAllAsRef( AdminUserService.getLocale( request ) ) );
        model.put( MARK_EXTENDER_TYPES, _extenderService.getExtenderTypes( request.getLocale( ) ) );
        model.put( MARK_MANAGE_BY_RESOURCE, Boolean.parseBoolean( request.getParameter( MARK_MANAGE_BY_RESOURCE ) ) );
        model.put( MARK_FROM_URL, request.getParameter( PARAMETER_FROM_URL ) );

        return getPage( PROPERTY_CREATE_RESOURCE_EXTENDER_PAGE_TITLE, TEMPLATE_CREATE_RESOURCE_EXTENDER );
    }

    /**
     * Asks the user to confirm the creation of an extender requested from a resource.
     *
     * @param request
     *            the request
     * @return the confirmation message
     */
    @View( value = VIEW_CONFIRM_CREATE_RESOURCE_EXTENDER, securityTokenAction = ACTION_CREATE_RESOURCE_EXTENDER )
    public String getConfirmCreateResourceExtender( HttpServletRequest request )
    {
        String strExtenderType = request.getParameter( PARAMETER_EXTENDER_TYPE );
        String strIdExtendableResource = request.getParameter( PARAMETER_ID_EXTENDABLE_RESOURCE );
        String strExtendableResourceType = request.getParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE );

        if ( StringUtils.isAnyBlank( strExtenderType, strIdExtendableResource, strExtendableResourceType ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_CREATE_RESOURCE_EXTENDER ) );
        url.addParameter( PARAMETER_EXTENDER_TYPE, strExtenderType );
        url.addParameter( PARAMETER_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        url.addParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        url.addParameter( PARAMETER_FILTER_EXTENDER_TYPE, strExtenderType );
        url.addParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE, strIdExtendableResource );
        url.addParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE, strExtendableResourceType );
        url.addParameter( PARAMETER_SEARCH, PARAMETER_SEARCH );

        String strFromUrl = request.getParameter( PARAMETER_FROM_URL );

        if ( StringUtils.isNotBlank( strFromUrl ) )
        {
            url.addParameter( PARAMETER_FROM_URL, Strings.CS.replace( strFromUrl, CONSTANT_AND, CONSTANT_AND_HTML ) );
        }

        Object [ ] messageArgs = {
                ExtendUtils.getExtenderTypeLabel( strExtenderType, getLocale( ) ), strIdExtendableResource,
                ExtendUtils.getResourceTypeLabel( strExtendableResourceType, getLocale( ) )
        };

        return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CREATE_RESOURCE_EXTENDER, messageArgs, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Creates an extender.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_CREATE_RESOURCE_EXTENDER )
    public String doCreateResourceExtender( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            return redirect( request, getLastUrl( request ).getUrl( ) );
        }

        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
        populate( resourceExtender, request );

        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return redirect( request, strJspError );
        }

        if ( _extenderService.isAuthorizedToAllResources( resourceExtender.getExtendableResourceType( ), resourceExtender.getExtenderType( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_TO_ALL_RESOURCES_ALREADY_EXISTS, AdminMessage.TYPE_INFO ) );
        }

        if ( _extenderService.isAuthorized( resourceExtender.getIdExtendableResource( ), resourceExtender.getExtendableResourceType( ),
                resourceExtender.getExtenderType( ) ) )
        {
            Object [ ] params = {
                    resourceExtender.getIdExtendableResource( ),
                    ExtendUtils.getResourceTypeLabel( resourceExtender.getExtendableResourceType( ), getLocale( ) ),
            };

            return redirect( request,
                    AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_WITH_ID_RESOURCES_ALREADY_EXISTS, params, AdminMessage.TYPE_INFO ) );
        }

        if ( _resourceManager.useDefaultExtendableResourceService( resourceExtender.getIdExtendableResource( ),
                resourceExtender.getExtendableResourceType( ) ) )
        {
            return redirect( request, getUrlCreateDefaultResourceExtender( request, resourceExtender ).getUrl( ) );
        }

        return redirect( request, createResourceExtender( request, resourceExtender ) );
    }

    /**
     * Displays the form naming the default resource of a new extender.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_CREATE_DEFAULT_RESOURCE_EXTENDER )
    public String getCreateDefaultResourceExtender( Models model, HttpServletRequest request )
    {
        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
        populate( resourceExtender, request );

        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return redirect( request, strJspError );
        }

        ExtendUtils.fillTypeLabels( model::put, List.of( resourceExtender ), getLocale( ) );
        model.put( MARK_RESOURCE_EXTENDER, resourceExtender );
        model.put( MARK_FROM_URL, request.getParameter( PARAMETER_FROM_URL ) );

        return getPage( PROPERTY_CREATE_DEFAULT_RESOURCE_PAGE_TITLE, TEMPLATE_CREATE_DEFAULT_RESOURCE_EXTENDER );
    }

    /**
     * Creates a default resource and its extender.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_CREATE_DEFAULT_RESOURCE_EXTENDER )
    public String doCreateDefaultResourceExtender( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            return redirect( request, getLastUrl( request ).getUrl( ) );
        }

        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
        populate( resourceExtender, request );

        String strJspError = ExtendUtils.validateResourceExtender( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return redirect( request, strJspError );
        }

        if ( StringUtils.isBlank( resourceExtender.getName( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        DefaultExtendableResource resource = new DefaultExtendableResource( resourceExtender );

        try
        {
            _defaultResourceService.create( resource );
        }
        catch( Exception ex )
        {
            AppLogService.error( "{} when creating a default resource extender", ex.getMessage( ), ex );
            _defaultResourceService.remove( resource.getIdExtendableResource( ), resource.getExtendableResourceType( ) );

            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR ) );
        }

        return redirect( request, createResourceExtender( request, resourceExtender ) );
    }

    /**
     * Asks the user to confirm the removal of an extender.
     *
     * @param request
     *            the request
     * @return the confirmation message
     */
    @View( value = VIEW_CONFIRM_REMOVE_RESOURCE_EXTENDER, securityTokenAction = ACTION_REMOVE_RESOURCE_EXTENDER )
    public String getConfirmRemoveResourceExtender( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( !StringUtils.isNumeric( strIdExtender ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_RESOURCE_EXTENDER ) );
        url.addParameter( PARAMETER_ID_EXTENDER, strIdExtender );
        url.addParameter( PARAMETER_FROM_URL, Strings.CS.replace( request.getParameter( PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML ) );

        return redirect( request,
                AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_RESOURCE_EXTENDER, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Removes an extender, its add-on and its history, then the default resource it leaves alone.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_REMOVE_RESOURCE_EXTENDER )
    public String doRemoveResourceExtender( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( !StringUtils.isNumeric( strIdExtender ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender, ExtendableResourceResourceIdService.PERMISSION_DELETE, (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );
        }

        int nIdExtender = Integer.parseInt( strIdExtender );
        ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( nIdExtender );

        if ( resourceExtender != null )
        {
            try
            {
                _extenderService.remove( nIdExtender );
            }
            catch( Exception ex )
            {
                AppLogService.error( "{} when deleting a resource extender", ex.getMessage( ), ex );

                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR ) );
            }

            _extenderService.doDeleteResourceAddOn( resourceExtender );
            _resourceExtenderHistoryService.removeByResource( resourceExtender.getExtenderType( ), resourceExtender.getIdExtendableResource( ),
                    resourceExtender.getExtendableResourceType( ) );

            ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( );
            filter.setFilterIdExtendableResource( resourceExtender.getIdExtendableResource( ) );
            filter.setFilterExtendableResourceType( resourceExtender.getExtendableResourceType( ) );

            List<Integer> listIds = _extenderService.findIdsByFilter( filter );

            if ( ( listIds == null ) || listIds.isEmpty( ) )
            {
                try
                {
                    _defaultResourceService.remove( resourceExtender.getIdExtendableResource( ), resourceExtender.getExtendableResourceType( ) );
                }
                catch( Exception ex )
                {
                    AppLogService.error( "{} when deleting a default resource extender", ex.getMessage( ), ex );

                    return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR ) );
                }
            }
        }

        return redirect( request, getLastUrl( request ).getUrl( ) );
    }

    /**
     * Asks the user to confirm the deactivation of an extender.
     *
     * @param request
     *            the request
     * @return the confirmation message
     */
    @View( value = VIEW_CONFIRM_DISABLE_EXTENDER, securityTokenAction = ACTION_DISABLE_EXTENDER )
    public String getConfirmDisableExtender( HttpServletRequest request )
    {
        return getConfirmExtenderState( request, ACTION_DISABLE_EXTENDER, MESSAGE_CONFIRM_DISABLE_RESOURCE_EXTENDER );
    }

    /**
     * Disables an extender.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_DISABLE_EXTENDER )
    public String doDisableExtender( HttpServletRequest request )
    {
        return doChangeExtenderState( request, false );
    }

    /**
     * Asks the user to confirm the activation of an extender.
     *
     * @param request
     *            the request
     * @return the confirmation message
     */
    @View( value = VIEW_CONFIRM_ENABLE_EXTENDER, securityTokenAction = ACTION_ENABLE_EXTENDER )
    public String getConfirmEnableExtender( HttpServletRequest request )
    {
        return getConfirmExtenderState( request, ACTION_ENABLE_EXTENDER, MESSAGE_CONFIRM_ENABLE_RESOURCE_EXTENDER );
    }

    /**
     * Enables an extender.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_ENABLE_EXTENDER )
    public String doEnableExtender( HttpServletRequest request )
    {
        return doChangeExtenderState( request, true );
    }

    /**
     * Displays the configuration form of an extender.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_MODIFY_EXTENDER_CONFIG )
    public String getModifyExtenderConfig( Models model, HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( !StringUtils.isNumeric( strIdExtender ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender, ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );
        }

        ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( Integer.parseInt( strIdExtender ) );

        if ( resourceExtender == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        return getContentPage( model, PROPERTY_EXTENDER_CONFIG_PAGE_TITLE, _extenderComponentManager.getConfigHtml( resourceExtender, getLocale( ), request ) );
    }

    /**
     * Saves the configuration of an extender.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_MODIFY_EXTENDER_CONFIG )
    public String doModifyExtenderConfig( HttpServletRequest request )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( !StringUtils.isNumeric( strIdExtender ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender, ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );
        }

        ResourceExtenderDTO resourceExtender = _extenderService.findByPrimaryKey( Integer.parseInt( strIdExtender ) );

        if ( resourceExtender == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        if ( StringUtils.isBlank( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            String strError = saveConfig( request, resourceExtender );

            if ( strError != null )
            {
                return redirect( request, strError );
            }
        }

        return redirect( request, getLastUrl( request ).getUrl( ) );
    }

    /**
     * Displays the default configuration form of an extender type.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_MODIFY_DEFAULT_CONFIG )
    public String getModifyDefaultConfig( Models model, HttpServletRequest request )
    {
        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null, ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                (User) getUser( ) ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );
        }

        String strExtenderType = request.getParameter( PARAMETER_EXTENDER_TYPE_DEFAULT_CONFIG );

        if ( StringUtils.isEmpty( strExtenderType ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        return getContentPage( model, PROPERTY_EXTENDER_CONFIG_PAGE_TITLE,
                _extenderComponentManager.getDefaultConfigHtml( strExtenderType, getLocale( ), request ) );
    }

    /**
     * Saves the default configuration of an extender type.
     *
     * @param request
     *            the request
     * @return the next page
     */
    @Action( ACTION_MODIFY_DEFAULT_CONFIG )
    public String doModifyDefaultConfig( HttpServletRequest request )
    {
        if ( StringUtils.isBlank( request.getParameter( PARAMETER_CANCEL ) ) )
        {
            String strExtenderType = request.getParameter( PARAMETER_EXTENDER_TYPE );

            if ( StringUtils.isBlank( strExtenderType ) )
            {
                return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
            }

            if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null, ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                    (User) getUser( ) ) )
            {
                return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );
            }

            ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO( );
            resourceExtender.setIdExtender( -1 );
            resourceExtender.setExtenderType( strExtenderType );

            String strError = saveConfig( request, resourceExtender );

            if ( strError != null )
            {
                return redirect( request, strError );
            }
        }

        return redirect( request, getLastUrl( request ).getUrl( ) );
    }

    /**
     * Displays the information of an extender.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_EXTENDER_INFO )
    public String getViewExtenderInfo( Models model, HttpServletRequest request )
    {
        ResourceExtenderDTO resourceExtender = findResourceExtender( request, ExtendableResourceResourceIdService.PERMISSION_VIEW_INFO );

        if ( resourceExtender == null )
        {
            return null;
        }

        return getContentPage( model, PROPERTY_EXTENDER_INFO_PAGE_TITLE, _extenderComponentManager.getInfoHtml( resourceExtender, getLocale( ), request ) );
    }

    /**
     * Displays the history of an extender.
     *
     * @param model
     *            the model
     * @param request
     *            the request
     * @return the page
     */
    @View( VIEW_EXTENDER_HISTORY )
    public String getViewExtenderHistory( Models model, HttpServletRequest request )
    {
        ResourceExtenderDTO resourceExtender = findResourceExtender( request, ExtendableResourceResourceIdService.PERMISSION_VIEW_HISTORY );

        if ( resourceExtender == null )
        {
            return null;
        }

        return getContentPage( model, PROPERTY_EXTENDER_HISTORY_PAGE_TITLE,
                _extenderComponentManager.getHistoryHtml( resourceExtender, getLocale( ), request ) );
    }

    /**
     * Renders a page whose body another component built, so the forms it holds carry the security token.
     *
     * @param model
     *            the model
     * @param strPageTitleProperty
     *            the page title property
     * @param strContent
     *            the body of the page
     * @return the page
     */
    private String getContentPage( Models model, String strPageTitleProperty, String strContent )
    {
        model.put( MARK_EXTENDER_CONTENT, strContent );

        return getPage( strPageTitleProperty, TEMPLATE_RESOURCE_EXTENDER_CONTENT );
    }

    /**
     * Turns the result of a plugin action into the page to display or a redirection.
     *
     * @param request
     *            the request
     * @param result
     *            the result of the plugin action
     * @return the page, or null after a redirection
     */
    private String getPluginActionContent( HttpServletRequest request, IPluginActionResult result )
    {
        if ( StringUtils.isNotBlank( result.getRedirect( ) ) )
        {
            return redirect( request, result.getRedirect( ) );
        }

        return result.getHtmlContent( );
    }

    /**
     * Finds the extender a view displays, by its id or from the request, once the user is authorized; redirects otherwise.
     *
     * @param request
     *            the request
     * @param strPermission
     *            the permission the view requires
     * @return the extender, or null after a redirection
     */
    private ResourceExtenderDTO findResourceExtender( HttpServletRequest request, String strPermission )
    {
        ResourceExtenderDTO resourceExtender = null;
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( StringUtils.isNotBlank( strIdExtender ) && StringUtils.isNumeric( strIdExtender ) )
        {
            resourceExtender = _extenderService.findByPrimaryKey( Integer.parseInt( strIdExtender ) );

            if ( resourceExtender == null )
            {
                redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_NOT_FOUND, AdminMessage.TYPE_STOP ) );

                return null;
            }
        }

        if ( !RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, strIdExtender, strPermission, (User) getUser( ) ) )
        {
            redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_UNAUTHORIZED_ACTION, AdminMessage.TYPE_STOP ) );

            return null;
        }

        if ( resourceExtender == null )
        {
            resourceExtender = new ResourceExtenderDTO( );
            populate( resourceExtender, request );

            if ( StringUtils.isNotBlank( ExtendUtils.validateResourceExtender( request, resourceExtender ) ) )
            {
                redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );

                return null;
            }
        }

        return resourceExtender;
    }

    /**
     * Asks the user to confirm a change of state of an extender.
     *
     * @param request
     *            the request
     * @param strAction
     *            the action changing the state
     * @param strMessageKey
     *            the confirmation message key
     * @return the confirmation message
     */
    private String getConfirmExtenderState( HttpServletRequest request, String strAction, String strMessageKey )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );

        if ( !StringUtils.isNumeric( strIdExtender ) )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP ) );
        }

        UrlItem url = new UrlItem( getActionUrl( strAction ) );
        url.addParameter( PARAMETER_ID_EXTENDER, strIdExtender );

        return redirect( request, AdminMessageService.getMessageUrl( request, strMessageKey, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION ) );
    }

    /**
     * Enables or disables an extender.
     *
     * @param request
     *            the request
     * @param bActive
     *            the new state
     * @return the next page
     */
    private String doChangeExtenderState( HttpServletRequest request, boolean bActive )
    {
        String strIdExtender = request.getParameter( PARAMETER_ID_EXTENDER );
        ResourceExtenderDTO resourceExtender = StringUtils.isNumeric( strIdExtender ) ? _extenderService.findByPrimaryKey( Integer.parseInt( strIdExtender ) )
                : null;

        if ( resourceExtender == null )
        {
            return redirect( request, AdminMessageService.getMessageUrl( request, MESSAGE_EXTENDER_NOT_FOUND, AdminMessage.TYPE_STOP ) );
        }

        resourceExtender.setIsActive( bActive );
        _extenderService.update( resourceExtender );

        return redirect( request, getLastUrl( request ).getUrl( ) );
    }

    /**
     * Saves the configuration of an extender.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the extender
     * @return the url of the error message, or null when the configuration is saved
     */
    private String saveConfig( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        try
        {
            _extenderComponentManager.doSaveConfig( resourceExtender, request );
        }
        catch( ExtendErrorException e )
        {
            AppLogService.debug( e.getErrorMessage( ), e );

            Object [ ] params = {
                    e.getErrorMessage( )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_STOP_GENERIC_MESSAGE, params, AdminMessage.TYPE_STOP );
        }

        return null;
    }

    /**
     * Gets the url of a list view of this controller.
     *
     * @param request
     *            the request
     * @param strView
     *            the list view
     * @param bSession
     *            true to keep the filter of the session
     * @return the url
     */
    private UrlItem getManageUrl( HttpServletRequest request, String strView, boolean bSession )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + getViewFullUrl( strView ) );

        if ( bSession )
        {
            url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );
        }

        return url;
    }

    /**
     * Gets the url of the view creating an extender on a default resource.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the resource extender
     * @return the url
     */
    private UrlItem getUrlCreateDefaultResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + getViewFullUrl( VIEW_CREATE_DEFAULT_RESOURCE_EXTENDER ) );
        url.addParameter( PARAMETER_EXTENDER_TYPE, resourceExtender.getExtenderType( ) );
        url.addParameter( PARAMETER_ID_EXTENDABLE_RESOURCE, resourceExtender.getIdExtendableResource( ) );
        url.addParameter( PARAMETER_EXTENDABLE_RESOURCE_TYPE, resourceExtender.getExtendableResourceType( ) );
        addFromUrl( request, url );

        return url;
    }

    /**
     * Gets the url of the view modifying the configuration of an extender.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the resource extender
     * @return the url
     */
    private UrlItem getUrlModifyResourceExtenderConfig( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + getViewFullUrl( VIEW_MODIFY_EXTENDER_CONFIG ) );
        url.addParameter( PARAMETER_ID_EXTENDER, resourceExtender.getIdExtender( ) );
        addFromUrl( request, url );

        return url;
    }

    /**
     * Carries the page to come back to into an url.
     *
     * @param request
     *            the request
     * @param url
     *            the url
     */
    private static void addFromUrl( HttpServletRequest request, UrlItem url )
    {
        String strUrl = Strings.CS.replace( request.getParameter( PARAMETER_FROM_URL ), CONSTANT_AND, CONSTANT_AND_HTML );

        if ( StringUtils.isNotEmpty( strUrl ) )
        {
            url.addParameter( PARAMETER_FROM_URL, strUrl );
        }
    }

    /**
     * Gets the page to come back to the request names, when it belongs to this site.
     *
     * @param request
     *            the request
     * @return the url, or null when the request names none or a page of another site
     */
    private static String getFromUrl( HttpServletRequest request )
    {
        String strUrl = Strings.CS.replace( StringEscapeUtils.unescapeHtml4( request.getParameter( PARAMETER_FROM_URL ) ), CONSTANT_AND_HTML, CONSTANT_AND );

        if ( StringUtils.isEmpty( strUrl ) || Strings.CI.equals( strUrl, CONSTANT_NULL ) )
        {
            return null;
        }

        if ( strUrl.startsWith( AppPathService.getBaseUrl( request ) ) || !PATTERN_ABSOLUTE_URL.matcher( strUrl ).find( ) )
        {
            return strUrl;
        }

        return null;
    }

    /**
     * Gets the page to come back to.
     *
     * @param request
     *            the request
     * @return the url
     */
    private UrlItem getLastUrl( HttpServletRequest request )
    {
        String strUrl = getFromUrl( request );

        if ( strUrl != null )
        {
            return new UrlItem( strUrl );
        }

        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_SEARCH ) ) )
        {
            _lastUrl = getManageUrl( request, VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE, false );
            _lastUrl.addParameter( PARAMETER_FILTER_EXTENDER_TYPE, request.getParameter( PARAMETER_FILTER_EXTENDER_TYPE ) );
            _lastUrl.addParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE, request.getParameter( PARAMETER_FILTER_ID_EXTENDABLE_RESOURCE ) );
            _lastUrl.addParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE, request.getParameter( PARAMETER_FILTER_EXTENDABLE_RESOURCE_TYPE ) );
        }

        if ( _lastUrl != null )
        {
            return _lastUrl;
        }

        return getManageUrl( request, VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE, true );
    }

    /**
     * Creates an extender and its add-on, then gives the page to go to: its configuration when it needs one and the user may change it.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the resource extender
     * @return the url of the next page
     */
    private String createResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        try
        {
            _extenderService.create( resourceExtender );
        }
        catch( Exception ex )
        {
            AppLogService.error( "{} when creating a resource extender", ex.getMessage( ), ex );
            _extenderService.remove( resourceExtender.getIdExtender( ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_GENERIC_MESSAGE, AdminMessage.TYPE_ERROR );
        }

        _extenderService.doCreateResourceAddOn( resourceExtender );

        if ( RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, null, ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                (User) getUser( ) ) )
        {
            IResourceExtender extender = _extenderService.getResourceExtender( resourceExtender.getExtenderType( ) );

            if ( ( extender != null ) && extender.isConfigRequired( ) )
            {
                return getUrlModifyResourceExtenderConfig( request, resourceExtender ).getUrl( );
            }
        }

        return getLastUrl( request ).getUrl( );
    }
}
