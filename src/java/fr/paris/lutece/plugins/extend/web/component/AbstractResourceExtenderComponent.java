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
package fr.paris.lutece.plugins.extend.web.component;

import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.util.url.UrlItem;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * AbstractResourceExtenderComponent
 *
 */
public abstract class AbstractResourceExtenderComponent implements IResourceExtenderComponent, InitializingBean
{
    private IResourceExtender _extender;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceExtender( IResourceExtender extender )
    {
        _extender = extender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IResourceExtender getResourceExtender( )
    {
        return _extender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet( ) throws Exception
    {
        Assert.notNull( _extender, "The property 'extender' is required." );
    }

    /**
     * Get the URL to use for post backs
     * 
     * @param request
     *            The request
     * @return The URL to use for post backs
     */
    public String getPostBackUrl( HttpServletRequest request )
    {
        UrlItem urlItem = new UrlItem( request.getRequestURI( ) );
        Map<String, String [ ]> mapParameters = request.getParameterMap( );

        for ( Entry<String, String [ ]> entry : mapParameters.entrySet( ) )
        {
            urlItem.addParameter( entry.getKey( ), entry.getValue( ) [0] );
        }

        return urlItem.getUrl( );
    }
}
