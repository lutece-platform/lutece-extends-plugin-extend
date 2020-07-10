/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.plugins.extend.business.IDefaultExtendableResourceDAO;
import fr.paris.lutece.portal.service.resource.IExtendableResource;

import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import javax.inject.Inject;

/**
 *
 * Default service that is invoked when other IExtendableResourceService are not invoked. This service stores in database the information of the
 * {@link IExtendableResource} :
 * <ul>
 * <li>id</li>
 * <li>resourceType</li>
 * <li>name</li>
 * </ul>
 *
 */
public class DefaultExtendableResourceService implements IDefaultExtendableResourceService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.defaultExtendableResourceService";
    @Inject
    private IDefaultExtendableResourceDAO _extendableResourceDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strResourceType )
    {
        // This service is invoked only if the other services are not invoked
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtendableResource getResource( String strIdResource, String strResourceType )
    {
        return _extendableResourceDAO.load( strIdResource, strResourceType, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void create( IExtendableResource resource )
    {
        _extendableResourceDAO.insert( resource, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void update( IExtendableResource resource )
    {
        _extendableResourceDAO.store( resource, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void remove( String strIdResource, String strResourceType )
    {
        _extendableResourceDAO.delete( strIdResource, strResourceType, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceType( )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeDescription( Locale locale )
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceUrl( String strIdResource, String strResourceType )
    {
        return null;
    }
}
