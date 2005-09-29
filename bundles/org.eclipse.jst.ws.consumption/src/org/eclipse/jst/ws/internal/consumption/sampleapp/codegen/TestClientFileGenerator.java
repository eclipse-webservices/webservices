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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.StringUtils;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;


/**
* Objects of this class represent a TestClientFilegenerator.
* */
public class TestClientFileGenerator extends Generator
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  public String fMethodName;
  public String fInputName;
  public String fResultName;
  
  /**
  * Constructor.
  *
  */
  public TestClientFileGenerator (String inputs, String methods, String results)
  {
     super();
     fInputName = inputs;
     fMethodName = methods;
     fResultName = results;
  }

  public void setFileNames(String inputs, String methods, String results)
  {
    fInputName = inputs;
    fMethodName = methods;
    fResultName = results;
  }

  //takes in a bean node
   public IStatus visit (Object object)
   {
      fbuffer.append("<%@page contentType=\"text/html;charset=UTF-8\"%>");
      fbuffer.append("<HTML>" + StringUtils.NEWLINE);
      fbuffer.append("<HEAD>" + StringUtils.NEWLINE);
      fbuffer.append("<TITLE>Web Services Test Client</TITLE>" + StringUtils.NEWLINE);
      fbuffer.append("</HEAD>" + StringUtils.NEWLINE + "");
      fbuffer.append("<FRAMESET  COLS=\"220,*\">" + StringUtils.NEWLINE);
      fbuffer.append("<FRAME SRC=\""+ fMethodName + "\" NAME=\"methods\" MARGINWIDTH=\"1\" MARGINHEIGHT=\"1\" SCROLLING=\"yes\" FRAMEBORDER=\"1\">" + StringUtils.NEWLINE);
      fbuffer.append("<FRAMESET  ROWS=\"80%,20%\">" + StringUtils.NEWLINE);
      fbuffer.append("<FRAME SRC=\""+ fInputName  + "\" NAME=\"inputs\"  MARGINWIDTH=\"1\" MARGINHEIGHT=\"1\" SCROLLING=\"yes\" FRAMEBORDER=\"1\">" + StringUtils.NEWLINE);
      fbuffer.append("<%").append(StringUtils.NEWLINE);
      fbuffer.append("StringBuffer resultJSP = new StringBuffer(\"").append(fResultName).append("\");").append(StringUtils.NEWLINE);
      fbuffer.append("resultJSP.append(\"?\");").append(StringUtils.NEWLINE);
      fbuffer.append("java.util.Enumeration resultEnum = request.getParameterNames();");
      fbuffer.append("while (resultEnum.hasMoreElements()) {").append(StringUtils.NEWLINE);
      fbuffer.append("Object resultObj = resultEnum.nextElement();").append(StringUtils.NEWLINE);
      fbuffer.append("resultJSP.append(resultObj.toString()).append(\"=\").append(request.getParameter(resultObj.toString())).append(\"&\");").append(StringUtils.NEWLINE);
      fbuffer.append("}").append(StringUtils.NEWLINE);
      fbuffer.append("%>").append(StringUtils.NEWLINE);
      fbuffer.append("<FRAME SRC=\"<%=resultJSP.toString()%>\" NAME=\"result\"  MARGINWIDTH=\"1\" MARGINHEIGHT=\"1\" SCROLLING=\"yes\" FRAMEBORDER=\"1\">" + StringUtils.NEWLINE);
      fbuffer.append("</FRAMESET>" + StringUtils.NEWLINE);


      fbuffer.append("<NOFRAMES>" + StringUtils.NEWLINE);
      fbuffer.append("<BODY>" + StringUtils.NEWLINE);
      fbuffer.append("The Web Services Test Client requires a browser that supports frames." + StringUtils.NEWLINE);
      fbuffer.append("</BODY>" + StringUtils.NEWLINE);
      fbuffer.append("</NOFRAMES>" + StringUtils.NEWLINE);
      fbuffer.append("</FRAMESET>" + StringUtils.NEWLINE);
      fbuffer.append("</HTML>");

      return Status.OK_STATUS;
  }
}
