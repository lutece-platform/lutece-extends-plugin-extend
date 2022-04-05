package fr.paris.lutece.plugins.extend.service.extender.facade;

public class InfoExtenderException extends Exception {

	/**
	 * Serial Version 
	 */
	private static final long serialVersionUID = 3026283101427078494L;

	
	/**
     * Constructor
     *
     * @param strMessage
     *            The error message
     */
    public InfoExtenderException( String strMessage )
    {

        super( strMessage );
    }
    /**
     * Constructor
     *
     */
    public InfoExtenderException(  )
    {

        super(  );
    }
}
