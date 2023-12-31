/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060315   128711 joan@ca.ibm.com - Joan Haggarty
 * 20080221   146023 gilberta@ca.ibm.com - Gilbert Andrews 
 * 20080512   222094 makandre@ca.ibm.com - Andrew Mak, Error handling when Ant template cannot be found
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.ui.dialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.plugin.EnvPlugin;
import org.eclipse.wst.command.internal.env.ui.EnvironmentUIMessages;
import org.eclipse.wst.command.internal.env.ui.plugin.EnvUIPlugin;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.environment.NullStatusHandler;
import org.osgi.framework.Bundle;

public class AntFileImportWizard extends Wizard implements INewWizard {

	//private IConfigurationElement fConfigElement;
	private AntFileImportWizardPage mainPage;
	/**
     * The workbench.
     */
    private IWorkbench workbench;
    private AntExtensionCreation antExtC_;

    /**
     * The current selection.
     */
    protected IStructuredSelection selection;
    
	public AntFileImportWizard()
	{
		super();		 
		setWindowTitle(EnvironmentUIMessages.WIZARD_TITLE_ANT);
	}
	
	public void setWindowTitle(String newTitle) {
		super.setWindowTitle(newTitle);
	}
	
	 public void addPages() {
	        super.addPages();	        
	        mainPage = new AntFileImportWizardPage(EnvironmentUIMessages.WIZARD_PAGE_TITLE_ANT, getSelection());
            addPage(mainPage);
	    }
	
	public boolean performFinish() {	
		antExtC_ = AntExtensionCreation.getInstance();
		IPath destination = mainPage.getPath();
		if (destination == null)
		{
			return false;
		}			
		else
		{
			AntExtension antExt = antExtC_.getAnrExtByScenario(mainPage.getScenario());
			Bundle wsBundle = Platform.getBundle(antExt.getPlugin());
			IPath propertyPath = new Path(antExt.getPath());
			URL propertyURL = FileLocator.find( wsBundle, propertyPath, null);
			
			if (propertyURL == null) {
				mainPage.displayErrorDialog(
						NLS.bind(EnvironmentUIMessages.MSG_ERR_ANT_FILES_NOT_FOUND, antExt.getPath()));
				return false;
			}
			
			URLConnection conn;
			InputStream isProperty = null;
			try{
				conn = propertyURL.openConnection();
				isProperty = conn.getInputStream();
		    }
		    catch (IOException ioe){}
		    
		    Plugin sourcePlugin = EnvPlugin.getInstance();  
			String targetFile = antExt.getPath();
		    createIFile(sourcePlugin, isProperty, destination, targetFile, (IProgressMonitor)new NullProgressMonitor());
		    
		    IPath wsgenPath = new Path(antExt.getWSGenPath());
			URL fileURL = FileLocator.find( wsBundle, wsgenPath, null);
			
			if (fileURL == null) {
				mainPage.displayErrorDialog(
						NLS.bind(EnvironmentUIMessages.MSG_ERR_ANT_FILES_NOT_FOUND, antExt.getWSGenPath()));				
				return false;
			}
			
			URLConnection connWSGen;
			InputStream isWSGen = null;
			try{
				connWSGen = fileURL.openConnection();
				isWSGen = connWSGen.getInputStream();
		    }
		    catch (IOException ioe){}
		    
		    String targetFileWSGen = antExt.getWSGenPath();
		    createIFile(sourcePlugin, isWSGen, destination, targetFileWSGen, (IProgressMonitor)new NullProgressMonitor());
			
		    
		   
		}
	    return true;		
  }	
	
	
	
	private IStatus createIFile(Plugin plugin, InputStream source, IPath targetPath, String targetFile, /*IEnvironment env,*/ IProgressMonitor monitor )
	{
	    
	  if (plugin != null)
	  {
	    IPath target = targetPath.append(new Path(targetFile));
	    
	    ProgressUtils.report( monitor, EnvironmentUIMessages.MSG_STATUS_COPYING_ANT_FILES);
	    try
	    {
	       ResourceContext context = new TransientResourceContext();
	       // check to see if file exists before copy
	       IResource resource = FileResourceUtils.findResource(target);	     
	       
	       if (resource != null)
			{
				MessageBox overwriteBox = new MessageBox(getShell(), SWT.ICON_QUESTION|SWT.YES|SWT.NO);
				overwriteBox.setMessage(EnvironmentUIMessages.bind(EnvironmentUIMessages.MSG_WARNING_FILE_EXISTS, target.toString()));				
				overwriteBox.setText(EnvironmentUIMessages.DIALOG_TITLE_OVERWRITE);
				int result = overwriteBox.open();
				if (result != SWT.NO)
					FileResourceUtils.createFile(context, target, source,
                            monitor, (IStatusHandler)new NullStatusHandler());
				else 
					return Status.CANCEL_STATUS;
			}       
			else 
			{
				FileResourceUtils.createFile(context, target, source,
                                    monitor, (IStatusHandler)new NullStatusHandler());
			}
	    }
	    catch (Exception e) {	    	
	      return StatusUtils.errorStatus(EnvironmentUIMessages.MSG_ERR_COPYING_ANT_FILES, e);
	    }
	  }	  
	  return Status.OK_STATUS;
	}
	
    /**
     * Returns the selection which was passed to <code>init</code>.
     *
     * @return the selection
     */
    public IStructuredSelection getSelection() {
        return selection;
    }

    /**
     * Returns the workbench which was passed to <code>init</code>.
     *
     * @return the workbench
     */
    public IWorkbench getWorkbench() {
        return workbench;
    }

    /**
     * The <code>BasicNewResourceWizard</code> implementation of this 
     * <code>IWorkbenchWizard</code> method records the given workbench and
     * selection, and initializes the default banner image for the pages
     * by calling <code>initializeDefaultPageImageDescriptor</code>.
     * Subclasses may extend.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.workbench = workbench;
        this.selection = currentSelection;

        initializeDefaultPageImageDescriptor();
    }

    /**
     * Initializes the default page image descriptor to an appropriate banner.
     * By calling <code>setDefaultPageImageDescriptor</code>.
     * The default implementation of this method uses a generic new wizard image.
     * <p>
     * Subclasses may reimplement.
     * </p>
     */
    protected void initializeDefaultPageImageDescriptor() {
    	ImageDescriptor desc = EnvUIPlugin.getImageDescriptor("icons/full/wizban/newantfiles_wiz.png");//$NON-NLS-1$
        setDefaultPageImageDescriptor(desc);
    }

    /**
     * Selects and reveals the newly added resource in all parts
     * of the active workbench window's active page.
     *
     * @see ISetSelectionTarget
     */
    protected void selectAndReveal(IResource newResource) {
        selectAndReveal(newResource, getWorkbench().getActiveWorkbenchWindow());
    }

    /**
     * Attempts to select and reveal the specified resource in all
     * parts within the supplied workbench window's active page.
     * <p>
     * Checks all parts in the active page to see if they implement <code>ISetSelectionTarget</code>,
     * either directly or as an adapter. If so, tells the part to select and reveal the
     * specified resource.
     * </p>
     *
     * @param resource the resource to be selected and revealed
     * @param window the workbench window to select and reveal the resource
     * 
     * @see ISetSelectionTarget
     */
    public static void selectAndReveal(IResource resource,
            IWorkbenchWindow window) {
        // validate the input
        if (window == null || resource == null)
            return;
        IWorkbenchPage page = window.getActivePage();
        if (page == null)
            return;

        // get all the view and editor parts
        List parts = new ArrayList();
        IWorkbenchPartReference refs[] = page.getViewReferences();
        for (int i = 0; i < refs.length; i++) {
            IWorkbenchPart part = refs[i].getPart(false);
            if (part != null)
                parts.add(part);
        }
        refs = page.getEditorReferences();
        for (int i = 0; i < refs.length; i++) {
            if (refs[i].getPart(false) != null)
                parts.add(refs[i].getPart(false));
        }

        final ISelection selection = new StructuredSelection(resource);
        Iterator itr = parts.iterator();
        while (itr.hasNext()) {
            IWorkbenchPart part = (IWorkbenchPart) itr.next();

            // get the part's ISetSelectionTarget implementation
            ISetSelectionTarget target = null;
            if (part instanceof ISetSelectionTarget)
                target = (ISetSelectionTarget) part;
            else
                target = (ISetSelectionTarget) part
                        .getAdapter(ISetSelectionTarget.class);

            if (target != null) {
                // select and reveal resource
                final ISetSelectionTarget finalTarget = target;
                window.getShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        finalTarget.selectReveal(selection);
                    }
                });
            }
        }
    }
	
}
