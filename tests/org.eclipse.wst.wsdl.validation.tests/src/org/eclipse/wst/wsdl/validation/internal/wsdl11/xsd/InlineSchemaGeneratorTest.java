/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ibm.wsdl.Constants;

/**
 * Tests for org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaGenerator.
 *
 */
public class InlineSchemaGeneratorTest extends TestCase 
{
  private InlineSchemaGeneratorWrapper generator;
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
	return new TestSuite(InlineSchemaGeneratorTest.class);
  }
  
  /**
   * JUnit setup method.
   */
  public void setUp()
  {
	generator = new InlineSchemaGeneratorWrapper();
  }
  
  /**
   * Test that checkSOAPEncodingRequired method.
   */
  public void testCheckSOAPEncodingRequired()
  {
	// Test empty list.
	assertFalse("The method does not return false when given an empty list.", generator.checkSOAPEncodingRequired(new ArrayList()));
	
	// Test that checkSOAPEncodingRequired returns false
	// when given a list without the SOAP encoding namespace.
	List list = new ArrayList();
	list.add("http://notsoapencodingnamespace");
	assertFalse("The method does not return false when given a list without the SOAP envelope namespace.", generator.checkSOAPEncodingRequired(list));
	
	// Test that checkSOAPEncodingRequired returns true
	// when given a list with the SOAP encoding namespace.
	List list2 = new ArrayList();
	list2.add(InlineSchemaGeneratorWrapper.SOAP_ENCODING_URI);
	assertTrue("The method does not return true when given a list with the SOAP encoding namespace.", generator.checkSOAPEncodingRequired(list2));
  }
  
  /**
   * TODO: Implement tests for the CreateXSDStringRecursively method.
   */
  public void todoCreateXSDStringRecursively() 
  {
  }

  /**
   * TODO: Implement tests for the GetImportNamespace method.
   */
  public void todoGetImportNamespaces() 
  {
  }

  /**
   	* Test that this method returns the correct namespace
   	* resolver.
   	*/
  public void testGetNSResolver() 
  {
    Document doc = new DocumentImpl();
	Element rootElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "schema");

	// Check that the resolver is empty.
	Hashtable resolver = generator.getNSResolver(rootElem);
	assertTrue("The resolver is not empty.", resolver.isEmpty());
	
	// Check the resolver contains one value.
	rootElem.setAttribute("xmlns", Constants.NS_URI_XSD_2001);
	resolver = generator.getNSResolver(rootElem);
	assertTrue("The resolver does not contain the empty namespace.", resolver.containsKey(""));
	assertEquals("The resolver does not contain the correct value for the empty namespace.", Constants.NS_URI_XSD_2001, resolver.get(""));
	
	// Check the resolver contains two values.
	rootElem.setAttribute("xmlns:other", "http://othernamespace");
	resolver = generator.getNSResolver(rootElem);
	assertTrue("The resolver does not contain the other namespace.", resolver.containsKey("other"));
	assertEquals("The resolver does not contain the correct value for the other namespace.", "http://othernamespace", resolver.get("other"));
	
	// Check the resolver still contains the empty namespace.
	assertTrue("The resolver does not contain the empty namespace after adding a second namespace.", resolver.containsKey(""));
	assertEquals("The resolver does not contain the correct value for the empty namespace after adding a second namespace.", Constants.NS_URI_XSD_2001, resolver.get(""));
	
	// Check the resolver doesn't contain non-xmlns attribute value.
	rootElem.setAttribute("type:other2", "other2:type");
	resolver = generator.getNSResolver(rootElem);
	assertFalse("The resolver contains the other2 namespace.", resolver.containsKey("other2"));
  }

  /**
   * Test that this method returns the required prefixes.
   */
  public void testGetPrefixes() 
  {
	// Check document that contains no required namespaces and no prefix.
	Document doc = new DocumentImpl();
	Element rootElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "schema");
	rootElem.setAttribute("xmlns",Constants.NS_URI_XSD_2001);
	//doc.appendChild(rootElem);
	
	List reqNSs = generator.getNamespacePrefixes(rootElem);
	assertEquals("The required namespace list does not contain one empty string.", 1, reqNSs.size());
	assertEquals("The required namespace list does not contain one empty string.", "", reqNSs.get(0));
	
	// Check document that contains no required namespaces and the xsd prefix.
	Element rootElem2 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem2.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	List reqNSs2 = generator.getNamespacePrefixes(rootElem2);
	assertEquals("The required namespace list does not contain the one string 'xsd'.", 1, reqNSs2.size());
	assertEquals("The required namespace list does not contain the one string 'xsd'.", "xsd", reqNSs2.get(0));
	
	// Check document that contains an element with a required namespace.
	Element rootElem4 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem2.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	Element diffNSElem = doc.createElementNS("http://othernamespace", "other:element");
	rootElem4.appendChild(diffNSElem);
	List reqNSs4 = generator.getNamespacePrefixes(rootElem4);
	assertTrue("The required namespace list does not contain the prefix 'other' when the namespace is specified for an element.", reqNSs4.contains("other"));
	
	// Check document that contains a type with a required namespace.
	Element rootElem5 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem5.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	Element otherElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:element");
	otherElem.setAttribute("type", "other:type");
	rootElem5.appendChild(otherElem);
	List reqNSs5 = generator.getNamespacePrefixes(rootElem5);
	assertTrue("The required namespace list does not contain 'other' when the namespace is specified for a type.", reqNSs5.contains("other"));
	
	// Check document that contains an import. Import elements should be ignored.
	// This is a contrived example as it contains a type attribute for the import element.
	Element rootElem6 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem6.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	Element importElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:import");
	importElem.setAttribute("type", "other:type");
	rootElem6.appendChild(importElem);
	List reqNSs6 = generator.getNamespacePrefixes(rootElem6);
	assertEquals("The required namespace list does not contain the one string 'xsd' when an import element is used.", 1, reqNSs6.size());
	assertEquals("The required namespace list does not contain the one string 'xsd' when an import element is used.", "xsd", reqNSs6.get(0));
	
    // Check document that contains an include. Include elements should be ignored.
    // This is a contrived example as it contains a type attribute for the include element.
	Element rootElem7 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem7.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	Element includeElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:include");
	includeElem.setAttribute("type", "other:type");
	rootElem7.appendChild(includeElem);
	List reqNSs7 = generator.getNamespacePrefixes(rootElem6);
	assertEquals("The required namespace list does not contain the one string 'xsd' when an import element is used.", 1, reqNSs7.size());
	assertEquals("The required namespace list does not contain the one string 'xsd' when an import element is used.", "xsd", reqNSs7.get(0));
	
	// Check document that contains attribute with a required namespace.
	Element rootElem3 = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:schema");
	rootElem3.setAttribute("xmlns:xsd",Constants.NS_URI_XSD_2001);
	Element wsdlAttElem = doc.createElementNS(Constants.NS_URI_XSD_2001, "xsd:element");
	wsdlAttElem.setAttributeNS(Constants.NS_URI_WSDL, "wsdl:arrayType", "sometype[]");
	rootElem3.appendChild(wsdlAttElem);
	List reqNSs3 = generator.getNamespacePrefixes(rootElem3);
	assertTrue("The required namespace list does not contain 'wsdl' when the namespace is specified for an attribute.", reqNSs3.contains("wsdl"));
	
  }

  /**
   * TODO: Implement tests for the RemoveImports method.
   */
  public void todoRemoveImports() 
  {
  }

  /**
   * TODO: Implement tests for the RemoveLocalNamespace method.
   */
  public void todoRemoveLocalNamespaces() 
  {
  }

  /**
   * TODO: Implement tests for the ResolveNamespace method.
   */
  public void todoResolveNamespaces() 
  {
  }

  /**
   * 
   */
  public void testRestrictImports() 
  {
  }
}
