/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     bug 296245 - Yen Lu - yenlu@ca.ibm.com - Input.jsp produces malformed HTML
 *     bug 353272 - Ivan Castro Alvarez - JAX-RPC Sample JSP generator should produce Web samples that are not vulnerable to XSS 
 *******************************************************************************/


package org.eclipse.jst.ws.internal.consumption.sampleapp.codegen;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.MethodVisitor;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a MethodFilegenerator.
* */
public class InputFileGenerator extends Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  public String fResultName;

  /**
  * Constructor.
  * used directly 
  */
  public InputFileGenerator (String result)
  {
      super();
      fResultName = result;
  }

  /**
  * Constructor.
  * called from subclasses
  */
  public InputFileGenerator (StringBuffer buffer, String result)
  {
      super(buffer);
      fResultName = result;
  }

  /**
  * Constructor.
  * called from subclasses
  */
  public InputFileGenerator (StringBuffer buffer)
  {
      super(buffer);
  }
  
  public void setFileNames(String results)
  {
    fResultName = results;
  }

  //takes in a bean node
   public IStatus visit (Object object)
   {
      Element beanElement = (Element)object;
      fbuffer.append("<%@page contentType=\"text/html;charset=UTF-8\"%>" + StringUtils.NEWLINE);
      fbuffer.append("<HTML>" + StringUtils.NEWLINE);
      fbuffer.append("<HEAD>" + StringUtils.NEWLINE);
      fbuffer.append("<TITLE>Inputs</TITLE>" + StringUtils.NEWLINE);
      fbuffer.append("</HEAD>" + StringUtils.NEWLINE);
      fbuffer.append("<BODY>" + StringUtils.NEWLINE);
      fbuffer.append("<H1>Inputs</H1>" + StringUtils.NEWLINE + "" + StringUtils.NEWLINE);
      fbuffer.append("<%" + StringUtils.NEWLINE + "String method = request.getParameter(\"method\");" + StringUtils.NEWLINE);
      fbuffer.append("int methodID = 0;" + StringUtils.NEWLINE);
      fbuffer.append("if (method == null) methodID = -1;" + StringUtils.NEWLINE + "" + StringUtils.NEWLINE);
      fbuffer.append("boolean valid = true;" + StringUtils.NEWLINE + StringUtils.NEWLINE);

      fbuffer.append("if(methodID != -1) methodID = Integer.parseInt(method);" + StringUtils.NEWLINE); 
      fbuffer.append("switch (methodID){ " + StringUtils.NEWLINE);
      	
      // go to the next generator
      MethodVisitor methodVisitor = new MethodVisitor();
      InputFileHelp1Generator inputFileHelp1Generator = new InputFileHelp1Generator(fbuffer,fResultName); 
      methodVisitor.run(beanElement,inputFileHelp1Generator);
      fbuffer = inputFileHelp1Generator.getStringBuffer();

      fbuffer.append("case 1111111111:" + StringUtils.NEWLINE);
      fbuffer.append("valid = false;" + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE);
      fbuffer.append("<FORM METHOD=\"POST\" ACTION=\"" + fResultName + "\" TARGET=\"result\">" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"HIDDEN\" NAME=\"method\" VALUE=\"<%=org.eclipse.jst.ws.util.JspUtils.markup(method)%>\">" + StringUtils.NEWLINE);
      fbuffer.append("<TABLE>" + StringUtils.NEWLINE);
      fbuffer.append("<TR>" + StringUtils.NEWLINE);
      fbuffer.append("<TD COLSPAN=\"1\" ALIGN=\"LEFT\">URLString:</TD>" + StringUtils.NEWLINE);
      fbuffer.append("<TD ALIGN=\"left\"><INPUT TYPE=\"TEXT\" NAME=\"url1111111111\" SIZE=20></TD>" + StringUtils.NEWLINE);
      fbuffer.append("</TR>" + StringUtils.NEWLINE);
      fbuffer.append("</TABLE>" + StringUtils.NEWLINE);
      fbuffer.append("<BR>" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"SUBMIT\" VALUE=\"Invoke\">" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"RESET\" VALUE=\"Clear\">" + StringUtils.NEWLINE);
      fbuffer.append("</FORM>" + StringUtils.NEWLINE);
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("break;" + StringUtils.NEWLINE);
      fbuffer.append("case 1111111112:" + StringUtils.NEWLINE);
      fbuffer.append("valid = false;" + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE);
      fbuffer.append("<FORM METHOD=\"POST\" ACTION=\"" + fResultName + "\" TARGET=\"result\">" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"HIDDEN\" NAME=\"method\" VALUE=\"<%=org.eclipse.jst.ws.util.JspUtils.markup(method)%>\">" + StringUtils.NEWLINE);
      fbuffer.append("<BR>" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"SUBMIT\" VALUE=\"Invoke\">" + StringUtils.NEWLINE);
      fbuffer.append("<INPUT TYPE=\"RESET\" VALUE=\"Clear\">" + StringUtils.NEWLINE);
      fbuffer.append("</FORM>" + StringUtils.NEWLINE);
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("break;" + StringUtils.NEWLINE);
      
      

      fbuffer.append("}" + StringUtils.NEWLINE);    
      fbuffer.append("if (valid) {" + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE + "Select a method to test." + StringUtils.NEWLINE);  
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("}" + StringUtils.NEWLINE);
      fbuffer.append("%>").append(StringUtils.NEWLINE).append(StringUtils.NEWLINE);
      fbuffer.append("</BODY>" + StringUtils.NEWLINE);
      fbuffer.append("</HTML>" + StringUtils.NEWLINE);
      
      return Status.OK_STATUS;

    }
}
