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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Common control for adding JAXRS Library instances
 */
@SuppressWarnings("restriction")
public class JAXRSLibraryEditControl extends Composite implements
		ModifyListener, SelectionListener {
	private Text txtName;
	private Label lblName;
	// private CCombo cboVersions;
	// private Label lblVersions;
	private Button chkDeploy;
	// private Button chkImpl;
	private Composite btnBar;
	private Button btnAdd;
	private Button btnRemove;
	private TableViewer jars;

	private boolean initing = false;

	private JAXRSLibrary workingCopyLibrary;

	private String validationMsg;
	private Set<JAXRSLibraryValidationListener> _listeners;
	private int _isNew = -1;// use isNew() method.. not this variable directly

	/**
	 * @param workingCopyLibrary
	 *            working copy of the JAXRS Library
	 * @param parent
	 *            parent SWT control
	 */
	public JAXRSLibraryEditControl(JAXRSLibrary workingCopyLibrary,
			Composite parent) {
		super(parent, SWT.NONE);
		this.workingCopyLibrary = workingCopyLibrary;
		_listeners = new HashSet<JAXRSLibraryValidationListener>(1);
		createControl(parent);
	}

	/**
	 * @param listener
	 */
	public void addValidationListener(JAXRSLibraryValidationListener listener) {
		removeValidationListener(listener);
		_listeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeValidationListener(JAXRSLibraryValidationListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * @param parent
	 */
	public void createControl(Composite parent) {
		initing = true;

		// this = new Composite(parent, SWT.NONE);
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(GridData.FILL_BOTH));

		lblName = new Label(this, SWT.NONE);
		lblName.setText(Messages.JAXRSLibraryWizard_LibraryName);
		lblName.setLayoutData(new GridData(GridData.BEGINNING));

		txtName = new Text(this, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtName.addModifyListener(this);

		// lblVersions = new Label(this, SWT.NONE);
		// lblVersions.setText(Messages.JAXRSLibraryWizard_VersionSupported);

		// cboVersions = new CCombo(this, SWT.SINGLE | SWT.READ_ONLY |
		// SWT.BORDER);
		// cboVersions.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// cboVersions.addModifyListener(this);

		Group jarsComp = new Group(this, SWT.NONE);
		jarsComp.setText(Messages.JAXRSLibraryWizard_LibraryJars);
		GridLayout gl1 = new GridLayout(2, false);
		jarsComp.setLayout(gl1);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		gd2.horizontalSpan = 2;
		jarsComp.setLayoutData(gd2);

		createJarsViewer(jarsComp);

		createButtons(jarsComp);

		// chkImpl = new Button(this, SWT.CHECK);
		// chkImpl.setText(Messages.JAXRSLibraryWizard_IsJAXRSImplementation);
		// GridData gd3 = new GridData();
		// gd3.horizontalSpan = 2;
		// chkImpl.setLayoutData(gd3);
		// chkImpl.addSelectionListener(this);

		chkDeploy = new Button(this, SWT.CHECK);
		chkDeploy.setText(Messages.JAXRSLibraryWizard_DeployJars);
		GridData gd4 = new GridData();
		gd4.horizontalSpan = 2;
		chkDeploy.setLayoutData(gd4);
		// chkDeploy.addSelectionListener(this);
		chkDeploy.setVisible(false);

		// loadVersions();

		if (!isNew()) {
			txtName.setText(workingCopyLibrary.getName());
			// if
			// (workingCopyLibrary.getJAXRSVersion().getName().equals(JAXRSVersion.UNKNOWN_LITERAL.getName()))
			// {
			// cboVersions.setText(Messages.JAXRSLibraryEditControl_ImplVersion_UNKNOWN);
			// } else {
			// cboVersions.setText(workingCopyLibrary.getJAXRSVersion().getName());
			// }
			chkDeploy.setSelection(workingCopyLibrary.isDeployed());
			// chkImpl.setSelection(workingCopyLibrary.isImplementation());
		}
		jars.setInput(workingCopyLibrary);

		initing = false;

		txtName.setFocus();
	}

	private void createJarsViewer(Group jarsComp) {
		jars = new TableViewer(jarsComp, SWT.BORDER | SWT.MULTI);
		jars.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof JAXRSLibrary)
					return ((JAXRSLibrary) inputElement).getArchiveFiles()
							.toArray();
				return new Object[0];
			}

			public void dispose() {
				// do nothing
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// won't happen
			}
		});
		jars.setLabelProvider(new ILabelProvider() {
			private Image jarImg = null;

			public Image getImage(Object element) {
				if (jarImg == null) {
					ImageDescriptor desc = JAXRSUIPlugin
							.getImageDescriptor("obj16/jar_obj.gif"); //$NON-NLS-1$
					jarImg = desc.createImage();
				}
				return jarImg;
			}

			public String getText(Object element) {
				StringBuffer labelBuf = new StringBuffer();
				if (element instanceof ArchiveFile) {
					ArchiveFile archive = (ArchiveFile) element;
					labelBuf.append(archive.getName());
					if (!archive.exists())
						labelBuf
								.append(Messages.JAXRSLibrariesPreferencePage_MISSING_DESC);
					labelBuf.append(" - ").append(archive.getPath()); //$NON-NLS-1$
				}
				return labelBuf.toString();
			}

			public void addListener(ILabelProviderListener listener) {
				// no listeners supported
			}

			public void dispose() {
				if (jarImg != null)
					jarImg.dispose();
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
				// no listeners supported
			}
		});

		jars.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}
		});

		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.widthHint = convertWidthInCharsToPixels(30);
		// gd.heightHint = convertHeightInCharsToPixels(10);
		jars.getControl().setLayoutData(gd);
	}

	private void updateButtons() {
		btnRemove.setEnabled(!((StructuredSelection) jars.getSelection())
				.isEmpty());
		// getButton(IDialogConstants.OK_ID).setEnabled(modified);
	}

	private void createButtons(Composite c) {
		btnBar = new Composite(c, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gl.marginHeight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		btnBar.setLayout(gl);
		btnBar.setLayoutData(new GridData(GridData.END));

		btnAdd = new Button(btnBar, SWT.NONE);
		btnAdd.setText(Messages.JAXRSLibraryWizard_Add);
		btnAdd.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				String cur = null;
				String[] chosenJars = openExtJarFileDialog(cur);
				if (chosenJars != null) {
					for (int i = 0; i < chosenJars.length; i++) {
						String jar = chosenJars[i];
						if (!workingCopyLibrary.containsArchiveFile(jar)) {
							ArchiveFile archive = JAXRSLibraryRegistryFactory.eINSTANCE
									.createArchiveFile();
							archive.setSourceLocation(jar);
							archive.setRelativeDestLocation("WEB-INF/lib"); //$NON-NLS-1$
							workingCopyLibrary.getArchiveFiles().add(archive);
						}
					}
					jars.refresh();
					validate();
				}
			}
		});

		btnRemove = new Button(btnBar, SWT.NONE);
		btnRemove.setEnabled(false);
		btnRemove.setText(Messages.JAXRSLibraryWizard_Remove);
		btnRemove.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				if (jars.getSelection() instanceof StructuredSelection) {
					StructuredSelection objs = (StructuredSelection) jars
							.getSelection();
					if (objs != null) {
						Iterator it = objs.iterator();
						while (it.hasNext()) {
							Object obj = it.next();
							ArchiveFile jar = (ArchiveFile) obj;
							workingCopyLibrary.getArchiveFiles().remove(jar);
						}
					}
					jars.refresh();
					validate();
				}
			}
		});
	}

	private String[] openExtJarFileDialog(String existing) {
		String title = Messages.JAXRSLibraryWizard_ExtJarFileDialogTitle;

		FileDialog dialog = new FileDialog(getShell(),
				existing == null ? SWT.MULTI : SWT.SINGLE);
		dialog.setText(title);
		dialog.setFilterExtensions(new String[] { "*.jar;*.zip" }); //$NON-NLS-1$
		// FIXME: remember and use last path chosen??
		String filterPath = ResourcesPlugin.getWorkspace().getRoot()
				.getFullPath().toString();
		dialog.setFilterPath(filterPath);
		// if (existing != null) {
		// dialog.setFileName(existing.getPath().lastSegment());
		// }

		String res = dialog.open();
		if (res == null) {
			return null;
		}
		String[] fileNames = dialog.getFileNames();
		String[] elems = new String[fileNames.length];
		IPath file = new Path(res);
		IPath apath = file.removeLastSegments(1);
		for (int i = 0; i < fileNames.length; i++) {
			elems[i] = apath.append(fileNames[i]).toString();
		}
		return elems;
	}

	public void modifyText(ModifyEvent e) {
		validate();
		updateButtons();
	}

	private void fireValidateEvent(
			final JAXRSLibraryValidationEvent JAXRSLibraryValidationEvent) {
		new Runnable() {
			public void run() {
				for (Iterator<JAXRSLibraryValidationListener> it = _listeners
						.iterator(); it.hasNext();) {
					JAXRSLibraryValidationListener listener = it.next();
					listener.notifyValidation(JAXRSLibraryValidationEvent);
				}
			}
		}.run();
	}

	public void widgetSelected(SelectionEvent e) {
		validate();
		updateButtons();
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// no handling for default selection
	}

	private void validate() {
		if (initing)
			return;
		validationMsg = null;
		// setPageComplete(true);
		if (!validateName() || !validateJars()) {
			// setPageComplete(false);
		}
		fireValidateEvent(new JAXRSLibraryValidationEvent(validationMsg));
	}

	private boolean validateJars() {
		if (workingCopyLibrary.getArchiveFiles().isEmpty()) {
			validationMsg = Messages.JAXRSLibraryWizard_ValidateNoJars;
			return false;
		}
		return true;
	}

	private boolean validateName() {
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { //$NON-NLS-1$
			validationMsg = Messages.JAXRSLibraryWizard_ValidateNoLibraryName;
			return false;
		}
		String aName = txtName.getText().trim();
		if (isNew() || (!isNew() && !getCurrentLibraryName().equals(aName))) {
			if (isNameInRegistry(JAXRSLibraryRegistryUtil.getInstance()
					.getJAXRSLibraryRegistry().getAllJAXRSLibraries(), aName)) {
				validationMsg = Messages.JAXRSLibraryWizard_ValidateExistingLibraryName;
				return false;
			}
		}
		return true;
	}

	private boolean isNew() {
		if (_isNew == -1) {
			_isNew = workingCopyLibrary.getName() == null ? 1 : 0;
		}
		return _isNew == 1;
	}

	private String getCurrentLibraryName() {
		return workingCopyLibrary.getName();
	}

	@SuppressWarnings("unchecked")
	private boolean isNameInRegistry(Collection c, String name) {
		Iterator it = c.iterator();
		while (it.hasNext()) {
			JAXRSLibrary lib = (JAXRSLibrary) it.next();
			if (lib.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the trimmed name of the user input for JAXRS Library name
	 */
	public String getJAXRSLibraryName() {
		return txtName.getText().trim();
	}

	/**
	 * @return the value of the user input for the isDeployed checkbox
	 */
	public boolean getIsDeployed() {
		return chkDeploy.getSelection();
	}

	/**
	 * @return the value of the user input for the isJAXRSImplementation
	 *         checkbox
	 */

}
