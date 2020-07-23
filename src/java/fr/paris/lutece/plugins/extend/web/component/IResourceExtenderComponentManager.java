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
package fr.paris.lutece.plugins.extend.web.component;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * IResourceExtenderComponentManager.
 */
public interface IResourceExtenderComponentManager
{
    /**
     * Gets the resource extender component.
     *
     * @param strExtenderType
     *            the str extender type
     * @return the resource extender component
     */
    IResourceExtenderComponent getResourceExtenderComponent( String strExtenderType );

    /**
     * Builds the xml add on.
     *
     * @param resourceExtender
     *            the resource extender
     * @param strXml
     *            the str xml
     */
    void buildXmlAddOn( ResourceExtenderDTO resourceExtender, StringBuffer strXml );

    /**
     * Gets the page add on.
     *
     * @param resourceExtender
     *            the resource extender
     * @param request
     *            the request
     * @return the page add on
     */
    String getPageAddOn( ResourceExtenderDTO resourceExtender, HttpServletRequest request );

    /**
     * Gets the config html.
     *
     * @param resourceExtender
     *            the resource extender
     * @param locale
     *            the locale
     * @param request
     *            the request
     * @return the config html
     */
    String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request );

    /**
     * Gets the default config html of an extender type.
     *
     * @param strExtenderType
     *            the resource extender type
     * @param locale
     *            the locale
     * @param request
     *            the request
     * @return the config html
     */
    String getDefaultConfigHtml( String strExtenderType, Locale locale, HttpServletRequest request );

    /**
     * Gets the info html.
     *
     * @param resourceExtender
     *            the resource extender
     * @param locale
     *            the locale
     * @param request
     *            the request
     * @return the info html
     */
    String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request );

    /**
     * Gets the history html.
     *
     * @param resourceExtender
     *            the resource extender
     * @param locale
     *            the locale
     * @param request
     *            the request
     * @return the history html
     */
    String getHistoryHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request );

    /**
     * Do save config.
     *
     * @param resourceExtender
     *            the resource extender
     * @param request
     *            the request
     * @throws ExtendErrorException
     *             the extend error exception
     */
    void doSaveConfig( ResourceExtenderDTO resourceExtender, HttpServletRequest request ) throws ExtendErrorException;
}
