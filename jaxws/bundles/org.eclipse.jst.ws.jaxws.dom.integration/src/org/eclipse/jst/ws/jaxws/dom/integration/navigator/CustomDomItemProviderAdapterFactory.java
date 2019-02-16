/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.ui.DomItemProviderAdapterFactory;

/**
 * Factory that adapts DOM objects with Item provider adapters. For {@link ISEIChildList} and 
 * {@link IWebServiceChildList} it creates specific adapter for other DOM objects delegates to
 * {@link DomItemProviderAdapterFactory}.
 * 
 * @author
 */
public class CustomDomItemProviderAdapterFactory extends DomItemProviderAdapterFactory 
{
	/**
	 * The singleton instance
	 */
	public static final CustomDomItemProviderAdapterFactory INSTANCE = new CustomDomItemProviderAdapterFactory();
	
	/**
	 * Constructor
	 */
	private CustomDomItemProviderAdapterFactory() 
	{
		super();
		
		supportedTypes.add(IWebServiceChildList.class);
		supportedTypes.add(ISEIChildList.class);
	}
	
	protected Adapter createIWebServiceChildListAdapter(final IWebServiceProject wsProj) 
	{
		return new IWebServiceChildList()
			{
				public EList<IWebService> getWSChildList()
				{
					return wsProj.getWebServices();
				}

				public Notifier getTarget() 
				{
					return wsProj;
				}

				public boolean isAdapterForType(Object type) 
				{
					return false;
				}

				public void notifyChanged(Notification notification) 
				{
					// no processing needed currently
				}

				public void setTarget(Notifier newTarget) 
				{
					// no processing needed currently
				}
			};
	}
	
	protected Adapter createISEIChildListAdapter(final IWebServiceProject wsProj) 
	{
		return new ISEIChildList()
			{
				public EList<IServiceEndpointInterface> getSEIChildList()
				{
					return wsProj.getServiceEndpointInterfaces();
				}

				public Notifier getTarget() 
				{
					return wsProj;
				}

				public boolean isAdapterForType(Object type) 
				{
					return false;
				}

				public void notifyChanged(Notification notification) 
				{
					// no processing needed currently
				}

				public void setTarget(Notifier newTarget) 
				{
					// no processing needed currently
				}
			};
	}
	
	@Override
	public Adapter adapt(Notifier notifier, Object type)
	{
		if (notifier instanceof IWebServiceProject && type instanceof Class)
		{
			if(type.equals(IWebServiceChildList.class))
			{
				return adaptIWebServiceChildList((IWebServiceProject)notifier);
			}
			else if(type.equals(ISEIChildList.class))
			{
				return adaptISEIChildList((IWebServiceProject)notifier);
			}
		}
		
		return super.adapt(notifier, type);
	}
	
	protected Adapter adaptISEIChildList(final IWebServiceProject wsProject)
	{
		for(Adapter adapter : wsProject.eAdapters())
		{
			if(adapter instanceof ISEIChildList) {
				return adapter;
			}
		}
		
		final Adapter adapter = createISEIChildListAdapter(wsProject);
		associate(adapter, wsProject);
		
		return adapter;
	}
	
	protected Adapter adaptIWebServiceChildList(final IWebServiceProject wsProject)
	{
		for(Adapter adapter : wsProject.eAdapters())
		{
			if(adapter instanceof IWebServiceChildList) {
				return adapter;
			}
		}
		
		final Adapter adapter = createIWebServiceChildListAdapter(wsProject);
		associate(adapter, wsProject);
		
		return adapter;		
	}
}
