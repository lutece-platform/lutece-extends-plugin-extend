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
package fr.paris.lutece.plugins.extend.modules.hit.business;

import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.Test;


/**
 *
 * HitTest
 *
 */
public class HitTest extends LuteceTestCase
{
    private static final int ID_HIT_1 = 1;
    private static final String ID_RESOURCE_10 = "10";
    private static final String RESOURCE_TYPE_FOO = "foo";
    private static final int NB_HITS_2 = 2;
    private static final String ID_RESOURCE_20 = "20";
    private static final String RESOURCE_TYPE_BAR = "bar";
    private static final int NB_HITS_3 = 3;

    /**
     * Test business.
     */
    @Test
    public void testBusiness(  )
    {
        Plugin plugin = PluginService.getPlugin( ExtendPlugin.PLUGIN_NAME );
        IHitDAO dao = SpringContextService.getBean( "extend.hitDAO" );

        // Init object
        Hit hit = new Hit(  );
        hit.setIdHit( ID_HIT_1 );
        hit.setIdExtendableResource( ID_RESOURCE_10 );
        hit.setExtendableResourceType( RESOURCE_TYPE_FOO );
        hit.setNbHits( NB_HITS_2 );

        // Test create
        dao.insert( hit, plugin );

        Hit hitStored = dao.load( hit.getIdHit(  ), plugin );
        assertEquals( hit.getIdHit(  ), hitStored.getIdHit(  ) );
        assertEquals( hit.getIdExtendableResource(  ), hitStored.getIdExtendableResource(  ) );
        assertEquals( hit.getExtendableResourceType(  ), hitStored.getExtendableResourceType(  ) );
        assertEquals( hit.getNbHits(  ), hitStored.getNbHits(  ) );

        // Test update
        hit.setIdExtendableResource( ID_RESOURCE_20 );
        hit.setExtendableResourceType( RESOURCE_TYPE_BAR );
        hit.setNbHits( NB_HITS_3 );
        dao.store( hit, plugin );
        hitStored = dao.load( hit.getIdHit(  ), plugin );
        assertEquals( hit.getIdHit(  ), hitStored.getIdHit(  ) );
        assertEquals( hit.getIdExtendableResource(  ), hitStored.getIdExtendableResource(  ) );
        assertEquals( hit.getExtendableResourceType(  ), hitStored.getExtendableResourceType(  ) );
        assertEquals( hit.getNbHits(  ), hitStored.getNbHits(  ) );

        // Test finders
        dao.loadByParameters( ID_RESOURCE_20, RESOURCE_TYPE_BAR, plugin );

        // Test remove
        dao.delete( hit.getIdHit(  ), plugin );
        hitStored = dao.load( hit.getIdHit(  ), plugin );
        assertNull( hitStored );
    }
}
