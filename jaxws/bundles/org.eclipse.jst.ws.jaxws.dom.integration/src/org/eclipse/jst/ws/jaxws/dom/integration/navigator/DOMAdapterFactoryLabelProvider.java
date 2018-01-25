/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationMessages;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationPlugin;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject.ILoadingDummy;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.ui.Images;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;

/**
 * DOM adapter factory that extends {@link AdapterFactoryLabelProvider} by providing
 * icons and labels for some objects that are not part of DOM and also overrides some
 * labels
 * 
 * @author 
 */
public class DOMAdapterFactoryLabelProvider extends AdapterFactoryLabelProvider implements IDescriptionProvider
{
	/**
	 * Default Constructor uses {@link CustomDomItemProviderAdapterFactory} as default
	 * adapter factory provided to the base class.
	 */
	public DOMAdapterFactoryLabelProvider()
	{
		super(CustomDomItemProviderAdapterFactory.INSTANCE);
		
		DomIntegrationPlugin.getDefault().setLabelProvider(this);
	}

	@Override
	public Image getImage(Object element)
	{
		if(element instanceof IWebServiceChildList)
		{
			return Images.INSTANCE.getImage(Images.IMG_WEB_SERVICE_GROUP);
		}
		else if(element instanceof ISEIChildList)
		{
			return Images.INSTANCE.getImage(Images.IMG_SEI_GROUP);
		}
		else if(element instanceof ILoadingWsProject) 
		{
			return Images.INSTANCE.getImage(Images.IMG_DOM_WS_PROVIDER);
		}
		else if (element instanceof ILoadingDummy) 
		{
			return null;
		}
		
		return super.getImage(element);
	}

	@Override
	public String getText(Object element)
	{
		if(element instanceof IWebServiceProject || element instanceof ILoadingWsProject)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_JaxWsWebServicesLabel; 
		}
		if (element instanceof ILoadingDummy) 
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_LoadingDummyLabel;
		}
		else if(element instanceof IWebServiceChildList)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_WebServicesLabel;
		}
		else if(element instanceof ISEIChildList)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_SeiLabel;
		}
		
		return super.getText(element);
	}
	
	public String getDescription(Object element)
	{
		if(element instanceof IWebService)
		{
			return ((IWebService)element).getName();
		}
		else if(element instanceof IServiceEndpointInterface)
		{
			return ((IServiceEndpointInterface)element).getName();
		}
		else if(element instanceof IWebServiceProject || element instanceof ILoadingWsProject)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_JaxWsWebServicesLabel;
		}
		else if(element instanceof IWebServiceChildList)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_WebServicesLabel;
		}
		else if(element instanceof ISEIChildList)
		{
			return DomIntegrationMessages.DOMAdapterFactoryLabelProvider_SeiLabel;
		}
		else if(element instanceof IWebMethod)
		{
			return ((IWebMethod)element).getName();
		}
		
		return super.getText(element);
	}
	
	public void fireLabelProviderChanged(Object element) 
	{
		for (ILabelProviderListener labelProviderListener : labelProviderListeners) {
			labelProviderListener.labelProviderChanged(new LabelProviderChangedEvent(this, element));
		}
	}
	
	@Override
	protected Image getDefaultImage(Object object)
	{
		// No default images 
		return null;
	}
}
