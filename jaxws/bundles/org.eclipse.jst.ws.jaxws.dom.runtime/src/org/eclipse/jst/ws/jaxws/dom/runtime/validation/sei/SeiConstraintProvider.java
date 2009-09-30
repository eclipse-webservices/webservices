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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.sei;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.validation.model.IModelConstraint;

/**
 * Constraint provider for SEI DOM objects
 * 
 * @author Georgi Vachkov
 */
public class SeiConstraintProvider 
{
	private Set<IModelConstraint> constraints;
	
	/**
	 * @return constraints related to SEI
	 */
	public Collection<IModelConstraint> getConstraints() 
	{
		if (constraints==null) 
		{
			constraints = new HashSet<IModelConstraint>();
			constraints.add(new SeiNameInNCNameConstraint());
			constraints.add(new TargetNsValidUriConstraint());
			constraints.add(new RedundantAttributesConstraint());
			constraints.add(new SeiClassCorrectConstraint());
		}

		return constraints;
	}
}
