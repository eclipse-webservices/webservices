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
package org.eclipse.wst.wsdl.ui.internal.graph.editparts;
                              
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.util.WSDLSwitch;
import org.eclipse.wst.wsdl.ui.internal.model.WSDLGroupObject;


public class WSDLEditPartFactory implements EditPartFactory
{ 
  protected static WSDLEditPartFactory instance;                            

  public static WSDLEditPartFactory getInstance()
  {
    if (instance == null)
    {
      instance = new WSDLEditPartFactory();
    }
    return instance;
  }
           


  public EditPart createEditPart(final EditPart parent, Object model)
  {             
      WSDLSwitch wsdlSwitch = new WSDLSwitch()
      {                   
      	public Object caseWSDLElement(WSDLElement wsdlElement)
        {   
          return new WSDLTreeNodeEditPart();
	    }   
       
      	public Object caseDefinition(Definition definition)
        {   
          return new DefinitionEditPart();
	    }  
		
		public Object caseExtensibilityElement(ExtensibilityElement object) 
		{
	       return new WSDLTreeNodeEditPart();
		}
      };
	 
	  
    EditPart editPart = null;                                
    if (model instanceof EObject)
    {
      editPart = (EditPart)wsdlSwitch.doSwitch((EObject)model);
    }
    else if (model instanceof WSDLGroupObject)
    {
      editPart = new GroupEditPart();
    }                                

    if (editPart != null)   
    {
      editPart.setModel(model);
      editPart.setParent(parent);
    }
    else
    {      
      System.out.println("can't create editPart for " + model);
      Thread.dumpStack();
    }
    return editPart;
  }
}