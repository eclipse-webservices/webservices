/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100319   306595 ericdp@ca.ibm.com - Eric D. Peters, several install scenarios fail for both user library & non-user library
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.common.project.facet.core.libprov.IPropertyChangeListener;
import org.eclipse.jst.common.project.facet.ui.libprov.user.UserLibraryProviderInstallPanel;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigDialogSettingData;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigModel;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfiglModelSource;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUserLibraryProviderInstallOperationConfig;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class JAXRSUserLibraryProviderInstallPanel extends UserLibraryProviderInstallPanel
{
  private JAXRSFacetIncludeLibrariesGroup includeLibsGroup;
  private JAXRSLibraryConfigModel workingCopyModel = null;
  private IDataModel model;

  private ServletInformationGroup servletInfoGroup;

  public JAXRSUserLibraryProviderInstallPanel()
  {
    super();
  }

  protected Control createControlNextToManageHyperlink(final Composite composite)
  {
    final JAXRSUserLibraryProviderInstallOperationConfig cfg = (JAXRSUserLibraryProviderInstallOperationConfig) getOperationConfig();

    Composite mainComp = new Composite(composite, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    gl.marginRight = 0;
    gl.marginLeft = 0;
    mainComp.setLayout(gl);
    includeLibsGroup = new JAXRSFacetIncludeLibrariesGroup(mainComp, SWT.NONE);
    includeLibsGroup.getCopyOnPublishCheckBox().setSelection(cfg.isIncludeWithApplicationEnabled());


		includeLibsGroup.getCopyOnPublishCheckBox().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent event) {
						cfg.setIncludeWithApplicationEnabled(includeLibsGroup
								.getCopyOnPublishCheckBox().getSelection());
						if (includeLibsGroup.getCopyOnPublishCheckBox()
								.getSelection()) {
							boolean deployLib = includeLibsGroup
									.getBtnDeployJars().getSelection();
							boolean shareLib = includeLibsGroup
									.getBtnSharedLibrary().getSelection();
							cfg.setIsDeploy(deployLib);
							cfg.setSharedLibrary(shareLib);
							IDataModel model = cfg.getModel();
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
									deployLib);
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
									shareLib);
						} else {
							boolean deployLib = false;
							boolean shareLib = false;
							cfg.setIsDeploy(deployLib);
							cfg.setSharedLibrary(shareLib);
							IDataModel model = cfg.getModel();
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
									deployLib);
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
									shareLib);
						}

					}
				});

		includeLibsGroup.getBtnDeployJars().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						boolean deployLib = includeLibsGroup.getBtnDeployJars()
								.getSelection();
						boolean shareLib = includeLibsGroup
								.getBtnSharedLibrary().getSelection();
						cfg.setIsDeploy(deployLib);
						cfg.setSharedLibrary(shareLib);
						IDataModel model = cfg.getModel();
						model.setProperty(
								IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
								deployLib);
						model.setProperty(
								IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
								shareLib);
					}
				});

		includeLibsGroup.getBtnSharedLibrary().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						boolean deployLib = includeLibsGroup.getBtnDeployJars()
								.getSelection();
						boolean shareLib = includeLibsGroup
								.getBtnSharedLibrary().getSelection();
						cfg.setIsDeploy(deployLib);
						cfg.setSharedLibrary(shareLib);
						IDataModel model = cfg.getModel();
						model.setProperty(
								IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
								deployLib);
						model.setProperty(
								IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
								shareLib);
					}
				});

    final IPropertyChangeListener listener = new IPropertyChangeListener()
    {
      public void propertyChanged(final String property, final Object oldValue, final Object newValue)
      {
        if (oldValue != newValue)
        {
//          copyOnPublishCheckBox.setSelection(cfg.isIncludeWithApplicationEnabled());
        }
      }
    };

    cfg.addListener(listener, JAXRSUserLibraryProviderInstallOperationConfig.PROP_INCLUDE_WITH_APPLICATION_ENABLED);

    includeLibsGroup.getCopyOnPublishCheckBox().addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(final DisposeEvent event)
      {
        cfg.removeListener(listener);
      }
    });

    setDownloadCommandEnabled(false);
    initialize();
    return mainComp;
  }


  private void initialize()
  {
    // if shared lib not supported but shared lib setting was true, assume they
    // still want to include libraries
    JAXRSLibraryConfiglModelSource source = new JAXRSLibraryConfigDialogSettingData(includeLibsGroup.getBtnDeployJars().getSelection(), includeLibsGroup.getBtnSharedLibrary().getSelection(), true);
    if (source != null)
    {
      // never read persistentModel = source;
      workingCopyModel = JAXRSLibraryConfigModel.JAXRSLibraryConfigModelFactory.createInstance(source);
      initializeControlValues();
    }
    else
    {
      JAXRSUIPlugin.log(IStatus.ERROR, "Error null project"); // Messages.JAXRSLibraryConfigControl_NullProject);
    }
  }

  private void initializeControlValues()
  {
// btnDeployJars.setSelection(false);
// btnSharedLibrary.setSelection(false);
// copyOnPublishCheckBox.setSelection(false);
    JAXRSLibraryInternalReference savedImplLib = workingCopyModel.getSavedJAXRSImplementationLibrary();
    if (savedImplLib != null)
    {
      /*
       * Get the input for the control to set selection.
       */
      JAXRSLibraryInternalReference selected = JAXRSLibraryRegistryUtil.getInstance().getJAXRSLibraryReferencebyID(savedImplLib.getID());
      updateIncludeLibrariesGroupState(selected);

    }
    else
    {
      JAXRSLibraryInternalReference dftJAXRSImplLib = JAXRSLibraryRegistryUtil.getInstance().getDefaultJAXRSImplementationLibrary();
      updateIncludeLibrariesGroupState(dftJAXRSImplLib);
    }

// redraw();
  }

  private void updateIncludeLibrariesGroupState(JAXRSLibraryInternalReference selected)
  {
    if (selected == null)
    {
// copyOnPublishCheckBox.setEnabled(false);
// btnDeployJars.setEnabled(false);
// btnSharedLibrary.setEnabled(false);

    }
    else
    {
// copyOnPublishCheckBox.setEnabled(true);
// copyOnPublishCheckBox.setSelection(selected
// .isCheckedToBeDeployed()
// || (selected.isSharedLibSupported() && selected
// .isCheckedToBeSharedLibrary()));
// updateChildrenState(selected);
    }

  }

  private void updateChildrenState(JAXRSLibraryInternalReference selected)
  {
//    btnDeployJars.setSelection(selected.isCheckedToBeDeployed() || (copyOnPublishCheckBox.getSelection() && (!selected.isSharedLibSupported() || (selected.isSharedLibSupported() && !selected.isCheckedToBeSharedLibrary()))));
//    // shared library has precedence
//    btnSharedLibrary.setSelection(selected.isSharedLibSupported() && selected.isCheckedToBeSharedLibrary());
//    btnSharedLibrary.setEnabled(copyOnPublishCheckBox.getSelection() && selected.isSharedLibSupported());
//    btnDeployJars.setEnabled(copyOnPublishCheckBox.getSelection());
//    selected.setToBeDeployed(copyOnPublishCheckBox.getSelection() && btnDeployJars.getSelection());
//    selected.setToBeSharedLibrary(copyOnPublishCheckBox.getSelection() && selected.isSharedLibSupported() && btnSharedLibrary.getSelection());

  }

}
