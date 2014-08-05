/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * IResourceExtender.
 */
public interface IResourceExtender
{
    /**
     * Gets the key.
     *
     * @return the key
     */
    String getKey(  );

    /**
     * Sets the key.
     *
     * @param strKey the new key
     */
    void setKey( String strKey );

    /**
    * Gets the name.
    *
    * @param locale the locale
    * @return the name
    */
    String getTitle( Locale locale );

    /**
     * Sets the i18n title key.
     *
     * @param strI18nTitleKey the new i18n title key
     */
    void setI18nTitleKey( String strI18nTitleKey );

    /**
     * Checks if is invoked.
     *
     * @param strExtenderType the str extender type
     * @return true, if is invoked
     */
    boolean isInvoked( String strExtenderType );

    /**
     * Gets the content.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strParameters the str parameters
     * @param request the request
     * @return the content
     */
    String getContent( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request );

    /**
     * Do create resource add on.
     *
     * @param resourceExtender the resource extender
     */
    void doCreateResourceAddOn( ResourceExtenderDTO resourceExtender );

    /**
     * Do delete resource add on.
     *
     * @param resourceExtender the resource extender. If the extender has a
     *            configuration, then the configuration is also removed unless
     *            the extender id is equals to 0.
     */
    void doDeleteResourceAddOn( ResourceExtenderDTO resourceExtender );

    /**
     * Checks if is config required.
     *
     * @return true, if is config required
     */
    boolean isConfigRequired(  );

    /**
     * Checks if is history enable.
     *
     * @return true, if is history enable
     */
    boolean isHistoryEnable(  );
}
