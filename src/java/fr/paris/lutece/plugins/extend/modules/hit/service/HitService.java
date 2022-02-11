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
package fr.paris.lutece.plugins.extend.modules.hit.service;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.business.HitHome;
import java.util.List;

/**
 *
 * HitService
 *
 */
public class HitService implements IHitService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.hitService";


    /**
     * {@inheritDoc}
     */
    @Override
    public void create( Hit hit )
    {
    	HitHome.create( hit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( Hit hit )
    {
    	HitHome.update( hit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove( int nIdExtender )
    {
    	HitHome.remove( nIdExtender  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByResource( String strIdResource, String strResourceType )
    {
    	HitHome.removeByResource( strIdResource, strResourceType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incrementHit( Hit hit )
    {
        hit.setNbHits( hit.getNbHits( ) + 1 );
        HitHome.update( hit );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit findByPrimaryKey( int nIdHit )
    {
        return HitHome.findByPrimaryKey( nIdHit  ).orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit findByParameters( String strIdExtendableResource, String strExtendableResourceType )
    {
        return HitHome.findByParameters( strIdExtendableResource, strExtendableResourceType ).orElse( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdMostHitedResources( String strExtendableResourceType, int nItemsOffset, int nMaxItemsNumber )
    {
        return HitHome.findIdMostHitedResources( strExtendableResourceType, nItemsOffset, nMaxItemsNumber );
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public List<Hit> findByResourceList(List<String> listIdExtendableResource, String strExtendableResourceType) 
	{

		return HitHome.findByResourceList( listIdExtendableResource, strExtendableResourceType );
	}
}
