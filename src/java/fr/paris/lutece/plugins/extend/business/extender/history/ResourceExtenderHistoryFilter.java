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

import fr.paris.lutece.plugins.extend.util.OperatorEnum;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import javax.validation.constraints.NotNull;

/**
 * ResourceExtenderHistoryFilter.
 */
public class ResourceExtenderHistoryFilter implements Serializable
{
    /** The Constant ALL. */
    public static final int ALL = -1;
    private static final long serialVersionUID = -3449479672496815070L;

    // SQL
    private static final String SQL_WHERE = " WHERE ";
    private static final String SQL_AND = " AND ";
    private static final String SQL_OR = " OR ";

    // FILTERS
    private static final String SQL_FILTER_ID_HISTORY = " id_history = ? ";
    private static final String SQL_FILTER_EXTENDER_TYPE = " extender_type = ? ";
    private static final String SQL_FILTER_ID_RESOURCE = " id_resource = ? ";
    private static final String SQL_FILTER_RESOURCE_TYPE = " resource_type = ? ";
    private static final String SQL_FILTER_USER_GUID = " user_guid = ? ";
    private static final String SQL_FILTER_IP_ADDRESS = " ip_address = ? ";
    private static final String SQL_FILTER_DATE_CREATION = " date_creation ";
    private static final String SQL_ORDER_BY_ID_RESOURCE = " CAST( id_resource AS DECIMAL ) ";

    // SQL ORDER BY
    private static final String SQL_ORDER_BY = " ORDER BY ";
    private static final String SQL_DESC = " DESC ";
    private static final String SQL_ASC = " ASC ";
    private static final String SQL_GROUP_BY = " GROUP BY ";

    // CONSTANTS
    private static final String QUESTION_MARK = " ? ";

    // PARAMETERS
    private static final String PARAMETER_ID_RESOURCE = "id_resource";

    // VARIABLES
    private long _lIdHistory;
    @NotNull
    private String _strExtenderType;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private String _strUserGuid;
    private String _strIpAddress;
    private String _strDateCreation;
    private String _strDateCreationBegin;
    private String _strDateCreationEnd;
    private boolean _bIsWideSearch;
    private String _strSortedAttributeName;
    private boolean _bIsAscSort;
    private OperatorEnum _operatorDateCreation;
    private String _strGroupByAttributeName;
    private int _nItemsPerPage;
    private int _nPageIndex;

    /**
     * Instantiates a new resource extender history filter.
     */
    public ResourceExtenderHistoryFilter( )
    {
        _lIdHistory = ALL;
        _strExtenderType = StringUtils.EMPTY;
        _strExtendableResourceType = StringUtils.EMPTY;
        _strIdExtendableResource = StringUtils.EMPTY;
        _strUserGuid = StringUtils.EMPTY;
        _strIpAddress = StringUtils.EMPTY;
        _strDateCreation = StringUtils.EMPTY;
        _strDateCreationBegin = StringUtils.EMPTY;
        _strDateCreationEnd = StringUtils.EMPTY;
        _bIsWideSearch = false;
        _bIsAscSort = true;
        _strSortedAttributeName = StringUtils.EMPTY;
        _operatorDateCreation = OperatorEnum.EQUAL;
        _nItemsPerPage = ALL;
        _nPageIndex = ALL;
    }

    /**
     * Gets the id history.
     *
     * @return the lIdHistory
     */
    public long getIdHistory( )
    {
        return _lIdHistory;
    }

    /**
     * Sets the id history.
     *
     * @param lIdHistory
     *            the lIdHistory to set
     */
    public void setIdHistory( long lIdHistory )
    {
        _lIdHistory = lIdHistory;
    }

    /**
     * Contains id history.
     *
     * @return true, if successful
     */
    public boolean containsIdHistory( )
    {
        return _lIdHistory > 0;
    }

    /**
     * Gets the extender type.
     *
     * @return the strExtenderType
     */
    public String getExtenderType( )
    {
        return _strExtenderType;
    }

    /**
     * Sets the extender type.
     *
     * @param strExtenderType
     *            the strExtenderType to set
     */
    public void setExtenderType( String strExtenderType )
    {
        _strExtenderType = strExtenderType;
    }

    /**
     * Contains extender type.
     *
     * @return true, if successful
     */
    public boolean containsExtenderType( )
    {
        return StringUtils.isNotBlank( _strExtenderType );
    }

    /**
     * Gets the id extendable resource.
     *
     * @return the strIdExtendableResource
     */
    public String getIdExtendableResource( )
    {
        return _strIdExtendableResource;
    }

    /**
     * Sets the id extendable resource.
     *
     * @param strIdExtendableResource
     *            the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * Contains id extendable resource.
     *
     * @return true, if successful
     */
    public boolean containsIdExtendableResource( )
    {
        return StringUtils.isNotBlank( _strIdExtendableResource );
    }

    /**
     * Gets the extendable resource type.
     *
     * @return the extendableResourceType
     */
    public String getExtendableResourceType( )
    {
        return _strExtendableResourceType;
    }

    /**
     * Sets the extendable resource type.
     *
     * @param strExtendableResourceType
     *            the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * Contains extendable resource type.
     *
     * @return true, if successful
     */
    public boolean containsExtendableResourceType( )
    {
        return StringUtils.isNotBlank( _strExtendableResourceType );
    }

    /**
     * Gets the user guid.
     *
     * @return the strUserGuid
     */
    public String getUserGuid( )
    {
        return _strUserGuid;
    }

    /**
     * Sets the user guid.
     *
     * @param strUserGuid
     *            the strUserGuid to set
     */
    public void setUserGuid( String strUserGuid )
    {
        _strUserGuid = strUserGuid;
    }

    /**
     * Contains user guid.
     *
     * @return true, if successful
     */
    public boolean containsUserGuid( )
    {
        return StringUtils.isNotBlank( _strUserGuid );
    }

    /**
     * Gets the ip address.
     *
     * @return the strIpAddress
     */
    public String getIpAddress( )
    {
        return _strIpAddress;
    }

    /**
     * Sets the ip address.
     *
     * @param strIpAddress
     *            the strIpAddress to set
     */
    public void setIpAddress( String strIpAddress )
    {
        _strIpAddress = strIpAddress;
    }

    /**
     * Contains ip address.
     *
     * @return true, if successful
     */
    public boolean containsIpAddress( )
    {
        return StringUtils.isNotBlank( _strIpAddress );
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreation( )
    {
        return _strDateCreation;
    }

    /**
     * Sets the date creation.
     *
     * @param strDateCreation
     *            the new date creation
     */
    public void setDateCreation( String strDateCreation )
    {
        _strDateCreation = strDateCreation;
    }

    /**
     * Contains date creation.
     *
     * @return true, if successful
     */
    public boolean containsDateCreation( )
    {
        return StringUtils.isNotBlank( _strDateCreation );
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreationBegin( )
    {
        return _strDateCreationBegin;
    }

    /**
     * Sets the date creation.
     *
     * @param strDateCreationBegin
     *            the new date creation
     */
    public void setDateCreationBegin( String strDateCreationBegin )
    {
        _strDateCreationBegin = strDateCreationBegin;
    }

    /**
     * Contains date creation.
     *
     * @return true, if successful
     */
    public boolean containsDateCreationBegin( )
    {
        return StringUtils.isNotBlank( _strDateCreationBegin );
    }

    /**
     * Gets the date creation.
     *
     * @return the date creation
     */
    public String getDateCreationEnd( )
    {
        return _strDateCreationEnd;
    }

    /**
     * Sets the date creation.
     *
     * @param strDateCreationEnd
     *            the new date creation
     */
    public void setDateCreationEnd( String strDateCreationEnd )
    {
        _strDateCreationEnd = strDateCreationEnd;
    }

    /**
     * Contains date creation.
     *
     * @return true, if successful
     */
    public boolean containsDateCreationEnd( )
    {
        return StringUtils.isNotBlank( _strDateCreationEnd );
    }

    /**
     * Set true if the filter is applied to a wide search. <br/>
     * In other words, the SQL query will use
     * <ul>
     * <li>SQL <b>OR</b> if it is applied to a wide search</li>
     * <li>SQL <b>AND</b> if it is not applied to a wide search</li>
     * </ul>
     * 
     * @param bIsWideSearch
     *            true if it a wide search, false otherwise
     */
    public void setIsWideSearch( boolean bIsWideSearch )
    {
        _bIsWideSearch = bIsWideSearch;
    }

    /**
     * Check if the filter is applied to a wide search or not. <br/>
     * In other words, the SQL query will use
     * <ul>
     * <li>SQL <b>OR</b> if it is applied to a wide search</li>
     * <li>SQL <b>AND</b> if it is not applied to a wide search</li>
     * </ul>
     * 
     * @return true if it is applied to a wide search
     */
    public boolean isWideSearch( )
    {
        return _bIsWideSearch;
    }

    /**
     * Gets the sorted attribute name.
     *
     * @return the sorted attribute name
     */
    public String getSortedAttributeName( )
    {
        return _strSortedAttributeName;
    }

    /**
     * Gets the sorted attribute name for sql query.
     *
     * @return the sorted attribute name for sql query
     */
    public String getSortedAttributeNameForSQLQuery( )
    {
        if ( PARAMETER_ID_RESOURCE.equals( _strSortedAttributeName ) )
        {
            return SQL_ORDER_BY_ID_RESOURCE;
        }

        return _strSortedAttributeName;
    }

    /**
     * Sets the sorted attribute name.
     *
     * @param strSortedAttributeName
     *            the new sorted attribute name
     */
    public void setSortedAttributeName( String strSortedAttributeName )
    {
        _strSortedAttributeName = strSortedAttributeName;
    }

    /**
     * Sets the sorted attribute name.
     *
     * @param request
     *            the request
     */
    public void setSortedAttributeName( HttpServletRequest request )
    {
        String strAttributeName = request.getParameter( Parameters.SORTED_ATTRIBUTE_NAME );

        if ( StringUtils.isNotBlank( strAttributeName ) )
        {
            _strSortedAttributeName = strAttributeName;
        }
        else
        {
            _strSortedAttributeName = StringUtils.EMPTY;
        }
    }

    /**
     * Contains attribute name.
     *
     * @return true, if successful
     */
    public boolean containsAttributeName( )
    {
        return StringUtils.isNotBlank( _strSortedAttributeName );
    }

    /**
     * Checks if is asc sort.
     *
     * @return true, if is asc sort
     */
    public boolean isAscSort( )
    {
        return _bIsAscSort;
    }

    /**
     * Sets the asc sort.
     *
     * @param bAscSort
     *            the new asc sort
     */
    public void setAscSort( boolean bAscSort )
    {
        _bIsAscSort = bAscSort;
    }

    /**
     * Sets the asc sort.
     *
     * @param request
     *            the request
     */
    public void setAscSort( HttpServletRequest request )
    {
        if ( StringUtils.isNotBlank( request.getParameter( Parameters.SORTED_ASC ) ) )
        {
            _bIsAscSort = Boolean.parseBoolean( request.getParameter( Parameters.SORTED_ASC ) );
        }
    }

    /**
     * @return the operatorDateCreation
     */
    public OperatorEnum getOperatorDateCreation( )
    {
        return _operatorDateCreation;
    }

    /**
     * @param operatorDateCreation
     *            the operatorDateCreation to set
     */
    public void setOperatorDateCreation( OperatorEnum operatorDateCreation )
    {
        _operatorDateCreation = operatorDateCreation;
    }

    /**
     * Gets the group by attribute name.
     *
     * @return the group by attribute name
     */
    public String getGroupByAttributeName( )
    {
        return _strGroupByAttributeName;
    }

    /**
     * Sets the group by attribute name.
     *
     * @param strGroupByAttributeName
     *            the new group by attribute name
     */
    public void setGroupByAttributeName( String strGroupByAttributeName )
    {
        _strGroupByAttributeName = strGroupByAttributeName;
    }

    /**
     * Contains group by attribute name.
     *
     * @return true, if successful
     */
    public boolean containsGroupByAttributeName( )
    {
        return StringUtils.isNotBlank( _strGroupByAttributeName );
    }

    /**
     * @return the nItemsPerPage
     */
    public int getItemsPerPage( )
    {
        return _nItemsPerPage;
    }

    /**
     * @param nItemsPerPage
     *            the nItemsPerPage to set
     */
    public void setItemsPerPage( int nItemsPerPage )
    {
        _nItemsPerPage = nItemsPerPage;
    }

    /**
     * Contains items per page.
     *
     * @return true, if successful
     */
    public boolean containsItemsPerPage( )
    {
        return _nItemsPerPage != ALL;
    }

    /**
     * @return the nPageIndex
     */
    public int getPageIndex( )
    {
        return _nPageIndex;
    }

    /**
     * @param nPageIndex
     *            the nPageIndex to set
     */
    public void setPageIndex( int nPageIndex )
    {
        _nPageIndex = nPageIndex;
    }

    /**
     * Contains page index.
     *
     * @return true, if successful
     */
    public boolean containsPageIndex( )
    {
        return _nPageIndex != ALL;
    }

    /**
     * Builds the sql query.
     *
     * @param strSQL
     *            the str sql
     * @return the string
     */
    public String buildSQLQuery( String strSQL )
    {
        StringBuilder sbSQL = new StringBuilder( strSQL );
        int nIndex = 1;

        nIndex = buildFilter( sbSQL, containsIdHistory( ), SQL_FILTER_ID_HISTORY, nIndex );
        nIndex = buildFilter( sbSQL, containsExtenderType( ), SQL_FILTER_EXTENDER_TYPE, nIndex );
        nIndex = buildFilter( sbSQL, containsIdExtendableResource( ), SQL_FILTER_ID_RESOURCE, nIndex );
        nIndex = buildFilter( sbSQL, containsExtendableResourceType( ), SQL_FILTER_RESOURCE_TYPE, nIndex );
        nIndex = buildFilter( sbSQL, containsUserGuid( ), SQL_FILTER_USER_GUID, nIndex );
        nIndex = buildFilter( sbSQL, containsIpAddress( ), SQL_FILTER_IP_ADDRESS, nIndex );
        nIndex = buildFilter( sbSQL, containsDateCreation( ), SQL_FILTER_DATE_CREATION + getOperatorDateCreation( ) + QUESTION_MARK, nIndex );
        nIndex = buildFilter( sbSQL, containsDateCreationBegin( ), SQL_FILTER_DATE_CREATION + OperatorEnum.GREATER_OR_EQUAL + QUESTION_MARK, nIndex );
        buildFilter( sbSQL, containsDateCreationEnd( ), SQL_FILTER_DATE_CREATION + OperatorEnum.LOWER_OR_EQUAL + QUESTION_MARK, nIndex );

        if ( containsGroupByAttributeName( ) )
        {
            sbSQL.append( SQL_GROUP_BY );
            sbSQL.append( getGroupByAttributeName( ) );
        }

        if ( containsAttributeName( ) )
        {
            sbSQL.append( SQL_ORDER_BY );
            sbSQL.append( getSortedAttributeNameForSQLQuery( ) );

            if ( isAscSort( ) )
            {
                sbSQL.append( SQL_ASC );
            }
            else
            {
                sbSQL.append( SQL_DESC );
            }
        }

        if ( containsPageIndex( ) && containsItemsPerPage( ) )
        {
            sbSQL.append( " LIMIT " + _nItemsPerPage );
            sbSQL.append( " OFFSET " + ( ( _nPageIndex - 1 ) * _nItemsPerPage ) );
        }

        return sbSQL.toString( );
    }

    /**
     * Sets the filter values.
     *
     * @param daoUtil
     *            the new filter values
     */
    public void setFilterValues( DAOUtil daoUtil )
    {
        int nIndex = 1;

        if ( containsIdHistory( ) )
        {
            daoUtil.setLong( nIndex++, getIdHistory( ) );
        }

        if ( containsExtenderType( ) )
        {
            daoUtil.setString( nIndex++, getExtenderType( ) );
        }

        if ( containsIdExtendableResource( ) )
        {
            daoUtil.setString( nIndex++, getIdExtendableResource( ) );
        }

        if ( containsExtendableResourceType( ) )
        {
            daoUtil.setString( nIndex++, getExtendableResourceType( ) );
        }

        if ( containsUserGuid( ) )
        {
            daoUtil.setString( nIndex++, getUserGuid( ) );
        }

        if ( containsIpAddress( ) )
        {
            daoUtil.setString( nIndex++, getIpAddress( ) );
        }

        if ( containsDateCreation( ) )
        {
            daoUtil.setTimestamp( nIndex++, DateUtil.formatTimestamp( getDateCreation( ), I18nService.getDefaultLocale( ) ) );
        }

        if ( containsDateCreationBegin( ) )
        {
            daoUtil.setTimestamp( nIndex++, DateUtil.formatTimestamp( getDateCreationBegin( ), I18nService.getDefaultLocale( ) ) );
        }

        if ( containsDateCreationEnd( ) )
        {
            daoUtil.setTimestamp( nIndex++, DateUtil.formatTimestamp( getDateCreationEnd( ), I18nService.getDefaultLocale( ) ) );
        }
    }

    /**
     * Builds the filter.
     *
     * @param sbSQL
     *            the sb sql
     * @param bAddFilter
     *            the b add filter
     * @param strSQL
     *            the str sql
     * @param nIndex
     *            the n index
     * @return the int
     */
    private int buildFilter( StringBuilder sbSQL, boolean bAddFilter, String strSQL, int nIndex )
    {
        int nIndexTmp = nIndex;

        if ( bAddFilter )
        {
            nIndexTmp = addSQLWhereOr( isWideSearch( ), sbSQL, nIndex );
            sbSQL.append( strSQL );
        }

        return nIndexTmp;
    }

    /**
     * Add a <b>WHERE</b> or a <b>OR</b> depending of the index. <br/>
     * <ul>
     * <li>if <code>nIndex</code> == 1, then we add a <b>WHERE</b></li>
     * <li>if <code>nIndex</code> != 1, then we add a <b>OR</b> or a <b>AND</b> depending of the wide search characteristic</li>
     * </ul>
     * 
     * @param bIsWideSearch
     *            true if it is a wide search, false otherwise
     * @param sbSQL
     *            the SQL query
     * @param nIndex
     *            the index
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
