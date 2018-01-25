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
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * @model
 * @author Hristo Sabev
 */
public interface IWebServiceProject extends EObject
{
	/**
	 * @model type="IWebService" suppressedSetVisibility="true" many="true" containment="true"
	 * @return
	 */
	public EList<IWebService> getWebServices();
	
	
	/**
	 * @model type="IServiceEndpointInterface" suppressedSetVisibility="true" many="true" containment="true"
	 */
	public EList<IServiceEndpointInterface> getServiceEndpointInterfaces();
	
	/**
	 * @model suppressedSetVisibility="true" required="true"
	 */
	public String getName();
}
