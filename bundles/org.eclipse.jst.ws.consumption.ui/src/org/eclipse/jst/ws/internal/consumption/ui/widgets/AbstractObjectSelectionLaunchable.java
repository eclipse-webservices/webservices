/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060420   135912 joan@ca.ibm.com - Joan Haggarty
 *******************************************************************************/
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
	
	public boolean validate(String s) {
		// subclass should override this method
		return true;
	}  

}
