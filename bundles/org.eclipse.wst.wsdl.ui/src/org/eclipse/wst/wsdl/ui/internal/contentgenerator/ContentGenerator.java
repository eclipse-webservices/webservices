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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;  
        
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.w3c.dom.Element;



public interface ContentGenerator
{        
	
  public static String ADDRESS_LOCATION = "http://tempuri.org/";  
  public void init(Definition definition, Object generator, Object[] options);

  public String[] getRequiredNamespaces();
  
  public String[] getPreferredNamespacePrefixes();

  // generates the 'address' extensiblity element for a port
  //
  public void generatePortContent(Element portElement, Port port);
 
  public void generateBindingContent(Element bindingElement, PortType portType);

  public void generateBindingOperationContent(Element bindingOperationElement, Operation operation);
 
  public void generateBindingInputContent(Element bindingInputElement, Input input);    

  public void generateBindingOutputContent(Element bindingOutputElement, Output output);

  public void generateBindingFaultContent(Element bindingFaultElement, Fault fault);
}