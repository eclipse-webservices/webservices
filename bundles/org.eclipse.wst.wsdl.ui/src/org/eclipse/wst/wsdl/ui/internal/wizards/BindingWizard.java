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

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.sse.core.internal.encoding.CommonEncodingPreferenceNames;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.AbstractGenerator;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ContentGeneratorExtension;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.ui.ContentGeneratorOptionsPage;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.ui.internal.widgets.ProtocolComponentControl;
import org.eclipse.wst.xml.core.document.DOMNode;
import org.eclipse.wst.xml.core.internal.XMLCorePlugin;
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

  /**
   * Constructor for BindingWizard.
   */
  public BindingWizard(Definition definition)
  {
    this(definition, KIND_NEW_BINDING);
  }

  public BindingWizard(Definition definition, Document document)
  {
    this(definition, KIND_NEW_BINDING);
    this.document = document;
  }
  
  public BindingWizard(Definition definition, int kind)
  {
    super();
    this.kind = kind;
    bindingGenerator = new BindingGenerator(definition);
    setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_BINDING_WIZARD")); //$NON-NLS-1$
    //setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditorPlugin.class, "icons/NewXML.gif"));
  }

  public void setBindingName(String bindingName)
  {
    bindingGenerator.setName(bindingName);

    Definition definition = bindingGenerator.getDefinition();
    QName qname = new QName(definition.getTargetNamespace(), bindingName);
    Binding binding = (Binding) definition.getBinding(qname);
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
  }

  public void setPortTypeName(String portTypeName)
  {
    bindingGenerator.setPortTypeName(portTypeName);
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
  	Definition definition = bindingGenerator.getDefinition();
  	boolean recordingStarted = false;
  	if (definition.getElement() == null || (document != null && document.getChildNodes().getLength() == 0)) {
  		recordingStarted = true;
  		beginRecording();
  		
  		// Create the Definitions element with proper namespace
  	    Preferences preference = XMLCorePlugin.getDefault().getPluginPreferences();
  		String charSet = preference.getString(CommonEncodingPreferenceNames.OUTPUT_CODESET);
  	    if (charSet == null || charSet.trim().equals(""))
  	    {
  	    	charSet = "UTF-8";
  	    }
  	    document.appendChild(document.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"" + charSet + "\""));
  		Element root = document.createElement("wsdl:definitions");  		
  		document.appendChild(root);

  		// Add various namespace attributes here. 
  		root.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/wsdl/soap/");
  		root.setAttribute("xmlns:tns", getDefaultNamespace(definition));
  		root.setAttribute("xmlns:wsdl", "http://schemas.xmlsoap.org/wsdl/");
  		root.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
  		root.setAttribute("name", getFileName(definition));
  		root.setAttribute("targetNamespace", getDefaultNamespace(definition));

  		definition.setElement(root);	
  	}
  	
    bindingGenerator.generate();

    try
    {
      Object object = bindingGenerator.getNewComponent();
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
    finally {
    	if (recordingStarted)
    		endRecording();
    }

    return true;
  }
  
  private String getDefaultNamespace(Definition definition)
  {
    String namespace = WSDLEditorPlugin.getInstance().getPreferenceStore().getString(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"));
    if (!namespace.endsWith("/")) {
    	namespace = namespace.concat("/");
    }
    
    namespace += getFileName(definition) + "/";

    return namespace;
  }
  
  private String getFileName(Definition definition) {
    String fileLocation = definition.getLocation();
  	IPath filePath = new Path(fileLocation);
  	return filePath.removeFileExtension().lastSegment().toString();
  }
  
  private void beginRecording() {
    Node node = document;
    if (node instanceof DOMNode)
    {
      ((DOMNode)node).getModel().beginRecording(this, WSDLEditorPlugin.getWSDLString("_UI_ACTION_ADD_BINDING"));  
    }
  }
  
  private void endRecording() {
    Node node = document; 
    if (node instanceof DOMNode)
    {
      ((DOMNode)node).getModel().endRecording(this);  
    }
  }

  class BindingWizardOptionsPage extends WizardPage
  {
    protected ProtocolComponentControl protocolComponentControl;

    public BindingWizardOptionsPage()
    {
      super("SpecifyBindingPage");
      setTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING_DETAILS"));
      setDescription(WSDLEditorPlugin.getWSDLString("_UI_SPECIFY_BINDING_DETAILS_LABEL"));
    }

    protected BindingGenerator getBindingGenerator()
    {
      return ((BindingWizard) getWizard()).getBindingGenerator();
    }

    public void createControl(Composite parent)
    {
      ProtocolComponentControl protocolComponentControl = new BindingProtocolComponentControl(parent, bindingGenerator, kind == KIND_REGENERATE_BINDING);
      protocolComponentControl.initFields();
      setControl(protocolComponentControl);
    }
  }

  public static class BindingProtocolComponentControl extends ProtocolComponentControl
  {
    public BindingProtocolComponentControl(Composite parent, AbstractGenerator generator)
    {
      this(parent, generator, false);
    }

    public BindingProtocolComponentControl(Composite parent, AbstractGenerator generator, boolean showOverwriteButton)
    {
      super(parent, generator, showOverwriteButton);

      if (generator.getName() != null)
      {
        componentNameField.setEditable(false);
      }
    }

    public String getRefNameLabelText()
    {
      return WSDLEditorPlugin.getWSDLString("_UI_PORT_TYPE");
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
      ContentGeneratorOptionsPage optionsPage = null;
      ContentGeneratorExtension extension = WSDLEditorPlugin.getInstance().getContentGeneratorExtensionRegistry().getContentGeneratorExtension(protocol);
      if (extension != null)
      {
        optionsPage = extension.createBindingContentGeneratorOptionsPage();
      }
      return optionsPage;
    }
  }
}
