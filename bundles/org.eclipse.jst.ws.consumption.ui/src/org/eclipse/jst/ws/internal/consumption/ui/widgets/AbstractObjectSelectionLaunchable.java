package org.eclipse.jst.ws.internal.consumption.ui.widgets;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractObjectSelectionLaunchable implements IObjectSelectionLaunchable {

	public int launch(Shell shell) {		
		return -1;
	}
	
	public IStatus validateSelection(IStructuredSelection objectSelection) {
		 //  subclass should override this method
		 return Status.OK_STATUS;
	}
	
	public IStructuredSelection getObjectSelection() {
		 //  subclass should override this method
		return new StructuredSelection();
	}
	
	public void setInitialSelection(IStructuredSelection initialSelection) {
		 //  subclass should override this method
	}	
	
	public String getObjectSelectionDisplayableString()
	{
		 //  subclass should override this method
		return "";
	}

}
