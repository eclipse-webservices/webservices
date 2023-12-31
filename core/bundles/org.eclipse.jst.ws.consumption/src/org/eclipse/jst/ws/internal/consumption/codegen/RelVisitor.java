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

package org.eclipse.jst.ws.internal.consumption.codegen;
      
import java.util.Enumeration;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;
import org.eclipse.wst.ws.internal.datamodel.Rel;


/**
* Objects of this class represent a visitor.
* This vistor will visit elements in a model given a 
* particular rel
* */
public class RelVisitor implements Visitor
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

  
  private int fElementCounter;
  private int fTotalElements;
  private String frelName;

  /**
  * Constructor.
  * 
  */

  public RelVisitor (String relName)
  {
     frelName = relName;
  }
  
  public int getTotalElementsToVisit()
  {
     return fTotalElements;
  }

  public boolean isLastElement()
  {
     if(getTotalElementsToVisit() - presentElement() == 0) return true;
     return false;
  }

  public int presentElement()
  {
     return fElementCounter;
  }
  
  public void initialize(VisitorAction va)
  {
    //implemented by subclasses
  }  
  public IStatus run (Object sourceElement,VisitorAction vAction)
  {
  	    IStatus status = Status.OK_STATUS;
        initialize(vAction);
  
        vAction.setVisitor(this);
        Rel rel = ((BasicElement)sourceElement).getRel(frelName);
        //set some state data
        fTotalElements = rel.getNumberOfTargetElements();
        fElementCounter = 0;  
        
        Enumeration e = rel.getTargetElements();
        while (e.hasMoreElements()){
           BasicElement targetElement = (BasicElement)e.nextElement(); 
           fElementCounter++;
           initialize(vAction);
           status = vAction.visit(targetElement);
        }
        return status;
  } 

}
