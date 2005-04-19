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
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.actions;

import org.eclipse.wst.ws.internal.explorer.platform.engine.transformer.ITransformer;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.transformer.FragmentTransformer;

import javax.wsdl.*;
import java.util.*;

public class InvokeWSDLSOAPOperationFormAction extends InvokeWSDLSOAPOperationAction
{
  public InvokeWSDLSOAPOperationFormAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    boolean resultsValid = true;
    super.processParsedResults(parser);
    String sourceContent = parser.getParameter(FragmentConstants.SOURCE_CONTENT);
    if (sourceContent == null)
    {
      WSDLPerspective wsdlPerspective = controller_.getWSDLPerspective();
      MessageQueue messageQueue = wsdlPerspective.getMessageQueue();
      WSDLOperationElement operElement = (WSDLOperationElement) getSelectedNavigatorNode().getTreeElement();
      Iterator it = operElement.getOrderedBodyParts().iterator();
      while (it.hasNext())
      {
        Part part = (Part) it.next();
        IFragment frag = operElement.getFragment(part);
        if (!frag.processParameterValues(parser))
        {
          messageQueue.addMessage(wsdlPerspective.getMessage("MSG_ERROR_VALIDATING_PARAMETER", part.getName()));
          resultsValid = false;
        }
      }
    }
    return resultsValid;
  }

  public ITransformer[] getTransformers()
  {
    ITransformer[] parentTransformers = super.getTransformers();
    ITransformer[] transformers = new ITransformer[parentTransformers.length + 1];
    System.arraycopy(parentTransformers, 0, transformers, 0, parentTransformers.length);
    transformers[transformers.length - 1] = new FragmentTransformer(controller_);
    return transformers;
  }
}