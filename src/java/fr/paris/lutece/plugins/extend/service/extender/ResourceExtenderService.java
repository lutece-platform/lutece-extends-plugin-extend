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
package fr.paris.lutece.plugins.extend.service.extender;

import fr.paris.lutece.plugins.extend.business.extender.IResourceExtenderDAO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.ExtendableResourceResourceIdService;
import fr.paris.lutece.plugins.extend.service.IExtendableResourceManager;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.plugins.extend.service.type.IExtendableResourceTypeService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * ResourceExtenderService
 *
 */
public class ResourceExtenderService implements IResourceExtenderService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.resourceExtenderService";
    @Inject
    private IResourceExtenderDAO _extenderDAO;
    @Inject
    private IExtendableResourceTypeService _extendableResourceTypeService;
    @Inject
    private IExtendableResourceManager _extendableResourceManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void create( ResourceExtenderDTO extender )
    {
        // Check if the resource type exists or not
        if ( ( extender != null ) &&
                ( _extendableResourceTypeService.findByPrimaryKey( extender.getExtendableResourceType(  ) ) != null ) )
        {
            _extenderDAO.insert( extender, ExtendPlugin.getPlugin(  ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void update( ResourceExtenderDTO extender )
    {
        // Check if the resource type exists or not
        if ( ( extender != null ) &&
                ( _extendableResourceTypeService.findByPrimaryKey( extender.getExtendableResourceType(  ) ) != null ) )
        {
            _extenderDAO.store( extender, ExtendPlugin.getPlugin(  ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void remove( int nIdExtender )
    {
        _extenderDAO.delete( nIdExtender, ExtendPlugin.getPlugin(  ) );
    }

    // CHECKS

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthorized( String strIdExtendableResource, String strExtendableResourceType,
        String strExtenderType )
    {
        // First check if the wildcard '*' is defined for this resourceType and extenderType
        if ( isAuthorizedToAllResources( strExtendableResourceType, strExtenderType ) )
        {
            return true;
        }

        // If no wildcard, then check for this specific id
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( strExtenderType, strIdExtendableResource,
                strExtendableResourceType );
        filter.setWideSearch( false );

        List<ResourceExtenderDTO> listResources = findByFilter( filter );

        return ( listResources != null ) && !listResources.isEmpty(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthorizedToAllResources( String strExtendableResourceType, String strExtenderType )
    {
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( strExtenderType,
                ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE, strExtendableResourceType );
        filter.setWideSearch( false );

        List<ResourceExtenderDTO> listResources = findByFilter( filter );

        return ( listResources != null ) && !listResources.isEmpty(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthorizedToAllResources( int nIdExtender )
    {
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter(  );
        filter.setFilterIdExtender( nIdExtender );
        filter.setFilterIdExtendableResource( ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE );
        filter.setWideSearch( false );

        List<ResourceExtenderDTO> listResources = findByFilter( filter );

        return ( listResources != null ) && !listResources.isEmpty(  );
    }

    // FINDERS

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderDTO findByPrimaryKey( int nIdExtender )
    {
        ResourceExtenderDTO resourceExtender = _extenderDAO.load( nIdExtender, ExtendPlugin.getPlugin(  ) );
        resourceExtender.setName( getExtendableResourceName( resourceExtender ) );

        return resourceExtender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> findAll(  )
    {
        List<ResourceExtenderDTO> listResourceExtenders = _extenderDAO.loadAll( ExtendPlugin.getPlugin(  ) );

        for ( ResourceExtenderDTO resourceExtender : listResourceExtenders )
        {
            resourceExtender.setName( getExtendableResourceName( resourceExtender ) );
        }

        return listResourceExtenders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> findByFilter( ResourceExtenderDTOFilter filter )
    {
        List<ResourceExtenderDTO> listResourceExtenders = _extenderDAO.loadByFilter( filter,
                ExtendPlugin.getPlugin(  ) );

        for ( ResourceExtenderDTO resourceExtender : listResourceExtenders )
        {
            resourceExtender.setName( getExtendableResourceName( resourceExtender ) );
        }

        return listResourceExtenders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderDTO findResourceExtender( String strExtenderType, String strIdExtendableResource,
        String strExtendableResourceType )
    {
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( strExtenderType, strIdExtendableResource,
                strExtendableResourceType );
        filter.setWideSearch( false );

        List<ResourceExtenderDTO> listResources = findByFilter( filter );

        if ( ( listResources != null ) && ( listResources.size(  ) == 1 ) )
        {
            ResourceExtenderDTO resourceExtender = listResources.get( 0 );
            resourceExtender.setName( getExtendableResourceName( resourceExtender ) );

            return resourceExtender;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderDTO findWildCardResourceExtender( String strExtenderType, String strExtendableResourceType )
    {
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( strExtenderType,
                ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE, strExtendableResourceType );
        filter.setWideSearch( false );

        List<ResourceExtenderDTO> listResources = findByFilter( filter );

        if ( ( listResources != null ) && ( listResources.size(  ) == 1 ) )
        {
            ResourceExtenderDTO resourceExtender = listResources.get( 0 );
            resourceExtender.setName( getExtendableResourceName( resourceExtender ) );

            return resourceExtender;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdsByFilter( ResourceExtenderDTOFilter filter )
    {
        return _extenderDAO.loadIdsByFilter( filter, ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> findByListIds( List<Integer> listIds )
    {
        List<ResourceExtenderDTO> listResourceExtenders = _extenderDAO.loadByListIds( listIds,
                ExtendPlugin.getPlugin(  ) );

        for ( ResourceExtenderDTO resourceExtender : listResourceExtenders )
        {
            resourceExtender.setName( getExtendableResourceName( resourceExtender ) );
        }

        return listResourceExtenders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getExtenderTypes( Locale locale )
    {
        ReferenceList refExtenderTypes = new ReferenceList(  );

        for ( IResourceExtender extender : getResourceExtenders(  ) )
        {
            refExtenderTypes.addItem( extender.getKey(  ), extender.getTitle( locale ) );
        }

        return refExtenderTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent( String strIdExtendableResource, String strExtendableResourceType, String strExtenderType,
        String strParameters, HttpServletRequest request )
    {
        if ( isAuthorized( strIdExtendableResource, strExtendableResourceType, strExtenderType ) )
        {
            for ( IResourceExtender extender : getResourceExtenders(  ) )
            {
                if ( extender.isInvoked( strExtenderType ) )
                {
                    return extender.getContent( strIdExtendableResource, strExtendableResourceType, strParameters,
                        request );
                }
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateResourceAddOn( ResourceExtenderDTO resourceExtender )
    {
        for ( IResourceExtender extender : getResourceExtenders(  ) )
        {
            if ( extender.isInvoked( resourceExtender.getExtenderType(  ) ) )
            {
                extender.doCreateResourceAddOn( resourceExtender );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doDeleteResourceAddOn( ResourceExtenderDTO resourceExtender )
    {
        for ( IResourceExtender extender : getResourceExtenders(  ) )
        {
            if ( extender.isInvoked( resourceExtender.getExtenderType(  ) ) )
            {
                extender.doDeleteResourceAddOn( resourceExtender );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IResourceExtender> getResourceExtenders(  )
    {
        return SpringContextService.getBeansOfType( IResourceExtender.class );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IResourceExtender getResourceExtender( String strExtenderType )
    {
        for ( IResourceExtender extender : getResourceExtenders(  ) )
        {
            if ( extender.isInvoked( strExtenderType ) )
            {
                return extender;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceName( ResourceExtenderDTO resourceExtender )
    {
        return getExtendableResourceName( resourceExtender.getIdExtendableResource(  ),
            resourceExtender.getExtendableResourceType(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceName( String strIdExtendableResource, String strExtendableResourceType )
    {
        IExtendableResource extendableResource = _extendableResourceManager.getResource( strIdExtendableResource,
                strExtendableResourceType );

        // If no resource is found, then try to fetch the resource with a wildcard id '*'
        if ( extendableResource == null )
        {
            extendableResource = _extendableResourceManager.getResource( ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE,
                    strExtendableResourceType );
        }

        if ( extendableResource != null )
        {
            return extendableResource.getExtendableResourceName(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Map<String, Boolean>> getActionPermissions( List<Integer> listIds, AdminUser user )
    {
        Map<String, Map<String, Boolean>> mapActionPermissions = new HashMap<String, Map<String, Boolean>>(  );

        for ( int nId : listIds )
        {
            Map<String, Boolean> mapPermissions = new HashMap<String, Boolean>(  );
            mapPermissions.put( ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION,
                RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, Integer.toString( nId ),
                    ExtendableResourceResourceIdService.PERMISSION_MODIFY_CONFIGURATION, user ) );
            mapPermissions.put( ExtendableResourceResourceIdService.PERMISSION_VIEW_INFO,
                RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, Integer.toString( nId ),
                    ExtendableResourceResourceIdService.PERMISSION_VIEW_INFO, user ) );
            mapPermissions.put( ExtendableResourceResourceIdService.PERMISSION_VIEW_HISTORY,
                RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, Integer.toString( nId ),
                    ExtendableResourceResourceIdService.PERMISSION_VIEW_HISTORY, user ) );
            mapPermissions.put( ExtendableResourceResourceIdService.PERMISSION_DELETE,
                RBACService.isAuthorized( ResourceExtenderDTO.RESOURCE_TYPE, Integer.toString( nId ),
                    ExtendableResourceResourceIdService.PERMISSION_DELETE, user ) );

            mapActionPermissions.put( Integer.toString( nId ), mapPermissions );
        }

        return mapActionPermissions;
    }
}
