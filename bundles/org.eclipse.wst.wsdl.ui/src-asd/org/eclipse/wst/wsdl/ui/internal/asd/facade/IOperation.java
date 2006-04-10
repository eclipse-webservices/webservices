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
package org.eclipse.wst.wsdl.ui.internal.asd.facade;

import java.util.List;

import org.eclipse.gef.commands.Command;

public interface IOperation extends INamedObject
{
	public List getMessages();
	public IInterface getOwnerInterface();
  
	public Command getAddInputCommand();
	public Command getAddOutputCommand();
  // TODO: rmah: We need to revisit this (Sending in an existing FaultImpl when we want to
  // add a Fault Parameter.  What do we really want to do when we add a
  // 'new Fault'.... This also relates to what we really want to do when we delete a Fault...
	public Command getAddFaultCommand(Object fault);
	public Command getReorderMessageReferencesCommand(IMessageReference leftSibling, IMessageReference rightSibling, IMessageReference movingMessageRef);
	public Command getDeleteCommand();
}
