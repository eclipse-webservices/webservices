/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.wsrt;

public class WebServiceClientImpl {

	private String id;
	private String label;
	private String[] resourceTypeMetadata;
	private String[] extensionMetadata;
	
	public String[] getExtensionMetadata() {
		return extensionMetadata;
	}
	
	public void setExtensionMetadata(String[] extensionMetadata) {
		this.extensionMetadata = extensionMetadata;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String[] getResourceTypeMetadata() {
		return resourceTypeMetadata;
	}
	
	public void setResourceTypeMetadata(String[] resourceTypeMetadata) {
		this.resourceTypeMetadata = resourceTypeMetadata;
	}
	
	

	
	
	
	
	
}
