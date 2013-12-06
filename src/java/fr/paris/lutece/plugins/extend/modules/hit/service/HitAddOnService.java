package fr.paris.lutece.plugins.extend.modules.hit.service;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


/**
 * Manager for add on display
 */
public class HitAddOnService implements IResourceDisplayManager
{
    public static final String PROPERTY_RESOURCE_TYPE = "document";

    private static final String TAG_NUMBER_HITS = "document-number-hits";

    @Inject
    @Named( HitService.BEAN_SERVICE )
    private IHitService _hitService;

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
        if ( PROPERTY_RESOURCE_TYPE.equals( strResourceType ) )
        {
            // Add on for document type
            Hit hit = _hitService.findByParameters( String.valueOf( nResourceId ), strResourceType );
            if ( hit != null )
            {
                XmlUtil.addElement( strXml, TAG_NUMBER_HITS, hit.getNbHits( ) );
            }
        }
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
            String strPortletId, HttpServletRequest request )
    {
        // TODO Auto-generated method stub
        return;
    }

}
