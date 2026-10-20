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
package fr.paris.lutece.plugins.extend.web.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.core.TemplateHTMLOutputModel;

/**
 * The HTML an extender component builds is put in the models of plugin-extend as markup (LUT-33153), and left out when
 * blank so that the page shows its empty state.
 */
public class ResourceExtenderComponentManagerHtmlTest
{
    private static final String MARK = "resourceExtenderConfig";

    /**
     * A fragment is put as HTML markup, unchanged.
     *
     * @throws Exception
     *             if the markup cannot be read
     */
    @Test
    public void testFragmentIsMarkup( ) throws Exception
    {
        Map<String, Object> model = new HashMap<>( );
        ResourceExtenderComponentManager.putHtml( model, MARK, "<strong>12</strong>" );

        TemplateHTMLOutputModel markup = assertInstanceOf( TemplateHTMLOutputModel.class, model.get( MARK ) );
        assertEquals( "<strong>12</strong>", markup.getOutputFormat( ).getMarkupString( markup ) );
    }

    /**
     * A null or blank fragment is left out of the model.
     */
    @Test
    public void testBlankFragmentIsLeftOut( )
    {
        Map<String, Object> model = new HashMap<>( );
        ResourceExtenderComponentManager.putHtml( model, MARK, null );
        ResourceExtenderComponentManager.putHtml( model, MARK, " \n" );

        assertFalse( model.containsKey( MARK ) );
    }
}
