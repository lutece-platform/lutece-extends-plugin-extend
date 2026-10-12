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
package fr.paris.lutece.plugins.extend.service.extender.facade;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.business.HitHome;
import fr.paris.lutece.plugins.extend.modules.hit.service.extender.HitResourceExtender;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.jupiter.api.Test;

/**
 * Tests the hit values the facade gives to the modules.
 */
public class ResourceExtenderServiceFacadeTest extends LuteceTestCase
{
    private static final String RESOURCE_TYPE = "E2E_FACADE";

    /**
     * A resource never viewed counts 0 views, in the recap as in the export.
     *
     * @throws InfoExtenderException
     *             if the hit extender type is not registered
     */
    @Test
    public void testNeverViewedCountsZero( ) throws InfoExtenderException
    {
        assertEquals( "0", ResourceExtenderServiceFacade.getInfoForRecap( HitResourceExtender.EXTENDER_TYPE, "e2e-never", RESOURCE_TYPE ) );
        assertEquals( "0", ResourceExtenderServiceFacade.getInfoForExport( HitResourceExtender.EXTENDER_TYPE, "e2e-never", RESOURCE_TYPE ) );
    }

    /**
     * A resource viewed gives its count.
     *
     * @throws InfoExtenderException
     *             if the hit extender type is not registered
     */
    @Test
    public void testViewedGivesCount( ) throws InfoExtenderException
    {
        Hit hit = new Hit( );
        hit.setIdExtendableResource( "e2e-seen" );
        hit.setExtendableResourceType( RESOURCE_TYPE );
        hit.setNbHits( 7 );
        HitHome.create( hit );

        try
        {
            assertEquals( "7", ResourceExtenderServiceFacade.getInfoForRecap( HitResourceExtender.EXTENDER_TYPE, "e2e-seen", RESOURCE_TYPE ) );
        }
        finally
        {
            HitHome.removeByResource( "e2e-seen", RESOURCE_TYPE );
        }
    }
}
