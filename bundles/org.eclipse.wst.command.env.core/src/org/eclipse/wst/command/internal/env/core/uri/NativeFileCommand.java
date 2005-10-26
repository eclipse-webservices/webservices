/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.core.uri;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.environment.uri.IURI;
import org.eclipse.wst.common.environment.uri.URIException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public abstract class NativeFileCommand extends AbstractDataModelOperation
{
  protected String name;

  protected String description;

  protected IURI[] urisToRead;

  protected IURI[] urisToWrite;

  protected NativeFileCommand ()
  {
    this(null,null);
  }

  protected NativeFileCommand ( String name, String description )
  {
    this.name = name;
    this.description = description;
  }

  public String getName ()
  {
    return name;
  }

  public String getDescription ()
  {
    return description;
  }

  public void setURIsToRead ( IURI[] urisToRead )
  {
    this.urisToRead = urisToRead;
  }

  public IURI[] getURIsToRead ()
  {
    return urisToRead;
  }

  public void setURIsToWrite ( IURI[] urisToWrite )
  {
    this.urisToWrite = urisToWrite;
  }

  public IURI[] getURIsToWrite ()
  {
    return urisToWrite;
  }

  public IStatus execute ( IProgressMonitor monitor, IAdaptable adaptable )
  {
    File[] filesToRead = getFiles(urisToRead);
    File[] filesToWrite = getFiles(urisToWrite);
    preProcess(filesToRead,filesToWrite);
    IStatus status = execute(filesToRead,filesToWrite);
    postProcess(filesToRead,filesToWrite);
    return status;
  }

  public abstract IStatus execute ( File[] filesToRead, File[] filesToWrite );

  private void preProcess ( File[] filesToRead, File[] filesToWrite )
  {
    // TBD.
  }

  private void postProcess ( File[] filesToRead, File[] filesToWrite )
  {
    // TBD.
  }

  private File[] getFiles ( IURI[] uris )
  {
    List list = new LinkedList();
    if (uris != null)
    {
      for (int i=0; i<uris.length; i++)
      {
        if (uris[i].isAvailableAsFile())
        {
          try
          {
            list.add(uris[i].asFile());
          }
          catch (URIException e)
          {
          }
        }
      }
    }
    return (File[])list.toArray(new File[0]);
  }
}
