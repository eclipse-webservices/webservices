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
import java.util.List;
import java.util.Vector;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDComplexFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.xsd.XSDComplexFinal;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDModelGroupDefinition;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XSDComplexFragment extends XSDMapFragment implements IXSDComplexFragment
{
  public XSDComplexFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller)
  {
    super(id, name, config, controller);
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments)
  {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    boolean paramsValid = internalEquals(instanceDocumentsCopy, instanceDocuments);
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)getXSDTypeDefinition();
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    if (complexTypeContent instanceof XSDTypeDefinition)
      return setParamsForXSDTypeDef(instanceDocumentsCopy) && paramsValid;
    else
      return setParamsForXSDParticle(instanceDocumentsCopy) && paramsValid;
  }

  private boolean setParamsForXSDTypeDef(Element[] instanceDocuments)
  {
    boolean paramsValid = true;
    removeAllFragments();
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      IXSDFragment childFrag = getFragment(createInstance());
      Element[] childInstanceDocuments = new Element[1];
      childInstanceDocuments[0] = instanceDocuments[i];
      if (!childFrag.setParameterValuesFromInstanceDocuments(setElementsTagName(childInstanceDocuments, childFrag.getName())))
        paramsValid = false;
    }
    return paramsValid;
  }

  private boolean setParamsForXSDParticle(Element[] instanceDocuments)
  {
    boolean paramsValid = true;
    removeAllFragments();
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      IXSDFragment childFrag = getFragment(createInstance());
      Vector instancesVector = new Vector();
      NodeList nl = instanceDocuments[i].getChildNodes();
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
      if (!childFrag.setParameterValuesFromInstanceDocuments(childInstanceDocuments))
        paramsValid = false;
    }
    return paramsValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc)
  {
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)getXSDTypeDefinition();
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    Element[] instanceDocuments;
    String tagName = getInstanceDocumentTagName(namespaceTable);
    if (complexTypeContent instanceof XSDTypeDefinition)
      instanceDocuments = genInstancesForXSDTypeDef(genXSIType, namespaceTable, tagName, doc);
    else
      instanceDocuments = genInstancesForXSDParticle(genXSIType, namespaceTable, tagName, doc);
    return (genXSIType ? addXSIType(instanceDocuments, namespaceTable) : instanceDocuments);
  }

  private Element[] genInstancesForXSDTypeDef(boolean genXSIType, Hashtable namespaceTable, String tagName, Document doc)
  {
    IXSDFragment[] fragments = getAllFragments();
    Vector instanceDocumentsCopy = new Vector();
    for (int i = 0; i < fragments.length; i++)
    {
      Element[] fragmentInstances = fragments[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      if (fragmentInstances.length > 0)
        instanceDocumentsCopy.add(fragmentInstances[0]);
    }
    Element[] instanceDocuments = new Element[instanceDocumentsCopy.size()];
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      instanceDocuments[i] = (Element)instanceDocumentsCopy.get(i);
    }
    return setElementsTagName(instanceDocuments, tagName);
  }

  private Element[] genInstancesForXSDParticle(boolean genXSIType, Hashtable namespaceTable, String tagName, Document doc)
  {
    IXSDFragment[] childFrags = getAllFragments();
    Element[] instanceDocuments = new Element[childFrags.length];
    for (int i = 0; i < instanceDocuments.length; i++)
    {
      Element[] childInstances = childFrags[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
      Element instanceDocument = doc.createElement(tagName);
      for (int j = 0; j < childInstances.length; j++)
      {
        if (childInstances[j] != null)
          instanceDocument.appendChild(childInstances[j]);
      }
      instanceDocuments[i] = instanceDocument;
    }
    return instanceDocuments;
  }

  private XSDModelGroup getXSDModelGroup(XSDParticle xsdParticle)
  {
    if (xsdParticle != null)
    {
      XSDParticleContent xsdParticleContent = xsdParticle.getContent();
      if (xsdParticleContent != null)
      {
        if (xsdParticleContent instanceof XSDModelGroupDefinition)
        {
          XSDModelGroupDefinition xsdModelGroupDef = (XSDModelGroupDefinition)xsdParticleContent;
          if (xsdModelGroupDef.isModelGroupDefinitionReference())
            xsdModelGroupDef = xsdModelGroupDef.getResolvedModelGroupDefinition();
          return xsdModelGroupDef.getModelGroup();
        }
        else if (xsdParticleContent instanceof XSDModelGroup)
          return (XSDModelGroup)xsdParticleContent;
      }
    }
    return null;
  }

  private List getInheritedParticles(XSDComplexTypeDefinition complexType)
  {
    Vector v = new Vector();
    if (complexType.getDerivationMethod().getValue() == XSDComplexFinal.EXTENSION)
    {
      XSDTypeDefinition extType = complexType.getBaseType();
      if (extType != null && !(extType instanceof XSDComplexTypeDefinition))
      {
        String namespace = extType.getTargetNamespace();
        String localname = extType.getName();
        if (namespace != null && localname != null)
        {
          XSDNamedComponent xsdNamedComp = getXSDToFragmentController().getWSDLPartsToXSDTypeMapper().getXSDTypeFromSchema(namespace, localname, false);
          if (xsdNamedComp instanceof XSDComplexTypeDefinition)
          {
            extType = (XSDComplexTypeDefinition)xsdNamedComp;
          }
        }
      }
      if (extType != null && extType != complexType && extType instanceof XSDComplexTypeDefinition)
      {
        XSDComplexTypeContent extTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent((XSDComplexTypeDefinition)extType);
        if (extTypeContent instanceof XSDParticle)
        {
          XSDModelGroup xsdModelGroup = getXSDModelGroup((XSDParticle)extTypeContent);
          if (xsdModelGroup != null)
          {
            v.addAll(getInheritedParticles((XSDComplexTypeDefinition)extType));
            v.addAll(xsdModelGroup.getParticles());
          }
        }
      }
    }
    return v;
  }

  private void gatherInheritedParticles(XSDComplexTypeDefinition complexType)
  {
    if (complexType.getDerivationMethod().getValue() == XSDComplexFinal.EXTENSION)
    {
      XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
      if (complexTypeContent instanceof XSDParticle)
      {
        XSDModelGroup xsdModelGroup = getXSDModelGroup((XSDParticle)complexTypeContent);
        if (xsdModelGroup != null)
        {
          List inheritedParticles = getInheritedParticles(complexType);
          List particles = xsdModelGroup.getParticles();
          particles.addAll(0, inheritedParticles);
        }
      }
    }
  }

  public String createComplexInstance()
  {
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)getXSDTypeDefinition();
    gatherInheritedParticles(complexType);
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    XSDToFragmentConfiguration thisConfig = getXSDToFragmentConfiguration();
    XSDToFragmentConfiguration xsdConfig = new XSDToFragmentConfiguration();
    xsdConfig.setXSDComponent(complexTypeContent);
    xsdConfig.setStyle(thisConfig.getStyle());
    xsdConfig.setPartEncoding(thisConfig.getPartEncoding());
    xsdConfig.setWSDLPartName(thisConfig.getWSDLPartName());
    String newID = genID();
    addFragment(newID, getXSDToFragmentController().getFragment(xsdConfig, newID, newID));
    return newID;
  }

  public String createInstance()
  {
    return createComplexInstance();
  }
}
