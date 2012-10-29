package fr.paris.lutece.plugins.extend.web;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface to add content to header of a page.
 */
public interface IExtendPageInclude
{
    /**
     * Get the html code to include to the header of the page.
     * @param request The request
     * @return The html code to include to the header of the page.
     */
    String getIncludeString( HttpServletRequest request );
}
