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
package fr.paris.lutece.plugins.extend.modules.hit.web.component;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.service.IHitService;
import fr.paris.lutece.plugins.extend.modules.hit.service.extender.HitResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.util.JSONUtils;
import fr.paris.lutece.plugins.extend.web.component.NoConfigResourceExtenderComponent;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * HitResourceExtenderComponent
 *
 */
public class HitResourceExtenderComponent extends NoConfigResourceExtenderComponent
{
    // MARKS
    private static final String MARK_HIT = "hit";

    // TEMPLATES
    private static final String TEMPLATE_HIT = "skin/plugins/socialhub/modules/hit/hit.html";
    private static final String TEMPLATE_INFO = "admin/plugins/socialhub/modules/hit/hit_info.html";

    // CONSTANTS
    private static final String JSON_KEY_SHOW = "show";

    // SERVICES
    @Inject
    private IHitService _hitService;
    @Inject
    private IResourceExtenderHistoryService _resourceHistoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildXmlAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        StringBuffer strXml )
    {
        // Nothing yet
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageAddOn( String strIdExtendableResource, String strExtendableResourceType, String strParameters,
        HttpServletRequest request )
    {
        Hit hit = _hitService.findByParameters( strIdExtendableResource, strExtendableResourceType );

        if ( hit == null )
        {
            hit = new Hit(  );
            hit.setIdExtendableResource( strIdExtendableResource );
            hit.setExtendableResourceType( strExtendableResourceType );
            // By default, start hit at 0
            hit.setNbHits( 0 );
            _hitService.create( hit );
        }

        _hitService.incrementHit( hit );

        // Add to the resource extender history
        _resourceHistoryService.create( HitResourceExtender.EXTENDER_TYPE, strIdExtendableResource,
            strExtendableResourceType, request );

        if ( showInFO( strParameters ) )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_HIT, hit );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HIT, request.getLocale(  ), model );

            return template.getHtml(  );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInfoHtml( ResourceExtenderDTO resourceExtender, Locale locale, HttpServletRequest request )
    {
        Hit hit = _hitService.findByParameters( resourceExtender.getIdExtendableResource(  ),
                resourceExtender.getExtendableResourceType(  ) );

        if ( hit != null )
        {
            Map<String, Object> model = new HashMap<String, Object>(  );
            model.put( MARK_HIT, hit );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_INFO, locale, model );

            return template.getHtml(  );
        }

        return StringUtils.EMPTY;
    }

    /**
    * Show in fo.
    *
    * @param strParameters the str parameters
    * @return true, if successful
    */
    private boolean showInFO( String strParameters )
    {
        boolean bShow = true;
        JSONObject jsonParameters = JSONUtils.parseParameters( strParameters );

        if ( jsonParameters != null )
        {
            try
            {
                bShow = jsonParameters.getBoolean( JSON_KEY_SHOW );
            }
            catch ( JSONException je )
            {
                AppLogService.debug( je.getMessage(  ), je );
            }
        }

        return bShow;
    }
}
