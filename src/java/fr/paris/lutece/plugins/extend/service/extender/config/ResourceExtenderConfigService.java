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
package fr.paris.lutece.plugins.extend.service.extender.config;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderCacheService;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.service.util.AppLogService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;

import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;

/**
 *
 * ResourceExtenderConfigService
 *
 */
@Named( "extend.resourceExtenderConfigService" )
public class ResourceExtenderConfigService implements IResourceExtenderConfigService
{
    private IExtenderConfigDAO<IExtenderConfig> _extenderConfigDAO;

    @Inject
    private IResourceExtenderService _extenderService;

    @Inject
    @LuteceCache( cacheName = "extenderCache", keyType = String.class, valueType = Object.class, enable = true )
    Lutece107Cache<String, Object> _extenderCache;

    /**
     * Set the extender config DAO
     * 
     * @param extenderConfigDAO
     *            the extender config DAO
     */
    public void setExtenderConfigDAO( IExtenderConfigDAO<IExtenderConfig> extenderConfigDAO )
    {
        _extenderConfigDAO = extenderConfigDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create( IExtenderConfig config )
    {
        if ( config != null )
        {
            _extenderConfigDAO.insert( config );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( IExtenderConfig config )
    {
        if ( config != null )
        {
            _extenderConfigDAO.store( config );
            _extenderCache.remove( getCacheKey( config.getIdExtender( ) ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( int nIdExtender )
    {
        _extenderConfigDAO.delete( nIdExtender );
        _extenderCache.remove( getCacheKey( nIdExtender ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T find( int nIdExtender )
    {
        String strKey = getCacheKey( nIdExtender );
        IExtenderConfig config = (IExtenderConfig) _extenderCache.get( strKey );
        if ( config == null )
        {
            config = _extenderConfigDAO.load( nIdExtender );
            _extenderCache.put( strKey, config );
        }
        return getConfigBean( config );
    }

    /**
     * Get a cache key for the extender config
     * 
     * @param nIdExtender
     *            id of the extender
     * @return the cache key
     */
    private String getCacheKey( int nIdExtender )
    {
        return new StringBuilder( "ExtenderConfig_" ).append( nIdExtender ).toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T find( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType )
    {
        ResourceExtenderDTO resourceExtender = _extenderService.findResourceExtenderIncludingWildcard( strExtenderType, strIdExtendableResource,
                strExtendableResourceType );

        if ( resourceExtender != null )
        {
            return find( resourceExtender.getIdExtender( ) );
        }

        return null;
    }

    /**
     * Get the config bean
     * 
     * @param <T>
     *            The class of the bean
     * @param config
     *            the config
     * @return the config bean
     */
    public static <T> T getConfigBean( IExtenderConfig config )
    {
        if ( config != null )
        {
            try
            {
                return (T) config;
            }
            catch( Exception e )
            {
                AppLogService.error( e.getMessage( ), e );
            }
        }

        return null;
    }

    /**
     * Get the DAO
     * 
     * @return the DAO
     */
    protected IExtenderConfigDAO<IExtenderConfig> getDAO( )
    {
        return _extenderConfigDAO;
    }
}
