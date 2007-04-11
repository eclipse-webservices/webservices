/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070112   165721 makandre@ca.ibm.com - Andrew Mak, WSDL import cannot use relative import with to parent directories
 * 20070125   171071 makandre@ca.ibm.com - Andrew Mak, Create public utility method for copying WSDL files
 * 20070409   181635 makandre@ca.ibm.com - Andrew Mak, WSDLCopier utility should create target folder
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.command.common;

import javax.wsdl.Definition;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;
import org.eclipse.wst.ws.internal.util.WSDLCopier;

/**
 * @deprecated Use {@link WSDLCopier} instead. 
 */
public class CopyWSDLTreeCommand extends AbstractDataModelOperation
{
  private String wsdlURI;
  private WebServicesParser webServicesParser;
  private String destinationURI;
  private Definition def;
  private String wsdlRelPath;
  
  /**
   * Execute the command
   */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    
    try
    {
      WSDLCopier copier = new WSDLCopier(webServicesParser);        
      copier.setSourceURI(wsdlURI, def);
      copier.setTargetFolderURI(getBaseURI(destinationURI));
      copier.setTargetFilename(getLocalname(destinationURI));
        
      ResourceUtils.getWorkspace().run(copier, monitor);
        
      wsdlRelPath = copier.getRelativePath().toString();

      return Status.OK_STATUS;
    }
    catch (CoreException ce)
    {
        IStatus status = ce.getStatus();
		env.getStatusHandler().reportError(status);
      return status;
    }
  }

  private String getBaseURI(String uri)
  {
    int index = uri.lastIndexOf('/');
    if (index == -1)
      index = uri.lastIndexOf('\\');
    if (index != -1)
      return uri.substring(0, index + 1);
    else
      return null;
  }

  private String getLocalname(String uri)
  {
    int index = uri.lastIndexOf('/');
    if (index == -1)
      index = uri.lastIndexOf('\\');
    if (index != -1)
      return uri.substring(index + 1);
    else
      return uri;
  }
 	  
  public void setWsdlURI(String wsdlURI)
  {
    this.wsdlURI = wsdlURI;
  }

  public void setWebServicesParser(WebServicesParser webServicesParser)
  {
    this.webServicesParser = webServicesParser;
  }

  public void setDestinationURI(String destinationURI)
  {
    this.destinationURI = destinationURI;
  }

  public void setDefinition(Definition def)
  {
    this.def = def;
  }
  
  public String getWSDLRelPath() {
    return wsdlRelPath;
  }
}
