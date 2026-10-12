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
package fr.paris.lutece.plugins.extend.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

import org.junit.jupiter.api.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/**
 * Renders templates with extend_commons.html auto-included, as the plugin declares it as a FreeMarker macro file.
 */
public class ExtendCommonsTemplateTest
{
    private static final String TEMPLATES_DIR = "webapp/WEB-INF/templates";
    private static final String COMMONS = "/admin/plugins/extend/extend_commons.html";
    private static final String TEMPLATE = "module.html";

    /**
     * A model variable named like a type label shows its value, not a function of the common template.
     *
     * @throws Exception
     *             if the template cannot be rendered
     */
    @Test
    public void testModuleModelKeepsItsTypeLabelVariables( ) throws Exception
    {
        String strOutput = render( "${extenderTypeLabel}|${resourceTypeLabel}",
                Map.of( "extenderTypeLabel", "Feedback", "resourceTypeLabel", "Page" ) );

        assertEquals( "Feedback|Page", strOutput );
    }

    /**
     * The label functions of the common template name a known type and keep the code of an unknown one.
     *
     * @throws Exception
     *             if the template cannot be rendered
     */
    @Test
    public void testTypeLabelFunctions( ) throws Exception
    {
        String strOutput = render(
                "${extendExtenderTypeLabel( 'hit' )}|${extendExtenderTypeLabel( 'none' )}|${extendResourceTypeLabel( 'PAGE' )}|${extendResourceTypeLabel( 'NONE' )}",
                Map.of( "extenderTypeLabels", Map.of( "hit", "Hit" ), "resourceTypeLabels", Map.of( "PAGE", "Page" ) ) );

        assertEquals( "Hit|none|Page|NONE", strOutput );
    }

    /**
     * Renders a template source with the common template auto-included.
     *
     * @param strSource
     *            the template source
     * @param model
     *            the model
     * @return the output
     * @throws Exception
     *             if the template cannot be rendered
     */
    private static String render( String strSource, Map<String, Object> model ) throws Exception
    {
        StringTemplateLoader stringLoader = new StringTemplateLoader( );
        stringLoader.putTemplate( TEMPLATE, strSource );

        Configuration cfg = new Configuration( Configuration.VERSION_2_3_28 );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                stringLoader, new FileTemplateLoader( new File( TEMPLATES_DIR ) )
        } ) );
        cfg.addAutoInclude( COMMONS );

        StringWriter writer = new StringWriter( );
        cfg.getTemplate( TEMPLATE ).process( model, writer );

        return writer.toString( );
    }
}
