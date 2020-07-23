/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.extend.service.extender.history;

import fr.paris.lutece.plugins.extend.business.extender.history.IResourceExtenderHistoryDAO;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;

import org.apache.commons.lang.StringUtils;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * ResourceExtenderHistoryService
 *
 */
public class ResourceExtenderHistoryService implements IResourceExtenderHistoryService
{
    /** The Constant BBEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.resourceExtenderHistoryService";
    @Inject
    private IResourceExtenderHistoryDAO _resourceExtenderHistoryDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderHistory create( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType,
            HttpServletRequest request )
    {
        ResourceExtenderHistory history = new ResourceExtenderHistory( );
        history.setExtenderType( strExtenderType );
        history.setIdExtendableResource( strIdExtendableResource );
        history.setExtendableResourceType( strExtendableResourceType );
        history.setIpAddress( request.getRemoteAddr( ) );

        String strUserGuid = StringUtils.EMPTY;

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            if ( user != null )
            {
                strUserGuid = user.getName( );
            }
        }

        history.setUserGuid( strUserGuid );
        create( history );

        return history;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void create( ResourceExtenderHistory history )
    {
        _resourceExtenderHistoryDAO.insert( history, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void remove( int nIdHistory )
    {
        _resourceExtenderHistoryDAO.delete( nIdHistory, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void removeByResource( String strExtenderType, String strIdExtendableResource, String strExtendableResourceType )
    {
        _resourceExtenderHistoryDAO.deleteByResource( strExtenderType, strIdExtendableResource, strExtendableResourceType, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderHistory findByPrimary( int nIdHistory )
    {
        return _resourceExtenderHistoryDAO.load( nIdHistory, ExtendPlugin.getPlugin( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceExtenderHistory> findByFilter( ResourceExtenderHistoryFilter filter )
    {
        return _resourceExtenderHistoryDAO.loadByFilter( filter, ExtendPlugin.getPlugin( ) );
    }
}
