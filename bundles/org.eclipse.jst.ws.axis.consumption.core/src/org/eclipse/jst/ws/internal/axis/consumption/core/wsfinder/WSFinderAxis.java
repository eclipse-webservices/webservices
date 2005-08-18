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

package org.eclipse.jst.ws.internal.axis.consumption.core.wsfinder;

import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.internal.consumption.wsfinder.WSFinderCommon;

public class WSFinderAxis extends WSFinderCommon
{
  public WSFinderAxis()
  {
    super();
  }

  public List find()
  {
    final Vector wsdlURLs = new Vector();
//  TODO Remove old Nature references
//    IProject[] projects = getWorkspaceProjects();
//    for (int i = 0; i < projects.length; i++)
//    {
//      J2EEWebNatureRuntime webNature = getWebNature(projects[i]);
//      if (webNature != null)
//      {
//        final String webProjectURL = ResourceUtils.getWebProjectURL(projects[i]);
//        if (webProjectURL != null && webProjectURL.length() > 0)
//        {
//          final IFolder folderWSDL = getFolderRootPublishable(webNature).getFolder("wsdl");
//          try
//          {
//            folderWSDL.accept(
//              new IResourceVisitor()
//              {
//                public boolean visit(IResource resource)
//                {
//                  if (resource.getType() == IResource.FILE)
//                  {
//                    String ext = resource.getFileExtension();
//                    if (ext != null && ext.equalsIgnoreCase("wsdl"))
//                    {
//                      String resPath = resource.getFullPath().toString();
//                      String folderPath = folderWSDL.getFullPath().toString();
//                      int index = resPath.indexOf(folderPath);
//                      if (index != -1)
//                        resPath = resPath.substring(index+folderPath.length(), resPath.length());
//                      StringBuffer sb = new StringBuffer(webProjectURL);
//                      sb.append("/wsdl");
//                      sb.append(resPath);
//                      wsdlURLs.add(sb.toString());
//                    }
//                  }
//                  return true;
//                }
//              }
//            );
//          }
//          catch (CoreException ce)
//          {
//          }
//        }
//      }
//    }
//    LiveWSDLFilter[] filters = new LiveWSDLFilter[wsdlURLs.size()];
//    for (int i = 0; i < filters.length; i++)
//    {
//      filters[i] = new LiveWSDLFilter((String)wsdlURLs.get(i));
//      filters[i].start();
//    }
//    for (int i = 0; i < filters.length; i++)
//    {
//      if (!filters[i].isFinish())
//      {
//        Thread.yield();
//        i = -1;
//      }
//    }
//    for (int i = 0; i < filters.length; i++)
//    {
//      if (!filters[i].isWSDLLive())
//        wsdlURLs.remove(filters[i].getWSDLURL());
//    }
    return wsdlURLs;
  }
}
