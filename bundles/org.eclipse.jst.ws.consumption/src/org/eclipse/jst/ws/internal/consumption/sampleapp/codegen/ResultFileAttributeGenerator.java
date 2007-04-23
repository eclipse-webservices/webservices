/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.TypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.AttributeElementType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.DataType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.ws.internal.datamodel.BasicElement;

/**
* Objects of this class represent a ResultFileAttributeGenerator.
* */
public class ResultFileAttributeGenerator extends ResultFileHelp2Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  private Vector fSetterVector; 

  /**
  * Constructor.
  * @param buffer StringBuffer object that this code generator writes to 
  */
  public ResultFileAttributeGenerator (StringBuffer buffer)
  {
      super(buffer);
      
  }
  
  /**
  * Setter vector
  */
  public Vector getSetterVector()
  {
      return fSetterVector;
  }
  

  /**
  * Visit Method generates code for this Visitor
  * @param Parameter parameter code will be generated 
  */
  public IStatus visit (Object object)
  {
     AttributeElementType attributeElementType = (AttributeElementType)object;
     if(attributeElementType.getSetterMethod() == null) return Status.OK_STATUS;
     BasicElement element = (BasicElement)object;
     if(attributeElementType.getTypeElement().isSimple() || TypeFactory.recognizedBean(attributeElementType.getTypeElement().getName())){      
        //start the codegen
        //ask the datatype for its request line, it may need mark up or not.
        //ie For the simple types we need the mark up
        //for the dom element we cant use it.
        DataType dataType = TypeFactory.createType(attributeElementType.getTypeElement().getName(),element.getMUID());
        setTypeOwnerId(idName(element.getName())); 
        fbuffer.append(dataType.getRequestCode(element.getMUID(),getTypeOwnerId()));
                
     }
     
     TypeVisitor typeVisitor = new TypeVisitor();
     ResultFileTypeGenerator resultFileTypeGenerator = new ResultFileTypeGenerator(fbuffer);
     resultFileTypeGenerator.setNumberFactory(getNumberFactory());
     resultFileTypeGenerator.setTypeOwnerId(getTypeOwnerId());
     
     typeVisitor.run(attributeElementType,resultFileTypeGenerator);
     setNumberFactory(resultFileTypeGenerator.getNumberFactory());
     Vector setterInputs = resultFileTypeGenerator.getResidentVector(); 
     putResidentVector(attributeElementType.getSetterSignature((String)setterInputs.firstElement()));
     
     return Status.OK_STATUS;
  }   

  
  
}
