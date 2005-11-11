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



