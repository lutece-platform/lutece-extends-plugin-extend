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
package fr.paris.lutece.plugins.extend.service.content;

import fr.paris.lutece.portal.service.content.ContentPostProcessor;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.test.LuteceTestCase;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 * ExtendableContentPostProcessorTest
 *
 */
public class ExtendableContentPostProcessorTest extends LuteceTestCase
{
    private static final String HTML = "<html><head><base href=\"http://localhost:8080/lutece\"/></head><body><h1>Test</h1>@extendable[1,resourceType,hit,parameters]@<p>@extendable[2,resourceType-2,extenderType 2,{parameter_A = 2, parameter_B = 3}]@</p></body></html>";
    private static final String BEAN_CONTENT_POST_PROCESS = "extend.extendableContentPostProcessor";

    /**
     * Test process.
     */
    @Test
    public void testProcess( )
    {
        ContentPostProcessor processor = SpringContextService.getBean( BEAN_CONTENT_POST_PROCESS );

        if ( processor == null )
        {
            fail( "ContentPostProcessor " + BEAN_CONTENT_POST_PROCESS + " not initialized." );
        }

        String strOutput = processor.process( new MockHttpServletRequest( ), HTML );
        System.out.println( "Original HTML content :\n" + HTML );
        System.out.println( "Result HTML content :\n" + strOutput );
    }
}
