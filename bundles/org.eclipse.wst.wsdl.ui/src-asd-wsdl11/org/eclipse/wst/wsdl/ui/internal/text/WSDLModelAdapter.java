/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.text;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.ui.internal.extensions.ExtensibleTypeSystemProvider;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xsd.ui.internal.util.ModelReconcileAdapter;
import org.eclipse.wst.xsd.ui.internal.util.XSDSchemaLocationResolverAdapterFactory;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WSDLModelAdapter implements INodeAdapter
{
  protected ResourceSet resourceSet;
  protected Definition definition;
  private ModelReconcileAdapter modelReconcileAdapter;

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

  /**
   * @deprecated
   */
  public Definition createDefinition(Element element, Document document)
  {
    return createDefinition(document);
  }

  public Definition createDefinition(final Document document)
  {
    try
    {
      IDOMNode domNode = (IDOMNode) document;
      String baseLocation = "blankWSDL.wsdl"; //$NON-NLS-1$
      if (domNode != null)
      {
        baseLocation = domNode.getModel().getBaseLocation();
      }
      else if (document instanceof IDOMNode)
      {
        IDOMModel domModel = ((IDOMNode) document).getModel();
        baseLocation = domModel.getBaseLocation();
      }
      resourceSet = new ResourceSetImpl();
      resourceSet.getAdapterFactories().add(new WSDLModelLocatorAdapterFactory());
      resourceSet.getAdapterFactories().add(new XSDSchemaLocationResolverAdapterFactory());
      // TODO.. .revist the best approach to obtain a URI from the SSE model
      //
      URI uri = null;
      if (baseLocation.startsWith("/")) //$NON-NLS-1$
      {
        uri = URI.createPlatformResourceURI(baseLocation);
      }
      else if (baseLocation.startsWith("http")) //$NON-NLS-1$
      {
        uri = URI.createURI(baseLocation);
      }
      else
      {
        uri = URI.createFileURI(baseLocation);
      }
      definition = WSDLFactory.eINSTANCE.createDefinition();
      definition.setDocumentBaseURI(uri.toString());
      definition.setDocument(document);

      WSDLResourceFactoryImpl resourceFactory = new WSDLResourceFactoryImpl();
      Resource resource = resourceFactory.createResource(uri);
      resourceSet.getResources().add(resource);
      resource.getContents().add(definition);
      resource.setModified(false);

      IRunnableWithProgress setElementOperation = new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          // Use the animated flavour as we don't know beforehand how many ticks we need.
          // The task name will be displayed by the code in WSDLResourceImpl and XSDResourceImpl.
          
          monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

          Map loadOptions = resourceSet.getLoadOptions();
          
          loadOptions.put(WSDLResourceImpl.WSDL_PROGRESS_MONITOR, monitor);
          loadOptions.put(XSDResourceImpl.XSD_PROGRESS_MONITOR, monitor);
          
          definition.setElement(document.getDocumentElement());
          ((DefinitionImpl) definition).reconcileReferences(true);
          
          loadOptions.remove(WSDLResourceImpl.WSDL_PROGRESS_MONITOR);
          loadOptions.remove(XSDResourceImpl.XSD_PROGRESS_MONITOR);
        }
      };

      IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
      try
      {
        progressService.busyCursorWhile(setElementOperation);
      }
      catch (InvocationTargetException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }             
      
      // attach an adapter to keep the WSDL model and DOM in sync
      //
      modelReconcileAdapter = new WSDLModelReconcileAdapter(document, definition);
      domNode.getModel().addModelStateListener(modelReconcileAdapter);
      
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
  
  public ModelReconcileAdapter getModelReconcileAdapter()
  {
    return modelReconcileAdapter;
  }  
  
  public static WSDLModelAdapter lookupOrCreateModelAdapter(Document document)
  {
    WSDLModelAdapter adapter = null;
    if (document instanceof INodeNotifier)
    {
      INodeNotifier notifier = (INodeNotifier)document;
      adapter = (WSDLModelAdapter)notifier.getAdapterFor(WSDLModelAdapter.class);
      if (adapter == null)
      {
        adapter = new WSDLModelAdapter();       
        notifier.addAdapter(adapter);        
      } 
    }   
    return adapter;
  }  
}
