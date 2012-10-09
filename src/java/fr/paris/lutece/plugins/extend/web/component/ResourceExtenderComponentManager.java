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
package fr.paris.lutece.plugins.extend.web.component;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;
import fr.paris.lutece.plugins.extend.util.ExtendUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;


/**
 *
 * ResourceExtenderComponentManager
 *
 */
public class ResourceExtenderComponentManager implements IResourceExtenderComponentManager
{
    /** The Constant BEAN_MANAGER. */
    public static final String BEAN_MANAGER = "extend.resourceExtenderComponentManager";

    // CONSTANTS
    private static final String ORDER_BY_DATE_CREATION = " date_creation ";

    // PROPERTIES
    private static final String PROPERTY_DEFAULT_LIST_HISTORIES_PER_PAGE = "extend.listHistories.itemsPerPage";

    // PARAMETERS
    private static final String PARAMETER_ID_EXTENDER = "idExtender";

    // MARKS
    private static final String MARK_RESOURCE_EXTENDER = "resourceExtender";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_RESOURCE_EXTENDER_CONFIG = "resourceExtenderConfig";
    private static final String MARK_RESOURCE_EXTENDER_INFO = "resourceExtenderInfo";
    private static final String MARK_LIST_HISTORIES = "listHistories";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // TEMPLATES
    private static final String TEMPLATE_RESOURCE_EXTENDER_CONFIG = "admin/plugins/extend/resource_extender_config.html";
    private static final String TEMPLATE_RESOURCE_EXTENDER_INFO = "admin/plugins/extend/resource_extender_info.html";
    private static final String TEMPLATE_RESOURCE_EXTENDER_HISTORY = "admin/plugins/extend/resource_extender_history.html";

    // JSP
    private static final String JSP_URL_VIEW_EXTENDER_HISTORY = "jsp/admin/plugins/extend/ViewExtenderHistory.jsp";

    // MESSAGES
    private static final String MESSAGE_STOP_GENERIC_MESSAGE = "extend.message.stop.genericMessage";

    // SERVICES
    @Inject
    private IResourceExtenderService _resourceExtenderService;
    @Inject
    private IResourceExtenderHistoryService _resourceHistoryService;

    // VARIABLES
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * {@inheritDoc}
     */
    @Override
    public IResourceExtenderComponent getResourceExtenderComponent( String strExtenderType )
    {
        if ( StringUtils.isNotBlank( strExtenderType ) )
        {
            for ( IResourceExtenderComponent component : SpringContextService.getBeansOfType( 
                    IResourceExtenderComponent.class ) )
            {
                if ( component.getResourceExtender(  ).getKey(  ).equals( strExtenderType ) )
                {
                    return component;
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildXmlAddOn( ResourceExtenderDTO resourceExtender, StringBuffer strXml )
    {
        if ( resourceExtender != null )
        {
            IResourceExtenderComponent component = getResourceExtenderComponent( resourceExtender.getExtenderType(  ) );

            if ( component != null )
            {
                component.buildXmlAddOn( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ), resourceExtender.getParameters(  ), strXml );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageAddOn( ResourceExtenderDTO resourceExtender, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            IResourceExtenderComponent component = getResourceExtenderComponent( resourceExtender.getExtenderType(  ) );

            if ( component != null )
            {
                return component.getPageAddOn( resourceExtender.getIdExtendableResource(  ),
                    resourceExtender.getExtendableResourceType(  ), resourceExtender.getParameters(  ), request );
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            IResourceExtenderComponent component = getResourceExtenderComponent( resourceExtender.getExtenderType(  ) );

            if ( component != null )
            {
                Map<String, Object> model = new HashMap<String, Object>(  );
                model.put( MARK_RESOURCE_EXTENDER, resourceExtender );
                model.put( MARK_LOCALE, locale );
                model.put( MARK_RESOURCE_EXTENDER_CONFIG, component.getConfigHtml( resourceExtender, locale, request ) );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESOURCE_EXTENDER_CONFIG, locale, model );

                return template.getHtml(  );
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            IResourceExtenderComponent component = getResourceExtenderComponent( resourceExtender.getExtenderType(  ) );

            if ( component != null )
            {
                Map<String, Object> model = new HashMap<String, Object>(  );
                model.put( MARK_RESOURCE_EXTENDER, resourceExtender );
                model.put( MARK_LOCALE, locale );
                model.put( MARK_RESOURCE_EXTENDER_INFO, component.getInfoHtml( resourceExtender, locale, request ) );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESOURCE_EXTENDER_INFO, locale, model );

                return template.getHtml(  );
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHistoryHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        if ( resourceExtender != null )
        {
            ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );
            filter.setExtenderType( resourceExtender.getExtenderType(  ) );
            filter.setIdExtendableResource( resourceExtender.getIdExtendableResource(  ) );
            filter.setExtendableResourceType( resourceExtender.getExtendableResourceType(  ) );
            filter.setSortedAttributeName( ORDER_BY_DATE_CREATION );
            filter.setAscSort( true );

            List<ResourceExtenderHistory> listHistories = _resourceHistoryService.findByFilter( filter );

            // PAGINATOR
            _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_HISTORIES_PER_PAGE, 50 );
            _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                    _nDefaultItemsPerPage );

            UrlItem url = new UrlItem( JSP_URL_VIEW_EXTENDER_HISTORY );
            url.addParameter( PARAMETER_ID_EXTENDER, resourceExtender.getIdExtender(  ) );

            LocalizedPaginator<ResourceExtenderHistory> paginator = new LocalizedPaginator<ResourceExtenderHistory>( listHistories,
                    _nItemsPerPage, url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex,
                    request.getLocale(  ) );

            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_LIST_HISTORIES, paginator.getPageItems(  ) );
            model.put( MARK_RESOURCE_EXTENDER, resourceExtender );
            model.put( MARK_LOCALE, locale );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage(  ) ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESOURCE_EXTENDER_HISTORY, locale, model );

            return template.getHtml(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doSaveConfig( ResourceExtenderDTO resourceExtender, HttpServletRequest request )
        throws ExtendErrorException
    {
        if ( resourceExtender != null )
        {
            IResourceExtenderComponent component = getResourceExtenderComponent( resourceExtender.getExtenderType(  ) );

            if ( component != null )
            {
                IExtenderConfig config = component.getConfig( resourceExtender.getIdExtender(  ) );

                // If the config is not found, then create it
                if ( config == null )
                {
                    IResourceExtender extender = _resourceExtenderService.getResourceExtender( resourceExtender.getExtenderType(  ) );

                    if ( extender != null )
                    {
                        extender.doCreateResourceAddOn( resourceExtender );
                        config = component.getConfig( resourceExtender.getIdExtender(  ) );
                    }
                }

                if ( config != null )
                {
                    try
                    {
                        BeanUtils.populate( config, request.getParameterMap(  ) );
                    }
                    catch ( IllegalAccessException e )
                    {
                        AppLogService.error( "Unable to fetch data from request", e );
                    }
                    catch ( InvocationTargetException e )
                    {
                        AppLogService.error( "Unable to fetch data from request", e );
                    }

                    // Check mandatory fields
                    Set<ConstraintViolation<IExtenderConfig>> constraintViolations = BeanValidationUtil.validate( config );

                    if ( constraintViolations.size(  ) > 0 )
                    {
                        Object[] params = { ExtendUtils.buildStopMessage( constraintViolations ) };

                        throw new ExtendErrorException( I18nService.getLocalizedString( 
                                MESSAGE_STOP_GENERIC_MESSAGE, params, request.getLocale(  ) ) );
                    }

                    component.doSaveConfig( request, config );
                }
            }
        }
    }
}
