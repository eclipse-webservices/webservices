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

public interface WSDLTestFinishCommand extends WebServiceTestFinishCommand
{
  public void setServiceProject(String serverProject);
  public void setWsdlServiceURL(String wsdlURI);
  public void setExternalBrowser(boolean external); 
}
