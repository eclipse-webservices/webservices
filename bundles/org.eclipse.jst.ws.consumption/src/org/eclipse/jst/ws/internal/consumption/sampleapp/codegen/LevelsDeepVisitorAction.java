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
package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.codegen.Visitor;
import org.eclipse.jst.ws.internal.consumption.codegen.VisitorAction;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.AttributeVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.FieldVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.TypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a LevelsDeepVisitorAction.
* */
public class LevelsDeepVisitorAction implements VisitorAction 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  private int fLevelsDeep;
  private int fDeepestLevel;
  
  /**
  * Constructor.
  * This is the starting point
  * 
  */
  public LevelsDeepVisitorAction ()
  {
    fLevelsDeep = 0;
  }

  /**
  * Constructor.
  * This is the starting point
  * 
  */
  public LevelsDeepVisitorAction (int current,boolean increment)
  {
    fLevelsDeep = current;
    if(increment) fLevelsDeep++;
    fDeepestLevel = fLevelsDeep; 
  }
  
  public void initialize(String resident)
  {
    //nothing to be done but must be implemented
  }



  /**
  * The visitor that called this VisitorAction
  * @param visitor the visitor that called this visitor action
  */
  public void setVisitor(Visitor visitor)
  {
  }


  /**
  * Returns the level of nesting within this bean
  * @return int returns the int number representing the number of nests of this bean
  */
  public int getLevelsDeep()
  {
     return fDeepestLevel;
  }
 

  /**
  * Takes in an object to be acted upon by this visitor action
  * @param Object The object to be acted upon
  */
  public IStatus visit (Object object)
  {
      Element element = (Element)object;
      if (element instanceof AttributeElement || element instanceof TypeElement){
         TypeVisitor typeVisitor = new TypeVisitor();
         LevelsDeepVisitorAction lvda = new LevelsDeepVisitorAction(fLevelsDeep,true);
         typeVisitor.run(element,lvda);

         if (lvda.getLevelsDeep() > fDeepestLevel) fDeepestLevel = lvda.getLevelsDeep(); 
         
      }
      else if (element instanceof BeanElement && !(TypeFactory.recognizedBean(element.getName()))){
         AttributeVisitor attributeVisitor = new AttributeVisitor();
         LevelsDeepVisitorAction lvda = new LevelsDeepVisitorAction(fLevelsDeep,false);
         attributeVisitor.run(element,lvda);
         if (lvda.getLevelsDeep() > fDeepestLevel) fDeepestLevel = lvda.getLevelsDeep(); 
   
         FieldVisitor fieldVisitor = new FieldVisitor();
         LevelsDeepVisitorAction lvda2 = new LevelsDeepVisitorAction(fLevelsDeep,false);
         fieldVisitor.run(element,lvda2);
         if (lvda2.getLevelsDeep() > fDeepestLevel) fDeepestLevel = lvda2.getLevelsDeep(); 
   
      }
         
     return Status.OK_STATUS;   
  }

}
