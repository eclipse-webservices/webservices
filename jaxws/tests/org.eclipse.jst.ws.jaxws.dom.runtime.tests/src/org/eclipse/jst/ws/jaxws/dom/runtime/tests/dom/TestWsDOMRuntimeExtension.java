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

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDomLoadListener;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.jmock.Mock;

public class TestWsDOMRuntimeExtension extends MockObjectTestCase implements IWsDOMRuntimeExtension 
{
	public IProgressMonitor monitor;
	
	public void createDOM(IProgressMonitor monitor) 
	{
		this.monitor = monitor;
	}
	
	public IDOM getDOM()
	{
		Mock iter = new Mock(Iterator.class);
		iter.expects(atLeastOnce()).method("hasNext").will(returnValue(false));
		((Iterator<?>)iter.proxy()).hasNext();
		Mock eList = new Mock(EList.class);
		eList.expects(atLeastOnce()).method("iterator").will(returnValue(iter.proxy()));
		((EList<?>)eList.proxy()).iterator();
		Mock dom = new Mock(IDOM.class);
		dom.expects(atLeastOnce()).method("getWebServiceProjects").will(returnValue(eList.proxy()));
		((IDOM)dom.proxy()).getWebServiceProjects();
		return (IDOM)dom.proxy();
	}

	public void addLoadListener(IWsDomLoadListener listener) {
		// TODO Auto-generated method stub
	}

	public void removeLoadListener(IWsDomLoadListener listener) {
		// TODO Auto-generated method stub
	}

	public boolean isLoadCanceled() {
		// TODO Auto-generated method stub
		return false;
	}
}
