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
package fr.paris.lutece.plugins.socialhub.service;

import fr.paris.lutece.plugins.socialhub.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.socialhub.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.socialhub.service.extender.ResourceExtenderService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Locale;


/**
 *
 * ExtendableResourceResourceIdService
 *
 */
public class ExtendableResourceResourceIdService extends ResourceIdService
{
    // PERMISSIONS
    /** The Constant PERMISSION_CREATE. */
    public static final String PERMISSION_MODIFY_CONFIGURATION = "MODIFY_CONFIG";

    /** The Constant PERMISSION_DELETE. */
    public static final String PERMISSION_VIEW_INFO = "VIEW_INFO";

    /** The Constant PERMISSION_VIEW_HISTORY. */
    public static final String PERMISSION_VIEW_HISTORY = "VIEW_HISTORY";

    /** The Constant PERMISSION_DELETE. */
    public static final String PERMISSION_DELETE = "DELETE";

    // PROPERTIES
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "socialhub.permission.label.extendableResource";
    private static final String PROPERTY_LABEL_MODIFY_CONFIGURATION = "socialhub.permission.label.modifyConfiguration";
    private static final String PROPERTY_LABEL_VIEW_INFO = "socialhub.permission.label.viewInfo";
    private static final String PROPERTY_LABEL_VIEW_HISTORY = "socialhub.permission.label.viewHistory";
    private static final String PROPERTY_LABEL_DELETE = "socialhub.permission.label.delete";

    /**
     * Instantiates a new resource type resource id service.
     */
    public ExtendableResourceResourceIdService(  )
    {
        setPluginName( SocialHubPlugin.PLUGIN_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( ExtendableResourceResourceIdService.class.getName(  ) );
        rt.setPluginName( SocialHubPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( ResourceExtenderDTO.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY_CONFIGURATION );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY_CONFIGURATION );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_VIEW_INFO );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW_INFO );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_VIEW_HISTORY );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW_HISTORY );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getResourceIdList( Locale locale )
    {
        IResourceExtenderService resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );

        List<ResourceExtenderDTO> listResources = resourceExtenderService.findAll(  );

        ReferenceList ref = new ReferenceList(  );

        for ( ResourceExtenderDTO resourceExtender : listResources )
        {
            String strName = resourceExtender.getName(  );
            StringBuilder sbName = new StringBuilder( StringUtils.isNotEmpty( strName ) ? strName : StringUtils.EMPTY );
            sbName.append( " (" );
            sbName.append( "Extender type : " ).append( resourceExtender.getExtenderType(  ) );
            sbName.append( " - ID resource : " ).append( resourceExtender.getIdExtendableResource(  ) );
            sbName.append( " - Resource type : " ).append( resourceExtender.getExtendableResourceType(  ) );
            sbName.append( ")" );
            ref.addItem( resourceExtender.getIdExtender(  ), sbName.toString(  ) );
        }

        return ref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        if ( StringUtils.isNotBlank( strId ) && StringUtils.isNumeric( strId ) )
        {
            int nId = Integer.parseInt( strId );
            IResourceExtenderService resourceExtenderService = SpringContextService.getBean( ResourceExtenderService.BEAN_SERVICE );
            ResourceExtenderDTO resource = resourceExtenderService.findByPrimaryKey( nId );

            if ( resource != null )
            {
                return resource.getName(  );
            }
        }

        return null;
    }
}
