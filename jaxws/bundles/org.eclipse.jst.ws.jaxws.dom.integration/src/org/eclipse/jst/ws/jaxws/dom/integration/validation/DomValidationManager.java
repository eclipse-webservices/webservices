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

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.ws.jaxws.dom.integration.internal.plugin.DomIntegrationPlugin;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.CustomDomItemProviderAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryLabelProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.DomValidatorFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IDomValidator;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.validation.internal.provisional.core.IProjectValidationContext;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidationContext;
import org.eclipse.wst.validation.internal.provisional.core.IValidatorJob;

/**
 * Registered to WST validation framework IValidator. Receives notifications for 
 * validation to be performed and calls registered DOM validators to perform the
 * real validation.
 * 
 * @author Georgi Vachkov
 */
public class DomValidationManager implements IValidatorJob
{
	public void cleanup(final IReporter reporter) {	
		// no resources for clean up
	}
	
	public void validate(final IValidationContext context, final IReporter reporter) {
		validateInJob(context, reporter);
	}	

	public IStatus validateInJob(final IValidationContext context, final IReporter reporter)
	{
		cleanMarkers((IProjectValidationContext) context);
		
		IWebServiceProject wsProject;
		for (IDomValidator validator : getValidators()) 
		{
			wsProject = (IWebServiceProject)context.loadModel("webServiceProject", new String []{validator.getSupportedDomRuntime()}); //$NON-NLS-1$
			if (wsProject == null) {
				continue;
			}
			
			validator.validate(wsProject);			
			refreshTree(wsProject);
		}
		
		return IValidatorJob.OK_STATUS;
	}
	
	public ISchedulingRule getSchedulingRule(final IValidationContext helper) {
		return null;
	}

	protected void refreshTree(final IWebServiceProject wsProject) 
	{
		final DOMAdapterFactoryLabelProvider labelProvider = labelProvider();
		if (labelProvider == null) {
			return;
		}
		
		Display.getDefault().asyncExec(new RefreshRunnable(labelProvider, wsProject));
	}
	
	protected DOMAdapterFactoryLabelProvider labelProvider()
	{
		return DomIntegrationPlugin.getDefault().getLabelProvider();
	}
	
	protected void cleanMarkers(IProjectValidationContext context)
	{
		try {
			final IProject project = context.getProject();
			if (project != null && project.isAccessible()) {
				project.deleteMarkers(DomValidationConstants.MARKER_ID, true, IResource.DEPTH_INFINITE);
			}
		} 
		catch (CoreException e) {
			(new Logger()).logError(e.getMessage(), e);
		}		
	}

	protected Collection<IDomValidator> getValidators()
	{
		return DomValidatorFactory.INSTANCE.getRegisteredValidators();
	}
	
	/**
	 * Class that fires label changed event to the label provider to apply validation
	 * results in the displayed tree - i.e. decorate images
	 * 
	 * @author Georgi Vachkov
	 */
	protected class RefreshRunnable implements Runnable 
	{
		private final DOMAdapterFactoryLabelProvider labelProvider;
		private final IWebServiceProject wsProject;
		
		public RefreshRunnable(final DOMAdapterFactoryLabelProvider labelProvider, final IWebServiceProject wsProject) {
			this.labelProvider = labelProvider;
			this.wsProject = wsProject;
		}
		
		public void run() 
		{
			labelProvider.fireLabelProviderChanged(wsProject);
			
			fireLabelProviderChanged(ISEIChildList.class);
			fireLabelProviderChanged(IWebServiceChildList.class);
			
			final TreeIterator<EObject> allChilds = wsProject.eAllContents();
			while (allChilds.hasNext()) {
				labelProvider.fireLabelProviderChanged(allChilds.next());
			}
		}
		
		private void fireLabelProviderChanged(final Class<?> adapterClass)
		{
			final Adapter adapter = CustomDomItemProviderAdapterFactory.INSTANCE.adapt(wsProject, adapterClass);
			if (adapter == null) {
				return;
			}
			
			labelProvider.fireLabelProviderChanged(adapter);	
		}
	}
}
