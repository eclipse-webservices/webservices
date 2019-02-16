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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.serializer;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IAnnotationSerializer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.AbstractSerializerAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.serializer.WsSerializerAdapter;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * Tests for {@link WsSerializerAdapter} class.
 * 
 * @author Georgi Vachkov
 */
public class AbstractSerializerAdapterTest extends MockObjectTestCase 
{
	private TestWsSerializerAdapter adapter;
	private JaxWsWorkspaceResource resource; 
	
	@Override
	public void setUp()
	{
		Mock<IJavaModel> javaModelMock = mock(IJavaModel.class);
		resource = new JaxWsWorkspaceResource(javaModelMock.proxy());
		adapter = new TestWsSerializerAdapter(null, false);
	}
	
	public void testWsSerializerAdapter() 
	{
		try {
			new WsSerializerAdapter(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _) {
		}
	}
	
	public void testNotifyChangedNotificationEditEnabledOnSet() 
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.expects(once()).method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.SET));
		
		adapter.notifyChanged(notificationMock.proxy());
		assertTrue(adapter.saveCalled);		
	}	
	
	public void testNotifyChangedNotificationEditEnabledOnUnset() 
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.expects(once()).method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.UNSET));
		
		adapter.notifyChanged(notificationMock.proxy());
		assertTrue(adapter.saveCalled);		
	}	
	
	public void testNotifyChangedNotificationEditEnabledOnAdd() 
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.expects(once()).method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.ADD));
		
		adapter.notifyChanged(notificationMock.proxy());
		assertFalse(adapter.saveCalled);		
	}		
	
	public void testNotifyChangedNotificationObjectTouched() 
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.expects(once()).method("isTouch").will(returnValue(true));
		adapter.notifyChanged(notificationMock.proxy());
		assertFalse(adapter.saveCalled);		
	}		

	public void testNotifyChangedNotificationEditDisabled() 
	{
		resource.disableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		adapter.notifyChanged(notificationMock.proxy());
		assertFalse(adapter.saveCalled);		
	}
	
	public void testCheckStringValue()
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		TestWsSerializerAdapter adapter = new TestWsSerializerAdapter(ws, true);
		
		Mock<Notification> msg = mock(Notification.class);
		msg.stubs().method("getFeature").will(returnValue(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		msg.stubs().method("getOldStringValue").will(returnValue("oldValue"));
		msg.stubs().method("getNewStringValue").will(returnValue("newValue"));		
		
		assertTrue(adapter.checkValue(msg.proxy()));
		assertEquals("newValue", ws.getName());
	}
	
	public void testCheckStringValueInvalidValues()
	{
		IWebService ws = DomFactory.eINSTANCE.createIWebService();
		TestWsSerializerAdapter adapter = new TestWsSerializerAdapter(ws, true);
		
		Mock<Notification> msg = mock(Notification.class);
		msg.stubs().method("getFeature").will(returnValue(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		msg.stubs().method("getOldValue").will(returnValue("oldValue"));
		
		msg.stubs().method("getNewStringValue").will(returnValue(null));
		assertFalse(adapter.checkValue(msg.proxy()));
		assertEquals("oldValue", ws.getName());
		msg.stubs().method("getNewStringValue").will(returnValue(""));
		assertFalse(adapter.checkValue(msg.proxy()));
		assertEquals("oldValue", ws.getName());
		msg.stubs().method("getNewStringValue").will(returnValue(" "));
		assertFalse(adapter.checkValue(msg.proxy()));
		assertEquals("oldValue", ws.getName());		
		msg.stubs().method("getNewStringValue").will(returnValue(" oldValue "));
		assertTrue(adapter.checkValue(msg.proxy()));
		assertEquals("oldValue", ws.getName());				
	}

	public void testIsAdapterForTypeObject() 
	{
		assertTrue(adapter.isAdapterForType(IAnnotationSerializer.class));
		assertFalse(adapter.isAdapterForType(Notification.class));
	}
	
	public void testUpdateCalledForRequiredAnnotation()
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.stubs().method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.UNSET));
		notificationMock.stubs().method("getNewStringValue").will(returnValue("test"));
		notificationMock.stubs().method("getOldValue").will(returnValue("oldValue"));	
		notificationMock.stubs().method("getFeature").will(returnValue(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getParamValuePairs").will(returnValue(new HashSet<IParamValuePair>()));
		
		TestSavingWsSerializerAdapter saveAdapter = new TestSavingWsSerializerAdapter(resource, true, ann.proxy());
		saveAdapter.setTarget(DomFactory.eINSTANCE.createIWebService());
		saveAdapter.notifyChanged(notificationMock.proxy());
		assertTrue(saveAdapter.annWriter.updateCalled);
		assertFalse(saveAdapter.annWriter.removeCalled);		
	}
	
	public void testUpdateCalledForNonEmptyAnnotation()
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.stubs().method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.UNSET));
		notificationMock.stubs().method("getNewStringValue").will(returnValue("test"));
		notificationMock.stubs().method("getOldValue").will(returnValue("oldValue"));	
		notificationMock.stubs().method("getFeature").will(returnValue(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		Set<IParamValuePair> params = new HashSet<IParamValuePair>();
		Mock<IParamValuePair> param = mock(IParamValuePair.class);
		params.add(param.proxy());
		ann.stubs().method("getParamValuePairs").will(returnValue(params));
		
		TestSavingWsSerializerAdapter saveAdapter = new TestSavingWsSerializerAdapter(resource, false, ann.proxy());
		saveAdapter.setTarget(DomFactory.eINSTANCE.createIWebService());
		saveAdapter.notifyChanged(notificationMock.proxy());
		assertTrue(saveAdapter.annWriter.updateCalled);
		assertFalse(saveAdapter.annWriter.removeCalled);		
	}	
	
	public void testRemoveCalledForNotRequiredAnnotation()
	{
		resource.enableSaving();
		Mock<Notification> notificationMock = mock(Notification.class);
		notificationMock.stubs().method("isTouch").will(returnValue(false));
		notificationMock.stubs().method("getEventType").will(returnValue(Notification.UNSET));
		notificationMock.stubs().method("getNewStringValue").will(returnValue("test"));
		notificationMock.stubs().method("getOldValue").will(returnValue("oldValue"));	
		notificationMock.stubs().method("getFeature").will(returnValue(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		Mock<IAnnotation<IType>> ann = mock(IAnnotation.class);
		ann.stubs().method("getParamValuePairs").will(returnValue(new HashSet<IParamValuePair>()));
		
		TestSavingWsSerializerAdapter saveAdapter = new TestSavingWsSerializerAdapter(resource, false, ann.proxy());
		saveAdapter.setTarget(DomFactory.eINSTANCE.createIWebService());
		saveAdapter.notifyChanged(notificationMock.proxy());
		assertFalse(saveAdapter.annWriter.updateCalled);
		assertTrue(saveAdapter.annWriter.removeCalled);		
	}
		
	
	protected class TestWsSerializerAdapter extends AbstractSerializerAdapter
	{
		public boolean saveCalled;
		
		private boolean doCheck;
		
		private IWebService ws;
			
		public TestWsSerializerAdapter(final IWebService ws, boolean doCheck) {
			super(resource);
			this.ws = ws;
			this.doCheck = doCheck;
		}		

		@Override
		public void save(Notification msg) {
			saveCalled = true;
		}

		@Override
		protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException {
			return null;
		}

		@Override
		public boolean checkValue(Notification msg) 
		{
			if(!doCheck) {
				return true;
			}
			
			return super.checkValue(msg);
		}
		
		@Override
		public EObject getTarget() {
			return ws;
		}

		@Override
		protected boolean isAnnotationRequired() {
			return true;
		}
	}
	
	protected class TestSavingWsSerializerAdapter extends AbstractSerializerAdapter
	{
		boolean annRequired;
		MyAnnotationWriter annWriter = new MyAnnotationWriter();
		IAnnotation<? extends IJavaElement> ann;
		
		public TestSavingWsSerializerAdapter(JaxWsWorkspaceResource resource, boolean annRequired, IAnnotation<? extends IJavaElement> ann) {
			super(resource);
			this.annRequired = annRequired;
			this.ann = ann;
		}

		@Override
		protected IAnnotation<? extends IJavaElement> getAnnotation() throws JavaModelException {
			return ann;
		}

		@Override
		protected boolean isAnnotationRequired() {
			return annRequired;
		}
		
		@Override
		protected AnnotationWriter getAnnotationWriter() {
			return annWriter;
		}
	}
	
	protected class MyAnnotationWriter extends AnnotationWriter
	{
		boolean removeCalled;
		boolean updateCalled;
		
		public void clear()
		{
			removeCalled = false;
			updateCalled = false;
		}
		
		@Override
		public <T extends IJavaElement> void update(final IAnnotation<T> annotation) {
			updateCalled = true;
		}
		
		@Override
		public <T extends IJavaElement> void remove(final IAnnotation<T> annotation) {
			removeCalled = true;
		}
	}
}
