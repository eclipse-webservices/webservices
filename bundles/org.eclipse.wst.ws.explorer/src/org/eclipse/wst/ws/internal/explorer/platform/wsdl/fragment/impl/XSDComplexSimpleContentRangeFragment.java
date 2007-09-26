/**
* <copyright>
*
* Licensed Material - Property of IBM
* (C) Copyright IBM Corp. 2002 - All Rights Reserved.
* US Government Users Restricted Rights - Use, duplication or disclosure
* restricted by GSA ADP Schedule Contract with IBM Corp.
*
* </copyright>
*
* File plugins/com.ibm.etools.webservice.explorer/wsexplorer/src/com/ibm/etools/webservice/explorer/wsdl/fragment/Impl/XSDComplexRangeFragment.java, wsa.etools.ws.explorer, lunar-5.1.2 1
* Version 1.1 03/02/28 15:25:18
*/
package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDAttributeFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentConfiguration;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.XSDToFragmentController;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.util.XSDTypeDefinitionUtil;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeContent;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDComplexSimpleContentRangeFragment extends XSDComplexSimpleContentFixFragment {
  public XSDComplexSimpleContentRangeFragment(String id, String name, XSDToFragmentConfiguration config, XSDToFragmentController controller) {
    super(id, name, config, controller);
  }

  public boolean setParameterValuesFromInstanceDocuments(Element[] instanceDocuments) {
    Element[] instanceDocumentsCopy = getInstanceDocumentsByTagName(instanceDocuments, getName());
    boolean paramsValid = internalEquals(instanceDocumentsCopy, instanceDocuments);
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)getXSDTypeDefinition();
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    if (complexTypeContent instanceof XSDTypeDefinition)
      return setParamsForXSDTypeDef(instanceDocumentsCopy) && paramsValid;
    else
      return setParamsForXSDParticle(instanceDocumentsCopy) && paramsValid;
  }

  private boolean setParamsForAttributes(Element instanceDocument,IXSDFragment frag){
  	boolean paramsValid = true;
  	
  	IXSDAttributeFragment[] attributeFragments = getAllAttributeFragments();
  	for (int i = 0; i < attributeFragments.length; i++){
  	  if(attributeFragments[i].getID().startsWith(frag.getID())){  
  	  	  NamedNodeMap nodeMap = instanceDocument.getAttributes();	
  	      String name = ((XSDAttributeUse)attributeFragments[i].getXSDToFragmentConfiguration().getXSDComponent()).getAttributeDeclaration().getName();
  	      paramsValid = attributeFragments[i].getXSDDelegationFragment().setAttributeParamsFromInstanceDocuments(nodeMap.getNamedItem(name));	
      }
  	}
    return paramsValid;
  }
  
  private boolean setParamsForXSDTypeDef(Element[] instanceDocuments)
  {
    boolean paramsValid = true;
    removeAllFragments();
    removeAllAttributeFragments();
    for (int i = 0; i < instanceDocuments.length; i++) {
      IXSDFragment childFrag = getFragment(createInstance());
      Element[] childInstanceDocuments = new Element[1];
      childInstanceDocuments[0] = instanceDocuments[i];
      if (!childFrag.setParameterValuesFromInstanceDocuments(setElementsTagName(childInstanceDocuments, childFrag.getName())))
        paramsValid = false;
      setParamsForAttributes(childInstanceDocuments[0],childFrag);
    }
    return paramsValid;
  }

  private boolean setParamsForXSDParticle(Element[] instanceDocuments) {
    boolean paramsValid = true;
    removeAllFragments();
    removeAllAttributeFragments();
    for (int i = 0; i < instanceDocuments.length; i++) {
      IXSDFragment childFrag = getFragment(createInstance());
      Vector instancesVector = new Vector();
      
      NodeList nl = instanceDocuments[i].getChildNodes();
      for (int j = 0; j < nl.getLength(); j++) {
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
      setParamsForAttributes(childInstanceDocuments[i],childFrag);
    
    }
    return paramsValid;
  }

  public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable,Document doc) {
    XSDComplexTypeDefinition complexType = (XSDComplexTypeDefinition)getXSDTypeDefinition();
    XSDComplexTypeContent complexTypeContent = XSDTypeDefinitionUtil.getXSDComplexTypeContent(complexType);
    Element[] instanceDocuments;
    String tagName = getInstanceDocumentTagName(namespaceTable);
    if (complexTypeContent instanceof XSDTypeDefinition)
      instanceDocuments = genInstancesForXSDTypeDef(genXSIType, namespaceTable, tagName, doc);
    else
      instanceDocuments = genInstancesForXSDParticle(genXSIType, namespaceTable, tagName, doc);
      if(genXSIType) addXSIType(instanceDocuments, namespaceTable);
  	return instanceDocuments;
  }

  private Element addAttributes(Element instanceDocument, IXSDFragment fragment, Hashtable namespaceTable ){
  	
  	IXSDAttributeFragment[] attributeFragments = getAllAttributeFragments();
    for (int k = 0; k < attributeFragments.length; k++){
      if(attributeFragments[k].getID().startsWith(fragment.getID())){
        String name = ((XSDAttributeUse)attributeFragments[k].getXSDToFragmentConfiguration().getXSDComponent()).getAttributeDeclaration().getName();
        attributeFragments[k].getXSDDelegationFragment().setAttributesOnInstanceDocuments(instanceDocument,name);	
      }
    }
   	
   	return instanceDocument;
  }
  
  private Element[] genInstancesForXSDTypeDef(boolean genXSIType, Hashtable namespaceTable, String tagName,Document doc)
  {
    IXSDFragment[] fragments = getAllFragments();
    Vector instanceDocumentsCopy = new Vector();
    for (int i = 0; i < fragments.length; i++) {
      Element[] fragmentInstances = fragments[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable,doc);
      if (fragmentInstances.length > 0){
      	fragmentInstances[0] = addAttributes(fragmentInstances[0],fragments[i],namespaceTable);
        instanceDocumentsCopy.add(fragmentInstances[0]);
      }
    }
    Element[] instanceDocuments = new Element[instanceDocumentsCopy.size()];
    for (int i = 0; i < instanceDocuments.length; i++) {
      instanceDocuments[i] = (Element)instanceDocumentsCopy.get(i);
    }
    return setElementsTagName(instanceDocuments, tagName);
  }

  private Element[] genInstancesForXSDParticle(boolean genXSIType, Hashtable namespaceTable, String tagName,Document doc) {
    IXSDFragment[] childFrags = getAllFragments();
    Element[] instanceDocuments = new Element[childFrags.length];
    
      for (int i = 0; i < instanceDocuments.length; i++) {
        Element[] childInstances = childFrags[i].genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
        
        Element instanceDocument = doc.createElement(tagName);
        for (int j = 0; j < childInstances.length; j++) {
          if (childInstances[j] != null){
          	addAttributes(childInstances[j],childFrags[i],namespaceTable); 
          	instanceDocument.appendChild(doc.importNode(childInstances[j], true));
          }
        }
        instanceDocuments[i] = instanceDocument;
      }
    return instanceDocuments;
  }

  
  
  public String getInformationFragment() {
    return "/wsdl/fragment/XSDDefaultInfoFragmentJSP.jsp";
  }

  public String getReadFragment() {
    return "/wsdl/fragment/XSDComplexSimpleContentRFragmentJSP.jsp";
  }

  public String getWriteFragment() {
    return "/wsdl/fragment/XSDComplexSimpleContentRangeWFragmentJSP.jsp";
  }
}
