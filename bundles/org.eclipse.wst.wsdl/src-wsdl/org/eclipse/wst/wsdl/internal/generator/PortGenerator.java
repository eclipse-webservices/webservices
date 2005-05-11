package org.eclipse.wst.wsdl.internal.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;

public class PortGenerator extends BaseGenerator {
	private Service service;
	
	public PortGenerator(Service service) {
		this.service = service;
		definition = service.getEnclosingDefinition();
	}
	
	public Port generatePort() {
		String name = getName();
		String bindingName = getRefName();		
		
	    Port port = WSDLFactory.eINSTANCE.createPort();
	    port.setName(getName());
	    port.setEnclosingDefinition(service.getEnclosingDefinition());
		port.setBinding(getBinding(getRefName()));
	    service.addPort(port);
		
		if (this.getContentGenerator() != null) {
			this.getContentGenerator().generatePortContent(port);
		}
		
		return port;
	}
	
	private Binding getBinding(String bindingName) {
		Iterator bindingIt = definition.getEBindings().iterator();
		while (bindingIt.hasNext()) {
			Binding binding = (Binding) bindingIt.next();
			String currentBindingName = binding.getQName().getLocalPart();
			String currentNamespace = binding.getQName().getNamespaceURI();
			
			List prefixedNames = getPrefixedNames(binding);
			
			if (prefixedNames.contains(bindingName)) {
				return binding;
			}
		}
		
		return null;
	}
	
	private List getPrefixedNames(Binding binding) {
		List prefixedNames = new ArrayList();
		String currentBindingName = binding.getQName().getLocalPart();
		String currentNamespace = binding.getQName().getNamespaceURI();

		Map namespaceMap = definition.getNamespaces();
		Iterator keys = namespaceMap.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			Object value = namespaceMap.get(key);
			
			if (currentNamespace.equals(value)) {
				// Found a match.  Add to our list
				prefixedNames.add(key + ":" + currentBindingName);
			}
		}
		
		return prefixedNames;
	}
	
	public Service getService() {
		return service;
	}
}
