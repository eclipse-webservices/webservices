/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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

public interface IBinding extends INamedObject
{
	public IInterface getInterface();
	public List getBindingOperations();
	public List getBindingContentList();
	public List getExtensiblityObjects();
	public String getProtocol();  
	public IDescription getOwnerDescription();
  
	public Command getSetInterfaceCommand(IInterface newInterface);
	public Command getGenerateBindingCommand();
}
