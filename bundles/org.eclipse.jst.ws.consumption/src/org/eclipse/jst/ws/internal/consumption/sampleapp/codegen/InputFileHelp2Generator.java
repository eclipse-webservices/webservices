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
import org.eclipse.jst.ws.internal.consumption.codegen.RelVisitor;
import org.eclipse.jst.ws.internal.consumption.codegen.bean.TypeVisitor;
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.ParameterElement;
import org.eclipse.jst.ws.internal.datamodel.Element;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


/**
* Objects of this class represent a InputFileHelp2Generator.
* */
public class InputFileHelp2Generator extends InputFileHelp1Generator 
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";

   
  /*
  * This is actually used by the result generator to show the 
  * resultant Bean
  * The name is the name the resultant bean is using as its instance name
  */
  protected String fInstanceName;
  private boolean fReturnParam=false;

  /**
  * Constructor.
  * 
  */
  public InputFileHelp2Generator (StringBuffer buffer)
  {
      super(buffer);
      fInstanceName="";
  }

  /**
  * This is state data to be used by the generators
  * @param String name The instance name of the parameters type bean
  */
  public void setInstanceName(String name)
  {
    fInstanceName = name;
  }

  /**
  * This is state data to be used by the generators
  * @return String name The instance name of the parameters type bean
  */
  public String getInstanceName()
  {
    return fInstanceName;
  }


  /*
  * Takes in an object to be acted upon by this visitor action
  * @param Object The object to be acted upon
  */
  public Status visit (Object object)
  {
      Element parameterElement = (Element)object;
      RelVisitor visitor = (RelVisitor)getVisitor();
      ParameterElement parameter = (ParameterElement)parameterElement;

      fbuffer.append("<TABLE>" + StringUtils.NEWLINE);

      TypeVisitor typeVisitor = new TypeVisitor();
      InputFileTypeGenerator inputFileTypeGenerator = new InputFileTypeGenerator(fbuffer,0);
      inputFileTypeGenerator.setReturnParam(getReturnParam());
      inputFileTypeGenerator.setInstanceName(fInstanceName);
      typeVisitor.run(parameterElement,inputFileTypeGenerator);
      fbuffer = inputFileTypeGenerator.getStringBuffer();

      fbuffer.append("</TABLE>" + StringUtils.NEWLINE);
      
      return new SimpleStatus("");
  
  }

  public boolean getReturnParam()
  {
    return fReturnParam;
  }

  public void setReturnParam(boolean returnParam)
  {
    fReturnParam = returnParam;
  } 


}
