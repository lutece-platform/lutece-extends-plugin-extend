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
package fr.paris.lutece.plugins.extend.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 * DefaultExtendableResourceDAO
 *
 */
public class DefaultExtendableResourceDAO implements IDefaultExtendableResourceDAO
{
    private static final String SQL_QUERY_INSERT = " INSERT INTO extend_default_extendable_resource (id_resource, resource_type, name) VALUES ( ?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE extend_default_extendable_resource SET name = ? WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM extend_default_extendable_resource WHERE id_resource = ? AND resource_type = ? ";
    private static final String SQL_QUERY_SELECT = " SELECT id_resource, resource_type, name FROM extend_default_extendable_resource WHERE id_resource = ? AND resource_type = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( IExtendableResource resource, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIndex = 1;

        daoUtil.setString( nIndex++, resource.getIdExtendableResource( ) );
        daoUtil.setString( nIndex++, resource.getExtendableResourceType( ) );
        daoUtil.setString( nIndex, resource.getExtendableResourceName( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( IExtendableResource resource, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nIndex = 1;

        daoUtil.setString( nIndex++, resource.getExtendableResourceName( ) );

        daoUtil.setString( nIndex++, resource.getExtendableResourceType( ) );
        daoUtil.setString( nIndex, resource.getIdExtendableResource( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strIdResource, String strResourceType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( nIndex++, strIdResource );
        daoUtil.setString( nIndex, strResourceType );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtendableResource load( String strIdResource, String strResourceType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( nIndex++, strIdResource );
        daoUtil.setString( nIndex, strResourceType );
        daoUtil.executeQuery( );

        DefaultExtendableResource resource = null;

        if ( daoUtil.next( ) )
        {
            nIndex = 1;
            resource = new DefaultExtendableResource( );
            resource.setIdExtendableResource( daoUtil.getString( nIndex++ ) );
            resource.setExtendableResourceType( daoUtil.getString( nIndex++ ) );
            resource.setExtendableResourceName( daoUtil.getString( nIndex ) );
        }

        daoUtil.free( );

        return resource;
    }
}
