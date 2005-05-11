package org.eclipse.wst.wsdl.internal.generator;

import org.eclipse.wst.wsdl.Definition;

public abstract class BaseGenerator {
	private String name;
	private String refName;
	private boolean overwrite;
	protected Definition definition;
	protected ContentGenerator contentGenerator;
	
	
	public void setContentGenerator(ContentGenerator generator) {
		contentGenerator = generator;
	}
	
	public ContentGenerator getContentGenerator() {
		return contentGenerator;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	public boolean getOverwrite() {
		return overwrite;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRefName(String refName) {
		this.refName = refName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRefName() {
		return refName;
	}
	
	public Definition getDefinition() {
		return definition;
	}
	
	public String getProtocol() {
		if (contentGenerator != null) {
			return contentGenerator.getProtocol();
		}
		
		return null;
	}
}
