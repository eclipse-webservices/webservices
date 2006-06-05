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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author cbrealey
 * This object manages a set of rules.
 * Rules in the set may be enabled or disabled.
 */
public class JavaWebServiceRuleSet
{
	private Set enabledRules_ = new HashSet();
	private Set disabledRules_ = new HashSet();
	
	/**
	 * Creates a new, initially empty set of rules.
	 */
	public JavaWebServiceRuleSet ()
	{
		// Nothing to do.
	}

	/**
	 * Creates a new set of rules initialized with those
	 * from the given array of <code>rules</code>.
	 * @param rules An array of rules to add.
	 */
	public JavaWebServiceRuleSet ( IJavaWebServiceRule[] rules )
	{
		addRules(rules);
	}
	
	/**
	 * Adds a <code>rule</code> to the set.
	 * Rules added to the set are initially enabled.
	 * @param rule The rule to add.
	 */
	public void addRule ( IJavaWebServiceRule rule )
	{
		enabledRules_.add(rule);
	}
	
	/**
	 * Adds several <code>rules</code> to the set.
	 * Rules added to the set are initially enabled.
	 * @param rules The array of rules to add.
	 */
	public void addRules ( IJavaWebServiceRule[] rules )
	{
		enabledRules_.addAll(Arrays.asList(rules));
	}
	
	/**
	 * Marks the given <code>rule</code> as disabled or enabled.
	 * This method has no effect if the rule is not already in
	 * the set, in other words, this method will not have the
	 * side effect of adding a rule to the set.
	 * @param rule The rule to disable or enable.
	 * @param disabled True to disable the rule or false to enable it.
	 * @return True if a change in status was actually made.
	 */
	public boolean setRuleDisabled ( IJavaWebServiceRule rule, boolean disabled )
	{
		if (disabled)
		{
			if (enabledRules_.remove(rule))
			{
				disabledRules_.add(rule);
				return true;
			}
		}
		else
		{
			if (disabledRules_.remove(rule))
			{
				enabledRules_.add(rule);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the number of rules in the set.
	 * @return The number of rules in the set.
	 */
	public int size ()
	{
		return enabledRules_.size() + disabledRules_.size();
	}
	
	/**
	 * Returns the number of enabled rules in the set.
	 * @return The number of enabled rules in the set.
	 */
	public int numberEnabled ()
	{
		return enabledRules_.size();
	}
	
	/**
	 * Returns the number of disabled rules in the set.
	 * @return The number of disabled rules in the set.
	 */
	public int numberDisabled ()
	{
		return disabledRules_.size();
	}
	
	/**
	 * Returns an array of all currently enabled rules from the set.
	 * @return An array of all currently enabled rules from the set.
	 */
	public IJavaWebServiceRule[] getEnabledRules ()
	{
		return (IJavaWebServiceRule[])enabledRules_.toArray(new IJavaWebServiceRule[0]);
	}
	
	/**
	 * Returns an array of all currently disabled rules from the set.
	 * @return An array of all currently disabled rules from the set.
	 */
	public IJavaWebServiceRule[] getDisabledRules ()
	{
		return (IJavaWebServiceRule[])disabledRules_.toArray(new IJavaWebServiceRule[0]);
	}
	
	/**
	 * Initializes all rules in the set by calling
	 * their init() methods with the given engine.
	 * @param engine The engine initializing 
	 */
	public void init ( IJavaWebServiceRuleEngine engine )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.init(engine);
		}
	}

	/**
	 * Visits the "visitClass" method of all rules in the set. 
	 * @param jdtClass The JDT IType being visited.
	 * @param peanutTrail The stack of objects up to the root.
	 */
	public void visitClass ( IType jdtClass, Stack peanutTrail )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.visitClass(jdtClass,peanutTrail);
		}		
	}

	/**
	 * Visits the "visitException" method of all rules in the set. 
	 * @param jdtClass The JDT IType being visited.
	 * @param peanutTrail The stack of objects up to the root.
	 */
	public void visitException ( IType jdtClass, Stack peanutTrail )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.visitException(jdtClass,peanutTrail);
		}
	}


	/**
	 * Visits the "visitField" method of all rules in the set. 
	 * @param jdtClass The JDT IField being visited.
	 * @param peanutTrail The stack of objects up to the root.
	 */
	public void visitField ( IField jdtField, Stack peanutTrail )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.visitField(jdtField,peanutTrail);
		}
	}

	/**
	 * Visits the "visitProperty" method of all rules in the set. 
	 * @param jdtClass The IJavaBeanProperty being visited.
	 * @param peanutTrail The stack of objects up to the root.
	 */
	public void visitProperty ( IJavaBeanProperty beanProperty, Stack peanutTrail )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.visitProperty(beanProperty,peanutTrail);
		}
	}

	/**
	 * Visits the "visitMethod" method of all rules in the set. 
	 * @param jdtClass The JDT IMethod being visited.
	 * @param peanutTrail The stack of objects up to the root.
	 */
	public void visitMethod ( IMethod jdtMethod, Stack peanutTrail )
	{
		Iterator i = enabledRules_.iterator();
		while (i.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)i.next();
			rule.visitMethod(jdtMethod,peanutTrail);
		}
	}
	
	/**
	 * Collects the results of all rules in the set together.
	 * If one or more warnings or errors are found, returns
	 * a status object whose top level message is the root
	 * message passed to this message, otherwise, returns a
	 * basic "OK" status.
	 * @param rootMessage The warning/error message to act as
	 * the root message if necessary (ie. if at least one rule
	 * in the set reported at least one warning/error).
	 * @return The collective status.
	 */
	public IStatus getResults ( String rootMessage )
	{
		List list = new LinkedList();
		Iterator e = enabledRules_.iterator();
		while (e.hasNext())
		{
			IJavaWebServiceRule rule = (IJavaWebServiceRule)e.next();
			IStatus status = rule.getResults();
			if (!status.isOK())
			{
				list.add(status);
			}
		}
		if (list.size() > 0)
		{
			MultiStatus multiStatus = new MultiStatus("org.eclipse.jst.ws",0,rootMessage,null);
			Iterator l = list.iterator();
			while (l.hasNext())
			{
				IStatus status = (IStatus)l.next();
				multiStatus.addAll(status);
			}
			return multiStatus;
		}
		else
		{
			return new Status(IStatus.OK,"org.eclipse.jst.ws",0,"",null);
		}
	}
}
