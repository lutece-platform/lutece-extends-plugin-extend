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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.type.ExtendableResourceTypeService;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * ResourceExtenderSearchFields
 *
 */
public class ResourceExtenderSearchFields implements IResourceExtenderSearchFields, Serializable
{
    private static final long serialVersionUID = 5171135962785175642L;

    // PROPERTIES
    private static final String PROPERTY_DEFAULT_LIST_RESOURCES_EXTENDERS_PER_PAGE = "socialhub.listResourceExtenders.itemsPerPage";
    private static final String PROPERTY_LABEL_ALL = "socialhub.labelAll";

    // PARAMETERS
    private static final String PARAMETER_SESSION = "session";
    private static final String PARAMETER_RESET = "reset";

    // MARKS
    private static final String MARK_LIST_RESOURCE_EXTENDERS = "listResourceExtenders";
    private static final String MARK_LIST_EXTENDERS = "listExtenders";
    private static final String MARK_FILTER = "filter";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_RESOURCE_TYPES_FOR_FILTER = "resourceTypesForFilter";
    private static final String MARK_EXTENDER_TYPES_FOR_FILTER = "extenderTypesForFilter";
    private static final String MARK_MAP_ACTION_PERMISSIONS = "mapActionPermissions";

    // VARIABLES
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_RESOURCES_EXTENDERS_PER_PAGE,
            50 );
    private String _strCurrentPageIndex;
    private String _strSortedAttributeName;
    private boolean _bIsAscSort;
    private ResourceExtenderDTOFilter _filter;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPageIndex(  )
    {
        return _strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefaultItemsPerPage(  )
    {
        return _nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPageIndex( String strCurrentPageIndex )
    {
        _strCurrentPageIndex = strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultItemsPerPage( int nDefaultItemsPerPage )
    {
        _nDefaultItemsPerPage = nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsPerPage(  )
    {
        return _nItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemsPerPage( int nItemsPerPage )
    {
        _nItemsPerPage = nItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSortedAttributeName(  )
    {
        return _strSortedAttributeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSortedAttributeName( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME ) ) )
        {
            _strSortedAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );
        }
        else
        {
            _strSortedAttributeName = StringUtils.EMPTY;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAscSort(  )
    {
        return _bIsAscSort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAscSort( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( Parameters.SORTED_ASC ) ) )
        {
            _bIsAscSort = Boolean.parseBoolean( request.getParameter( Parameters.SORTED_ASC ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( String strBaseUrl, HttpServletRequest request, Map<String, Object> model, AdminUser user )
        throws AccessDeniedException
    {
        fillModel( strBaseUrl, request, model, null, user );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( String strBaseUrl, HttpServletRequest request, Map<String, Object> model,
        String strIdExtendableResource, AdminUser user )
        throws AccessDeniedException
    {
        initFilter( request );

        // SORT
        setSortedAttributeName( request );
        setAscSort( request );
        _filter.setSortedAttributeName( _strSortedAttributeName );
        _filter.setAscSort( _bIsAscSort );

        if ( StringUtils.isNotBlank( strIdExtendableResource ) )
        {
            _filter.setFilterIdExtendableResource( strIdExtendableResource );
        }

        UrlItem url = new UrlItem( strBaseUrl );

        if ( getSortedAttributeName(  ) != null )
        {
            url.addParameter( Parameters.SORTED_ATTRIBUTE_NAME, getSortedAttributeName(  ) );
            url.addParameter( Parameters.SORTED_ASC, Boolean.toString( isAscSort(  ) ) );
        }

        url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        // PAGINATOR
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_RESOURCES_EXTENDERS_PER_PAGE,
                50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        IResourceExtenderService resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
        IExtendableResourceTypeService resourceTypeService = SpringContextService.getBean( ExtendableResourceTypeService.BEAN_SERVICE );
        List<Integer> listIdsExtenders = resourceExtenderService.findIdsByFilter( _filter );

        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<Integer>( listIdsExtenders, getItemsPerPage(  ),
                url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, getCurrentPageIndex(  ), request.getLocale(  ) );

        List<ResourceExtenderDTO> listResourceExtenders = resourceExtenderService.findByListIds( paginator.getPageItems(  ) );

        Map<String, Map<String, Boolean>> mapActionPermissions = resourceExtenderService.getActionPermissions( paginator.getPageItems(  ),
                user );

        // RESOURCE TYPES
        ReferenceList listResourceTypes = resourceTypeService.findAllAsRef(  );
        listResourceTypes.addItem( StringUtils.EMPTY,
            I18nService.getLocalizedString( PROPERTY_LABEL_ALL, request.getLocale(  ) ) );

        // EXTENDER TYPES
        ReferenceList listExtenderTypes = resourceExtenderService.getExtenderTypes( request.getLocale(  ) );
        listExtenderTypes.addItem( StringUtils.EMPTY,
            I18nService.getLocalizedString( PROPERTY_LABEL_ALL, request.getLocale(  ) ) );

        model.put( MARK_LIST_RESOURCE_EXTENDERS, listResourceExtenders );
        model.put( MARK_LIST_EXTENDERS, resourceExtenderService.getResourceExtenders(  ) );
        model.put( MARK_FILTER, _filter );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage(  ) ) );
        model.put( MARK_RESOURCE_TYPES_FOR_FILTER, listResourceTypes );
        model.put( MARK_EXTENDER_TYPES_FOR_FILTER, listExtenderTypes );
        model.put( MARK_MAP_ACTION_PERMISSIONS, mapActionPermissions );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initFilter( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( PARAMETER_RESET ) ) )
        {
            _filter = new ResourceExtenderDTOFilter(  );
        }
        else if ( StringUtils.isBlank( request.getParameter( PARAMETER_SESSION ) ) || ( _filter == null ) )
        {
            _filter = new ResourceExtenderDTOFilter(  );
            _filter.init( request );
        }
    }
}
