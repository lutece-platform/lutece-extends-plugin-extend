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
package fr.paris.lutece.plugins.extend.modules.hit.service;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;

import java.util.List;


/**
 * 
 * IHitService
 * 
 */
public interface IHitService
{
    /**
     * Creates the a hit extender.
     * 
     * @param hit the hit
     */
    void create( Hit hit );

    /**
     * Update a hit extender.
     * 
     * @param hit the hit
     */
    void update( Hit hit );

    /**
     * Removes a hit extender.
     * 
     * @param nIdHit the n id hit
     */
    void remove( int nIdHit );

    /**
     * Removes a hit extender by id resource and resource type.
     * @param strIdResource The id of the resource to remove
     * @param strResourceType The type of the resource to remove
     */
    void removeByResource( String strIdResource, String strResourceType );

    /**
     * Increment hit.
     * 
     * @param hit the hit
     */
    void incrementHit( Hit hit );

    /**
     * Find.
     * 
     * @param nIdExtender the n id extender
     * @return the hit
     */
    Hit findByPrimaryKey( int nIdExtender );

    /**
     * Find by id extender.
     * 
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the hit
     */
    Hit findByParameters( String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Get the ids of resources ordered by their number of hits
     * @param strExtendableResourceType The type of resources to consider
     * @param nItemsOffset The offset of the items to get, or 0 to get items
     *            from the first one
     * @param nMaxItemsNumber The maximum number of items to return, or 0 to get
     *            every items
     * @return The list of ids of resources ordered by the number hits
     */
    public List<Integer> findIdMostHitedResources( String strExtendableResourceType, int nItemsOffset,
            int nMaxItemsNumber );
}
