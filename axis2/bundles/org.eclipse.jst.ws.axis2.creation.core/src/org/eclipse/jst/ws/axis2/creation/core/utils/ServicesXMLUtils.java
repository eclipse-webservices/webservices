/*******************************************************************************
 * Copyright (c) 2007, 2009 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20080604   193371 samindaw@wso2.com - Saminda Wijeratne, creating a function to validate services.xml
 * 20090120   248023 samindaw@wso2.com - Saminda Wijeratne, the xsd resource path string was not valid for windows
 *******************************************************************************/

package org.eclipse.jst.ws.axis2.creation.core.utils;
import java.io.File;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;

public class ServicesXMLUtils {
	
	public ServicesXMLUtils(){
	}
	
	/**
	 * Validates the given xml file against the axis2 services schema. 
	 * @return return true if the xml is valid
	 */
	public static boolean isServicesXMLValid(String servicesXmlPath){
        SchemaFactory factory = 
            SchemaFactory.newInstance(Axis2Constants.XML_SCHEMA);
        
        try {
        	String resourcePath=Axis2Constants.RESOURCE_FOLDER+"/"+Axis2Constants.SERVICES_XSD_SCHEMA_NAME;
            Schema schema = factory.newSchema(
            		WebServiceAxis2CorePlugin.getInstance().getBundle().getResource(resourcePath));
            Validator validator = schema.newValidator();
            Source source = new StreamSource(new File(servicesXmlPath));
            validator.validate(source);
            return true;
        }
        catch (Exception ex) {
            return false;
        }  
	}
}
