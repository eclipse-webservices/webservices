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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.MethodVisitor;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a MethodFilegenerator.
* */
public class MethodFileGenerator extends Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  public String fInputName;
  /**
  * Constructor.
  * 
  */
  public MethodFileGenerator (String inputs)
  {
    super();
    fInputName = inputs;
  }

   /**
  * Constructor.
  * @param StringBuffer buffer to append codegen 
  */
  public MethodFileGenerator (StringBuffer buffer,String inputs)
  {
    super(buffer);
    fInputName = inputs;
  }


  public void setFileNames(String inputs)
  {
    fInputName = inputs;
  }

  //public static String LOCATOR = "Locator";
  public static String BEAN = "Proxy";
  public static String GET = "get";

  
  //takes in a bean node
  public IStatus visit (Object object)
   {
      Element beanElement = (Element)object;
      fbuffer.append("<%@page contentType=\"text/html;charset=UTF-8\"%>");
      fbuffer.append("<HTML>" + StringUtils.NEWLINE + "<HEAD>" + StringUtils.NEWLINE + "<TITLE>Methods</TITLE>" + StringUtils.NEWLINE + "</HEAD>" + StringUtils.NEWLINE + "<BODY>" + StringUtils.NEWLINE + "<H1>Methods</H1>" + StringUtils.NEWLINE + "<UL>" + StringUtils.NEWLINE);
      MethodVisitor methodVisitor = new MethodVisitor();
      MethodFileHelpGenerator methodFileHelpGenerator = new MethodFileHelpGenerator(fbuffer,fInputName); 
      methodVisitor.run(beanElement,methodFileHelpGenerator);
      fbuffer = methodFileHelpGenerator.getStringBuffer();
      fbuffer.append("</UL>" + StringUtils.NEWLINE + "</BODY>" + StringUtils.NEWLINE + "</HTML>");
      
      return Status.OK_STATUS;
  }
}
