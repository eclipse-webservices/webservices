/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060612   145433 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.TypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElementType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;


/**
* Objects of this class represent a InputFileTypeGenerator.
* */
public class InputFileAttributeGenerator extends InputFileTypeGenerator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  private boolean fStateLess;

  /**
  * Constructor.
  * 
  */
  public InputFileAttributeGenerator (StringBuffer buffer, int currentLevel, int levelsDeep)
  {
     super(buffer,currentLevel,levelsDeep);
  }

 
  /*
  * Takes in an object to be acted upon by this visitor action
  * @param Object The object to be acted upon
  */
  public IStatus visit (Object object)
  {
     
     AttributeElementType attributeElementType = (AttributeElementType)object;
     //if it is a bean is it stateless
     attributeElementType.getTypeElement(); 
     if(attributeElementType.getSetterMethod() == null) return Status.OK_STATUS;
     if(attributeElementType.getTypeElement().isBean()){
       BeanElement bean = (BeanElement)attributeElementType.getTypeElement();
       fStateLess = bean.isStateLess();
     }	 

     TypeVisitor typeVisitor = new TypeVisitor();
     InputFileTypeGenerator inputFileTypeGenerator = new InputFileTypeGenerator(fbuffer,fCurrentLevel,fLevelsDeep);
     if(attributeElementType.getTypeElement().isBean() && !TypeFactory.recognizedBean(attributeElementType.getTypeElement().getName()) 
     	&& !(getReturnParam() && (TypeFactory.isRecognizedReturnType(attributeElementType.getTypeElement().getTypeName()) || fStateLess)))
                
     addParentGetter(attributeElementType.getGetterMethod(),attributeElementType.getTypeElement().getName());
     inputFileTypeGenerator.addParentGetter(returnParentGetter(),returnParentGetterType());
     inputFileTypeGenerator.setInstanceName(fInstanceName);
     inputFileTypeGenerator.setReturnParam(getReturnParam());
     typeVisitor.run(attributeElementType,inputFileTypeGenerator);   
     fbuffer = inputFileTypeGenerator.getStringBuffer();
     
     return Status.OK_STATUS;
  }
}
