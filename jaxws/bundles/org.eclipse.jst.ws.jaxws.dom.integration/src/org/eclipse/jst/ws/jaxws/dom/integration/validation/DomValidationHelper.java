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
package org.eclipse.jst.ws.jaxws.dom.integration.validation;

import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMRuntimeManager;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.wst.validation.internal.operations.WorkbenchContext;
import org.eclipse.wst.validation.internal.provisional.core.IValidationContext;

/**
 * {@link IValidationContext} implementor. Acts as helper class for validation. Used
 * by {@link DomValidationManager} to retrieve DOM model.
 *  
 * @author Georgi Vachkov
 */
@SuppressWarnings("restriction")
public class DomValidationHelper extends WorkbenchContext
{	
	/**
	 * Constructor - registers models that this helper will support. Registration is done 
	 * by setting model 'symbolicName' and method to be called for this method to be retrieved
	 */
	public DomValidationHelper()
	{
		registerModel("webServiceProject", "getWebServiceProject", new Class[] {String.class}); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Executed by {@link WorkbenchContext} when {@link WorkbenchContext#loadModel(String, Object[])} is called
	 * and finds the corresponding to {@link #getProject()} {@link IWebServiceProject}
	 * 
	 * @param domId
	 * @return found {@link IWebServiceProject} or <code>null</code>
	 */
	public IWebServiceProject getWebServiceProject(final String domId) 
	{
		ContractChecker.nullCheckParam(domId, "domId"); //$NON-NLS-1$
		
		final IWsDOMRuntimeExtension domRuntime = getDomRuntime(domId);
		if (domRuntime == null) {
			return null;
		}
		
		try {
			final IDOM dom = domRuntime.getDOM();
			if (dom==null) {
				return null;
			}
			
			return DomUtil.INSTANCE.findProjectByName(dom, getProject().getName());
		} 
		catch (WsDOMLoadCanceledException e) { // $JL-EXC$
			return null;
		}
	}

	protected IWsDOMRuntimeExtension getDomRuntime(final String domId) {
		return WsDOMRuntimeManager.instance().getDOMRuntime(domId);
	}
}
