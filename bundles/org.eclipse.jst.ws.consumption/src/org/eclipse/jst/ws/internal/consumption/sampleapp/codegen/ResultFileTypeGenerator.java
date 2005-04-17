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

package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import java.util.Enumeration;

import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.AttributeVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.FieldVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.DataType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Element;

/**
* Objects of this class represent a ResultFileHelp2generator.
* */
public class ResultFileTypeGenerator extends ResultFileHelp2Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";



  public String fTypeIdName;
 
  /**
  * Constructor.
  * @param buffer StringBuffer object that this code generator writes to 
  */
  public ResultFileTypeGenerator (StringBuffer buffer)
  {
      super(buffer);
  }
  
  /**
  * Visit Method generates code for this Visitor
  * @param Parameter parameter code will be generated 
  */
  public Status visit (Object object)
  {
     Element typeElement = (Element)object;
     TypeElement type = (TypeElement)typeElement;      
     // right now we only have simple to worry about
     if(type.isSimple() || TypeFactory.recognizedBean(type.getName())){
       // create the type that represents the simple type name
       DataType dataType = TypeFactory.createType(type.getName(),type.getOwningElement().getMUID());
  
       String nodeName = getTypeOwnerId()+ "Temp";
       fbuffer.append(dataType.stringConversion(type.getName(),nodeName,getTypeOwnerId()));

       putResidentVector(nodeName);
     }

     else {
       //must be complex
       String typeName = typeElement.getName(); 
       fTypeIdName = idName(typeName);
              
       AttributeVisitor attributeVisitor = new AttributeVisitor();
       ResultFileAttributeGenerator resultFileAttributeGenerator = new ResultFileAttributeGenerator(fbuffer);
       resultFileAttributeGenerator.setNumberFactory(getNumberFactory());
       attributeVisitor.run(typeElement,resultFileAttributeGenerator);
       setNumberFactory(resultFileAttributeGenerator.getNumberFactory());
       fbuffer = resultFileAttributeGenerator.getStringBuffer();

       FieldVisitor fieldVisitor = new FieldVisitor();
       ResultFileAttributeGenerator resultFileAttributeGenerator2 = new ResultFileAttributeGenerator(fbuffer);
       resultFileAttributeGenerator2.setNumberFactory(getNumberFactory());
       fieldVisitor.run(typeElement,resultFileAttributeGenerator2);
       setNumberFactory(resultFileAttributeGenerator2.getNumberFactory());
       fbuffer = resultFileAttributeGenerator2.getStringBuffer();

      
       fbuffer.append(Generator.DOUBLE_TAB + "%>" + StringUtils.NEWLINE);
       fbuffer.append(Generator.DOUBLE_TAB + "<jsp:useBean id=\"" + fTypeIdName + "\" scope=\"session\" class=\"" + typeName + "\" />" + StringUtils.NEWLINE);
       fbuffer.append(Generator.DOUBLE_TAB + "<%" + StringUtils.NEWLINE);
         
       Enumeration e = resultFileAttributeGenerator.getResidentVector().elements();
       while(e.hasMoreElements()){
          fbuffer.append(Generator.DOUBLE_TAB + fTypeIdName + "." + e.nextElement() + StringUtils.NEWLINE);
       }

       Enumeration e2 = resultFileAttributeGenerator2.getResidentVector().elements();
       while(e2.hasMoreElements()){
          fbuffer.append(Generator.DOUBLE_TAB + fTypeIdName + "." + e2.nextElement() + StringUtils.NEWLINE);
       } 
       putResidentVector(fTypeIdName);
       //end of changes
     
    }
     
     return new SimpleStatus("");
  }   
  

  
}
