/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * A basic default implementation of IJavaWebServiceRule.
 */
public class JavaWebServiceRule implements IJavaWebServiceRule
{
	protected IJavaWebServiceRuleEngine engine_;
	protected List statusList_;
	protected int id_;
	protected String namespace_;
	protected String name_;
	protected String description_;
	
	protected JavaWebServiceRule ()
	{
		// Not publicly constructable.
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#getId()
	 */
	public int getId ()
	{
		return id_;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#getNamespace()
	 */
	public String getNamespace ()
	{
		return namespace_;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#getName()
	 */
	public String getName ()
	{
		return name_;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#getDescription()
	 */
	public String getDescription ()
	{
		return description_;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#init()
	 */
	public void init ( IJavaWebServiceRuleEngine engine )
	{
		engine_ = engine;
		statusList_ = new LinkedList();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#visitClass(org.eclipse.jdt.core.IType, java.util.Stack)
	 */
	public void visitClass ( IType jdtClass, Stack peanutTrail )
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#visitException(org.eclipse.jdt.core.IField, java.util.Stack)
	 */
	public void visitException ( IField jdtClass, Stack peanutTrail )
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#visitField(org.eclipse.jdt.core.IField, java.util.Stack)
	 */
	public void visitField ( IField jdtField, Stack peanutTrail )
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#visitProperty(org.eclipse.jdt.core.IMethod, org.eclipse.jdt.core.IMethod, java.util.Stack)
	 */
	public void visitProperty ( IJavaBeanProperty beanProperty, Stack peanutTrail )
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#visitMethod(org.eclipse.jdt.core.IMethod, java.util.Stack)
	 */
	public void visitMethod ( IMethod jdtMethod, Stack peanutTrail )
	{
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaWebServiceRule#getResults()
	 */
	public IStatus getResults ()
	{
		IStatus status = null;
		if (statusList_.size() == 0)
		{
			status = new Status(IStatus.OK,"org.eclipse.jst.ws",0,"",null);
		}
		else
		{
			MultiStatus multiStatus = new MultiStatus("org.eclipse.jst.ws",0,"",null);
			Iterator i = statusList_.iterator();
			while (i.hasNext())
			{
				multiStatus.add((IStatus)i.next());
			}
			status = multiStatus;
		}
		return status;
	}
}
