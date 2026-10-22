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
package fr.paris.lutece.plugins.extend.service.extender;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.modules.hit.service.IHitService;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;

import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.junit.jupiter.api.Test;

/**
 * Tests the lookup of extenders and the cache behind it.
 */
public class ResourceExtenderServiceTest extends LuteceTestCase
{
    private static final String EXTENDER_TYPE = "hit";
    private static final String RESOURCE_TYPE = "PAGE";
    private static final String ID_RESOURCE = "424243";

    @Inject
    private IResourceExtenderService _extenderService;
    @Inject
    private IHitService _hitService;

    /**
     * An extender found nowhere is answered null without an error in the log.
     */
    @Test
    public void testFindAbsentExtenderLogsNoError( )
    {
        Logger logger = (Logger) AppLogService.getLogger( );
        ErrorCounter counter = new ErrorCounter( );
        counter.start( );
        logger.addAppender( counter );

        try
        {
            assertNull( _extenderService.findResourceExtenderIncludingWildcard( "hit", "e2e-absent", "E2E_NO_TYPE" ) );
        }
        finally
        {
            logger.removeAppender( counter );
            counter.stop( );
        }

        assertTrue( counter.getErrors( ).isEmpty( ), "unexpected error logs: " + counter.getErrors( ) );
    }

    /**
     * An extender created, removed and created again on a resource already looked up is seen at once each time.
     */
    @Test
    public void testCreateResetsAuthorizedCache( )
    {
        assertFalse( _extenderService.isAuthorized( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE ) );

        ResourceExtenderDTO extender = newExtender( );
        _extenderService.create( extender );
        assertTrue( _extenderService.isAuthorized( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE ) );

        _extenderService.remove( extender.getIdExtender( ) );
        assertFalse( _extenderService.isAuthorized( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE ) );

        ResourceExtenderDTO extenderAgain = newExtender( );
        _extenderService.create( extenderAgain );
        try
        {
            assertTrue( _extenderService.isAuthorized( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE ) );
            assertNotNull( _extenderService.findResourceExtenderIncludingWildcard( EXTENDER_TYPE, ID_RESOURCE, RESOURCE_TYPE ) );
        }
        finally
        {
            _extenderService.remove( extenderAgain.getIdExtender( ) );
        }
    }

    /**
     * An extender declared for every resource of a type is removable from the screen of that type (resource id '*'), and only shown as enabled by
     * default (not removable) on the screen of one resource of the type.
     */
    @Test
    public void testWildcardExtenderRemovableFromWildcardResource( )
    {
        ResourceExtenderDTO extender = newExtender( );
        extender.setIdExtendableResource( ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE );
        _extenderService.create( extender );
        try
        {
            Map<String, Boolean> mapWildcard = _extenderService.getExtenderTypesInstalled( ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE, RESOURCE_TYPE,
                    ExtendPlugin.getPlugin( ) );
            assertEquals( Boolean.FALSE, mapWildcard.get( EXTENDER_TYPE ) );

            Map<String, Boolean> mapResource = _extenderService.getExtenderTypesInstalled( ID_RESOURCE, RESOURCE_TYPE, ExtendPlugin.getPlugin( ) );
            assertEquals( Boolean.TRUE, mapResource.get( EXTENDER_TYPE ) );
        }
        finally
        {
            _extenderService.remove( extender.getIdExtender( ) );
        }
    }

    /**
     * The extender type all, written by the page template of the core (@Extender[id,PAGE,all]@), renders every extender added to the resource: here the
     * hit extender, whose content counts the hit of the page. A resource without extender gets an empty content.
     */
    @Test
    public void testContentOfAllExtendersRendersTheExtendersOfTheResource( )
    {
        ResourceExtenderDTO extender = newExtender( );
        _extenderService.create( extender );
        try
        {
            assertNull( _hitService.findByParameters( ID_RESOURCE, RESOURCE_TYPE ) );

            _extenderService.getContent( ID_RESOURCE, RESOURCE_TYPE, ResourceExtenderService.EXTENDER_TYPE_ALL, null, new MockHttpServletRequest( ) );

            assertNotNull( _hitService.findByParameters( ID_RESOURCE, RESOURCE_TYPE ) );
            assertEquals( "", _extenderService.getContent( ID_RESOURCE + "0", RESOURCE_TYPE, ResourceExtenderService.EXTENDER_TYPE_ALL, null,
                    new MockHttpServletRequest( ) ) );
        }
        finally
        {
            _extenderService.remove( extender.getIdExtender( ) );
            _hitService.removeByResource( ID_RESOURCE, RESOURCE_TYPE );
        }
    }

    /**
     * A disabled extender is not displayed in the front office, neither by its own marker nor by the type all; it still counts as installed for the
     * back office, which refuses to add the same extender twice.
     */
    @Test
    public void testDisabledExtenderNotDisplayed( )
    {
        ResourceExtenderDTO extender = newExtender( );
        _extenderService.create( extender );
        try
        {
            extender.setIsActive( false );
            _extenderService.update( extender );

            _extenderService.getContent( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE, null, new MockHttpServletRequest( ) );
            _extenderService.getContent( ID_RESOURCE, RESOURCE_TYPE, ResourceExtenderService.EXTENDER_TYPE_ALL, null, new MockHttpServletRequest( ) );
            assertNull( _hitService.findByParameters( ID_RESOURCE, RESOURCE_TYPE ) );
            assertTrue( _extenderService.isAuthorized( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE ) );

            extender.setIsActive( true );
            _extenderService.update( extender );
            _extenderService.getContent( ID_RESOURCE, RESOURCE_TYPE, EXTENDER_TYPE, null, new MockHttpServletRequest( ) );
            assertNotNull( _hitService.findByParameters( ID_RESOURCE, RESOURCE_TYPE ) );
        }
        finally
        {
            _extenderService.remove( extender.getIdExtender( ) );
            _hitService.removeByResource( ID_RESOURCE, RESOURCE_TYPE );
        }
    }

    /**
     * Builds an extender of the test resource.
     *
     * @return the extender
     */
    private static ResourceExtenderDTO newExtender( )
    {
        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setExtenderType( EXTENDER_TYPE );
        extender.setIdExtendableResource( ID_RESOURCE );
        extender.setExtendableResourceType( RESOURCE_TYPE );

        return extender;
    }

    /**
     * Appender keeping the messages logged at ERROR level or above.
     */
    private static final class ErrorCounter extends AbstractAppender
    {
        private final List<String> _listErrors = new ArrayList<>( );

        /**
         * Creates the appender.
         */
        ErrorCounter( )
        {
            super( "extendErrorCounter", null, null, true, Property.EMPTY_ARRAY );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void append( LogEvent event )
        {
            if ( event.getLevel( ).isMoreSpecificThan( Level.ERROR ) )
            {
                _listErrors.add( event.getMessage( ).getFormattedMessage( ) );
            }
        }

        /**
         * Gets the messages logged at ERROR level or above.
         *
         * @return the messages
         */
        List<String> getErrors( )
        {
            return _listErrors;
        }
    }
}
