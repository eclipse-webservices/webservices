/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorCSHelpIds;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.dialogs.ProtocolComponentControl;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BindingWizard extends Wizard
{
  protected BindingGenerator bindingGenerator;
  protected BindingWizardOptionsPage specifyBindingPage;

  protected Document document;
  protected int kind;

  public static final int KIND_NEW_BINDING = 1;
  public static final int KIND_REGENERATE_BINDING = 2;

  protected Definition definition;
  
  /*
   * Constructor used when are creating a brand new Binding
   */
  public BindingWizard(Definition definition)
  {
    this(definition, null, KIND_NEW_BINDING);
  }

  /*
   * Constructor used when are creating a brand new Binding
   */
  public BindingWizard(Definition definition, Document document)
  {
    this(definition, null, KIND_NEW_BINDING);
    this.document = document;
  }
  
  /*
   * Constructor used when there is an existing Binding we wish to modify/regenerate
   */
  public BindingWizard(Definition definition, Binding binding, int kind)
  {
    super();
	this.definition = definition;
    this.kind = kind;	
	bindingGenerator = new BindingGenerator(definition, binding);

    setWindowTitle(Messages._UI_BINDING_WIZARD); //$NON-NLS-1$
    //setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditorPlugin.class, "icons/NewXML.gif"));
  }

  public void setBindingName(String bindingName)
  {
//    QName qname = new QName(definition.getTargetNamespace(), bindingName);
    bindingGenerator.setName(bindingName);
/*
 	// Binding binding = (Binding) definition.getBinding(qname);
	if (binding != null)
    {
	  List eeList = binding.getEExtensibilityElements();
      if (eeList.size() > 0)
      {
        ExtensibilityElement ee = (ExtensibilityElement) eeList.get(0);
        Element element = WSDLEditorUtil.getInstance().getElementForObject(ee);
        String namespace = element.getNamespaceURI();
        if (namespace != null)
        {
          ContentGeneratorExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorExtensionRegistry().getExtensionForNamespace(namespace);

          if (extension != null)
          {
            bindingGenerator.setProtocol(extension.getName());
          }
        }
      }
    }
    */
  }

  public void setPortTypeName(String portTypeName)
  {
    bindingGenerator.setRefName(portTypeName);
  }

  public BindingGenerator getBindingGenerator()
  {
    return bindingGenerator;
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
    specifyBindingPage = new BindingWizardOptionsPage();
    addPage(specifyBindingPage);
  }

  public boolean performFinish()
  {
  	boolean recordingStarted = false;
  	if (definition.getElement() == null || (document != null && document.getChildNodes().getLength() == 0)) {
  		recordingStarted = true;
  		beginRecording();
  		
  		// Create the Definitions element with proper namespace
  	    Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
  		String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
  	    if (charSet == null || charSet.trim().equals("")) //$NON-NLS-1$
  	    {
  	    	charSet = "UTF-8"; //$NON-NLS-1$
  	    }
  	    document.appendChild(document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"" + charSet + "\"")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  		Element root = document.createElement("wsdl:definitions");  		 //$NON-NLS-1$
  		document.appendChild(root);

  		// Add various namespace attributes here. 
  		root.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/wsdl/soap/"); //$NON-NLS-1$ //$NON-NLS-2$
  		root.setAttribute("xmlns:tns", getDefaultNamespace(definition)); //$NON-NLS-1$
  		root.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/"); //$NON-NLS-1$ //$NON-NLS-2$
  		root.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$ //$NON-NLS-2$
  		root.setAttribute("name", getFileName(definition)); //$NON-NLS-1$
  		root.setAttribute("targetNamespace", getDefaultNamespace(definition)); //$NON-NLS-1$

  		definition.setElement(root);	
  	}
  	
	// Generate/re-generate the Binding
    try
    {
      
    	// go ahead and add required namespaces first before generating binding content
    	CreateWSDLElementHelper.addRequiredNamespaces(bindingGenerator.getContentGenerator(), definition);
		Binding binding = bindingGenerator.generateBinding();
		
      if (binding != null)
      {
        IEditorPart editorPart = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        ISelectionProvider selectionProvider = (ISelectionProvider) editorPart.getAdapter(ISelectionProvider.class);
        if (selectionProvider != null)
        {
          selectionProvider.setSelection(new StructuredSelection(binding));
        }
      }
    }
    catch (Exception e)
    {
    }
    finally {
    	if (recordingStarted)
    		endRecording();
    }

    return true;
  }
  
  private String getDefaultNamespace(Definition definition)
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(WSDLEditorPlugin.DEFAULT_TARGET_NAMESPACE_ID); 
    if (!namespace.endsWith("/")) { //$NON-NLS-1$
    	namespace = namespace.concat("/"); //$NON-NLS-1$
    }
    
    namespace += getFileName(definition) + "/"; //$NON-NLS-1$

    return namespace;
  }
  
  private String getFileName(Definition definition) {
    String fileLocation = definition.getLocation();
  	IPath filePath = new Path(fileLocation);
  	return filePath.removeFileExtension().lastSegment().toString();
  }
  
  private void beginRecording() {
    Node node = document;
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().beginRecording(this, Messages._UI_ACTION_ADD_BINDING);   //$NON-NLS-1$
    }
  }
  
  private void endRecording() {
    Node node = document; 
    if (node instanceof IDOMNode)
    {
      ((IDOMNode)node).getModel().endRecording(this);  
    }
  }

  class BindingWizardOptionsPage extends WizardPage
  {
    protected ProtocolComponentControl protocolComponentControl;

    public BindingWizardOptionsPage()
    {
      super("SpecifyBindingPage"); //$NON-NLS-1$
      setTitle(Messages._UI_TITLE_SPECIFY_BINDING_DETAILS); //$NON-NLS-1$
      setDescription(Messages._UI_SPECIFY_BINDING_DETAILS_LABEL); //$NON-NLS-1$
    }

    protected BindingGenerator getBindingGenerator()
    {
      return ((BindingWizard) getWizard()).getBindingGenerator();
    }

    public void createControl(Composite parent)
    {
      ProtocolComponentControl protocolComponentControl = new BindingProtocolComponentControl(parent, bindingGenerator, true);
      protocolComponentControl.initFields();
      setControl(protocolComponentControl);
      PlatformUI.getWorkbench().getHelpSystem().setHelp(protocolComponentControl, ASDEditorCSHelpIds.BINDING_WIZARD);
    }
  }

  public static class BindingProtocolComponentControl extends ProtocolComponentControl
  {
    public BindingProtocolComponentControl(Composite parent, BindingGenerator generator)
    {
      this(parent, generator, true);
    }

    public BindingProtocolComponentControl(Composite parent, BindingGenerator generator, boolean showOverwriteButton)
    {
      super(parent, generator, showOverwriteButton);

      if (generator.getName() != null)
      {
        componentNameField.setEditable(false);
      }
    }

    public String getRefNameLabelText()
    {
      return Messages._UI_PORT_TYPE; //$NON-NLS-1$
    }

    public List getRefNames()
    {
      return new ComponentReferenceUtil(generator.getDefinition()).getPortTypeNames();
    }

    public String getDefaultName()
    {
      String bindingName = generator.getName();
      return bindingName != null ? bindingName : NameUtil.buildUniqueBindingName(generator.getDefinition(), null);
    }

    public ContentGeneratorOptionsPage createContentGeneratorOptionsPage(String protocol)
    {
      ContentGeneratorUIExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForLabel(protocol);
      if (extension != null) {
    	  return extension.getBindingContentGeneratorOptionsPage();
      }

      return null;
    }
  }
}
