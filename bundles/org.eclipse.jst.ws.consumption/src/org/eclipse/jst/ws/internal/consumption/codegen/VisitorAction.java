/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen;

import org.eclipse.core.runtime.IStatus;


/**
* Objects of this class represent a VisitorAction.
* The visitor walks the model and the VisitorAction 
* does the action
* */
public interface VisitorAction 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  public IStatus visit (Object object);
  public void setVisitor(Visitor visitor);
  public void initialize(String residentString);
}
