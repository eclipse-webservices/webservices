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
package org.eclipse.jst.ws.jaxws.utils.annotations;

import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;

/**
 * Interface representing Complex Annotation consisting of param-value pairs.
 * 
 * @author Plamen Pavlov
 */
public interface IAnnotation<T extends IJavaElement>
{
	/**
	 * Return the param-value pairs which this Annotation has.
	 * 
	 * @return Set of param-values pairs.
	 */
	public Set<IParamValuePair> getParamValuePairs();
	
	/**
	 * Returns the value of the parameter with name param. 
	 * @param param - the name of the parameter who's value is requested
	 * @return - the value represented as a string. Null if such parameter cannot be found or has a value of null.
	 * @throws - NullPointerException if param is null
	 */
	public String getPropertyValue(String param);
		
	/**
	 * Returns the T, to which this annotattion is associated.
	 */
    public T getAppliedElement();
        
    
//From IAnnotattionBase    
	/**
	 * @return - annotation name
	 */
	public String getAnnotationName();
	
	/**
	 * Gets the information about location in the source code.
	 * 
	 * @return ILocator
	 */
	public ILocator getLocator();
	

}
