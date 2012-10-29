package fr.paris.lutece.plugins.extend.web;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Class that generates included content of page header.
 */
public class ExtendPageIncluder implements PageInclude
{

    private static final String MARK_EXTEND_META = "extend_meta";

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        StringBuilder sbMeta = new StringBuilder( );

        List<IExtendPageInclude> listPageInclude = SpringContextService.getBeansOfType( IExtendPageInclude.class );

        for ( IExtendPageInclude pageInclude : listPageInclude )
        {
            sbMeta.append( pageInclude.getIncludeString( request ) );
        }

        rootModel.put( MARK_EXTEND_META, sbMeta.toString( ) );
    }

}
