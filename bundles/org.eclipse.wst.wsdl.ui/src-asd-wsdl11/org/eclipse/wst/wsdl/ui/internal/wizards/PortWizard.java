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

import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.generator.PortGenerator;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.dialogs.ProtocolComponentControl;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class PortWizard extends Wizard
{
  protected final static int STYLE_NEW_BINDING = 1;
  protected final static int STYLE_EXISTING_BINDING = 1 << 1;
  protected final static int STYLE_DEFAULT = STYLE_NEW_BINDING | STYLE_EXISTING_BINDING;

  protected PortGenerator portGenerator;
  protected PortWizardOptionsPage specifyBindingPage;
  protected int style;

  /**
   * Constructor for PortWizard.
   */
  public PortWizard(Service service)
  {
    this(service, 0);
  }

  public PortWizard(Service service, int style)
  {
    super();
    portGenerator = new PortGenerator(service);
    setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_PORT_WIZARD")); //$NON-NLS-1$
    //setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditorPlugin.class, "icons/NewXML.gif"));
  }

  /**
   * Return true if wizard setup is successful, false otherwise
   */
  public boolean setup()
  {
    return true;
  }

  public void addPages()
  {
    specifyBindingPage = new PortWizardOptionsPage(style);
    addPage(specifyBindingPage);
  }

  public boolean performFinish()
  {
    Object object = portGenerator.generatePort();

    try
    {
      if (object != null)
      {
        IEditorPart editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        ISelectionProvider selectionProvider = (ISelectionProvider) editorPart.getAdapter(ISelectionProvider.class);
        if (selectionProvider != null)
        {
          selectionProvider.setSelection(new StructuredSelection(object));
        }
      }
    }
    catch (Exception e)
    {
    }
    return true;
  }

  class PortWizardOptionsPage extends WizardPage
  {

    protected ProtocolComponentControl protocolComponentControl;

    public PortWizardOptionsPage(int style)
    {
      super("SpecifyPortPage");
      setTitle(WSDLEditorPlugin.getWSDLString("_UI_SPECIFY_PORT_DETAILS"));
      setDescription(WSDLEditorPlugin.getWSDLString("_UI_SPECIFY_PORT_DETAILS_TO_BE_CREATED"));
    }

    public PortGenerator getGenerator()
    {
      return portGenerator;
    }

    public void createControl(Composite parent)
    {
      ProtocolComponentControl protocolComponentControl = new PortProtocolComponentControl(parent, portGenerator);
      protocolComponentControl.initFields();
      setControl(protocolComponentControl);
    }
  }

  class PortProtocolComponentControl extends ProtocolComponentControl
  {

    public PortProtocolComponentControl(Composite parent, PortGenerator generator)
    {
      super(parent, generator, false);
    }

    public String getRefNameLabelText()
    {
      return WSDLEditorPlugin.getWSDLString("_UI_BINDING");
    }

    public List getRefNames()
    {
      return new ComponentReferenceUtil(portGenerator.getDefinition()).getBindingNames();
    }

    public String getDefaultName()
    {
      Service service = portGenerator.getService();
      return NameUtil.buildUniquePortName(service, null);
    }

    public ContentGeneratorOptionsPage createContentGeneratorOptionsPage(String protocol)
    {
      ContentGeneratorOptionsPage optionsPage = null;	  
	  String protocolSelection = protocolCombo.getItem(protocolCombo.getSelectionIndex());
	  if (protocolSelection.equals("SOAP")) {
		  optionsPage = new SoapBindingOptionsPage();
	  }
	  else if (protocolSelection.equals("HTTP")) {
		  optionsPage = new SoapBindingOptionsPage();
	  }	  
//      ContentGeneratorExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorExtensionRegistry().getContentGeneratorExtension(protocol);
//      if (extension != null)
//      {
//        optionsPage = extension.createPortContentGeneratorOptionsPage();
//      }
      return optionsPage;
    }
  }
}