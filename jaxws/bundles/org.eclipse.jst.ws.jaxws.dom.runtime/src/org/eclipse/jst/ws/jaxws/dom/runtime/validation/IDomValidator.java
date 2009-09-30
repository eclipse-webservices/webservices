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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

/**
 * Implement this interface if you intend to implement specific DOM runtime validation.
 * You need to register the validator using org.eclipse.jst.ws.jaxws.dom.runtime.domValidator
 * extension point and specify for which DOM runtime version it will be relevant.
 * 
 * @author Georgi Vachkov
 */
public interface IDomValidator 
{
	/**
	 * Called when validation is triggered - for details see WST validation framework and
	 * DomValidationManager.
	 * @param object object to be validated
	 * @return the validation results
	 */
	public IStatus validate(EObject object);
	
	/**
	 * Here you must return the DOM runtime ID which this validator supports
	 * @return DOM runtime ID
	 */
	public String getSupportedDomRuntime();
}
