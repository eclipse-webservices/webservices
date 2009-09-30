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

import java.util.Set;

import org.eclipse.emf.validation.model.IConstraintStatus;

/**
 * Extended constraint status which carries information about the problem location 
 * @author Danail Branekov
 */
public interface IConstraintStatusExtended extends IConstraintStatus
{
	/**
	 * Gets the set of the problem locations. The result is an unmodifiable set 
	 * @return set of problem locations; empty set if none
	 */
	public Set<IProblemLocation> getProblemLocations();

}
