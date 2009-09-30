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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice;

import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimePlugin;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.DomValidationConstraintDescriptor;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;

/**
 * The constraint descriptor for WS constraints 
 * 
 * @author Georgi Vachkov
 */
class WsConstraintDescriptor extends DomValidationConstraintDescriptor
{	
	public String getId() {
		return DomValidationConstants.WS_CONSTRAINT_ID;
	}
	
	public String getPluginId() {
		return JaxWsDomRuntimePlugin.PLUGIN_ID;
	}

	public ConstraintSeverity getSeverity() {
		return ConstraintSeverity.ERROR;
	}

	public int getStatusCode() {
		return -26;
	}
}