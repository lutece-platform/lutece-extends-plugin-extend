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

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 *
 * ResourceExtenderHistoryDAO
 *
 */
public class ResourceExtenderHistoryDAO implements IResourceExtenderHistoryDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_history ) FROM extend_resource_extender_history ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_resource_extender_history ( id_history, extender_type, id_resource, resource_type, user_guid, ip_address ) "
            + " VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_history, extender_type, id_resource, resource_type, user_guid, ip_address, date_creation "
            + " FROM extend_resource_extender_history ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_history = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_resource_extender_history WHERE id_history = ? ";
    private static final String SQL_QUERY_DELETE_BY_RESOURCE = " DELETE FROM extend_resource_extender_history WHERE extender_type = ? AND resource_type = ? ";
    private static final String SQL_QUERY_FILTER_BY_ID_RESOURCE = " AND id_resource = ? ";
    private static final String SQL_QUERY_FIND_BY_ID_LIST_RESOURCES = SQL_QUERY_SELECT_ALL + " WHERE extender_type= ? and  resource_type = ? and   id_resource IN ( ";

    /**
     * Generates a new primary key.
     *
     * @param plugin
     *            the plugin
     * @return The new primary key
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
    public synchronized void insert( ResourceExtenderHistory history, Plugin plugin )
    {
        int nNewPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        history.setIdHistory( nNewPrimaryKey );

        int nIndex = 1;

        daoUtil.setLong( nIndex++, history.getIdHistory( ) );
        daoUtil.setString( nIndex++, history.getExtenderType( ) );
        daoUtil.setString( nIndex++, history.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, history.getExtendableResourceType( ) );
        daoUtil.setString( nIndex++, history.getUserGuid( ) );
        daoUtil.setString( nIndex, history.getIpAddress( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderHistory load( int nIdHistory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdHistory );
        daoUtil.executeQuery( );

        ResourceExtenderHistory history = null;

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            history = new ResourceExtenderHistory( );
            history.setIdHistory( daoUtil.getLong( nIndex++ ) );
            history.setExtenderType( daoUtil.getString( nIndex++ ) );
            history.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            history.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            history.setUserGuid( daoUtil.getString( nIndex++ ) );
            history.setIpAddress( daoUtil.getString( nIndex++ ) );
            history.setDateCreation( daoUtil.getTimestamp( nIndex ) );
        }

        daoUtil.free( );

        return history;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderHistory> loadByFilter( ResourceExtenderHistoryFilter filter, Plugin plugin )
    {
        List<ResourceExtenderHistory> listHistories = new ArrayList<ResourceExtenderHistory>( );
        DAOUtil daoUtil = new DAOUtil( filter.buildSQLQuery( SQL_QUERY_SELECT_ALL ), plugin );
        filter.setFilterValues( daoUtil );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            int nIndex = 1;
            ResourceExtenderHistory history = new ResourceExtenderHistory( );
            history.setIdHistory( daoUtil.getLong( nIndex++ ) );
            history.setExtenderType( daoUtil.getString( nIndex++ ) );
            history.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            history.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            history.setUserGuid( daoUtil.getString( nIndex++ ) );
            history.setIpAddress( daoUtil.getString( nIndex++ ) );
            history.setDateCreation( daoUtil.getDate( nIndex ) );

            listHistories.add( history );
        }

        daoUtil.free( );

        return listHistories;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderHistory> loadByListIdResource( List<String> listIdResource, String strExtendableResourceType, String strExtenderType,  Plugin plugin )
    {
        List<ResourceExtenderHistory> listHistories = new ArrayList< >( );
        
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_FIND_BY_ID_LIST_RESOURCES );
	    if ( CollectionUtils.isNotEmpty( listIdResource ) )
	    {
	        sbSql.append( listIdResource.stream( ).map( s -> "?" ).collect( Collectors.joining( "," ) ) );
	        sbSql.append( ")" );
	    }
		       
	   try ( DAOUtil daoUtil = new DAOUtil( sbSql.toString() , plugin ))
	   {
	   	int nIndex= 0;
	       daoUtil.setString( ++nIndex, strExtenderType );	
	       daoUtil.setString( ++nIndex, strExtendableResourceType );	

	       for ( String idResource : listIdResource )
	       {
	           daoUtil.setString( ++nIndex, idResource );
	       }
	       daoUtil.executeQuery(  );
	
	       while ( daoUtil.next( ) )
	        {
	            nIndex = 1;
	            ResourceExtenderHistory history = new ResourceExtenderHistory( );
	            history.setIdHistory( daoUtil.getLong( nIndex++ ) );
	            history.setExtenderType( daoUtil.getString( nIndex++ ) );
	            history.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
	            history.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
	            history.setUserGuid( daoUtil.getString( nIndex++ ) );
	            history.setIpAddress( daoUtil.getString( nIndex++ ) );
	            history.setDateCreation( daoUtil.getDate( nIndex ) );

	            listHistories.add( history );
	    
	        }
	       return listHistories;

	   }
	   }    

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdHistory, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdHistory );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByResource( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType, Plugin plugin )
    {
        StringBuilder sbSql = new StringBuilder( SQL_QUERY_DELETE_BY_RESOURCE );

        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            sbSql.append( SQL_QUERY_FILTER_BY_ID_RESOURCE );
        }

        DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
        int nIndex = 1;
        daoUtil.setString( nIndex++, strExtenderType );
        daoUtil.setString( nIndex++, strExtendableResourceType );

        if ( !ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strIdExtendableResource ) )
        {
            daoUtil.setString( nIndex, strIdExtendableResource );
        }

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
