/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

// Definition must not have "types" element to run this command.
public final class AddTypesCommand extends WSDLElementCommand
{
  private Definition definition;
  private Types types;
  
  public AddTypesCommand(Definition definition)
  {
    this.definition = definition;
  }

  public void run()
  {
  	if ((types = definition.getETypes()) != null)
      return; // "types" already exists.
  	
    types = WSDLFactory.eINSTANCE.createTypes();
    types.setEnclosingDefinition(definition);
    definition.setTypes(types);
  }
  
  public WSDLElement getWSDLElement()
  {
    return types;
  }
}
