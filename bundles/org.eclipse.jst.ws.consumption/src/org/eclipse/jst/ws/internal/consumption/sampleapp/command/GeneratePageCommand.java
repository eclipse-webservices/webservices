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

package org.eclipse.jst.ws.internal.consumption.sampleapp.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.codegen.Generator;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.ws.internal.datamodel.Element;
import org.eclipse.wst.ws.internal.datamodel.Model;


/**
 * MofToBeanModelCommand
 * Creation date: (4/10/2001 12:41:48 PM)
 * @author: Gilbert Andrews
 */
public class GeneratePageCommand extends EnvironmentalOperation 
{
		
private Model model_;
private Generator fGenerator;
private IFile fIFile;
private ResourceContext resourceContext_;
private StringBuffer fStringBuffer;

/**
 * Build constructor comment.
 */
public GeneratePageCommand()
{
}

/**
* Constructor
* This command will generate code from a Model
* @param model The model to be traversed
* @param generator The code generator to be used
* @param resource the resource to place the finished product
*/
public GeneratePageCommand(ResourceContext context, Model model, Generator generator, IFile file)
{
  model_ = model;
  fGenerator = generator;
  fIFile = file;
  resourceContext_ = context;
}

public Model getJavaDataModel()
{
  return model_;
}

/**
 *
 */
public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
{
  Environment env = getEnvironment();
  
  IStatus status = Status.OK_STATUS;
  try {
    fGenerator.visit(model_.getRootElement());
    fStringBuffer = fGenerator.getStringBuffer();
    String tempString = fStringBuffer.toString();
    OutputStream fileResource = FileResourceUtils.newFileOutputStream(resourceContext_, fIFile.getFullPath(), monitor, env.getStatusHandler());
    //PrintStream ps = new PrintStream(fileResource);
    //ps.print(tempString);
    OutputStreamWriter osw = new OutputStreamWriter(fileResource,"UTF-8");
    osw.write(tempString,0,fStringBuffer.length());  
    osw.close();
    fileResource.close();
    return status;
  } catch (IOException ioexc) {
  	status = StatusUtils.errorStatus( ioexc );
  	return status;
  }
}

public void setRootElement(Element rootElement)
{
}

}

