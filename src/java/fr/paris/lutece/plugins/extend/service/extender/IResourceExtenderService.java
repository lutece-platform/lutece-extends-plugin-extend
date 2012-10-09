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
package fr.paris.lutece.plugins.extend.service.extender;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IResourceExtenderService
 *
 */
public interface IResourceExtenderService
{
    /**
     * Creates.
     *
     * @param extender the extender
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void create( ResourceExtenderDTO extender );

    /**
     * Update.
     *
     * @param extender the extender
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void update( ResourceExtenderDTO extender );

    /**
     * Delete.
     *
     * @param nIdExtender the n id extender
     */
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    void remove( int nIdExtender );

    /**
     * Find by primary key.
     *
     * @param nIdExtender the n id extender
     * @return the extender
     */
    ResourceExtenderDTO findByPrimaryKey( int nIdExtender );

    /**
     * Find by id hub resource.
     *
     * @return the list
     */
    List<ResourceExtenderDTO> findAll(  );

    /**
     * Find by filter.
     *
     * @param filter the filter
     * @return the list
     */
    List<ResourceExtenderDTO> findByFilter( ResourceExtenderDTOFilter filter );

    /**
     * Find ids by filter.
     *
     * @param filter the filter
     * @return the list
     */
    List<Integer> findIdsByFilter( ResourceExtenderDTOFilter filter );

    /**
     * Find by list ids.
     *
     * @param listIds the list ids
     * @return the list
     */
    List<ResourceExtenderDTO> findByListIds( List<Integer> listIds );

    /**
     * Find the resource extender that either has an ID.
     * @param strExtenderType the str extender type
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the resource extender dto
     */
    ResourceExtenderDTO findResourceExtender( String strExtenderType, String strIdExtendableResource,
        String strExtendableResourceType );

    /**
     * Find wildcard resource extender.
     *
     * @param strExtenderType the str extender type
     * @param strExtendableResourceType the str extendable resource type
     * @return the resource extender dto
     */
    ResourceExtenderDTO findWildCardResourceExtender( String strExtenderType, String strExtendableResourceType );

    /**
     * Gets the extender types.
     *
     * @param locale the locale
     * @return the extender types
     */
    ReferenceList getExtenderTypes( Locale locale );

    /**
     * Checks if is authorized.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strExtenderType the str extender type
     * @return true, if is authorized
     */
    boolean isAuthorized( String strIdExtendableResource, String strExtendableResourceType, String strExtenderType );

    /**
     * Checks if is authorized to all resources.
     *
     * @param strExtendableResourceType the str extendable resource type
     * @param strExtenderType the str extender type
     * @return true, if is authorized to all resources
     */
    boolean isAuthorizedToAllResources( String strExtendableResourceType, String strExtenderType );

    /**
     * Checks if is authorized to all resources.
     *
     * @param nIdExtender the n id extender
     * @return true, if is authorized to all resources
     */
    boolean isAuthorizedToAllResources( int nIdExtender );

    /**
     * Gets the resource extender content.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @param strExtenderType the str resource extender type
     * @param strParameters the str parameters
     * @param request the request
     * @return the resource extender content
     */
    String getContent( String strIdExtendableResource, String strExtendableResourceType, String strExtenderType,
        String strParameters, HttpServletRequest request );

    /**
     * Do create resource add on.
     *
     * @param resourceExtender the extender
     */
    void doCreateResourceAddOn( ResourceExtenderDTO resourceExtender );

    /**
     * Do delete resource add on.
     *
     * @param resourceExtender the extender
     */
    void doDeleteResourceAddOn( ResourceExtenderDTO resourceExtender );

    /**
     * Gets the resource extenders.
     *
     * @return the resource extenders
     */
    List<IResourceExtender> getResourceExtenders(  );

    /**
     * Gets the resource extender.
     *
     * @param strExtenderType the str extender type
     * @return the resource extender
     */
    IResourceExtender getResourceExtender( String strExtenderType );

    /**
     * Gets the extendable resource name.
     *
     * @param resourceExtender the resource extender
     * @return the extendable resource name
     */
    String getExtendableResourceName( ResourceExtenderDTO resourceExtender );

    /**
     * Gets the extendable resource name.
     *
     * @param strIdExtendableResource the str id extendable resource
     * @param strExtendableResourceType the str extendable resource type
     * @return the extendable resource name
     */
    String getExtendableResourceName( String strIdExtendableResource, String strExtendableResourceType );

    /**
     * Gets the actions permission.
     *
     * @param listIds the list ids
     * @param user the user
     * @return the actions permission
     */
    Map<String, Map<String, Boolean>> getActionPermissions( List<Integer> listIds, AdminUser user );
}
