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
package org.eclipse.jst.ws.jaxws.dom.ui.tests.internal;

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.ui.DomItemProviderAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.ui.IJavaWebServiceElementItemProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.IServiceEndpointInterfaceItemProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.IWebMethodItemProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.IWebParamItemProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.IWebServiceItemProvider;
import org.eclipse.jst.ws.jaxws.dom.ui.IWebServiceProjectItemProvider;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.jmock.Mock;

public class AllDomItemProviderTest extends MockObjectTestCase 
{
	private DomItemProviderAdapterFactory itemProvider; 
	
	@Override
	public void setUp()
	{
		itemProvider = new DomItemProviderAdapterFactory();
	}
	
	@Override
	public void tearDown()
	{
		itemProvider.dispose();
	}
	
	public void testDOMItemProvider()
	{
		assertNotNull(itemProvider.getRootAdapterFactory());
		itemProvider.setParentAdapterFactory(null);
		assertNotNull(itemProvider.getRootAdapterFactory());
		itemProvider.removeListener(null);
		assertNotNull(itemProvider.getRootAdapterFactory());
	}
	
	public void testIJavaWebServiceElementItemProvider()
	{
		IJavaWebServiceElementItemProvider javaItemProvider = (IJavaWebServiceElementItemProvider) itemProvider.createIJavaWebServiceElementAdapter();
		
		assertNotNull(javaItemProvider);
		
		List<IItemPropertyDescriptor> propDescs = javaItemProvider.getPropertyDescriptors(null);
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		Mock javaWSElem = mock(IJavaWebServiceElement.class);
		javaWSElem.expects(once()).method("getName").will(returnValue("TestName"));
		assertNotNull(javaItemProvider.getImage(new Object()));
		assertNotNull(javaItemProvider.getText(javaWSElem.proxy()));
		assertNull(javaItemProvider.getNameCategory());
	}
	
	public void testIServiceEndpointItemProvider()
	{
		IServiceEndpointInterfaceItemProvider seiItemProvider = (IServiceEndpointInterfaceItemProvider) itemProvider.createIServiceEndpointInterfaceAdapter();
		
		assertNotNull(seiItemProvider);
		
		List<IItemPropertyDescriptor> propDescs = seiItemProvider.getPropertyDescriptors(null);
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		Mock sEIElem = mock(IServiceEndpointInterface.class);
		sEIElem.expects(once()).method("getName").will(returnValue("TestName"));
		assertNotNull(seiItemProvider.getImage(new Object()));
		assertNotNull(seiItemProvider.getText(sEIElem.proxy()));
		assertNotNull(seiItemProvider.getChildrenFeatures(null));
		assertNotNull(seiItemProvider.getNameCategory());
	}
	
	public void testIWebMethodItemProvider()
	{
		IWebMethodItemProvider methodItemProvider = (IWebMethodItemProvider) itemProvider.createIWebMethodAdapter();
		
		assertNotNull(methodItemProvider);
		
		List<IItemPropertyDescriptor> propDescs = methodItemProvider.getPropertyDescriptors(null);
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		Mock wMElem = mock(IWebMethod.class);
		wMElem.expects(once()).method("getName").will(returnValue("TestName"));
		assertNotNull(methodItemProvider.getImage(new Object()));
		assertNotNull(methodItemProvider.getText(wMElem.proxy()));
		assertNotNull(methodItemProvider.getChildrenFeatures(null));
		assertNotNull(methodItemProvider.getNameCategory());
	}
	
	public void testIWebServiceItemProvider()
	{
		IWebServiceItemProvider wsItemProvider = (IWebServiceItemProvider) itemProvider.createIWebServiceAdapter();
		
		assertNotNull(wsItemProvider);
		
		List<IItemPropertyDescriptor> propDescs = wsItemProvider.getPropertyDescriptors(null);
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		Mock wsElem = mock(IWebService.class);
		wsElem.expects(once()).method("getName").will(returnValue("TestName"));
		assertNotNull(wsItemProvider.getImage(new Object()));
		assertNotNull(wsItemProvider.getText(wsElem.proxy()));
		assertNotNull(wsItemProvider.getNameCategory());
	}
	
	public void testIWebServiceProjectItemProvider()
	{
		IWebServiceProjectItemProvider wsProjItemProvider = (IWebServiceProjectItemProvider) itemProvider.createIWebServiceProjectAdapter();
		
		assertNotNull(wsProjItemProvider);
		
		List<IItemPropertyDescriptor> propDescs = wsProjItemProvider.getPropertyDescriptors(null);
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		Mock wsProjElem = mock(IWebServiceProject.class);
		wsProjElem.expects(once()).method("getName").will(returnValue("TestName"));
		assertNotNull(wsProjItemProvider.getImage(new Object()));
		assertNotNull(wsProjItemProvider.getText(wsProjElem.proxy()));
		assertNotNull(wsProjItemProvider.getChildrenFeatures(null));
	}
	
	public void testIWebMethodItemProviderGetImplementation()
	{
		IWebMethodItemProvider methodItemProvider = (IWebMethodItemProvider) itemProvider.createIWebMethodAdapter();
		assertNotNull(methodItemProvider);
		
		Mock webParam1 = mock(IWebParam.class);
		webParam1.expects(atLeastOnce()).method("getName").will(returnValue("Param1"));
		webParam1.expects(atLeastOnce()).method("getTypeName").will(returnValue("boolean"));
		Mock webParam2 = mock(IWebParam.class);
		webParam2.expects(atLeastOnce()).method("getName").will(returnValue("return"));
		webParam2.expects(atLeastOnce()).method("getTypeName").will(returnValue("java.lang.String"));
		org.eclipse.emf.common.util.EList<IWebParam> webParams = new org.eclipse.emf.common.util.BasicEList<IWebParam>();
		webParams.add((IWebParam)webParam1.proxy());
		webParams.add((IWebParam)webParam2.proxy());
		Mock webMethod = mock(IWebMethod.class);
		webMethod.expects(atLeastOnce()).method("getParameters").will(returnValue(webParams));
		webMethod.expects(atLeastOnce()).method("getImplementation").will(returnValue("testMethod(B)QString"));
		
		BasicEList<StateAdapter> adapters = new org.eclipse.emf.common.util.BasicEList<StateAdapter>();
		Mock adapter = mock(StateAdapter.class);
		adapter.stubs().method("isAdapterForType").will(returnValue(true));
		adapter.stubs().method("isChangeable").will(returnValue(false));
		adapters.add((StateAdapter)adapter.proxy());
		
		webMethod.expects(atLeastOnce()).method("eAdapters").will(returnValue(adapters));
		((IWebMethod)webMethod.proxy()).eAdapters();
		methodItemProvider.setTarget((Notifier)webMethod.proxy());
		List<IItemPropertyDescriptor> propDescs = methodItemProvider.getPropertyDescriptors(webMethod.proxy());
		
		assertNotNull(propDescs);
		assertTrue(propDescs.size() > 0);
		
		for(IItemPropertyDescriptor propDesc : propDescs)
		{
			if(propDesc.getFeature(null).equals(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION))
			{
				assertEquals(((PropertyValueWrapper)propDesc.getPropertyValue(webMethod.proxy())).getText(null), "String testMethod(boolean)");
			}
		}
	}
	
	public void testIWebParamItemProvider()
	{
		IWebParamItemProvider itemProvider = (IWebParamItemProvider) new DomItemProviderAdapterFactory().createIWebParamAdapter();
		Mock paramMock = mock(IWebParam.class);		
		assertNotNull(itemProvider.getImage(new Object()));

		paramMock.stubs().method("getName").will(returnValue("myParam"));
		paramMock.stubs().method("getTypeName").will(returnValue("java.util.List"));
		paramMock.stubs().method("getImplementation").will(returnValue("test"));
		paramMock.stubs().method("eContainer").will(returnValue(null));
		paramMock.stubs().method("eResource").will(returnValue(null));
		
		assertEquals("myParam", itemProvider.getText(paramMock.proxy()));
		assertNotNull(itemProvider.getImage(paramMock.proxy()));
		assertNotNull(itemProvider.getNameCategory());
		paramMock.stubs().method("getImplementation").will(returnValue("return"));
		assertNotNull(itemProvider.getImage(paramMock.proxy()));	
	}
	
	public void testIWebParamItemProviderForTypeName()
	{
		// IWebParamItemProvider itemProvider = (IWebParamItemProvider) new DomItemProviderAdapterFactory().createIWebParamAdapter();
		final IItemPropertyDescriptor typeNamePropertyDescriptor = findWebParamItemPropertyDescriptor(DomPackage.Literals.IWEB_PARAM__TYPE_NAME);
		
		final IWebParam webParam = DomFactory.eINSTANCE.createIWebParam();
		webParam.setHeader(true);
		webParam.setKind(WebParamKind.INOUT);
		webParam.setName("anyName"); //$NON-NLS-1$
		webParam.setPartName("anyPartName"); //$NON-NLS-1$
		webParam.setTargetNamespace("anyTargetNamespace"); //$NON-NLS-1$
		((EObject) webParam).eSet(DomPackage.Literals.IWEB_PARAM__TYPE_NAME, null);
				
		assertNull("Unexpected value.", typeNamePropertyDescriptor.getPropertyValue(webParam)); //$NON-NLS-1$

		Object propertyValueWrapper = null;
		
		((EObject) webParam).eSet(DomPackage.Literals.IWEB_PARAM__TYPE_NAME, "I"); //$NON-NLS-1$
		propertyValueWrapper = typeNamePropertyDescriptor.getPropertyValue(webParam);		
		assertEquals("Unresolved value.", "int", ((IItemPropertySource) propertyValueWrapper).getEditableValue(null)); //$NON-NLS-1$ //$NON-NLS-2$
		
		((EObject) webParam).eSet(DomPackage.Literals.IWEB_PARAM__TYPE_NAME, "[QString;"); //$NON-NLS-1$
		propertyValueWrapper = typeNamePropertyDescriptor.getPropertyValue(webParam);		
		assertEquals("Unresolved value.", "String[]", ((IItemPropertySource) propertyValueWrapper).getEditableValue(null)); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private IItemPropertyDescriptor findWebParamItemPropertyDescriptor(final EAttribute attribute)
	{
		final IWebParamItemProvider itemProvider = (IWebParamItemProvider) new DomItemProviderAdapterFactory().createIWebParamAdapter();
		List<IItemPropertyDescriptor> descriptors = itemProvider.getPropertyDescriptors(null);
					
		for (IItemPropertyDescriptor descriptor : descriptors)			
		{
			if (descriptor.getFeature(null).equals(attribute))
			{
				return descriptor;				
			}
		}
		
		return null;
	}
	
	public interface StateAdapter extends IPropertyState, Adapter
	{
	}
}
