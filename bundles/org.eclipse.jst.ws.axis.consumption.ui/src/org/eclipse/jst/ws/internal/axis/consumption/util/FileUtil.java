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
package org.eclipse.jst.ws.internal.axis.consumption.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class FileUtil
{
  public FileUtil()
  {
    super();
  }

  public static void copyFile(String src, String dest)
  {
    InputStream is = null;
    FileOutputStream fos = null;
    try
    {
      is = new FileInputStream(src);
      byte buff[] = new byte[1024];
      fos = new FileOutputStream(dest);
      int c = 0;
      byte[] array = new byte[1024];
      while ((c = is.read(array)) >= 0)
      {
        fos.write(array, 0, c);
      }
    }
    catch (Exception e)
    {
    }
    finally
    {
      try
      {
        fos.close();
        is.close();
      }
      catch (Exception e)
      {
      }
    }
  }

  public static File createFileAndParentDirectories(String fileName) throws Exception
  {
    File file = new File(fileName);
    File parent = file.getParentFile();
    if (!parent.exists())
    {
      parent.mkdirs();
    }
    file.createNewFile();
    return file;
  }

  public static void deleteDirectories(File dir) throws Exception
  {
    File[] children = dir.listFiles();
    for (int i = 0; i < children.length; i++)
    {
      if (children[i].list() != null && children[i].list().length > 0)
      {
        deleteDirectories(children[i]);
      }
      else
      {
        children[i].delete();
      }
    }
    dir.delete();
  }

  /**
   * Creates a folder and all parent folders if not existing Project must exist
   */
  public static void createFolder(IFolder folder, boolean force, boolean local) throws CoreException
  {
    if (!folder.exists())
    {
      IContainer parent = folder.getParent();
      if (parent instanceof IFolder)
      {
        createFolder((IFolder)parent, force, local);
      }
      folder.create(force, local, new NullProgressMonitor());
    }
  }

  public static void createTargetFile(String sourceFileName, String targetFileName) throws Exception
  {
    createTargetFile(sourceFileName, targetFileName, false);
  }

  public static void createTargetFile(String sourceFileName, String targetFileName, boolean overwrite) throws Exception
  {
    File idealResultFile = new File(targetFileName);
    if (overwrite || !idealResultFile.exists())
    {
      FileUtil.createFileAndParentDirectories(targetFileName);
      FileUtil.copyFile(sourceFileName, targetFileName);
    }
  }
}
