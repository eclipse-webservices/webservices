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
import org.eclipse.jst.ws.internal.consumption.datamodel.beanmodel.MethodElement;
import org.eclipse.wst.ws.internal.datamodel.Element;


/**
* Objects of this class represent a MethodFilegenerator.
* */
public class MethodFileHelpGenerator extends MethodFileGenerator
{

  // Copyright
  public static final String copyright = "(c) Copyright IBM Corporation 2000, 2002.";


  /**
  * Constructor.
  *
  */
  public MethodFileHelpGenerator (StringBuffer buffer,String inputName)
  {
      super(buffer,inputName);
  }


  //takes in a Method node
  public IStatus visit (Object object)
  {
      Element methodElement = (Element)object;
      MethodElement method = (MethodElement)methodElement;
      if (method.getMethodOmmission()) return Status.OK_STATUS;
      
      fbuffer.append("<LI><A HREF=\"" + fInputName + "?method=" + method.getNumberID());
      fbuffer.append("\" TARGET=\"inputs\"> " + method.getDisplayName());
      fbuffer.append("</A></LI>" + StringUtils.NEWLINE);

      return Status.OK_STATUS;
  }


}
