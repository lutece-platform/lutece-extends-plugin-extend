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
package fr.paris.lutece.plugins.extend.util;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;

import org.apache.commons.lang.StringUtils;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;

/**
 *
 * ExtendUtils
 *
 */
public final class ExtendUtils
{
    // CONSTANTS
    private static final String HTML_BR = "<br />";

    // MESSAGES
    private static final String MESSAGE_STOP_GENERIC_MESSAGE = "extend.message.stop.genericMessage";

    /**
     * Instantiates a new extend utils.
     */
    private ExtendUtils( )
    {
    }

    /**
     * Validate.
     *
     * @param <A>
     *            the generic type
     * @param request
     *            the request
     * @param bean
     *            the resource type
     * @return the JSP error
     */
    public static <A> String validate( HttpServletRequest request, A bean )
    {
        // Check mandatory fields
        Set<ConstraintViolation<A>> constraintViolations = BeanValidationUtil.validate( bean );

        if ( !constraintViolations.isEmpty( ) )
        {
            Object [ ] params = {
                    buildStopMessage( constraintViolations )
            };

            return AdminMessageService.getMessageUrl( request, MESSAGE_STOP_GENERIC_MESSAGE, params, AdminMessage.TYPE_STOP );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Validate.
     *
     * @param request
     *            the request
     * @param resourceType
     *            the resource type
     * @return the string
     */
    public static String validateResourceType( HttpServletRequest request, ExtendableResourceType resourceType )
    {
        String strJspError = validate( request, resourceType );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        if ( StringUtils.isBlank( resourceType.getKey( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Validate resource extender.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the resource extender
     * @return the string
     */
    public static String validateResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        String strJspError = validate( request, resourceExtender );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        if ( StringUtils.isBlank( resourceExtender.getExtenderType( ) ) || StringUtils.isBlank( resourceExtender.getIdExtendableResource( ) )
                || StringUtils.isBlank( resourceExtender.getExtendableResourceType( ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Builds the stop message.
     *
     * @param <A>
     *            the generic type
     * @param listErrors
     *            the list errors
     * @return the string
     */
    public static <A> String buildStopMessage( Set<ConstraintViolation<A>> listErrors )
    {
        StringBuilder sbError = new StringBuilder( );

        if ( ( listErrors != null ) && !listErrors.isEmpty( ) )
        {
            for ( ConstraintViolation<A> error : listErrors )
            {
                sbError.append( error.getMessage( ) );
                sbError.append( HTML_BR );
            }
        }

        return sbError.toString( );
    }
}
