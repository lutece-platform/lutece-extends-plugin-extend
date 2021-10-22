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
package fr.paris.lutece.plugins.extend.business.extender;

import fr.paris.lutece.portal.service.rbac.RBACResource;

import javax.validation.constraints.NotNull;

/**
 *
 * ResourceExtenderDTO
 *
 */
public class ResourceExtenderDTO implements RBACResource
{
    /** The Constant RESOURCE_TYPE. */
    public static final String RESOURCE_TYPE = "EXTEND_EXTENDABLE_RESOURCE";
    private int _nIdExtender;
    @NotNull
    private String _strExtenderType;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private String _strParameters;
    private String _strName;
    private boolean _bIsActive;

    /**
     * @return the nIdExtender
     */
    public int getIdExtender( )
    {
        return _nIdExtender;
    }

    /**
     * @param nIdExtender
     *            the nIdExtender to set
     */
    public void setIdExtender( int nIdExtender )
    {
        _nIdExtender = nIdExtender;
    }

    /**
     * @return the strExtenderType
     */
    public String getExtenderType( )
    {
        return _strExtenderType;
    }

    /**
     * @param strExtenderType
     *            the strExtenderType to set
     */
    public void setExtenderType( String strExtenderType )
    {
        _strExtenderType = strExtenderType;
    }

    /**
     * @return the strIdExtendableResource
     */
    public String getIdExtendableResource( )
    {
        return _strIdExtendableResource;
    }

    /**
     * @param strIdExtendableResource
     *            the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * @return the extendableResourceType
     */
    public String getExtendableResourceType( )
    {
        return _strExtendableResourceType;
    }

    /**
     * @param strExtendableResourceType
     *            the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * @return the strParameters
     */
    public String getParameters( )
    {
        return _strParameters;
    }

    /**
     * @param strParameters
     *            the strParameters to set
     */
    public void setParameters( String strParameters )
    {
        _strParameters = strParameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString( )
    {
        return "ResourceExtenderDTO [_nIdExtender=" + _nIdExtender + ", _strExtenderType=" + _strExtenderType + ", _strIdExtendableResource="
                + _strIdExtendableResource + ", _strExtendableResourceType=" + _strExtendableResourceType + ", _strParameters=" + _strParameters + "]";
    }

    /**
     * @return the strName
     */
    public String getName( )
    {
        return _strName;
    }

    /**
     * @param strName
     *            the strName to set
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeCode( )
    {
        return RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceId( )
    {
        return Integer.toString( _nIdExtender );
    }

    /**
     * @return the _bIsActive
     */
    public boolean isIsActive( )
    {
        return _bIsActive;
    }

    /**
     * @param bIsActive
     *            the _bIsActive to set
     */
    public void setIsActive( boolean bIsActive )
    {
        this._bIsActive = bIsActive;
    }
}
