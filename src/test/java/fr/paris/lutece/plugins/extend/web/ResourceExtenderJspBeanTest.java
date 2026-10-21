/*
 * Copyright (c) 2002-2026, City of Paris
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
package fr.paris.lutece.plugins.extend.web;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.ISecurityTokenService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

/**
 * Tests the screen the button of an error message of the extenders back office opens when there is no page to go back to.
 */
public class ResourceExtenderJspBeanTest extends LuteceTestCase
{
    private static final String JSP_MANAGE = "jsp/admin/plugins/extend/ManageResourceExtendersByResource.jsp";

    @Inject
    private ResourceExtenderJspBean _bean;
    @Inject
    private ISecurityTokenService _securityTokenService;

    private MockHttpServletRequest getUserRequest( )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUserUtils.registerAdminUserWithRight( request, new AdminUser( ), ResourceExtenderJspBean.RIGHT_MANAGE_RESOURCE_EXTENDER );

        return request;
    }

    /**
     * The form of the default resource of a new extender, opened without a resource, returns to the form creating an extender.
     *
     * @throws AccessDeniedException
     *             if the user is refused
     */
    @Test
    public void testCreateDefaultResourceExtenderWithoutResourceReturnsToTheCreationForm( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.addParameter( "view", ResourceExtenderJspBean.VIEW_CREATE_DEFAULT_RESOURCE_EXTENDER );

        _bean.processController( request, new MockHttpServletResponse( ) );

        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( JSP_MANAGE + "?view=" + ResourceExtenderJspBean.VIEW_CREATE_RESOURCE_EXTENDER ), strBackUrl );
    }

    /**
     * Disabling an extender that does not exist returns to the list of the extenders.
     *
     * @throws AccessDeniedException
     *             if the user is refused
     */
    @Test
    public void testDisableUnknownExtenderReturnsToTheList( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = getUserRequest( );
        request.addParameter( "action", ResourceExtenderJspBean.ACTION_DISABLE_EXTENDER );
        request.addParameter( "idExtender", "999999" );
        request.addParameter( SecurityTokenService.PARAMETER_TOKEN,
                _securityTokenService.getToken( request, ResourceExtenderJspBean.ACTION_DISABLE_EXTENDER ) );

        _bean.processController( request, new MockHttpServletResponse( ) );

        String strBackUrl = AdminMessageService.getMessage( request ).getBackUrl( );
        assertTrue( strBackUrl.endsWith( JSP_MANAGE + "?view=" + ResourceExtenderJspBean.VIEW_MANAGE_RESOURCE_EXTENDERS_BY_RESOURCE + "&session=session" ),
                strBackUrl );
    }
}
