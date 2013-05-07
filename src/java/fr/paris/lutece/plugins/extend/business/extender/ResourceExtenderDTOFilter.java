/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * ResourceExtenderDTOFilter
 *
 */
public class ResourceExtenderDTOFilter implements Serializable
{
    /** The Constant WILDCARD_ID_RESOURCE. */
    public static final String WILDCARD_ID_RESOURCE = "*";
    private static final long serialVersionUID = -8695505212483945673L;
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_OR = " OR ";

    // FILTERS
    private static final String SQL_FILTER_ID_EXTENDER = " id_extender = ? ";
    private static final String SQL_FILTER_EXTENDER_TYPE = " extender_type = ? ";
    private static final String SQL_FILTER_ID_RESOURCE = " id_resource = ? ";
    private static final String SQL_FILTER_RESOURCE_TYPE = " resource_type = ? ";

    // SQL ORDER BY
    private static final String SQL_ORDER_BY = " ORDER BY ";
    private static final String SQL_DESC = " DESC ";
    private static final String SQL_ASC = " ASC ";

    // VARIABLES
    private int _nIdExtender;
    private String _strExtenderType;
    private String _strIdExtendableResource;
    private String _strExtendableResourceType;
    private boolean _bIsWideSearch;
    private String _strSortedAttributeName;
    private boolean _bIsAscSort;

    /**
     * Instantiates a new resource extender dto filter.
     */
    public ResourceExtenderDTOFilter(  )
    {
    }

    /**
     * Instantiates a new resource extender dto filter.
     *
     * @param strExtenderType the str extender type
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     */
    public ResourceExtenderDTOFilter( String strExtenderType, String strIdExtendableResource,
        String strExtendableResourceType )
    {
        _strExtenderType = strExtenderType;
        _strIdExtendableResource = strIdExtendableResource;
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * Init.
     *
     * @param request the request
     */
    public void init( HttpServletRequest request )
    {
        try
        {
            BeanUtils.populate( this, request.getParameterMap(  ) );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( "Unable to fetch data from request", e );
        }
        catch ( InvocationTargetException e )
        {
            AppLogService.error( "Unable to fetch data from request", e );
        }
    }

    /**
     * @return the nIdExtender
     */
    public int getFilterIdExtender(  )
    {
        return _nIdExtender;
    }

    /**
     * @param nIdExtender the nIdExtender to set
     */
    public void setFilterIdExtender( int nIdExtender )
    {
        _nIdExtender = nIdExtender;
    }

    /**
     * Contains id extender.
     *
     * @return true, if successful
     */
    public boolean containsFilterIdExtender(  )
    {
        return _nIdExtender > 0;
    }

    /**
     * @return the strExtenderType
     */
    public String getFilterExtenderType(  )
    {
        return _strExtenderType;
    }

    /**
     * @param strExtenderType the strExtenderType to set
     */
    public void setFilterExtenderType( String strExtenderType )
    {
        _strExtenderType = strExtenderType;
    }

    /**
     * Contains extender type.
     *
     * @return true, if successful
     */
    public boolean containsFilterExtenderType(  )
    {
        return StringUtils.isNotBlank( _strExtenderType );
    }

    /**
     * @return the strIdExtendableResource
     */
    public String getFilterIdExtendableResource(  )
    {
        return _strIdExtendableResource;
    }

    /**
     * @param strIdExtendableResource the strIdExtendableResource to set
     */
    public void setFilterIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * Contains id extendable resource.
     *
     * @return true, if successful
     */
    public boolean containsFilterIdExtendableResource(  )
    {
        return StringUtils.isNotBlank( _strIdExtendableResource );
    }

    /**
     * @return the extendableResourceType
     */
    public String getFilterExtendableResourceType(  )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType the extendableResourceType to set
     */
    public void setFilterExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * Contains extendable resource type.
     *
     * @return true, if successful
     */
    public boolean containsFilterExtendableResourceType(  )
    {
        return StringUtils.isNotBlank( _strExtendableResourceType );
    }

    /**
    * Set true if the search is wide, false otherwise
    * @param isWideSearch true if the search is wide, false otherwise
    */
    public void setWideSearch( boolean isWideSearch )
    {
        _bIsWideSearch = isWideSearch;
    }

    /**
     * Return true if the search is wide, false otherwise
     * @return true if the search is wide, false otherwise
     */
    public boolean isWideSearch(  )
    {
        return _bIsWideSearch;
    }

    /**
     * Gets the sorted attribute name.
     *
     * @return the sorted attribute name
     */
    public String getSortedAttributeName(  )
    {
        return _strSortedAttributeName;
    }

    /**
     * Sets the sorted attribute name.
     *
     * @param strSortedAttributeName the new sorted attribute name
     */
    public void setSortedAttributeName( String strSortedAttributeName )
    {
        _strSortedAttributeName = strSortedAttributeName;
    }

    /**
     * Contains attribute name.
     *
     * @return true, if successful
     */
    public boolean containsAttributeName(  )
    {
        return StringUtils.isNotBlank( _strSortedAttributeName );
    }

    /**
     * Checks if is asc sort.
     *
     * @return true, if is asc sort
     */
    public boolean isAscSort(  )
    {
        return _bIsAscSort;
    }

    /**
     * Sets the asc sort.
     *
     * @param bAscSort the new asc sort
     */
    public void setAscSort( boolean bAscSort )
    {
        _bIsAscSort = bAscSort;
    }

    /**
     * Builds the sql query.
     *
     * @param strSQL the str sql
     * @return the string
     */
    public String buildSQLQuery( String strSQL )
    {
        StringBuilder sbSQL = new StringBuilder( strSQL );
        int nIndex = 1;

        nIndex = buildFilter( sbSQL, containsFilterIdExtender(  ), SQL_FILTER_ID_EXTENDER, nIndex );
        nIndex = buildFilter( sbSQL, containsFilterExtenderType(  ), SQL_FILTER_EXTENDER_TYPE, nIndex );
        nIndex = buildFilter( sbSQL, containsFilterIdExtendableResource(  ), SQL_FILTER_ID_RESOURCE, nIndex );
        buildFilter( sbSQL, containsFilterExtendableResourceType(  ), SQL_FILTER_RESOURCE_TYPE, nIndex );

        if ( containsAttributeName(  ) )
        {
            sbSQL.append( SQL_ORDER_BY );
            sbSQL.append( getSortedAttributeName(  ) );

            if ( isAscSort(  ) )
            {
                sbSQL.append( SQL_ASC );
            }
            else
            {
                sbSQL.append( SQL_DESC );
            }
        }

        return sbSQL.toString(  );
    }

    /**
     * Sets the filter values.
     *
     * @param daoUtil the new filter values
     */
    public void setFilterValues( DAOUtil daoUtil )
    {
        int nIndex = 1;

        if ( containsFilterIdExtender(  ) )
        {
            daoUtil.setInt( nIndex++, getFilterIdExtender(  ) );
        }

        if ( containsFilterExtenderType(  ) )
        {
            daoUtil.setString( nIndex++, getFilterExtenderType(  ) );
        }

        if ( containsFilterIdExtendableResource(  ) )
        {
            daoUtil.setString( nIndex++, getFilterIdExtendableResource(  ) );
        }

        if ( containsFilterExtendableResourceType(  ) )
        {
            daoUtil.setString( nIndex++, getFilterExtendableResourceType(  ) );
        }
    }

    /**
     * Builds the filter.
     *
     * @param sbSQL the sb sql
     * @param bAddFilter the b add filter
     * @param strSQL the str sql
     * @param nIndex the n index
     * @return the int
     */
    private int buildFilter( StringBuilder sbSQL, boolean bAddFilter, String strSQL, int nIndex )
    {
        int nIndexTmp = nIndex;

        if ( bAddFilter )
        {
            nIndexTmp = addSQLWhereOr( isWideSearch(  ), sbSQL, nIndex );
            sbSQL.append( strSQL );
        }

        return nIndexTmp;
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index.
     * <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b> depending of the wide search characteristic</li>
     * </ul>
     * @param bIsWideSearch true if it is a wide search, false otherwise
     * @param sbSQL the SQL query
     * @param nIndex the index
     * @return the new index
     */
    private int addSQLWhereOr( boolean bIsWideSearch, StringBuilder sbSQL, int nIndex )
    {
        if ( nIndex == 1 )
        {
            sbSQL.append( SQL_WHERE );
        }
        else
        {
            sbSQL.append( bIsWideSearch ? SQL_OR : SQL_AND );
        }

        return nIndex + 1;
    }
}
