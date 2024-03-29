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
package fr.paris.lutece.plugins.extend.business.extender.history;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;

/**
 *
 * IResourceExtenderHistoryDAO
 *
 */
public interface IResourceExtenderHistoryDAO
{
    /**
     * Delete.
     *
     * @param nIdHistory
     *            the n id history
     * @param plugin
     *            the plugin
     */
    void delete( int nIdHistory, Plugin plugin );

    /**
     * Delete.
     *
     * @param strExtenderType
     *            the str extender type
     * @param strIdExtendableResource
     *            the str id extendable resource
     * @param strExtendableResourceType
     *            the str extendable resource type
     * @param plugin
     *            the plugin
     */
    void deleteByResource( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Insert.
     *
     * @param history
     *            the history
     * @param plugin
     *            the plugin
     */
    void insert( ResourceExtenderHistory history, Plugin plugin );

    /**
     * Load.
     *
     * @param nIdHistory
     *            the n id history
     * @param plugin
     *            the plugin
     * @return the history
     */
    ResourceExtenderHistory load( int nIdHistory, Plugin plugin );

    /**
     * Load by filter.
     *
     * @param filter
     *            the filter
     * @param plugin
     *            the plugin
     * @return the list
     */
    List<ResourceExtenderHistory> loadByFilter( ResourceExtenderHistoryFilter filter, Plugin plugin );
    /**
     * Load by list id resources and extender type
     * @param listIdResource
     * 			the ids resource list
     * @param strExtendableResourceType
     * 			the extender resource type
     * @param strExtenderType
     * 			the extender type
     * @param plugin
     * 			the plugin
     * @return	list of ResourceExtenderHistory
     */
    List<ResourceExtenderHistory> loadByListIdResource( List<String> listIdResource, String strExtendableResourceType, String strExtenderType,  Plugin plugin );

}
