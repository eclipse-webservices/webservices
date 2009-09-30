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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.CategoryManager;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.AbstractConstraintDescriptor;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;

public abstract class DomValidationConstraintDescriptor extends AbstractConstraintDescriptor
{
	public DomValidationConstraintDescriptor() {
		addCategory(CategoryManager.getInstance().getCategory("org.eclipse.jst.ws.jaxws.dom.jee5"));//$NON-NLS-1$
	}
	
	public String getBody() {
		return null;
	}

	public String getDescription() {
		return JaxWsDomRuntimeMessages.DomValidationConstraintDescriptor_WS_DOM_VALIDATION_CONSTRAINT;
	}

	public EvaluationMode<EObject> getEvaluationMode() {
		return EvaluationMode.BATCH;
	}

	public String getMessagePattern() {
		return null;
	}

	public String getName() {
		return JaxWsDomRuntimeMessages.DomValidationConstraintDescriptor_WS_DOM_VALIDATION_CONSTRAINT;
	}

	public boolean targetsEvent(Notification notification) {
		return true;
	}

	public boolean targetsTypeOf(EObject object) {
		return true;
	}

}
