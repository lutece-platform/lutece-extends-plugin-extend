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
package fr.paris.lutece.plugins.extend.service.converter;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;

import org.springframework.beans.factory.InitializingBean;

import org.springframework.util.Assert;

import java.util.StringTokenizer;


/**
 * ExtenderStringMapper.
 */
public class ExtenderStringMapper implements IStringMapper<ResourceExtenderDTO>, InitializingBean
{
    private String _strDelimeter;

    /**
     * Sets the delimeter.
     *
     * @param strDelimeter the new delimeter
     */
    public void setDelimeter( String strDelimeter )
    {
        _strDelimeter = strDelimeter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceExtenderDTO map( String strToMap )
    {
        ResourceExtenderDTO resourceExtender = new ResourceExtenderDTO(  );
        StringTokenizer token = new StringTokenizer( strToMap, _strDelimeter );
        int nIndex = 1;

        while ( token.hasMoreTokens(  ) )
        {
            switch ( nIndex )
            {
                case 1:
                    resourceExtender.setIdExtendableResource( token.nextToken(  ) );

                    break;

                case 2:
                    resourceExtender.setExtendableResourceType( token.nextToken(  ) );

                    break;

                case 3:
                    resourceExtender.setExtenderType( token.nextToken(  ) );

                    break;

                case 4:
                    resourceExtender.setParameters( token.nextToken(  ) );

                    break;

                default:
                    resourceExtender.setParameters( resourceExtender.getParameters(  ) + _strDelimeter +
                        token.nextToken(  ) );
            }

            nIndex++;
        }

        return resourceExtender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet(  ) throws Exception
    {
        Assert.notNull( _strDelimeter, "The property 'delimeter' must be set." );
    }
}
