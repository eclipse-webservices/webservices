/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.impl.ImportImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class OpenOnSelectionHelper extends WSDLSwitch
{
  Definition definition;
  Attr attr = null;
  Element element = null;

  public OpenOnSelectionHelper(Definition definition)
  {
    this.definition = definition;
  }

  public void openEditor(EObject eObject)
  {
    String[] array = computeSpecification(eObject);
    if (array != null)
    {
      openEditor(array[0], array[1]);
    }
  }

  public void openEditor(Node node)
  {
    String[] array = computeSpecification(node);
    if (array != null)
    {
      openEditor(array[0], array[1]);
    }
  }

  protected void openEditor(String resource, String spec)
  {
    String pattern = "platform:/resource"; //$NON-NLS-1$
    if (resource != null && resource.startsWith(pattern))
    {
      try
      {
        Path path = new Path(resource.substring(pattern.length()));
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

        IWorkbenchPage workbenchPage = WSDLEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart editorPart = workbenchPage.getActiveEditor();
        
        if (editorPart.getEditorInput() instanceof IFileEditorInput &&
           ((IFileEditorInput)editorPart.getEditorInput()).getFile().equals(file))
        {  
        	workbenchPage.getNavigationHistory().markLocation(editorPart);
        }
        else
        {
          try
          {
            if (resource.endsWith("xsd")) //$NON-NLS-1$
            {
              editorPart = workbenchPage.openEditor(new FileEditorInput(file), WSDLEditorPlugin.XSD_EDITOR_ID); 
            }
            else
            {
              // Since we are already in the wsdleditor
			        editorPart =  workbenchPage.openEditor(new FileEditorInput(file), editorPart.getEditorSite().getId());
            }
          }
					catch (PartInitException initEx)
 					{
 					}
        }

        Class theClass = editorPart.getClass();
        Class[] methodArgs = { String.class };
        Method method = theClass.getMethod("openOnSelection", methodArgs); //$NON-NLS-1$
        Object args[] = { spec };
        method.invoke(editorPart, args);
        workbenchPage.getNavigationHistory().markLocation(editorPart);
      }
      catch (Exception e)
      {
      }
    }
  }

  public String[] computeSpecification(EObject eObject)
  {
    String[] result = null;
    Object referencedObject = doSwitch(eObject);
    if (referencedObject instanceof EObject)
    {
      EObject referencedEObject = (EObject)referencedObject;
      if (referencedEObject != eObject || 
         referencedEObject.eResource() != definition.eResource())
      {        
      
      Resource resource = referencedEObject.eResource();
      if (resource != null)
      {
        result = new String[2];
        result[0] = resource.getURI().toString();
        result[1] = resource.getURIFragment(referencedEObject);
      }
      }
    }
    return result;
  }

  public String[] computeSpecification(Node node)
  {
    String[] result = null;
    switch (node.getNodeType())
    {
      case Node.ELEMENT_NODE :
        {
          element = (Element)node;
          break;
        }
      case Node.ATTRIBUTE_NODE :
        {
          attr = (Attr)node;
          element = attr.getOwnerElement();
          break;
        }
      case Node.TEXT_NODE :
        {
          Node parent = node.getParentNode();
          element = (parent instanceof Element) ? (Element)parent : null;
          break;
        }
    }
    if (element != null)
    {
      Object object = WSDLEditorUtil.getInstance().findModelObjectForElement(definition, element);
      if (object instanceof EObject)
      {
        result = computeSpecification((EObject)object);
      }
    }
    return result;
  }

  public Object caseBinding(Binding binding)
  {
    Object result = binding;
    if (isMatchingAttribute(WSDLConstants.TYPE_ATTRIBUTE))
    {
      result = binding.getEPortType();
    }
    return result;
  }

  public Object caseDefinition(Definition definition)
  {
    return definition;
  }

  public Object casePart(Part part)
  {
    Object result = part;
    if (attr != null)
    {
      if (isMatchingAttribute(WSDLConstants.TYPE_ATTRIBUTE))
      {
        result = part.getTypeDefinition();
      }
      else if (isMatchingAttribute(WSDLConstants.ELEMENT_ATTRIBUTE))
      {
        result = part.getElementDeclaration();
      }
    }
    else if (part.getEnclosingDefinition() == definition)
    {
      result = part.getTypeDefinition() != null ? (Object)part.getTypeDefinition() : (Object)part.getElementDeclaration();
    }
    return result;
  }

  public Object casePort(Port port)
  {
    Object result = port;
    if (isMatchingAttribute(WSDLConstants.BINDING_ATTRIBUTE))
    {
      result = port.getEBinding();

    }
    return result;
  }

  public Object caseFault(Fault fault)
  {
    Object result = fault;
    if (isMatchingAttribute(WSDLConstants.MESSAGE_ATTRIBUTE))
    {
      result = fault.getMessage();
    }
    return result;
  }

  public Object caseInput(Input input)
  {
    Object result = input;
    if (isMatchingAttribute(WSDLConstants.MESSAGE_ATTRIBUTE))
    {
      result = input.getMessage();
    }
    return result;
  }

  public Object caseOutput(Output output)
  {
    Object result = output;

    if (isMatchingAttribute(WSDLConstants.MESSAGE_ATTRIBUTE))
    {
      result = output.getMessage();
    }
    return result;
  }

  public Object caseImport(Import theImport)
  {
		Object result = null;
		
		// For now, look at the file extension.  When bugzilla 118293 is fixed, look at
		// the Import's eDefinition and eSchema instead.
		Path importPath = new Path(theImport.getLocationURI());
		String extension = importPath.getFileExtension();
		if (extension.equalsIgnoreCase("xsd")) { //$NON-NLS-1$
			if (theImport.getESchema() == null) {
				((ImportImpl)theImport).importDefinitionOrSchema();
			}
			
			result = theImport.getESchema();
		}
		else if (extension.equalsIgnoreCase("wsdl")) { //$NON-NLS-1$
			if (theImport.getEDefinition() == null) {
				((ImportImpl)theImport).importDefinitionOrSchema();
			}
			
			result = theImport.getEDefinition();
		}
		
		
//		if (theImport.getESchema() != null) {
//			result = theImport.getESchema();
//		}
//		else if (theImport.getEDefinition() != null) {
//			result = theImport.getEDefinition();
//		}
//		else {
//	        // Need to resolve imports because the model doesn't automatically
//	        // do it for us
//	        ((ImportImpl)theImport).importDefinitionOrSchema();
//	        result = theImport.getESchema();
//	    }
		
		return result;
  }

  public Object caseWSDLElement(WSDLElement wsdlElement)
  {
    return wsdlElement;
  }

  private boolean isMatchingAttribute(String value)
  {
    return attr != null && value.equals(attr.getName());
  }

  public Object defaultCase(EObject object)
  {
    EClass theEClass = object.eClass();
    if (theEClass.eContainer() == XSDPackage.eINSTANCE)
    {
      Object result = object;
      switch (theEClass.getClassifierID())
      {
        case XSDPackage.XSD_ELEMENT_DECLARATION:
          if (isMatchingAttribute(XSDConstants.REF_ATTRIBUTE))
          {
            result = ((XSDElementDeclaration)object).getResolvedElementDeclaration();
          }
          return result;     
        case XSDPackage.XSD_COMPLEX_TYPE_DEFINITION:
          if (isMatchingAttribute(XSDConstants.BASE_ATTRIBUTE))
          {
            result = ((XSDComplexTypeDefinition)object).getBaseTypeDefinition();
          }
          return result;          
      }
    }
    return super.defaultCase(object);
  }

}
