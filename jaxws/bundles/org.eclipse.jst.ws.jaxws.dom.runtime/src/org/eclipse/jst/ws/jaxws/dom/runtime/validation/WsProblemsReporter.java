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

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IValidationListener;
import org.eclipse.emf.validation.service.ValidationEvent;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.utils.dom.validation.DomValidationConstants;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * Problems reporter. Listens to validation events and reports errors.  
 * 
 * @author Georgi Vachkov
 */
public class WsProblemsReporter implements IValidationListener 
{
	/** The validation marker ID */
	public static final String MARKER_ID = DomValidationConstants.MARKER_ID;

	
	public void validationOccurred(ValidationEvent event) 
	{
		final List<IConstraintStatus> list = event.getValidationResults();
		for (IConstraintStatus cStatus : list)
		{
			assert cStatus instanceof IConstraintStatusExtended;
			
			if (cStatus.isOK()) {
				continue;
			}

			processStatus(cStatus);
		}
	}
	
	private void processStatus(final IConstraintStatus status)
	{		
		if (!(status instanceof IConstraintStatusExtended)) {
			return;
		}

		try {
			final IProblemLocation locator = ((IConstraintStatusExtended)status).getProblemLocations().iterator().next();
			final IMarker marker = locator.getResource().createMarker(MARKER_ID);
			if (locator.getLocator() != null) 
			{
				marker.setAttribute(IMarker.CHAR_START, locator.getLocator().getStartPosition());
				marker.setAttribute(IMarker.CHAR_END, locator.getLocator().getStartPosition() + locator.getLocator().getLength());
				marker.setAttribute(IMarker.LINE_NUMBER, locator.getLocator().getLineNumber());
			}
			marker.setAttribute(IMarker.MESSAGE, status.getMessage());
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
			marker.setAttribute(IMarker.SEVERITY, getMarkerSeverity(status.getSeverity()));
			marker.setAttribute(IMarker.SOURCE_ID, status.getConstraint().getDescriptor().getId());
			marker.setAttribute(DomValidationConstants.IMPLEMENTATION, DomUtil.INSTANCE.calcUniqueImpl(status.getTarget()));
		} 
		catch (CoreException e) {
			logger().logError(e.getMessage(), e);
		}
	}
	
	private int getMarkerSeverity(int statusSeverity)
	{
		switch(statusSeverity)
		{
		case IStatus.INFO: return IMarker.SEVERITY_INFO;
		case IStatus.WARNING: return IMarker.SEVERITY_WARNING;
		default:
			return IMarker.SEVERITY_ERROR;
		}
	}

	private ILogger logger() {
		return new Logger();
	}
}
