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
package org.eclipse.wst.wsdl.ui.internal.asd.facade;

import java.util.List;

import org.eclipse.gef.commands.Command;


public interface IDescription extends INamedObject
{
	public String getTargetNamespace();
	public String getTargetNamespacePrefix();
//	public List getNamespacesInfo();	// TODO: Add this method in the future
	
	public List getImports();
	public List getTypes();
	public List getServices();
	public List getBindings();
	public List getInterfaces();
	public List getMessages();
  
	public Command getAddImportCommand();
	public Command getAddSchemaCommand();
	public Command getAddServiceCommand();
	public Command getAddBindingCommand();
	public Command getAddInterfaceCommand();
	public Command getAddMessageCommand();
//	public Command getEditNamespacesCommand();	// TODO: Add this method in the future
}
