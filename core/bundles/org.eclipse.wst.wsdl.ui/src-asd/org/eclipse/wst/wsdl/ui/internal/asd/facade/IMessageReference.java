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

// this is a simplification of an input, output or fault object
// (or any other kind of object future specs might cook up)
public interface IMessageReference extends INamedObject
{  
  public final static int KIND_INPUT  = 1;
  public final static int KIND_OUTPUT = 2;
  public final static int KIND_FAULT = 3;
  
  public int getKind();
  public List getParameters();   
  public IOperation getOwnerOperation();
  
  public Command getReorderParametersCommand(IParameter leftSibling, IParameter rightSibling, IParameter movingParameter);
}
