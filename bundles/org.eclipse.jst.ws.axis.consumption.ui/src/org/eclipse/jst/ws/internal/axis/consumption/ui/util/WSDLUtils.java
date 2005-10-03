/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.axis.consumption.ui.util;

import java.text.Collator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.apache.axis.wsdl.toJava.Utils;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;

public class WSDLUtils {

  private static final String DOT = ".";

  /**
  * These are java keywords as specified at the following URL (sorted alphabetically).
  * http://java.sun.com/docs/books/jls/second_edition/html/lexical.doc.html#229308
  * Note that false, true, and null are not strictly keywords; they are literal values,
  * but for the purposes of this array, they can be treated as literals.
  */
  static final String keywords[] =
  {
      "abstract",  "boolean",      "break",      "byte",      "case",
      "catch",     "char",         "class",      "const",     "continue",
      "default",   "do",           "double",     "else",      "extends",
      "false",     "final",        "finally",    "float",     "for",
      "goto",      "if",           "implements", "import",    "instanceof",
      "int",       "interface",    "long",       "native",    "new",
      "null",      "package",      "private",    "protected", "public",
      "return",    "short",        "static",     "strictfp",  "super",
      "switch",    "synchronized", "this",       "throw",     "throws",
      "transient", "true",         "try",        "void",      "volatile",
      "while"
  };

  /** Collator for comparing the strings */
  static final Collator englishCollator = Collator.getInstance(Locale.ENGLISH);

  /** Use this character as suffix */
  static final char keywordPrefix = '_';

  private WSDLUtils() {
  }

  /**
  * Returns the name of the first service element in the WSDL definition.
  * @return String service element name
  */
  public static String getServiceElementName(Definition definition) {
  	Service service = (Service) definition.getServices().values().iterator().next();
  	return service.getQName().getLocalPart();
  }

  /**
  * Returns the name of the port type element points to by the first service and port element in the WSDL definition.
  * @return String port type element name
  */
  public static String getPortTypeName(Definition definition) {
  	Service service = (Service) definition.getServices().values().iterator().next();
    Iterator iterator = service.getPorts().values().iterator(); 
    while(iterator.hasNext()){
      Port port = (Port) iterator.next();
      for (int i=0; i<port.getExtensibilityElements().size();i++) {
        if (port.getExtensibilityElements().get(i) instanceof SOAPAddress) {
           Binding binding = port.getBinding();
           return binding.getPortType().getQName().getLocalPart();
        }
      }
    }
    return "";
  
  }

  /**
  * Returns the name of the port element in the WSDL definition.
  * @return String port name
  */
  public static String getPortName(Definition definition) {
  	Service service = (Service) definition.getServices().values().iterator().next();
    Iterator iterator = service.getPorts().values().iterator(); 
    while(iterator.hasNext()){
      Port port = (Port) iterator.next();
      for (int i=0; i<port.getExtensibilityElements().size();i++) {
        if (port.getExtensibilityElements().get(i) instanceof SOAPAddress) 
          return port.getName();
      }  
    }
     return "";
  }



  public static String makeNamespace (String clsName) {
	  return makeNamespace(clsName, "http");
  }

  /**
   * Make namespace from a fully qualified class name
   * and the given protocol
   *
   * @param clsName fully qualified class name
   * @param protocol protocol String
   * @return namespace namespace String
   */
  public static String makeNamespace (String clsName, String protocol) {
	  if (clsName.lastIndexOf('.') == -1)
		  return protocol + "://" + "DefaultNamespace";
	  String packageName = clsName.substring(0, clsName.lastIndexOf('.'));
	  return makeNamespaceFromPackageName(packageName, protocol);       
  }

    
  private static String makeNamespaceFromPackageName(String packageName, String protocol) {
	  if (packageName == null || packageName.equals(""))
		  return protocol + "://" + "DefaultNamespace";
	  StringTokenizer st = new StringTokenizer( packageName, "." );
	  String[] words = new String[ st.countTokens() ];
	  for(int i = 0; i < words.length; ++i)
		  words[i] = st.nextToken();

	  StringBuffer sb = new StringBuffer(80);
	  for(int i = words.length-1; i >= 0; --i) {
		  String word = words[i];
		  // seperate with dot
		  if( i != words.length-1 )
			  sb.append('.');
		  sb.append( word );
	  }
	  return protocol + "://" + sb.toString();
  }

  /**
  * Return a Definition for the wsdl url given 
  *
  */
  public static Definition getWSDLDefinition(String wsdlURL)
  {
    if(wsdlURL == null) return null;

    WSDLFactory wsdlFactory;
    Definition definition = null;
    try {
      wsdlFactory = new WSDLFactoryImpl();
      WSDLReader wsdlReader = wsdlFactory.newWSDLReader();
      definition = wsdlReader.readWSDL(wsdlURL);
    } 
    catch (Exception e) { // can be WSDLException or IOException
      return null;
    }
    return definition;
  }

  public static String getPackageName(Definition definition)
  {
     if (definition != null)
     {
        String namespace = definition.getTargetNamespace();
        return namespaceURI2PackageName(namespace);
     }
     return "";
  }

  public static String getPackageNameForBindingImpl(Port port, Map ns2pkgMap)
  {
    if (port != null && ns2pkgMap != null)
    {
      Binding binding = port.getBinding();
      QName bndQName = binding.getQName();
      String namespace = bndQName.getNamespaceURI();
      Object pkg = ns2pkgMap.get(namespace);
      if (pkg != null)
        return (String)pkg;
    }
    return getPackageNameForBindingImpl(port);
  }

  public static String getPackageNameForBindingImpl(Definition definition, Map ns2pkgMap)
  {
    if (definition != null && ns2pkgMap != null)
    {
      Service service = (Service) definition.getServices().values().iterator().next();
      Port port = (Port) service.getPorts().values().iterator().next();
      return getPackageNameForBindingImpl(port, ns2pkgMap);
    }
    return getPackageNameForBindingImpl(definition);
  }

  public static String getPackageNameForBindingImpl(Definition definition)
  {
    Port port = null;
    if (definition != null)
    {
      Service service = (Service)definition.getServices().values().iterator().next();
      port = (Port)service.getPorts().values().iterator().next();
    }
    return getPackageNameForBindingImpl(port);
  }

// This is yet another naming algorithm based on webservices.jar
// They always use the binding namespace as the package name
// of the BindingImpl class.
public static String getPackageNameForBindingImpl(Port port)
{
   if (port != null)
   {
	  Binding binding = port.getBinding();
//	  PortType portType = binding.getPortType();
	  QName bndQName = binding.getQName();
	  String namespace = bndQName.getNamespaceURI();
	  return namespaceURI2PackageName(namespace);
   }
   return "";
}

/**
* Get the namespace for the Port Type
* 
*/
public static String getPortTypeNamespace(Definition definition)
{
  String namespace = "";
  if (definition != null)
  {
	Service service = (Service) definition.getServices().values().iterator().next();
    Iterator iterator = service.getPorts().values().iterator(); 
	while(iterator.hasNext()){
      Port port = (Port) iterator.next();
      for (int i=0; i<port.getExtensibilityElements().size();i++) {
        if (port.getExtensibilityElements().get(i) instanceof SOAPAddress){ 
	      PortType portType = port.getBinding().getPortType();
	      QName bndQName = portType.getQName();
	      namespace = bndQName.getNamespaceURI();
      	}  
	  }
  	}
  }  
  return namespace;
}

// This is yet another naming algorithm based on webservices.jar
// They always use the porttype namespace as the package name
// of the Java class (in ejb).
public static String getPackageNameForPortType(Definition definition)
{
   if (definition != null)
   {
	  String namespace = getPortTypeNamespace(definition);
	  return namespaceURI2PackageName(namespace);
    }
    return "";
}

  /**
  * checks if the input string is a valid java keyword.
  * @return boolean true/false
  */
  public static boolean isJavaKeyword(String keyword) {
    return (Arrays.binarySearch(keywords, keyword, englishCollator) >= 0);
  }

  /**
  * Turn a java keyword string into a non-Java keyword string.  (Right now
  * this simply means appending an underscore.)
  */
  public static String makeNonJavaKeyword(String keyword){
    return  keywordPrefix + keyword;
  }

  public static String getFullyQualifiedPortTypeName(Definition definition)
  {
    StringBuffer beanName = new StringBuffer();
    beanName.append(getPackageNameForPortType(definition)); 
    beanName.append(DOT);
    beanName.append(getPortTypeName(definition));
    return beanName.toString();

  }

  /**
   * getName
   * @param uri String
   * @return get the file name after the last \ and /
   */
  public static String getName(String uri) {

	// Get everything after last slash or backslash
	int bslash = uri.lastIndexOf("\\");
	int slash = uri.lastIndexOf("/");
	int i =  bslash > slash ? bslash : slash;
	String fileName = uri.substring(i+1).replace('?', '.');

	return fileName;
  }


/**
 * getWSDLName
 * @param uri String
 * @return get the file name after the last \ and /, trimmed, defaulted to
 *         "default.wsdl" if there is no name, and ending with ".wsdl".
 */
 public static String getWSDLName(String uri) {

	// Get everything after last slash or backslash from input URI
	// with no whitespace.
	String WSDLName = getName(uri).trim();

	// if empty, return the default "default.wsdl"
	if ( WSDLName.equals( "" ) ) {
		WSDLName = "default.wsdl";
	}

	// make sure name ends with ".wsdl", lower case.
	else {
		if ( ! WSDLName.endsWith( ".wsdl" ) ) {
			if ( WSDLName.toLowerCase().endsWith( ".wsdl" ) ) {
				int lastDot = WSDLName.lastIndexOf(".");
				WSDLName = WSDLName.substring( 0, lastDot ) + ".wsdl";
			}
			else {
				WSDLName = WSDLName + ".wsdl";
			}
		}
	}

	return WSDLName;
 }

 /**
  * getPortTypeNameFromBeanName
  * @param beanname String
  * @return get the port type name based on the bean name
  */
  public static String getPortTypeNameFromBeanName(String beanName) {
    return beanName.substring(beanName.lastIndexOf('.') + 1);	
  }

  public static String getPackageName(Service service, Map ns2pkgMap)
  {
    if (service != null)
    {
      String namespace = service.getQName().getNamespaceURI();
      if (ns2pkgMap != null)
      {
        Object pkg = ns2pkgMap.get(namespace);
        if (pkg != null)
          return (String)pkg;
      }
      return namespaceURI2PackageName(namespace);
    }
    else
      return "";
  }

  public static String getPackageName(Port port, Map ns2pkgMap)
  {
    if (port != null)
      return getPackageName(port.getBinding(), ns2pkgMap);
    else
      return "";
  }

  public static String getPackageName(Binding binding, Map ns2pkgMap)
  {
    if (binding != null)
    {
      String namespace = binding.getQName().getNamespaceURI();
      if (ns2pkgMap != null)
      {
        Object pkg = ns2pkgMap.get(namespace);
        if (pkg != null)
          return (String)pkg;
      }
      return namespaceURI2PackageName(namespace);
    }
    else
      return "";
  }

  public static String getPackageName(PortType portType, Map ns2pkgMap)
  {
    if (portType != null)
    {
      String namespace = portType.getQName().getNamespaceURI();
      if (ns2pkgMap != null)
      {
        Object pkg = ns2pkgMap.get(namespace);
        if (pkg != null)
          return (String)pkg;
      }
      return namespaceURI2PackageName(namespace);
    }
    else
      return "";
  }


  /**
   * namespaceURI2PackageName
   * @param namespaceURI
   * @return package name based on namespace
   */
  public static String namespaceURI2PackageName(String namespaceURI)
  {
  	/**
  	 * TODO:  The makePackageName method from 
  	 * org.apache.axis.wsdl.toJava.Utils in axis-1_1 is called to map namespace to package name.  
  	 * This will be replaced with an extension point to plug in runtime emitter specific namespace to 
  	 * package mapping algorithm 
  	 */
  	return Utils.makePackageName(namespaceURI);
  	
//    StringBuffer sb = new StringBuffer(80);
//    if (namespaceURI != null && namespaceURI.length() > 0)
//    {
//      String hostname = null;
//      try
//      {
//        hostname = new URL(namespaceURI).getHost();
//      }
//      catch (MalformedURLException e)
//      {
//        int index = namespaceURI.indexOf(":");
//        if (index > -1)
//        {
//          hostname = namespaceURI.substring(index+1);
//          index = hostname.indexOf("/");
//          if (index > -1)
//            hostname = hostname.substring(0, index);
//        }
//        else
//          hostname = namespaceURI;
//      }
//      
//	  // if we didn't file a hostname, bail
//	  if (hostname == null) {
//		  return null;
//	  }
//
//      //convert illegal java identifier
//      hostname = hostname.replace('-', '_');
//      // tokenize the hostname and reverse it
//      StringTokenizer st = new StringTokenizer(hostname, ".");
//      String[] words = new String[st.countTokens()];
//      for (int i = 0; i < words.length; ++i)
//        words[i] = st.nextToken();
//      for(int i = words.length-1; i >= 0; --i)
//      {
//        String word = words[i];
//        if (isJavaKeyword(word))
//          word = makeNonJavaKeyword(word);
//        // seperate with dot
//        if (i != words.length-1)
//          sb.append('.');
//        // convert digits to underscores
//        if (Character.isDigit(word.charAt(0)))
//          sb.append('_');
//          sb.append(word);
//      }
//    }
//    return normalizePackageName(sb.toString(), DOT.charAt(0));
  }

  
  public static String resolveDotInPortName(String name) {
	if(name.indexOf(".")<0)
	{
		return name;
	}
    StringBuffer sb = new StringBuffer();
    boolean afterDot = false;
    for(int i=0; i<name.length(); i++)
    {
    	if(name.charAt(i)=='.')
    	{
    		afterDot = true;
    	}
    	else if(afterDot)
    	{
    		sb.append(name.substring(i,i+1).toUpperCase());
    		afterDot=false;
    	}
    	else
    	{
    		sb.append(name.charAt(i));
    	}
    }
    return sb.toString();
  }
}
