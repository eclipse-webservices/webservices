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
package org.eclipse.wst.wsdl.ui.internal.widgets;

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
import org.eclipse.ui.part.PageBook;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.AbstractGenerator;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui.ContentGeneratorOptionsPage;

public abstract class ProtocolComponentControl extends Composite implements SelectionListener, ModifyListener
{
  protected AbstractGenerator generator;

  protected Text componentNameField;
  protected Combo refNameCombo;
  protected Button refBrowseButton;
  protected Combo protocolCombo;
  protected Button overwriteButton;
  protected PageBook pageBook;
  protected Composite emptySettingsPage;
  protected Map pageMap = new HashMap();

  //protected boolean isBindingComponent = true;
  //protected boolean isNewComponent = true;
  protected String name;
  protected String refName;

  protected String UNSPECIFIED = WSDLEditorPlugin.getWSDLString("_UI_UNSPECIFIED");
  //protected BindingGenerator bindingGenerator;

  public ProtocolComponentControl(Composite parent, AbstractGenerator generator, boolean showOverwriteButton)
  {
    super(parent, SWT.NONE);
    this.generator = generator;
    //this.isBindingComponent = isBindingComponent;

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
    protocolLabel.setText(WSDLEditorPlugin.getWSDLString("_UI_LABEL_BINDING_PROTOCOL")); //$NON-NLS-1$
    protocolCombo = new Combo(labelValueComposite, SWT.READ_ONLY);
    GridData gdProtocol= new GridData();
    gdProtocol.horizontalAlignment= GridData.FILL;
    gdProtocol.grabExcessHorizontalSpace= true;
    protocolCombo.setLayoutData(gdProtocol);
    protocolCombo.addSelectionListener(this);
    //createPlaceHolder(labelValueComposite);

    // optional overwrite button
    //
    if (showOverwriteButton)
    {
      overwriteButton = new Button(this, SWT.CHECK);
      overwriteButton.setText(WSDLEditorPlugin.getWSDLString("_UI_CHECKBOX_OVERWRITE")); //$NON-NLS-1$
      overwriteButton.addSelectionListener(this);
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
      Label placeHolder = new Label(emptySettingsPage, SWT.NONE);
    }

    updatePageBook(generator.getProtocol());
  }

  public void initFields()
  {
    // prime the fields
    //
    componentNameField.setText(getDefaultName());
    updateRefNameCombo();
    updateProtocolCombo();
  }

  public Text getComponentNameField()
  {
    return componentNameField;
  }

  public String getComponentNameLabelText()
  {
    return "Name";
  }

  private GridData createGridData(boolean both, int span)
  {
    GridData gd = new GridData(both ? GridData.FILL_BOTH : GridData.FILL_HORIZONTAL);
    gd.horizontalSpan = span;
    return gd;
  }

  private Control createPlaceHolder(Composite parent)
  {
    Label label = new Label(parent, SWT.NONE);
    return label;
  }
  public AbstractGenerator getGenerator()
  {
    return generator;
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
      getGenerator().setOverwrite(overwriteButton.getSelection());
    }
    else if (event.widget == refNameCombo)
    {
      int index = refNameCombo.getSelectionIndex();
      String refName = refNameCombo.getItem(index);
      getGenerator().setRefName(refName);
    }
    else if (event.widget == protocolCombo)
    {
      int index = protocolCombo.getSelectionIndex();
      String protocol = (index != -1) ? protocolCombo.getItem(index) : null;
      getGenerator().setProtocol(protocol);
      getGenerator().setOptions(null);
      updatePageBook(protocol);
    }
  }

  private static final String IS_OVERWRITE_APPLICABLE = "IS_OVERWRITE_APPLICABLE";
  protected void updatePageBook(String protocol)
  {
    if (protocol != null)
    {
      Control control = (Control) pageMap.get(protocol);
      if (control == null)
      {
        ContentGeneratorOptionsPage optionsPage = createContentGeneratorOptionsPage(protocol);

        if (optionsPage != null)
        {
          optionsPage.init(getGenerator());
          control = optionsPage.createControl(pageBook);
          control.setData(IS_OVERWRITE_APPLICABLE, new Boolean(optionsPage.isOverwriteApplicable()));
          pageMap.put(protocol, control);
        }

        if (control != null)
        {
          pageMap.put(protocol, control);
        }
      }

      boolean enableOverwriteButton = true;
      if (control != null)
      {
        if (overwriteButton != null)
        {
          Boolean data = (Boolean) control.getData(IS_OVERWRITE_APPLICABLE);
          enableOverwriteButton = data == null || data.equals(Boolean.TRUE);
        }
        pageBook.showPage(control);
        pageBook.layout();
        pageBook.getParent().layout();
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
    list.addAll(WSDLEditorPlugin.getInstance().getContentGeneratorExtensionRegistry().getBindingExtensionNames());

    String protocolText = generator.getProtocol();

    for (Iterator i = list.iterator(); i.hasNext();)
    {
      String protocol = (String) i.next();
      protocolCombo.add(protocol);
    }

    if (protocolText == null && protocolCombo.getItemCount() > 0)
    {
      protocolText = protocolCombo.getItem(0);
      generator.setProtocol(!UNSPECIFIED.equals(protocolText) ? protocolText : null);
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
  public abstract ContentGeneratorOptionsPage createContentGeneratorOptionsPage(String protocol);
}
