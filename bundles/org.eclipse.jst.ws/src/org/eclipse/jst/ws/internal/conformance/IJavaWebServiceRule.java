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

import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author cbrealey
 * A Java Web service conformance Rule tests some collection
 * of Java resources that implement, or are meant to implement,
 * a Web service against a single, concise requirement as is
 * typically defined by a specification (eg. JAX-RPC, JSR 109)
 * or a runtime (eg. Apache Axis).
 * <p>
 * Every rule must have a namespace and a unique ID under that
 * that namespace. Every rule may have a short name and a long
 * description, both of which are recommended but optional.
 * <p>
 * Every rule follows a lifecycle of initialization, analysis
 * and completion. A Java Web service rule engine will drive
 * the <code>init()</code> method to initialize the rule, then
 * drive the various <code>visit...()</code> methods as it
 * navigates some collection of JDT resources, then drive the
 * <code>getResults()</code> method to indicate analysis is done
 * and retrieve a status object with the results of the analysis.
 */
public interface IJavaWebServiceRule
{
	/**
	 * Returns the unique identifier of the rule
	 * within the namespace to which the rule belongs.
	 * @return The unique identifier of the rule.
	 * Valid rule identifiers must not be negative.
	 */
	public int getId ();
	
	/**
	 * Returns the namespace to which this rule belongs.
	 * @return The namespace to which this rule belongs.
	 * Never returns null.
	 */
	public String getNamespace ();

	/**
	 * Returns the translatable short name of this rule.  
	 * @return The translatable short name of this rule,
	 * or null if the rule has no name.
	 */
	public String getName ();

	/**
	 * Returns the translatable description of this rule.
	 * @return The translatable description of this rule,
	 * or null if the rule has no description.
	 */
	public String getDescription ();

	/**
	 * Called by the rule engine to give this rule a chance
	 * to initialize itself prior to visiting JDT objects.
	 * @param engine The engine initializing this rule.
	 */
	public void init ( IJavaWebServiceRuleEngine engine );

	public void visitClass ( IType jdtClass, Stack peanutTrail );

	public void visitException ( IType jdtClass, Stack peanutTrail );

	public void visitField ( IField jdtField, Stack peanutTrail );

	public void visitProperty ( IJavaBeanProperty beanProperty, Stack peanutTrail );

	public void visitMethod ( IMethod jdtMethod, Stack peanutTrail );

	/**
	 * Called by the rule engine after all JDT objects have
	 * been visited so that the rule can return a status
	 * object (usually a multi-status) with a summary of
	 * any violations by JDT objects detected by this rule.
	 * @return The results of the rule's analysis.
	 */
	public IStatus getResults ();
}
