/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.util.ExtendErrorException;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IResourceExtenderComponent
 *
 */
public interface IResourceExtenderComponent
{
    /**
     * Gets the resource extender.
     *
     * @return the resource extender
     */
    IResourceExtender getResourceExtender(  );

    /**
     * Sets the resource extender.
     *
     * @param extender the new resource extender
     */
    void setResourceExtender( IResourceExtender extender );

    /**
     * Add to the the XML String additional datas.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strParameters the str parameters
     * @param strXml The xml string use by stylesheet
     */
    void buildXmlAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        StringBuffer strXml );

    /**
     * Add datas to the model use by document template.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strParameters the str parameters
     * @param request The HTTP Request
     * @return the page add on
     */
    String getPageAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request );

    /**
     * Gets the config html.
     *
     * @param resourceExtender the resource extender
     * @param locale the locale
     * @param request the request
     * @return the config html
     */
    String getConfigHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request );

    /**
     * Gets the info html.
     *
     * @param resourceExtender the resource extender
     * @param locale the locale
     * @param request the request
     * @return the info html
     */
    String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request );

    /**
     * Gets the config.
     *
     * @param nIdExtender the n id extender
     * @return the config
     */
    IExtenderConfig getConfig( int nIdExtender );

    /**
     * Do save config.
     * 
     * @param request the request
     * @param config the config
     * @throws ExtendErrorException the extend error exception
     */
    void doSaveConfig( HttpServletRequest request, IExtenderConfig config )
        throws ExtendErrorException;
}
