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
package fr.paris.lutece.plugins.extend.modules.hit.business;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

/**
 *
 * InteractionHitDAO
 *
 */
public class HitDAO implements IHitDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_hit ) FROM extend_extender_hit ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_extender_hit (id_hit, id_resource, resource_type, nb_hits) VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_extender_hit SET id_resource = ?, resource_type = ?, nb_hits = ? WHERE id_hit = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_extender_hit WHERE id_hit = ? ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = " DELETE FROM extend_extender_hit WHERE resource_type = ? ";
    private static final String SQL_QUERY_FILTER_BY_ID_RESOURCE = " AND id_resource = ? ";
    private static final String SQL_QUERY_SELECT_ID_MOST_HITED_RESOURCES = " SELECT DISTINCT(id_resource) FROM extend_extender_hit WHERE resource_type = ? ORDER BY nb_hits  ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_hit, id_resource, resource_type, nb_hits FROM extend_extender_hit ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_hit = ? ";
    private static final String SQL_QUERY_SELECT_BY_PARAMETERS = SQL_QUERY_SELECT_ALL + " WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT_BY_ID_RESOURCE_LIST = SQL_QUERY_SELECT_ALL + " WHERE resource_type = ? AND id_resource IN ( ";
    private static final String SQL_FILTER_ID_LIST_END = " ) ";
    private static final String SQL_LIMIT = " LIMIT ";
    private static final String CONSTANT_COMMA = ",";
    private static final String CONSTANT_QUESTION_MARK = "?";

    /**
     * New primary key
     * 
     * @param plugin
     *            the plugin
     * @return a new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( Hit hit, Plugin plugin )
    {
        hit.setIdHit( newPrimaryKey( plugin ) );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, hit.getIdHit( ) );
        daoUtil.setString( nIndex++, hit.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, hit.getExtendableResourceType( ) );
        daoUtil.setInt( nIndex, hit.getNbHits( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Hit hit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setString( nIndex++, hit.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, hit.getExtendableResourceType( ) );
        daoUtil.setInt( nIndex++, hit.getNbHits( ) );

        daoUtil.setInt( nIndex, hit.getIdHit( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdHit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setInt( 1, nIdHit );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByResource( String strIdResource, String strResourceType, Plugin plugin )
    {
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_BY_RESOURCE );

        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdResource ) )
        {
            sbSql.append( SQL_QUERY_FILTER_BY_ID_RESOURCE );
        }

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );

        int nIndex = 0;
        daoUtil.setString( ++nIndex, strResourceType );

        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdResource ) )
        {
            daoUtil.setString( ++nIndex, strIdResource );
        }

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit load( int nIdHit, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdHit );
        daoUtil.executeQuery( );

        Hit hit = null;

        int nIndex = 1;

        if ( daoUtil.next( ) )
        {
            hit = new Hit( );
            hit.setIdHit( daoUtil.getInt( nIndex++ ) );
            hit.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            hit.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            hit.setNbHits( daoUtil.getInt( nIndex ) );
        }

        daoUtil.free( );

        return hit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hit loadByParameters( String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PARAMETERS, plugin );
        int nIndex = 1;
        daoUtil.setString( nIndex++, strIdExtendableResource );
        daoUtil.setString( nIndex, strExtendableResourceType );
        daoUtil.executeQuery( );

        Hit hit = null;

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            hit = new Hit( );
            hit.setIdHit( daoUtil.getInt( nIndex++ ) );
            hit.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            hit.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            hit.setNbHits( daoUtil.getInt( nIndex ) );
        }

        daoUtil.free( );

        return hit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> findIdMostHitedResources( String strExtendableResourceType, int nItemsOffset, int nMaxItemsNumber, Plugin plugin )
    {
        List<Integer> listIds;

        if ( nMaxItemsNumber > 0 )
        {
            listIds = new ArrayList<Integer>( nMaxItemsNumber );
        }
        else
        {
            listIds = new ArrayList<Integer>( );
        }

        StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ID_MOST_HITED_RESOURCES );

        if ( nMaxItemsNumber > 0 )
        {
            sbSQL.append( SQL_LIMIT );

            if ( nItemsOffset > 0 )
            {
                sbSQL.append( CONSTANT_QUESTION_MARK ).append( CONSTANT_COMMA );
            }

            sbSQL.append( CONSTANT_QUESTION_MARK );
        }

        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
        daoUtil.setString( nIndex++, strExtendableResourceType );

        if ( nMaxItemsNumber > 0 )
        {
            if ( nItemsOffset > 0 )
            {
                daoUtil.setInt( nIndex++, nItemsOffset );
            }

            daoUtil.setInt( nIndex, nMaxItemsNumber );
        }

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIds.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listIds;
    }

	@Override
	public List<Hit> findByResourceList(List<String> listIdExtendableResource, String strExtendableResourceType,
			Plugin plugin) {
		List<Hit> listHit = new ArrayList<>( );
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_SELECT_BY_ID_RESOURCE_LIST );
        if ( CollectionUtils.isNotEmpty( listIdExtendableResource ) )
        {
            sbSql.append( listIdExtendableResource.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );          
            sbSql.append( SQL_FILTER_ID_LIST_END );    
           
            try( DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin )){
	        	int nIndex = 0;
	    		
		        daoUtil.setString( ++nIndex, strExtendableResourceType );
		        for ( String id : listIdExtendableResource )
		        {
		            daoUtil.setString( ++nIndex, id );
		        }
		        daoUtil.executeQuery(  );
		
		        while ( daoUtil.next(  ) )
		        {
		            nIndex = 1;
		
		            Hit hit = new Hit( );
		            hit.setIdHit( daoUtil.getInt( nIndex++ ) );
		            hit.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
		            hit.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
		            hit.setNbHits( daoUtil.getInt( nIndex ) );
		            listHit.add( hit );
		        }

           }
        }
        return listHit;
	}
}
