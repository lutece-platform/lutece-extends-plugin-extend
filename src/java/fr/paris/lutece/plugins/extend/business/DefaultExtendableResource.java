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
package fr.paris.lutece.plugins.extend.business;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.portal.service.resource.IExtendableResource;


/**
 * DefaultExtendableResource.
 */
public class DefaultExtendableResource implements IExtendableResource
{
    private String _strIdExtendableResource;
    private String _strResourceType;
    private String _strName;

    /**
     * Instantiates a new default extendable resource.
     */
    public DefaultExtendableResource(  )
    {
    }

    /**
     * Instantiates a new default extendable resource.
     *
     * @param resourceExtender the resource extender
     */
    public DefaultExtendableResource( ResourceExtenderDTO resourceExtender )
    {
        _strIdExtendableResource = resourceExtender.getIdExtendableResource(  );
        _strResourceType = resourceExtender.getExtendableResourceType(  );
        _strName = resourceExtender.getName(  );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdExtendableResource(  )
    {
        return _strIdExtendableResource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceType(  )
    {
        return _strResourceType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceName(  )
    {
        return _strName;
    }

    /**
     * Sets the id extendable resource.
     *
     * @param strId the new id extendable resource
     */
    public void setIdExtendableResource( String strId )
    {
        _strIdExtendableResource = strId;
    }

    /**
     * Sets the extendable resource type.
     *
     * @param strResourceType the new extendable resource type
     */
    public void setExtendableResourceType( String strResourceType )
    {
        _strResourceType = strResourceType;
    }

    /**
     * Sets the extendable resource name.
     *
     * @param strName the new extendable resource name
     */
    public void setExtendableResourceName( String strName )
    {
        _strName = strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceDescription( )
    {
        return _strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtendableResourceImageUrl( )
    {
        return null;
    }
}
