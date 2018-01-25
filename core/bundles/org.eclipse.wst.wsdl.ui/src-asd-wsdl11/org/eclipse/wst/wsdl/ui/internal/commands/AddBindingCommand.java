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
package org.eclipse.wst.wsdl.ui.internal.commands;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;


public final class AddBindingCommand extends WSDLElementCommand
{
  private Definition definition;
  private String localName;
  private Binding binding;
 
  public AddBindingCommand
		(Definition definition,  
		 String localName)
	{
	  this.definition = definition;
	  this.localName = localName;
	}
  
  public WSDLElement getWSDLElement()
  {
    return binding;
  }

  public void run()
  {
    binding = WSDLFactory.eINSTANCE.createBinding();
    binding.setQName(new QName(definition.getTargetNamespace(),localName));
    binding.setEnclosingDefinition(definition);
    definition.addBinding(binding);
  }
}
