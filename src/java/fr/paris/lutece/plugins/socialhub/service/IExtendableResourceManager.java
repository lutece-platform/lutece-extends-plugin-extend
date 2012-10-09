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
package fr.paris.lutece.plugins.socialhub.service;

import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;

import java.util.List;


/**
 *
 * IExtendableResourceManager
 *
 */
public interface IExtendableResourceManager
{
    /**
     * Gets the extendable resource services.
     *
     * @return the extendable resource services
     */
    List<IExtendableResourceService> getExtendableResourceServices(  );

    /**
     * Gets the extendable resource service.
     *
     * @param strResourceType the str resource type
     * @return the extendable resource service
     */
    IExtendableResourceService getExtendableResourceService( String strResourceType );

    /**
     * Gets the resource.
     *
     * @param strIdResource the str id resource
     * @param strResourceType the str resource type
     * @return the resource
     */
    IExtendableResource getResource( String strIdResource, String strResourceType );

    /**
     * Check if the user must be redirected to the default resource extender creation page
     * or not.
     * <br />
     * To be redirected, the following conditions must be set :
     * <ul>
     * <li>The resource service for the given resource type is not found</li>
     * <li>There are no existing resource for the given id resource</li>
     * </ul>
     *
     * @param strIdResource the str id resource
     * @param strResourceType the str resource type
     * @return true, if successful
     */
    boolean useDefaultExtendableResourceService( String strIdResource, String strResourceType );
}
