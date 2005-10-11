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

import java.util.Iterator;
import java.util.Vector;
import javax.wsdl.Part;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.Node;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.WSDLModelConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.datamodel.WSDLOperationElement;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.perspective.InvokeWSDLOperationTool;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SynchronizeFragmentViewsAction extends WSDLPropertiesFormAction
{
  public SynchronizeFragmentViewsAction(Controller controller)
  {
    super(controller);
  }

  protected boolean processParsedResults(MultipartFormDataParser parser) throws MultipartFormDataException
  {
    super.processParsedResults(parser);
    Node selectedNode = getSelectedNavigatorNode();
    WSDLOperationElement operElement = (WSDLOperationElement)selectedNode.getTreeElement();
    Iterator it = operElement.getOrderedBodyParts().iterator();
    InvokeWSDLOperationTool invokeWSDLOperationTool = (InvokeWSDLOperationTool)(selectedNode.getCurrentToolManager().getSelectedTool());
    String viewID = parser.getParameter(FragmentConstants.FRAGMENT_VIEW_ID);
    propertyTable_.put(FragmentConstants.FRAGMENT_VIEW_ID, viewID);
    if (viewID.equals(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE))
    {
      invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_FORM_TO_SOURCE);
      return processFormViewParsedResults(parser, operElement, it);
    }
    else
    {
      invokeWSDLOperationTool.setFragmentViewID(FragmentConstants.FRAGMENT_VIEW_SWITCH_SOURCE_TO_FORM);
      return processSourceViewParsedResults(parser, operElement, it);
    }
  }

  private boolean processFormViewParsedResults(MultipartFormDataParser parser, WSDLOperationElement operElement, Iterator it) throws MultipartFormDataException
  {
    operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT, null);
    operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE, null);
    boolean resultsValid = true;
    while (it.hasNext())
    {
      Part part = (Part)it.next();
      IXSDFragment frag = operElement.getFragment(part);
      if (!frag.processParameterValues(parser))
        resultsValid = false;
    }
    if (resultsValid)
      operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT,null);
    return resultsValid;
  }

  private boolean processSourceViewParsedResults(MultipartFormDataParser parser, WSDLOperationElement operElement, Iterator it) throws MultipartFormDataException
  {
    String sourceContent = parser.getParameter(FragmentConstants.SOURCE_CONTENT);
    if (sourceContent != null)
      operElement.setPropertyAsString(WSDLModelConstants.PROP_SOURCE_CONTENT, sourceContent);
    String[] nsDeclarations = parser.getParameterValues(FragmentConstants.SOURCE_CONTENT_NAMESPACE);
    if (nsDeclarations != null)
      operElement.setPropertyAsObject(WSDLModelConstants.PROP_SOURCE_CONTENT_NAMESPACE,nsDeclarations);
    sourceContent = addRootElement(sourceContent);
    try
    {
      Element sourceElements = XMLUtils.stringToElement(sourceContent);
      NodeList nl = sourceElements.getChildNodes();
      Vector elementsVector = new Vector();
      for (int i = 0; i < nl.getLength(); i++)
      {
        org.w3c.dom.Node node = nl.item(i);
        if (node != null && node instanceof Element)
          elementsVector.add(node);
      }
      Element[] instanceDocuments = new Element[elementsVector.size()];
      elementsVector.copyInto(instanceDocuments);
      boolean sourceElementsValid = true;
      while (it.hasNext())
      {
        Part part = (Part)it.next();
        IXSDFragment frag = operElement.getFragment(part);
        if (!frag.setParameterValuesFromInstanceDocuments(instanceDocuments))
          sourceElementsValid = false;
      }
      return sourceElementsValid;
    }
    catch (Throwable t)
    {
      return false;
    }
  }

  private String addRootElement(String element)
  {
    StringBuffer sb = new StringBuffer();
    sb.append(FragmentConstants.ROOT_ELEMENT_START_TAG);
    sb.append(element);
    sb.append(FragmentConstants.ROOT_ELEMENT_END_TAG);
    return sb.toString();
  }

  public boolean run() {
    return true;
  }
}