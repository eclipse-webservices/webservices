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

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDGroupChoiceFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class XSDGroupChoiceFragment extends XSDGroupFragment implements IXSDGroupChoiceFragment {
  private XSDParticle[] choices_;
  private Hashtable choiceIndexes_;

  public XSDGroupChoiceFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller, XSDModelGroup xsdModelGroup) {
    super(id, name, config, controller, xsdModelGroup);
    choices_ = null;
    choiceIndexes_ = new Hashtable();
  }

  public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
    super.processParameterValues(parser);
    String[] groupIDs = getGroupIDs();
    choiceIndexes_.clear();
    boolean paramsValid = true;
    for (int i = 0; i < groupIDs.length; i++) {
      String selectedChoiceFragID = parser.getParameter(groupIDs[i]);
      int choiceIndex = 0;
      IXSDFragment[] choiceFrags = getGroupMemberFragments(groupIDs[i]);
      for (int j = 0; j < choiceFrags.length; j++) {
        if (choiceFrags[j].getID().equals(selectedChoiceFragID)) {
          choiceIndex = j;
          paramsValid = (paramsValid && choiceFrags[j].validateAllParameterValues());
          break;
        }
      }
      setChoiceIndex(groupIDs[i], choiceIndex);
    }
    return paramsValid;
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    removeGroupIDs();
    choiceIndexes_.clear();
    boolean paramsValid = true;
    if (instanceDocuments != null) {
      for (int i = 0; i < instanceDocuments.length; i++) {
        String groupID = createGroupChoiceInstance(0);
        IXSDFragment[] choiceFrags = getGroupMemberFragments(groupID);
        for (int j = 0; j < choiceFrags.length; j++) {
          if (choiceFrags[j].getName().equals(trimPrefix(instanceDocuments[i].getTagName()))) {
            XSDToFragmentConfiguration xsdConfig = choiceFrags[j].getXSDToFragmentConfiguration();
            Element[] childInstances = getInstanceDocumentPartition(instanceDocuments, i, xsdConfig.getMinOccurs(), xsdConfig.getMaxOccurs());
            if (!choiceFrags[j].setParameterValuesFromInstanceDocuments(childInstances))
              paramsValid = false;
            setChoiceIndex(groupID, j);
            if (childInstances.length > 0)
              i = i + childInstances.length - 1; // minus 1 because there's an i++ in the outer for loop
            break;
          }
        }
      }
    }
    return paramsValid;
  }

  private Element[] getInstanceDocumentPartition(Element[] instanceDocuments, int start, int minSize, int maxSize) {
    String tagName = instanceDocuments[start].getTagName();
    int size = 1;
    for (int i = start + 1; i < instanceDocuments.length; i++) {
      if (instanceDocuments[i].getTagName().equals(tagName))
        size++;
      else
        break;
    }
    if (maxSize != FragmentConstants.UNBOUNDED && size > maxSize) {
      size = size - minSize;
      if (size > maxSize)
        size = maxSize;
    }
    Element[] subset = new Element[size];
    for (int j = 0; j < subset.length; j++) {
      subset[j] = instanceDocuments[start + j];
    }
    return subset;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc) {
    Vector instanceDocumentsCopy = new Vector();
    String[] groupIDs = getGroupIDs();
    for (int i = 0; i < groupIDs.length; i++) {
      IXSDFragment[] choiceFrags = getGroupMemberFragments(groupIDs[i]);
      int choiceIndex = getChoiceIndex(groupIDs[i]);
      Element[] choiceFragInstances = choiceFrags[choiceIndex].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      for (int j = 0; j < choiceFragInstances.length; j++) {
        instanceDocumentsCopy.add(choiceFragInstances[j]);
      }
    }
    Element[] instanceDocuments = new Element[instanceDocumentsCopy.size()];
    instanceDocumentsCopy.copyInto(instanceDocuments);
    return instanceDocuments;
  }

  public XSDParticle[] getChoices() {
    if (choices_ == null) {
      EList particles = getXSDModelGroup().getParticles();
      choices_ = new XSDParticle[particles.size()];
      for (int i = 0; i < choices_.length; i++) {
        choices_[i] = (XSDParticle)particles.get(i);
      }
    }
    return choices_;
  }

  public String createGroupChoiceInstance(int choiceIndex) {
    getChoices();
    if (choiceIndex < 0 || choiceIndex > choices_.length - 1)
      return null;
    String groupID = createGroupInstance();
    setChoiceIndex(groupID, choiceIndex);
    return groupID;
  }

  public String createInstance() {
    return createGroupChoiceInstance(0);
  }

  public int getChoiceIndex(String groupID) {
    Integer index = (Integer)choiceIndexes_.get(groupID);
    if (index != null)
      return index.intValue();
    else
      return 0;
  }

  public void setChoiceIndex(String groupID, int choiceIndex) {
    choiceIndexes_.put(groupID, new Integer(choiceIndex));
  }

  public boolean validateAllParameterValues()
  {
    String[] groupIDs = getGroupIDs();
    for (int i = 0; i < groupIDs.length; i++)
    {
      int selectionIndex = getChoiceIndex(groupIDs[i]);
      if (!getGroupMemberFragments(groupIDs[i])[selectionIndex].validateAllParameterValues())
        return false;
    }
    return true;
  }
}
