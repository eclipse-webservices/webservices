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
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.ui.internal.jaxrslibraryconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigModel;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfiglModelSource;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfiguratorUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.jst.ws.jaxrs.ui.internal.classpath.JAXRSLibraryWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelSynchHelper;

/**
 * A custom control used in wizard and property pages.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSLibraryConfigControl extends Composite {

	private JAXRSLibraryConfigModel workingCopyModel = null;

	private ComboViewer cvImplLib;
	private Composite includeLibRadiosComposite;
	private Button btnIncludeLibrariesGroup;
	private Button btnDeployJars;
	private Button btnSharedLibrary;
	
	private Combo comboImplLib;
	private Vector<IJAXRSImplLibraryCreationListener> newJAXRSLibCreatedListeners = new Vector<IJAXRSImplLibraryCreationListener>();
	private Set<JAXRSLibraryConfigControlChangeListener> _changeListeners;
	private boolean _initing;
	private IDataModel model;

	/**
	 * @param listener
	 */
	public void addOkClickedListener(IJAXRSImplLibraryCreationListener listener) {
		newJAXRSLibCreatedListeners.addElement(listener);
	}

	/**
	 * @param listener
	 */
	public void removeOkClickedListener(
			IJAXRSImplLibraryCreationListener listener) {
		newJAXRSLibCreatedListeners.removeElement(listener);
	}

	/**
	 * @param listener
	 */
	public void addChangeListener(
			JAXRSLibraryConfigControlChangeListener listener) {
		getChangeListeners().add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeChangeListener(
			JAXRSLibraryConfigControlChangeListener listener) {
		if (getChangeListeners().contains(listener))
			getChangeListeners().remove(listener);
	}

	private Set<JAXRSLibraryConfigControlChangeListener> getChangeListeners() {
		if (_changeListeners == null) {
			_changeListeners = new HashSet<JAXRSLibraryConfigControlChangeListener>();
		}
		return _changeListeners;
	}

	private void fireChangedEvent(final EventObject e) {
		if (_initing)
			return;
		SafeRunnable.run(new ISafeRunnable() {
			public void handleException(Throwable exception) {
				JAXRSUIPlugin.log(IStatus.ERROR, exception
						.getLocalizedMessage());
			}

			public void run() throws Exception {
				for (Iterator<JAXRSLibraryConfigControlChangeListener> it = getChangeListeners()
						.iterator(); it.hasNext();) {
					it.next().changed(
							new JAXRSLibraryConfigControlChangeEvent(e));
				}
			}
		});
	}

	/**
	 * Create the composite
	 * 
	 * @param parent
	 * @param style
	 */
	public JAXRSLibraryConfigControl(Composite parent, int style) {
		super(parent, style);
		final GridLayout gridLayoutImpls = new GridLayout(3, false);
		gridLayoutImpls.marginLeft = 0;
		gridLayoutImpls.marginTop = 0;
		gridLayoutImpls.marginBottom = 0;

		this.setLayout(gridLayoutImpls);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		this.setData(gd);
		_initing = true;
		createControls();
	}

	/**
	 * set control values from provided model.
	 * 
	 * @param source
	 */
	public void loadControlValuesFromModel(JAXRSLibraryConfiglModelSource source) {
		if (source != null) {
			// never read persistentModel = source;
			workingCopyModel = JAXRSLibraryConfigModel.JAXRSLibraryConfigModelFactory
					.createInstance(source);
			initializeControlValues();
			_initing = false;
		} else {
			JAXRSUIPlugin.log(IStatus.ERROR,
					Messages.JAXRSLibraryConfigControl_NullProject);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		super.dispose();
	}

	/**
	 * Return current selected JAXRS Implementation Library. Otherwise, return
	 * null.
	 * 
	 * @return JAXRSLibraryInternalReference
	 */
	public JAXRSLibraryInternalReference getSelectedJAXRSLibImplementation() {
		return workingCopyModel.getCurrentJAXRSImplementationLibrarySelection();
	}

	/**
	 * 
	 * @return JAXRSLibraryConfigModelAdapter
	 */
	public JAXRSLibraryConfigModel getWorkingModel() {
		return workingCopyModel;
	}

	private void initializeControlValues() {
		loadJAXRSImplList();

		btnDeployJars.setSelection(false);
		btnSharedLibrary.setSelection(false);
		btnIncludeLibrariesGroup.setSelection(false);
		JAXRSLibraryInternalReference savedImplLib = workingCopyModel
				.getSavedJAXRSImplementationLibrary();
		if (savedImplLib != null) {
			/*
			 * Get the input for the control to set selection.
			 */
			JAXRSLibraryInternalReference selected = JAXRSLibraryRegistryUtil
					.getInstance().getJAXRSLibraryReferencebyID(
							savedImplLib.getID());
			if (selected != null) {
				cvImplLib.setSelection(new StructuredSelection(selected), true);
			}
			updateIncludeLibrariesGroupState(selected);

		} else {
			JAXRSLibraryInternalReference dftJAXRSImplLib = JAXRSLibraryRegistryUtil
					.getInstance().getDefaultJAXRSImplementationLibrary();
			if (dftJAXRSImplLib != null) {
				cvImplLib.setSelection(
						new StructuredSelection(dftJAXRSImplLib), true);
			} 
			updateIncludeLibrariesGroupState(dftJAXRSImplLib);
		}

		redraw();
	}

	private void updateIncludeLibrariesGroupState(
			JAXRSLibraryInternalReference selected) {
		if (selected == null) {
			btnIncludeLibrariesGroup.setEnabled(false);
			setChildrenEnabled(includeLibRadiosComposite, false);

		} else {
			btnIncludeLibrariesGroup.setEnabled(true);
			btnIncludeLibrariesGroup.setSelection(selected
					.isCheckedToBeDeployed()
					|| (selected.isSharedLibSupported() && selected
							.isCheckedToBeSharedLibrary()));
			updateChildrenState(selected);
		}

	}

	private void updateChildrenState(JAXRSLibraryInternalReference selected) {
		btnDeployJars.setSelection(selected.isCheckedToBeDeployed()
				|| (btnIncludeLibrariesGroup.getSelection() && (!selected
						.isSharedLibSupported() || (selected
						.isSharedLibSupported() && !selected
						.isCheckedToBeSharedLibrary()))));
		// shared library has precedence
		btnSharedLibrary.setSelection(selected.isSharedLibSupported()
				&& selected.isCheckedToBeSharedLibrary());
		btnSharedLibrary.setEnabled(btnIncludeLibrariesGroup.getSelection() && selected.isSharedLibSupported());
		btnDeployJars.setEnabled(btnIncludeLibrariesGroup.getSelection());
		selected.setToBeDeployed(btnIncludeLibrariesGroup.getSelection() && btnDeployJars.getSelection());
		selected.setToBeSharedLibrary(btnIncludeLibrariesGroup.getSelection() && selected.isSharedLibSupported() && btnSharedLibrary.getSelection());
		
	}

	private void loadJAXRSImplList() {
		cvImplLib.setInput(workingCopyModel.getJAXRSImplementationLibraries());
	}

	private JAXRSLibraryInternalReference getCurrentSelectedJAXRSImplLib() {
		JAXRSLibraryInternalReference selJAXRSImpl = null;
		StructuredSelection objs = (StructuredSelection) cvImplLib
				.getSelection();
		if (objs != null) {
			if (objs.getFirstElement() instanceof JAXRSLibraryInternalReference) {
				selJAXRSImpl = (JAXRSLibraryInternalReference) objs
						.getFirstElement();
			}
		}
		return selJAXRSImpl;
	}

	private void createImplLibControls() {
		Composite cmpImpls = this;

		Label library = new Label(cmpImpls, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		library.setLayoutData(gd);
		library.setText(Messages.JAXRSLibraryConfigControl_Library);
		cvImplLib = new ComboViewer(cmpImpls, SWT.READ_ONLY);

		cvImplLib.setLabelProvider(new ImplLibCVListLabelProvider());
		cvImplLib.setContentProvider(new ImplLibCVContentProvider());
		comboImplLib = cvImplLib.getCombo();
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
		gd2.widthHint = 275;
		comboImplLib.setLayoutData(gd2);

		cvImplLib.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection ss = (StructuredSelection) event
						.getSelection();
				JAXRSLibraryInternalReference crtSelImplLib = (JAXRSLibraryInternalReference) ss
						.getFirstElement();
				updateIncludeLibrariesGroupState(crtSelImplLib);
//				if (crtSelImplLib != null) {
//					crtSelImplLib.setToBeDeployed(btnDeployJars.getSelection());
//					crtSelImplLib.setToBeSharedLibrary(crtSelImplLib.isSharedLibSupported() && btnSharedLibrary.getSelection() );
//				}
				workingCopyModel
						.setCurrentJAXRSImplementationLibrarySelection(crtSelImplLib);
				model.setProperty(
						IJAXRSFacetInstallDataModelProperties.IMPLEMENTATION,
						crtSelImplLib);
				fireChangedEvent(event);
			}
		});


		final Button btnNewImpl = new Button(cmpImpls, SWT.NONE); 
		GridData gd4 = new GridData(GridData.FILL_HORIZONTAL);
		btnNewImpl.setLayoutData(gd4);

		btnNewImpl
				.setText(Messages.JAXRSLibraryConfigControl_NewImplementationLibrary);
		btnNewImpl
				.setToolTipText(Messages.JAXRSLibraryConfigControl_NewImplButtonTooltip);
		btnNewImpl.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				JAXRSLibraryWizard wizard = new JAXRSLibraryWizard();
				IWorkbench wb = PlatformUI.getWorkbench();
				wizard.init(wb, null);
				WizardDialog dialog = new WizardDialog(wb
						.getActiveWorkbenchWindow().getShell(), wizard);
				int ret = dialog.open();
				if (ret == Window.OK) {
					JAXRSLibraryInternalReference lib = new JAXRSLibraryInternalReference(
							wizard.getJAXRSLibrary(), true, true, false);
					boolean sharedLibSupported = SharedLibraryConfiguratorUtil.isSharedLibSupportAvailable(lib.getID(), model
							.getStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME), SharedLibraryConfiguratorUtil.getWebProject(model), SharedLibraryConfiguratorUtil.getEARProject(model), SharedLibraryConfiguratorUtil.getAddToEar(model));

					lib.setSharedLibSupported(sharedLibSupported);
					JAXRSLibraryRegistryUtil.getInstance().addJAXRSLibrary(lib);
					workingCopyModel.getJAXRSImplementationLibraries().add(lib);
					workingCopyModel
							.setCurrentJAXRSImplementationLibrarySelection(lib);

					loadJAXRSImplList();
					updateIncludeLibrariesGroupState(lib);
					cvImplLib.setSelection(new StructuredSelection(lib), true);

				}
				// notify listeners that a JAXRS implementation is created.
				JAXRSImplLibraryCreationEvent event = new JAXRSImplLibraryCreationEvent(
						this, (ret == Window.OK));
				int size = newJAXRSLibCreatedListeners.size();
				for (int i = 0; i < size; i++) {
					IJAXRSImplLibraryCreationListener listener = newJAXRSLibCreatedListeners
							.elementAt(i);
					listener.okClicked(event);
				}
			}
		});
		
		btnIncludeLibrariesGroup = createCheckbox(cmpImpls,
				Messages.JAXRSLibraryConfigControl_IncludeGroupLabel,
				null, null);
		((GridData)this.getData()).horizontalSpan = 3;
		GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
		gd3.verticalIndent =15;
		gd3.horizontalSpan = 3;


		btnIncludeLibrariesGroup.setLayoutData(gd3);
		includeLibRadiosComposite  = new Composite( cmpImpls, SWT.NONE );
	    
	    GridLayout gridlayout   = new GridLayout();
	    gridlayout.numColumns   = 1;
	    gridlayout.marginLeft = 25;
	    
	    includeLibRadiosComposite.setLayout( gridlayout );
	    GridData griddata = new GridData(GridData.FILL_HORIZONTAL);
	    includeLibRadiosComposite.setLayoutData( griddata );
	    
	    btnDeployJars = createRadioButton(includeLibRadiosComposite, Messages.JAXRSLibraryConfigControl_DeployButtonLabel, Messages.JAXRSLibraryConfigControl_DeployJAR , null);
		btnDeployJars.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!_initing) {
					JAXRSLibraryInternalReference jaxrslib = getCurrentSelectedJAXRSImplLib();
					if (jaxrslib != null)
						jaxrslib.setToBeDeployed(btnDeployJars.getSelection());
					workingCopyModel
							.setCurrentJAXRSImplementationLibrarySelection(jaxrslib);
					model.setProperty(IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
					btnDeployJars.getSelection());
					fireChangedEvent(e);
				}
			}
		});

		
		btnSharedLibrary = createRadioButton(includeLibRadiosComposite, Messages.JAXRSLibraryConfigControl_SharedLibButtonLabel, Messages.JAXRSLibraryConfigControl_TooltipIncludeAsSharedLib, null);

		btnSharedLibrary.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!_initing) {
 					JAXRSLibraryInternalReference jaxrslib = getCurrentSelectedJAXRSImplLib();
					if (jaxrslib != null) {
						jaxrslib.setToBeSharedLibrary(btnSharedLibrary.getSelection());
					}
					workingCopyModel
					.setCurrentJAXRSImplementationLibrarySelection(jaxrslib);
         			model.setProperty(IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY, btnSharedLibrary.getSelection());
         			fireChangedEvent(e);
				}
			}
		});
		
		
		
		
		btnIncludeLibrariesGroup.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!_initing) {
					JAXRSLibraryInternalReference crtSelImplLib = (JAXRSLibraryInternalReference) ((StructuredSelection) cvImplLib
							.getSelection()).getFirstElement();

					if (btnIncludeLibrariesGroup.getSelection()) {
						updateChildrenState(crtSelImplLib);
					} else {
						setChildrenEnabled(includeLibRadiosComposite, false);
						crtSelImplLib.setToBeDeployed(false);
						crtSelImplLib.setToBeSharedLibrary(false);
					}
				}
			}
		});
	}

	private void createControls() {
		setRedraw(true);
		createImplLibControls();

	}

	/**
	 * Configure the JAXRSLibraryConfigControl elements to used the containers
	 * synchHelper
	 * 
	 * @param synchHelper
	 */
	public void setSynchHelper(DataModelSynchHelper synchHelper) {
		model = synchHelper.getDataModel();
		synchHelper.synchCombo(cvImplLib.getCombo(),
				IJAXRSFacetInstallDataModelProperties.IMPLEMENTATION_LIBRARIES,
				null);
		synchHelper.synchCheckbox(btnDeployJars,
				IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
				null);
		synchHelper.synchCheckbox(btnSharedLibrary,
				IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
				null);
		// not using synch helper for IMPLEMENTATION_TYPE
		// synchHelper.synchCheckBoxTableViewer(ctvSelCompLib,
		// IJAXRSFacetInstallDataModelProperties.COMPONENT_LIBRARIES, new
		// Control[]{hiddenList});
	}

	private static class ImplLibCVContentProvider implements
			IStructuredContentProvider {
		@SuppressWarnings("unchecked")
		private List jaxrsImplLibs = new ArrayList(0);

		public Object[] getElements(Object inputElement) {
			return jaxrsImplLibs.toArray();
		}

		public void dispose() {
			// do nothing
		}

		@SuppressWarnings("unchecked")
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput == null) {
				jaxrsImplLibs = Collections.EMPTY_LIST;
			} else {
				jaxrsImplLibs = (List) newInput;
			}
		}
	}

	private static class ImplLibCVListLabelProvider extends LabelProvider {
		private JAXRSLibrary defaultImpl = null;

		public String getText(Object element) {
			if (element instanceof JAXRSLibraryInternalReference) {
				StringBuffer labelBuf = new StringBuffer(
						((JAXRSLibraryInternalReference) element).getLabel());
				JAXRSLibrary lib = ((JAXRSLibraryInternalReference) element)
						.getLibrary();
				if (getDefaultImpl() != null && lib != null
						&& lib.getID().equals(getDefaultImpl().getID()))
					labelBuf
							.append(" ").append(JAXRSLibraryRegistry.DEFAULT_IMPL_LABEL); //$NON-NLS-1$
				return labelBuf.toString();
			}
			return null;
		}

		private JAXRSLibrary getDefaultImpl() {
			if (defaultImpl == null) {
				JAXRSLibraryRegistry jaxrslibreg = JAXRSLibraryRegistryUtil
						.getInstance().getJAXRSLibraryRegistry();
				defaultImpl = jaxrslibreg.getDefaultImplementation();
			}
			return defaultImpl;
		}

		public Image getImage(Object element) {
			return null;
		}
	}

	private Button createRadioButton(Composite parent, String labelName,
			String tooltip, String infopop) {
		return createButton(SWT.RADIO, parent, labelName, tooltip, infopop);
	}

	private Button createButton(int kind, Composite parent, String labelName,
			String tooltip, String infopop) {
		Button button = new Button(parent, kind);

		tooltip = tooltip == null ? labelName : tooltip;
		button.setText(labelName);
		button.setToolTipText(tooltip);

		if (infopop != null)
			PlatformUI.getWorkbench().getHelpSystem().setHelp(button,
					JAXRSUIPlugin.PLUGIN_ID + "." + infopop);

		return button;
	}

	private Button createCheckbox( Composite parent, String labelName, String tooltip, String infopop )
	  {
	    return createButton( SWT.CHECK, parent, labelName, tooltip, infopop );
	  }


	public boolean getIncludeLibs() {
		if (btnIncludeLibrariesGroup != null) {
			return btnIncludeLibrariesGroup.getSelection();
		}
		return false;
	}
	
	public static void setChildrenEnabled(Composite parentComposite, boolean enabled) {
		Control[] wsdlControls = parentComposite.getChildren();
		for (int i=0; i<wsdlControls.length; i++) {
			wsdlControls[i].setEnabled(enabled);
		}
	}


}
