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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.ConstraintStatus;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IConstraintStatusExtended;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IProblemLocation;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;

/**
 * Extended constraint status which carries information about the problem location 
 * @author Danail Branekov
 */
public class ConstraintStatusExtended extends ConstraintStatus implements IConstraintStatusExtended
{
	private final Set<IProblemLocation> problemLocations;

	/**
	 * Constructs a <code>ConstraintStatusExtended</code> instance with empty result locus
	 * @param constraint
	 * @param target
	 * @param severity
	 * @param code
	 * @param message
	 * @param problemLocations a set of problem locations or empty set if none; cannot be null.<br> 
	 * 
	 * @see ConstraintStatus#ConstraintStatus(IModelConstraint, EObject, int, int, String, Set)
	 */
	public ConstraintStatusExtended(final IModelConstraint constraint, final EObject target, final int severity, final int code, final String message, final Set<IProblemLocation> problemLocations)
	{
		super(constraint, target, severity, code, message, new HashSet<EObject>());
		ContractChecker.nullCheckParam(problemLocations, "problemLocation");//$NON-NLS-1$
		this.problemLocations = problemLocations;
	}
	
	/**
	 * Constructs a <code>ConstraintStatusExtended</code> instance with empty result locus
	 * @param constraint
	 * @param target
	 * @param message
	 * @param problemLocations a set of problem locations or empty set if none; cannot be null.<br> 
	 * 
	 * @see ConstraintStatus#ConstraintStatus(IModelConstraint, EObject, String, Set)
	 */
	public ConstraintStatusExtended(final IModelConstraint constraint, final EObject target, final String message, final Set<IProblemLocation> problemLocations)
	{
		super(constraint, target, message, new HashSet<EObject>());
		this.problemLocations = problemLocations;
	}
	
	public Set<IProblemLocation> getProblemLocations()
	{
		return Collections.unmodifiableSet(this.problemLocations);
	}
}
