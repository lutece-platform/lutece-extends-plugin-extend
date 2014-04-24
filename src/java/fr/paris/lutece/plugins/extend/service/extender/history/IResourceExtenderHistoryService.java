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
package fr.paris.lutece.plugins.extend.service.extender.history;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * IResourceExtenderHistoryService.
 */
public interface IResourceExtenderHistoryService
{
    /**
     * Delete.
     *
     * @param nIdResourceExtenderHistory the n id history
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void remove( int nIdResourceExtenderHistory );

    /**
     * Delete.
     *
     * @param strExtenderType the str extender type
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void removeByResource( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Creates the history from the HTTP request.
     *
     * @param strExtenderType the str extender type
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param request the request
     */
    void create( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType,
        HttpServletRequest request );

    /**
     * Insert.
     *
     * @param history the history
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void create( ResourceExtenderHistory history );

    /**
     * Load.
     *
     * @param nIdResourceExtenderHistory the n id history
     * @return the history
     */
    ResourceExtenderHistory findByPrimary( int nIdResourceExtenderHistory );

    /**
     * Load by filter.
     *
     * @param filter the filter
     * @return the list
     */
    List<ResourceExtenderHistory> findByFilter( ResourceExtenderHistoryFilter filter );
}
