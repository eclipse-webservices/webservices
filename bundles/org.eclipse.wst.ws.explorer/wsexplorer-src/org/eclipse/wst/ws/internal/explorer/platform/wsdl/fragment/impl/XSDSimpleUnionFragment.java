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


import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDSimpleUnionFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDSimpleTypeDefinition;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Vector;
import java.util.Hashtable;

public abstract class XSDSimpleUnionFragment extends XSDMapFragment implements IXSDSimpleUnionFragment {
  private XSDSimpleTypeDefinition[] memberTypes_;

  public XSDSimpleUnionFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller) {
    super(id, name, config, controller);
    memberTypes_ = null;
  }

  public void setName(String name) {
    super.setName(name);
    IXSDFragment[] fragments = getAllFragments();
    for (int i = 0; i < fragments.length; i++) {
      fragments[i].setName(name);
    }
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    int numTypes = getMemberTypeDefinitions().length;
    boolean paramsValid = internalEquals(instanceDocumentsCopy, instanceDocuments);
    Vector memberFragments = new Vector();
    for (int i = 0; i < instanceDocumentsCopy.length; i++) {
      IXSDFragment compatibleFrag = null;
      for (int j = 0; j < numTypes; j++) {
        IXSDFragment memberFrag = getFragment(createUnionInstance(j));
        Element[] memberInstanceDocument = new Element[1];
        memberInstanceDocument[0] = setElementTagName(instanceDocumentsCopy[i], memberFrag.getName());
        if (memberFrag.setParameterValuesFromInstanceDocuments(memberInstanceDocument)) {
          if (memberFrag.validateAllParameterValues()) {
            compatibleFrag = memberFrag;
            break;
          }
          else if (compatibleFrag == null)
            compatibleFrag = memberFrag;
        }
      }
      if (compatibleFrag != null)
        memberFragments.add(compatibleFrag);
      else
        paramsValid = false;
    }
    removeAllFragments();
    addFragments(memberFragments);
    return paramsValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc) {
    IXSDFragment[] fragments = getAllFragments();
    Vector instanceDocumentsCopy = new Vector();
    for (int i = 0; i < fragments.length; i++) {
      Element[] fragmentInstances = fragments[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      if (fragmentInstances.length > 0)
        instanceDocumentsCopy.add(fragmentInstances[0]);
    }
    Element[] instanceDocuments = new Element[instanceDocumentsCopy.size()];
    for (int i = 0; i < instanceDocuments.length; i++) {
      instanceDocuments[i] = (Element)instanceDocumentsCopy.get(i);
    }
    return setElementsTagName(instanceDocuments, getInstanceDocumentTagName(namespaceTable));
  }

  public XSDSimpleTypeDefinition[] getMemberTypeDefinitions() {
    if (memberTypes_ == null) {
      EList memberTypes = ((XSDSimpleTypeDefinition)getXSDTypeDefinition()).getMemberTypeDefinitions();
      memberTypes_ = new XSDSimpleTypeDefinition[memberTypes.size()];
      for (int i = 0; i < memberTypes_.length; i++) {
        memberTypes_[i] = (XSDSimpleTypeDefinition)memberTypes.get(i);
      }
    }
    return memberTypes_;
  }

  public String createUnionInstance(int memberTypeIndex) {
    getMemberTypeDefinitions();
    if (memberTypeIndex < 0 || memberTypeIndex > memberTypes_.length - 1)
      return null;
    String newID = genID();
    XSDToFragmentConfiguration thisConfig = getXSDToFragmentConfiguration();
    XSDToFragmentConfiguration xsdConfig = new XSDToFragmentConfiguration();
    xsdConfig.setXSDComponent(memberTypes_[memberTypeIndex]);
    xsdConfig.setStyle(thisConfig.getStyle());
    xsdConfig.setPartEncoding(thisConfig.getPartEncoding());
    xsdConfig.setWSDLPartName(thisConfig.getWSDLPartName());
    IXSDFragment frag = getXSDToFragmentController().getFragment(xsdConfig, newID, getName());
    frag.setID(newID);
    frag.setName(getName());
    addFragment(newID, frag);
    return newID;
  }

  public String createInstance() {
    return createUnionInstance(0);
  }

}
