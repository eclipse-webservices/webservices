package org.eclipse.jst.ws.internal.axis2.consumption.ui.task;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class DefaultsForHTTPBasicAuthCommand extends AbstractDataModelOperation {

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		IStatus status = Status.OK_STATUS;
		return status;
	}

}
