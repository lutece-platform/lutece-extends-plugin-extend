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
package fr.paris.lutece.plugins.extend.service.extender;

import fr.paris.lutece.plugins.extend.web.component.IResourceExtenderComponent;
import fr.paris.lutece.portal.service.i18n.I18nService;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * AbstractResourceExtender
 *
 */
public abstract class AbstractResourceExtender implements IResourceExtender, InitializingBean
{
    private String _strKey;
    private String _strI18nTitleKey;
    private boolean _bIsConfigRequired;
    private boolean _bIsHistoryEnable;
    private boolean _bIsStateEnable;
    private IResourceExtenderComponent _resourceExtenderComponent;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey( )
    {
        return _strKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKey( String strKey )
    {
        _strKey = strKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        return I18nService.getLocalizedString( _strI18nTitleKey, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setI18nTitleKey( String strI18nTitleKey )
    {
        _strI18nTitleKey = strI18nTitleKey;
    }

    /**
     * @return the bIsConfigRequired
     */
    @Override
    public boolean isConfigRequired( )
    {
        return _bIsConfigRequired;
    }

    /**
     * @param bIsConfigRequired
     *            the bIsConfigRequired to set
     */
    public void setConfigRequired( boolean bIsConfigRequired )
    {
        _bIsConfigRequired = bIsConfigRequired;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHistoryEnable( )
    {
        return _bIsHistoryEnable;
    }

    /**
     * @param bIsHistoryEnable
     *            the bIsHistoryEnable to set
     */
    public void setHistoryEnable( boolean bIsHistoryEnable )
    {
        _bIsHistoryEnable = bIsHistoryEnable;
    }

    /**
     * @return the _bIsStateEnable
     */
    public boolean isStateEnable( )
    {
        return _bIsStateEnable;
    }

    /**
     * @param bIsStateEnable
     *            the _bIsStateEnable to set
     */
    public void setStateEnable( boolean bIsStateEnable )
    {
        this._bIsStateEnable = bIsStateEnable;
    }

    /**
     * @return the resourceExtenderComponent
     */
    public IResourceExtenderComponent getResourceExtenderComponent( )
    {
        return _resourceExtenderComponent;
    }

    /**
     * Sets the resource extender component.
     *
     * @param resourceExtenderComponent
     *            the new resource extender component
     */
    public void setResourceExtenderComponent( IResourceExtenderComponent resourceExtenderComponent )
    {
        _resourceExtenderComponent = resourceExtenderComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet( ) throws Exception
    {
        Assert.notNull( _strKey, "The property 'key' must be provided" );
        Assert.notNull( _strI18nTitleKey, "The property 'i18nTitleKey' must be provided" );
        Assert.notNull( _resourceExtenderComponent, "The property 'resourceExtenderComponent' must be provided" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strExtenderType )
    {
        if ( StringUtils.isNotBlank( strExtenderType ) )
        {
            return getKey( ).equals( strExtenderType );
        }

        return false;
    }

}
