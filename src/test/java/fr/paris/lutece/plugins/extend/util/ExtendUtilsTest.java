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
package fr.paris.lutece.plugins.extend.util;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests the labels of extender types and resource types.
 */
public class ExtendUtilsTest extends LuteceTestCase
{
    /**
     * A known extender type is named by its label, an extender type and a resource type no service knows keep their codes.
     */
    @Test
    public void testFillTypeLabels( )
    {
        ResourceExtenderDTO known = new ResourceExtenderDTO( );
        known.setExtenderType( "hit" );
        known.setExtendableResourceType( "E2E_NONE" );

        ResourceExtenderDTO unknown = new ResourceExtenderDTO( );
        unknown.setExtenderType( "e2e-none" );
        unknown.setExtendableResourceType( "E2E_NONE" );

        Map<String, Object> model = new HashMap<>( );
        ExtendUtils.fillTypeLabels( model::put, List.of( known, unknown ), Locale.FRENCH );

        Map<?, ?> extenderTypeLabels = (Map<?, ?>) model.get( "extenderTypeLabels" );
        Map<?, ?> resourceTypeLabels = (Map<?, ?>) model.get( "resourceTypeLabels" );

        assertEquals( "Compteur de vues", extenderTypeLabels.get( "hit" ) );
        assertEquals( "e2e-none", extenderTypeLabels.get( "e2e-none" ) );
        assertEquals( "E2E_NONE", resourceTypeLabels.get( "E2E_NONE" ) );
    }

    /**
     * The resource type wildcard is named, not shown as a star.
     */
    @Test
    public void testWildcardResourceTypeLabel( )
    {
        assertEquals( "Tous les types de ressource", ExtendUtils.getResourceTypeLabel( "*", Locale.FRENCH ) );
    }

    /**
     * An extender without type nor resource is refused, and the button of the message returns to the screen the caller gives.
     */
    @Test
    public void testValidateResourceExtenderReturnsToTheGivenScreen( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        String strReturnUrl = "http://localhost/lutece/jsp/admin/plugins/extend/ManageResourceExtendersByResource.jsp?view=createResourceExtender";

        String strUrl = ExtendUtils.validateResourceExtender( request, new ResourceExtenderDTO( ), strReturnUrl );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        assertEquals( strReturnUrl, AdminMessageService.getMessage( request ).getBackUrl( ) );
    }

    /**
     * The former signature still refuses an extender without type nor resource, and its message only goes back in the history.
     */
    @Test
    @SuppressWarnings( "deprecation" )
    public void testValidateResourceExtenderFormerSignatureGoesBackInTheHistory( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );

        String strUrl = ExtendUtils.validateResourceExtender( request, new ResourceExtenderDTO( ) );

        assertTrue( strUrl.endsWith( AdminMessageService.getMessageRelativeUrl( ) ), strUrl );
        assertNull( AdminMessageService.getMessage( request ).getBackUrl( ) );
    }
}
