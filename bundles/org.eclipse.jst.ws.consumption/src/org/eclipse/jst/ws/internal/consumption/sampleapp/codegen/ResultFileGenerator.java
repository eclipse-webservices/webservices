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

import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.MethodVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.BeanElement;
import org.eclipse.jst.ws.internal.datamodel.Element;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


/**
* Objects of this class represent a MethodFilegenerator.
* */
public class ResultFileGenerator extends Generator 
{
  public static String TRIPLE_TAB = Generator.DOUBLE_TAB + Generator.TAB; 
  public static String QUAD_TAB = Generator.DOUBLE_TAB + Generator.DOUBLE_TAB;
  
  private String setEndpointMethod;

  /**
  * Constructor.
  * 
  */
  public ResultFileGenerator ()
  {
      super();
  }

   /**
  * Constructor.
  * 
  */
  public ResultFileGenerator (StringBuffer buffer)
  {
      super(buffer);
  }

  //takes in a bean node
  public Status visit (Object object)
  {
      Element beanElement = (Element)object;
      BeanElement bean = (BeanElement)beanElement;
      fbuffer.append("<%@page contentType=\"text/html;charset=UTF-8\"%>");
      fbuffer.append("<HTML>" + StringUtils.NEWLINE);
      fbuffer.append("<HEAD>" + StringUtils.NEWLINE);
      fbuffer.append("<TITLE>Result</TITLE>" + StringUtils.NEWLINE);
      fbuffer.append("</HEAD>" + StringUtils.NEWLINE);
      fbuffer.append("<BODY>" + StringUtils.NEWLINE);
      fbuffer.append("<H1>Result</H1>" + StringUtils.NEWLINE + StringUtils.NEWLINE);

      fbuffer.append("<jsp:useBean id=\""+ getSessionBeanId() + "\" scope=\"session\" class=\"");
      fbuffer.append(bean.getName() + "\" />" + StringUtils.NEWLINE);
      if (setEndpointMethod != null && setEndpointMethod.length() > 0)
      {
        fbuffer.append("<%");
        fbuffer.append(StringUtils.NEWLINE);
        fbuffer.append("if (request.getParameter(\"endpoint\") != null && request.getParameter(\"endpoint\").length() > 0)");
        fbuffer.append(StringUtils.NEWLINE);
        fbuffer.append(getSessionBeanId());
        fbuffer.append(".");
        int index = setEndpointMethod.indexOf('?');
        if (index != -1)
        {
          fbuffer.append(setEndpointMethod.substring(0, index));
          fbuffer.append("(new ");
          fbuffer.append(setEndpointMethod.substring(index+1, setEndpointMethod.length()));
          fbuffer.append("(request.getParameter(\"endpoint\")));");
        }
        else
        {
          fbuffer.append(setEndpointMethod);
          fbuffer.append("(request.getParameter(\"endpoint\"));");
        }
        fbuffer.append(StringUtils.NEWLINE);
        fbuffer.append("%>");
        fbuffer.append(StringUtils.NEWLINE);
      }
      fbuffer.append(StringUtils.NEWLINE); 
            
      //carry on with regular gorp
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("String method = request.getParameter(\"method\");" + StringUtils.NEWLINE);
      fbuffer.append("int methodID = 0;" + StringUtils.NEWLINE);
      fbuffer.append("if (method == null) methodID = -1;" + StringUtils.NEWLINE + "" + StringUtils.NEWLINE);
      fbuffer.append("if(methodID != -1) methodID = Integer.parseInt(method);" + StringUtils.NEWLINE); 
      fbuffer.append("boolean gotMethod = false;" + StringUtils.NEWLINE + StringUtils.NEWLINE);

      fbuffer.append("try {" + StringUtils.NEWLINE);
      fbuffer.append("switch (methodID){ " + StringUtils.NEWLINE);
    
      // go to the next generator
      MethodVisitor methodVisitor = new MethodVisitor();
      ResultFileHelp1Generator resultFileHelp1Generator = new ResultFileHelp1Generator(fbuffer); 
      resultFileHelp1Generator.setNumberFactory(getNumberFactory());
      resultFileHelp1Generator.setClientFolderPath(getClientFolderPath());
      methodVisitor.run(beanElement,resultFileHelp1Generator);
      setNumberFactory(resultFileHelp1Generator.getNumberFactory());

      fbuffer = resultFileHelp1Generator.getStringBuffer();
 
      fbuffer.append("}" + StringUtils.NEWLINE);    
      fbuffer.append("} catch (Exception e) { " + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE);
      fbuffer.append("exception: <%= e %>" + StringUtils.NEWLINE);
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("return;" + StringUtils.NEWLINE);
      fbuffer.append("}" + StringUtils.NEWLINE);
      fbuffer.append("if(!gotMethod){" + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE);
      fbuffer.append("result: N/A" + StringUtils.NEWLINE);
      fbuffer.append("<%" + StringUtils.NEWLINE);
      fbuffer.append("}" + StringUtils.NEWLINE);
      fbuffer.append("%>" + StringUtils.NEWLINE);
      fbuffer.append("</BODY>" + StringUtils.NEWLINE);
      fbuffer.append("</HTML>");

      return new SimpleStatus("");
    }

  /**
   * @param setEndpointMethod The setEndpointMethod to set.
   */
  public void setSetEndpointMethod(String setEndpointMethod)
  {
    this.setEndpointMethod = setEndpointMethod;
  }
}
