/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class ParameterVisitor extends RelVisitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  /**
  * Constructor.
  * 
  */

  public ParameterVisitor ()
  {
     super(MethodElement.REL_PARAMETERS);
  }
  
   

}
