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
package org.eclipse.wst.wsdl.ui.internal.viewers;

import java.util.Map;


import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.common.ui.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.model.ModelAdapterListener;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLModelAdapterFactory;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.util.XMLQuickScan;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class ImportViewer extends BaseViewer implements ModelAdapterListener
{
  protected Composite control;
  protected IEditorPart editorPart;
  
  protected Text namespaceText;
  protected Text prefixText;
  protected Text locationText;
  Button selectButton;

  /**
   * Constructor for ImportViewer.
   * @param parent
   * @param editorPart
   */
  public ImportViewer(Composite parent, IEditorPart editorPart)
  {
    super(getStatusLineManager(editorPart));
    this.editorPart = editorPart;
    createControl(parent);
  }
  /**
   * @see org.eclipse.wst.wsdl.ui.internal.viewers.NamedComponentViewer#getHeadingText()
   */
  protected String getHeadingText()
  {
    return WSDLEditorPlugin.getWSDLString("_UI_LABEL_IMPORT"); //$NON-NLS-1$
  }

  public void createControl(Composite parent)
  { 
    control = flatViewUtility.createComposite(parent, 1, true);                 
    GridLayout layout = (GridLayout)control.getLayout();
    layout.verticalSpacing = 0;
                                      
    flatViewUtility.createFlatPageHeader(control, getHeadingText());

    Composite c = flatViewUtility.createComposite(control, 1, true);

    Label bogus = flatViewUtility.createLabel(c, 0, "");
    GridData gd= new GridData();
    gd.horizontalAlignment= GridData.FILL;
    gd.grabExcessHorizontalSpace= true;
    
    bogus.setLayoutData(gd);

    Composite composite = flatViewUtility.createComposite(c, 2, true); 
    GridLayout layout2 = new GridLayout();
    layout2.makeColumnsEqualWidth  = false;
    layout2.numColumns = 3;
    layout2.marginHeight = 3;
    composite.setLayout(layout2);

    Label namespaceLabel = flatViewUtility.createLabel(composite, 0, WSDLEditorPlugin.getWSDLString("_UI_LABEL_NAMESPACE")); //$NON-NLS-1$
    namespaceText = flatViewUtility.createTextField(composite);
    namespaceText.setEnabled(false);

    // place holder
    Control con= flatViewUtility.createLabel(composite, 0, "");
    gd = new GridData();
    gd.grabExcessHorizontalSpace = false;
    con.setLayoutData(gd);

    Label prefixLabel = flatViewUtility.createLabel(composite, 0, WSDLEditorPlugin.getWSDLString("_UI_LABEL_PREFIX")); //$NON-NLS-1$
    prefixText = flatViewUtility.createTextField(composite);
    prefixText.addListener(SWT.Modify,this);
    prefixText.setEnabled(false);    

    // place holder
    Control con2= flatViewUtility.createLabel(composite, 0, "");
    gd = new GridData();
    gd.grabExcessHorizontalSpace = false;
    con2.setLayoutData(gd);

    Label locationLabel = flatViewUtility.createLabel(composite, 0, WSDLEditorPlugin.getWSDLString("_UI_LABEL_LOCATION")); //$NON-NLS-1$
    locationText = flatViewUtility.createTextField(composite);
    locationText.setEnabled(false);

    selectButton = flatViewUtility.createPushButton(composite, "...");
    gd = new GridData();
    gd.horizontalAlignment = GridData.BEGINNING;
    gd.grabExcessHorizontalSpace = false;
    selectButton.setLayoutData(gd);
    selectButton.addSelectionListener(this);
  }  

  public void doSetInput(Object input)
  {    
    setListenerEnabled(false);                          
    try
    {          
      Node node = WSDLEditorUtil.getInstance().getNodeForObject(input);
      update();
    }
    finally
    {
      setListenerEnabled(true);
    }

    if (oldInput != null)
    {
      WSDLModelAdapterFactory.getWSDLModelAdapterFactory().removeModelAdapterListener(oldInput, this);
    }

    WSDLModelAdapterFactory.getWSDLModelAdapterFactory().addModelAdapterListener(input, this);
    oldInput = input;
  }

  protected void handleEventHelper(Element element, Event event)
  {                        
    if (event.type == SWT.Modify)
    {  
    }
  }

  protected void update()
  {
    Element element = ((WSDLElement)input).getElement();                           
    flatViewUtility.updateFlatPageHeaderTitle(getHeadingText());
    prefixText.setText("");
    locationText.setText("");
    namespaceText.setText("                                                                  ");
    namespaceText.getParent().layout();    
    namespaceText.setText("");
    String locationValue = element.getAttribute("location");
    locationText.setText(locationValue != null ? locationValue : "");  

    String namespaceValue = element.getAttribute("namespace");
    namespaceText.setText(namespaceValue != null ? namespaceValue : "");
    
    String prefix = ((Import)input).getEnclosingDefinition().getPrefix(namespaceValue);
    prefixText.setText(prefix != null ? prefix : "");
  }

  public Control getControl()
  {
    return control;
  }

  /*
   * @see BaseWindow#doHandleEvent(Event)
   */
  public void doHandleEvent(Event event)
  {
//    if (event.widget == prefixText)
//    {
//      String newPrefix = prefixText.getText();
//      String errorMessage = ValidateHelper.checkXMLPrefix(newPrefix);
//    
//      if (errorMessage == null || errorMessage.length() == 0)
//      {
//        Import importObj = (Import)input;
//        org.w3c.dom.Element importElement = WSDLUtil.getInstance().getElementForObject(importObj);
//        
//        Definition definition = importObj.getEnclosingDefinition();
//        org.w3c.dom.Element definitionElement = WSDLUtil.getInstance().getElementForObject(definition);
//  
//        String nsFromModel = importObj.getNamespaceURI();
//        String nsFromDOM = importElement.getAttribute("namespace");
//        System.out.println(nsFromModel);
//        System.out.println(nsFromDOM);
//  
//        String oldPrefix = definition.getPrefix(nsFromModel);
//        definitionElement.removeAttribute("xmlns:" + oldPrefix);
//        definitionElement.setAttribute("xmlns:" + newPrefix, nsFromModel);
//      }
//    }
  }             

  public void propertyChanged(Object object, String property)
  {
    if (isListenerEnabled())
    {
      setListenerEnabled(false);
      update();
      setListenerEnabled(true);
    }
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {

  }

  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == selectButton)
    {
      ResourceSet resourceSet = null;
// TODO: port check
      resourceSet = ((org.eclipse.emf.ecore.EObject)input).eResource().getResourceSet();

      WSDLEditor editor = (WSDLEditor)editorPart;
      IFile currentWSDLFile = ((IFileEditorInput)editor.getEditorInput()).getFile();
      
      SelectSingleFileDialog dialog = new SelectSingleFileDialog(WSDLEditorPlugin.getShell(), null, true);
      String [] filters = { "xsd", "wsdl" };
      IFile [] excludedFiles = { currentWSDLFile };
      
      dialog.addFilterExtensions(filters, excludedFiles);
      dialog.create();
      dialog.getShell().setText(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT")); //$NON-NLS-1$
      dialog.setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SELECT_FILE")); //$NON-NLS-1$
      dialog.setMessage(WSDLEditorPlugin.getWSDLString("_UI_DESCRIPTION_SELECT_WSDL_OR_XSD")); //$NON-NLS-1$
      int rc = dialog.open();
      if (rc == IDialogConstants.OK_ID)
      {
        IFile selectedFile = dialog.getFile();
                
        //if (selectedFile.getLocation().toOSString().equals(currentWSDLFile.getLocation().toOSString()))
        //{
        //  System.out.println("SAME FILE:" + currentWSDLFile.getLocation());
        //}

        String location = ComponentReferenceUtil.computeRelativeURI(selectedFile, currentWSDLFile, true);

        Import importObj = (Import)input;
        org.w3c.dom.Element importElement = WSDLEditorUtil.getInstance().getElementForObject(importObj);
        Definition definition = importObj.getEnclosingDefinition();
        org.w3c.dom.Element definitionElement = WSDLEditorUtil.getInstance().getElementForObject(definition);
        
        String importTargetNamespace = "";
        String prefix = prefixText.getText();
        String uniquePrefix = "";
      
		URI uri = URI.createPlatformResourceURI(selectedFile.getFullPath().toString());      

		// note that the getTargetNamespaceURIForSchema works for both schema and wsdl files
		// I should change the name of this convenience method
        importTargetNamespace =  XMLQuickScan.getTargetNamespaceURIForSchema(uri.toString());

        if (prefix.trim().equals(""))
        {
          uniquePrefix = getUniquePrefix(definition, uri.fileExtension());
        }
        else
        {
          uniquePrefix = prefix; 
        }
       
        
        if (importTargetNamespace == null ||
           (importTargetNamespace != null && importTargetNamespace.trim().length() == 0))
        {
          return;  // what to do with no namespace docs?
        }

        importElement.setAttribute("location", location);
        importElement.setAttribute("namespace", importTargetNamespace);

        definitionElement.setAttribute("xmlns:" + uniquePrefix, importTargetNamespace);

        namespaceText.setText(importTargetNamespace);
        locationText.setText(location);
        prefixText.setText(uniquePrefix);
      }
    }
  }

  private String getUniquePrefix(Definition definition, String initPrefix)
  {
    String uniquePrefix;
    Map map = definition.getNamespaces();

    if (definition.getNamespace(initPrefix) == null)
    {
      uniquePrefix = initPrefix;
    }
    else // if used, then try to create a unique one
    {
      String tempPrefix = initPrefix;
      int i = 1;
      while(map.containsKey(tempPrefix + i))
      {
        i++;
      }
      uniquePrefix = tempPrefix + i;
    }
    return uniquePrefix;    
  } 
}
