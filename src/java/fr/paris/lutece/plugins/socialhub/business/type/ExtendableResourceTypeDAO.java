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
package fr.paris.lutece.plugins.socialhub.business.type;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * ExtendableResourceTypeDAO
 *
 */
public class ExtendableResourceTypeDAO implements IExtendableResourceTypeDAO
{
    private static final String SQL_QUERY_INSERT = " INSERT INTO socialhub_resource_type (resource_type, description) VALUES ( ?,? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE socialhub_resource_type SET description = ? WHERE resource_type = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM socialhub_resource_type WHERE resource_type = ? ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT resource_type, description FROM socialhub_resource_type ";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECT_ALL + " WHERE resource_type = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( ExtendableResourceType resourceType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setString( nIndex++, resourceType.getKey(  ) );
        daoUtil.setString( nIndex, resourceType.getDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( ExtendableResourceType resourceType, Plugin plugin )
    {
        int nIndex = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( nIndex++, resourceType.getDescription(  ) );

        daoUtil.setString( nIndex, resourceType.getKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );

        daoUtil.setString( 1, strKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendableResourceType load( String strKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery(  );

        ExtendableResourceType resourceType = null;

        if ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            resourceType = new ExtendableResourceType(  );
            resourceType.setKey( daoUtil.getString( nIndex++ ) );
            resourceType.setDescription( daoUtil.getString( nIndex ) );
        }

        daoUtil.free(  );

        return resourceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExtendableResourceType> loadAll( Plugin plugin )
    {
        List<ExtendableResourceType> listResourceTypes = new ArrayList<ExtendableResourceType>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIndex = 1;
            ExtendableResourceType resourceType = new ExtendableResourceType(  );
            resourceType.setKey( daoUtil.getString( nIndex++ ) );
            resourceType.setDescription( daoUtil.getString( nIndex ) );

            listResourceTypes.add( resourceType );
        }

        daoUtil.free(  );

        return listResourceTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuplicate( String strKey, Plugin plugin )
    {
        boolean bIsDuplicate = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setString( 1, strKey );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bIsDuplicate = true;
        }

        daoUtil.free(  );

        return bIsDuplicate;
    }
}
