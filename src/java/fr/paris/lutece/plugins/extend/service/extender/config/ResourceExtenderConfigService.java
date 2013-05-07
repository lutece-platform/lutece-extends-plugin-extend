/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.service.util.AppLogService;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 *
 * ResourceExtenderConfigService
 *
 */
public class ResourceExtenderConfigService implements IResourceExtenderConfigService, InitializingBean
{
    private IExtenderConfigDAO<IExtenderConfig> _extenderConfigDAO;
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * Set the extender config DAO
     * @param extenderConfigDAO the extender config DAO
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
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( int nIdExtender )
    {
        _extenderConfigDAO.delete( nIdExtender );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T find( int nIdExtender )
    {
        return getConfigBean( _extenderConfigDAO.load( nIdExtender ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T find( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType )
    {
        ResourceExtenderDTO resourceExtender = _extenderService.findResourceExtender( strExtenderType,
                strIdExtendableResource, strExtendableResourceType );

        if ( resourceExtender == null )
        {
            // Check if the resource extender has an ID resource '*'
            resourceExtender = _extenderService.findWildCardResourceExtender( strExtenderType, strExtendableResourceType );
        }

        if ( resourceExtender != null )
        {
            return find( resourceExtender.getIdExtender( ) );
        }

        return null;
    }

    /**
     * Get the config bean
     * @param <T> The class of the bean
     * @param config the config
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
            catch ( Exception e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return null;
    }

    /**
     * Get the DAO
     * @return the DAO
     */
    protected IExtenderConfigDAO<IExtenderConfig> getDAO(  )
    {
        return _extenderConfigDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet(  ) throws Exception
    {
        Assert.notNull( _extenderConfigDAO, "The property 'extenderConfigDAO' must be set." );
    }
}
