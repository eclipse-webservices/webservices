/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.ui.internal.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.wst.common.ui.internal.dialogs.SelectSingleFileDialog;
import org.eclipse.wst.wsi.ui.internal.Resource;
import org.eclipse.wst.wsi.ui.internal.WSIUIPlugin;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page that allows the user to specify the location of the 
 * WSDL document for the Web service.
 * 
 * @author David Lauzon, IBM
 * @author Lawrence Mandel, IBM
 */

public class ValidationWizardWSDLPage extends WizardPage implements SelectionListener
{

  IStructuredSelection selection;

  protected boolean wsdlinvalid = false;
  /**
   * Access to the includeWSDL flag.  
   */
  protected Button includeWSDLButton;

  /**
   * Access to the wsdl document group.
   */
  protected Group documentGroup;
  protected Text documentLocationText;
  protected Combo fileField;
  /**
   * Constructor.
   * 
   * @param selection: selection in the Resource Navigator view.
   */
  public ValidationWizardWSDLPage(IStructuredSelection selection)
  {
    super("ValidationWizardWSDLPage");
    this.selection = selection;
    this.setTitle(WSIUIPlugin.getResourceString("_UI_WIZARD_V_SELECT_WSDL_FILENAME_HEADING"));
    this.setDescription(WSIUIPlugin.getResourceString("_UI_WIZARD_V_SELECT_WSDL_FILENAME_EXPL"));
  }

  /**
   * Always returns true.
   */
  public boolean isPageComplete()
  {
    if (includeWSDLFile())
    {

      if (fileField.getText() == null || fileField.getText().equals("") || wsdlinvalid)
      {
        return false;
      }
    }
    return true;
  }

  /*
   * Creates the top level control for this page under the given parent
   * composite. Implementors are responsible for ensuring that the created
   * control can be accessed via getControl
   * 
   * @param parent - the parent composite
   */
  public void createControl(Composite parent)
  {
    Composite base = new Composite(parent, SWT.NONE);
    base.setLayout(new GridLayout());
    GridData data;

    // add includeWSDL flag
    includeWSDLButton = new Button(base, SWT.CHECK);
    includeWSDLButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_INCLUDE_WSDL_BUTTON"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    includeWSDLButton.setLayoutData(data);
    includeWSDLButton.addSelectionListener(this);
    includeWSDLButton.setSelection(false);

    // create a WSDL document group
    documentGroup = new Group(base, SWT.SHADOW_ETCHED_IN);
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    documentGroup.setLayoutData(data);
    GridLayout layout = new GridLayout();
    layout.numColumns = 1;
    documentGroup.setLayout(layout);
    documentGroup.setVisible(false);

    createDocumentGroupContents(documentGroup);

    setControl(base);
  }

  protected Control createDocumentGroupContents(Composite documentGroup)
  {
    //radio buttons' container
    Composite basePanel = new Composite(documentGroup, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.makeColumnsEqualWidth = false;
    layout.marginWidth = 0;
    basePanel.setLayout(layout);
    GridData data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    basePanel.setLayoutData(data);

    Composite group1 = new Composite(basePanel, SWT.NONE);
    group1.setFont(getFont());
    layout = new GridLayout();
    layout.numColumns = 1;
    group1.setLayout(layout);
    data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    group1.setLayoutData(data);

    Label documentLabel = new Label(group1, SWT.LEFT);
    documentLabel.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_INCLUDE_WSDL_LABEL"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    documentLabel.setLayoutData(data);
    /**
	 * Label label = new Label(basePanel, SWT.LEFT); data = new GridData();
	 * data.horizontalAlignment = GridData.FILL; data.horizontalSpan = 1;
	 * label.setLayoutData(data);
	 */
    fileField = new Combo(group1, SWT.DROP_DOWN);
    String[] wsdllocations = ((ValidationWizard) getWizard()).getWSDLLocations();
    if(wsdllocations != null)
    {
      int numlocs = wsdllocations.length;
      for(int i = 0; i < numlocs; i++)
      {
      	fileField.add(wsdllocations[i]);
      }
    }
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    data.grabExcessHorizontalSpace = true;
    data.verticalAlignment = GridData.FILL;
    data.grabExcessVerticalSpace = true;
    data.widthHint = 30;
    fileField.setLayoutData(data);
    fileField.addModifyListener(new FileFieldListener());

    Label wsdlLabel = new Label(group1, SWT.LEFT);
    wsdlLabel.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_VALID_WSDL_LABEL"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    documentLabel.setLayoutData(data);

    // documentLocationText = new Text(documentGroup, SWT.SINGLE | SWT.BORDER);
    // data = new GridData();
    // data.horizontalAlignment = GridData.FILL;
    // data.grabExcessHorizontalSpace = true;
    // data.widthHint = 30;
    // documentLocationText.setLayoutData(data);

    Composite group = new Composite(basePanel, SWT.NONE);
    group.setFont(getFont());
    layout = new GridLayout();
    layout.numColumns = 1;
    group.setLayout(layout);
    data = new GridData();
    data.verticalAlignment = GridData.FILL;
    data.horizontalAlignment = GridData.FILL;
    group.setLayoutData(data);

    Button workspaceButton = new Button(group, SWT.PUSH);
    workspaceButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_WORKBENCH_BUTTON"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    workspaceButton.setLayoutData(data);
    workspaceButton.addSelectionListener(new WorkspaceButtonListener());

    Button browseButton = new Button(group, SWT.PUSH);
    browseButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_BROWSE_BUTTON"));
    data = new GridData();
    data.horizontalAlignment = GridData.FILL;
    browseButton.setLayoutData(data);
    browseButton.addSelectionListener(new BrowseButtonListener());

    // Button searchUDDIButton = new Button(group, SWT.PUSH);
    // searchUDDIButton.setText(WSIUIPlugin.getResourceString("_UI_WIZARD_V_SEARCH_UDDI_BUTTON"));
    // data = new GridData();
    // data.horizontalAlignment = GridData.FILL;
    // searchUDDIButton.setLayoutData(data);
    // searchUDDIButton.addSelectionListener(new SearchUDDIButtonListener());

    return documentGroup;
  }

  public boolean includeWSDLFile()
  {
    return includeWSDLButton.getSelection();
  }

  public String getWSDLFile()
  {
    return fileField.getText();
  }

  /**
   * Always return true.
   */
  public boolean performFinish()
  {
    return true;
  }

  /**
   * Sent when default selection occurs in the control.
   * 
   * @param e -
   *            an event containing information about the selection
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {}

  /**
   * Handles the includeWSDL checkbox. Sent when selection occurs in the
   * control.
   * 
   * @param e -
   *            an event containing information about the selection
   */
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == includeWSDLButton)
    {
      if (includeWSDLButton.getSelection())
      {
        documentGroup.setVisible(true);
      }
      else
      {
        documentGroup.setVisible(false);
      }
      getContainer().updateButtons();
    }
  }

  public void setWSDLInvalid()
  {
    wsdlinvalid = true;
  }

  /**
   * Add value to the list box
   */
  class WorkspaceButtonListener implements SelectionListener
  {
    public void widgetDefaultSelected(SelectionEvent e)
    {}

    public void widgetSelected(SelectionEvent e)
    {
      SelectSingleFileDialog dialog = new SelectSingleFileDialog(getShell(), selection, false);
      dialog.addFilterExtensions(new String[] { ".wsdl" });
      dialog.create();
      dialog.getShell().setText(WSIUIPlugin.getResourceString("_UI_DIALOG_SS_FILE_SHELL_TEXT"));
      dialog.setTitle(WSIUIPlugin.getResourceString("_UI_DIALOG_SS_FILE_TITLE"));
      dialog.setMessage(WSIUIPlugin.getResourceString("_UI_DIALOG_SS_FILE_DESCRIPTION"));
      dialog.setTitleImage(WSIUIPlugin.getResourceImage(Resource.VALIDATE_WSI_LOGFILE_WIZ));
      
      int result = dialog.open();

      if (result == Window.OK)
      {
        IFile currentIFile = dialog.getFile();
        if (currentIFile != null)
        {
          fileField.add(currentIFile.getLocation().toString(), 0);
          fileField.select(0);
        }
      }
    }
  }

  /**
   * Add value to the list box
   */
  class BrowseButtonListener implements SelectionListener
  {
    public void widgetDefaultSelected(SelectionEvent e)
    {}

    public void widgetSelected(SelectionEvent e)
    {
      FileDialog dialog = new FileDialog(getShell());
      dialog.setFilterExtensions(new String[] { "*.wsdl" });
      String selection = dialog.open();
      if (selection != null)
      {
        fileField.add(selection, 0);
        fileField.select(0);
      }
    }
  }

  /**
   * Add value to the list box
   */
  // class SearchUDDIButtonListener implements SelectionListener
  // {
  //  public void widgetDefaultSelected(SelectionEvent e)
  //  {}
  //
  //  public void widgetSelected(SelectionEvent e)
  //  {
  //    String title = "Information";
  //    String message = "The chosen operation is not currently available.";
  //    MessageDialog.openInformation(Display.getDefault().getActiveShell(), title, message);
  //  }
  //}

  /**
   * Add value to the file field.
   */
  class FileFieldListener implements ModifyListener
  {

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
    public void modifyText(ModifyEvent e)
    {
      wsdlinvalid = false;
      getContainer().updateButtons();

    }

  }

}
