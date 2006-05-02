/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.util;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.edit.W11BindingReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.W11InterfaceReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.W11MessageReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDElementReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDTypeReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.editor.XSDElementReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTypeReferenceEditManager;

public class ReferenceEditManagerHelper {

    
    public static ComponentReferenceEditManager getBindingReferenceEditManager(IASDObject asdObject) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(W11InterfaceReferenceEditManager.class) != null) {
            return  (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(W11BindingReferenceEditManager.class);
        }
        else { 
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput && asdObject instanceof W11EndPoint) {
                WSDLElement element = (WSDLElement) ((W11EndPoint) asdObject).getTarget(); 
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new W11BindingReferenceEditManager((W11Description) description, ((IFileEditorInput) input).getFile());
            }
        }
        
        return null;
    }

    public static ComponentReferenceEditManager getInterfaceReferenceEditManager(IASDObject asdObject) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(W11InterfaceReferenceEditManager.class) != null) {
            return (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(W11InterfaceReferenceEditManager.class);
        }
        else {
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput && asdObject instanceof W11Binding) {
                WSDLElement element = (WSDLElement) ((W11Binding) asdObject ).getTarget();
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new W11InterfaceReferenceEditManager((W11Description) description, ((IFileEditorInput) input).getFile());
            }
        }
        
        return null;
    }

    public static ComponentReferenceEditManager getMessageReferenceEditManager(IASDObject asdObject) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(W11MessageReferenceEditManager.class) != null) {
            return (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(W11MessageReferenceEditManager.class);
        }
        else {
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput && asdObject instanceof W11MessageReference) {
                WSDLElement element = (WSDLElement) ((W11MessageReference) asdObject).getTarget();
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new W11MessageReferenceEditManager((W11Description) description, ((IFileEditorInput) input).getFile());
            }
        }
   
        return null;
    }

    public static ComponentReferenceEditManager getXSDElementReferenceEditManager(IASDObject asdObject) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(XSDElementReferenceEditManager.class) != null) {
            return (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(XSDElementReferenceEditManager.class);
        }
        else {
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput && asdObject instanceof W11ParameterForPart) {
                WSDLElement element = (WSDLElement) ((W11ParameterForPart) asdObject).getTarget();
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new WSDLXSDElementReferenceEditManager(((IFileEditorInput) input).getFile(), null, description);
            }
        }
        
        return null;
    }

    public static ComponentReferenceEditManager getXSDTypeReferenceEditManager(IASDObject asdObject) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(XSDTypeReferenceEditManager.class) != null) {
            return (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(XSDTypeReferenceEditManager.class);
        }
        else {
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput && asdObject instanceof IParameter && asdObject instanceof WSDLBaseAdapter) {
                WSDLElement element = (WSDLElement) ((WSDLBaseAdapter) asdObject).getTarget();
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new WSDLXSDTypeReferenceEditManager(((IFileEditorInput) input).getFile(), null, description);
            }
        }

        return null;
    }
}
