/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

import java.util.Vector;
import org.eclipse.jst.ws.internal.consumption.codegen.RelVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.sampleapp.codegen.InputFileAttributeGenerator;


/**
* Objects of this class represent a visitor.
* */
public class AttributeVisitor extends RelVisitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  private Vector fResidentVector1;
  private Vector fResidentVector2;

   /**
  * Constructor.
  * 
  */

  public AttributeVisitor ()
  {
    super(TypeElement.REL_ATTRIBUTES);
  }

  public AttributeVisitor (String rel)
  {
    super(rel);
  }
  
  public void setResidentString(String resident)
  {
  }

  public void setResidentVector1(Vector resident)
  {
    fResidentVector1 = resident;
  }

  public void setResidentVector2(Vector resident)
  {
    fResidentVector2 = resident;
  }

  
  public void initialize(VisitorAction vAction)
  {
     //hack for 197726

     if(vAction instanceof InputFileAttributeGenerator){
       if(fResidentVector1 == null) fResidentVector1=new Vector();
       if(fResidentVector2 == null) fResidentVector2=new Vector();
       ((InputFileAttributeGenerator)vAction).setParentGetters(fResidentVector1,fResidentVector2);
     }
  }
 
  

}
