package org.eclipse.jst.ws.internal.consumption.ui.widgets.object;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.wst.ws.internal.parser.wsil.WebServicesParser;

/**
 * This class is a very simple wrapper that binds a WSDL parser with
 * a wsdlSelection object.
 */
public class WSDLSelectionWrapper 
{
  public WebServicesParser    parser;
  public IStructuredSelection wsdlSelection;
  
  public WSDLSelectionWrapper( WebServicesParser    parser,
                               IStructuredSelection selection )
  {
    this.parser        = parser;
    this.wsdlSelection = selection;
  }
}
