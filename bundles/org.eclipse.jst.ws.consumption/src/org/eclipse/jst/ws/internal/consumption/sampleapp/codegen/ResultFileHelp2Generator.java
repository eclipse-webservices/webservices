/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.TypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.DataType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.ParameterElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Element;

/**
* Objects of this class represent a ResultFileHelp2generator.
* */
public class ResultFileHelp2Generator extends Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  private String fTypeOwnerId;  


  /**
  * Constructor.
  * @param buffer StringBuffer object that this code generator writes to 
  */
  public ResultFileHelp2Generator (StringBuffer buffer)
  {
      super(buffer);
      
  }
  
  /**
  * Visit Method generates code for this Visitor
  * @param Parameter parameter code will be generated 
  */
  public Status visit (Object object)
  {
    

     ParameterElement parameterElement = (ParameterElement)object;
     Element element= parameterElement.getTypeElement(); 
     if(parameterElement.getTypeElement().isSimple() || TypeFactory.recognizedBean(parameterElement.getTypeElement().getName())){      
        //start the codegen
         //ask the datatype for its request line, it may need mark up or not.
        //ie For the simple types we need the mark up
        //for the dom element we cant use it.
        DataType dataType = TypeFactory.createType(parameterElement.getTypeElement().getName(),parameterElement.getMUID());
        setTypeOwnerId(idName(parameterElement.getName()));
        fbuffer.append(dataType.getRequestCode(parameterElement.getMUID(),getTypeOwnerId()));
     }
         
     //visit the type generator
     ResultFileTypeGenerator resultFileTypeGenerator = new ResultFileTypeGenerator(fbuffer); 
     resultFileTypeGenerator.setNumberFactory(getNumberFactory());
     resultFileTypeGenerator.setTypeOwnerId(getTypeOwnerId());
     TypeVisitor TypeVisitor = new TypeVisitor();
     TypeVisitor.run(parameterElement,resultFileTypeGenerator);
     fbuffer = resultFileTypeGenerator.getStringBuffer();
     setNumberFactory(resultFileTypeGenerator.getNumberFactory());
     String name = (String)resultFileTypeGenerator.getResidentVector().firstElement();
     putResidentVector(name); 
     
     return new SimpleStatus("");
  }


  public String getTypeOwnerId()
  {
     if (fTypeOwnerId == null) fTypeOwnerId = ""; 
     return fTypeOwnerId;
  }

  public void setTypeOwnerId(String name)
  {
    fTypeOwnerId = name;
  }

  public String idName(String name)
  {
  	
    String uniqueNum = String.valueOf(getUniqueNumber());
    String newName = name.replace('.','1') + "_" +uniqueNum + "id";
    return newName;
  }


}
