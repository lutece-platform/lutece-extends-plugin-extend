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
package fr.paris.lutece.plugins.extend.util;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.ResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.type.ExtendableResourceTypeService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.message.MessageHtml;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.beanvalidation.BeanValidationUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.ConstraintViolation;

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
    private static final String MESSAGE_ALL_RESOURCE_TYPES = "extend.labelAllResourceTypes";

    // MARKS
    private static final String MARK_EXTENDER_TYPE_LABELS = "extenderTypeLabels";
    private static final String MARK_RESOURCE_TYPE_LABELS = "resourceTypeLabels";

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
     * @deprecated use {@link #validate(HttpServletRequest, Object, String)} with the URL of the screen of the form: without it, the button of the error message only goes
     *             back in the history
     */
    @Deprecated
    public static <A> String validate( HttpServletRequest request, A bean )
    {
        return validate( request, bean, null );
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
     * @param strReturnUrl
     *            the URL of the screen of the form, opened by the button of an error message when there is no page to go back to
     * @return the JSP error
     */
    public static <A> String validate( HttpServletRequest request, A bean, String strReturnUrl )
    {
        // Check mandatory fields
        Set<ConstraintViolation<A>> constraintViolations = BeanValidationUtil.validate( bean );

        if ( constraintViolations.size( ) > 0 )
        {
            Object [ ] params = {
                    MessageHtml.of( buildStopMessage( constraintViolations ) )
            };

            return AdminMessageService.getMessageUrlReturningTo( request, MESSAGE_STOP_GENERIC_MESSAGE, params, AdminMessage.TYPE_STOP, strReturnUrl );
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
     * @deprecated use {@link #validateResourceType(HttpServletRequest, ExtendableResourceType, String)} with the URL of the screen of the form: without it, the button of the error message only goes
     *             back in the history
     */
    @Deprecated
    public static String validateResourceType( HttpServletRequest request, ExtendableResourceType resourceType )
    {
        return validateResourceType( request, resourceType, null );
    }

    /**
     * Validate.
     *
     * @param request
     *            the request
     * @param resourceType
     *            the resource type
     * @param strReturnUrl
     *            the URL of the screen of the form, opened by the button of an error message when there is no page to go back to
     * @return the string
     */
    public static String validateResourceType( HttpServletRequest request, ExtendableResourceType resourceType, String strReturnUrl )
    {
        String strJspError = validate( request, resourceType, strReturnUrl );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        if ( StringUtils.isBlank( resourceType.getKey( ) ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP, strReturnUrl );
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
     * @deprecated use {@link #validateResourceExtender(HttpServletRequest, ResourceExtenderDTO, String)} with the URL of the screen of the form: without it, the button of the error message only goes
     *             back in the history
     */
    @Deprecated
    public static String validateResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender )
    {
        return validateResourceExtender( request, resourceExtender, null );
    }

    /**
     * Validate resource extender.
     *
     * @param request
     *            the request
     * @param resourceExtender
     *            the resource extender
     * @param strReturnUrl
     *            the URL of the screen of the form, opened by the button of an error message when there is no page to go back to
     * @return the string
     */
    public static String validateResourceExtender( HttpServletRequest request, ResourceExtenderDTO resourceExtender, String strReturnUrl )
    {
        String strJspError = validate( request, resourceExtender, strReturnUrl );

        if ( StringUtils.isNotBlank( strJspError ) )
        {
            return strJspError;
        }

        if ( StringUtils.isBlank( resourceExtender.getExtenderType( ) ) || StringUtils.isBlank( resourceExtender.getIdExtendableResource( ) )
                || StringUtils.isBlank( resourceExtender.getExtendableResourceType( ) ) )
        {
            return AdminMessageService.getMessageUrlReturningTo( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP, strReturnUrl );
        }

        return StringUtils.EMPTY;
    }

    /**
     * Builds the stop message: the messages of the constraint violations, each followed by {@code <br />}. The result is
     * HTML: pass it to a message as {@code MessageHtml.of( ... )}.
     *
     * @param <A>
     *            the generic type
     * @param listErrors
     *            the list errors
     * @return the HTML of the message
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

    /**
     * Returns the label of an extender type, or its code when no extender answers it.
     *
     * @param strExtenderType
     *            the extender type
     * @param locale
     *            the locale
     * @return the label
     */
    public static String getExtenderTypeLabel( String strExtenderType, Locale locale )
    {
        IResourceExtender extender = CDI.current( ).select( ResourceExtenderService.class ).get( ).getResourceExtender( strExtenderType );
        String strLabel = ( extender != null ) ? extender.getTitle( locale ) : null;

        return StringUtils.isNotBlank( strLabel ) ? strLabel : strExtenderType;
    }

    /**
     * Returns the label the resource service gives a resource type, or its code when no service knows it.
     *
     * @param strResourceType
     *            the resource type
     * @param locale
     *            the locale
     * @return the label
     */
    public static String getResourceTypeLabel( String strResourceType, Locale locale )
    {
        if ( ResourceExtenderDTOFilter.WILDCARD_ID_RESOURCE.equals( strResourceType ) )
        {
            return I18nService.getLocalizedString( MESSAGE_ALL_RESOURCE_TYPES, locale );
        }

        ExtendableResourceType resourceType = CDI.current( ).select( ExtendableResourceTypeService.class ).get( ).findByPrimaryKey( strResourceType,
                locale );
        String strLabel = ( resourceType != null ) ? resourceType.getDescription( ) : null;

        return StringUtils.isNotBlank( strLabel ) ? strLabel : strResourceType;
    }

    /**
     * Puts in the model the labels of the extender types and resource types of the given extenders, by code.
     *
     * @param model
     *            the put operation of the model
     * @param listExtenders
     *            the extenders
     * @param locale
     *            the locale
     */
    public static void fillTypeLabels( BiConsumer<String, Object> model, Collection<ResourceExtenderDTO> listExtenders, Locale locale )
    {
        Map<String, String> mapExtenderTypeLabels = new HashMap<>( );
        Map<String, String> mapResourceTypeLabels = new HashMap<>( );

        for ( ResourceExtenderDTO extender : listExtenders )
        {
            if ( extender.getExtenderType( ) != null )
            {
                mapExtenderTypeLabels.computeIfAbsent( extender.getExtenderType( ), strType -> getExtenderTypeLabel( strType, locale ) );
            }

            if ( extender.getExtendableResourceType( ) != null )
            {
                mapResourceTypeLabels.computeIfAbsent( extender.getExtendableResourceType( ), strType -> getResourceTypeLabel( strType, locale ) );
            }
        }

        model.accept( MARK_EXTENDER_TYPE_LABELS, mapExtenderTypeLabels );
        model.accept( MARK_RESOURCE_TYPE_LABELS, mapResourceTypeLabels );
    }
}
