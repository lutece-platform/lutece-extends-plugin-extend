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
package fr.paris.lutece.plugins.extend.modules.hit.service.extender;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.service.IHitService;
import fr.paris.lutece.plugins.extend.modules.hit.web.component.HitResourceExtenderComponent;
import fr.paris.lutece.plugins.extend.service.extender.AbstractResourceExtender;
import fr.paris.lutece.plugins.extend.web.component.IResourceExtenderComponent;

import java.security.spec.ECFieldF2m;

import org.apache.commons.lang3.StringUtils;

import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.enterprise.inject.spi.CDI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Locale;
import fr.paris.lutece.portal.service.i18n.I18nService;

/**
 *
 * Extender for hit functionality. <br />
 * Macro to use in the templates : <strong>@Extender[idResource,resourceType,hit,{show:true|false}]@</strong> <br />
 * Example : @Extender[22,document,hit,{show:true}]@
 */

@ApplicationScoped
@Named( "extend.hitResourceExtender" )
public class HitResourceExtender extends AbstractResourceExtender
{
    /** The Constant EXTENDER_TYPE. */
    public static final String EXTENDER_TYPE = "hit";

    @Inject
    private IHitService _hitService;

    @Inject
    @ConfigProperty( name = "extend.hit.titleKey", defaultValue = "module.extend.hit.extender.labelHit" )
    private String titleKey;

    private HitResourceExtenderComponent resourceExtenderComponent;

    HitResourceExtender( )
    {

    }

    @PostConstruct
    public void producesHitResourceExtender( )
    {
        setResourceExtenderComponent( resourceExtenderComponent );
        setKey( EXTENDER_TYPE );
        setI18nTitleKey( I18nService.getLocalizedString( titleKey, Locale.getDefault( ) ) );
        setConfigRequired( true );
        setHistoryEnable( false );
        setStateEnable( true );
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContent( String strIdExtendableResource, String strExtendableResourceType, String strParameters, HttpServletRequest request )
    {
        return getResourceExtenderComponent( ).getPageAddOn( strIdExtendableResource, strExtendableResourceType, strParameters, request );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCreateResourceAddOn( ResourceExtenderDTO extender )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doDeleteResourceAddOn( ResourceExtenderDTO extender )
    {
        _hitService.removeByResource( extender.getIdExtendableResource( ), extender.getExtendableResourceType( ) );
    }
}
