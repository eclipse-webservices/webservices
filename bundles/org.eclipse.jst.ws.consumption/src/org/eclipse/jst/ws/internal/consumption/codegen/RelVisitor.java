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

package org.eclipse.jst.ws.internal.consumption.codegen;
      
import java.util.Enumeration;

import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
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
  public Status run (Object sourceElement,VisitorAction vAction)
  {
  	    Status status = new SimpleStatus("");
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
