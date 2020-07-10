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
package fr.paris.lutece.plugins.extend.business.extender;

import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.Test;

/**
 *
 * ResourceExtenderTest
 *
 */
public class ResourceExtenderTest extends LuteceTestCase
{
    private static final int ID_1 = 1;
    private static final String EXT_TYPE_1 = "EXT_TYPE_1";
    private static final String EXT_TYPE_2 = "EXT_TYPE_2";
    private static final String ID_RES_1 = "ID_1";
    private static final String ID_RES_2 = "ID_2";
    private static final String RES_TYPE_1 = "RES_TYPE_1";
    private static final String RES_TYPE_2 = "RES_TYPE_2";

    /**
     * Test business.
     */
    @Test
    public void testBusiness( )
    {
        Plugin plugin = PluginService.getPlugin( ExtendPlugin.PLUGIN_NAME );
        IResourceExtenderDAO dao = SpringContextService.getBean( "extend.resourceExtenderDAO" );

        // Init object
        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setIdExtender( ID_1 );
        extender.setExtenderType( EXT_TYPE_1 );
        extender.setIdExtendableResource( ID_RES_1 );
        extender.setExtendableResourceType( RES_TYPE_1 );

        // Test create
        dao.insert( extender, plugin );

        ResourceExtenderDTO extenderStored = dao.load( extender.getIdExtender( ), plugin );
        assertEquals( extender.getIdExtender( ), extenderStored.getIdExtender( ) );
        assertEquals( extender.getExtenderType( ), extenderStored.getExtenderType( ) );
        assertEquals( extender.getIdExtendableResource( ), extenderStored.getIdExtendableResource( ) );
        assertEquals( extender.getExtendableResourceType( ), extenderStored.getExtendableResourceType( ) );

        // Test update
        extender.setExtenderType( EXT_TYPE_2 );
        extender.setIdExtendableResource( ID_RES_2 );
        extender.setExtendableResourceType( RES_TYPE_2 );
        dao.store( extender, plugin );
        extenderStored = dao.load( extender.getIdExtender( ), plugin );
        assertEquals( extender.getIdExtender( ), extenderStored.getIdExtender( ) );
        assertEquals( extender.getExtenderType( ), extenderStored.getExtenderType( ) );
        assertEquals( extender.getIdExtendableResource( ), extenderStored.getIdExtendableResource( ) );
        assertEquals( extender.getExtendableResourceType( ), extenderStored.getExtendableResourceType( ) );

        // Test finders
        dao.loadAll( plugin );

        // Test remove
        dao.delete( extender.getIdExtender( ), plugin );
        extenderStored = dao.load( extender.getIdExtender( ), plugin );
        assertNull( extenderStored );
    }
}
