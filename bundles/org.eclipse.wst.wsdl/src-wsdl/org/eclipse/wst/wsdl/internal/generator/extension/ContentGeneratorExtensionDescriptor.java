package org.eclipse.wst.wsdl.internal.generator.extension;

import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;

/*
 * Class which acts as a container to hold information about the extension.
 */
public class ContentGeneratorExtensionDescriptor {
	protected ClassLoader classLoader;
	protected ContentGenerator contentGenerator;
	protected String namespace;
	protected String name;
	protected String className;
	
	public ContentGeneratorExtensionDescriptor(ClassLoader classLoader, String classString, String namespace, String name) {
		this.classLoader = classLoader;
		this.namespace = namespace;
		this.name = name;
		this.className = classString;
	}
	
	
	public Object getContentGenerator() {
		try {
	        Class theClass = classLoader != null ? classLoader.loadClass(className) : Class.forName(className);
	        contentGenerator = (ContentGenerator) theClass.newInstance();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
		
		return contentGenerator;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getName() {
		return name;
	}
}