package fr.paris.lutece.plugins.extend.service.extender.factory;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;


/**
 * Generic service for reading data extend
 * @param <T> the type of elements in this ExtenderType (example: Hit)
 */
public class ExtenderType<T>{
	
	/**
	 * The type of extender
	 */
	private String _strType;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to export in the file export
	 */
	private BinaryOperator<String> _getInfoForExport;
	/**
	 * BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 */
	private BinaryOperator<String> _getInfoForRecap;
	/**
	 * BiFunction to get the optional extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtender
	 */
	private BiFunction<String, String, ? extends T > _getInfoExtender;
	
	/**
	 * BiFunction to get the list of extender type information object
	 * @param <T> the type of the output arguments of the getInfoExtenderByList
	 */
	private BiFunction<List<String>, String, List<T>> _getInfoExtenderByList;

	/**
	 * Constructor (example: new ExtenderType (HitResourceExtender.EXTENDER_TYPE))
	 * @param strType
	 * 			the type of extender
	 */
	public ExtenderType ( String strType ){
		Objects.requireNonNull( strType );
		_strType= strType ;
	}
	/**
	 * Constructor ExtenderType
	 * 
	 * @param strType
	 * 		  the type of extender
	 * @param getInfoExtender
	 * 		  	the BiFunction to get the optional extender type information object
	 * @param getInfoExtenderByList
	 * 		   	the BiFunction to get the list of extender type information object
	 * @param getInfoForExport
	 * 			the BinaryOperator to get the information value associate to the ExtenderType to export in the file export
	 * @param getInfoForRecap
	 * 			the BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 * @param <T> the type of the output arguments of the getInfoExtende and getInfoExtenderByList
	 * @see ResourceExtenderServiceFacotory#initExtenderType constructor example
	 */
	public ExtenderType( String strType , BiFunction< String, String, ? extends T > getInfoExtender, BiFunction< List<String>, String, List<T> > getInfoExtenderByList, BinaryOperator<String > getInfoForExport, BinaryOperator< String > getInfoForRecap){
		
		Objects.requireNonNull( strType );
		Objects.requireNonNull( getInfoExtender );
		Objects.requireNonNull( getInfoExtenderByList );
		Objects.requireNonNull( getInfoForExport );
		Objects.requireNonNull( getInfoForRecap );
		
		 _strType= strType;
		 _getInfoExtender = getInfoExtender;
		 _getInfoExtenderByList= getInfoExtenderByList;
		 _getInfoForExport= getInfoForExport;
		 _getInfoForRecap = getInfoForRecap;
	}
	/**
	 * Get type of the extender
	 * @return _strType
	 */
	public String getType( ){
		
		return _strType;
	}
	/**
	 * Get the information value associate to the extenderType to export in the file export
	 * 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extenderType to export in the file export
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public String getInfoForExport( String strIdExtendableResource , String strExtendableResourceType  ) throws InfoExtenderException{
		
		if(_getInfoForExport != null )
			return _getInfoForExport.apply( strIdExtendableResource,  strExtendableResourceType ); 
	    throw new InfoExtenderException( "getInfoForExport service note found");
	}
	/**
	 * Get the information value associate to the extenderType to write in the recap
	 * 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extenderType to write in the recap
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public String getInfoForRecap( String strIdExtendableResource , String strExtendableResourceType  ) throws InfoExtenderException{
		
		if( _getInfoForRecap != null)
			return _getInfoForRecap.apply( strIdExtendableResource, strExtendableResourceType );
		throw new InfoExtenderException( "getInfoForRecap service note found" );
	}
	/**
	 * Get optional extenderType(hit or rate) information object (example: Optional<Hit> or Optional<Rating>..)
	 * 
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @param T
	 * 			the extenderType information object (example: Hit)
	 * @return the optional extenderType(example: hit or rate) information object (example: Optional<Hit> or Optional<Rating>..)
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public T getInfoExtender( String strIdExtendableResource , String strExtendableResourceType  ) throws InfoExtenderException{
		if(_getInfoExtender != null )
			return _getInfoExtender.apply( strIdExtendableResource, strExtendableResourceType ); 
		throw new InfoExtenderException("getInfoExtender service note found");
	}
	/**
	 * Get list extenderType information object (example: List<Hit> or List<Rating>..)
	 * 
	 * @param listIdExtendableResource
	 * 					the list of IdExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @return list of extender type information object (example: List<Hit> or List<Rating>..)
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public List<T> getInfoExtenderByList( List<String> listIdExtendableResource , String strExtendableResourceType  ) throws InfoExtenderException{
		if(_getInfoExtenderByList != null )
			return _getInfoExtenderByList.apply( listIdExtendableResource, strExtendableResourceType ); 
		throw new InfoExtenderException("_getInfoExtenderByList service note found");
	}
	/**
	 * Sets the BinaryOperator to get the information value associate to the ExtenderType to export in the file export
	 * @param binaryOperatorInfoForExport
	 * 			the binaryOperatorInfoForExport
	 */
	public void setInfoForExport( BinaryOperator< String> binaryOperatorInfoForExport  ){

		Objects.requireNonNull( binaryOperatorInfoForExport );	
		_getInfoForExport =binaryOperatorInfoForExport ;
	}
	/**
	 * Sets the BinaryOperator to get the information value associate to the ExtenderType to write in the recap
	 * @param binaryOperatorInfoForRecap
	 * 			the binaryOperatorInfoForRecap
	 */
	public void setInfoForRecap( BinaryOperator< String> binaryOperatorInfoForRecap  ){
		 
		Objects.requireNonNull( binaryOperatorInfoForRecap );	
		_getInfoForRecap = binaryOperatorInfoForRecap;
	}
	/**
	 * Sets the BiFunction to get the optional extender type information object
	 * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @param biFunctionInfoExtender
	 * 			the biFunctionInfoExtender
	 */
	public void setInfoExtender( BiFunction< String, String, ? extends T > biFunctionInfoExtender ){
		
		Objects.requireNonNull( biFunctionInfoExtender );	
		_getInfoExtender= biFunctionInfoExtender; 
	}
	/**
	 * Sets the BiFunction to get the list of extender type information object
	 * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @param biFunctionInfoExtender
	 * 			the biFunctionInfoExtender
	 */
	public void setInfoExtenderByList( BiFunction< List<String>, String, List<T> > biFunctionInfoExtender ){
		
		Objects.requireNonNull( biFunctionInfoExtender );	
		_getInfoExtenderByList= biFunctionInfoExtender; 
	}
}
