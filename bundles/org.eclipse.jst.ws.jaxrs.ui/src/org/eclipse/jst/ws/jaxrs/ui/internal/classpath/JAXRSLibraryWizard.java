/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.ui.internal.classpath;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Dialog for creating or editing a JAXRS Library or Implementation. <br>
 * If the selection passed in init is not null then the item will be edit mode.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSLibraryWizard extends Wizard implements INewWizard {

	private JAXRSLibraryEditControl JAXRSLibraryEditControl;

	private boolean isNew = false;
	private boolean modified = false;
	private JAXRSLibrary curLibrary;
	private JAXRSLibrary workingCopyLibrary;

	private JAXRSLibraryWizardPage page;

	/**
	 * Constructor. List will include all JAXRS Libraries.
	 */
	public JAXRSLibraryWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		if (selection != null
				&& selection.getFirstElement() instanceof JAXRSLibrary) {
			curLibrary = (JAXRSLibrary) selection.getFirstElement();
			workingCopyLibrary = curLibrary.getWorkingCopy();
		} else {
			isNew = true;
			workingCopyLibrary = JAXRSLibraryRegistryFactory.eINSTANCE
					.createJAXRSLibrary();
		}
		if (isNew) {
			setWindowTitle(Messages.JAXRSLibraryWizard_CreateImplementation);
		} else {
			setWindowTitle(isNew ? Messages.JAXRSLibraryWizard_CreateJAXRSLibrary
					: Messages.JAXRSLibraryWizard_EditJAXRSLibrary);
		}
	}

	/**
	 * Updates the JAXRS Library instance with the values from the working copy
	 * and persists the registry.
	 * 
	 * If editing a library reference, referencing java models will be updated.
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		final String name = JAXRSLibraryEditControl.getJAXRSLibraryName();
		final boolean isDeployed = JAXRSLibraryEditControl.getIsDeployed();

		workingCopyLibrary.setName(name);
		workingCopyLibrary.setDeployed(isDeployed);

		final String originalID = curLibrary != null ? curLibrary.getID()
				: workingCopyLibrary.getID();

		if (isNew) {
			JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryRegistry()
					.addJAXRSLibrary(workingCopyLibrary);
		} else {
			curLibrary.updateValues(workingCopyLibrary);
			try {
				JAXRSLibraryRegistryUtil.rebindClasspathContainerEntries(
						originalID, workingCopyLibrary.getID(), null);
			} catch (JavaModelException e) {
				JAXRSUIPlugin.log(IStatus.ERROR,
						"Exception while updating JAXRS Library containers", e); //$NON-NLS-1$
			}
		}
		JAXRSLibraryRegistryUtil.getInstance().saveJAXRSLibraryRegistry();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	public void addPages() {
		page = new JAXRSLibraryWizardPage(
				Messages.JAXRSLibraryWizard_JAXRSLibrary);
		super.addPage(page);
		page.setWizard(this);
	}

	/**
	 * @return the JAXRSLibrary being modified by this wizard
	 */
	public JAXRSLibrary getJAXRSLibrary() {
		return workingCopyLibrary;
	}

	private class JAXRSLibraryWizardPage extends WizardPage {

		/**
		 * @param pageName
		 */
		protected JAXRSLibraryWizardPage(String pageName) {
			super(pageName);
			setDescription(Messages.JAXRSLibraryWizard_DESCRIPTION);
			setTitle(Messages.JAXRSLibraryWizard_JAXRSLibrary);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
		 */
		public boolean isPageComplete() {
			if (modified == false) {
				return false;
			}
			return super.isPageComplete();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt
		 * .widgets.Composite)
		 */
		public void createControl(Composite parent) {
			initializeDialogUnits(parent);

			JAXRSLibraryEditControl = new JAXRSLibraryEditControl(
					workingCopyLibrary, parent);
			JAXRSLibraryEditControl.setLayout(new GridLayout(2, false));
			JAXRSLibraryEditControl.setLayoutData(new GridData(
					GridData.FILL_BOTH));

			JAXRSLibraryEditControl
					.addValidationListener(new JAXRSLibraryValidationListener() {
						public void notifyValidation(
								JAXRSLibraryValidationEvent e) {
							setErrorMessage(e.getMessage());
							modified = true;
							setPageComplete(getErrorMessage() == null);
						}
					});

			setControl(JAXRSLibraryEditControl);
			setPageComplete(false);
		}

	}

}
