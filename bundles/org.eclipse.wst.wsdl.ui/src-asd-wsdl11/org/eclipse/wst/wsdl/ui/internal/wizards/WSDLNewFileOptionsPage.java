/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.ws.service.policy.IServicePolicy;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui.BaseContentGeneratorOptionsPage;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui.ISoapStyleInfo;
import org.eclipse.wst.wsdl.ui.internal.util.ServicePolicyHelper;
import org.eclipse.wst.wsdl.ui.internal.util.ValidateHelper;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.xml.ui.internal.dialogs.UpdateListener;

public class WSDLNewFileOptionsPage extends WizardPage implements ModifyListener, UpdateListener, SelectionListener
{
  protected WizardNewFileCreationPage newFileCreationPage;
  protected Text targetNamespaceText;
  protected Text prefixText;
  protected boolean updatePortOpFieldBoolean = true;
  
  protected PageBook protocolPageBook;
  protected Combo protocolCombo;
  protected Link WSIPreferenceLink;
  
  private BindingGenerator generator;
  private Map pageMap = new HashMap();
  private IServicePolicy activeServicePolicy;

  /**
   * Constructor for WSDLNewFileOptionsPage.
   * @param pageName
   */
  public WSDLNewFileOptionsPage(String pageName)
  {
    super(pageName);
  }
  
  public void setBindingGenerator(BindingGenerator generator) {
	  this.generator = generator;
  }
  
  /**
   * Constructor for WSDLNewFileOptionsPage.
   * @param pageName
   * @param title
   * @param titleImage
   */
  public WSDLNewFileOptionsPage(String pageName, String title, ImageDescriptor titleImage)
  {
    super(pageName, title, titleImage);
    setDescription(Messages._UI_DESCRIPTION_NEW_WSDL_FILE);
  }

  public WSDLNewFileOptionsPage(String pageName, String title, ImageDescriptor titleImage, WizardNewFileCreationPage newFilePage)
  {
    super(pageName, title, titleImage);
    setDescription(Messages._UI_DESCRIPTION_NEW_WSDL_FILE);
    newFileCreationPage = newFilePage;
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite base = new Composite(parent, SWT.NONE);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(base, Messages._UI_HELP); //$NON-NLS-1$
    base.setLayout(new GridLayout());

    //  Group wsdlGroup = ViewUtility.createGroup(base, 2, "WSDL", false);
    Composite wsdlGroup = new Composite(base, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    wsdlGroup.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    wsdlGroup.setLayoutData(data);
    
    GridData gd = (GridData)wsdlGroup.getLayoutData();
    gd.grabExcessHorizontalSpace = true;

    Label targetNamespace = new Label(wsdlGroup, SWT.LEFT);
    targetNamespace.setText(Messages._UI_LABEL_TARGET_NAMESPACE);
    GridData nsData = new GridData();
    nsData.horizontalAlignment = GridData.FILL;
    targetNamespace.setLayoutData(nsData);
    
    targetNamespaceText = new Text(wsdlGroup, SWT.SINGLE | SWT.BORDER);
    GridData textData = new GridData();
    textData.horizontalAlignment = GridData.FILL;
    textData.grabExcessHorizontalSpace = true;
    textData.widthHint = 10;
    targetNamespaceText.setLayoutData(textData);
    
    targetNamespaceText.addModifyListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(targetNamespaceText, ASDEditorCSHelpIds.WSDL_WIZARD_OPTIONS_PAGE_TNS_TEXT);

    Label prefix = new Label(wsdlGroup, SWT.LEFT);
    prefix.setText(Messages._UI_LABEL_PREFIX_WITH_COLON);

    GridData prefixData = new GridData();
    prefixData.horizontalAlignment = GridData.FILL;
    prefix.setLayoutData(prefixData);
    
    prefixText = new Text(wsdlGroup, SWT.SINGLE | SWT.BORDER);
    GridData prefixTextData = new GridData();
    prefixTextData.horizontalAlignment = GridData.FILL;
    prefixTextData.grabExcessHorizontalSpace = true;
    prefixTextData.widthHint = 10;
    prefixText.setLayoutData(prefixTextData);

    prefixText.addModifyListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(prefixText, ASDEditorCSHelpIds.WSDL_WIZARD_OPTIONS_PAGE_PREFIX_TEXT);
    
    //    XMLCatalog xmlCatalog = XMLCatalogRegistry.getInstance().lookupOrCreateXMLCatalog("default");
    //    SelectXMLCatalogIdPanel catalog = new SelectXMLCatalogIdPanel(base, xmlCatalog);

//  Determine if the user wishes to create a skeleton WSDL.  If yes, present the user with input fields.
    createSkeletonCheckBox = new Button(base, SWT.CHECK);
    createSkeletonCheckBox.setText(Messages._UI_LABEL_CREATE_WSDL_SKELETON);
    createSkeletonCheckBox.setSelection(true);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(createSkeletonCheckBox, ASDEditorCSHelpIds.WSDL_WIZARD_OPTIONS_PAGE_CREATE_SKELETON_CHECKBOX);
    
    wsdlSkeletonGroup = new Composite(base, SWT.NONE);
    GridLayout layout2 = new GridLayout();
    layout2.numColumns = 2;
    wsdlSkeletonGroup.setLayout(layout2);

    GridData data2 = new GridData();
    data2.verticalAlignment = GridData.FILL;
    data2.horizontalAlignment = GridData.FILL;
    wsdlSkeletonGroup.setLayoutData(data2);

   createLabel(wsdlSkeletonGroup, Messages._UI_LABEL_BINDING_PROTOCOL);
   protocolCombo = new Combo(wsdlSkeletonGroup, SWT.READ_ONLY);
   GridData dataC = new GridData();
   dataC.horizontalAlignment = GridData.FILL;
   dataC.grabExcessHorizontalSpace = true;
   protocolCombo.setLayoutData(dataC);

    IProject project = getProject();
    activeServicePolicy = ServicePolicyHelper.getActivePolicyWithProtocol(project);
    String defaultProtocolNS = ServicePolicyHelper.getDefaultBinding(project, activeServicePolicy);
    String defaultProtocolLabel = null;
    ContentGeneratorUIExtensionRegistry registry = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry();
    ContentGeneratorUIExtension extension = registry.getExtensionForNamespace(defaultProtocolNS);
    if (extension != null)
      defaultProtocolLabel = extension.getLabel();

    Iterator it = registry.getBindingExtensionNames().iterator();
    int defaultIndex = 0;
    boolean defaultFound = false;
    while (it.hasNext()) {
      String protocolName = (String) it.next();
      String protocolLabel = null;
      ContentGeneratorUIExtension ext = registry.getExtensionForName(protocolName);
      if (ext != null) {
        String label = ext.getLabel();
        if (label != null) {
          protocolLabel = label;
          protocolCombo.add(protocolLabel);
        }
      }

      if (!defaultFound && protocolLabel != null)
      {
        defaultFound = protocolLabel.equals(defaultProtocolLabel);
        if (!defaultFound) {
          ++defaultIndex;
        }
      }
    }
    if (!defaultFound) {    	
      defaultIndex = 0;
    }

    protocolCombo.addModifyListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolCombo, ASDEditorCSHelpIds.WSDL_WIZARD_OPTIONS_PAGE_PROTOCOL_COMBO);

    // Create PageBook and pages/controls for the PageBook
    protocolPageBook = new PageBook(base, SWT.NONE);
    GridData gdFill = new GridData();
    gdFill.horizontalAlignment = GridData.FILL;
    gdFill.grabExcessHorizontalSpace = true;
    gdFill.verticalAlignment = GridData.FILL;
    gdFill.grabExcessVerticalSpace = true;
    protocolPageBook.setLayoutData(gdFill);

    if (protocolCombo.getItemCount() > 0) {
      String protocol = protocolCombo.getItem(defaultIndex);
      ContentGeneratorUIExtension ext = registry.getExtensionForLabel(protocol);
      ContentGeneratorOptionsPage page = ext.getBindingContentGeneratorOptionsPage();
      page.init(generator);

      protocolPageBook.showPage(page.getControl());
      protocolPageBook.setVisible(true);
      protocolCombo.select(defaultIndex);
      updatePageBook(protocol);
    }

    wsdlSkeletonGroup.setVisible(true);

    WSIPreferenceLink = new Link(base, SWT.NONE);
    WSIPreferenceLink.setText("<A>" + Messages._WSI_COMPLIANCE_LINK_TEXT + "</A>"); //$NON-NLS-1$ //$NON-NLS-2$
    WSIPreferenceLink.addSelectionListener(new SelectionAdapter()
    {
      public void widgetSelected(SelectionEvent e)
      {
        getNewWSDLWizard().openProjectWSIProperties();
        IProject project = getProject();
        activeServicePolicy = ServicePolicyHelper.getActivePolicyWithProtocol(project);
        setPageComplete(validatePage());
      }
    });

    WSIPreferenceLink.setLayoutData(new GridData(GridData.GRAB_VERTICAL | GridData.VERTICAL_ALIGN_END));

    createSkeletonCheckBox.addSelectionListener(this);

    PlatformUI.getWorkbench().getHelpSystem().setHelp(base, ASDEditorCSHelpIds.WSDL_WIZARD_OPTIONS_PAGE);

    setControl(base);

  }

  private NewWSDLWizard getNewWSDLWizard()
  {
    return (NewWSDLWizard)getWizard();
  }

  private String computeDefaultDefinitionName()
  {
    String name = "DefaultName"; //$NON-NLS-1$
    IPath path = getNewWSDLWizard().getNewFilePath();
    if (path != null)
    {
      name = path.removeFileExtension().lastSegment().toString();
    }
    return name;
  }

  private String computeDefaultNamespaceName()
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(Messages._UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE);
    if (!namespace.endsWith("/")) { //$NON-NLS-1$
    	namespace = namespace.concat("/"); //$NON-NLS-1$
    }
    
    IPath path = getNewWSDLWizard().getNewFilePath();
    if (path != null)
    {
      namespace += path.removeFileExtension().toString() + "/"; //$NON-NLS-1$
    }
    return namespace;
  }

  public void setVisible(boolean visible)
  {
    super.setVisible(visible);

    if (visible)
    {
      // prime the fields    
      targetNamespaceText.setText(computeDefaultNamespaceName());
      prefixText.setText("tns"); //$NON-NLS-1$
    }
  }

  public String getTargetNamespace()
  {
    return targetNamespaceText.getText();
  }

  public String getPrefix()
  {
    return prefixText.getText().trim();
  }

  public String getDefinitionName()
  {
  	return computeDefaultDefinitionName();
  }

  public void modifyText(ModifyEvent e)
  {
    if (e.widget == targetNamespaceText)
    {
      startDelayedEvent(e);
    }
    else if (e.widget == prefixText)
    {
      setPageComplete(validatePage());
    }
    else if (e.widget == protocolCombo)
    {
      String protocol = protocolCombo.getText();

      ContentGeneratorUIExtension ext = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForLabel(protocol);
      generator.setContentGenerator(BindingGenerator.getContentGenerator(ext.getNamespace()));
      updatePageBook(protocol);
      setPageComplete(validatePage());
    }
  }

  protected void updatePageBook(String protocol)
  {
    if (protocol != null)
    {
	  ContentGeneratorOptionsPage page = (ContentGeneratorOptionsPage) pageMap.get(protocol);
      if (page == null)
      {
        page = createContentGeneratorOptionsPage(protocol);

        if (page != null)
        {
          page.init(generator);
          page.createControl(protocolPageBook);
          pageMap.put(protocol, page);
          
          if (page instanceof BaseContentGeneratorOptionsPage) {
        	  // We should put the setWizardPage() method into the ContentGeneratorOptionsPage Interface
        	  ((BaseContentGeneratorOptionsPage) page).setWizardPage(this);
          }
          
        }
      }

      if (page != null)
      {
    	  protocolPageBook.showPage(page.getControl());
    	  protocolPageBook.layout();
    	  protocolPageBook.getParent().layout();
    	  
    	  // resize the wizard dialog if necessary for the updated page
    	  Point size = protocolPageBook.getShell().getSize();
    	  Point minSize = protocolPageBook.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    	  int newX = Math.max(size.x, minSize.x);
    	  int newY = Math.max(size.y, minSize.y);
    	  protocolPageBook.getShell().setSize(newX, newY);
    	  
    	  page.setOptionsOnGenerator();
      }
    }
  }

  public ContentGeneratorOptionsPage createContentGeneratorOptionsPage(String protocol)
  {
    ContentGeneratorOptionsPage optionsPage = null;
    ContentGeneratorUIExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForLabel(protocol);
    if (extension != null) {
  	  optionsPage = extension.getBindingContentGeneratorOptionsPage();
    }
    return optionsPage;
  }

  public void updateOccured(Object arg1, Object arg2)
  {
    setPageComplete(validatePage());
  }

  public boolean validatePage()
  {
    boolean ready = true;

    setErrorMessage(null);
    setMessage(null);

    // this.setMessage(Messages.getString("_UI_DESCRIPTION_NEW_WSDL_FILE"),this.NONE);
    // //$NON-NLS-1$

    // so that the page doesn't immediately show up with an error
    if (targetNamespaceText.getText().trim().equals("")) //$NON-NLS-1$
    {
      if (ready)
      {
        setErrorMessage(null);
      }
      return false;
    }

    if (!validateTargetNamespace(targetNamespaceText.getText()))
    {
      ready = false;
    }

    if (createSkeletonCheckBox.getSelection())
    {
      if (!displayDialogMessages())
      {
        ready = false;
      }
    }
    else
    {
      this.setMessage(Messages._UI_DESCRIPTION_NEW_WSDL_FILE, NONE);
    }

    return ready;
  }

  protected boolean displayDialogMessages()
  {
    String protocol = getProtocol();
    if (!(pageMap.get(protocol) instanceof ContentGeneratorOptionsPage))
    {
      return false;
    }

    if (!validateProtocol(protocol))
    {
      if (getMessageType() == IMessageProvider.ERROR)
        return false;
      else
        return true;
    }

    ContentGeneratorOptionsPage optionsPage = (ContentGeneratorOptionsPage) pageMap.get(protocol);
    IFile targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(newFileCreationPage.getContainerFullPath().append(newFileCreationPage.getFileName()));

    if (optionsPage instanceof BaseContentGeneratorOptionsPage)
    {
      ((BaseContentGeneratorOptionsPage) optionsPage).setTargetIFile(targetFile);

      String message = ((IMessageProvider) optionsPage).getMessage();
      int messageType = ((IMessageProvider) optionsPage).getMessageType();

      if (messageType == IMessageProvider.NONE)
      {
        setMessage(Messages._UI_DESCRIPTION_NEW_WSDL_FILE, NONE);
      }
      else
      {
        setMessage(message, messageType);
      }

      if (messageType == IMessageProvider.ERROR)
      {
        return false;
      }
    }

    return true;
  }

  protected boolean validatePrefix(String prefix)
  {
    String errorMessage = ValidateHelper.checkXMLPrefix(prefix);

    if (errorMessage == null || errorMessage.length() == 0)
    {
      return true;
    }
    return false;
  }

  /**
   * Validates protocol by checking if there is an active wsi policy requires or
   * suggests a default protocol and that protocol is not the same as the
   * currently selected protocol
   * 
   * @param protocol
   * @return
   */
  private boolean validateProtocol(String protocol)
  {
    ContentGeneratorUIExtensionRegistry registry = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry();
    ContentGeneratorUIExtension extension = registry.getExtensionForLabel(protocol);
    if (extension != null)
    {
      String namespace = extension.getNamespace();
      if (namespace != null)
      {
        IProject project = getProject();
        if (activeServicePolicy != null)
        {
          // get default binding and compare if same or not
          String defaultBinding = ServicePolicyHelper.getDefaultBinding(project, activeServicePolicy);
          if (!defaultBinding.equals(namespace))
          {
            int messageType = ServicePolicyHelper.getMessageSeverity(project, activeServicePolicy);
            if (messageType == IMessageProvider.ERROR)
            {
              // if not same, set error message
              // put up an error message
              ContentGeneratorUIExtension ext = registry.getExtensionForNamespace(defaultBinding);
              if (ext != null)
              {
                String n = ext.getLabel();
                setMessage(NLS.bind(Messages._ERROR_WSI_COMPLIANCE_PROTOCOL, new String[] { n }), IMessageProvider.ERROR);
              }
              return false;
            }
            else if (messageType == IMessageProvider.WARNING)
            {
              // put up a warning message
              ContentGeneratorUIExtension ext = registry.getExtensionForNamespace(defaultBinding);
              if (ext != null)
              {
                String n = ext.getLabel();
                setMessage(NLS.bind(Messages._WARN_WSI_COMPLIANCE_PROTOCOL, new String[] { n }), IMessageProvider.WARNING);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }

  protected boolean validateXMLName(String xmlName)
  {
    String errorMessage = ValidateHelper.checkXMLName(xmlName);

    if (errorMessage == null || errorMessage.length() == 0)
    {
      return true;
    }
    setErrorMessage(errorMessage);
    return false;
  }

  protected boolean validateTargetNamespace(String ns)
  {
    boolean test = true;
    try
    {
    	new URI(ns);
//      URI testURI = new URI(ns);
      //      if (!testURI.isGenericURI())
      //      {
      //        setErrorMessage(WSDLEditorPlugin.getInstance().getWSDLString("_UI_ERROR_NAMESPACE_INVALID")); //$NON-NLS-1$
      //        test = false;
      //      }
    }
    catch (URISyntaxException e)
    {
      //			String errorMessage = ValidateHelper.checkXMLName(ns);
      //			if (errorMessage == null || errorMessage.length() == 0)
      //			{
      //				test = true;
      //			}
      //			else
      //			{
      setErrorMessage(Messages._UI_ERROR_NAMESPACE_INVALID); //$NON-NLS-1$
      test = false;
      //			}
    }

    return test;
  }

  /*
   * private boolean arePrefixesUniqueAndValid() { java.util.List infoList =
   * namespaceInfo.getNamespaceInfoList(); java.util.List checkedList =
   * namespaceInfo.getNamespaceCheckedList();
   * 
   * Vector prefixList = new Vector(); boolean test = true; boolean isOneBlank =
   * false;
   * 
   * String currentPrefix = prefixText.getText().trim(); if
   * (currentPrefix.length() == 0) { isOneBlank = true; } else { if
   * (validatePrefix(currentPrefix)) { prefixList.add(currentPrefix); } else {
   * setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_INVALID",
   * currentPrefix)); //$NON-NLS-1$ return false; } }
   * 
   * for (int i = 0; i < infoList.size(); i++) { NamespaceInfo info =
   * (NamespaceInfo)infoList.get(i); if
   * (((String)checkedList.get(i)).equals("true")) //$NON-NLS-1$ { String
   * aPrefix = info.prefix.trim(); if (aPrefix.length() > 0) { if
   * (!prefixList.contains(aPrefix)) { if (validatePrefix(aPrefix)) {
   * prefixList.add(aPrefix); } else {
   * setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_INVALID",
   * info.prefix)); //$NON-NLS-1$ test = false; break; } } else {
   * setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_PREFIX_IS_A_DUPLICATE",
   * info.prefix)); //$NON-NLS-1$ test = false; break; } } else { if
   * (!isOneBlank) { isOneBlank = true; } else {
   * setErrorMessage(WSDLEditorPlugin.getWSDLString("_UI_ERROR_MORE_THAN_ONE_PREFIX_IS_BLANK"));
   * //$NON-NLS-1$ test = false; break; } } } } return test; }
   */

  protected DelayedEvent delayedTask;

  protected void startDelayedEvent(ModifyEvent e)
  {
    if (delayedTask == null || delayedTask.getEvent() == null)
    {
      delayedTask = new DelayedEvent();
      delayedTask.setEvent(e);
      Display.getDefault().timerExec(500, delayedTask);
    }
    else
    {
      ModifyEvent delayedEvent = delayedTask.getEvent();

      if (e.widget == delayedEvent.widget)
      {
        // same event, just different data, delay new event
        delayedTask.setEvent(null);
      }
      delayedTask = new DelayedEvent();
      delayedTask.setEvent(e);
      Display.getDefault().timerExec(500, delayedTask);
    }
  }

  class DelayedEvent implements Runnable
  {
    protected ModifyEvent event;

    /*
     * @see Runnable#run()
     */
    public void run()
    {
      if (event != null)
      {
        setPageComplete(validatePage());
        event = null;
      }
    }

    public ModifyEvent getEvent()
    {
      return event;
    }

    public void setEvent(ModifyEvent event)
    {
      this.event = event;
    }
  }

  public void widgetDefaultSelected(SelectionEvent e) {
  }

  public void widgetSelected(SelectionEvent e) {
	  if (e.widget == createSkeletonCheckBox) {
  	 	if (createSkeletonCheckBox.getSelection()) {
  	 		wsdlSkeletonGroup.setVisible(true);
  	 		protocolPageBook.setVisible(true);
  	 	}
  	 	else {
  	 		wsdlSkeletonGroup.setVisible(false);
  	 		protocolPageBook.setVisible(false);
  	 	}
  	 	setPageComplete(validatePage());
  	 }
  }

  Composite wsdlSkeletonGroup;

  Button createSkeletonCheckBox;

  private Label createLabel(Composite comp, String labelString) {
    Label label = new Label(comp, SWT.LEFT);
    label.setText(labelString);
    GridData nsData = new GridData();
    nsData.horizontalAlignment = GridData.FILL;
    label.setLayoutData(nsData);
    
    return label;
  }

  public Vector getNamespaceInfo() {
		Vector namespaces = new Vector();
  		
  		// Add Default Namespaces
		NamespaceInfo info1 = new NamespaceInfo();
		info1.prefix = "wsdl"; //$NON-NLS-1$
		info1.uri = "http://schemas.xmlsoap.org/wsdl/"; //$NON-NLS-1$
		namespaces.addElement(info1);
		
		NamespaceInfo info8 = new NamespaceInfo();
		info8.prefix = "xsd"; //$NON-NLS-1$
		info8.uri = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$
		namespaces.addElement(info8);

		return namespaces;
  }

  public boolean getCreateSkeletonBoolean() {
  	return createSkeletonCheckBox.getSelection();
  }

  public String getProtocol() {
  	return protocolCombo.getText();
  }

  public boolean isSoapDocLiteralProtocol() {
	  // We need to revisit this....  Can we make this code more generic...
	  // Wee need this method because the NewWSDLWizard needs to know if it should create the new
	  // WSDL with a doc-literal pattern (A WSDL Part referencing an XSD Element)
	  ContentGeneratorOptionsPage optionsPage = (ContentGeneratorOptionsPage) pageMap.get(getProtocol());
	  if (optionsPage instanceof ISoapStyleInfo) {
		  return ((ISoapStyleInfo) optionsPage).isDocumentLiteralPattern();
	  }

	  return false;
  }

  /**
   * Returns the project the current containing project
   * 
   * @return IProject object. If path is <code>null</code> the return value is
   *         also <code>null</code>.
   */
  public IProject getProject()
  {
    IPath path = newFileCreationPage.getContainerFullPath();
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IProject project = null;

    if (path != null)
    {
      if (workspace.validatePath(path.toString(), IResource.PROJECT).isOK())
      {
        project = workspace.getRoot().getProject(path.toString());
      }
      else
      {
        project = workspace.getRoot().getFile(path).getProject();
      }
    }

    return project;
  }

  public IServicePolicy getServicePolicy()
  {
    return activeServicePolicy;
  }
}