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
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;


/**
 *
 * ExtendableResourceTypeService
 *
 */
public class ExtendableResourceTypeService implements IExtendableResourceTypeService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.extendableResourceTypeService";
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendableResourceType findByPrimaryKey( String strKey, Locale locale )
    {
        List<IExtendableResource> listExtendableResources = SpringContextService
                .getBeansOfType( IExtendableResource.class );
        ExtendableResourceType resourceType = null;
        for ( IExtendableResource resource : listExtendableResources )
        {
            if ( StringUtils.equals( resource.getExtendableResourceType( ), strKey ) )
            {
                resourceType = new ExtendableResourceType( );
                resourceType.setKey( resource.getExtendableResourceType( ) );
                resourceType.setDescription( resource.getExtendableResourceTypeDescription( locale ) );
            }
        }
        return resourceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExtendableResourceType> findAll( Locale locale )
    {
        List<IExtendableResource> listExtendableResources = SpringContextService
                .getBeansOfType( IExtendableResource.class );
        List<ExtendableResourceType> listResourceTypes = new ArrayList<ExtendableResourceType>( );
        for ( IExtendableResource resource : listExtendableResources )
        {
            ExtendableResourceType resourceType = new ExtendableResourceType( );
            resourceType.setKey( resource.getExtendableResourceType( ) );
            resourceType.setDescription( resource.getExtendableResourceTypeDescription( locale ) );
            listResourceTypes.add( resourceType );
        }
        return listResourceTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList findAllAsRef( Locale locale )
    {
        ReferenceList ref = new ReferenceList(  );

        for ( ExtendableResourceType resourceType : findAll( locale ) )
        {
            ref.addItem( resourceType.getKey( ), resourceType.getDescription( ) );
        }

        return ref;
    }
}
