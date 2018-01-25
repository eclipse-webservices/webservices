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
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.internal.util.NamedExtensionInfo;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.IWsDOMRuntimeInfo;
import org.eclipse.jst.ws.jaxws.dom.runtime.registry.WsDOMRuntimeRegistry;

public class WsDOMRuntimeRegistryTest extends TestCase 
{
	private static final class RuntimeInfo extends NamedExtensionInfo implements IWsDOMRuntimeInfo
	{
		private HashMap<String, String> projectFacets;
		
		RuntimeInfo(final String pId, final String pLabel, final HashMap<String, String> projectFacets)
		{
			super(pId, pLabel);
			
			this.projectFacets = projectFacets;
		}
		
		public HashMap<String,String> getProjectFacets()
		{
			return this.projectFacets;
		}
 	}
	
	
	public void testInstantiateRuntime() 
	{
		Collection<IWsDOMRuntimeInfo> runtimeInfos = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
		Iterator<IWsDOMRuntimeInfo> runtimeInfoIter = runtimeInfos.iterator();
		
		while(runtimeInfoIter.hasNext())
		{
			IWsDOMRuntimeInfo runtimeInfo = runtimeInfoIter.next();
			
			if(runtimeInfo.getId().equals("supportedruntimetest")
					|| runtimeInfo.getId().equals("supportedruntimetest1"))
			{
				assertNotNull(WsDOMRuntimeRegistry.instantiateRuntime(runtimeInfo));
				assertTrue(WsDOMRuntimeRegistry.instantiateRuntime(runtimeInfo) instanceof TestWsDOMRuntimeExtension);
			}
			else if(runtimeInfo.getId().equals("supportedruntimetest2"))
			{
				assertNull(WsDOMRuntimeRegistry.instantiateRuntime(runtimeInfo));
			}
		}
		
		assertNull(WsDOMRuntimeRegistry.instantiateRuntime(null));
		
		RuntimeInfo info = new RuntimeInfo("Tst", "ddgg", new HashMap<String,String>());

		assertNull(WsDOMRuntimeRegistry.instantiateRuntime(info));
	}

	public void testFindExtension() 
	{
		assertNotNull(WsDOMRuntimeRegistry.findExtension(WsDOMRuntimeRegistry.getRegisteredRuntimesInfo().iterator().next()));
		assertNull(WsDOMRuntimeRegistry.findExtension(null));
		
		RuntimeInfo runtimeInfo = new RuntimeInfo("Tst", "ddgg", new HashMap<String,String>());
		assertNull(WsDOMRuntimeRegistry.findExtension(runtimeInfo));
	}

	public void testGetRegisteredRuntimeInfo() 
	{
		Collection<IWsDOMRuntimeInfo> runtimeInfos = WsDOMRuntimeRegistry.getRegisteredRuntimesInfo();
		
		assertNotNull(runtimeInfos);
		assertTrue(runtimeInfos.size() >= 3);
		
		Iterator<IWsDOMRuntimeInfo> runtimeInfoIter = runtimeInfos.iterator();
		
		while(runtimeInfoIter.hasNext())
		{
			IWsDOMRuntimeInfo runtimeInfo = runtimeInfoIter.next();
			
			assertNotNull(runtimeInfo.getId());
			
			if(runtimeInfo.getId().equals("supportedruntimetest"))
			{
				assertNotNull(runtimeInfo.getName());
				assertEquals(runtimeInfo.getName(), "Testing");
				
				assertNotNull(runtimeInfo.getProjectFacets());
				assertEquals(runtimeInfo.getProjectFacets().size(), 2);
				
				assertTrue(runtimeInfo.getProjectFacets().containsKey("jst.webb"));
				assertTrue(runtimeInfo.getProjectFacets().containsKey("test.facet"));
				
				assertFalse(runtimeInfo.getProjectFacets().containsKey("jst.web"));
				assertFalse(runtimeInfo.getProjectFacets().containsKey("test.facett"));
				
				assertNotNull(runtimeInfo.getProjectFacets().get("jst.webb"));
				assertEquals(runtimeInfo.getProjectFacets().get("jst.webb"), "1.0.0.0");
				assertNull(runtimeInfo.getProjectFacets().get("test.facet"));
				
				assertNull(runtimeInfo.getProjectFacets().get("dummy.facet"));
			}
			else if(runtimeInfo.getId().equals("supportedruntimetest1"))
			{
				assertNotNull(runtimeInfo.getName());
				assertEquals(runtimeInfo.getName(), "Testing1");
				
				assertNotNull(runtimeInfo.getProjectFacets());
				assertEquals(runtimeInfo.getProjectFacets().size(), 2);
				
				assertTrue(runtimeInfo.getProjectFacets().containsKey("jst.webb"));
				assertTrue(runtimeInfo.getProjectFacets().containsKey("test.facett"));
				
				assertFalse(runtimeInfo.getProjectFacets().containsKey("jst.web"));
				assertFalse(runtimeInfo.getProjectFacets().containsKey("test.facet"));
				
				assertNotNull(runtimeInfo.getProjectFacets().get("jst.webb"));
				assertEquals(runtimeInfo.getProjectFacets().get("jst.webb"), "1.0.0.0");
				assertNull(runtimeInfo.getProjectFacets().get("test.facett"));
				
				assertNull(runtimeInfo.getProjectFacets().get("dummy.facet"));
			}
		}
	}
	
	
	public void testGetRuntimeInfo()
	{
		assertNull(WsDOMRuntimeRegistry.getRuntimeInfo("test.id"));
		assertNotNull(WsDOMRuntimeRegistry.getRuntimeInfo("supportedruntimetest"));
		assertNotNull(WsDOMRuntimeRegistry.getRuntimeInfo("supportedruntimetest1"));
		assertNull(WsDOMRuntimeRegistry.getRuntimeInfo(null));
	}
}
