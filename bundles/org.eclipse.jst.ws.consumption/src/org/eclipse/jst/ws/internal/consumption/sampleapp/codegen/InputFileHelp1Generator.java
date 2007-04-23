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
import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.ParameterVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.MethodElement;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a InputFileHelp1Generator.
* */
public class InputFileHelp1Generator extends InputFileGenerator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  public static final int INITIAL_STATE = 1;
  public static final int FINAL_STATE = 2;

  public int fstate;
   
  /**
  * Constructor.
  * 
  */
  public InputFileHelp1Generator (StringBuffer buffer)
  {
      super(buffer);
      fstate = INITIAL_STATE;
  }
  
  /**
  * Constructor.
  * 
  */
  public InputFileHelp1Generator (StringBuffer buffer,String resultName)
  {
      super(buffer,resultName);
      fstate = INITIAL_STATE;
  }
  
  /*
  * Takes in an element
  * @param Object Takes in an object to be acted upon
  */
  public IStatus visit (Object object)
  {
     Element methodElement = (Element)object;
     MethodElement method = (MethodElement)methodElement;
     if (method.getMethodOmmission()) return Status.OK_STATUS;
     
     ParameterVisitor parameterVisitor = new ParameterVisitor();
     fbuffer.append("case " + method.getNumberID()+ ":" + StringUtils.NEWLINE);
     fbuffer.append("valid = false;" + StringUtils.NEWLINE);
     fbuffer.append("%>" + StringUtils.NEWLINE);
     fbuffer.append("<FORM METHOD=\"POST\" ACTION=\"" + fResultName + "\" TARGET=\"result\">" + StringUtils.NEWLINE);
     fbuffer.append("<INPUT TYPE=\"HIDDEN\" NAME=\"method\" VALUE=\"<%=method%>\">" + StringUtils.NEWLINE);
    
     // go to the next generator
     InputFileHelp2Generator inputFileHelp2Generator = new InputFileHelp2Generator(fbuffer); 
     parameterVisitor.run(methodElement,inputFileHelp2Generator);
     fbuffer = inputFileHelp2Generator.getStringBuffer();
     
     fbuffer.append("<BR>" + StringUtils.NEWLINE);
     fbuffer.append("<INPUT TYPE=\"SUBMIT\" VALUE=\"Invoke\">" + StringUtils.NEWLINE);
     fbuffer.append("<INPUT TYPE=\"RESET\" VALUE=\"Clear\">" + StringUtils.NEWLINE);
     fbuffer.append("</FORM>" + StringUtils.NEWLINE);
     fbuffer.append("<%" + StringUtils.NEWLINE);
     fbuffer.append("break;" + StringUtils.NEWLINE);
      

     return Status.OK_STATUS;
     
  
  }   
  
}
