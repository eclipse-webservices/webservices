/*******************************************************************************
 * Copyright (c) 2010, 2013 IBM Corporation and others.
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
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100319   306595 ericdp@ca.ibm.com - Eric D. Peters, several install scenarios fail for both user library & non-user library
 * 20100324   306937 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Properties page- NPE after pressing OK
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 * 20100428   310905 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet fails to install due to NPE or runtime exception due to duplicate cp entries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;


import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.IPropertyChangeListener;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.ui.libprov.user.UserLibraryProviderInstallPanel;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigDialogSettingData;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigModel;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfiglModelSource;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfiguratorUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUserLibraryProviderInstallOperationConfig;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.common.frameworks.datamodel.DataModelEvent;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelListener;

public class JAXRSUserLibraryProviderInstallPanel extends UserLibraryProviderInstallPanel
{
  private JAXRSFacetIncludeLibrariesGroup includeLibsGroup;
  private JAXRSLibraryConfigModel workingCopyModel = null;
  private IDataModel model;
  private JAXRSUserLibraryProviderInstallOperationConfig cfg;;
  private ServletInformationGroup servletInfoGroup;
  private boolean sharedLibSupported;
  IDataModelListener listener;
  private ILibraryProvider currentlySelectedLibraryType;
  public JAXRSUserLibraryProviderInstallPanel()
  {
    super();
  }

  protected Control createControlNextToManageHyperlink(final Composite composite)
  {
    cfg = (JAXRSUserLibraryProviderInstallOperationConfig) getOperationConfig();
    model = cfg.getModel();


    Composite mainComp = new Composite(composite, SWT.NONE);
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    gl.marginRight = 0;
    gl.marginLeft = 0;
    mainComp.setLayout(gl);
    if (!onPropertiesPage()) {
      	String libraryID = ((LibraryInstallDelegate)model.getProperty(IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE)).getLibraryProvider().getId();
      	//listen for all changes in the model- if user changes addToEAR or library type we might need to
      	//update the Deploy / Shared Library options
		listener = new IDataModelListener() {
		
			public void propertyChanged(DataModelEvent arg0) {
				DataModelEvent event = arg0;
				if (event == null || event.getPropertyName() == null || (!event.getPropertyName().equals(IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE) && !event.getPropertyName().equals(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR) ))
					return;
						LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) model
								.getProperty(IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE);
						ILibraryProvider thisProvider = librariesInstallDelegate
						.getLibraryProvider();
						if (currentlySelectedLibraryType == null)
							currentlySelectedLibraryType = thisProvider;
						else {
							//we are sometimes notified when the user has not actually changed the selected library type
							if (currentlySelectedLibraryType != thisProvider) {
								//only update servlet class name & update DD state when library 
								//type has changed
								currentlySelectedLibraryType = thisProvider;
							} else if (!event.getPropertyName().equals(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR)) {
								return;
							}
						}
						String libraryProviderID = librariesInstallDelegate
								.getLibraryProvider().getId();
				      	sharedLibSupported = SharedLibraryConfiguratorUtil
						.isSharedLibSupportAvailable(
								libraryProviderID,
								model.getStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME),
								SharedLibraryConfiguratorUtil
										.getWebProject(model),
								SharedLibraryConfiguratorUtil
										.getEARProject(model),
								SharedLibraryConfiguratorUtil
										.getAddToEar(model));
						try {
							model.removeListener(listener);
							initializeControlValues();
							model.addListener(listener);
						} catch (Exception e) {
							//TODO exception as we are notified in non-UI thread and Invalid thread access exception,
							//should find another way to get notified when data model changes

						}

					}
		
		};
		model.addListener(listener);
      	sharedLibSupported = SharedLibraryConfiguratorUtil
    						.isSharedLibSupportAvailable(
    								libraryID,
    								model.getStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME),
    								SharedLibraryConfiguratorUtil
    										.getWebProject(model),
    								SharedLibraryConfiguratorUtil
    										.getEARProject(model),
    								SharedLibraryConfiguratorUtil
    										.getAddToEar(model));


    	//we are in facet install mode
    	includeLibsGroup = new JAXRSFacetIncludeLibrariesGroup(mainComp, SWT.NONE);
    	includeLibsGroup.getCopyOnPublishCheckBox().setSelection(cfg.isIncludeWithApplicationEnabled());
    
    


		includeLibsGroup.getCopyOnPublishCheckBox().addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(final SelectionEvent event) {
						cfg.setIncludeWithApplicationEnabled(includeLibsGroup
								.getCopyOnPublishCheckBox().getSelection());
						boolean deployLib = false;
						boolean shareLib = false;
						if (includeLibsGroup.getCopyOnPublishCheckBox()
								.getSelection()) {
							deployLib = includeLibsGroup
									.getBtnDeployJars().getSelection();
							shareLib = includeLibsGroup
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
							updateIncludeLibrariesGroupState(shareLib, deployLib, sharedLibSupported);
						} else {
							cfg.setIsDeploy(deployLib);
							cfg.setSharedLibrary(shareLib);
							IDataModel model = cfg.getModel();
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
									deployLib);
							model.setProperty(
									IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
									shareLib);
							setChildrenEnabled(includeLibsGroup.getIncludeLibRadiosComposite(), false);

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
    }

    initialize();
    return mainComp;
  }

	private boolean onPropertiesPage() {
		return cfg.getModel() == null;
	}


  private void initialize()
  {
    // if shared lib not supported but shared lib setting was true, assume they
    // still want to include libraries
	JAXRSLibraryConfiglModelSource source;
	if (!onPropertiesPage()) 
		source = new JAXRSLibraryConfigDialogSettingData(includeLibsGroup.getBtnDeployJars().getSelection(), includeLibsGroup.getBtnSharedLibrary().getSelection(), true);
	else
		source = new JAXRSLibraryConfigDialogSettingData(true, false, false);
    if (source != null)
    {
      // never read persistentModel = source;
      workingCopyModel = JAXRSLibraryConfigModel.JAXRSLibraryConfigModelFactory.createInstance(source);
      initializeControlValues();
    }
  }

  private void initializeControlValues()
  {
// btnDeployJars.setSelection(false);
// btnSharedLibrary.setSelection(false);
// copyOnPublishCheckBox.setSelection(false);
	  
    if (!onPropertiesPage())
    {
  	  boolean bSharedLib = (Boolean) model
		.getDefaultProperty(IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY);
  	  boolean bDeploy = (Boolean) model
		.getDefaultProperty(IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION);
				if (sharedLibSupported) {
					bSharedLib = SharedLibraryConfiguratorUtil
							.isSharedLibSelectedByDefault(model
									.getStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME));
					if (!bSharedLib)
						bDeploy = true;
					else
						// don't want both to be true, shared lib takes
						// precedence
						bDeploy = false;

				}
				// set the properties on the model
				model.setBooleanProperty(
						IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION,
						bDeploy);
				// set the properties on the configuration object as well
				cfg.setIsDeploy(bDeploy);				
				if (sharedLibSupported) {
					cfg.setSharedLibrary(bSharedLib);
					model.setBooleanProperty(
							IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY,
							bSharedLib);
				}

				updateIncludeLibrariesGroupState(bSharedLib, bDeploy, sharedLibSupported);

			}

// redraw();
  }

	private void updateIncludeLibrariesGroupState(boolean bSharedLib, boolean bDeploy, boolean sharedLibSupported) {
			includeLibsGroup.getCopyOnPublishCheckBox().setEnabled(true);
			includeLibsGroup.getCopyOnPublishCheckBox().setSelection(bDeploy
					|| (sharedLibSupported &&bSharedLib));
			updateChildrenState(bSharedLib, bDeploy, sharedLibSupported);

	}

	private void updateChildrenState(boolean bSharedLib, boolean bDeploy, boolean sharedLibSupported) {
		includeLibsGroup.getBtnDeployJars().setSelection(bDeploy
				|| (includeLibsGroup.getCopyOnPublishCheckBox().getSelection() && (!sharedLibSupported || (sharedLibSupported && !bSharedLib))));
		// shared library has precedence
		includeLibsGroup.getBtnSharedLibrary().setSelection(sharedLibSupported
				&& bSharedLib);
		includeLibsGroup.getBtnSharedLibrary().setEnabled(includeLibsGroup.getCopyOnPublishCheckBox().getSelection() && sharedLibSupported);
		includeLibsGroup.getBtnDeployJars().setEnabled(includeLibsGroup.getCopyOnPublishCheckBox().getSelection());
		
	}
	public static void setChildrenEnabled(Composite parentComposite, boolean enabled) {
		Control[] wsdlControls = parentComposite.getChildren();
		for (int i=0; i<wsdlControls.length; i++) {
			wsdlControls[i].setEnabled(enabled);
		}
	}


}
