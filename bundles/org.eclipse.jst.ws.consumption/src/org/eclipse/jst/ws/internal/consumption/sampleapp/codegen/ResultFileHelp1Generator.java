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

import java.util.Enumeration;

import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.ParameterVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.ReturnParameterVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.DataType;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.MethodElement;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.TypeFactory;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a ResultFilegenerator.
* */
public class ResultFileHelp1Generator extends ResultFileGenerator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  public static final int INITIAL_STATE = 1;
  public static final int FINAL_STATE = 2;
  public static final String MTEMP = "mtemp";
  public static String BEAN = "Proxy";

  /**
  * keeps the state of the visitor 
  * */

  private int fstate;

  
  /**
  * Constructor.
  * @param buffer StringBuffer object that this code generator writes to 
  */
  public ResultFileHelp1Generator (StringBuffer buffer)
  {
      super(buffer);
      fstate = INITIAL_STATE;
    
  }
  
  /**
  * Visit Method generates code for this Visitor
  * @param method method code will be generated for 
  */
  public Status visit (Object object)
  {
     Element methodElement = (Element)object;
  

     MethodElement method = (MethodElement)methodElement;
     if (method.getMethodOmmission()) return new SimpleStatus("");

     fbuffer.append("case " + method.getNumberID()+ ":" + StringUtils.NEWLINE);
     visitHelper(method);
     
     fbuffer.append("break;" + StringUtils.NEWLINE); 
  
     return new SimpleStatus("");
  }   
  /**
  * Helps out the visitor
  * @param method method code will be generated for 
  */
  public void visitHelper (Element methodElement)
  {

     fbuffer.append(Generator.DOUBLE_TAB + "gotMethod = true;" + StringUtils.NEWLINE);
     
     MethodElement method = (MethodElement)methodElement;
     // go to the next generator
     ResultFileHelp2Generator resultFileHelp2Generator = new ResultFileHelp2Generator(fbuffer); 
     resultFileHelp2Generator.setNumberFactory(getNumberFactory());
     ParameterVisitor parameterVisitor = new ParameterVisitor();
     parameterVisitor.run(methodElement,resultFileHelp2Generator);
     fbuffer = resultFileHelp2Generator.getStringBuffer();
     setNumberFactory(resultFileHelp2Generator.getNumberFactory());
     // we must now grab the state data from the resident vector
     setResidentVector(resultFileHelp2Generator.getResidentVector());

     
     //there is no return type if void occurs    
    
     
     if (method.getReturnParameterElement().getTypeElement().getName().equals("void"))
        fbuffer.append(Generator.DOUBLE_TAB + getSessionBeanId() + "." +method.getName() + "(");

     else{
        fbuffer.append(Generator.DOUBLE_TAB + method.getReturnParameterElement().getTypeElement().getName() + Generator.SPACE + method.getMUID() + MTEMP + Generator.SPACE);
        fbuffer.append("=" + Generator.SPACE + getSessionBeanId() + "." + method.getName() + "(");
     }

     Enumeration e = fResidentVector.elements();
     while (e.hasMoreElements()){
          fbuffer.append((String)e.nextElement());
          if (e.hasMoreElements())
              fbuffer.append(",");    
     }
     fbuffer.append(");" + StringUtils.NEWLINE);
     if (!method.getReturnParameterElement().getTypeElement().getName().equals("void") && !method.getReturnParameterElement().getTypeElement().isPrimitive()){
        //in case our result is null
        fbuffer.append("if(" + method.getMUID() + "mtemp == null){" + StringUtils.NEWLINE);
        fbuffer.append("%>" + StringUtils.NEWLINE);
        fbuffer.append("<%=" + method.getMUID() + "mtemp %>" + StringUtils.NEWLINE);
        fbuffer.append("<%" + StringUtils.NEWLINE);
        fbuffer.append("}else{" + StringUtils.NEWLINE);
     }
     
     //now lets display the return bean
     // if it is simple we dont need to use the generator
     
     if (!method.getReturnParameterElement().getTypeElement().getName().equals("void")){
        if (method.getReturnParameterElement().getTypeElement().isSimple() 
           || TypeFactory.recognizedBean(method.getReturnParameterElement().getTypeElement().getName())
           || TypeFactory.isStateLessBean(method.getReturnParameterElement().getTypeElement().getTypeName())
           || TypeFactory.isRecognizedReturnType(method.getReturnParameterElement().getTypeElement().getTypeName())){
           DataType dataType = TypeFactory.createType(method.getReturnParameterElement().getTypeElement().getTypeName(),method.getReturnParameterElement().getMUID());
           fbuffer.append(dataType.TypeConversion(method.getMUID() + MTEMP));
        }
        else{
           fbuffer.append("%>" + StringUtils.NEWLINE);
           InputFileHelp2Generator inputFileHelp2Generator = new InputFileHelp2Generator(fbuffer);
           inputFileHelp2Generator.setInstanceName(method.getMUID() + MTEMP);
           inputFileHelp2Generator.setReturnParam(true);
           ReturnParameterVisitor returnParameterVisitor = new ReturnParameterVisitor();
           returnParameterVisitor.run(method,inputFileHelp2Generator);
           fbuffer = inputFileHelp2Generator.getStringBuffer();
           fbuffer.append("<%" + StringUtils.NEWLINE);

        }
     }

    if (!method.getReturnParameterElement().getTypeElement().getName().equals("void")  && !method.getReturnParameterElement().getTypeElement().isPrimitive()) fbuffer.append("}" + StringUtils.NEWLINE);    
    
  }



}
