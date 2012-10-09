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
package fr.paris.lutece.plugins.extend.service.type;

import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.type.ExtendableResourceType;
import fr.paris.lutece.plugins.extend.business.type.IExtendableResourceTypeDAO;
import fr.paris.lutece.plugins.extend.service.ExtendPlugin;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.inject.Inject;


/**
 *
 * ExtendableResourceTypeService
 *
 */
public class ExtendableResourceTypeService implements IExtendableResourceTypeService
{
    /** The Constant BEAN_SERVICE. */
    public static final String BEAN_SERVICE = "extend.extendableResourceTypeService";
    @Inject
    private IExtendableResourceTypeDAO _resourceTypeDAO;
    @Inject
    private IResourceExtenderService _extenderService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void create( ExtendableResourceType resourceType )
    {
        _resourceTypeDAO.insert( resourceType, ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void update( ExtendableResourceType resourceType )
    {
        _resourceTypeDAO.store( resourceType, ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional( ExtendPlugin.TRANSACTION_MANAGER )
    public void remove( String strKey )
    {
        // First remove the resource extenders
        ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter(  );
        filter.setFilterExtendableResourceType( strKey );

        for ( ResourceExtenderDTO resourceExtender : _extenderService.findByFilter( filter ) )
        {
            try
            {
                _extenderService.remove( resourceExtender.getIdExtender(  ) );
            }
            catch ( Exception ex )
            {
                // Something wrong happened... a database check might be needed
                AppLogService.error( ex.getMessage(  ) + " when deleting a resource extender", ex );
            }

            _extenderService.doDeleteResourceAddOn( resourceExtender );
        }

        _resourceTypeDAO.delete( strKey, ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendableResourceType findByPrimaryKey( String strKey )
    {
        return _resourceTypeDAO.load( strKey, ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ExtendableResourceType> findAll(  )
    {
        return _resourceTypeDAO.loadAll( ExtendPlugin.getPlugin(  ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList findAllAsRef(  )
    {
        ReferenceList ref = new ReferenceList(  );

        for ( ExtendableResourceType resourceType : findAll(  ) )
        {
            ref.addItem( resourceType.getKey(  ), resourceType.getKey(  ) );
        }

        return ref;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuplicate( String strKey )
    {
        return _resourceTypeDAO.isDuplicate( strKey, ExtendPlugin.getPlugin(  ) );
    }
}
