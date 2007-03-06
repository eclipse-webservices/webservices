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

import org.eclipse.gef.commands.Command;


// this class is used to describe in simplified descritpion of the input/output/fault structure
// in the familiar style of a typical programming language parameter e.g. "String[] foo"
// 
// often this class represents a simplification of a more complex service description or schema
// this class serves common denominator to drive a simple view of an operation's message structure
// implementation specific editors can choose to provide 'advanced' views that delve into addition details
// of a Message structure
public interface IParameter extends INamedObject
{
	public Object getOwner();
//  String getKind();
	public String getComponentName();
	public String getComponentNameQualifier();
	
	// TODO: rmah: Do these strings belong here???
    public static final String SET_NEW_ACTION_ID = "SetTypeAction_AddType"; //$NON-NLS-1$
	public static final String SELECT_EXISTING_ACTION_ID = "SetTypeAction_ExistingType"; //$NON-NLS-1$
  
	public Command getSetTypeCommand(String actionId);
}
