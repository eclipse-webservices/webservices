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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension2;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.jaxrsappconfig.JAXRSAppConfigUtils;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryConfigurationHelper;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;

/**
 * Provides a classpath container wizard page for JAXRS Libraries.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSLibraryContainerWizardPage extends WizardPage implements
		IClasspathContainerPage, IClasspathContainerPageExtension,
		IClasspathContainerPageExtension2 {

	private CheckboxTableViewer lv;
	private JAXRSLibrariesTableViewerAdapter lvAdapter;
	private JAXRSLibrariesListLabelProvider lvLabelProvider;

	private boolean isJAXRSProject = false;
	private IClasspathEntry containerEntry;
	private IClasspathEntry[] currentEntries;
	@SuppressWarnings("unchecked")
	private Map _currentLibs;
	private JAXRSLibrary currentLib;

	private IProject _iproject;
	private boolean updatedLibs = false;

	/**
	 * Zero arg constructor
	 */
	public JAXRSLibraryContainerWizardPage() {
		super(Messages.JAXRSLibraryContainerWizardPage_PageName);
		setTitle(Messages.JAXRSLibraryContainerWizardPage_Title);
		setDescription(Messages.JAXRSLibraryContainerWizardPage_Description);
		// TODO: Replace with a custom image.
		setImageDescriptor(JAXRSUIPlugin
				.getImageDescriptor("full/wizban/addlibrary_wiz.gif")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension#initialize
	 * (org.eclipse.jdt.core.IJavaProject,
	 * org.eclipse.jdt.core.IClasspathEntry[])
	 */
	public void initialize(IJavaProject project,
			IClasspathEntry[] currentEntries_) {
		this.currentEntries = currentEntries_;

		_iproject = project.getProject();
		this.isJAXRSProject = JAXRSAppConfigUtils
				.isValidJAXRSProject(_iproject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#finish()
	 */
	public boolean finish() {
		boolean finish = true;
		return finish;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension2#getNewContainers
	 * ()
	 */
	public IClasspathEntry[] getNewContainers() {
		IPath cp = new Path(
				JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID);
		List<IClasspathEntry> res = new ArrayList<IClasspathEntry>();
		Object[] items = lv.getCheckedElements();
		for (int i = 0; i < items.length; i++) {
			JAXRSLibrary jaxrsLib = (JAXRSLibrary) items[i];
			if (getSelectedJAXRSLibariesForProject().get(jaxrsLib.getID()) == null) {
				IPath path = cp.append(new Path(jaxrsLib.getID()));
				IClasspathEntry entry = JavaCore.newContainerEntry(path);
				// need to update wtp dependency in j2ee mod dependency ui
				res.add(entry);
			}
		}
		return res.toArray(new IClasspathEntry[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete() {
		if (!isJAXRSProject) {
			return false;
		}
		if (updatedLibs)
			return isValid();
		if (isEditReference() && !selectionHasChanged())
			return false;

		return isValid();
	}

	private boolean isValid() {
		return isCheckedItems() && getErrorMessage() == null;
	}

	// to be used to know whether the selected library has changed when in
	// "edit" mode
	private boolean selectionHasChanged() {
		JAXRSLibrary lib = getCurrentLibrarySelection();
		if (lib == null)
			return false;
		return (getJAXRSLibraryForEdit(containerEntry) != lib);

	}

	private JAXRSLibrary getCurrentLibrarySelection() {
		JAXRSLibrary lib = null;
		if (!(lv == null)) {
			StructuredSelection ssel = (StructuredSelection) lv.getSelection();
			if (ssel != null && !ssel.isEmpty()) {
				lib = (JAXRSLibrary) ssel.getFirstElement();
			}
		}
		return lib;
	}

	private boolean isCheckedItems() {
		return lv.getCheckedElements().length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.ui.wizards.IClasspathContainerPage#getSelection()
	 */
	public IClasspathEntry getSelection() {
		IClasspathEntry entry = null;
		if (isEditReference()) {
			if (lv.getCheckedElements().length == 0)
				return containerEntry;

			JAXRSLibrary lib = (JAXRSLibrary) lv.getCheckedElements()[0];
			if (lib != null) {
				if (lib == getJAXRSLibraryForEdit(containerEntry)) {
					return containerEntry;
				}
				IPath path = new Path(
						JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID)
						.append(new Path(lib.getID()));
				entry = JavaCore.newContainerEntry(path, containerEntry
						.getAccessRules(), containerEntry.getExtraAttributes(),
						containerEntry.isExported());
			}
		}
		return entry;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jdt.ui.wizards.IClasspathContainerPage#setSelection(org.eclipse
	 * .jdt.core.IClasspathEntry)
	 */
	public void setSelection(IClasspathEntry containerEntry) {
		// this is signalling that this is an "edit"
		this.containerEntry = containerEntry;
	}

	@SuppressWarnings("unchecked")
	public void createControl(Composite parent) {
		// Build UI to display JAXRS Lib components from registry
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(GridData.FILL_BOTH));

		// disable wizard if this is not a valid JAXRS project
		if (!isJAXRSProject) {
			Label warning = new Label(c, SWT.NONE);
			warning
					.setText(Messages.JAXRSLibraryContainerWizardPage_WarningNoJAXRSFacet);
			setControl(c);
			return;
		}

		Label lblViewer = new Label(c, SWT.NONE);
		lblViewer
				.setText(Messages.JAXRSLibraryContainerWizardPage_JAXRSLibraries);
		GridData gd1 = new GridData(GridData.BEGINNING);
		gd1.horizontalSpan = 2;
		lblViewer.setLayoutData(gd1);

		lv = createTableViewer(c);
		lv.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

		lvAdapter = new JAXRSLibrariesTableViewerAdapter();
		lvLabelProvider = new JAXRSLibrariesListLabelProvider();
		lv.setContentProvider(lvAdapter);
		lv.setLabelProvider(lvLabelProvider);
		lv.addSelectionChangedListener(lvAdapter);
		lv.addDoubleClickListener(lvAdapter);
		lv.setComparator(lvAdapter);

		Composite buttons = new Composite(c, SWT.NONE);
		buttons.setLayout(new GridLayout(1, false));
		buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		final Button addButton = new Button(buttons, SWT.NONE);
		addButton.setText(Messages.JAXRSLibraryContainerWizardPage_Add);
		addButton.setLayoutData(new GridData(GridData.END
				| GridData.VERTICAL_ALIGN_BEGINNING));
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openJAXRSLibraryWizard(null);
			}
		});

		final Button editButton = new Button(buttons, SWT.NONE);
		editButton.setText(Messages.JAXRSLibraryContainerWizardPage_Edit);
		editButton.setLayoutData(new GridData(GridData.END
				| GridData.VERTICAL_ALIGN_BEGINNING));
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection sel = (StructuredSelection) lv
						.getSelection();
				if ((sel == null || sel.isEmpty()) && containerEntry != null) {
					JAXRSLibrary jaxrsLib = getJAXRSLibraryForEdit(containerEntry);
					sel = new StructuredSelection(jaxrsLib);
				}
				openJAXRSLibraryWizard(sel);
			}

		});
		editButton.setVisible(false);
		lv.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setEditButton(event.getSelection());
			}

			private void setEditButton(final ISelection selection) {
				IStructuredSelection sel = (IStructuredSelection) selection;
				editButton.setVisible(sel.size() == 1);
				if (sel.size() == 1) {
					JAXRSLibrary lib = (JAXRSLibrary) sel.getFirstElement();
					boolean pp = lib instanceof PluginProvidedJAXRSLibrary;
					editButton.setEnabled(!pp);
					if (isEditReference()) {
						lv.setAllChecked(false);
						lv.setChecked(lib, true);
					}
				}

			}
		});
		setControl(c);

		if (isEditReference()) {
			JAXRSLibrary lib = getJAXRSLibraryForEdit(containerEntry);
			lv.setInput(getAllUnselectedJAXRSLibrariesExceptReferencedLib(lib));
			selectAndCheckCurrentLib(lib);
			setDescription(Messages.JAXRSLibraryContainerWizardPage_EditLibrary_DescriptionText);
		} else {
			lv.setInput(getAllJAXRSLibraries());
			lv.setCheckedElements(getSelectedJAXRSLibariesForProject().values()
					.toArray(new Object[0]));
		}
	}

	private void selectAndCheckCurrentLib(final JAXRSLibrary lib) {
		if (lib != null) {
			StructuredSelection ssel = new StructuredSelection(lib);
			lv.setSelection(ssel);
			lv.setChecked(lib, true);
		}
	}

	@SuppressWarnings("unchecked")
	private Object getAllUnselectedJAXRSLibrariesExceptReferencedLib(
			JAXRSLibrary referenceLib) {
		List allLibs = getAllJAXRSLibraries();
		Collection selLibs = getSelectedJAXRSLibariesForProject().values();
		for (Iterator it = selLibs.iterator(); it.hasNext();) {
			JAXRSLibrary aLib = (JAXRSLibrary) it.next();
			int i = allLibs.indexOf(aLib);
			// remove from allLibs unless it is the selected reference
			if (i >= 0
					&& ((referenceLib == null) || (aLib != null && !aLib
							.getID().equals(referenceLib.getID())))) {
				allLibs.remove(i);
			}
		}
		return allLibs;
	}

	private List<JAXRSLibrary> getJAXRSLibraryEntries(IClasspathEntry[] entries) {
		List<JAXRSLibrary> jaxrsLibs = new ArrayList<JAXRSLibrary>();
		for (int i = 0; i < entries.length; i++) {
			IClasspathEntry entry = entries[i];
			if (JAXRSLibraryConfigurationHelper.isJAXRSLibraryContainer(entry)) {
				JAXRSLibrary lib = JAXRSLibraryRegistryUtil.getInstance()
						.getJAXRSLibraryRegistry().getJAXRSLibraryByID(
								getLibraryId(entry));
				if (lib != null) {
					jaxrsLibs.add(lib);
				}
			}
		}

		return jaxrsLibs;
	}

	private String getLibraryId(IClasspathEntry entry) {
		return entry.getPath().segment(1);
	}

	private void openJAXRSLibraryWizard(IStructuredSelection element) {
		IWorkbenchWizard wizard = new JAXRSLibraryWizard();
		IWorkbench wb = PlatformUI.getWorkbench();
		wizard.init(wb, element);
		WizardDialog dialog = new WizardDialog(wb.getActiveWorkbenchWindow()
				.getShell(), wizard);
		int ret = dialog.open();
		if (ret == Window.OK) {
			// user made a change to libraries
			updatedLibs = true;
			// FIXME: select returned object
			if (containerEntry == null) {
				lv.setInput(getAllJAXRSLibraries());
			} else {
				lv
						.setInput(getAllUnselectedJAXRSLibrariesExceptReferencedLib(getJAXRSLibraryForEdit(containerEntry)));
				lv.refresh(true);
			}
			lv.refresh();
			setPageComplete(isPageComplete());
		}
	}

	private CheckboxTableViewer createTableViewer(Composite parent) {
		Table table = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		table.setFont(parent.getFont());
		CheckboxTableViewer tableViewer = new CheckboxTableViewer(table);
		tableViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent e) {
				if (!isEditReference()) {
					// ensure that existing CP entries cannot be unchecked
					if (getSelectedJAXRSLibariesForProject().get(
							((JAXRSLibrary) e.getElement()).getID()) != null) {
						if (containerEntry == null)
							e.getCheckable().setChecked(e.getElement(), true);
						else
							lv.setAllChecked(true);
					}
				} else {
					// select only one
					lv.setAllChecked(false);
					lv.setChecked(e.getElement(), true);
					if (isEditReference())
						lv
								.setSelection(new StructuredSelection(e
										.getElement()));
				}
				validate();
			}
		});
		return tableViewer;
	}

	@SuppressWarnings("unchecked")
	private Map getSelectedJAXRSLibariesForProject() {
		if (_currentLibs == null) {
			List allLibs = getAllJAXRSLibraries();
			List<JAXRSLibrary> curLibs = getJAXRSLibraryEntries(currentEntries);
			_currentLibs = new HashMap(curLibs.size());
			for (Iterator<JAXRSLibrary> it = curLibs.iterator(); it.hasNext();) {
				JAXRSLibrary lib = it.next();
				int index = getIndex(allLibs, lib);
				if (index >= 0)
					_currentLibs.put(lib.getID(), allLibs.get(index));
			}

		}
		return _currentLibs;
	}

	@SuppressWarnings("unchecked")
	private List getAllJAXRSLibraries() {
		List allLibs = JAXRSLibraryRegistryUtil.getInstance()
				.getJAXRSLibraryRegistry().getAllJAXRSLibraries();

		return allLibs;
	}

	private JAXRSLibrary getJAXRSLibraryForEdit(IClasspathEntry containerEntry_) {
		if (currentLib == null) {
			String id = getLibraryId(containerEntry_);
			currentLib = JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getJAXRSLibraryByID(id);
		}
		return currentLib;

	}

	@SuppressWarnings("unchecked")
	private int getIndex(List libs, JAXRSLibrary lib) {
		for (int i = 0; i < libs.size(); i++) {
			if (lib.getID().equals(((JAXRSLibrary) libs.get(i)).getID()))
				return i;
		}
		return -1;
	}

	private class JAXRSLibrariesTableViewerAdapter extends ViewerComparator
			implements IStructuredContentProvider, ISelectionChangedListener,
			IDoubleClickListener {

		private Object input;

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			input = newInput;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			return ((List) input).toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged
		 * (org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			if (isEditReference()) {
				setPageComplete(isPageComplete());
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse
		 * .jface.viewers.DoubleClickEvent)
		 */
		public void doubleClick(DoubleClickEvent event) {
			doDoubleClick(event);
		}

		@SuppressWarnings("unchecked")
		public int compare(Viewer viewer, Object e1, Object e2) {
			JAXRSLibrary lib1 = (JAXRSLibrary) e1;
			JAXRSLibrary lib2 = (JAXRSLibrary) e2;

			// sort first by in selection already and then by name
			boolean lib1Sel = getSelectedJAXRSLibariesForProject().get(
					lib1.getID()) != null;
			boolean lib2Sel = getSelectedJAXRSLibariesForProject().get(
					lib2.getID()) != null;

			if ((lib1Sel && lib2Sel) || (!lib1Sel && !lib2Sel)) {
				return getComparator()
						.compare(lib1.getLabel(), lib2.getLabel());
			} else if (lib1Sel)
				return -1;
			else
				return 1;
		}
	}

	private static class JAXRSLibrariesListLabelProvider implements
			ILabelProvider {
		Image libImg;

		public Image getImage(Object element) {
			if (libImg == null) {
				ImageDescriptor libImgDesc = JAXRSUIPlugin
						.getImageDescriptor("obj16/library_obj.gif"); //$NON-NLS-1$
				libImg = libImgDesc.createImage();
			}
			return libImg;
		}

		public String getText(Object element) {
			if (element instanceof JAXRSLibrary) {
				JAXRSLibrary lib = (JAXRSLibrary) element;
				return lib.getLabel();
			}
			return null;
		}

		public void dispose() {
			if (libImg != null)
				libImg.dispose();
		}

		public void addListener(ILabelProviderListener listener) {
			// no listener support
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// no listener support
		}
	}

	private void validate() {
		setErrorMessage(null);
		int implChosenCount = implSelectedCount();
		if (implChosenCount > 1) {
			setErrorMessage(Messages.JAXRSLibraryContainerWizardPage_ImplAlreadyPresent);
		}
		setPageComplete(isPageComplete());
	}

	private boolean isEditReference() {
		return (containerEntry != null);
	}

	private int implSelectedCount() {
		return lv.getCheckedElements().length;
	}

	private void doDoubleClick(DoubleClickEvent event) {
		StructuredSelection ssel = (StructuredSelection) event.getSelection();
		if (ssel != null
				&& (!((JAXRSLibrary) ssel.getFirstElement() instanceof PluginProvidedJAXRSLibrary)))
			openJAXRSLibraryWizard((IStructuredSelection) event.getSelection());
	}

}
