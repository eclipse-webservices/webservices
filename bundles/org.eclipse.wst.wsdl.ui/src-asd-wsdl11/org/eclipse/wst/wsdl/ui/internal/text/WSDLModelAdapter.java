/*******************************************************************************
 * Copyright (c) 2000 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.text;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.ui.internal.extensions.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.util.XSDSchemaLocationResolverAdapterFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WSDLModelAdapter implements INodeAdapter
{
  protected ResourceSet resourceSet;
  protected Definition definition;

  public Definition getDefinition()
  {
    return definition;
  }

  public void setDefinition(Definition definition)
  {
    this.definition = definition;
  }

  public boolean isAdapterForType(Object type)
  {
    return type == WSDLModelAdapter.class;
  }

  public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos)
  {
  }

  public Definition createDefinition(Element element, Document document)
  {     
    try
    {    	
      IDOMNode domNode = (IDOMNode)element;
      String baseLocation = "blankWSDL.wsdl";
      if (domNode != null) {
    	  baseLocation = domNode.getModel().getBaseLocation();
      }
      else if (document instanceof IDOMNode){
    	  IDOMModel domModel = ((IDOMNode) document).getModel();
    	  baseLocation = domModel.getBaseLocation();
      }
          
      resourceSet = new ResourceSetImpl();
      resourceSet.getAdapterFactories().add(new WSDLModelLocatorAdapterFactory());
      resourceSet.getAdapterFactories().add(new XSDSchemaLocationResolverAdapterFactory());
                     
      // TODO.. .revist the best approach to obtain a URI from the SSE model
      //
      URI uri = null;
      if (baseLocation.startsWith("/"))
      {
        uri = URI.createPlatformResourceURI(baseLocation);
      }
      else
      {
        uri = URI.createFileURI(baseLocation);
      }            
      
      definition = WSDLFactory.eINSTANCE.createDefinition();
      definition.setDocumentBaseURI(uri.toString());
      definition.setDocument(document);
      definition.setElement(element);
      
      WSDLResourceFactoryImpl resourceFactory = new WSDLResourceFactoryImpl();
      Resource resource = resourceFactory.createResource(uri);
      resourceSet.getResources().add(resource);       
      resource.getContents().add(definition);
      resource.setModified(false);    
      ((DefinitionImpl)definition).reconcileReferences(true);    
                 
      // attach an adapter to keep the WSDL model and DOM in sync
      //
      new WSDLModelReconcileAdapter(document, definition);

      // TODO... CS : revisit this line
      // currently this is used to associate a 'type' system with the definition      
      // I suspect that this could be made a whole lot more simple
      //
      WSDLEditorUtil.getInstance().setTypeSystemProvider(definition, new ExtensibleTypeSystemProvider());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return definition;
  }
}
