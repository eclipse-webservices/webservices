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
package org.eclipse.wst.command.internal.provisional.env.core.uri;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.common.environment.uri.URI;
import org.eclipse.wst.common.environment.uri.URIException;

public abstract class NativeFileCommand extends AbstractDataModelOperation
{
  protected String name;

  protected String description;

  protected URI[] urisToRead;

  protected URI[] urisToWrite;

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

  public void setURIsToRead ( URI[] urisToRead )
  {
    this.urisToRead = urisToRead;
  }

  public URI[] getURIsToRead ()
  {
    return urisToRead;
  }

  public void setURIsToWrite ( URI[] urisToWrite )
  {
    this.urisToWrite = urisToWrite;
  }

  public URI[] getURIsToWrite ()
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

  private File[] getFiles ( URI[] uris )
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
