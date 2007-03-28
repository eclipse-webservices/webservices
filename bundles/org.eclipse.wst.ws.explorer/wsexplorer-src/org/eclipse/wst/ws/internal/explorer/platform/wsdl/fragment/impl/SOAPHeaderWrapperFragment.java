/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070305   117034 makandre@ca.ibm.com - Andrew Mak, Web Services Explorer should support SOAP Headers
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataException;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.constants.FragmentConstants;
import org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.IXSDFragment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A SOAP header fragment, which is a wrapper around the fragment that represents the SOAP header content.  
 */
public class SOAPHeaderWrapperFragment extends XSDDelegationFragment {
	
	private static final String MUST_UNDERSTAND = "mustUnderstand";
	private static final String ACTOR           = "actor";
	
	private boolean mustUnderstand = false;	 
	private String  actor          = "";
	private boolean validActor     = true;
	
	/**
	 * Constructor.
	 * 
	 * @param fragment The fragment that this SOAP header fragment wraps around.
	 */
	public SOAPHeaderWrapperFragment(IXSDFragment fragment) {
		super(fragment.genID(), fragment.getName(), null);	    
		setXSDDelegationFragment(fragment);
	}
	
	/*
	 * Retrieves the first element from a string array.
	 */
	private String getFirstElement(String[] stringArray) {
		if (stringArray == null || stringArray.length == 0)
			return null;
		return stringArray[0];		 
	}
	
	/*
	 * Retrieves the first element from a vector.
	 */
	private String getFirstElement(Vector vector) {
		if (vector == null || vector.isEmpty())
			return null;
		 
		Object obj = vector.firstElement();
		if (!(obj instanceof String))
			return null;
		 
		return (String) obj;
	}
	
	/*
	 * Sets the mustUnderstand value. Any non-empty string (except "0")
	 * translates to a mustUnderstand value of true.
	 */
	private void setMustUnderstand(String param) {
		mustUnderstand = (param != null && !param.equals("0"));
	}
	
	/*
	 * Sets the actor value.  Also sets the flag for validActor.
	 */
	private void setActor(String param) {
		if (param == null || param.length() == 0)
			actor = "";
		else {
			actor = param;
			try {
				new URI(actor);
			}
			catch (URISyntaxException e) {
				validActor = false;
				return;
			}
		}		 
		 
		validActor = true;
	}	 
	
    /* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#processParameterValues(org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser)
	 */
	public boolean processParameterValues(MultipartFormDataParser parser) throws MultipartFormDataException {
	
		setMustUnderstand(parser.getParameter(getMustUnderstandID()));
	    setActor(parser.getParameter(getActorID()));

		// mustUnderstand is either set or unset, no further validation necessary
		// only need to validate actor URI
		return super.processParameterValues(parser) && validateActor();			 		 
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#setParameterValues(java.lang.String, java.lang.String[])
	 */
	public void setParameterValues(String paramKey, String[] params) {
		if (getMustUnderstandID().equals(paramKey))
			setMustUnderstand(getFirstElement(params));
		else if (getActorID().equals(paramKey))
			setActor(getFirstElement(params));
		else
			super.setParameterValues(paramKey, params);		 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#setParameterValues(java.lang.String, java.util.Vector)
	 */
	public void setParameterValues(String paramKey, Vector params) {
		if (getMustUnderstandID().equals(paramKey))
			setMustUnderstand(getFirstElement(params));
		else if (getActorID().equals(paramKey))
			setActor(getFirstElement(params));
		else
			super.setParameterValues(paramKey, params);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String paramKey) {
		if (getMustUnderstandID().equals(paramKey))
			return new String[] { mustUnderstand ? "1" : "0" };
		else if (getActorID().equals(paramKey))
			return new String[] { actor };
		else
			return super.getParameterValues(paramKey);		 
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#getParameterValue(java.lang.String, int)
	 */
	public String getParameterValue(String paramKey, int paramIndex) {
		return getParameterValues(paramKey)[paramIndex];
	}	 
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#validateAllParameterValues()
	 */
	public boolean validateAllParameterValues() {
		return super.validateAllParameterValues() && validateActor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#validateParameterValues(java.lang.String)
	 */
	public boolean validateParameterValues(String paramKey) {
		if (getMustUnderstandID().equals(paramKey))
			return true;
		else if (getActorID().equals(paramKey))
			return validateActor();
		else
			return super.validateParameterValues(paramKey);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#validateParameterValue(java.lang.String, int)
	 */
	public boolean validateParameterValue(String paramKey, int paramIndex) {
		if (getMustUnderstandID().equals(paramKey))
			return true;
		else if (getActorID().equals(paramKey))
			return validateActor();
		else
			return super.validateParameterValue(paramKey, paramIndex);
	}
	
	/**
	 * Sets the values for this SOAP header given a hashtable of instance documents and a namespace table.
	 * 
	 * @param instanceDocuments The hashtable of instance documents.  The key is the document's name with namespace prefix.
	 * @param namespaceTable The namespace table.
	 * 
	 * @return True if all values extracted from the instance document are valid.
	 */
	public boolean setParameterValuesFromInstanceDocuments(Hashtable instanceDocuments, Hashtable namespaceTable) {
		String tagName = ((XSDFragment) getXSDDelegationFragment()).getInstanceDocumentTagName(namespaceTable);
		Element instanceDocument = (Element) instanceDocuments.get(tagName);
		 
		if (instanceDocument == null)
			return false;
		 
		boolean valid = setParameterValuesFromInstanceDocuments(new Element[] { instanceDocument });		
		 
		String prefix = getPrefixFromNamespaceURI(FragmentConstants.NS_URI_SOAP_ENV, namespaceTable) + FragmentConstants.COLON;
		 
		String mustUnderstandValue = instanceDocument.getAttribute(prefix + MUST_UNDERSTAND);
		if ("".equals(mustUnderstandValue))
			mustUnderstand = false;
		else if ("0".equals(mustUnderstandValue) || "1".equals(mustUnderstandValue))
			setMustUnderstand(mustUnderstandValue);
		else
			valid = false;
		 
		setActor(instanceDocument.getAttribute(prefix + ACTOR));
		 
		return valid && validateActor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#genInstanceDocumentsFromParameterValues(boolean, java.util.Hashtable, org.w3c.dom.Document)
	 */
	public Element[] genInstanceDocumentsFromParameterValues(boolean genXSIType, Hashtable namespaceTable, Document doc) {
		Element[] elements = super.genInstanceDocumentsFromParameterValues(genXSIType, namespaceTable, doc);
		 
		if (elements.length == 0)
			return elements;
		 
		String prefix = getPrefixFromNamespaceURI(FragmentConstants.NS_URI_SOAP_ENV, namespaceTable) + FragmentConstants.COLON;
		 
		for (int i = 0; i < elements.length; i++) {			 			 				 
			
			if (mustUnderstand)
				elements[i].setAttribute(prefix + MUST_UNDERSTAND, "1");

			if (actor.length() > 0)
				elements[i].setAttribute(prefix + ACTOR, actor);
		}
		 
		return elements;
	}
	 
	/**
	 * Convenience method for getting for the mustUnderstand value
	 * @return The mustUnderstand value, true if mustUnderstand="1", false otherwise.
	 */
	public boolean isMustUnderstand() {
		return mustUnderstand;
	}
	 
	/**
	 * Convenience method for getting the actor value
	 * @return The actor value
	 */
	public String getActor() {
		return actor;
	}
	
	/**
	 * Determines if the actor value is a valid URI.
	 * @return True if the actor value is valid, false otherwise.
	 */
	public boolean validateActor() {		 
		return validActor;
	}
	
	/**
	 * Returns the ID of the mustUnderstand input element
	 * @return The ID of the mustUnderstand input element
	 */
	public String getMustUnderstandID() {
		return getID() + FragmentConstants.ID_SEPERATOR + MUST_UNDERSTAND;
	}
	
	/**
	 * Returns the ID of the actor input element
	 * @return The ID of the actor input element
	 */
	public String getActorID() {
		return getID() + FragmentConstants.ID_SEPERATOR + ACTOR;
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.wst.ws.internal.explorer.platform.wsdl.fragment.impl.XSDDelegationFragment#getWriteFragment()
	 */
	public String getWriteFragment() {
		return "/wsdl/fragment/SOAPHeaderWrapperWFragmentJSP.jsp";
	}		 
}
