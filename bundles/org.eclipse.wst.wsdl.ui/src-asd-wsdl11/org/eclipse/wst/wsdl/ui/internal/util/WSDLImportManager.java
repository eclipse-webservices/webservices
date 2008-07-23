/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.common.util.XSDDirectivesManager;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class performs cleanup/removal of unused WSDL imports, unused XSD imports and includes from 
 * inline XML Schemas, and xmlns entries.
 * 
 */
public class WSDLImportManager extends XSDDirectivesManager
{
  // The definition to modify
  protected Definition definition;
  // Map of inline schemas and their used prefixes
  protected Map schemaToUsedPrefixesMap;
  // Map of inline schemas and their unused prefixes
  protected Map schemaToUnusedPrefixesMap;
  // Set of used prefixes in the WSDL document's XML namespace table
  protected Set usedWSDLPrefixes;
  // Set of unused prefixes in the WSDL document's XML namespace table
  protected Set unusedWSDLPrefixes;
  // List of used definitions, computed from the cross referencer
  protected List usedDefinitions;
  // List of unused WSDL Imports to be removed
  protected List unusedWSDLImports;

  public WSDLImportManager()
  {
    super();
  }
  
  public static void removeUnusedImports(Definition definition)
  {
    // Only do the removal if the preference is turned on
    if (WSDLEditorPlugin.getInstance().getRemoveImportSetting())
    {
      WSDLImportManager mgr = new WSDLImportManager();
      mgr.performRemoval(definition);
      mgr.cleanup();
    }
  }
 
  /**
   * Main method to do the clean up.
   * @param definition
   */
  public void performRemoval(Definition definition)
  {
    this.definition = definition;
    // Compute unused WSDL Imports, unused imports from inline schemas, and unused prefixes
    computeUnusedImports(definition);
    // Remove the imports
    removeUnusedImports();
    // Remove the prefixes
    removeUnusedPrefixes();
  }
  
  /**
   * Returns the list of all the unused directives from inline schemas to remove
   * 
   * @return the list of all the unused directives from inline schemas to remove
   */
  public List getUnusedImports()
  {
    return unusedDirectives;
  }
  
  /**
   * Returns the list of the unused WSDL imports to remove
   * 
   * @return list of unused WSDL Imports
   */
  public List getWSDLUnusedImports()
  {
    return unusedWSDLImports;
  }

  /**
   * Returns the list of unused prefixes in the XML Namespace table of the WSDL file
   * @return the list of unused prefixes in the XML Namespace table of the WSDL file
   */
  public Set getUnusedWSDLPrefixes()
  {
    return unusedWSDLPrefixes;
  }
  
  /**
   * This computes the list of unused imports for a definition and its inline schemas
   * @param definition
   */
  protected void computeUnusedImports(Definition definition)
  {
    List usedSchemas = new ArrayList();
    
    unusedDirectives = new ArrayList();
    usedPrefixes = new HashSet();
    schemaToPrefixMap = new HashMap();
    
    schemaToUsedPrefixesMap = new HashMap();
    schemaToUnusedPrefixesMap = new HashMap();
    
    usedDefinitions = new ArrayList();
    unusedWSDLImports = new ArrayList();
    
    usedWSDLPrefixes = new HashSet();
    unusedWSDLPrefixes = new HashSet();
    
    Types types = definition.getETypes();
    if (types != null)
    {
      try
      {
        // Compute cross references
        Map xsdNamedComponentUsage = TopLevelComponentCrossReferencer.find(definition);

        // Now find unused imports from each inline schema
        for (Iterator schemaIterator = types.getSchemas().iterator(); schemaIterator.hasNext();)
        {
          XSDSchema schema = (XSDSchema) schemaIterator.next();
          
          // need to reset this for each inline schema
          usedPrefixes.clear();

          // Step One.  Find additional unused imports using cross referencer
          doCrossReferencer(schema, usedSchemas, xsdNamedComponentUsage);

          // Step Two.  Update the unusedImport list given the list of used schemas obtained from cross referencing
          addToUnusedImports(schema, usedSchemas);

          // Step Three. Compute unused xmlns entries
          computeUnusedPrefixes(schema);
        }
      }
      catch (Exception e)
      {
        unusedDirectives.clear();
        schemaToPrefixMap.clear();
      }
    }
    
    // Next, compute unused WSDL imports
    computeUsedDefinitions(definition);
    
    // Analyze the current xml ns table for the definition and determine list of used prefixes
    analyzeDefinitionXMLNSTable(definition);

    // Now compute the list of unused imports
    computeUnusedWSDLImports(definition);
    
    // Finally, compute unused prefixes for the definition
    unusedWSDLPrefixes = computeUnusedPrefixes(definition);
  }

  /*
   * This computes the list of unused WSDL Imports and updates list of used prefixes 
   */
  private void computeUsedDefinitions(Definition definition)
  {
    // Find cross references
    Map wsdlNamedComponentUsage = WSDLNamedComponentCrossReferencer.find(definition);
    Iterator iterator = wsdlNamedComponentUsage.keySet().iterator();
    while (iterator.hasNext())
    {
      WSDLElement wsdlElement = (WSDLElement)iterator.next();
      Definition wsdlElementDef = wsdlElement.getEnclosingDefinition();
      
      if (wsdlElementDef == definition)
      {
        continue;
      }
      
      Collection collection = (Collection)wsdlNamedComponentUsage.get(wsdlElement);
      for (Iterator iter = collection.iterator(); iter.hasNext(); )
      {
        Setting setting = (Setting) iter.next();
        Object obj = setting.getEObject();
        // If component is used, then mark its definition as used which we will
        // use later to compare with the import's definition
        if (isComponentUsed(obj, definition, wsdlElementDef))
        {
          if (!usedDefinitions.contains(wsdlElementDef))
            usedDefinitions.add(wsdlElementDef);
        }
      }
    }
  }

  /**
   * @param definition
   */
  private void computeUnusedWSDLImports(Definition definition)
  {
    for (Iterator iter = definition.getEImports().iterator(); iter.hasNext(); )
    {
      Object obj = iter.next();
      if (obj instanceof Import)
      {
        Import imp = (Import)obj;
        Definition def = imp.getEDefinition();
        XSDSchema schema = imp.getESchema();
        boolean isUsed = false;
        if (def != null)
        {
          for (Iterator usedDef = usedDefinitions.iterator(); usedDef.hasNext();)
          {
            Definition used = (Definition) usedDef.next();
            if (used == def)
            {
              isUsed = true;
              break;
            }
          }
        }
        if (schema != null)
        {
          String ns = imp.getNamespaceURI();
          Map defPrefixMap = definition.getNamespaces();
          Set wsdlKeys = defPrefixMap.keySet();
          boolean prefixFound = false;
          String pref = null;
          for (Iterator iterator = wsdlKeys.iterator(); iterator.hasNext();)
          {
            pref = (String)iterator.next();
            String aNS = (String)defPrefixMap.get(pref);
            if ((aNS != null && aNS.equals(ns)) || (aNS == ns))
            {
              prefixFound = true;
              break;
            }
          }
          if (prefixFound && (usedPrefixes.contains(pref) || usedWSDLPrefixes.contains(pref)))
            isUsed = true;
        }
        if (!isUsed)
        {
          unusedWSDLImports.add(imp);
        }
      }
    }
  }
  
  /**
   * Removes the unused directives and unused WSDL imports
   */
  protected void removeUnusedImports()
  {
    super.removeUnusedImports();
    Iterator iter = unusedWSDLImports.iterator();
    while (iter.hasNext())
    {
      Import wsdlImport = (Import) iter.next();
      
      Element element = wsdlImport.getElement();
      
      Document doc = element.getOwnerDocument();

      if (doc instanceof IDOMNode)
        ((IDOMNode)doc).getModel().aboutToChangeModel();

      try
      {
        if (!removeTextNodesBetweenNextElement(element))
        {
          removeTextNodeBetweenPreviousElement(element);
        }
        element.getParentNode().removeChild(element);
      }
      finally
      {
        if (doc instanceof IDOMNode)
         ((IDOMNode)doc).getModel().changedModel();
      }
    }
  }

  /**
   * Mark definition's target namespace prefix, and WSDL namespace prefix as used.
   * Also find prefixes used in the document
   * @param definition
   */
  private void analyzeDefinitionXMLNSTable(Definition definition)
  {
    String prefix = definition.getPrefix(definition.getTargetNamespace());
    usedWSDLPrefixes.add(prefix);
    
    String wsdlPrefix = definition.getPrefix(WSDLConstants.WSDL_NAMESPACE_URI);
    usedWSDLPrefixes.add(wsdlPrefix);
    
    Element element = definition.getElement();
    
    NodeList childElements = element.getChildNodes();
    int length = childElements.getLength();
    for (int index = 0; index < length; index++)
    {
      Node node = childElements.item(index);
      if (node instanceof Element)
      {
        traverseDOMElement((Element)node, definition);
      }
    }
  }
  
  /**
   * Removes the unused prefixes from the inline schema's XML namespace tables as well as the
   * unused prefixes in the WSDL's namespace table
   */
  protected void removeUnusedPrefixes()
  {
    super.removeUnusedPrefixes();

    Map defPrefixMap = definition.getNamespaces();
    
    for (Iterator iter = unusedWSDLPrefixes.iterator(); iter.hasNext(); )
    {
      String string = (String)iter.next();
      // bug, key is "" not null
      defPrefixMap.remove(string != null ? string : "");  //$NON-NLS-1$
      Element element = definition.getElement();
      if (element != null)
      {
        if (string != null)
          element.removeAttribute(XMLNS + ":" + string); //$NON-NLS-1$
        else
          element.removeAttribute(XMLNS);
      }
    }
  }
  
  /**
   * Compute the unused prefixes 
   * @param definition
   * @return set of unused prefixes
   */
  private Set computeUnusedPrefixes(Definition definition)
  {
    Map defPrefixMap = definition.getNamespaces();
    Set wsdlKeys = defPrefixMap.keySet();
    
    Set netSet = new HashSet();

    Types eTypes = definition.getETypes();
    List extElements = new ArrayList();
    if (eTypes != null)
    {
      extElements = definition.getETypes().getEExtensibilityElements();
      for (Iterator eeIter = extElements.iterator(); eeIter.hasNext(); )
      {
        Object ee = eeIter.next();
        if (ee instanceof XSDSchemaExtensibilityElement)
        {
          XSDSchemaExtensibilityElement xsdEx = (XSDSchemaExtensibilityElement)ee;
          XSDSchema inlineXSD = xsdEx.getSchema();
          usedWSDLPrefixes.add(inlineXSD.getSchemaForSchemaQNamePrefix());
        }
      }
    }
    
    for (Iterator wsdlIter = wsdlKeys.iterator(); wsdlIter.hasNext(); )
    {
      String aWsdlPrefix = (String)wsdlIter.next();
      if (aWsdlPrefix != null && aWsdlPrefix.equals("")) aWsdlPrefix = null;

      String nsForWSDLPrefix = (String)defPrefixMap.get(aWsdlPrefix);
      
      // If the wsdl prefix is not referenced by any wsdl component other than
      // the import then check if the inline schemas reference that prefix/ns.
      if (!usedWSDLPrefixes.contains(aWsdlPrefix))
      {
        boolean canRemoveWSDLPrefix = true;
        
        for (Iterator eeIter = extElements.iterator(); eeIter.hasNext(); )
        {
          Object ee = eeIter.next();
          if (ee instanceof XSDSchemaExtensibilityElement)
          {
            XSDSchemaExtensibilityElement xsdEx = (XSDSchemaExtensibilityElement)ee;
            XSDSchema inlineXSD = xsdEx.getSchema();
            Map inlineXSDMap = inlineXSD.getQNamePrefixToNamespaceMap();

            Set usedInlineXSDPrefixes = (Set)schemaToUsedPrefixesMap.get(inlineXSD);
            
            if (usedInlineXSDPrefixes.contains(aWsdlPrefix))
            {
              String nsForXSDPrefix = (String)inlineXSDMap.get(aWsdlPrefix);
              if ((nsForXSDPrefix != null && nsForXSDPrefix.equals(nsForWSDLPrefix)) ||
                  (nsForXSDPrefix == null && nsForWSDLPrefix == null))
              {
                canRemoveWSDLPrefix = false;
              }
            }
          }
        }
        if (canRemoveWSDLPrefix)
          netSet.add(aWsdlPrefix);
      }
    }
    return netSet;
  }
  
  /**
   * Need to keep track of the inline schemas and their prefixes
   */
  protected void doAdditionalProcessing(XSDSchema schema)
  {
    schemaToUsedPrefixesMap.put(schema, new HashSet(usedPrefixes));
    schemaToUnusedPrefixesMap.put(schema, new HashSet(unusedPrefixes));
  }
  
  /**
   * Find prefixes that are in the document.
   * @param element
   * @param definition
   */
  private void traverseDOMElement(Element element, Definition definition)
  {
    String prefix = element.getPrefix();
    usedWSDLPrefixes.add(prefix);
    
    NamedNodeMap attrs = element.getAttributes();
    int numOfAttrs = attrs.getLength();
    for (int index = 0; index < numOfAttrs; index++)
    {
      Node node = attrs.item(index);
      String attrPrefix = node.getPrefix();
      if (attrPrefix != null && !attrPrefix.equals(XMLNS)) // This is to avoid adding the xmlns for the schema attributes
      {
        usedWSDLPrefixes.add(attrPrefix);
      }
      
      String attr = node.getLocalName();
      if (attr != null)
      {
        String value = node.getNodeValue();
        if (value == null) continue;
        if (attr.equals(WSDLConstants.BINDING_ATTRIBUTE) ||
            attr.equals(WSDLConstants.TYPE_ATTRIBUTE) ||
            attr.equals(WSDLConstants.ELEMENT_ATTRIBUTE) ||
            attr.equals(WSDLConstants.MESSAGE_ATTRIBUTE))
        {
          usedWSDLPrefixes.add(extractPrefix(value));
        }
      }
    }
    
    NodeList childElements = element.getChildNodes();
    int length = childElements.getLength();
    for (int index = 0; index < length; index++)
    {
      Node node = childElements.item(index);
      if (node instanceof Element)
      {
        if (XSDConstants.SCHEMA_ELEMENT_TAG.equals(((Element)node).getLocalName()))
        {
          usedWSDLPrefixes.add(node.getPrefix());
        }
        else
        {
          traverseDOMElement((Element)node, definition);
        }
      }
    }
  }
  
  /**
   * Determines if the object to be analyzed is referenced by the inline schema
   */
  protected boolean isComponentUsed(Object obj, XSDSchema schema, XSDSchema targetSchema)
  {
    if (obj instanceof XSDConcreteComponent)
    {
      return super.isComponentUsed(obj, schema, targetSchema);
    }
    else if (obj instanceof Part)
    {
      Part part = (Part) obj;
      XSDElementDeclaration elem = part.getElementDeclaration();
      XSDTypeDefinition type = part.getTypeDefinition();
      if (elem != null)
      {
        // This reference belongs to the imported schema, so it is used.
        if (elem.getSchema() == targetSchema)
        {
          String value = part.getElement().getAttribute(WSDLConstants.ELEMENT_ATTRIBUTE);
          String pref = extractPrefix(value);
          usedPrefixes.add(pref);
          usedWSDLPrefixes.add(pref);
          return true;
        }
      }
      if (type != null)
      {
        // This reference belongs to the imported schema, so it is used.
        if (type.getSchema() == targetSchema)
        {
          String value = part.getElement().getAttribute(WSDLConstants.TYPE_ATTRIBUTE);
          String pref = extractPrefix(value);
          usedPrefixes.add(pref);
          usedWSDLPrefixes.add(pref);
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * clear out maps
   */
  protected void clearMaps()
  {
    super.clearMaps();
    if (schemaToUsedPrefixesMap != null)
      schemaToUsedPrefixesMap.clear();
    if (schemaToUnusedPrefixesMap != null)
      schemaToUnusedPrefixesMap.clear();
    if (usedDefinitions != null)
      usedDefinitions.clear();
  }

  /**
   * Determines if the object to be analyzed is referenced by the definition
   * @param obj
   * @param definition
   * @param targetDefinition
   * @return
   */
  protected boolean isComponentUsed(Object obj, Definition definition, Definition targetDefinition)
  {
    if (obj instanceof WSDLElement)
    {
      WSDLElement component = (WSDLElement) obj;
      if (component == definition || component instanceof Definition)
      {
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * See cross referencer for more details. 
   */
  protected static class WSDLNamedComponentCrossReferencer extends EcoreUtil.CrossReferencer
  {
    private static final long serialVersionUID = 1L;

    protected WSDLNamedComponentCrossReferencer(EObject eObject)
    {
      super(eObject);
    }

    protected WSDLNamedComponentCrossReferencer(Resource resource)
    {
      super(resource);
    }

    protected WSDLNamedComponentCrossReferencer(ResourceSet resourceSet)
    {
      super(resourceSet);
    }

    protected WSDLNamedComponentCrossReferencer(Collection emfObjects)
    {
      super(emfObjects);
    }

    protected boolean containment(EObject eObject)
    {
      // Create an empty setting collection for any named component.
      //
      if (isWSDLNamedComponent(eObject))
      {
        getCollection(eObject);
      }
      return true;
    }

    protected static boolean isWSDLNamedComponent(EObject eObject)
    {
      return eObject instanceof Message || eObject instanceof PortType || eObject instanceof Binding || eObject instanceof Port;
    }

    protected boolean crossReference(EObject eObject, EReference eReference, EObject crossReferencedEObject)
    {
      // Add a setting for any named component in an interesting reference.
      //
      return !eReference.isVolatile() && 
      eReference.isChangeable() && 
      isWSDLNamedComponent(crossReferencedEObject);
    }

    /**
     * Returns a map of all XSDNamedComponent cross references in the content
     * tree.
     */
    public static Map find(EObject eObject)
    {
      WSDLNamedComponentCrossReferencer result = new WSDLNamedComponentCrossReferencer(eObject);
      result.crossReference();
      result.done();
      return result;
    }

    /**
     * Returns a map of all XSDNamedComponent cross references in the content
     * tree.
     */
    public static Map find(Resource resource)
    {
      WSDLNamedComponentCrossReferencer result = new WSDLNamedComponentCrossReferencer(resource);
      result.crossReference();
      result.done();
      return result;
    }

    /**
     * Returns a map of all XSDNamedComponent cross references in the content
     * tree.
     */
    public static Map find(ResourceSet resourceSet)
    {
      WSDLNamedComponentCrossReferencer result = new WSDLNamedComponentCrossReferencer(resourceSet);
      result.crossReference();
      result.done();
      return result;
    }
  }
}
