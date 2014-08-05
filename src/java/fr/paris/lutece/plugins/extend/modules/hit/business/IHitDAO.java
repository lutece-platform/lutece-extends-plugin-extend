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
package fr.paris.lutece.plugins.extend.modules.hit.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * IInteractionHitDAO.
 */
public interface IHitDAO
{
    /**
     * Insert.
     *
     * @param hit the hit
     * @param plugin the plugin
     */
    void insert( Hit hit, Plugin plugin );

    /**
     * Update.
     *
     * @param hit the hit
     * @param plugin the plugin
     */
    void store( Hit hit, Plugin plugin );

    /**
     * Delete.
     *
     * @param nIdHit the n id hit
     * @param plugin the plugin
     */
    void delete( int nIdHit, Plugin plugin );

    /**
     * Delete hits of a given resource
     *
     * @param strIdResource the id of the resource, or a wildcare to remove all
     * @param strResourceType The resource type to delete
     * @param plugin the plugin
     */
    void deleteByResource( String strIdResource, String strResourceType, Plugin plugin );

    /**
     * Load.
     *
     * @param nIdHit the n id hit
     * @param plugin the plugin
     * @return the hit
     */
    Hit load( int nIdHit, Plugin plugin );

    /**
     * Load by id extender.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param plugin the plugin
     * @return the hit
     */
    Hit loadByParameters( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin );

    /**
     * Get the ids of resources ordered by their number of hits
     * @param strExtendableResourceType The type of resources to consider
     * @param nItemsOffset The offset of the items to get, or 0 to get items
     *            from the first one
     * @param nMaxItemsNumber The maximum number of items to return, or 0 to get
     *            every items
     * @param plugin the plugin
     * @return The list of ids of resources ordered by the number hits
     */
    List<Integer> findIdMostHitedResources( String strExtendableResourceType, int nItemsOffset, int nMaxItemsNumber,
        Plugin plugin );
}
