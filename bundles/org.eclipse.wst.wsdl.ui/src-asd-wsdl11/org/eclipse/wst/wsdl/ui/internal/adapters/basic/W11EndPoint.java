/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.extension.ContentGeneratorExtensionFactoryRegistry;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetAddressCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11SetBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.specialized.W11AddressExtensibilityElementAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddEndPointAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetExistingBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDSetNewBindingAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObjectListener;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IService;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLEditorUtil;

public class W11EndPoint extends WSDLBaseAdapter implements IEndPoint, IASDObjectListener {
 
    protected List addressExtensiblityElements = null;
    protected List thingsToListenTo = null;
    
	public String getAddress() {
        List list = getAddressExtensiblityElements();
        if (list.size() > 0)
        {
          W11AddressExtensibilityElementAdapter addressEE = (W11AddressExtensibilityElementAdapter)list.get(0);
          return addressEE.getLocationURI();
        }	
		return ""; //$NON-NLS-1$
	}
	
	public List getApplicableProtocol() {
		ContentGeneratorExtensionFactoryRegistry factoryRegistry = ContentGeneratorExtensionFactoryRegistry.getInstance();
		List protocols = factoryRegistry.getBindingExtensionNames();
		return protocols;
	}
	
	public void setProtocol(String newProtocol) {
		ContentGeneratorExtensionFactoryRegistry factoryRegistry = ContentGeneratorExtensionFactoryRegistry.getInstance();
		ContentGenerator contentGenerator = factoryRegistry.getGeneratorClassFromName(newProtocol);
		if (contentGenerator != null) {
			String currentAddress = getAddress();
			if (currentAddress != null) {
				contentGenerator.setAddressLocation(currentAddress);
			}

			Port port = getPort();
			// go ahead and add required namespaces first before generating port content
			CreateWSDLElementHelper.addRequiredNamespaces(contentGenerator, port.getEnclosingDefinition());
			contentGenerator.generatePortContent(port);
		}
	}

    protected List getAddressExtensiblityElements()
    {       
        addressExtensiblityElements = new ArrayList();
        thingsToListenTo = new ArrayList();
        Port port = (Port) getTarget();          
        for (Iterator it = port.getEExtensibilityElements().iterator(); it.hasNext(); )
        {   
          Notifier item = (Notifier)it.next();
          Adapter adapter = createAdapter(item);
          if (adapter instanceof W11AddressExtensibilityElementAdapter)
          {  
            addressExtensiblityElements.add(adapter);
          }             
          if (adapter instanceof IASDObject)
          {  
            thingsToListenTo.add(adapter);
          }  
        }
        for (Iterator i = thingsToListenTo.iterator(); i.hasNext(); )
        {
          IASDObject object = (IASDObject)i.next();
          object.registerListener(this);
        }  
       
      return addressExtensiblityElements;
    }
    
    protected void clearAddressExtensiblityElements()
    {    
      if (thingsToListenTo != null)
      {  
        for (Iterator i = thingsToListenTo.iterator(); i.hasNext(); )
        {
          IASDObject object = (IASDObject)i.next();
          object.unregisterListener(this);
        }
      }
      thingsToListenTo = null;
      addressExtensiblityElements = null;
    }

	public IBinding getBinding() {
		if (getPort().getEBinding() != null) {
			return (IBinding) createAdapter(getPort().getEBinding());
		}
		
		return null;
	}

	public String getName() {
		return getPort().getName();
	}

	public String getTypeName() {
	  return getAddress();
	}
	
	public Object getType() {
		return getBinding();
	}

	private Port getPort() {
		return (Port) target;
	}
	
	public IService getOwnerService() {
		return (IService) owner;
	}
	
	public String[] getActions(Object object) {
    Collection actionIDs = new ArrayList();

    actionIDs.add(ASDAddEndPointAction.ID);
    actionIDs.add(BaseSelectionAction.SUBMENU_START_ID + Messages._UI_ACTION_SET_BINDING); //$NON-NLS-1$
    actionIDs.add(ASDSetNewBindingAction.ID);
    actionIDs.add(ASDSetExistingBindingAction.ID);
    actionIDs.add(BaseSelectionAction.SUBMENU_END_ID);
    actionIDs.add(ASDDeleteAction.ID);
    if (isReadOnly()) {
      actionIDs.add(OpenInNewEditor.ID);
    }
    return (String [])actionIDs.toArray(new String[0]);
	}
	
	public Command getSetBindingCommand(IBinding binding) {
		W11Binding w11Binding = (W11Binding) binding;
		return new W11SetBindingCommand((Port) target, (Binding) w11Binding.getTarget());
	}
	
	public Command getSetAddressCommand(String newAddress) {
		return new W11SetAddressCommand((Port) this.getTarget(), newAddress);
	}
    
    public void propertyChanged(Object object, String property)
    {
      // this is called when one of the 'address' extensibility element adapters we're listening to changes
      //
      clearAddressExtensiblityElements();      
      notifyListeners(this, null);
    }
    
    public Image getImage() {
        String imagePath = WSDLEditorUtil.isOrientationRightToLeft() ? "icons/port_obj_rtl.gif" : "icons/port_obj.gif"; //$NON-NLS-1$ //$NON-NLS-2$  
        WSDLEditorPlugin plugin = WSDLEditorPlugin.getInstance();
        return plugin.getImage(imagePath);
    }

	public String getText() {
		return "port"; //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		return ITreeElement.EMPTY_LIST;
	}
	
	public boolean hasChildren() {
		return false;
	}
	
	public ITreeElement getParent() {
		return null;
	}

	public String getProtocol() {
		String protocol = ""; //$NON-NLS-1$
		
		List list = getAddressExtensiblityElements();
        if (list.size() > 0) {
        	W11AddressExtensibilityElementAdapter addressEE = (W11AddressExtensibilityElementAdapter)list.get(0);
        	Object target = addressEE.getTarget();
			if (target instanceof WSDLElement) {
				WSDLElement wsdlElement = (WSDLElement) target;
				String namespace = wsdlElement.getElement().getNamespaceURI();
				
				if (namespace != null) {
					ContentGeneratorExtensionFactoryRegistry factoryRegistry = ContentGeneratorExtensionFactoryRegistry.getInstance();
					ContentGenerator contentGenerator = factoryRegistry.getGeneratorClassFromNamespace(namespace);
					if (contentGenerator != null) {
						protocol = contentGenerator.getProtocol();
					}
				}
			}
        }	

		return protocol;
	}
}
