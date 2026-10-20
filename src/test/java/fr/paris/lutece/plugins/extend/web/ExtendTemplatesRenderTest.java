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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the plugin-extend pages that hold HTML built by another component, with HTML auto-escaping (LUT-33153): the
 * configuration form and the information block of an extender module, and the body of a content page, marked with
 * HtmlMarkup by plugin-extend, keep their tags; a fragment left as a String prints as text; the data around them is
 * escaped; the return URL goes through safeUrl(); the front office script writes its values JavaScript-escaped. The core
 * macros the templates call are replaced by stand-ins that write their arguments (escaped) and their nested content; the
 * plugin's extend_commons.html is auto-included as the plugin descriptor declares it: only the FreeMarker engine is
 * needed.
 */
public class ExtendTemplatesRenderTest
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String COMMONS = "admin/plugins/extend/extend_commons.html";
    private static final String TEMPLATE_CONFIG = "admin/plugins/extend/resource_extender_config.html";
    private static final String TEMPLATE_DEFAULT_CONFIG = "admin/plugins/extend/resource_extender_default_config.html";
    private static final String TEMPLATE_INFO = "admin/plugins/extend/resource_extender_info.html";
    private static final String TEMPLATE_HISTORY = "admin/plugins/extend/resource_extender_history.html";
    private static final String TEMPLATE_CONTENT = "admin/plugins/extend/resource_extender_content.html";
    private static final String TEMPLATE_POST_PROCESSOR = "skin/plugins/extend/extendable_content_post_processor.html";
    private static final String DEFAULT_URL = "jsp/admin/plugins/extend/ManageResourceExtendersByResource.jsp";

    /**
     * Stand-ins of the core macros and functions the templates call. tform writes its params, which must be a map; safeUrl
     * keeps a relative or http(s) URL and returns the fallback for any other scheme, as the core function does.
     */
    private static final String MACROS = "macros.ftl";
    private static final String MACROS_SOURCE = "<#macro pageContainer rest...><#nested></#macro><#macro pageColumn rest...><#nested></#macro>"
            + "<#macro pageHeader title='' rest...><h1>${title}</h1><#nested></#macro>"
            + "<#macro tform action='' params={} rest...><form action=\"${action}\"<#list params as key, param> ${key}=\"${param}\"</#list>><#nested></form></#macro>"
            + "<#macro input name='' value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro button name='' rest...><button name=\"${name}\"></button></#macro>"
            + "<#macro formGroup rest...><div class=\"form-group\"><#nested></div></#macro>"
            + "<#macro empty title='' rest...><div class=\"empty\">${title}</div></#macro>"
            + "<#macro table caption='' rest...><table><#if caption?has_content><caption>${caption}</caption></#if><#nested></table></#macro>"
            + "<#macro tr rest...><tr><#nested></tr></#macro><#macro th rest...><th><#nested></th></#macro>"
            + "<#macro td rest...><td><#nested></td></#macro><#macro tableHeadBodySeparator rest...></#macro>"
            + "<#macro paginationAdmin rest...></#macro><#macro btnGroup rest...><#nested></#macro>"
            + "<#macro row rest...><#nested></#macro><#macro columns rest...><#nested></#macro>"
            + "<#macro inputGroup rest...><#nested></#macro><#macro inputGroupItem rest...><#nested></#macro>"
            + "<#macro select name='' rest...><select name=\"${name}\"></select></#macro>"
            + "<#function safeUrl url fallback='#'><#if url?matches( '(?i)^\\\\s*(https?:|/|[^:/?#]*([/?#]|$)).*', 's' )><#return url /></#if><#return fallback /></#function>";

    private static final String CONFIG = "<div class=\"mb-3\"><label for=\"idMailingList\">Mailing list</label><select name=\"idMailingList\">"
            + "<option value=\"1\">List &lt;one&gt;</option></select></div>";
    private static final String INFO = "Number of views : <strong>12</strong>";
    private static final String CONTENT = "<div class=\"container-fluid\"><form method=\"post\" action=\"jsp/admin/plugins/extend/ManageResourceExtendersByResource.jsp\">"
            + "<input type=\"hidden\" name=\"action\" value=\"modifyExtenderConfig\"></form></div>";
    private static final String NAME = "Comments <b>x</b> \"q\"";
    private static final String NAME_ESCAPED = "Comments &lt;b&gt;x&lt;/b&gt; &quot;q&quot;";

    /**
     * Renders a template of the plugin with the core configuration of the output format and the plugin's macro file.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the HTML
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, Object> model ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                macros, new FileTemplateLoader( new File( TEMPLATES_ROOT ) )
        } ) );
        cfg.addAutoInclude( MACROS );
        cfg.addAutoInclude( COMMONS );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * An extender of a resource, with a name holding markup characters.
     *
     * @return the extender
     */
    private static ResourceExtenderDTO extender( )
    {
        ResourceExtenderDTO extender = new ResourceExtenderDTO( );
        extender.setIdExtender( 7 );
        extender.setName( NAME );
        extender.setExtenderType( "comment" );
        extender.setExtendableResourceType( "PAGE" );
        extender.setIdExtendableResource( "42" );

        return extender;
    }

    /**
     * The model of a configuration or information page.
     *
     * @param strMark
     *            the mark of the fragment
     * @param fragment
     *            the fragment, or null to leave it out as plugin-extend does for a blank one
     * @param strFromUrl
     *            the return URL, or null
     * @return the model
     */
    private static Map<String, Object> model( String strMark, Object fragment, String strFromUrl )
    {
        Map<String, Object> model = new HashMap<>( );
        model.put( "resourceExtender", extender( ) );
        if ( fragment != null )
        {
            model.put( strMark, fragment );
        }
        if ( strFromUrl != null )
        {
            model.put( "from_url", strFromUrl );
        }

        return model;
    }

    /**
     * The configuration page writes the module's form as HTML and targets the top window through a params map.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testConfigKeepsTheModuleForm( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, model( "resourceExtenderConfig", HtmlMarkup.of( CONFIG ), "jsp/admin/Page.jsp?a=1&b=2" ) );

        assertTrue( strHtml.contains( CONFIG ), strHtml );
        assertTrue( strHtml.contains( " target=\"_top\"" ), strHtml );
        assertTrue( strHtml.contains( "value=\"jsp/admin/Page.jsp?a=1&amp;b=2\"" ), strHtml );
        assertFalse( strHtml.contains( "class=\"empty\"" ), strHtml );
    }

    /**
     * A configuration form left as a String is escaped: the reason plugin-extend marks it.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testAStringConfigIsEscaped( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, model( "resourceExtenderConfig", CONFIG, null ) );

        assertFalse( strHtml.contains( CONFIG ), strHtml );
        assertTrue( strHtml.contains( "&lt;div class=&quot;mb-3&quot;&gt;" ), strHtml );
    }

    /**
     * Without a configuration form, the page shows its empty state.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNoConfigShowsTheEmptyState( ) throws Exception
    {
        String strHtml = render( TEMPLATE_CONFIG, model( "resourceExtenderConfig", null, null ) );

        assertTrue( strHtml.contains( "<div class=\"empty\">#i18n{extend.resource_extender_config.noConfig}</div>" ), strHtml );
        assertFalse( strHtml.contains( "modifyExtenderConfig" ), strHtml );
    }

    /**
     * The default configuration page writes the module's form as HTML, and the type selector captured as box tools.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testDefaultConfigKeepsTheModuleFormAndTheTools( ) throws Exception
    {
        Map<String, Object> model = model( "resourceExtenderConfig", HtmlMarkup.of( CONFIG ), null );
        model.put( "extenderTypeModifyConfig", "comment" );
        model.put( "configurableExtenderTypes", List.of( Map.of( "code", "comment", "name", NAME ) ) );
        String strHtml = render( TEMPLATE_DEFAULT_CONFIG, model );

        assertTrue( strHtml.contains( CONFIG ), strHtml );
        assertTrue( strHtml.contains( "<select name=\"extenderTypeModifyConfig\"></select>" ), strHtml );
        assertTrue( strHtml.contains( "<form action=\"" + DEFAULT_URL + "?view=manageResourceExtendersByResourceType\" target=\"_top\">" ), strHtml );
    }

    /**
     * The information page writes the module's block as HTML, escapes the extender name, and sends the back button to
     * the return URL.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testInfoKeepsTheModuleBlock( ) throws Exception
    {
        String strHtml = render( TEMPLATE_INFO, model( "resourceExtenderInfo", HtmlMarkup.of( INFO ), "jsp/admin/Page.jsp?a=1&b=2" ) );

        assertTrue( strHtml.contains( INFO ), strHtml );
        assertTrue( strHtml.contains( "(" + NAME_ESCAPED + ")" ), strHtml );
        assertFalse( strHtml.contains( NAME ), strHtml );
        assertTrue( strHtml.contains( "<form action=\"jsp/admin/Page.jsp?a=1&amp;b=2\" target=\"_top\">" ), strHtml );
    }

    /**
     * A return URL with another scheme than http(s) is replaced by the management page.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testInfoRefusesAJavaScriptReturnUrl( ) throws Exception
    {
        String strHtml = render( TEMPLATE_INFO, model( "resourceExtenderInfo", HtmlMarkup.of( INFO ), "javascript:alert(1)" ) );

        assertFalse( strHtml.contains( "javascript:" ), strHtml );
        assertTrue( strHtml.contains( "<form action=\"" + DEFAULT_URL + "\" target=\"_top\">" ), strHtml );
    }

    /**
     * The history page escapes the history rows and writes its caption with the macro parameter.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testHistoryEscapesTheRows( ) throws Exception
    {
        Map<String, Object> model = model( null, null, "javascript:alert(1)" );
        model.put( "listHistories", List.of( Map.of( "ipAddress", "127.0.0.1", "userGuid", NAME, "dateCreation", Optional.empty( ) ) ) );
        String strHtml = render( TEMPLATE_HISTORY, model );

        assertTrue( strHtml.contains( "<caption>#i18n{extend.resource_extender_history.pageTitle}</caption>" ), strHtml );
        assertTrue( strHtml.contains( "<td>" + NAME_ESCAPED + "</td>" ), strHtml );
        assertFalse( strHtml.contains( "javascript:" ), strHtml );
    }

    /**
     * The content page writes the page another component rendered as HTML.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testContentKeepsThePage( ) throws Exception
    {
        assertTrue( render( TEMPLATE_CONTENT, Map.of( "resourceExtenderContent", HtmlMarkup.of( CONTENT ) ) ).contains( CONTENT ) );
        assertFalse( render( TEMPLATE_CONTENT, Map.of( "resourceExtenderContent", CONTENT ) ).contains( CONTENT ) );
    }

    /**
     * The front office script reads the marker pattern and the base URL from JavaScript strings: a quote or a closing
     * script tag in the configuration does not end the string nor the script.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testPostProcessorScriptEscapesItsValues( ) throws Exception
    {
        String strHtml = render( TEMPLATE_POST_PROCESSOR,
                Map.of( "extendRegexPattern", "@Extender\\[([^\\]@]*)\\]@'</script>", "baseUrl", "https://site.example/?a='b'" ) );

        assertTrue( strHtml.contains( "new RegExp('@Extender\\\\[([^\\\\]@]*)\\\\]@\\'<\\/script>')" ), strHtml );
        assertTrue( strHtml.contains( "fetch('https://site.example/?a=\\'b\\'servlet/plugins/extend?'" ), strHtml );
        assertFalse( strHtml.contains( "&#39;" ), strHtml );
    }
}
