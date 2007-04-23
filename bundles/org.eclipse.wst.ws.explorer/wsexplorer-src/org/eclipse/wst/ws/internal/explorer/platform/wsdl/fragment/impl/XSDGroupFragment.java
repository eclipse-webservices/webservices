/*******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;


import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDGroupFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDWildcard;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class XSDGroupFragment extends XSDMapFragment implements IXSDGroupFragment {
  private XSDModelGroup xsdModelGroup_;
  private Vector groupIDsOrder_;
  private Hashtable groupIDs_;
  
  public XSDGroupFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller, XSDModelGroup xsdModelGroup) {
    super(id, name, config, controller);
    xsdModelGroup_ = xsdModelGroup;
    groupIDsOrder_ = new Vector();
    groupIDs_ = new Hashtable();
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    boolean valuesValid = true;
    String[] params = parser.getParameterValues(getID());
    Vector frags = new Vector();
    Vector groupIDsOrderCopy = new Vector();
    Hashtable groupIDsCopy = new Hashtable();
    for (int i = 0; params != null && i < params.length; i++) {
      if (params[i] != null) {
        groupIDsOrderCopy.add(params[i]);
        groupIDsCopy.put(params[i], getGroupMemberIDs(params[i]));
        IXSDFragment[] groupMemberFragments = getGroupMemberFragments(params[i]);
        for (int j = 0; j < groupMemberFragments.length; j++) {
          if (groupMemberFragments[j] != null) {
            frags.add(groupMemberFragments[j]);
            if (!groupMemberFragments[j].processParameterValues(parser))
              valuesValid = false;
          }
        }
      }
    }
    groupIDsOrder_ = groupIDsOrderCopy;
    groupIDs_ = groupIDsCopy;
    removeAllFragments();
    addFragments(frags);
    return valuesValid;
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    removeGroupIDs();
    boolean paramsValid = true;
    String prevGroupID = null;
    String currGroupID = null;
    int groupMemberIndex = 0;
    for (int i = 0; i < instanceDocuments.length; ) {
      if (currGroupID == null) {
        String newGroupID = createInstance();
        prevGroupID = currGroupID;
        currGroupID = newGroupID;
      }
      Element[] instancePartition = null;
      int j = groupMemberIndex;
      boolean paramsAssigned = false;
      int wildcardFragIndex = -1;
      do {
        IXSDFragment[] groupMemberFrags = getGroupMemberFragments(currGroupID);
        XSDComponent xsdComponent = groupMemberFrags[j].getXSDToFragmentConfiguration().getXSDComponent();
        if (xsdComponent instanceof XSDWildcard) {
          if (wildcardFragIndex < 0)
            wildcardFragIndex = j;
        }
        else if (groupMemberFrags[j].getName().equals(trimPrefix(instanceDocuments[i].getTagName()))) {
          instancePartition = getInstanceDocumentPartition(instanceDocuments, i, groupMemberFrags[j].getXSDToFragmentConfiguration().getMaxOccurs());
          if (!groupMemberFrags[j].setParameterValuesFromInstanceDocuments(instancePartition))
            paramsValid = false;
          paramsAssigned = true;
        }
        else if(groupMemberFrags[j] instanceof XSDGroupFragment) {
        	groupMemberFrags[j].setParameterValuesFromInstanceDocuments(instanceDocuments);
        }
        
        if (j == groupMemberFrags.length - 1) {
          j = 0;
          String newGroupID;
          if (!paramsAssigned)
            newGroupID = createInstance();
          else
            newGroupID = null;
          prevGroupID = currGroupID;
          currGroupID = newGroupID;
        }
        else
          j++;
      } while (!paramsAssigned && j != groupMemberIndex);
      groupMemberIndex = j;
      if (!paramsAssigned) {
        instancePartition = getInstanceDocumentPartition(instanceDocuments, i, FragmentConstants.UNBOUNDED);
        if (wildcardFragIndex >= 0) {
          if (wildcardFragIndex >= groupMemberIndex) {
            removeGroupID(currGroupID);
            currGroupID = prevGroupID;
          }
          groupMemberIndex = wildcardFragIndex;
          IXSDFragment[] groupMemberFrags = getGroupMemberFragments(currGroupID);
          if (!groupMemberFrags[wildcardFragIndex].setParameterValuesFromInstanceDocuments(instancePartition))
            paramsValid = false;
          paramsAssigned = true;
        }
        else if (currGroupID != null && (prevGroupID == null || !currGroupID.equals(prevGroupID))) {
          paramsValid = false;
          removeGroupID(currGroupID);
          currGroupID = prevGroupID;
        }
      }
      i = i + instancePartition.length;
    }
    return paramsValid;
  }

  private Element[] getInstanceDocumentPartition(Element[] instanceDocuments, int start, int maxSize) {
    String tagName = instanceDocuments[start].getTagName();
    int size = 1;
    for (int i = start + 1; i < instanceDocuments.length; i++) {
      if (maxSize != FragmentConstants.UNBOUNDED && size >= maxSize)
        break;
      else if (instanceDocuments[i].getTagName().equals(tagName))
        size++;
      else
        break;
    }
    Element[] subset = new Element[size];
    for (int j = 0; j < subset.length; j++) {
      subset[j] = instanceDocuments[start + j];
    }
    return subset;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc) {
    IXSDFragment[] groupMemberFragments = getAllFragments();
    Vector instanceDocumentsCopy = new Vector();
    for (int i = 0; i < groupMemberFragments.length; i++) {
      Element[] fragmentInstances = groupMemberFragments[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      for (int j = 0; j < fragmentInstances.length; j++) {
        instanceDocumentsCopy.add(fragmentInstances[j]);
      }
    }
    Element[] instanceDocuments = new Element[instanceDocumentsCopy.size()];
    for (int k = 0; k < instanceDocuments.length; k++) {
      instanceDocuments[k] = (Element)instanceDocumentsCopy.get(k);
    }
    return instanceDocuments;
  }

  public void setXSDModelGroup(XSDModelGroup xsdModelGroup) {
    xsdModelGroup_ = xsdModelGroup;
  }

  public XSDModelGroup getXSDModelGroup() {
    return xsdModelGroup_;
  }

  public String[] getGroupIDs() {
    String[] groupIDs = new String[groupIDsOrder_.size()];
    for (int i = 0; i < groupIDs.length; i++) {
      groupIDs[i] = (String)groupIDsOrder_.get(i);
    }
    return groupIDs;
  }

  protected void removeGroupIDs() {
    removeAllFragments();
    groupIDsOrder_.clear();
    groupIDs_.clear();
  }

  protected void removeGroupID(String groupID) {
    String[] memberFragIDs = getGroupMemberIDs(groupID);
    removeFragments(memberFragIDs);
    groupIDsOrder_.remove(groupID);
    groupIDs_.remove(groupID);
  }

  protected void setGroupMemberIDs(String groupID, String[] groupMemberIDs) {
    if (groupID != null) {
      if (!groupIDsOrder_.contains(groupID))
        groupIDsOrder_.add(groupID);
      if (groupMemberIDs != null)
        groupIDs_.put(groupID, groupMemberIDs);
      else
        groupIDs_.put(groupID, new String[0]);
    }
  }

  protected void setGroupMemberFragments(String groupID, IXSDFragment[] groupMemberFragments) {
    addFragments(groupMemberFragments);
    String[] ids = new String[groupMemberFragments.length];
    for (int i = 0; i < ids.length; i++) {
      ids[i] = groupMemberFragments[i].getID();
    }
    setGroupMemberIDs(groupID, ids);
  }

  public String[] getGroupMemberIDs(String groupID) {
    if (groupID != null) {
      String[] groupMemberIDs = (String[])groupIDs_.get(groupID);
      if (groupMemberIDs != null)
        return groupMemberIDs;
    }
    return new String[0];
  }

  public IXSDFragment[] getGroupMemberFragments(String groupID) {
    String[] groupMemberIDs = getGroupMemberIDs(groupID);
    IXSDFragment[] groupMemberFragments = new IXSDFragment[groupMemberIDs.length];
    for (int i = 0; i < groupMemberFragments.length; i++) {
      groupMemberFragments[i] = getFragment(groupMemberIDs[i]);
    }
    return groupMemberFragments;
  }

  protected String createGroupInstance() {
    String groupID = genID();
    EList particles = getXSDModelGroup().getParticles();
    String[] groupMemberIDs = new String[particles.size()];
    for (int i = 0; i < particles.size(); i++) {
      XSDParticle xsdParticle = (XSDParticle)particles.get(i);
      XSDParticleContent xsdParticleContent = xsdParticle.getContent();
      XSDToFragmentConfiguration thisConfig = getXSDToFragmentConfiguration();
      XSDToFragmentConfiguration xsdConfig = new XSDToFragmentConfiguration();
      if (xsdParticleContent instanceof XSDComponent)
        xsdConfig.setXSDComponent((XSDComponent)xsdParticleContent);
      else
        xsdConfig.setXSDComponent(null);
      xsdConfig.setStyle(thisConfig.getStyle());
      xsdConfig.setPartEncoding(thisConfig.getPartEncoding());
      xsdConfig.setWSDLPartName(thisConfig.getWSDLPartName());
      groupMemberIDs[i] = genID();
      addFragment(groupMemberIDs[i], getXSDToFragmentController().getFragment(xsdConfig, groupMemberIDs[i], groupMemberIDs[i]));
    }
    setGroupMemberIDs(groupID, groupMemberIDs);
    return groupID;
  }
}
