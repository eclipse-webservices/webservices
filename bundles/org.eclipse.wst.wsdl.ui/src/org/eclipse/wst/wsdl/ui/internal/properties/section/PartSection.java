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
package org.eclipse.wst.wsdl.ui.internal.properties.section;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.wst.common.ui.properties.ITabbedPropertyConstants;
import org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.dialogs.InvokeSetDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLComponentSelectionDialog;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLComponentSelectionProvider;
import org.eclipse.wst.wsdl.ui.internal.dialogs.types.WSDLSetComponentHelper;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.xsd.ui.internal.dialogs.types.xml.XMLComponentSpecification;


public class PartSection extends AbstractSection
{
  CCombo componentNameCombo;
	CCombo referenceKindCombo;
  CLabel componentNameLabel;
  Button button;
  IEditorPart editorPart;

	public void handleEvent (Event event)
	{
    if (event.type == SWT.Modify)
    {  
      if (event.widget == referenceKindCombo)
      {
        boolean isType = referenceKindCombo.getText().equals(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE")); //$NON-NLS-1$
        ComponentReferenceUtil.setComponentReference((Part)getElement(), isType, null);
        refresh();
      } 
    }

	}

	/**
	 * @see org.eclipse.wst.common.ui.properties.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite, org.eclipse.wst.common.ui.properties.TabbedPropertySheetWidgetFactory)
	 */
	public void createControls(Composite parent, TabbedPropertySheetWidgetFactory factory)
	{
		super.createControls(parent, factory);
		composite =	getWidgetFactory().createFlatFormComposite(parent);

		FormData data;

		referenceKindCombo = getWidgetFactory().createCCombo(composite);
		referenceKindCombo.setBackground(composite.getBackground());
		referenceKindCombo.add(WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT")); //$NON-NLS-1$
		referenceKindCombo.add(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE")); //$NON-NLS-1$

		CLabel valueLabel = getWidgetFactory().createCLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_REFERENCE_KIND")); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(referenceKindCombo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(referenceKindCombo, 0, SWT.CENTER);
		valueLabel.setLayoutData(data);

    componentNameCombo = getWidgetFactory().createCCombo(composite);
    componentNameCombo.setBackground(composite.getBackground());
    componentNameCombo.addListener(SWT.Modify, this);
    componentNameCombo.addSelectionListener(this);

    componentNameLabel = getWidgetFactory().createCLabel(composite, WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT") + ":"); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(componentNameCombo, -ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(componentNameCombo, 0, SWT.CENTER);
		componentNameLabel.setLayoutData(data);
		
    button = getWidgetFactory().createButton(composite, "", SWT.PUSH); //$NON-NLS-1$
    button.setImage(WSDLEditorPlugin.getInstance().getImage("icons/browsebutton.gif")); //$NON-NLS-1$

    button.addSelectionListener(this);
		data = new FormData();
		data.left = new FormAttachment(100, -rightMarginSpace + 2);
		data.right = new FormAttachment(100, 0);
		data.top = new FormAttachment(componentNameCombo, 0, SWT.CENTER);
		button.setLayoutData(data);
		
    data = new FormData();
    data.left = new FormAttachment(0, 100);
    data.right = new FormAttachment(100, -rightMarginSpace - ITabbedPropertyConstants.HSPACE);
    data.top = new FormAttachment(0, 0);
    referenceKindCombo.setLayoutData(data);
    
    data = new FormData();
    data.left = new FormAttachment(0, 100);
    data.right = new FormAttachment(button, 0);
    data.top = new FormAttachment(referenceKindCombo, +ITabbedPropertyConstants.VSPACE);
    componentNameCombo.setLayoutData(data);

    referenceKindCombo.addListener(SWT.Modify, this);
	}

	/*
	 * @see org.eclipse.wst.common.ui.properties.view.ITabbedPropertySection#refresh()
	 */
	public void refresh()
	{
    super.refresh();
	  referenceKindCombo.removeListener(SWT.Modify, this);
		if (((Part)getElement()).getTypeDefinition() != null)
		{
		  referenceKindCombo.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE")); //$NON-NLS-1$
		}
		else
		{
		  referenceKindCombo.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT")); //$NON-NLS-1$
		}
		
    try
    {
      Part part = (Part)getElement();
      boolean isType = ComponentReferenceUtil.isType(part);
      String value = ComponentReferenceUtil.getPartComponentReference(part);

      // update the combo-box content
      //
      componentNameCombo.removeAll(); 
      List compList = ComponentReferenceUtil.getComponentNameList(part, isType);
      if (compList != null)
      {
        for (Iterator iterator =  compList.iterator(); iterator.hasNext();)
        {
          componentNameCombo.add((String)iterator.next());
        }
      } 
      
      if (isType)
      {                                                 
        referenceKindCombo.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE")); //$NON-NLS-1$
        componentNameLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE") + ":");  //$NON-NLS-1$
        componentNameCombo.setText(value != null ? value : ""); //$NON-NLS-1$
//        button.setReferenceKind("type");
      }
      else
      {
        referenceKindCombo.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT"));  //$NON-NLS-1$
        componentNameLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_ELEMENT") + ":");  //$NON-NLS-1$
        componentNameCombo.setText(value != null ? value : ""); //$NON-NLS-1$
        
//        button.setReferenceKind("element");
      }                                                        
//      button.setInput(input);
//      button.setEditor(editorPart);
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
  		referenceKindCombo.addListener(SWT.Modify, this);
    }
	}

  /**
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    Part part = (Part)getElement();
    boolean isType = referenceKindCombo.getText().equals(WSDLEditorPlugin.getWSDLString("_UI_LABEL_TYPE")); //$NON-NLS-1$
    if (e.widget == componentNameCombo)
    {                                                      
      ComponentReferenceUtil.setComponentReference(part, isType, componentNameCombo.getText());
      refresh();
    }
    else if (e.widget == button)
    {
      if (e.widget == button)
      {
          Shell shell = Display.getCurrent().getActiveShell();
          IFile iFile = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
          Definition definition = ((WSDLElement) getElement()).getEnclosingDefinition();
          String dialogTitle = "";
          String property = "";

          WSDLComponentSelectionDialog dialog = null;
          WSDLComponentSelectionProvider provider = null; 
          
          List validExtensions = new ArrayList(2);
          validExtensions.add("wsdl");
          validExtensions.add("xsd");
          List lookupPaths = new ArrayList(4);
          if (isType) {
              lookupPaths.add("/definitions/types/schema/simpleType");
              lookupPaths.add("/definitions/types/schema/complexType");
              lookupPaths.add("/schema/complexType");
              lookupPaths.add("/schema/simpleType");
              property = ""; 
              dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_TYPE");
              
              provider = new WSDLComponentSelectionProvider(iFile, definition, lookupPaths, validExtensions);
              dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, "type", provider);
          }
          else {
              lookupPaths.add("/definitions/types/schema/element");
              lookupPaths.add("/schema/element");
              property = ""; 
              dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_ELEMENT");
              
              provider = new WSDLComponentSelectionProvider(iFile, definition, lookupPaths, validExtensions);
              dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, "element", provider);
          }
          provider.setDialog(dialog);


          dialog.setBlockOnOpen(true);
          dialog.create();

          if (dialog.open() == Window.OK) {
              XMLComponentSpecification spec = dialog.getSelection();
              
              WSDLSetComponentHelper helper = new WSDLSetComponentHelper(iFile, definition);
//              helper.setComponent(getElement(), property, dialog.getSelection());
          }

          
          
          
//        InvokeSetDialog dialog = new InvokeSetDialog();
//        
//        if (getElement() instanceof Part)
//        {
//          dialog.setReferenceKind(referenceKindCombo.getText());
//        }
//        dialog.run(getElement(), editorPart);
        refresh();
      }

    }

  }
  
  public void setEditorPart(IEditorPart editorPart)
  {
    this.editorPart = editorPart;
  }
}
