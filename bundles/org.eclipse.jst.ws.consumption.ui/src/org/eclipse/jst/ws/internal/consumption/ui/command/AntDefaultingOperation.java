package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AntDefaultingOperation extends AbstractDataModelOperation{

	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		// TODO Auto-generated method stub
		return Status.OK_STATUS;
	}
	
	public boolean getServiceIdsFixed()
	{
		return true;
	}

	public boolean getClientIdsFixed()
	{
		return true;
	}
	
	public boolean getStartService()
	{
		return false;	
	}
	
	public boolean getInstallService()
	{
		return false;
	}
}
