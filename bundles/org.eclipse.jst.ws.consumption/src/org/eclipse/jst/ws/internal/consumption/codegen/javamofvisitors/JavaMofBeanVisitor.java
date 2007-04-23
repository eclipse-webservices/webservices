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

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;




/**
* Objects of this class represent a visitor.
* */
public class JavaMofBeanVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  /*
  *Constructor
  **/
  public JavaMofBeanVisitor()
  {
  }
 
  public IStatus run ( Object javaClass, VisitorAction vAction)
  {
    return vAction.visit((JavaClass)javaClass);   
  }
        
  
}
