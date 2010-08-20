/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100302   304405 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Facet : support JAX-RS 1.1 (JSR 311)
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100310   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100324   306937 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Properties page- NPE after pressing OK
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100428   310905 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet fails to install due to NPE or runtime exception due to duplicate cp entries
 * 20100519   313576 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS tools- validation problems
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20100820   323192 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS- Not prompted to enter servlet class when adding JAX-RS facet to a new web project
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.common.project.facet.core.libprov.ILibraryProvider;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryProviderOperationConfig;
import org.eclipse.jst.common.project.facet.core.libprov.internal.LibraryProvider;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryProviderFrameworkUi;
import org.eclipse.jst.j2ee.project.EarUtilities;
import org.eclipse.jst.j2ee.project.WebUtilities;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig.JAXRSLibraryProviderUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfiguratorUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSSharedLibraryProviderInstallOperationConfig;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUserLibraryProviderInstallOperationConfig;
import org.eclipse.jst.ws.jaxrs.ui.internal.IJAXRSUIConstants;
import org.eclipse.jst.ws.jaxrs.ui.internal.JAXRSUIPlugin;
import org.eclipse.jst.ws.jaxrs.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelEvent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IPreset;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.internal.BridgedRuntime;
import org.eclipse.wst.common.project.facet.ui.IFacetWizardPage;
import org.eclipse.wst.common.project.facet.ui.IWizardContext;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;

/**
 * JAXRS Facet installation wizard page.
 * 
 */
@SuppressWarnings("restriction")
public class JAXRSFacetInstallPage extends DataModelWizardPage implements IJAXRSFacetInstallDataModelProperties, IFacetWizardPage
{
  // UI
  private ServletInformationGroup servletInfoGroup;

  private IDialogSettings dialogSettings;
  private IDataModel webAppDataModel;
  private String sEARProject = null;
  private String sWEBProject = null;
  private IWizardContext context;
  private String sTargetRuntime = null;
  private IRuntime targetRuntime = null;
  private boolean bAddToEAR = false;
  private static final String SETTINGS_SERVLET = "servletName"; //$NON-NLS-1$
  private static final String SETTINGS_SERVLET_CLASSNAME = "servletClassname"; //$NON-NLS-1$
  private static final String SETTINGS_URL_MAPPINGS = "urlMappings"; //$NON-NLS-1$
  private static final String SETTINGS_URL_PATTERN = "pattern"; //$NON-NLS-1$
  private Button updateDDCheckBox;
  private Composite composite = null;
  private java.util.List<IProject> earProjects = null;
  private IPreset selectedPreset = null;
  private ILibraryProvider currentlySelectedLibraryType;
  private boolean isProjectCreationMode = true; // project creation = true,
                                                // add/remove facets mode =
                                                // false

  /**
   * Zero argument constructor
   */
  public JAXRSFacetInstallPage()
  {
    super(DataModelFactory.createDataModel(new AbstractDataModelProvider()
    {/*
      * do nothing
      */
    }), "jaxrs.facet.install.page"); //$NON-NLS-1$
    setTitle(Messages.JAXRSFacetInstallPage_title);
    setDescription(Messages.JAXRSFacetInstallPage_description);
    dialogSettings = JAXRSUIPlugin.getDefault().getDialogSettings();

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #createTopLevelComposite(org.eclipse.swt.widgets.Composite)
   */
  protected Composite createTopLevelComposite(final Composite parent)
  {
    initializeDialogUnits(parent);
    composite = new Composite(parent, SWT.NONE);
    final GridLayout jaxrsCompositeLayout = new GridLayout(1, false);
    jaxrsCompositeLayout.marginTop = 0;
    jaxrsCompositeLayout.marginBottom = 0;
    jaxrsCompositeLayout.marginRight = 0;
    jaxrsCompositeLayout.marginLeft = 0;
    composite.setLayout(jaxrsCompositeLayout);

    final LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);
    
    currentlySelectedLibraryType = librariesInstallDelegate == null ? null : librariesInstallDelegate.getLibraryProvider();
	librariesInstallDelegate.getLibraryProviders();
	java.util.List<ILibraryProvider> providers = librariesInstallDelegate.getLibraryProviders();
	if (providers != null) {
		for (ILibraryProvider provider : providers) {
			if (provider != null) {
				if (provider instanceof LibraryProvider) {
					if (!provider.isAbstract()) {
						LibraryProviderOperationConfig config = librariesInstallDelegate
								.getLibraryProviderOperationConfig(provider);

						if (config instanceof JAXRSUserLibraryProviderInstallOperationConfig) {
							JAXRSUserLibraryProviderInstallOperationConfig customConfig = (JAXRSUserLibraryProviderInstallOperationConfig) config;
							customConfig.setModel(getDataModel());
						} else if (config instanceof JAXRSSharedLibraryProviderInstallOperationConfig) {
							JAXRSSharedLibraryProviderInstallOperationConfig customConfig = (JAXRSSharedLibraryProviderInstallOperationConfig) config;
							customConfig.setModel(getDataModel());
						}
					}
				}
			}
		}
	}    

    final Control librariesComposite = LibraryProviderFrameworkUi.createInstallLibraryPanel(composite, librariesInstallDelegate, Messages.JAXRSFacetInstallPage_JAXRSImplementationLibrariesFrame);

    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 3;

    librariesComposite.setLayoutData(gd);
	updateDDCheckBox = new Button(composite, SWT.CHECK);
	updateDDCheckBox.setText(Messages.JAXRSFacetInstallPage_UpdateDD);
	updateDDCheckBox.addSelectionListener(
			new SelectionAdapter() {
				public void widgetSelected(final SelectionEvent event) { 
					servletInfoGroup.setFieldsEnabled(updateDDCheckBox.getSelection());
				}
				});

    servletInfoGroup = new ServletInformationGroup(composite, SWT.NONE);
    servletInfoGroup.setDataModel(model);
   	updateUpdateDDState(librariesInstallDelegate.getLibraryProvider().getId());

    addModificationListeners();

    return composite;
  }

private void updateUpdateDDState(String libraryProviderID) {
	boolean bUserLibrary = libraryProviderID.equals(IJAXRSUIConstants.USER_LIBRARY_ID);
	if (bUserLibrary) {
		updateDDCheckBox.setVisible(isJEE6orGreater());
	}  else
		updateDDCheckBox.setVisible(showUpdateDDCheckBox(libraryProviderID));
    if (updateDDCheckBox.getVisible()) {
    		boolean selected;
    		if (!bUserLibrary)
    			selected = getUpdateDDCheckBoxSelected(libraryProviderID);
    		else 
    			selected = true;
    		servletInfoGroup.setFieldsEnabled(selected);
    		updateDDCheckBox.setSelection(selected);
    		updateDDCheckBox.getSelection();
    }
	if (updateDDCheckBox.getVisible())
		model.setBooleanProperty(IJAXRSFacetInstallDataModelProperties.UPDATEDD, updateDDCheckBox.getSelection());
	else
		model.setBooleanProperty(IJAXRSFacetInstallDataModelProperties.UPDATEDD, true);
}

	@SuppressWarnings("rawtypes")
	private boolean isJEE6orGreater() {
		sWEBProject = this.context.getProjectName();
		Iterator it = this.context.getSelectedProjectFacets().iterator();
		IProjectFacetVersion webFacetVersion = null;
		while (it.hasNext()) {
			// find Web facet
			IProjectFacetVersion pfv = (IProjectFacetVersion) it.next();
			if (pfv.getProjectFacet().getId().equals("jst.web")) { //$NON-NLS-1$
				webFacetVersion = pfv;
				break;
			}
		}
		if (webFacetVersion != null) {
			if (webFacetVersion.equals(WebUtilities.DYNAMIC_WEB_30)) 
				return true;
				
		}

		return false;
	}

private boolean getUpdateDDCheckBoxSelected(String libraryProviderID) {
	return JAXRSLibraryProviderUtil.isUpdateDDCheckBoxSelectedByDefault(libraryProviderID);
}

private boolean showUpdateDDCheckBox(String libraryProviderID) {
	if (libraryProviderID.equals(IJAXRSUIConstants.USER_LIBRARY_ID))
		return true;
	return JAXRSLibraryProviderUtil.isUpdateDDCheckBoxSupportAvailable(libraryProviderID);
}

private void initializeValues()
  {
    IDialogSettings root = dialogSettings.getSection(IJAXRSUIConstants.SETTINGS_ROOT);

    if (!this.isProjectCreationMode)
    {
      // We are in add/remove facets mode
      IProject webProject = SharedLibraryConfiguratorUtil.getWebProject(model);
      if (webProject != null)
      {
        try
        {
          IFacetedProject fProject = ProjectFacetsManager.create(webProject);
          // Get the runtime associated with this project
          org.eclipse.wst.common.project.facet.core.runtime.IRuntime facetRuntime = fProject.getPrimaryRuntime();
          if (facetRuntime != null)
          {
            IRuntime runtime = FacetUtil.getRuntime(facetRuntime);
            if (runtime != null)
            {
              IRuntimeType rtType = runtime.getRuntimeType();
              if (rtType != null)
              {
                sTargetRuntime = rtType.getId();
              }
              // Now, set the property
              model.setStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME, sTargetRuntime);
            }
          }
        }
        catch (CoreException e)
        {
          // We should have a faceted project
        }
      }
      String earName = model.getStringProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME);
      if (earName == null || earName.equals(""))
      {
        if (webProject != null)
        {

          IProject[] earProjects = EarUtilities.getReferencingEARProjects(webProject); // required
                                                                                       // org.eclipse.jem.util
          if (earProjects.length > 0)
          {
            earName = earProjects[0].getName();
            // Since we do have an EAR...
            this.bAddToEAR = true;
            this.sEARProject = earName;
            model.setBooleanProperty(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR, true);
            model.setStringProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME, earName);
          }
        }
      }
    }

    String servletName = null;
    if (root != null)
      servletName = root.get(SETTINGS_SERVLET);
    if (servletName == null || servletName.equals("")) { //$NON-NLS-1$
      servletName = (String) model.getDefaultProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_NAME);
    }
    servletInfoGroup.txtJAXRSServletName.setText(servletName);

    String servletClassname = null;
    String libraryProviderID = "";
	LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);
	if (librariesInstallDelegate != null && librariesInstallDelegate.getLibraryProvider() != null)
		libraryProviderID = librariesInstallDelegate.getLibraryProvider().getId();
    if (root != null) {
        servletClassname = root.get(libraryProviderID + SETTINGS_SERVLET_CLASSNAME);
    }
    if (servletClassname == null) { 
      servletClassname = JAXRSLibraryProviderUtil.getServletClassName(libraryProviderID);
      if (servletClassname == null)
    	  servletClassname = (String) model.getDefaultProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_CLASSNAME);
    }
    servletInfoGroup.txtJAXRSServletClassName.setText(servletClassname);

    loadURLMappingPatterns(root);
  }

  private void saveSettings()
  {
    DialogSettings root = new DialogSettings(IJAXRSUIConstants.SETTINGS_ROOT);
    dialogSettings.addSection(root);
    root.put(SETTINGS_SERVLET, getJAXRSServletName());
    LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);
    root.put(new String (librariesInstallDelegate.getLibraryProvider().getId() + SETTINGS_SERVLET_CLASSNAME), getJAXRSServletClassname());
    DialogSettings mappings = new DialogSettings(SETTINGS_URL_MAPPINGS);
    root.addSection(mappings);
    mappings.put(SETTINGS_URL_PATTERN, getJAXRSPatterns());

  }

  private String getJAXRSServletName()
  {
    return servletInfoGroup.txtJAXRSServletName.getText().trim();
  }

  private String getJAXRSServletClassname()
  {
    return servletInfoGroup.txtJAXRSServletClassName.getText().trim();
  }

  private String[] getJAXRSPatterns()
  {
    return servletInfoGroup.lstJAXRSServletURLPatterns.getItems();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.project.facet.ui.IFacetWizardPage#setConfig(java
   * .lang.Object)
   */
  public void setConfig(Object config)
  {
    model.removeListener(this);
    synchHelper.dispose();

    model = (IDataModel) config;
    model.addListener(this);
    synchHelper = initializeSynchHelper(model);
    model.setStringProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME, sEARProject);
    model.setStringProperty(IJAXRSFacetInstallDataModelProperties.WEBPROJECT_NAME, sWEBProject);
    model.setStringProperty(IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME, sTargetRuntime);
    model.setBooleanProperty(IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR, bAddToEAR);
    
    model.setProperty(IJAXRSFacetInstallDataModelProperties.SERVER_IRUNTIME, targetRuntime);
    model.setProperty(IJAXRSFacetInstallDataModelProperties.CONFIGURATION_PRESET, selectedPreset);
    model.setProperty(IJAXRSFacetInstallDataModelProperties.EARPROJECTS, earProjects);

  }

  /*
   * (non-Javadoc)
   * 
   * @seeorg.eclipse.wst.common.project.facet.ui.IFacetWizardPage#
   * transferStateToConfig()
   */
  public void transferStateToConfig()
  {
    saveSettings();
  }

  private void addModificationListeners()
  {
// jaxrsLibCfgComp.setSynchHelper(synchHelper);
    synchHelper.synchText(servletInfoGroup.txtJAXRSServletName, SERVLET_NAME, null);
    synchHelper.synchText(servletInfoGroup.txtJAXRSServletClassName, SERVLET_CLASSNAME, null);
    synchHelper.synchList(servletInfoGroup.lstJAXRSServletURLPatterns, SERVLET_URL_PATTERNS, null);
    synchHelper.synchCheckbox(updateDDCheckBox, UPDATEDD, null);
  }

  private void loadURLMappingPatterns(IDialogSettings root)
  {
    servletInfoGroup.lstJAXRSServletURLPatterns.removeAll();
    IDialogSettings mappings = null;
    if (root != null)
      mappings = root.getSection(SETTINGS_URL_MAPPINGS);
    String[] patterns = null;
    if (mappings != null)
      patterns = mappings.getArray(SETTINGS_URL_PATTERN);

    if (patterns == null || patterns.length == 0)
    {
      patterns = (String[]) model.getDefaultProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_URL_PATTERNS);
    }
    for (int i = 0; i < patterns.length; i++)
    {
      addItemToList(patterns[i], false);
    }
  }

  private void addItemToList(String pattern, boolean selectMe)
  {
    servletInfoGroup.lstJAXRSServletURLPatterns.add(pattern == null ? "" : pattern); //$NON-NLS-1$
    if (pattern == null && selectMe)
      servletInfoGroup.lstJAXRSServletURLPatterns.setSelection(servletInfoGroup.lstJAXRSServletURLPatterns.getItemCount() - 1);
    // When 119321 is fixed - remove code below
    updateModelForURLPattern();
  }

  private void removeItemFromList(String[] selection)
  {
    for (int i = 0; i < selection.length; i++)
    {
      String sel = selection[i];
      servletInfoGroup.lstJAXRSServletURLPatterns.remove(sel);
    }
    // When 119321 is fixed - remove code below
    updateModelForURLPattern();
  }

  private void updateModelForURLPattern()
  {
    model.setProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_URL_PATTERNS, servletInfoGroup.lstJAXRSServletURLPatterns.getItems());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #getValidationPropertyNames()
   */
  protected String[] getValidationPropertyNames()
  {
    return new String[] { SERVLET_NAME, SERVLET_CLASSNAME, LIBRARY_PROVIDER_DELEGATE, UPDATEDD };
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.project.facet.ui.IFacetWizardPage#setWizardContext
   * (org.eclipse.wst.common.project.facet.ui.IWizardContext)
   */
  @SuppressWarnings("unchecked")
  public void setWizardContext(IWizardContext context)
  {
    // hook into web datamodel of new project wizard.
	this.context = context;
    sWEBProject = context.getProjectName();
    Iterator it = context.getSelectedProjectFacets().iterator();
    IProjectFacetVersion webFacetVersion = null;
    while (it.hasNext())
    {
      // find Web facet
      IProjectFacetVersion pfv = (IProjectFacetVersion) it.next();
      if (pfv.getProjectFacet().getId().equals("jst.web")) { //$NON-NLS-1$
        webFacetVersion = pfv;
        break;
      }
    }
    if (webFacetVersion != null)
    {
      try
      {
        webAppDataModel = (IDataModel) context.getConfig(webFacetVersion, IFacetedProject.Action.Type.INSTALL, context.getProjectName());
        if (webAppDataModel == null)
        {
          // This means the web facet has already been installed!
          isProjectCreationMode = false;
          return;
        }
        Object oAddToEAR = webAppDataModel.getProperty(IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR);
        Object oTargetRuntime = webAppDataModel.getProperty(IJ2EEModuleFacetInstallDataModelProperties.FACET_RUNTIME);
        if (oAddToEAR != null)
        {
          if (((Boolean) oAddToEAR).booleanValue() == true)
          {
            bAddToEAR = true;
            Object oEARProjectName = webAppDataModel.getProperty(IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME);
            if (oEARProjectName != null)
            {
              this.sEARProject = (String) oEARProjectName;

            }
          }
        }
        if (oTargetRuntime != null && oTargetRuntime instanceof BridgedRuntime)
        {
          BridgedRuntime br = (BridgedRuntime) oTargetRuntime;
          if (br != null)
          {
            IRuntime runtime = FacetUtil.getRuntime(br);
            if (runtime != null)
            {
              IRuntimeType rtType = runtime.getRuntimeType();
              if (rtType != null)
                sTargetRuntime = rtType.getId();
            }
          }
        }

        if (webAppDataModel != null)
        {
          webAppDataModel.addListener(this);
        }
      }
      catch (CoreException e)
      {
        JAXRSUIPlugin.log(IStatus.ERROR, Messages.JAXRSFacetInstallPage_ErrorNoWebAppDataModel, e);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #
   * propertyChanged(org.eclipse.wst.common.frameworks.datamodel.DataModelEvent
   * )
   */
  public void propertyChanged(DataModelEvent event)
  {

    if (webAppDataModel != null)
    {
      String propertyName = event.getPropertyName();
      if (propertyName.equals(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER))
      {
        model.setStringProperty(WEBCONTENT_DIR, event.getProperty().toString());
      }
      else if (propertyName.equals(IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR))
      {
        model.setBooleanProperty(ADD_TO_EAR, ((Boolean) event.getProperty()).booleanValue());
      }
      else if (propertyName.equals(IJ2EEModuleFacetInstallDataModelProperties.FACET_PROJECT_NAME))
      {
        model.setStringProperty(WEBPROJECT_NAME, event.getProperty().toString());
      }
      else if (propertyName.equals(IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME))
      {
        model.setStringProperty(EARPROJECT_NAME, event.getProperty().toString());
      }
      else if (propertyName.equals(IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE))
      {
        if (event.getProperty() != null) {

        	LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);
       		String libraryProviderID = librariesInstallDelegate.getLibraryProvider().getId();
       		ILibraryProvider thisProvider = librariesInstallDelegate.getLibraryProvider();
			try {
				//we are sometimes notified when the user has not actually changed the selected library type
				if (currentlySelectedLibraryType != thisProvider) {
					//only update servlet class name & update DD state when library 
					//type has changed
					currentlySelectedLibraryType = thisProvider;
					updateUpdateDDState(libraryProviderID);
					updateServletClassName(libraryProviderID);
				} 
			} catch (Exception e) {
				//TODO exception as we are notified in non-UI thread and Invalid thread access exception,
				//should find another way to get notified when library provider changes

			}
        	
        }
      }
    }
    super.propertyChanged(event);
  }

  private void updateServletClassName(String libraryProviderID) {
	  servletInfoGroup.txtJAXRSServletClassName.setText(JAXRSLibraryProviderUtil.getServletClassName(libraryProviderID));
  }

/*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #dispose()
   */
  public void dispose()
  {
    if (webAppDataModel != null)
      webAppDataModel.removeListener(this);

// jaxrsLibCfgComp.dispose();
    super.dispose();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #restoreDefaultSettings()
   */
  protected void restoreDefaultSettings()
  {
    initializeValues();

// checkToCompletePage(jaxrsLibCfgComp);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage
   * #showValidationErrorsOnEnter()
   */
  protected boolean showValidationErrorsOnEnter()
  {
    return true;
  }
  @Override
  public boolean isPageComplete()
  {
      final LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);
      if (librariesInstallDelegate == null)
          throw new IllegalArgumentException("LibraryInstallDelegate is expected to be non-null"); //$NON-NLS-1$

      return super.isPageComplete() && (librariesInstallDelegate.validate().getSeverity() != IStatus.ERROR);
  }


	private void setChildrenEnabled(Composite parentComposite, boolean enabled) {
		Control[] wsdlControls = parentComposite.getChildren();
		for (int i = 0; i < wsdlControls.length; i++) {
			wsdlControls[i].setEnabled(enabled);
		}
	}

}
