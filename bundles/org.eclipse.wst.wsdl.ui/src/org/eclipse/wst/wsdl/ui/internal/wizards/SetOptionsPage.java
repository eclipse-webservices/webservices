/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.AddElementDeclarationAction;
import org.eclipse.wst.wsdl.ui.internal.actions.AddImportAction;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.ValidateHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLResourceUtil;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Element;

public abstract class SetOptionsPage extends WizardPage implements ModifyListener, SelectionListener
{
  protected IEditorPart editorPart;
  protected Object input;

  protected Button selectExistingRadio;
  protected Button createNewRadio;
  protected Button importRadio;
  protected int choice = 1; // radio button choice
  protected PageBook pageBook;

  protected Composite page1;
  protected Text newNameText;
  protected String newName;

  protected Composite page2;
  protected org.eclipse.swt.widgets.List componentNameList;
  protected String existingListSelection;

  protected Composite page3;
  protected Button importButton;
  protected Text fileText;
  protected org.eclipse.swt.widgets.List importList;
  protected java.util.List importComponents;

  protected Object selection;

  protected IFile selectedFile; // the imported file

  private String wsdlItemName;
  String selectExistingRadioString;
  String createNewRadioString;
  String importRadioString;
  String newNameString;
  String pageOneDescription;
  String pageTwoDescription;
  String pageThreeDescription;

  /**
   * Constructor for NewOptionsPage.
   * @param pageName
   * @param title
   * @param titleImage
   */
  public SetOptionsPage(IEditorPart editorPart, String pageName, String title, ImageDescriptor titleImage, String wsdlItemName)
  {
    super(pageName, title, titleImage);
    this.editorPart = editorPart;
    this.wsdlItemName = wsdlItemName;

    // Alternate way of doing this?
    createNewRadioString = WSDLEditorPlugin.getWSDLString("_UI_RADIO_CREATE_NEW", wsdlItemName); //$NON-NLS-1$
    selectExistingRadioString = WSDLEditorPlugin.getWSDLString("_UI_RADIO_SELECT_EXISTING", wsdlItemName); //$NON-NLS-1$
    importRadioString = WSDLEditorPlugin.getWSDLString("_UI_RADIO_IMPORT_FROM_FILE", wsdlItemName); //$NON-NLS-1$
    newNameString = WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAME_OF_ITEM", wsdlItemName); //$NON-NLS-1$

    pageOneDescription = WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_PROVIDE_NAME", wsdlItemName); //$NON-NLS-1$
    pageTwoDescription = WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_CHOOSE_FROM_EXISTING", wsdlItemName); //$NON-NLS-1$
    pageThreeDescription = WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_IMPORT_FILE", wsdlItemName); //$NON-NLS-1$
  }

  public void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }

  public void setInput(Object input)
  {
    this.input = input;
  }

  protected void createPage1(Composite pageBook)
  {
    page1 = new Composite(pageBook, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    page1.setLayout(layout);

    GridData data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    page1.setLayoutData(data);
    
    Label newNameLabel = new Label(page1, SWT.LEFT);
    newNameLabel.setText(newNameString);

    GridData labelData = new GridData();
    labelData.horizontalAlignment = GridData.FILL;
    newNameLabel.setLayoutData(labelData);
    
    newNameText = new Text(page1, SWT.SINGLE | SWT.BORDER);
    GridData newNameTextData = new GridData();
    newNameTextData.horizontalAlignment = GridData.FILL;
    newNameTextData.grabExcessHorizontalSpace = true;
    newNameTextData.widthHint = 30;
    newNameText.setLayoutData(newNameTextData);
    
    newNameText.addModifyListener(this);
    initNewNameTextField();
  }

  /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
   */
  public void createControl(Composite parent)
  {
    Composite base = new Composite(parent, SWT.NONE);
    WorkbenchHelp.setHelp(base, WSDLEditorPlugin.getWSDLString("_UI_HELP")); //$NON-NLS-1$
    base.setLayout(new GridLayout());

    Composite radioGroup = new Composite(base, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    radioGroup.setLayout(layout);

    GridData radioGroupData = new GridData();
    radioGroupData.verticalAlignment = GridData.FILL;
    radioGroupData.horizontalAlignment = GridData.FILL;
    radioGroup.setLayoutData(radioGroupData);
    
    createNewRadio = createRadioButton(radioGroup, createNewRadioString);
    selectExistingRadio = createRadioButton(radioGroup, selectExistingRadioString);
    importRadio = createRadioButton(radioGroup, importRadioString);
    selectExistingRadio.addSelectionListener(this);
    createNewRadio.addSelectionListener(this);
    importRadio.addSelectionListener(this);

    Label label = new Label(base, SWT.LEFT | SWT.HORIZONTAL | SWT.SEPARATOR);
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    data.horizontalSpan = 1;
    label.setLayoutData(data);

    pageBook = new PageBook(base, SWT.NONE);
    GridData dataPB = new GridData();
    dataPB.grabExcessHorizontalSpace = true;
    dataPB.grabExcessVerticalSpace = true;
    dataPB.horizontalAlignment = GridData.FILL;
    dataPB.verticalAlignment = GridData.FILL;
    pageBook.setLayoutData(dataPB);

    //  ---------------------------------------------------------------        
	  createPage1(pageBook);

    //  ---------------------------------------------------------------    
    page2 = new Composite(pageBook, SWT.NONE);

    GridLayout page2Layout = new GridLayout();
    page2Layout.numColumns = 1;
    page2.setLayout(page2Layout);

    GridData page2Data = new GridData();
    page2Data.verticalAlignment = GridData.FILL;
    page2Data.horizontalAlignment = GridData.FILL;
    page2.setLayoutData(page2Data);
    
    componentNameList = new List(page2, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
    GridData componentNameListData = new GridData();
    componentNameListData.horizontalAlignment = GridData.FILL;
    componentNameListData.verticalAlignment = GridData.FILL;
    componentNameListData.grabExcessHorizontalSpace = true;
    componentNameListData.grabExcessVerticalSpace = true;
    componentNameList.setLayoutData(componentNameListData);
    
    componentNameList.addSelectionListener(this);

    //  ---------------------------------------------------------------    

    page3 = new Composite(pageBook, SWT.NONE);

    GridLayout page3Layout = new GridLayout();
    page3Layout.numColumns = 1;
    page3.setLayout(page3Layout);

    GridData page3Data = new GridData();
    page3Data.verticalAlignment = GridData.FILL;
    page3Data.horizontalAlignment = GridData.FILL;
    page3.setLayoutData(page3Data);
    
    Composite fileComp = new Composite(page3, SWT.NONE);

    GridLayout fileCompLayout = new GridLayout();
    fileCompLayout.numColumns = 3;
    fileCompLayout.marginHeight = 0;
    fileCompLayout.marginWidth = 0;
    fileComp.setLayout(fileCompLayout);

    GridData fileCompData = new GridData();
    fileCompData.verticalAlignment = GridData.FILL;
    fileCompData.horizontalAlignment = GridData.FILL;
    fileComp.setLayoutData(fileCompData);

    Label fileLabel = new Label(fileComp, SWT.LEFT);
    fileLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_FILE")); //$NON-NLS-1$

    GridData fileLabelData = new GridData();
    fileLabelData.horizontalAlignment = GridData.FILL;
    fileLabel.setLayoutData(fileLabelData);
    
    
    fileText = new Text(fileComp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
    GridData dataFile = new GridData();
    dataFile.horizontalAlignment = GridData.FILL;
    dataFile.grabExcessHorizontalSpace = true;
    dataFile.widthHint = 30;
    fileText.setLayoutData(data);
    fileText.setText(""); //$NON-NLS-1$

    importButton = new Button(fileComp, SWT.PUSH);
    importButton.setText(WSDLEditorPlugin.getWSDLString("_UI_BUTTON_BROWSE")); //$NON-NLS-1$

    GridData importButtonData = new GridData();
    importButtonData.horizontalAlignment = GridData.FILL;
    importButton.setLayoutData(importButtonData);

    importButton.addSelectionListener(this);

    importList = new List(page3, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);

    GridData importListData = new GridData();
    importListData.horizontalAlignment = GridData.FILL;
    importListData.verticalAlignment = GridData.FILL;
    importListData.grabExcessHorizontalSpace = true;
    importListData.grabExcessVerticalSpace = true;
    importList.setLayoutData(importListData);
    
    importList.addSelectionListener(this);
    //  ---------------------------------------------------------------    

    pageBook.showPage(page1);
    choice = 1;
    setControl(base);
  }
  
  private Button createRadioButton(Composite parent, String label)
  {
    Button button = new Button(parent, SWT.RADIO);
    button.setText(label);

    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    button.setLayoutData(data);

    return button;
  }
  
  /**
   * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
   */
  public void modifyText(ModifyEvent e)
  {
    if (e.widget == newNameText)
    {
      newName = newNameText.getText();
      setPageComplete(isPageComplete());
    }
  }

  /**
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {
  }

  abstract protected void initExistingNameList();

  abstract protected void initNewNameTextField();

  public String getExistingListSelection()
  {
    return existingListSelection;
  }

  /**
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == createNewRadio)
    {
      pageBook.showPage(page1);
      choice = 1;
      setDescription(pageOneDescription);
      setPageComplete(isPageComplete());
      newNameText.setFocus();
      newNameText.selectAll();
    }
    else if (e.widget == selectExistingRadio)
    {
      pageBook.showPage(page2);
      choice = 2;

      Definition definition = ((WSDLElement)input).getEnclosingDefinition();
      // Need to resolve imports because the model doesn't automatically
      // do it for us.  See also OpenOnSelectionHelper
      Iterator imports = definition.getEImports().iterator();
      while (imports.hasNext())
      {
        Import theImport = (Import)imports.next();
        ((ImportImpl)theImport).importDefinitionOrSchema();
      }

      initExistingNameList();
      setDescription(pageTwoDescription);
      setPageComplete(isPageComplete());
      setErrorMessage(null);
    }
    else if (e.widget == importRadio)
    {
      pageBook.showPage(page3);
      choice = 3;
      setDescription(pageThreeDescription);
      setPageComplete(isPageComplete());
      setErrorMessage(null);
    }
    else if (e.widget == componentNameList)
    {
      existingListSelection = (componentNameList.getSelection())[0];
      setPageComplete(isPageComplete());
    }
    else if (e.widget == importList)
    {
      selection = importComponents.get(importList.getSelectionIndex());
      setPageComplete(isPageComplete());
    }
    else if (e.widget == importButton)
    {
      handleImport();
      if (importList.getItemCount() > 0)
      {
        importList.select(0);
        selection = importComponents.get(0);
      }
      else
      {
        selection = null;
      }
      setPageComplete(isPageComplete());
    }
  }

  protected abstract void handleImport();

  protected java.util.List loadFile(IFile wsdlFile, ResourceSet modelResourceSet)
  {
    java.util.List modelObjectList = Collections.EMPTY_LIST;
    try
    {
      if (wsdlFile != null)
      {
        URI uri = URI.createPlatformResourceURI(wsdlFile.getFullPath().toString());

        Object rootModelObject = null;
        if (uri.toString().endsWith("xsd"))
        {
          ResourceSet resourceSet = new ResourceSetImpl();
          Resource resource = resourceSet.getResource(uri, true);
          if (resource instanceof XSDResourceImpl)
          {
            rootModelObject = ((XSDResourceImpl) resource).getSchema();
          }
        }
        else
        {
          rootModelObject = WSDLResourceUtil.lookupAndLoadDefinition(modelResourceSet, uri.toString());
        }

        if (rootModelObject != null)
        {
          modelObjectList = new ArrayList(getModelObjects(rootModelObject));
        }
      }
      else
      {
        modelObjectList = new ArrayList(getModelObjects(null));
      }

    }
    catch (Exception e)
    {
    }
    return modelObjectList;
  }

  abstract public Collection getModelObjects(Object rootModelObject);

  public Object getSelection()
  {
    return selection;
  }

  public IFile getSelectedFile()
  {
    return selectedFile;
  }

  public String getRelativeLocationOfSelectedFile(boolean enableIEStyleLocation)
  {
    IFile currentWSDLFile = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
    return ComponentReferenceUtil.computeRelativeURI(selectedFile, currentWSDLFile, true);
  }

  public int getChoice()
  {
    return choice;
  }

  public String getNewName()
  {
    return newName;
  }

  public boolean isPageComplete()
  {
    if (selectExistingRadio.getSelection())
    {
      if (componentNameList.getSelectionCount() == 1)
      {
        return true;
      }
    }
    else if (createNewRadio.getSelection())
    {
      if (validateXMLName(newNameText.getText().trim()))
      {
        return true;
      }
    }
    else if (importRadio.getSelection())
    {
      if (importList.getSelectionCount() == 1)
      {
        return true;
      }
    }
    return false;
  }

  private boolean validateXMLName(String xmlName)
  {
    String errorMessage = ValidateHelper.checkXMLName(xmlName);

    if (errorMessage == null || errorMessage.length() == 0)
    {
      setErrorMessage(null);
      return true;
    }
    setErrorMessage(errorMessage);
    return false;
  }

  public String addWSDLImport(Definition definition, Element definitionElement, String namespaceURI)
  {
    return addWSDLImport(definition, definitionElement, namespaceURI, "wsdl");
  }

  public String addWSDLImport(Definition definition, Element definitionElement, String namespaceURI, String basePrefix)
  {
    AddElementDeclarationAction action = new AddElementDeclarationAction(definition, namespaceURI, basePrefix);
    action.run();
    String prefix = action.getPrefix();

    String location = getRelativeLocationOfSelectedFile(true);
    AddImportAction addImport = new AddImportAction(null, definition, definitionElement, definitionElement.getPrefix(), namespaceURI, location);
    addImport.run();

    return prefix;
  }
}
