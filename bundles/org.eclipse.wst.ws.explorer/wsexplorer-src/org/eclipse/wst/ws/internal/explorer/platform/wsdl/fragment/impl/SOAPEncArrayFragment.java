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

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.ISOAPEncArrayFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.emf.common.util.EList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.Vector;
import java.util.Hashtable;

public abstract class SOAPEncArrayFragment extends XSDGroupSeqFragment implements ISOAPEncArrayFragment
{
  public SOAPEncArrayFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller, XSDModelGroup xsdModelGroup)
  {
    super(id, name, config, controller, xsdModelGroup);
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    removeGroupIDs();
    boolean paramsValid = internalEquals(instanceDocumentsCopy, instanceDocuments);
    for (int i = 0; i < instanceDocumentsCopy.length; i++)
    {
      IXSDFragment soapEncArrayTypeFrag = (getGroupMemberFragments(createSOAPEncArrayInstance()))[0];
      Vector instancesVector = new Vector();
      NodeList nl = instanceDocumentsCopy[i].getChildNodes();
      for (int j = 0; j < nl.getLength(); j++)
      {
        Node node = nl.item(j);
        if (node instanceof Element)
          instancesVector.add(node);
        else
          paramsValid = false;
      }
      Element[] childInstanceDocuments = new Element[instancesVector.size()];
      instancesVector.copyInto(childInstanceDocuments);
      if (!soapEncArrayTypeFrag.setParameterValuesFromInstanceDocuments(setElementsTagName(childInstanceDocuments, soapEncArrayTypeFrag.getName())))
        paramsValid = false;
    }
    return paramsValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc)
  {
    String[] groupIDs = getGroupIDs();
    Element[] instanceDocument = new Element[groupIDs.length];
    StringBuffer attrName = new StringBuffer();
    StringBuffer attrValue = new StringBuffer();
    StringBuffer attrArrayTypeName = new StringBuffer();
    StringBuffer attrArrayTypeValue = new StringBuffer();
    for (int i = 0; i < instanceDocument.length; i++)
    {
      int numInstances = 0;
      instanceDocument[i] = doc.createElement(getInstanceDocumentTagName(namespaceTable));
      IXSDFragment fragment = (getGroupMemberFragments(groupIDs[i]))[0];
      Element[] fragmentInstanceDocuments = fragment.genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      for (int j = 0; j < fragmentInstanceDocuments.length; j++)
      {
        if (fragmentInstanceDocuments[j] != null)
          instanceDocument[i].appendChild(fragmentInstanceDocuments[j]);
        numInstances++;
      }
      String soapURI = FragmentConstants.URI_SOAP;
      String soapPrefix = getPrefixFromNamespaceURI(soapURI, namespaceTable);
      String soapEncArrayTypeURI = getXSDTypeDefinition().getTargetNamespace();
      String soapEncArrayTypePrefix = getPrefixFromNamespaceURI(soapEncArrayTypeURI, namespaceTable);
      // Set the arrayType attribute
      attrName.setLength(0);
      attrName.setLength(0);
      attrName.append(soapPrefix);
      attrName.append(FragmentConstants.COLON);
      attrName.append(FragmentConstants.SOAP_ENC_ARRAY_TYPE);
      attrValue.setLength(0);
      attrValue.append(soapEncArrayTypePrefix);
      attrValue.append(FragmentConstants.COLON);
      attrValue.append(getXSDTypeDefinition().getName());
      attrValue.append(FragmentConstants.LEFT_SQUARE_BRACKET);
      attrValue.append(numInstances);
      attrValue.append(FragmentConstants.RIGHT_SQUARE_BRACKET);
      instanceDocument[i].setAttribute(attrName.toString(), attrValue.toString());
    }
    if (genXSIType)
    {
//	  TODO: Used to call the plugin's ignoreSchemaForSOAPArrays()method.
	  boolean ignoreSchemaForSOAPArrays = true;
      if (ignoreSchemaForSOAPArrays)
      {
        for (int i = 0; i < instanceDocument.length; i++)
        {
          String xsiURI = FragmentConstants.URI_XSI;
          String xsiPrefix = getPrefixFromNamespaceURI(xsiURI, namespaceTable);
          String xsiTypeURI = FragmentConstants.URI_SOAP;
          String xsiTypePrefix = getPrefixFromNamespaceURI(xsiTypeURI, namespaceTable);
          attrName.setLength(0);
          attrName.append(xsiPrefix).append(FragmentConstants.COLON).append(FragmentConstants.XSI_TYPE);
          attrValue.setLength(0);
          attrValue.append(xsiTypePrefix).append(FragmentConstants.COLON);
          attrValue.append(FragmentConstants.QNAME_LOCAL_NAME_ARRAY);
          instanceDocument[i].setAttribute(attrName.toString(), attrValue.toString());
        }
      }
      else
        return addXSIType(instanceDocument, namespaceTable);
    }
    return instanceDocument;
  }

  public String createSOAPEncArrayInstance()
  {
    if (getXSDTypeDefinition() != null)
    {
      String groupID = genID();
      String[] groupMemberID = {genID()};
      int minOccurs = 0;
      int maxOccurs = FragmentConstants.UNBOUNDED;
      XSDElementDeclaration elementDecl = null;
      if (getXSDModelGroup() != null)
      {
        XSDParticle groupOwner = (XSDParticle)getXSDModelGroup().getContainer();
        int groupMin = groupOwner.getMinOccurs();
        int groupMax = groupOwner.getMaxOccurs();
        int elementMin = 0;
        int elementMax = FragmentConstants.UNBOUNDED;
        EList xsdParticles = getXSDModelGroup().getParticles();
        if (xsdParticles.size() > 0)
        {
          XSDParticleContent xsdParticleContent = ((XSDParticle)xsdParticles.get(0)).getContent();
          if (xsdParticleContent instanceof XSDElementDeclaration)
          {
            elementDecl = (XSDElementDeclaration)xsdParticleContent;
            XSDParticle elementOwner = (XSDParticle)elementDecl.getContainer();
            elementMin = elementOwner.getMinOccurs();
            elementMax = elementOwner.getMaxOccurs();
          }
        }
        if (groupMin >= 0 && elementMin >= 0)
          minOccurs = groupMin * elementMin;
        if (groupMax >= 0 && groupMax != FragmentConstants.UNBOUNDED && elementMax >= 0 && elementMax != FragmentConstants.UNBOUNDED)
          maxOccurs = groupMax * elementMax;
      }
      XSDToFragmentConfiguration thisConfig = getXSDToFragmentConfiguration();
      XSDToFragmentConfiguration xsdConfig = new XSDToFragmentConfiguration();
      xsdConfig.setXSDComponent(getXSDTypeDefinition());
      xsdConfig.setMinOccurs(minOccurs);
      xsdConfig.setMaxOccurs(maxOccurs);
      xsdConfig.setStyle(thisConfig.getStyle());
      xsdConfig.setPartEncoding(thisConfig.getPartEncoding());
      xsdConfig.setWSDLPartName(thisConfig.getWSDLPartName());
      String name = (elementDecl != null) ? elementDecl.getName() : getXSDTypeDefinition().getName();
      IXSDFragment frag = getXSDToFragmentController().getFragment(xsdConfig, groupMemberID[0], name);
      addFragment(groupMemberID[0], frag);
      setGroupMemberIDs(groupID, groupMemberID);
      return groupID;
    }
    else
      return null;
  }

  public String createGroupSeqInstance()
  {
    return createSOAPEncArrayInstance();
  }

  public String createInstance()
  {
    return createSOAPEncArrayInstance();
  }
}
