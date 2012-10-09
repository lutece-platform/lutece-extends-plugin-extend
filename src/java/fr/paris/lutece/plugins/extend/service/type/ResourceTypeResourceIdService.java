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
package fr.paris.lutece.plugins.extend.service.type;

import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 *
 * ResourceTypeResourceIdService
 *
 */
public class ResourceTypeResourceIdService extends ResourceIdService
{
    // PERMISSIONS
    /** The Constant PERMISSION_CREATE. */
    public static final String PERMISSION_CREATE = "CREATE";

    /** The Constant PERMISSION_DELETE. */
    public static final String PERMISSION_DELETE = "DELETE";

    // PROPERTIES
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "socialhub.permission.label.resourceType";
    private static final String PROPERTY_LABEL_CREATE = "socialhub.permission.label.create";
    private static final String PROPERTY_LABEL_DELETE = "socialhub.permission.label.delete";

    /**
     * Instantiates a new resource type resource id service.
     */
    public ResourceTypeResourceIdService(  )
    {
        setPluginName( ExtendPlugin.PLUGIN_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
		rt.setResourceIdServiceClass( fr.paris.lutece.plugins.extend.service.type.ResourceTypeResourceIdService.class.getName( ) );
        rt.setPluginName( ExtendPlugin.PLUGIN_NAME );
        rt.setResourceTypeKey( ExtendableResourceType.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
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
        IExtendableResourceTypeService extendableResourceTypeService = SpringContextService.getBean( ExtendableResourceTypeService.BEAN_SERVICE );

        return extendableResourceTypeService.findAllAsRef(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( String strId, Locale locale )
    {
        IExtendableResourceTypeService extendableResourceTypeService = SpringContextService.getBean( ExtendableResourceTypeService.BEAN_SERVICE );
        ExtendableResourceType resourceType = extendableResourceTypeService.findByPrimaryKey( strId );

        if ( resourceType != null )
        {
            return resourceType.getKey(  );
        }

        return null;
    }
}
