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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom;

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.IWsDOMRuntimeInfo;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.WsDOMRuntimeRegistry;

public class WsDOMRuntimeManagerTest extends TestCase
{
	private WsDOMRuntimeManager target;
	
	@Override
	public void setUp() 
	{
		target = (WsDOMRuntimeManager)WsDOMRuntimeManager.instance();
	}
	
	public void testGetDOMRuntimeForRuntimeInfo()
	{
		Collection<IWsDOMRuntimeInfo> registeredRuntimes = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
		
		assertNotNull(target.getDOMRuntime(registeredRuntimes.iterator().next()));
		
		IWsDOMRuntimeExtension runtime = target.getDOMRuntime(registeredRuntimes.iterator().next());
		
		assertEquals(runtime, target.getDOMRuntime(registeredRuntimes.iterator().next()));
		
		registeredRuntimes = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
		
		assertEquals(runtime, target.getDOMRuntime(registeredRuntimes.iterator().next()));
	}

	public void testGetDOMRuntimeForRuntimeId()
	{
		assertNotNull(target.getDOMRuntime("supportedruntimetest"));
		assertNull(target.getDOMRuntime("test.id"));
		assertEquals(target.getDOMRuntime("supportedruntimetest"), target.getDOMRuntime("supportedruntimetest"));
	}
	
	public void testCreateDOMRuntimes()
	{
		target.createDOMRuntimes(null);
		
		assertNotNull(target.getDOMRuntime("supportedruntimetest"));
		assertNotNull(target.getDOMRuntime("supportedruntimetest1"));
	}
	
	public void testReloadDOMRuntimes()
	{
		target.reloadDOMRuntimes(null);
		
		IWsDOMRuntimeExtension domRuntime = target.getDOMRuntime("supportedruntimetest");
		IWsDOMRuntimeExtension domRuntime1 = target.getDOMRuntime("supportedruntimetest1");
			
		target.reloadDOMRuntimes(null);
		
		assertNotSame(domRuntime,target.getDOMRuntime(WsDOMRuntimeRegistry.getRuntimeInfo("supportedruntimetest").getId()));
		assertNotSame(domRuntime1,target.getDOMRuntime(WsDOMRuntimeRegistry.getRuntimeInfo("supportedruntimetest1").getId()));
	}
	
	public void testGetDOMRuntimes()
	{
		IWsDOMRuntimeExtension domRuntime = target.getDOMRuntime("supportedruntimetest");		
		assertNotNull(domRuntime);
		assertTrue(domRuntime instanceof TestWsDOMRuntimeExtension);
		
		IWsDOMRuntimeExtension domRuntime1 = target.getDOMRuntime("supportedruntimetest1");		
		assertNotNull(domRuntime1);
		assertTrue(domRuntime1 instanceof TestWsDOMRuntimeExtension);		
		
		IWsDOMRuntimeExtension domRuntime2 = target.getDOMRuntime("supportedruntimetest2");
		assertNull(domRuntime2);
	}
	
	public void testCreateDOMPassesProgressMonitor()
	{
		MyWsDomRuntimeManager manager = new MyWsDomRuntimeManager();
		IWsDOMRuntimeInfo info = WsDOMRuntimeRegistry.getRuntimeInfo("supportedruntimetest1");
		NullProgressMonitor npm = new NullProgressMonitor();
		IWsDOMRuntimeExtension rtEx = manager.createDOMRuntime(info, npm);
		assertTrue(rtEx instanceof TestWsDOMRuntimeExtension);
		assertEquals(npm, ((TestWsDOMRuntimeExtension)rtEx).monitor);		
	}
	
	private class MyWsDomRuntimeManager extends WsDOMRuntimeManager
	{
		protected IWsDOMRuntimeExtension createDOMRuntime(final IWsDOMRuntimeInfo runtimeInfo, final IProgressMonitor monitor) {
			return super.createDOMRuntime(runtimeInfo, monitor);
		}
	}
}
