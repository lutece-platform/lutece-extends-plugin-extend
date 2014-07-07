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
package fr.paris.lutece.plugins.extend.business.extender;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 
 * ExtenderResourceDAO
 * 
 */
public class ResourceExtenderDAO implements IResourceExtenderDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_extender ) FROM extend_resource_extender ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_extender, extender_type, id_resource, resource_type, is_active FROM extend_resource_extender ";
    private static final String SQL_QUERY_SELECT_ID_EXTENDER = " SELECT id_extender FROM extend_resource_extender ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE id_extender = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_resource_extender SET extender_type = ?, id_resource = ?, resource_type = ?, is_active = ? WHERE id_extender = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_resource_extender (id_extender, extender_type, id_resource, resource_type) VALUES ( ?,?,?,? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_resource_extender where id_extender = ? ";

    // FILTERS
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_FILTER_ID_EXTENDER = " id_extender IN ";

    // CONSTANTS
    private static final String OPEN_BRACKET = " ( ";
    private static final String CLOSED_BRACKET = " ) ";
    private static final String QUESTION_MARK = " ? ";
    private static final String COMMA = " , ";

    /**
     * New primary key
     * @param plugin the plugin
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
    public synchronized void insert( ResourceExtenderDTO extender, Plugin plugin )
    {
        int nIndex = 1;
        int nIdExtender = newPrimaryKey( plugin );
        extender.setIdExtender( nIdExtender );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( nIndex++, extender.getIdExtender( ) );
        daoUtil.setString( nIndex++, extender.getExtenderType( ) );
        daoUtil.setString( nIndex++, extender.getIdExtendableResource( ) );
        daoUtil.setString( nIndex, extender.getExtendableResourceType( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( ResourceExtenderDTO extender, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, extender.getExtenderType( ) );
        daoUtil.setString( nIndex++, extender.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, extender.getExtendableResourceType( ) );
        daoUtil.setBoolean( nIndex++, extender.isIsActive( ) );

        daoUtil.setInt( nIndex, extender.getIdExtender( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdExtender, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdExtender );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderDTO load( int nIdExtender, Plugin plugin )
    {
        ResourceExtenderDTO extender = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdExtender );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            extender = populateDTO( daoUtil );
        }

        daoUtil.free( );

        return extender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> loadAll( Plugin plugin )
    {
        List<ResourceExtenderDTO> listExtenders = new ArrayList<ResourceExtenderDTO>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ResourceExtenderDTO extender = populateDTO( daoUtil );
            listExtenders.add( extender );
        }

        daoUtil.free( );

        return listExtenders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> loadByFilter( ResourceExtenderDTOFilter filter, Plugin plugin )
    {
        List<ResourceExtenderDTO> listExtenders = new ArrayList<ResourceExtenderDTO>( );
        DAOUtil daoUtil = new DAOUtil( filter.buildSQLQuery( SQL_QUERY_SELECT_ALL ), plugin );
        filter.setFilterValues( daoUtil );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            ResourceExtenderDTO extender = populateDTO( daoUtil );
            listExtenders.add( extender );
        }

        daoUtil.free( );

        return listExtenders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> loadIdsByFilter( ResourceExtenderDTOFilter filter, Plugin plugin )
    {
        List<Integer> listIdsExtender = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( filter.buildSQLQuery( SQL_QUERY_SELECT_ID_EXTENDER ), plugin );
        filter.setFilterValues( daoUtil );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            listIdsExtender.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );

        return listIdsExtender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderDTO> loadByListIds( List<Integer> listIds, Plugin plugin )
    {
        List<ResourceExtenderDTO> listExtenders = new ArrayList<ResourceExtenderDTO>( );

        if ( ( listIds != null ) && !listIds.isEmpty( ) )
        {
            // array to keep order from listId
            // because we have no way to keep it with a query
            ResourceExtenderDTO[] arrayExtenders = new ResourceExtenderDTO[listIds.size( )];
            StringBuilder sbSQL = new StringBuilder( SQL_QUERY_SELECT_ALL );
            sbSQL.append( SQL_WHERE ).append( SQL_FILTER_ID_EXTENDER );
            sbSQL.append( OPEN_BRACKET );

            for ( int i = 0; i < listIds.size( ); i++ )
            {
                sbSQL.append( QUESTION_MARK );

                if ( i < ( listIds.size( ) - 1 ) )
                {
                    sbSQL.append( COMMA );
                }
            }

            sbSQL.append( CLOSED_BRACKET );

            DAOUtil daoUtil = new DAOUtil( sbSQL.toString( ), plugin );
            int nIndex = 1;

            for ( int nId : listIds )
            {
                daoUtil.setInt( nIndex++, nId );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                ResourceExtenderDTO extender = populateDTO( daoUtil );
                listExtenders.add( extender );
                // keep id order
                arrayExtenders[listIds.indexOf( extender.getIdExtender( ) )] = extender;
            }

            daoUtil.free( );
            // get list from array
            listExtenders = Arrays.asList( arrayExtenders );
        }

        return listExtenders;
    }

    /**
     * Populate dto.
     * 
     * @param daoUtil the dao util
     * @return the resource extender dto
     */
    private ResourceExtenderDTO populateDTO( DAOUtil daoUtil )
    {
        int nIndex = 1;
        ResourceExtenderDTO dto = new ResourceExtenderDTO( );
        dto.setIdExtender( daoUtil.getInt( nIndex++ ) );
        dto.setExtenderType( daoUtil.getString( nIndex++ ) );
        dto.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
        dto.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
        dto.setIsActive( daoUtil.getBoolean( nIndex ) );

        return dto;
    }
}
