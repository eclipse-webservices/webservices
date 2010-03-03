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
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jst.common.project.facet.core.libprov.LibraryInstallDelegate;
import org.eclipse.jst.common.project.facet.ui.libprov.LibraryProviderFrameworkUi;
import org.eclipse.jst.j2ee.project.EarUtilities;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.server.core.internal.J2EEUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfigurator;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig.SharedLibraryConfiguratorUtil;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.DataModelEvent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizardPage;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
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
// private Group servletInfo;

  private ServletInformationGroup servletInfoGroup;
// private Label lblJAXRSServletName;
// private Text txtJAXRSServletName;
// private Label lblJAXRSServletClassName;
// private Text txtJAXRSServletClassName;
// private Label lblJAXRSServletURLPatterns;
// private List lstJAXRSServletURLPatterns;
// private Button btnAddPattern;
// private Button btnRemovePattern;

  private IDialogSettings dialogSettings;
  private IDataModel webAppDataModel;
  private String sEARProject = null;
  private String sWEBProject = null;
  private String sTargetRuntime = null;
  private boolean bAddToEAR = false;
  private static final String SETTINGS_SERVLET = "servletName"; //$NON-NLS-1$
  private static final String SETTINGS_SERVLET_CLASSNAME = "servletClassname"; //$NON-NLS-1$
  private static final String SETTINGS_URL_MAPPINGS = "urlMappings"; //$NON-NLS-1$
  private static final String SETTINGS_URL_PATTERN = "pattern"; //$NON-NLS-1$
  private Composite composite = null;

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

    Object libProvDel = getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);

    final LibraryInstallDelegate librariesInstallDelegate = (LibraryInstallDelegate) getDataModel().getProperty(LIBRARY_PROVIDER_DELEGATE);

    JAXRSUserLibraryProviderInstallOperationConfig customConfig = (JAXRSUserLibraryProviderInstallOperationConfig) librariesInstallDelegate.getLibraryProviderOperationConfig();
    customConfig.setModel(getDataModel());

    System.out.println(librariesInstallDelegate.getLibraryProvider().getId());
    final Control librariesComposite = LibraryProviderFrameworkUi.createInstallLibraryPanel(composite, librariesInstallDelegate, Messages.JAXRSFacetInstallPage_JAXRSImplementationLibrariesFrame);

    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = 3;

    librariesComposite.setLayoutData(gd);

    servletInfoGroup = new ServletInformationGroup(composite, SWT.NONE);
    servletInfoGroup.setDataModel(model);

// servletInfo = new Group(composite, SWT.NONE);
// GridData servletGD = new GridData(GridData.FILL_HORIZONTAL);
// servletInfo.setLayoutData(servletGD);
// servletInfo.setLayout(new GridLayout(3, false));
// servletInfo.setText(Messages.JAXRSFacetInstallPage_JAXRSServletLabel);
//
// lblJAXRSServletName = new Label(servletInfo, SWT.NONE);
// lblJAXRSServletName
// .setText(Messages.JAXRSFacetInstallPage_JAXRSServletNameLabel);
// lblJAXRSServletName.setLayoutData(new GridData(GridData.BEGINNING));
//
// txtJAXRSServletName = new Text(servletInfo, SWT.BORDER);
// GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
// gd2.horizontalSpan = 2;
// txtJAXRSServletName.setLayoutData(gd2);
//
// lblJAXRSServletClassName = new Label(servletInfo, SWT.NONE);
// lblJAXRSServletClassName
// .setText(Messages.JAXRSFacetInstallPage_JAXRSServletClassNameLabel);
// lblJAXRSServletClassName
// .setLayoutData(new GridData(GridData.BEGINNING));
//
// txtJAXRSServletClassName = new Text(servletInfo, SWT.BORDER);
// GridData gd2c = new GridData(GridData.FILL_HORIZONTAL);
// gd2c.horizontalSpan = 2;
// txtJAXRSServletClassName.setLayoutData(gd2c);
//
// lblJAXRSServletURLPatterns = new Label(servletInfo, SWT.NULL);
// lblJAXRSServletURLPatterns
// .setText(Messages.JAXRSFacetInstallPage_JAXRSURLMappingLabel);
// lblJAXRSServletURLPatterns.setLayoutData(new GridData(
// GridData.BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
// lstJAXRSServletURLPatterns = new List(servletInfo, SWT.BORDER);
// GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
// gd3.heightHint = convertHeightInCharsToPixels(5);
// lstJAXRSServletURLPatterns.setLayoutData(gd3);
// lstJAXRSServletURLPatterns.addSelectionListener(new SelectionAdapter() {
// public void widgetSelected(SelectionEvent e) {
// btnRemovePattern.setEnabled(lstJAXRSServletURLPatterns
// .getSelectionCount() > 0);
// }
// });
//
// Composite btnComposite = new Composite(servletInfo, SWT.NONE);
// GridLayout gl = new GridLayout(1, false);
// gl.marginLeft = 0;
// btnComposite.setLayout(gl);
// btnComposite.setLayoutData(new GridData(GridData.END
// | GridData.VERTICAL_ALIGN_FILL));
//
// btnAddPattern = new Button(btnComposite, SWT.NONE);
// btnAddPattern.setText(Messages.JAXRSFacetInstallPage_Add2);
// btnAddPattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
// | GridData.VERTICAL_ALIGN_BEGINNING));
// btnAddPattern.addSelectionListener(new SelectionAdapter() {
// public void widgetSelected(SelectionEvent e) {
// InputDialog dialog = new InputDialog(getShell(),
// Messages.JAXRSFacetInstallPage_PatternDialogTitle,
// Messages.JAXRSFacetInstallPage_PatternDialogDesc, null,
// new IInputValidator() {
//
// public String isValid(String newText) {
// return isValidPattern(newText);
// }
//
// });
// dialog.open();
// if (dialog.getReturnCode() == Window.OK) {
// addItemToList(dialog.getValue(), true);
// }
// }
// });
//
// btnRemovePattern = new Button(btnComposite, SWT.NONE);
// btnRemovePattern.setText(Messages.JAXRSFacetInstallPage_Remove);
// btnRemovePattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
// | GridData.VERTICAL_ALIGN_BEGINNING));
// btnRemovePattern.setEnabled(false);
// btnRemovePattern.addSelectionListener(new SelectionAdapter() {
// public void widgetSelected(SelectionEvent e) {
// removeItemFromList(lstJAXRSServletURLPatterns.getSelection());
// btnRemovePattern.setEnabled(false);
// }
// });

    addModificationListeners();

    this.getContainer().getShell().pack();

    return composite;
  }

  private void initializeValues()
  {
    IDialogSettings root = dialogSettings.getSection(IJAXRSUIConstants.SETTINGS_ROOT);

// initJAXRSCfgCtrlValues(root);

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
    if (root != null)
      servletClassname = root.get(SETTINGS_SERVLET_CLASSNAME);
    if (servletClassname == null || servletClassname.equals("")) { //$NON-NLS-1$
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
    root.put(SETTINGS_SERVLET_CLASSNAME, getJAXRSServletClassname());
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
  }

  private String isValidPattern(String value)
  {
    if (value == null || value.trim().equals("")) //$NON-NLS-1$
      return Messages.JAXRSFacetInstallPage_PatternEmptyMsg;
    if (servletInfoGroup.lstJAXRSServletURLPatterns.indexOf(value) >= 0)
      return Messages.JAXRSFacetInstallPage_PatternSpecifiedMsg;

    return null;
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
    return new String[] { SERVLET_NAME, SERVLET_CLASSNAME, LIBRARY_PROVIDER_DELEGATE };
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
      else if (propertyName.equals(IJ2EEModuleFacetInstallDataModelProperties.FACET_RUNTIME))
      {
        if (event.getProperty() != null) 
          model.setStringProperty(TARGETRUNTIME, event.getProperty().toString());
      }
    }
    super.propertyChanged(event);
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

}
