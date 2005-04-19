/*******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDGroupAllFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDModelGroup;

import org.w3c.dom.Element;
import java.util.Vector;

public class XSDGroupAllFragment extends XSDGroupFragment implements IXSDGroupAllFragment {
  private String groupID_;

  public XSDGroupAllFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller, XSDModelGroup xsdModelGroup) {
    super(id, name, config, controller, xsdModelGroup);
    groupID_ = null;
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    String groupID = parser.getParameter(getID());
    if (groupID_ != null && groupID != null && groupID_.equals(groupID)) {
      setGroupMemberIDsOrdering(parser.getParameterValues(groupID_));
      boolean valuesValid = true;
      IXSDFragment[] groupMemberFragments = getGroupMemberFragments(groupID_);
      for (int i = 0; i < groupMemberFragments.length; i++) {
        if (groupMemberFragments[i] != null && !groupMemberFragments[i].processParameterValues(parser))
          valuesValid = false;
      }
      return valuesValid;
    }
    else {
      groupID_ = null;
      removeGroupIDs();
      return true;
    }
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    removeGroupIDs();
    boolean paramsValid = true;
    if (instanceDocuments != null && instanceDocuments.length > 0) {
      groupID_ = createInstance();
      IXSDFragment[] groupMemberFrags = getGroupMemberFragments(groupID_);
      String[] groupMemberIDsOrder = new String[groupMemberFrags.length];
      int orderIndex = 0;
      for (int i = 0; i < instanceDocuments.length; i++) {
        boolean paramsAssigned = false;
        for (int j = 0; j < groupMemberFrags.length; j++) {
          if (groupMemberFrags[j] != null && groupMemberFrags[j].getName().equals(trimPrefix(instanceDocuments[i].getTagName()))) {
            Element[] childInstance = {instanceDocuments[i]};
            if (!groupMemberFrags[j].setParameterValuesFromInstanceDocuments(childInstance))
              paramsValid = false;
            groupMemberIDsOrder[orderIndex] = groupMemberFrags[j].getID();
            orderIndex++;
            paramsAssigned = true;
            groupMemberFrags[j] = null;
            break;
          }
        }
        if (!paramsAssigned)
          paramsValid = false;
      }
      for (int k = 0; k < groupMemberFrags.length; k++) {
        if (groupMemberFrags[k] != null) {
          groupMemberIDsOrder[orderIndex] = groupMemberFrags[k].getID();
          orderIndex++;
        }
      }
      setGroupMemberIDsOrdering(groupMemberIDsOrder);
    }
    return paramsValid;
  }

  public String getGroupAllInstance() {
    if (groupID_ == null)
      groupID_ = createGroupInstance();
    return groupID_;
  }

  public String createInstance() {
    return getGroupAllInstance();
  }

  public boolean setGroupMemberIDsOrdering(String[] groupMemberIDs) {
    if (groupMemberIDs == null)
      return false;
    String[] existingGroupMemberIDs = getGroupMemberIDs(groupID_);
    if (existingGroupMemberIDs.length != groupMemberIDs.length)
      return false;
    Vector existingGroupMemberIDsVector = new Vector();
    for (int i = 0; i < existingGroupMemberIDs.length; i++) {
      existingGroupMemberIDsVector.add(existingGroupMemberIDs[i]);
    }
    for (int j = 0; j < groupMemberIDs.length; j++) {
      if (!existingGroupMemberIDsVector.remove(groupMemberIDs[j]))
        return false;
    }
    if (setFragmentsOrder(groupMemberIDs)) {
      setGroupMemberIDs(groupID_, groupMemberIDs);
      return true;
    }
    else
      return false;
  }

  public boolean moveUpGroupMemberID(String groupMemberID) {
    String[] groupMemberIDs = getGroupMemberIDs(groupID_);
    if (groupMemberID.equals(groupMemberIDs[0]))
      return false;
    for (int i = 1; i < groupMemberIDs.length; i++) {
      if (groupMemberID.equals(groupMemberIDs[i])) {
        groupMemberIDs[i] = groupMemberIDs[i - 1];
        groupMemberIDs[i - 1] = groupMemberID;
        if (setFragmentsOrder(groupMemberIDs)) {
          setGroupMemberIDs(groupID_, groupMemberIDs);
          return true;
        }
        else
          return false;
      }
    }
    return false;
  }

  public boolean moveDownGroupMemberID(String groupMemberID) {
    String[] groupMemberIDs = getGroupMemberIDs(groupID_);
    if (groupMemberID.equals(groupMemberIDs[groupMemberIDs.length - 1]))
      return false;
    for (int i = 0; i < groupMemberIDs.length - 1; i++) {
      if (groupMemberID.equals(groupMemberIDs[i])) {
        groupMemberIDs[i] = groupMemberIDs[i + 1];
        groupMemberIDs[i + 1] = groupMemberID;
        if (setFragmentsOrder(groupMemberIDs)) {
          setGroupMemberIDs(groupID_, groupMemberIDs);
          return true;
        }
        else
          return false;
      }
    }
    return false;
  }

  public String getInformationFragment() {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDGroupRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDGroupAllWFragmentJSP.jsp";
  }
}
