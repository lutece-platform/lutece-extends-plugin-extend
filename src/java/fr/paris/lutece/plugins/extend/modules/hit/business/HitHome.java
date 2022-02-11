package fr.paris.lutece.plugins.extend.modules.hit.business;

import java.util.List;
import java.util.Optional;

import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class HitHome {

    private static IHitDAO _hitDAO = SpringContextService.getBean( "extend.hitDAO" );
    private static Plugin _plugin = ExtendPlugin.getPlugin( );

    /**
     * Private constructor
     */
    private HitHome ()
    {
    	
    }    
    /**
     * Creates the a hit extender.
     *
     * @param hit
     *            the hit
     */
    public static void create( Hit hit )
    {
        _hitDAO.insert( hit, _plugin );
    }
    /**
     * Update a hit extender.
     *
     * @param hit
     *            the hit
     */
    public static void update( Hit hit )
    {
        _hitDAO.store( hit, _plugin );
    }
    /**
    * Removes a hit extender.
    *
    * @param nIdHit
    *            the n id hit
    */
    public static void remove( int nIdExtender )
    {
        _hitDAO.delete( nIdExtender,_plugin );
    }
    /**
     * Removes a hit extender by id resource and resource type.
     * 
     * @param strIdResource
     *            The id of the resource to remove
     * @param strResourceType
     *            The type of the resource to remove
     */
    public static void removeByResource( String strIdResource, String strResourceType )
    {
        _hitDAO.deleteByResource( strIdResource, strResourceType, _plugin );
    }
    /**
     * Find.
     *
     * @param nIdExtender
     *            the n id extender
     * @return the Optional<Hit>
     */
    public static Optional<Hit> findByPrimaryKey( int nIdHit )
    {
        return Optional.ofNullable(_hitDAO.load( nIdHit, _plugin ));
    }
    /**
     * Find by id extender.
     *
     * @param strIdExtendableResource
     *            the str id extendable resource
     * @param strExtendableResourceType
     *            the str extendable resource type
     * @return the Optional<Hit>
     */
    public static Optional<Hit> findByParameters( String strIdExtendableResource, String strExtendableResourceType )
    {
        return Optional.ofNullable(_hitDAO.loadByParameters( strIdExtendableResource, strExtendableResourceType, _plugin ));
    }
    /**
     * Get the ids of resources ordered by their number of hits
     * 
     * @param strExtendableResourceType
     *            The type of resources to consider
     * @param nItemsOffset
     *            The offset of the items to get, or 0 to get items from the first one
     * @param nMaxItemsNumber
     *            The maximum number of items to return, or 0 to get every items
     * @return The list of ids of resources ordered by the number hits
     */
    public static List<Integer> findIdMostHitedResources( String strExtendableResourceType, int nItemsOffset, int nMaxItemsNumber )
    {
        return _hitDAO.findIdMostHitedResources( strExtendableResourceType, nItemsOffset, nMaxItemsNumber, _plugin );
    }
    /**
     * Find by list id extendable resource.
     *
     * @param listIdExtendableResource
     *            the list of str id extendable resource
     * @param strExtendableResourceType
     *            the str extendable resource type
     * @return the hit list
     */
	public static List<Hit> findByResourceList(List<String> listIdExtendableResource, String strExtendableResourceType) 
	{

		return _hitDAO.findByResourceList( listIdExtendableResource, strExtendableResourceType, _plugin );
	}
}
