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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.common.ui.UIPlugin;
import org.eclipse.wst.sse.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.sse.core.internal.contentmodel.CMDataType;
import org.eclipse.wst.sse.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.sse.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.sse.core.internal.contentmodel.CMGroup;
import org.eclipse.wst.sse.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.sse.core.internal.contentmodel.CMNode;
import org.eclipse.wst.sse.core.internal.contentmodel.CMNodeList;
import org.eclipse.wst.sse.core.internal.contentmodel.internal.util.DOMValidator;
import org.eclipse.wst.sse.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.sse.core.internal.contentmodel.util.CMVisitor;
import org.eclipse.wst.sse.core.internal.contentmodel.util.NamespaceInfo;
import org.eclipse.wst.sse.core.preferences.CommonModelPreferenceNames;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceImpl;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditor;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.commands.AddUnknownExtensibilityElementCommand;
import org.eclipse.wst.wsdl.ui.internal.contentgenerator.BindingGenerator;
import org.eclipse.wst.wsdl.ui.internal.util.ComponentReferenceUtil;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.xml.core.XMLModelPlugin;
import org.w3c.dom.Element;

public class NewWSDLWizard extends Wizard implements INewWizard
{
  private WSDLNewFilePage newFilePage;
  private WSDLNewFileOptionsPage optionsPage;  
  private IStructuredSelection selection;
  private IWorkbench workbench;
  
  /**
   * Constructor for NewWSDLWizard.
   */
  public NewWSDLWizard()
  {
    super();
  }
  /**
   * @see org.eclipse.jface.wizard.IWizard#performFinish()
   */
  public boolean performFinish()
  {
    IFile file = newFilePage.createNewFile();
    
    Preferences preference = XMLModelPlugin.getDefault().getPluginPreferences();
	String charSet = preference.getString(CommonModelPreferenceNames.OUTPUT_CODESET);
     if (charSet == null || charSet.trim().equals(""))
    {
    	charSet = "UTF-8";
    }
    
    String wsdlPrefix = "wsdl";
    Vector namespaces = optionsPage.getNamespaceInfo();
    
    String prefix = optionsPage.getPrefix();
    String definitionName = optionsPage.getDefinitionName();

    WSDLFactoryImpl factory = new WSDLFactoryImpl();
    DefinitionImpl definition = (DefinitionImpl) factory.createDefinition();
    
    definition.setTargetNamespace(optionsPage.getTargetNamespace());
    definition.setLocation(file.getLocation().toString());
    definition.setEncoding(charSet);											
    definition.setQName(new QName(wsdlPrefix, definitionName));
    definition.addNamespace(prefix, optionsPage.getTargetNamespace());
    
    for (int i=0; i<namespaces.size(); i++)
    {
        NamespaceInfo info = (NamespaceInfo)namespaces.get(i);

        if (info.prefix.length() > 0)
        {
        	definition.addNamespace(info.prefix, info.uri);
        }
        else
        {
        	definition.addNamespace(null, info.uri);
        }
    }
    
    try {
    	if (optionsPage.getCreateSkeletonBoolean()) {
    		if (optionsPage.isSoapDocLiteralProtocol()) {
        		CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.PART_INFO_ELEMENT_DECLARATION;
    		}
    		else {
    			CreateWSDLElementHelper.PART_TYPE_OR_DEFINITION = CreateWSDLElementHelper.PART_INFO_TYPE_DEFINITION;
    		}
    		
    		CreateWSDLElementHelper.serviceName = definitionName;
    		CreateWSDLElementHelper.portName = definitionName + optionsPage.getProtocol();
    		Service service = CreateWSDLElementHelper.createService(definition);
    		definition.updateElement(true);
    		
    		// Generate Binding
    		BindingGenerator bindingGenerator = new BindingGenerator(definition);
    		Port port = (Port) service.getEPorts().iterator().next();
    		bindingGenerator.setName(ComponentReferenceUtil.getName(port.getEBinding()));
    		bindingGenerator.setPortTypeName(ComponentReferenceUtil.getPortTypeReference(port.getEBinding()));
    		bindingGenerator.setProtocol(optionsPage.getProtocol());
    		bindingGenerator.setOverwrite(true);
    		bindingGenerator.setOptions(optionsPage.getProtocolOptions());
    		bindingGenerator.generate();
    		
    		// Generate address
    		String addressName = optionsPage.getProtocol().toLowerCase() + ":address";
    		Map table = new Hashtable(1);
    		String uri = WSDLEditorPlugin.getInstance().getPluginPreferences().getString(WSDLEditorPlugin.getWSDLString("_UI_PREF_PAGE_DEFAULT_TARGET_NAMESPACE"));
    		table.put("location", uri);
    	  	AddUnknownExtensibilityElementCommand addEECommand = new AddUnknownExtensibilityElementCommand(port, "", addressName, table);
    		addEECommand.run();
    		
        	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        	WSDLResourceImpl.serialize(outputStream, definition.getDocument(), charSet);
        	ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            file.setContents(inputStream,true,false,null);
    	}
    	else {
    		URI uri = URI.createPlatformResourceURI(file.getFullPath().toOSString());
    		definition.updateElement(true);
    	    ResourceSet resourceSet = new ResourceSetImpl();
    	    WSDLResourceImpl resource = (WSDLResourceImpl)resourceSet.createResource(URI.createURI("*.wsdl"));
    	    resource.setURI(uri);
    	    resource.getContents().add(definition);
    		resource.save(null);
    	}
    }
    catch (Exception e) {
    	System.out.println("\nCould not write new WSDL file in WSDL Wizard: " + e);
    }
 
/*
    if (file != null)
    {
      final ISelection selection = new StructuredSelection(file);
      if (selection != null)
      {
        IWorkbench workbench = UIPlugin.getDefault().getWorkbench();
        final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        final IWorkbenchPart focusPart = workbenchWindow.getActivePage().getActivePart();
        if (focusPart instanceof ISetSelectionTarget)
        {
          Display.getCurrent().asyncExec
          (new Runnable()
          {
            public void run()
            {
              ((ISetSelectionTarget)focusPart).selectReveal(selection);
            }
          });
        }
      }
    }
*/
    openEditor(file);
    
    return true;
  }  

  /**
   * @see org.eclipse.ui.IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.selection = selection;
    this.workbench = workbench;

// Need new icon
    this.setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(WSDLEditor.class, "icons/new_wsdl_wiz.gif"));
    this.setWindowTitle(WSDLEditorPlugin.getWSDLString("_UI_TITLE_NEW_WSDL_FILE")); //$NON-NLS-1$
  }

  public void addPages()
  {
    newFilePage = new WSDLNewFilePage(selection);
    optionsPage = new WSDLNewFileOptionsPage(WSDLEditorPlugin.getWSDLString("_UI_TITLE_OPTIONS"), WSDLEditorPlugin.getWSDLString("_UI_TITLE_OPTIONS"), null); //$NON-NLS-1$ //$NON-NLS-2$
    addPage(newFilePage);
    addPage(optionsPage);
  }

  public IPath getNewFilePath()
  {
  	String fileName = newFilePage.getFileName();
  	return fileName != null ? new Path(fileName) : null; 
  }

  public boolean canFinish()
  {
    if (newFilePage.isPageComplete() && optionsPage.isPageComplete())
    {
      return true;
    }
    return false;
  }
  
  static public void openEditor(final IFile iFile)
  {
    if (iFile != null)
    {
      IWorkbench workbench = UIPlugin.getDefault().getWorkbench();
      final IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();

      Display.getDefault().asyncExec
      (new Runnable()
          {
        public void run()
        {
          try
          {
            workbenchWindow.getActivePage().openEditor(new FileEditorInput(iFile), "org.eclipse.wst.wsdl.ui.internal.WSDLEditor");
          }
          catch (PartInitException ex)
          {
//            B2BGUIPlugin.getPlugin().getMsgLogger().write("Exception encountered when attempting to open file: " + iFile + "\n\n" + ex);
          }
        }
      });
    }
  }  

  public class AvailableContentCMVisitor extends CMVisitor
  {
    public static final int INCLUDE_ATTRIBUTES = ModelQuery.INCLUDE_ATTRIBUTES;
    public static final int INCLUDE_CHILD_NODES = ModelQuery.INCLUDE_CHILD_NODES;
    public static final int INCLUDE_SEQUENCE_GROUPS = ModelQuery.INCLUDE_SEQUENCE_GROUPS;
    
    public Hashtable childNodeTable = new Hashtable();
    public Hashtable attributeTable = new Hashtable();
    public Element rootElement;
    public CMElementDeclaration rootElementDeclaration; 
    public boolean isRootVisited;
    protected boolean includeSequenceGroups;
    public DOMValidator validator;

    public AvailableContentCMVisitor(Element rootElement, CMElementDeclaration rootElementDeclaration)
    {                                     
      this.rootElement = rootElement;
      this.rootElementDeclaration = rootElementDeclaration;
      validator = new DOMValidator();
    }

    protected String getKey(CMNode cmNode)
    {
      String key = cmNode.getNodeName();
      CMDocument cmDocument = (CMDocument)cmNode.getProperty("CMDocument");
      if (cmDocument != null)
      {                         
        String namespaceURI = (String)cmDocument.getProperty("http://org.eclipse.wst/cm/properties/targetNamespaceURI");   
        if (namespaceURI != null)
        {   
          key = "[" + namespaceURI + "]" + key;
        }
      }
      return key;
    }

    public List computeAvailableContent(int includeOptions)
    {                   
      Vector v = new Vector();  

      int contentType = rootElementDeclaration.getContentType();
      includeSequenceGroups = ((includeOptions & INCLUDE_SEQUENCE_GROUPS) != 0);
      visitCMNode(rootElementDeclaration);
      
      if ((includeOptions & INCLUDE_ATTRIBUTES) != 0)
      {
        v.addAll(attributeTable.values());
        CMAttributeDeclaration nillableAttribute = (CMAttributeDeclaration)rootElementDeclaration.getProperty("http://org.eclipse.wst/cm/properties/nillable");
        if (nillableAttribute != null)
        {
          v.add(nillableAttribute);
        }
      }  

      if ((includeOptions & INCLUDE_CHILD_NODES) != 0)
      {      
        if (contentType == CMElementDeclaration.MIXED ||
            contentType == CMElementDeclaration.ELEMENT)
        {
          v.addAll(childNodeTable.values());
        }
        else if (contentType == CMElementDeclaration.ANY)
        {      
          CMDocument cmDocument =  (CMDocument)rootElementDeclaration.getProperty("CMDocument");
          if (cmDocument != null)
          {
            CMNamedNodeMap elements = cmDocument.getElements();            
            for (Iterator i = elements.iterator(); i.hasNext(); )
            {
              v.add((CMElementDeclaration)i.next());
            } 
          }
        }
              
        if (contentType == CMElementDeclaration.MIXED ||
            contentType == CMElementDeclaration.PCDATA || 
            contentType == CMElementDeclaration.ANY)
        {
          CMDataType dataType = rootElementDeclaration.getDataType();
          if (dataType != null)
          {
            v.add(dataType);
          }                                       
        }
      }
      return v;
    }   
/*
    public void visitCMAnyElement(CMAnyElement anyElement)
    {            
      String uri = anyElement.getNamespaceURI();                          
      List list = getCMDocumentList(rootElement, rootElementDeclaration, uri);
      for (Iterator iterator = list.iterator(); iterator.hasNext(); )
      {
        CMDocument cmdocument = (CMDocument)iterator.next();
        if (cmdocument != null)
        {                          
          CMNamedNodeMap map = cmdocument.getElements();
          int size = map.getLength();
          for (int i = 0; i < size; i++)
          {                       
            CMNode ed = map.item(i);                  

            childNodeTable.put(getKey(ed), ed);
          }        
        }                
      }
    }
*/
    public void visitCMAttributeDeclaration(CMAttributeDeclaration ad)
    {
      super.visitCMAttributeDeclaration(ad);
      attributeTable.put(ad.getNodeName(), ad);
    }

    public void visitCMElementDeclaration(CMElementDeclaration ed)
    {
      if (ed == rootElementDeclaration && !isRootVisited)
      {
        isRootVisited = true;
        super.visitCMElementDeclaration(ed);
      }
      else
      {                                                                                  
        if (!Boolean.TRUE.equals(ed.getProperty("Abstract")))
        {
          childNodeTable.put(getKey(ed), ed);
        }

        CMNodeList substitutionGroup = (CMNodeList)ed.getProperty("SubstitutionGroup");
        if (substitutionGroup != null)
        {
          handleSubstitutionGroup(substitutionGroup);
        }
      }
    }                                              

    protected void handleSubstitutionGroup(CMNodeList substitutionGroup)
    {
      int substitutionGroupLength = substitutionGroup.getLength();
      if (substitutionGroupLength > 1)
      {
        for (int i = 0; i < substitutionGroupLength; i++)
        {
          CMNode ed = substitutionGroup.item(i);
          if (!Boolean.TRUE.equals(ed.getProperty("Abstract")))
          {
            childNodeTable.put(getKey(ed), ed);
          }
        }
      }
    }

    public void visitCMGroup(CMGroup group)
    {
      if (includeSequenceGroups)
      {
        if (group.getOperator() == CMGroup.SEQUENCE &&
            group.getChildNodes().getLength() > 1 &&
            includesRequiredContent(group))
        {                                        
          childNodeTable.put(group, group);
        }
      }  
      super.visitCMGroup(group);
    }   

    public boolean includesRequiredContent(CMGroup group)
    {
      List list = getValidator().createContentSpecificationList(group);
      return list.size() > 1;
    }
    
    public DOMValidator getValidator() {
    	return validator;
    }
  } /////////////////////////// here
}


