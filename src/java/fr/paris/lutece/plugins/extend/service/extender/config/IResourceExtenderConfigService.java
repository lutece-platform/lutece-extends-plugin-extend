/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.extend.service.extender.config;

import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfig;


/**
 * 
 * IResourceExtenderConfigService
 * 
 */
public interface IResourceExtenderConfigService
{
    /**
     * Create a new config
     * @param config the config
     */
    void create( IExtenderConfig config );

    /**
     * Update a config
     * @param config the config
     */
    void update( IExtenderConfig config );

    /**
     * Removes.
     * 
     * @param nIdExtender the n id extender
     */
    void remove( int nIdExtender );

    /**
     * Find an extender
     * @param <T> The class of extender to return.
     * @param nIdExtender The id of the extender to return
     * @return The extender of this service with the given id, or null id none
     *         was found
     */
    <T> T find( int nIdExtender );

    /**
     * Find an extender associated with a resource and with a given extender
     * type
     * @param <T> The class of extender to return.
     * @param strExtenderType The type of the extender to get
     * @param strIdExtendableResource The id of the resource the extender must
     *            be associated with.
     * @param strExtendableResourceType The resource type the extender must be
     *            associated with.
     * @return The extender, or null id none was found
     */
    <T> T find( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType );
}
