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
package org.eclipse.wst.wsdl.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource</b> implementation for the model.
 * This specialized resource implementation supports it's own way of making keys and 
 * hrefs, and it's own serialization. This class is not intended for subclassing 
 * outside of the model implementation; it is intended to be used as is with the 
 * Resource framework.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.util.WSDLResourceFactoryImpl
 * @generated
 */
public class WSDLResourceImpl extends ResourceImpl implements ErrorHandler
{
  protected WSDLModelLocator wsdlModelLocator;
  private boolean useExtensionFactories = true;
  private boolean continueOnLoadError = true;

  public static String USE_EXTENSION_FACTORIES = "USE_EXTENSION_FACTORIES";
  public static String CONTINUE_ON_LOAD_ERROR = "CONTINUE_ON_LOAD_ERROR";
  public static String WSDL_ENCODING = "WSDL_ENCODING";

  /**
   * Creates an instance of the resource. 
   * <!-- begin-user-doc --> 
   * <!-- end-user-doc -->
   * 
   * @param uri the URI of the new resource. 
   * @generated
   */
  public WSDLResourceImpl(URI uri)
  {
    super(uri);
    wsdlModelLocator = new DefaultURIResolver();
  }

  protected void doSave(OutputStream os, Map options) throws IOException
  {
    Definition definition = getDefinition();
    if (definition != null)
    {
      Document document = definition.getDocument();
      if (document == null)
      {
        ((DefinitionImpl) definition).updateDocument();
        document = definition.getDocument();
      }

      if (definition.getElement() == null)
      {
        ((DefinitionImpl) definition).updateElement();
      }

      doSerialize(os, document, options == null ? null : (String) options.get(WSDL_ENCODING));
    }
  }

  /**
   * Returns the resource's Definition.
   */
  public Definition getDefinition()
  {
    return getContents().size() == 1 && getContents().get(0) instanceof Definition ? (Definition) getContents().get(0) : null;
  }

  protected static void doSerialize(OutputStream outputStream, Document document) throws IOException
  {
    doSerialize(outputStream, document, null);
  }

  protected static void doSerialize(OutputStream outputStream, Document document, String encoding)
  {
    try
    {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      
      // Unless a width is set, there will be only line breaks but no indentation.
      // The IBM JDK and the Sun JDK don't agree on the property name,
      // so we set them both.
      //
      transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      if (encoding != null)
      {
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      }

      transformer.transform(new DOMSource(document), new StreamResult(outputStream));
    }
    catch (TransformerException exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
    }
  }

  /**
   * Loads a new {@link WSDLResourceImpl} into the resource set.
   * @param resourceSet the resource set to hold the new resource.
   * @param uri the URI of the new resource.
   * @param inputStream the contents of the new resource.
   * @param options any options to influence loading behavior.
   * @return a new WSDLResourceImpl.
   */
  protected void doLoad(InputStream inputStream, Map options) throws IOException
  {
    // This pattern avoids loading the IProgressMonitor class when there is no progress monitor.
    // This is important for stand-alone execution to work correctly.
    //
    IProgressMonitor progressMonitor = null;
    Object monitor = options == null ? null : options.get("WSDL_PROGRESS_MONITOR");
    if (monitor != null)
    {
      progressMonitor = (IProgressMonitor) monitor;
      progressMonitor.setTaskName(WSDLPlugin.INSTANCE.getString("_UI_ResourceLoad_progress"));
      progressMonitor.subTask(getURI().toString());
    }

    Object bindings = options == null ? null : options.get(USE_EXTENSION_FACTORIES);
    if (bindings != null && bindings instanceof Boolean) 
      // true by default
      useExtensionFactories = ((Boolean)bindings).booleanValue();
 
    Object continueOnError = options == null ? null : options.get(CONTINUE_ON_LOAD_ERROR);
    if (continueOnError != null && continueOnError instanceof Boolean) 
      // true by default
    	continueOnLoadError = ((Boolean)continueOnError).booleanValue();
    
    Document doc = null;
    try
    {
      // Create a DOM document
      doc = getDocument(inputStream, this);

      if (doc != null && doc.getDocumentElement() != null)
      {
        if (!findDefinition(doc.getDocumentElement()))
        {
          if (continueOnLoadError)
            handleDefinitionElement(doc.getDocumentElement());
          else
            throw new IOException(WSDLPlugin.getPlugin().getString("_ERROR_INVALID_WSDL"));
        }
      }
      else
      {
        handleDefinitionElement(null);
      }
    }
    catch (IOException exception)
    {
      if (continueOnLoadError)
      {
        WSDLPlugin.INSTANCE.log(exception);
        handleDefinitionElement(null);
      }
      else
      	throw exception;
    }

    Definition definition = null;

    for (Iterator i = getContents().iterator(); i.hasNext();)
    {
      definition = (Definition) i.next();

      // Initialize the inline schemas location 
      Types types = definition.getETypes();
      if (types != null)
      {
        XSDSchemaExtensibilityElement el = null;
        for (Iterator j = types.getEExtensibilityElements().iterator(); j.hasNext();)
        {
          el = (XSDSchemaExtensibilityElement) j.next();
          XSDSchema schema = el.getSchema();
          if (schema != null)
            schema.setSchemaLocation(getURI().toString());
        }
      }
    }

    if (progressMonitor != null)
    {
      progressMonitor.worked(1);
    }
  }

  /**
   * Builds a document using Xerces.
   * @param inputStream the contents to parse.
   * @param errorHandler the handled used by the parser.
   * @return a document.
   */
  protected static Document getDocument(InputStream inputStream, ErrorHandler errorHandler) throws IOException
  {
    ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(WSDLResourceFactoryImpl.class.getClassLoader());

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(false);

      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      // TBD - Revisit
      //EntityResolver entityResolver = createEntityResolver();
      //documentBuilder.setEntityResolver(entityResolver);

      documentBuilder.setErrorHandler(errorHandler);

      Document document = documentBuilder.parse(inputStream);
      return document;
    }
    catch (ParserConfigurationException exception)
    {
      throw new IOWrappedException(exception);
    }
    catch (SAXException exception)
    {
      throw new IOWrappedException(exception);
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(previousClassLoader);
    }
  }

  protected boolean findDefinition(Element element)
  {
    if (WSDLConstants.nodeType(element) == WSDLConstants.DEFINITION)
    {
      handleDefinitionElement(element);
      return true;
    }
    else
    {
      boolean result = false;
      /*
      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
        if (child instanceof Element)
        {
          if (findDefinition((Element) child))
          {
            result = true;
          }
        }
      }
      */
      return result;
    }
  }

  protected void handleDefinitionElement(Element element)
  {
    Definition definition = null;
    if (element == null)
    {
      definition = WSDLFactory.eINSTANCE.createDefinition();
      ((DefinitionImpl)definition).setUseExtensionFactories(useExtensionFactories);
    }
    else
    {
      definition = DefinitionImpl.createDefinition
	    (element,getURI().toString(),useExtensionFactories);
    }
    getContents().add(definition);
    // Do we need the next line?
    ((DefinitionImpl) definition).reconcileReferences(true);
  }

  public static void serialize(OutputStream outputStream, Document document)
  {
    serialize(outputStream, document, null);
  }

  public static void serialize(OutputStream outputStream, Document document, String encoding)
  {
    doSerialize(outputStream, document, encoding);
  }

  public static void serialize(OutputStream outputStream, Element element)
  {
    serialize(outputStream, element, null);
  }

  public static void serialize(OutputStream outputStream, Element element, String encoding)
  {
    try
    {
      doSerialize(outputStream, element, encoding);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }

  protected static void doSerialize(OutputStream outputStream, Element element, String encoding) throws IOException
  {
    try
    {
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      if (encoding != null)
      {
        transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
      }

      transformer.transform(new DOMSource(element), new StreamResult(outputStream));
    }
    catch (TransformerException exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
    }
  }

  public WSDLModelLocator getURIResolver()
  {
    return wsdlModelLocator;
  }

  public void setURIResolver(WSDLModelLocator resolver)
  {
    wsdlModelLocator = resolver;
  }

  protected class DefaultURIResolver implements WSDLModelLocator
  {
    public String resolveURI(String baseLocation, String namespace, String location)
    {
      URI baseLocationURI = createURI(baseLocation);
      URI locationURI = URI.createURI(location);
      return locationURI.resolve(baseLocationURI).toString();
    }
  }
  
  private static URI createURI(String uriString)
  {
    if (hasProtocol(uriString))
       return URI.createURI(uriString);
    else
       return URI.createFileURI(uriString);
  }
  
  private static boolean hasProtocol(String uri)
  {
		boolean result = false;     
		if (uri != null)
		{
		  int index = uri.indexOf(":");
		  if (index != -1 && index > 2) // assume protocol with be length 3 so that the'C' in 'C:/' is not interpreted as a protocol
		  {
		    result = true;
		  }
		}
		return result;
  }  

  public void error(SAXParseException e)
  {
    System.out.println("WSDL PARSE ERROR: " + e);
  }

  public void fatalError(SAXParseException e)
  {
    System.out.println("WSDL PARSE FATAL ERROR: " + e);
  }

  public void warning(SAXParseException e)
  {
    System.out.println("WSDL PARSE WARNING: " + e);
  }
  
  public void attached(EObject eObject)
  {
    super.attached(eObject);
     
    // we need to attach a XSDSchemaLocator in order to resolve inline schema locations
    // if there's not already one attached
    XSDSchemaLocator xsdSchemaLocator = (XSDSchemaLocator)EcoreUtil.getRegisteredAdapter(this, XSDSchemaLocator.class);
    if (xsdSchemaLocator == null)
    {
      getResourceSet().getAdapterFactories().add(new XSDSchemaLocatorAdapterFactory());  
    } 
    
    if (eObject instanceof Definition)
    {
      Definition definition = (Definition) eObject;
      setInlineSchemaLocations(definition);    
    }
  }
  
  public void setInlineSchemaLocations(Definition definition)
  {
    // Initialize the inline schemas location 
    Types types = definition.getETypes();
    if (types != null)
    {
      for (Iterator j = types.getEExtensibilityElements().iterator(); j.hasNext();)
      {
        XSDSchemaExtensibilityElement el = (XSDSchemaExtensibilityElement) j.next();
        XSDSchema schema = el.getSchema();
        if (schema != null)
        {  
          schema.setSchemaLocation(getURI().toString());
        }  
      }        
    }      
  }
} //WSDLResourceFactoryImpl
