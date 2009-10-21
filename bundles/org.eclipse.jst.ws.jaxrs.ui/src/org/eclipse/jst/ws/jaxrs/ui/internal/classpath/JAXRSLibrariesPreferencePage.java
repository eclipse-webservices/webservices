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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * Provides a preference page for JAXRS Libraries.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSLibrariesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private IWorkbench wb;

	private TreeViewer tv;
	private TreeViewerAdapter tvAdapter;
	private TreeLabelProvider tvLabelProvider;

	private Composite btnComp;

	private Button btnNew;
	private Button btnEdit;
	private Button btnDelete;
	private Button btnMakeDefaultImpl;

	protected Control createContents(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout(2, false));
		c.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblLibs = new Label(c, SWT.NONE);
		lblLibs
				.setText(Messages.JAXRSLibrariesPreferencePage_DefinedJAXRSLibraries);
		GridData gd1 = new GridData();
		gd1.horizontalSpan = 2;
		lblLibs.setLayoutData(gd1);

		tv = new TreeViewer(c, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		tvAdapter = new TreeViewerAdapter();
		tvLabelProvider = new TreeLabelProvider();
		tv.setContentProvider(tvAdapter);
		tv.setLabelProvider(tvLabelProvider);
		tv.addSelectionChangedListener(tvAdapter);
		tv.addDoubleClickListener(tvAdapter);
		tv.setComparator(tvAdapter);
		tv.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		tv.setInput(getJAXRSLibraries());

		createButtons(c);
		return c;
	}

	private void createButtons(Composite c) {
		btnComp = new Composite(c, SWT.NONE);
		GridLayout gl1 = new GridLayout(1, false);
		gl1.marginHeight = 0;
		gl1.marginWidth = 0;
		btnComp.setLayout(gl1);
		btnComp.setLayoutData(new GridData(GridData.END
				| GridData.VERTICAL_ALIGN_FILL));

		btnNew = new Button(btnComp, SWT.NONE);
		btnNew.setText(Messages.JAXRSLibrariesPreferencePage_New);
		btnNew.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openJAXRSLibraryEditDialog(null);
			}
		});

		btnEdit = new Button(btnComp, SWT.NONE);
		btnEdit.setText(Messages.JAXRSLibrariesPreferencePage_Edit);
		btnEdit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnEdit.setEnabled(false);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] element = tv.getTree().getSelection();
				if (element != null) {
					openJAXRSLibraryEditDialog(element[0]);
				}

			}
		});

		btnDelete = new Button(btnComp, SWT.NONE);
		btnDelete.setText(Messages.JAXRSLibrariesPreferencePage_Remove);
		btnDelete.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		btnDelete.setEnabled(false);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				boolean modified = false;
				if (tv.getSelection() instanceof StructuredSelection) {
					StructuredSelection objs = (StructuredSelection) tv
							.getSelection();
					if (objs != null) {
						Iterator it = objs.iterator();
						while (it.hasNext()) {
							JAXRSLibrary lib = (JAXRSLibrary) it.next();
							if (lib instanceof PluginProvidedJAXRSLibrary)
								MessageDialog
										.openInformation(
												getShell(),
												Messages.JAXRSLibrariesPreferencePage_CannotRemovePluginProvidedTitle,
												Messages.JAXRSLibrariesPreferencePage_CannotRemovePluginProvidedMessage);

							else {
								JAXRSLibraryRegistryUtil.getInstance()
										.getJAXRSLibraryRegistry()
										.removeJAXRSLibrary(lib);
								modified = true;
							}
						}
						if (modified) {
							JAXRSLibraryRegistryUtil.getInstance()
									.saveJAXRSLibraryRegistry();
							tv.refresh();
						}
					}
				}
			}
		});

		btnMakeDefaultImpl = new Button(btnComp, SWT.NONE);
		btnMakeDefaultImpl.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_END));
		btnMakeDefaultImpl
				.setText(Messages.JAXRSLibrariesPreferencePage_MakeDefault);
		btnMakeDefaultImpl.setVisible(false);
		btnMakeDefaultImpl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tv.getSelection() instanceof StructuredSelection) {
					StructuredSelection objs = (StructuredSelection) tv
							.getSelection();
					if (objs != null) {
						if (objs.getFirstElement() instanceof JAXRSLibrary) {
							JAXRSLibrary lib = (JAXRSLibrary) objs
									.getFirstElement();
							JAXRSLibraryRegistryUtil.getInstance()
									.getJAXRSLibraryRegistry()
									.setDefaultImplementation(lib);
						}
						JAXRSLibraryRegistryUtil.getInstance()
								.saveJAXRSLibraryRegistry();
						tv.refresh();
					}
				}
			}
		});

	}

	private Object getJAXRSLibraries() {
		return JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryRegistry()
				.getAllJAXRSLibraries();
	}

	public void init(IWorkbench workbench) {
		wb = workbench;
		setDescription(Messages.JAXRSLibrariesPreferencePage_Description);
		noDefaultAndApplyButton();
	}

	/**
	 * Getter created only for JUnit tests. Should not be used otherwise.
	 * 
	 * @return the TreeViewer of JAXRS Libraries
	 */
	public Viewer getLibraryViewer() {
		return tv;
	}

	private class TreeViewerAdapter extends ViewerComparator implements
			ITreeContentProvider, ISelectionChangedListener,
			IDoubleClickListener {
		private final Object[] NO_ELEMENTS = new Object[0];

		// ------- ITreeContentProvider Interface ------------

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// will never happen
		}

		public void dispose() {
			// do nothing
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object obj) {
			return ((List) getJAXRSLibraries()).toArray();
		}

		public Object[] getChildren(Object element) {
			if (element instanceof JAXRSLibrary) {
				return ((JAXRSLibrary) element).getArchiveFiles().toArray();
			}
			return NO_ELEMENTS;
		}

		public Object getParent(Object element) {
			// if (elements instanceof JAXRSLibrary) {
			// return tvAdapter.getParent(tv.getTree().class, element);
			// }
			return null;// fParentElement;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof JAXRSLibrary) {
				return true;
			}
			return false;
		}

		// ------- ISelectionChangedListener Interface ------------

		public void selectionChanged(SelectionChangedEvent event) {
			doListSelected(event);
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
			if (e1 instanceof JAXRSLibrary && e2 instanceof JAXRSLibrary) {
				JAXRSLibrary lib1 = (JAXRSLibrary) e1;
				JAXRSLibrary lib2 = (JAXRSLibrary) e2;

				return getComparator()
						.compare(lib1.getLabel(), lib2.getLabel());
			}
			return super.compare(viewer, e1, e2);
		}

	}

	/**
	 * Respond to a list selection event
	 * 
	 * @param event
	 */
	protected void doListSelected(SelectionChangedEvent event) {
		updateButtonState();
	}

	/**
	 * Respond to a double click event by opening the edit dialog
	 * 
	 * @param event
	 */
	protected void doDoubleClick(DoubleClickEvent event) {
		openJAXRSLibraryEditDialog(tv.getTree().getSelection()[0]);
	}

	private void updateButtonState() {
		btnEdit.setEnabled(tv.getTree().getSelectionCount() == 1);
		if (tv.getTree().getSelectionCount() == 1
				&& tv.getTree().getSelection()[0].getData() instanceof JAXRSLibrary) {
			btnDelete.setEnabled(true);
			btnMakeDefaultImpl.setVisible(false);
			btnMakeDefaultImpl.setVisible(true);
		} else {
			btnDelete.setEnabled(false);
			btnMakeDefaultImpl.setVisible(false);
		}
	}

	private void openJAXRSLibraryEditDialog(Object element) {
		if (isPluginProvidedJAXRSLibrary(element)) {
			MessageDialog
					.openInformation(
							getShell(),
							Messages.JAXRSLibrariesPreferencePage_CannotModifyPluginProvidedTitle,
							Messages.JAXRSLibrariesPreferencePage_CannotModifyPluginProvidedMessage);
			return;
		}
		IWorkbenchWizard wizard = new JAXRSLibraryWizard();
		wizard.init(wb, getStructuredElement(element));
		WizardDialog dialog = new WizardDialog(wb.getActiveWorkbenchWindow()
				.getShell(), wizard);
		int ret = dialog.open();
		if (ret == Window.OK) {
			tv.refresh();
		}
	}

	private IStructuredSelection getStructuredElement(Object element) {
		if (element instanceof TreeItem) {
			Object item = ((TreeItem) element).getData();
			if (item instanceof ArchiveFile) {
				JAXRSLibrary parent = ((ArchiveFile) item).getJAXRSLibrary();
				return new StructuredSelection(parent);
			} else if (item instanceof JAXRSLibrary) {
				return new StructuredSelection(item);
			}
		}
		return null;
	}

	private boolean isPluginProvidedJAXRSLibrary(Object treeElement) {
		if (treeElement instanceof TreeItem) {
			Object item = ((TreeItem) treeElement).getData();
			if (item instanceof PluginProvidedJAXRSLibrary) {
				return true;
			} else if (item instanceof ArchiveFile) {
				return (((ArchiveFile) item).getJAXRSLibrary() instanceof PluginProvidedJAXRSLibrary);
			}
		}
		return false;
	}

	private static class TreeLabelProvider implements ILabelProvider {
		private final Image libImg;
		private final Image jarImg;

		TreeLabelProvider() {
			ImageDescriptor jarImgDesc = JAXRSUIPlugin
					.getImageDescriptor("obj16/jar_obj.gif"); //$NON-NLS-1$
			jarImg = jarImgDesc.createImage();
			ImageDescriptor libImgDesc = JAXRSUIPlugin
					.getImageDescriptor("obj16/library_obj.gif"); //$NON-NLS-1$
			libImg = libImgDesc.createImage();
		}

		public Image getImage(Object element) {
			if (element instanceof JAXRSLibrary) {
				return libImg;
			}
			return jarImg;
		}

		public String getText(Object element) {
			StringBuffer labelBuf = new StringBuffer();
			if (element instanceof JAXRSLibrary) {
				JAXRSLibrary lib = (JAXRSLibrary) element;
				labelBuf.append(lib.getLabel());
				if (lib == JAXRSLibraryRegistryUtil.getInstance()
						.getJAXRSLibraryRegistry().getDefaultImplementation()) {
					labelBuf
							.append(Messages.JAXRSLibrariesPreferencePage_DEFAULT_IMPL_DESC);
				}

			}
			if (element instanceof ArchiveFile) {
				ArchiveFile jar = (ArchiveFile) element;
				labelBuf.append(jar.getName());
				if (!jar.exists())
					labelBuf
							.append(Messages.JAXRSLibrariesPreferencePage_MISSING_DESC);
				labelBuf
						.append(" - ").append(((ArchiveFile) element).getSourceLocation()); //$NON-NLS-1$
			}
			return labelBuf.toString();
		}

		public void addListener(ILabelProviderListener listener) {
			// no listeners supported
		}

		public void dispose() {
			if (libImg != null) {
				libImg.dispose();
			}
			if (jarImg != null) {
				jarImg.dispose();
			}
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
			// no listeners supported
		}
	}

}
