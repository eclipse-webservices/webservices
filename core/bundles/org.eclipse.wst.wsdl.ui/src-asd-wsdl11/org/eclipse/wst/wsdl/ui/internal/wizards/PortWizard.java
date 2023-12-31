/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.generator.PortGenerator;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.dialogs.ProtocolComponentControl;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
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
    setWindowTitle(Messages._UI_PORT_WIZARD); //$NON-NLS-1$
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
	// go ahead and add required namespaces first before generating port
	CreateWSDLElementHelper.addRequiredNamespaces(portGenerator.getContentGenerator(), portGenerator.getDefinition());
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
      super("SpecifyPortPage"); //$NON-NLS-1$
      setTitle(Messages._UI_SPECIFY_PORT_DETAILS); //$NON-NLS-1$
      setDescription(Messages._UI_SPECIFY_PORT_DETAILS_TO_BE_CREATED); //$NON-NLS-1$
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
      PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolComponentControl, ASDEditorCSHelpIds.PORT_WIZARD);
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
      return Messages._UI_BINDING; //$NON-NLS-1$
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
      ContentGeneratorUIExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForLabel(protocol);
      if (extension != null) {
        return extension.getPortContentGeneratorOptionsPage();
      }
      
      return null;
    }
  }
}