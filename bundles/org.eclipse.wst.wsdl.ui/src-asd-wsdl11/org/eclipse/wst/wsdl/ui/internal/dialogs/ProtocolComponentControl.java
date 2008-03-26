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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.internal.generator.BaseGenerator;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.wizards.ContentGeneratorOptionsPage;

public abstract class ProtocolComponentControl extends Composite implements SelectionListener, ModifyListener
{
  protected BaseGenerator generator;

  protected Text componentNameField;
  protected Combo refNameCombo;
  protected Button refBrowseButton;
  protected Combo protocolCombo;
  protected Button overwriteButton;
  protected PageBook pageBook;
  protected Composite emptySettingsPage;
  protected Map pageMap = new HashMap();

  protected String name;
  protected String refName;

  protected String UNSPECIFIED = Messages._UI_UNSPECIFIED; //$NON-NLS-1$

  public ProtocolComponentControl(Composite parent, BaseGenerator generator, boolean showOverwriteButton)
  {
    super(parent, SWT.NONE);
    this.generator = generator;

    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    setLayout(layout);
    setLayoutData(createGridData(true, 1));

    Composite labelValueComposite = new Composite(this, SWT.NONE);
    labelValueComposite.setLayout(new GridLayout(2, false));
    labelValueComposite.setLayoutData(createGridData(false, 1));

    // row 1
    //
    Label componentNameLabel = new Label(labelValueComposite, SWT.NONE);
    componentNameLabel.setText(getComponentNameLabelText()); //$NON-NLS-1$

    componentNameField = new Text(labelValueComposite, SWT.BORDER);
    GridData gdName= new GridData();
    gdName.horizontalAlignment= GridData.FILL;
    gdName.grabExcessHorizontalSpace= true;
    componentNameField.setLayoutData(gdName);
    componentNameField.addModifyListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(componentNameField, ASDEditorCSHelpIds.PROTOCOL_COMPONENT_NAME_TEXT);
    //createPlaceHolder(labelValueComposite);
    
    // row 2
    //
    Label refNameLabel = new Label(labelValueComposite, SWT.NONE);
    refNameLabel.setText(getRefNameLabelText()); //$NON-NLS-1$	
    refNameCombo = new Combo(labelValueComposite, SWT.READ_ONLY);
    GridData gdRefName= new GridData();
    gdRefName.horizontalAlignment= GridData.FILL;
    gdRefName.grabExcessHorizontalSpace= true;
    refNameCombo.setLayoutData(gdRefName);
    refNameCombo.addSelectionListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(refNameCombo, ASDEditorCSHelpIds.PROTOCOL_COMPONENT_REF_COMBO);

    //refBrowseButton = new Button(labelValueComposite, SWT.NONE);
    //refBrowseButton.setText("  ...  ");
    //GridData gd = new GridData();
    //gd.grabExcessHorizontalSpace = false;
    //gd.heightHint = 17;
    //gd.widthHint = 24;
    //refBrowseButton.setLayoutData(gd);
    //refBrowseButton.addSelectionListener(this);
    //createPlaceHolder(labelValueComposite);

    // row 3
    //
    Label protocolLabel = new Label(labelValueComposite, SWT.NONE);
    protocolLabel.setText(Messages._UI_LABEL_BINDING_PROTOCOL); //$NON-NLS-1$
    protocolCombo = new Combo(labelValueComposite, SWT.READ_ONLY);
    GridData gdProtocol= new GridData();
    gdProtocol.horizontalAlignment= GridData.FILL;
    gdProtocol.grabExcessHorizontalSpace= true;
    protocolCombo.setLayoutData(gdProtocol);
    protocolCombo.addSelectionListener(this);
    PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolCombo, ASDEditorCSHelpIds.PROTOCOL_COMPONENT_PROTOCOL_COMBO);
    //createPlaceHolder(labelValueComposite);

    // optional overwrite button
    //
    if (showOverwriteButton)
    {
      overwriteButton = new Button(this, SWT.CHECK);
      overwriteButton.setText(Messages._UI_CHECKBOX_OVERWRITE); //$NON-NLS-1$
      overwriteButton.addSelectionListener(this);
      PlatformUI.getWorkbench().getHelpSystem().setHelp(overwriteButton, ASDEditorCSHelpIds.pROTOCOL_COMPONENT_OVERWRITE_CHECKBOX);
    }

    // protocol specific settings
    //
    pageBook = new PageBook(this, SWT.NONE);
    GridData gdFill= new GridData();
    gdFill.horizontalAlignment= GridData.FILL;
    gdFill.grabExcessHorizontalSpace= true;
    gdFill.verticalAlignment= GridData.FILL;
    gdFill.grabExcessVerticalSpace= true;
    pageBook.setLayoutData(gdFill);

    emptySettingsPage = new Composite(pageBook, SWT.NONE);
    for (int i = 0; i < 6; i++)
    {
      new Label(emptySettingsPage, SWT.NONE);
    }
  }

  public void initFields()
  {
    // prime the fields
    //
    componentNameField.setText(getDefaultName());
    updateRefNameCombo();
    updateProtocolCombo();
    updatePageBook(protocolCombo.getText());
  }

  public Text getComponentNameField()
  {
    return componentNameField;
  }

  public String getComponentNameLabelText()
  {
    return Messages._UI_LABEL_NAME; //$NON-NLS-1$
  }

  private GridData createGridData(boolean both, int span)
  {
    GridData gd = new GridData(both ? GridData.FILL_BOTH : GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = span;
    return gd;
  }

  // implements SelectionListener
  //
  public void widgetDefaultSelected(SelectionEvent event)
  {
  }

  public void widgetSelected(SelectionEvent event)
  {
    if (event.widget == overwriteButton && overwriteButton != null)
    {
      generator.setOverwrite(overwriteButton.getSelection());
    }
    else if (event.widget == refNameCombo)
    {
      int index = refNameCombo.getSelectionIndex();
      String refName = refNameCombo.getItem(index);
	  if (refName.equals(UNSPECIFIED)) {
	      generator.setRefName(""); //$NON-NLS-1$
	  }
	  else {
	      generator.setRefName(refName);
	  }
    }
    else if (event.widget == protocolCombo)
    {
        int index = protocolCombo.getSelectionIndex();
        String protocol = (index != -1) ? protocolCombo.getItem(index) : null;
        
        ContentGeneratorUIExtension ext = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForLabel(protocol);
        String namespace = ext.getNamespace();
  	    generator.setContentGenerator(BindingGenerator.getContentGenerator(namespace));
        updatePageBook(protocol);
    }
  }

  private static final String IS_OVERWRITE_APPLICABLE = "IS_OVERWRITE_APPLICABLE"; //$NON-NLS-1$
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
          Control control = page.createControl(pageBook);
          control.setData(IS_OVERWRITE_APPLICABLE, new Boolean(page.isOverwriteApplicable()));
          pageMap.put(protocol, page);
        }
      }

      boolean enableOverwriteButton = true;
      if (page != null)
      {
        if (overwriteButton != null)
        {
          Boolean data = (Boolean) page.getControl().getData(IS_OVERWRITE_APPLICABLE);
          enableOverwriteButton = data == null || data.equals(Boolean.TRUE);
        }
        pageBook.showPage(page.getControl());
        pageBook.layout();
        pageBook.getParent().layout();
		
		page.setOptionsOnGenerator();
      }
      else
      {
        pageBook.showPage(emptySettingsPage);
      }

      if (overwriteButton != null)
      {
        if (enableOverwriteButton)
        {
          overwriteButton.setEnabled(true);
          overwriteButton.setSelection(generator.getOverwrite());
        }
        else
        {
          overwriteButton.setEnabled(false);
          overwriteButton.setSelection(true);
        }
      }
    }
  }

  protected void updateProtocolCombo()
  {
    protocolCombo.removeAll();

    List list = new ArrayList();
    list.add(UNSPECIFIED);
    ContentGeneratorUIExtensionRegistry registry = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry(); 
    list.addAll(registry.getBindingExtensionNames());
	
    String protocolText = generator.getProtocol();
    ContentGeneratorUIExtension extt = registry.getExtensionForName(protocolText);
    if (extt != null)
    {
      protocolText = extt.getLabel();
    }

    for (Iterator i = list.iterator(); i.hasNext();)
    {
      String protocol = (String) i.next();
      ContentGeneratorUIExtension ext = registry.getExtensionForName(protocol);
      if (ext != null)
      {
        String label = ext.getLabel();
        if (label != null)
          protocolCombo.add(label);
      }
    }

    if (protocolText == null && protocolCombo.getItemCount() > 0)
    {
      protocolText = protocolCombo.getItem(0);
    }
    protocolCombo.setText(protocolText);
  }

  protected void updateRefNameCombo()
  {
    refNameCombo.removeAll();

    refNameCombo.add(UNSPECIFIED);
    for (Iterator i = getRefNames().iterator(); i.hasNext();)
    {
      refNameCombo.add((String) i.next());
    }

    if (refNameCombo.getItemCount() > 0)
    {
      String refText = generator.getRefName();
      refNameCombo.setText(refText != null ? refText : UNSPECIFIED);
    }
  }

  public void modifyText(ModifyEvent e)
  {
    generator.setName(componentNameField.getText());
  }

  public abstract List getRefNames();
  public abstract String getRefNameLabelText();
  public abstract String getDefaultName();
  // TODO: We don't need the String argument for createContentGeneratorOptionsPage() method.
  // Remove when WTP allows API changes.
  public abstract ContentGeneratorOptionsPage createContentGeneratorOptionsPage(String protocol);
}
