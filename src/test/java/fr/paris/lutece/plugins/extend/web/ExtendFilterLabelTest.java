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
package fr.paris.lutece.plugins.extend.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * The "all" item of the filters of ManageResourceExtendersByResource (extend.labelAll) is put in a ReferenceList by
 * ResourceExtenderSearchFields and printed by the templates with auto-escaping (LUT-33153): the message holds the plain
 * text, written once as &amp;lt;Tous&amp;gt; by the escaping. Written as &amp;lt;Tous&amp;gt; in the bundle, it was
 * escaped a second time and the page showed "&amp;lt;Tous&amp;gt;".
 */
public class ExtendFilterLabelTest
{
    private static final String BUNDLES = "src/java/fr/paris/lutece/plugins/extend/resources/";

    private static Properties bundle( String strFile ) throws IOException
    {
        Properties properties = new Properties( );
        try ( Reader reader = Files.newBufferedReader( Path.of( BUNDLES + strFile ), StandardCharsets.ISO_8859_1 ) )
        {
            properties.load( reader );
        }
        return properties;
    }

    /**
     * The message is plain text, escaped once where the option is written.
     *
     * @throws Exception
     *             if a bundle cannot be read or the option cannot be rendered
     */
    @Test
    public void testLabelAllIsPlainTextEscapedOnce( ) throws Exception
    {
        assertEquals( "<All>", bundle( "extend_messages.properties" ).getProperty( "labelAll" ) );
        assertEquals( "<Tous>", bundle( "extend_messages_fr.properties" ).getProperty( "labelAll" ) );

        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        StringWriter writer = new StringWriter( );
        new Template( "option.html", new StringReader( "<option value=\"\">${name}</option>" ), cfg ).process(
                Map.of( "name", bundle( "extend_messages_fr.properties" ).getProperty( "labelAll" ) ), writer );
        assertEquals( "<option value=\"\">&lt;Tous&gt;</option>", writer.toString( ) );
    }

    /**
     * No template prints the message through #i18n{}, which is replaced after the rendering, without escaping: there the raw
     * message would be markup.
     *
     * @throws Exception
     *             if a template cannot be read
     */
    @Test
    public void testLabelAllIsNotAnI18nMarkerOfTheTemplates( ) throws Exception
    {
        try ( Stream<Path> templates = Files.walk( Path.of( "webapp/WEB-INF/templates" ) ) )
        {
            for ( Path template : (Iterable<Path>) templates.filter( Files::isRegularFile )::iterator )
            {
                assertFalse( Files.readString( template, StandardCharsets.UTF_8 ).contains( "#i18n{extend.labelAll}" ), template.toString( ) );
            }
        }
    }
}
