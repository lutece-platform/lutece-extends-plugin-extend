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

import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.portal.service.cache.Lutece107Cache;
import fr.paris.lutece.portal.service.cache.LuteceCache;
import fr.paris.lutece.test.LuteceTestCase;

import jakarta.inject.Inject;

import java.io.Serializable;

import org.junit.jupiter.api.Test;

/**
 * Tests the configuration service: absent configurations, and configurations of different extender types sharing one
 * cache.
 */
public class ResourceExtenderConfigServiceTest extends LuteceTestCase
{
    private static final int ID_EXTENDER_WITHOUT_CONFIG = 424242;
    private static final int ID_DEFAULT_CONFIG = -1;

    @Inject
    @LuteceCache( cacheName = "extendConfigServiceTestCache", keyType = String.class, valueType = Object.class, enable = true )
    private Lutece107Cache<String, Object> _cache;

    /**
     * A configuration the DAO does not find is answered null, twice, without being cached.
     */
    @Test
    public void testFindAbsentConfig( )
    {
        ResourceExtenderConfigService service = new ResourceExtenderConfigService( );
        service.setExtenderConfigDAO( new AbsentConfigDAO( ) );
        service.setExtenderCache( _cache );

        assertNull( service.find( ID_EXTENDER_WITHOUT_CONFIG ) );
        assertNull( service.find( ID_EXTENDER_WITHOUT_CONFIG ) );
        assertFalse( _cache.containsKey( service.getCacheKey( ID_EXTENDER_WITHOUT_CONFIG ) ) );
    }

    /**
     * Two extender types sharing the cache each read their own default configuration, before and after it is cached.
     */
    @Test
    public void testDefaultConfigScopedByType( )
    {
        ResourceExtenderConfigService firstService = new ResourceExtenderConfigService( );
        firstService.setExtenderConfigDAO( new FirstTypeConfigDAO( ) );
        firstService.setExtenderCache( _cache );
        ResourceExtenderConfigService secondService = new ResourceExtenderConfigService( );
        secondService.setExtenderConfigDAO( new SecondTypeConfigDAO( ) );
        secondService.setExtenderCache( _cache );

        IExtenderConfig firstConfig = firstService.find( ID_DEFAULT_CONFIG );
        IExtenderConfig secondConfig = secondService.find( ID_DEFAULT_CONFIG );

        assertTrue( firstConfig instanceof FirstTypeConfig );
        assertTrue( secondConfig instanceof SecondTypeConfig );
        assertTrue( firstService.<IExtenderConfig> find( ID_DEFAULT_CONFIG ) instanceof FirstTypeConfig );
        assertTrue( secondService.<IExtenderConfig> find( ID_DEFAULT_CONFIG ) instanceof SecondTypeConfig );
        assertNotEquals( firstService.getCacheKey( ID_DEFAULT_CONFIG ), secondService.getCacheKey( ID_DEFAULT_CONFIG ) );

        secondService.remove( ID_DEFAULT_CONFIG );

        assertTrue( _cache.containsKey( firstService.getCacheKey( ID_DEFAULT_CONFIG ) ) );
        assertFalse( _cache.containsKey( secondService.getCacheKey( ID_DEFAULT_CONFIG ) ) );
        firstService.remove( ID_DEFAULT_CONFIG );
    }

    /**
     * A configuration of a first extender type.
     */
    private static final class FirstTypeConfig implements IExtenderConfig, Serializable
    {
        private static final long serialVersionUID = 1L;
        private int _nIdExtender;

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIdExtender( )
        {
            return _nIdExtender;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setIdExtender( int nIdExtender )
        {
            _nIdExtender = nIdExtender;
        }
    }

    /**
     * A configuration of a second extender type.
     */
    private static final class SecondTypeConfig implements IExtenderConfig, Serializable
    {
        private static final long serialVersionUID = 1L;
        private int _nIdExtender;

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIdExtender( )
        {
            return _nIdExtender;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setIdExtender( int nIdExtender )
        {
            _nIdExtender = nIdExtender;
        }
    }

    /**
     * A configuration DAO of the first extender type, answering a new configuration for any id.
     */
    private static final class FirstTypeConfigDAO extends AbsentConfigDAO
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IExtenderConfig load( int nIdExtender )
        {
            FirstTypeConfig config = new FirstTypeConfig( );
            config.setIdExtender( nIdExtender );

            return config;
        }
    }

    /**
     * A configuration DAO of the second extender type, answering a new configuration for any id.
     */
    private static final class SecondTypeConfigDAO extends AbsentConfigDAO
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public IExtenderConfig load( int nIdExtender )
        {
            SecondTypeConfig config = new SecondTypeConfig( );
            config.setIdExtender( nIdExtender );

            return config;
        }
    }

    /**
     * A configuration DAO that stores nothing and finds nothing.
     */
    private static class AbsentConfigDAO implements IExtenderConfigDAO<IExtenderConfig>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void insert( IExtenderConfig config )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void store( IExtenderConfig config )
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IExtenderConfig load( int nIdExtender )
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void delete( int nIdExtender )
        {
        }
    }
}
