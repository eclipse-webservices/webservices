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
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ITraversalStrategy;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.Jee5WsDomRuntimeExtension;

/**
 * JAX-WS runtime DOM validator. Registered to the validation framework 
 * to contribute the validation process.
 * 
 * @author Georgi Vachkov
 */
public class JaxWsDomValidator implements IDomValidator 
{
	public IStatus validate(EObject object) 
	{
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setIncludeLiveConstraints(true);
		validator.setReportSuccesses(true);
		validator.getConstraintFilters();
		validator.setTraversalStrategy(new ITraversalStrategy.Recursive());

		validator.validate(object, null);

		return Status.OK_STATUS;
	}

	public String getSupportedDomRuntime() {	
		return Jee5WsDomRuntimeExtension.ID;
	}
}
