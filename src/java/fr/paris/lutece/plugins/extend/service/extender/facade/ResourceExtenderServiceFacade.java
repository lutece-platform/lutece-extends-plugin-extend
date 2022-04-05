package fr.paris.lutece.plugins.extend.service.extender.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;
import fr.paris.lutece.plugins.extend.modules.hit.business.HitHome;
import fr.paris.lutece.plugins.extend.modules.hit.service.extender.HitResourceExtender;

/**
 *	Facade service for reading data extend
 */
public class ResourceExtenderServiceFacade{
	
	/**
	 * The list of Extender Type (example: ExtenderType<Hit>, ExtenderType<Rating>........... )
	 * @see ExtenderType
	 */
	private static  List<ExtenderType<? extends IExtendableResourceResult>> _listExtenderType = initExtenderType( );
	
	/**
	 * Private Constructor
	 */
	private ResourceExtenderServiceFacade() {
		
	}

	/**
	 * Get the information value associate to the extenderType to export in the file export
	 * @param extenderType
	 * 				the extender type ( example: hit, rating ....)
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extenderType to export in the file export
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public static String getInfoForExport( String extenderType, String strIdExtendableResource, String strExtendableResourceType ) throws InfoExtenderException  {
		
		return _listExtenderType.stream().filter( type ->  type.getType( ).equals( extenderType ) )
				.findAny().orElseThrow( InfoExtenderException::new ).getInfoForExport( strIdExtendableResource, strExtendableResourceType );
				
	}
	/**
	 * Get the information value associate to the extenderType to write in the recap
	 * @param extenderType
	 * 				the extender type (example: hit, rating ....)
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @return the information value associate to the extenderType to write in the recap
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	public static String getInfoForRecap( String extenderType, String strIdExtendableResource, String strExtendableResourceType ) throws InfoExtenderException {
		
		return _listExtenderType.stream().filter( type -> type.getType( ).equals( extenderType ) )
				.findAny().orElseThrow( InfoExtenderException::new ).getInfoForRecap( strIdExtendableResource, strExtendableResourceType );
		
	}
	/**
	 * Get list extenderType information object (example: List<Hit> or List<Rating>..)
	 * @param extenderType
	 * 				the extender type (example: hit, rating ....)
	 * @param strIdExtendableResource
	 * 					the idExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @return the list extenderType(example: hit or rate) information object (example: Optional<Hit> or Optional<Rating>..)
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IExtendableResourceResult> List<T>  getInfoExtender( String extenderType, String strIdExtendableResource, String strExtendableResourceType ) throws InfoExtenderException {
		
		return  (List< T >) _listExtenderType.stream().filter( type ->  type.getType( ).equals( extenderType ))
				.findAny().orElseThrow( InfoExtenderException::new ).getInfoExtender( strIdExtendableResource, strExtendableResourceType );			
	}
	/**
	 * Get list extenderType information object (example: List<Hit> or List<Rating>..)
	 * @param extenderType
	 * 				the extender type (example: hit, rating ....)
	 * @param listIdExtendableResource
	 * 					the list of IdExtendableResource
	 * @param strExtendableResourceType
	 * 				the extendableResourceType 
	 * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @return list of extenderType information object (example: List<Hit> or List<Rating>..)
	 * @throws InfoExtenderException
	 * 				the InfoExtenderException
	 */
	@SuppressWarnings("unchecked")
	public <T extends IExtendableResourceResult> List<T> getInfoExtenderByList( String extenderType, List<String> listIdExtendableResource , String strExtendableResourceType  ) throws InfoExtenderException{
	
		return  (List<T>) _listExtenderType.stream().filter( type ->  type.getType( ).equals( extenderType ))
				.findAny().orElseThrow( InfoExtenderException::new ).getInfoExtenderByList( listIdExtendableResource, strExtendableResourceType );
	
	}
	/**
	 * Get  Optional<ExtenderType<T>>
	 * @param <T> 
	 * 			the type of extender type (example: Hit or Rating or......)
	 * @param clazz
	 * 			the class type of extender type (example: Hit.class or Rating.class or....)
	  * @param T 
	 * 			the extenderType information object (example: Hit)
	 * @return the Optional<ExtenderType<T>> 
	 * @see ExtenderType<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IExtendableResourceResult> Optional<ExtenderType<T>>  getExtenderType( Class<T> clazz ) {

		return  Optional.ofNullable((ExtenderType<T>) _listExtenderType.stream().filter( tp ->  tp.getClassType( ).isAssignableFrom( clazz ) ).findAny().orElse( null ));	  		
	}
	/** 
	 * Appends the extenderType element to the end of this list
	 * @param extenderType the ExtenderType<T> (example: ExtenderType<Hit> or ExtenderType<Rating> or ... )
	 * @param ? 
	 * 			the extenderType information object (example: Hit)
	 * @return true if this list changed as a result of the call
	 */
	public static <T extends IExtendableResourceResult> boolean addExtenderType( final ExtenderType<T> extenderType ) {
			
			if ( extenderType != null && extenderType.getType( ) != null && _listExtenderType.stream().noneMatch( type ->  type.getClassType( ).isAssignableFrom( extenderType.getClassType( ) ) || extenderType.getClassType( ).isAssignableFrom( type.getClassType( ) ) || type.getType().equals( extenderType.getType( ) ))) {
				_listExtenderType.add( extenderType );
				return true;
			}
			return false;
	}
	/**
	 * @param ? 
	 * 			the extender type information object (example: Hit)
	 * @return The list of Extender Type (example: ExtenderType<Hit>, ExtenderType<Rating>...)
	 */
	public static  List<ExtenderType<? extends IExtendableResourceResult>> getListExtenderType() {
		
		return _listExtenderType;
	}
	/**
	 * initialization the list of Extender Type with ExtenderType<Hit>
	 * @return The list of Extender Type with (ExtenderType<Hit>)
	 */
	private static  List<ExtenderType<? extends IExtendableResourceResult>> initExtenderType(  ) {
		 
		return new ArrayList<>(Arrays.asList( 				
				new ExtenderType< >(
						Hit.class,
						HitResourceExtender.EXTENDER_TYPE,
						(strIdExtendableResource,  strExtendableResourceType ) -> HitHome.findByParameters(strIdExtendableResource, strExtendableResourceType).map(Collections::singletonList).orElseGet(Collections::emptyList),
						HitHome::findByResourceList,
						(strIdExtendableResource,  strExtendableResourceType) -> HitHome.findByParameters(strIdExtendableResource, strExtendableResourceType).map( hit -> hit != null?String.valueOf (hit.getNbHits( )):null).orElse(null),						
						(strIdExtendableResource,  strExtendableResourceType) ->  HitHome.findByParameters(strIdExtendableResource, strExtendableResourceType).map( hit -> hit != null?String.valueOf (hit.getNbHits( )):null).orElse(null)
							
				)
		));
	}
}
