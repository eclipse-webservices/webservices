/**
* <copyright>
*
* Licensed Material - Property of IBM
* (C) Copyright IBM Corp. 2000, 2002 - All Rights Reserved.
* US Government Users Restricted Rights - Use, duplication or disclosure
* restricted by GSA ADP Schedule Contract with IBM Corp.
*
* </copyright>
*/

package org.eclipse.jst.ws.internal.ext.test;

import java.util.List;
import org.eclipse.wst.server.core.IServer;

public interface WebServiceTestFinishCommand 
{

  /**
   * If the command needs a server this is the chosen 
   * client serverID
   * @param sampleServerTypeID
   */
  public void setServerTypeID(String serviceServerTypeID);
  
  /**
   * This is the IServer if required
   * @param sampleExistingServer
   */
  public void setExistingServer(IServer serviceExistingServer);
  
  /**
   * This is the endpoints if monitor service is enabled
   * @param endpoints
   */
  public void setEndpoint(List endpoints);
  
}
