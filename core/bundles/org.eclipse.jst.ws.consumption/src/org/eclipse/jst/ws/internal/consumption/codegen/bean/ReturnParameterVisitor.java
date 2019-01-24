/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

package org.eclipse.jst.ws.internal.consumption.codegen.bean;

import org.eclipse.jst.ws.internal.consumption.codegen.RelVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.MethodElement;


/**
* Objects of this class represent a visitor.
* */
public class ReturnParameterVisitor extends RelVisitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  * Constructor.
  * 
  */

  public ReturnParameterVisitor ()
  {
     super(MethodElement.REL_RETURN_PARAMETERS);
  }
  
   

}
