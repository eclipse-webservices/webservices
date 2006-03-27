/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
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
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.internal.util.XSDSchemaLocatorAdapterFactory;
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
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl
 * @generated
 */
public class WSDLResourceImpl extends ResourceImpl 
{

  private boolean useExtensionFactories = true;
  private boolean continueOnLoadError = true;

  public static String USE_EXTENSION_FACTORIES = "USE_EXTENSION_FACTORIES"; //$NON-NLS-1$
  public static String CONTINUE_ON_LOAD_ERROR = "CONTINUE_ON_LOAD_ERROR"; //$NON-NLS-1$
  public static String WSDL_ENCODING = "WSDL_ENCODING"; //$NON-NLS-1$
  
  /**
   * Add this option with a value of Boolean.TRUE to the options map when
   * loading a resource to instruct the loader to track source code location for
   * each node in the source document.
   * 
   * @see WSDLParser#getUserData(org.w3c.dom.Node)
   * @see WSDLParser#getStartColumn(org.w3c.dom.Node)
   * @see WSDLParser#getEndColumn(org.w3c.dom.Node)
   * @see WSDLParser#getStartLine(org.w3c.dom.Node)
   * @see WSDLParser#getEndLine(org.w3c.dom.Node)
   */
  public static String TRACK_LOCATION = "TRACK_LOCATION"; //$NON-NLS-1$

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

  private static void doSerialize(OutputStream outputStream, Document document) throws IOException
  {
    doSerialize(outputStream, document, null);
  }

  private static void doSerialize(OutputStream outputStream, Document document, String encoding)
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
      boolean trackLocation = options != null && Boolean.TRUE.equals(options.get(TRACK_LOCATION)); 

      if (trackLocation)
      {
        doc = getDocumentUsingSAX(inputStream);
      }
      else
      {
        // Create a DOM document
        doc = getDocument(inputStream, new InternalErrorHandler());
      }

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
   * Use a custom SAX parser to allow us to track the source location of 
   * each node in the source XML document.
   * @param inputStream the parsing source. Must not be null. 
   * @return the DOM document created by parsing the input stream. 
   */
  private Document getDocumentUsingSAX(InputStream inputStream)
  {
    WSDLParser wsdlParser = new WSDLParser();
    wsdlParser.parse(inputStream);
    
    Collection errors = wsdlParser.getDiagnostics();
    
    if (errors != null)
    {
      Iterator iterator = errors.iterator();
      
      while(iterator.hasNext())
      {
          WSDLDiagnostic wsdlDiagnostic = (WSDLDiagnostic)iterator.next();
          switch (wsdlDiagnostic.getSeverity().getValue())
          {
            case WSDLDiagnosticSeverity.FATAL:
            case WSDLDiagnosticSeverity.ERROR:
            {
              getErrors().add(wsdlDiagnostic);
              break;
            }
            case WSDLDiagnosticSeverity.WARNING:
            case WSDLDiagnosticSeverity.INFORMATION:
            {
              getWarnings().add(wsdlDiagnostic);
              break;
          }
        }
      }
    }        
    
    Document doc = wsdlParser.getDocument();
    return doc;
  }

  /**
   * Builds a document using Xerces.
   * @param inputStream the contents to parse.
   * @param errorHandler the handled used by the parser.
   * @return a document.
   */
  private static Document getDocument(InputStream inputStream, ErrorHandler errorHandler) throws IOException
  {
    ClassLoader previousClassLoader = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(WSDLResourceFactoryImpl.class.getClassLoader());

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(false);

      try
      {
        // Using a deferred DOM document in the WSDL model may cause a
        // performance problem in terms of memory consumption in particular.
        // We're attempting to use the feature which instructs the Xerces parser
        // to not use deferred DOM trees.
        // TODO Convert to use setFeature when it becomes available. The Xerces
        // versions < 2.7.1 do not fully support setFeature, so we have to use
        // setAttribute.
        documentBuilderFactory.setAttribute("http://apache.org/xml/features/dom/defer-node-expansion", Boolean.FALSE); //$NON-NLS-1$
      }
      catch (IllegalArgumentException e)
      {
        // Ignore, as the code will have to run with parsers other than Xerces.
      }      

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

  private boolean findDefinition(Element element)
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

  private void handleDefinitionElement(Element element)
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

  private static void doSerialize(OutputStream outputStream, Element element, String encoding) throws IOException
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
 
  private class InternalErrorHandler implements ErrorHandler
  {
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
    
    if (eObject instanceof DefinitionImpl)
    {
      DefinitionImpl definition = (DefinitionImpl) eObject;
      definition.setInlineSchemaLocations(this);    
    }
  }
  /*
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
  }*/
} //WSDLResourceFactoryImpl
