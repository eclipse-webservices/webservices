/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080505   182167 makandre@ca.ibm.com - Andrew Mak, Warning not issued when non-instantiable class is bypassed in sampe JSPs
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.codegen.javamofvisitors;

import java.util.Iterator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.Method;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.wst.command.internal.env.core.selection.BooleanSelection;


/**
* Objects of this class represent a visitor.
* */
public class JavaMofMethodVisitor implements Visitor 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";
  public String SET_ENDPOINT = "setEndPoint";
  public String GET_ENDPOINT = "getEndPoint";
  private BooleanSelection[] fMethodsSelected;


  /*
  * Constructor
  **/
  public JavaMofMethodVisitor()
  {
  }
 
  public String UNDER_SCORE = "_";

  /*
  * Run through all the methods in this bean
  * @param JavaClass javaclass that holds the methods
  * @param VisitorAction Action to be performed on each method
  **/
  public IStatus run ( Object javaclass, VisitorAction vAction)
  {
  	IStatus status = Status.OK_STATUS;
    JavaClass javaClass = (JavaClass)javaclass;
    for (Iterator m=javaClass.getPublicMethods().iterator(); m.hasNext(); ) {
      Method method=(Method)m.next();
      if(fMethodsSelected != null)  {
        boolean methodSelected = false;
      	for(int i =0;i < fMethodsSelected.length;i++){
          if(fMethodsSelected[i] == null) continue;
          if ( fMethodsSelected[i].getValue().equals(method.getMethodElementSignature()))
            methodSelected = (boolean) fMethodsSelected[i].isSelected();
        }
        
        method.getName();
        
        if(methodSelected) {
          status = vAction.visit(method);  
          if (status.getSeverity() == IStatus.ERROR)
        	  break;
        }
      }
      else {
        status = vAction.visit(method);   
      }

    }
    return status;
  }

  public void setMethodSelection(BooleanSelection[] methodSelected)
  {
    fMethodsSelected = methodSelected;
  }
  
}
