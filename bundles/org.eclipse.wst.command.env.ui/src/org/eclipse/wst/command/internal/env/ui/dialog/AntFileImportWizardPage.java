/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.dialog;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;

public class AntFileImportWizardPage extends WizardResourceImportPage{
	  
	public AntFileImportWizardPage(String pageName,
	            IStructuredSelection selection) {
	        super("WSAntFilesPage1", selection);//$NON-NLS-1$
	        setTitle(pageName);	        
	        setDescription(EnvironmentUIMessages.WIZARD_PAGE_DESC_ANT);	        
	    }
	
	protected ITreeContentProvider getFileProvider() {
		return null;
	}
	
	public IPath getPath()
	{
		return getContainerFullPath();
	}
	
		protected void createSourceGroup(Composite parent) {
			//don't need source group
		}
	
	protected ITreeContentProvider getFolderProvider() {
		 return null;
	}
	
	protected void createOptionsGroup(Composite parent) {
        //empty - don't want an options group 
    }
	
	
	
}



