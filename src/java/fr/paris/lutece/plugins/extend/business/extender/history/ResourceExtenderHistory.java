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
package fr.paris.lutece.plugins.extend.business.extender.history;

import java.util.Date;

import javax.validation.constraints.NotNull;


/**
 * ResourceExtenderHistory.
 */
public class ResourceExtenderHistory
{
    private long _lIdHistory;
    @NotNull
    private String _strExtenderType;
    @NotNull
    private String _strIdExtendableResource;
    @NotNull
    private String _strExtendableResourceType;
    private String _strUserGuid;
    private String _strIpAddress;
    private Date _dateCreation;

    /**
     * Gets the id history.
     *
     * @return the lIdHistory
     */
    public long getIdHistory(  )
    {
        return _lIdHistory;
    }

    /**
     * Sets the id history.
     *
     * @param lIdHistory the lIdHistory to set
     */
    public void setIdHistory( long lIdHistory )
    {
        _lIdHistory = lIdHistory;
    }

    /**
     * Gets the extender type.
     *
     * @return the strExtenderType
     */
    public String getExtenderType(  )
    {
        return _strExtenderType;
    }

    /**
     * Sets the extender type.
     *
     * @param strExtenderType the strExtenderType to set
     */
    public void setExtenderType( String strExtenderType )
    {
        _strExtenderType = strExtenderType;
    }

    /**
     * Gets the id extendable resource.
     *
     * @return the strIdExtendableResource
     */
    public String getIdExtendableResource(  )
    {
        return _strIdExtendableResource;
    }

    /**
     * Sets the id extendable resource.
     *
     * @param strIdExtendableResource the strIdExtendableResource to set
     */
    public void setIdExtendableResource( String strIdExtendableResource )
    {
        _strIdExtendableResource = strIdExtendableResource;
    }

    /**
     * Gets the extendable resource type.
     *
     * @return the extendableResourceType
     */
    public String getExtendableResourceType(  )
    {
        return _strExtendableResourceType;
    }

    /**
     * Sets the extendable resource type.
     *
     * @param strExtendableResourceType the extendableResourceType to set
     */
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

    /**
     * Gets the user guid.
     *
     * @return the strUserGuid
     */
    public String getUserGuid(  )
    {
        return _strUserGuid;
    }

    /**
     * Sets the user guid.
     *
     * @param strUserGuid the strUserGuid to set
     */
    public void setUserGuid( String strUserGuid )
    {
        _strUserGuid = strUserGuid;
    }

    /**
     * Gets the ip address.
     *
     * @return the strIpAddress
     */
    public String getIpAddress(  )
    {
        return _strIpAddress;
    }

    /**
     * Sets the ip address.
     *
     * @param strIpAddress the strIpAddress to set
     */
    public void setIpAddress( String strIpAddress )
    {
        _strIpAddress = strIpAddress;
    }

    /**
     * Gets the date creation.
     *
     * @return the dateCreation
     */
    public Date getDateCreation(  )
    {
        return _dateCreation;
    }

    /**
     * Sets the date creation.
     *
     * @param dateCreation the dateCreation to set
     */
    public void setDateCreation( Date dateCreation )
    {
        _dateCreation = dateCreation;
    }
}
