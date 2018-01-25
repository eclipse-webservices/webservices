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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDomLoadListener;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.Jee5WsDomRuntimeExtension;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

/**
 * Tests for Jee5WsDomRuntimeExtension
 * 
 * @author Georgi Vachkov
 */
public class Jee5WsDomRuntimeExtensionTest extends MockObjectTestCase 
{
	private Jee5WsDomRuntimeExtension extension;
	
	public void setUp() throws Exception
	{
		extension = new Jee5WsDomRuntimeExtension();
	}
	
	public void testGetDOMCreateNotCalled() throws WsDOMLoadCanceledException 
	{
		assertNull(extension.getDOM());
	}

	public void testLoadListenerStartFinishCalled() throws Exception 
	{
		final Mock<IWsDomLoadListener> loadListenerMock = mock(IWsDomLoadListener.class);
		loadListenerMock.expects(once()).method("started");
		loadListenerMock.expects(once()).method("finished");
		
		extension.addLoadListener(loadListenerMock.proxy());
		extension.createDOM(null);
	}

	public void testRemoveLoadListener() throws Exception 
	{
		final IWsDomLoadListener loadListener = (IWsDomLoadListener) mock(IWsDomLoadListener.class).proxy();
		extension.addLoadListener(loadListener);
		extension.removeLoadListener(loadListener);
		extension.createDOM(null);
	}
	
	public void testLoadCanceled() throws Exception
	{
		final IProgressMonitor monitor = new NullProgressMonitor();
		monitor.setCanceled(true);
		
		final Mock<IWsDomLoadListener> loadListenerMock = mock(IWsDomLoadListener.class);
		loadListenerMock.expects(once()).method("started");
		loadListenerMock.expects(once()).method("finished");
		extension = new Jee5WsDomRuntimeExtension() {
			@Override
			protected JaxWsWorkspaceResource createResource() { 
				return new JaxWsWorkspaceResource(javaModel()) {
					@Override
					public boolean isLoadCnaceled() {
						return true;
					}
					@Override
					public void startSynchronizing() {
						fail("startSynchronizing should not be called in this case");
					}
				};
			}
		};
		extension.addLoadListener(loadListenerMock.proxy());
		try {
			extension.createDOM(monitor);		
			fail("WsDOMLoadCanceledException not thrown az expected");
		} catch (WsDOMLoadCanceledException _) {
			// nothing to do here
		}
	}
	
	public void testStartSynchronizingCalledIfLoadNotCanceled() throws Exception {
		final boolean[] startSyncCalled = { false };
		extension = new Jee5WsDomRuntimeExtension() {
			@Override
			protected JaxWsWorkspaceResource createResource() { 
				return new JaxWsWorkspaceResource(javaModel()) {
					@Override
					public void startSynchronizing() {
						startSyncCalled[0] = true;
					}
				};
			}
		};
		extension.createDOM(new NullProgressMonitor());
		assertTrue("Start syncronization was not started even the load wasa not canceled", startSyncCalled[0]);
	}	
	
	public void testCreateDOMBlocksWhenCalled() throws Exception 
	{
		final boolean[] doLoadStarted = { false };
		final boolean[] doLoadFinished = { false };
		extension = new Jee5WsDomRuntimeExtension() {
			@Override
			protected JaxWsWorkspaceResource createResource() { 
				return new JaxWsWorkspaceResource(javaModel()) {
					@Override
					protected void doLoad(final Map<?, ?> options) throws IOException {
						doLoadStarted[0] = true;
						try {
							Thread.sleep(400);
						} catch (InterruptedException e) {
							throw new IOException(e.getLocalizedMessage());
						}
						super.doLoad(options);
						doLoadFinished[0] = true;
					}
				};
			}
		};
		new Thread(){
			@Override
		    public void run() {
				try {
					extension.createDOM(new NullProgressMonitor());
				} catch (IOException e) {
					fail(e.getMessage());
				} catch (WsDOMLoadCanceledException e) {
					fail(e.getMessage());
				}
		    }
		}.start();
		Assertions.waitAssert(new IWaitCondition() {public boolean checkCondition() {
			return doLoadStarted[0];
		}}, "Loading from another thread not started");
		
		extension.createDOM(new NullProgressMonitor());
		assertTrue("Start syncronization was not started even the load was not canceled", doLoadFinished[0]);
	}
}
