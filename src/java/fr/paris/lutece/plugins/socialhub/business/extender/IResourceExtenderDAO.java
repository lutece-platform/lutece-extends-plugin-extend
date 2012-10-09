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
package fr.paris.lutece.plugins.socialhub.business.extender;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * IResourceExtenderDAO.
 */
public interface IResourceExtenderDAO
{
    /**
     * Insert.
     *
     * @param extender the extender
     * @param plugin the plugin
     */
    void insert( ResourceExtenderDTO extender, Plugin plugin );

    /**
     * Store.
     *
     * @param extender the extender
     * @param plugin the plugin
     */
    void store( ResourceExtenderDTO extender, Plugin plugin );

    /**
     * Delete.
     *
     * @param nIdExtender the n id extender
     * @param plugin the plugin
     */
    void delete( int nIdExtender, Plugin plugin );

    /**
     * Load.
     *
     * @param nIdExtender the n id extender
     * @param plugin the plugin
     * @return the i resource extender
     */
    ResourceExtenderDTO load( int nIdExtender, Plugin plugin );

    /**
     * Load all.
     *
     * @param plugin the plugin
     * @return the list
     */
    List<ResourceExtenderDTO> loadAll( Plugin plugin );

    /**
     * Load by filter.
     *
     * @param filter the filter
     * @param plugin the plugin
     * @return the list
     */
    List<ResourceExtenderDTO> loadByFilter( ResourceExtenderDTOFilter filter, Plugin plugin );

    /**
     * Find ids by filter.
     *
     * @param filter the filter
     * @param plugin the plugin
     * @return the list
     */
    List<Integer> loadIdsByFilter( ResourceExtenderDTOFilter filter, Plugin plugin );

    /**
     * Find by list ids.
     *
     * @param listIds the list ids
     * @param plugin the plugin
     * @return the list
     */
    List<ResourceExtenderDTO> loadByListIds( List<Integer> listIds, Plugin plugin );
}
