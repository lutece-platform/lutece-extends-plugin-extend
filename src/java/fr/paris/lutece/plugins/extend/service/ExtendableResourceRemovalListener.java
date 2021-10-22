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
package fr.paris.lutece.plugins.extend.service;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.resource.IExtendableResourceRemovalListener;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

/**
 * Remove extensions of resources when they are removed
 */
public class ExtendableResourceRemovalListener implements IExtendableResourceRemovalListener
{
    private IResourceExtenderService _resourceExtenderService;
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveResourceExtentions( String strExtendableResourceType, String strIdExtendableResource )
    {
        IResourceExtenderService resourceExtenderService = getResourceExtenderService( );
        IResourceExtenderHistoryService resourceExtenderHistoryService = getResourceExtenderHistoryService( );
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( );
        filter.setFilterExtendableResourceType( strExtendableResourceType );
        filter.setFilterIdExtendableResource( strIdExtendableResource );

        // We get and remove every extender associated with the removed resource
        List<ResourceExtenderDTO> listResourceExtender = resourceExtenderService.findByFilter( filter );

        if ( ( listResourceExtender != null ) && ( listResourceExtender.size( ) > 0 ) )
        {
            for ( ResourceExtenderDTO extender : listResourceExtender )
            {
                resourceExtenderService.doDeleteResourceAddOn( extender );
                resourceExtenderService.remove( extender.getIdExtender( ) );
                resourceExtenderHistoryService.removeByResource( extender.getExtenderType( ), strIdExtendableResource, strExtendableResourceType );
            }
        }

        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setExtendableResourceType( strExtendableResourceType );
        extender.setIdExtendableResource( strIdExtendableResource );
        extender.setIdExtender( 0 );

        // We now remove add on of extender associated with every resources of this resource type
        Map<String, Boolean> mapExtenderTypes = resourceExtenderService.getExtenderTypesInstalled( strIdExtendableResource, strExtendableResourceType,
                ExtendPlugin.getPlugin( ) );

        for ( String strExtenderType : mapExtenderTypes.keySet( ) )
        {
            extender.setExtenderType( strExtenderType );
            resourceExtenderService.doDeleteResourceAddOn( extender );
            resourceExtenderHistoryService.removeByResource( strExtenderType, strIdExtendableResource, strExtendableResourceType );
        }
    }

    /**
     * Get the resource extender service
     * 
     * @return the resource extender service
     */
    private IResourceExtenderService getResourceExtenderService( )
    {
        if ( _resourceExtenderService == null )
        {
            synchronized( this )
            {
                // Double null check to prevent concurrency errors
                if ( _resourceExtenderService == null )
                {
                    _resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
                }
            }
        }

        return _resourceExtenderService;
    }

    /**
     * Get the resource extender history service
     * 
     * @return the resource extender history service
     */
    private IResourceExtenderHistoryService getResourceExtenderHistoryService( )
    {
        if ( _resourceExtenderHistoryService == null )
        {
            synchronized( this )
            {
                // Double null check to prevent concurrency errors
                if ( _resourceExtenderHistoryService == null )
                {
                    _resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
                }
            }
        }

        return _resourceExtenderHistoryService;
    }
}
