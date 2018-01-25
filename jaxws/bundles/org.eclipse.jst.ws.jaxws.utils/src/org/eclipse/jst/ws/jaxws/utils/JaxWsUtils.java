/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils;

import org.eclipse.jdt.core.IType;

/**
 * Utility that contains JAX-WS releated functionality
 * 
 * @author Danail Branekov
 * 
 */
public class JaxWsUtils
{
	private static final String BEAN_SUFFIX = "Bean"; //$NON-NLS-1$
	private static final String PORT_SUFFIX = "Port"; //$NON-NLS-1$
	private static final String SERVICE_SUFFIX = "Service"; //$NON-NLS-1$
	
	private JaxWsUtils()
	{
		// hide default constructor
	}

	/**
	 * An utility method for composing a default target namespace for JaxWs customization
	 * out of a package name.
	 * Algorithm:
	 * <li>In case the package has 0 fragment (default package) the result would be http:///</li>
	 * <li>In case the package has 1 fragment the result would be http://fragment1/</li>
	 * <li>In case the package has 2 fragments the result would be http://fragment2.fragment1/</li>
	 * <li>In case the package has more than 2 fragments the result would be http://fragment2.fragment1/fragment3/fragment4/.../fragmentn/</li>
	 * @param packageName Name of the package
	 * @return the composed namespace
	 * @thorws NullPointerException when package name specified is null
	 */
	public static String composeJaxWsTargetNamespaceByPackage(String packageName)
	{
		if(packageName == null)
		{
			throw new NullPointerException("Package name cannot be null"); //$NON-NLS-1$
		}
		
		String[] pkgFragments = packageName.split("\\."); //$NON-NLS-1$
		
		if(pkgFragments.length > 1)
		{
			// reverse the first two fragments in case there are at least two fragments available
			String frag0 = pkgFragments[0];
			pkgFragments[0] = pkgFragments[1];
			pkgFragments[1] = frag0;
		}
		
		StringBuilder result = new StringBuilder("http://"); //$NON-NLS-1$
		for (int i = 0; i < pkgFragments.length; i++)
		{
			result.append(pkgFragments[i]);
			result.append(i==0 && pkgFragments.length>1 ? "." : "/"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		return result.toString();
	}
	
	/**
	 * Same as composeJaxWsTargetNamespaceByPackage() but first extracts the package out of class
	 * fully qualified name
	 * @param fqName the class fully qualified name
	 * @return calculated target namespace
	 */
	public static String composeJaxWsTargetNamespaceByFQName(String fqName)
	{
		ContractChecker.nullCheckParam(fqName, "fqName"); //$NON-NLS-1$
		return composeJaxWsTargetNamespaceByPackage(extractPackage(fqName));
	}
	
	/**
	 * Returns the PortType name from the <code>endpointType</code> and <code>seiName</code>.
	 * @param endpoint class.
	 * @param seiName
	 * @return PortType name for the @WebService annotation, the name of the <code>endpointType</code> with the "Port" suffix.
	 * @throws NullPointerException when <code>endpointType</code> specified is null.
	 */
	public static String getPorttypeName(IType endpointType, String seiName)
	{
		if(endpointType == null)
		{
			throw new NullPointerException("endpointType could not be null!"); //$NON-NLS-1$
		}

		if(seiName != null && seiName.length() > 0)
		{
			return seiName;
		}
		
		return removeBeanSuffixIfRequired(endpointType.getElementName());
	}
	
	/**
	 * Removes the "Bean" suffix from the <code>endpointType</code> in case it is present and generates Service name for the specified IType.
	 * @param endpoint class.
	 * @return Service name for the @WebService annotation, the name of the <code>endpointType</code> without the "Bean" suffix and <code>"Service"</code> at the end.
	 * In case the name of the <code>endpointType</code> equals "Bean" then the original name is returned with <code>"Service"</code> ate the end.
	 * @throws NullPointerException when <code>endpointType</code> specified is null
	 */
	public static String getServiceName(final IType endpointType)
	{
		if(endpointType == null)
		{
			throw new NullPointerException("endpointType could not be null!"); //$NON-NLS-1$
		}
		
		return removeBeanSuffixIfRequired(endpointType.getElementName()) + SERVICE_SUFFIX;
	}

	/**
	 * Removes the "Bean" suffix from the endpoint name in case it is present.  
	 * @param endpointName the name of the endpoint
	 * @return the name of the endpoint without the "Bean" suffix. In case the name of the endpoint equals "Bean" then the original name is returned
	 */
	private static String removeBeanSuffixIfRequired(final String endpointName)
	{
		assert endpointName != null && endpointName.length() > 0;
		
		if(endpointName.equals(BEAN_SUFFIX))
		{
			return endpointName;
		}
		
		if(endpointName.endsWith(BEAN_SUFFIX))
		{
			return endpointName.substring(0, endpointName.lastIndexOf(BEAN_SUFFIX));
		}
		
		return endpointName;
	}

	/**
	 * Returns the Port name from the <code>endpointType</code>.
	 * @param endpoint class.
	 * @return Port name for the @WebService annotation, the name of the <code>endpointType</code> with the "Port" suffix.
	 * @throws NullPointerException when <code>endpointType</code> specified is null.
	 */
	public static String getPortName(final IType endpointType)
	{
		return getDefaultPortName(endpointType.getElementName());
	}
	
	/**
	 * Returns the default service name calculated as the specification states.
	 * @param fqName
	 * @return the default service name
	 * @throws NullPointerException in case <code>endpointType</code> is <code>null</code>
	 */
	public static String getDefaultServiceName(final String fqName)
	{
		if(fqName == null)
		{
			throw new NullPointerException("endpointType could not be null!"); //$NON-NLS-1$
		}
		
		return extractShortName(fqName) + SERVICE_SUFFIX;
	}
	
	/**
	 * Returns the default wsdl:portType name defined as JAX-WS specification states. 
	 * @param fqName
	 * @return
	 */
	public static String getDefaultPorttypeName(final String fqName)
	{
		if(fqName == null)
		{
			throw new NullPointerException("fqName could not be null!"); //$NON-NLS-1$
		}
		
		return extractShortName(fqName);
	}
	
	/**
	 * Return the default name for a wsdl:port defined as JAX-WS specification states 
	 * @param fqName
	 * @return the calculated name
	 */
	public static String getDefaultPortName(final String fqName)
	{
		if(fqName == null)
		{
			throw new NullPointerException("fqName could not be null!"); //$NON-NLS-1$
		}

		return extractShortName(fqName) + PORT_SUFFIX;
	}	
	
	private static String extractShortName(String fqName) 
	{
		int pos = fqName.lastIndexOf('.');
		if (pos ==-1 ) {
			return fqName;
		}
		
		return fqName.substring(pos+1);
	}
	
	private static String extractPackage(final String fqName) 
	{
		int pos = fqName.lastIndexOf('.');
		if (pos ==-1 ) {
			return fqName;
		}
		
		return fqName.substring(0, pos);
	}
}
