/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

public class ImportAddResourceListener implements IResourceChangeListener, IResourceDeltaVisitor
{
  Vector importedFiles;

  ImportAddResourceListener()
  {
    importedFiles = new Vector(); 
  }
  
  public void resourceChanged(IResourceChangeEvent event)
  {
    IResourceDelta resourceDelta = event.getDelta();
    
    try
    {
      if (resourceDelta != null) 
      {
        resourceDelta.accept(this);
      }
    }
    catch (Exception e)
    {
//      B2BGUIPlugin.getPlugin().getMsgLogger().write("Exception caught during resource change" + e);
//      B2BGUIPlugin.getPlugin().getMsgLogger().writeCurrentThread(); 
    }      
  }

  public boolean visit(IResourceDelta delta)
  {
    if (delta.getKind() == IResourceDelta.ADDED)
    {
      if (delta.getResource() instanceof IFile) 
        importedFiles.add(delta.getResource());
    }
    return true;
  }

  public Collection getImportedFiles()
  {
    return importedFiles;
  }
    
  // This returns the first imported file in the list of imported files
  public IFile getImportedFile()
  {
    if (importedFiles.isEmpty() == false) 
      return (IFile)importedFiles.firstElement();
  
    return null;
  }
}
