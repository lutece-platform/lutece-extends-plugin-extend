/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.extend.business.extender.history;

import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.date.DateUtil;

import jakarta.inject.Inject;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * ResourceExtenderHistoryFilterTest
 */
public class ResourceExtenderHistoryFilterTest extends LuteceTestCase
{
    private static final String EXTENDER_TYPE = "hit";
    private static final String RESOURCE_TYPE = "EXTEND_FILTER_TEST";
    private static final String ID_RESOURCE = "1";
    private static final String USER_GUID = "test";

    @Inject
    private IResourceExtenderHistoryService _historyService;

    /**
     * Creates a history dated now.
     */
    @BeforeEach
    public void createHistory( )
    {
        _historyService.create( EXTENDER_TYPE, ID_RESOURCE, RESOURCE_TYPE, USER_GUID );
    }

    /**
     * Removes the history of the test resource.
     */
    @AfterEach
    public void removeHistory( )
    {
        _historyService.removeByResource( EXTENDER_TYPE, ID_RESOURCE, RESOURCE_TYPE );
    }

    /**
     * Formats a day in the short date format the filter parses.
     *
     * @param date
     *            the day
     * @return the formatted day
     */
    private static String format( LocalDate date )
    {
        return DateUtil.getDateString( Date.valueOf( date ), I18nService.getDefaultLocale( ) );
    }

    /**
     * Searches the test resource created between two days.
     *
     * @param dateBegin
     *            the first day
     * @param dateEnd
     *            the last day
     * @return the number of histories found
     */
    private int search( LocalDate dateBegin, LocalDate dateEnd )
    {
        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
        filter.setExtendableResourceType( RESOURCE_TYPE );
        filter.setDateCreationBegin( format( dateBegin ) );
        filter.setDateCreationEnd( format( dateEnd ) );

        return _historyService.findByFilter( filter ).size( );
    }

    /**
     * The end date includes its whole day.
     */
    @Test
    public void testEndDateIncludesItsDay( )
    {
        assertEquals( 1, search( LocalDate.now( ), LocalDate.now( ) ) );
    }

    /**
     * A history after the end date is not found.
     */
    @Test
    public void testEndDateExcludesTheNextDay( )
    {
        assertEquals( 0, search( LocalDate.now( ).minusDays( 1 ), LocalDate.now( ).minusDays( 1 ) ) );
    }
}
