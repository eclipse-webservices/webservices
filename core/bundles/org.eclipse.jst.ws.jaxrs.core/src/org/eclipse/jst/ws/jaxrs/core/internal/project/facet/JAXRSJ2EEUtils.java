/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100325   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20100408   308565 kchong@ca.ibm.com - Keith Chong, JAX-RS: Servlet name and class not updated
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20150325   463126 jgwest@ca.ibm.com - Jonathan West,  JAX-RS Facet Install Page servlet-class field validation is too strict  
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.common.Description;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.JSPType;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * Utility file for Web 2.3 & 2.4 model
 * 
 */
public class JAXRSJ2EEUtils extends JAXRSUtils {

	/**
	 * Convenience method for getting writeable WebApp model
	 * 
	 * @param project
	 * @return WebArtifactEdit
	 * @deprecated - must use IModelProviders
	 */
	public static WebArtifactEdit getWebArtifactEditForWrite(
			final IProject project) {
		return WebArtifactEdit.getWebArtifactEditForWrite(project);
	}

	/**
	 * Convenience method for getting read-only WebApp model
	 * 
	 * @param project
	 * @return WebArtifactEdit
	 * @deprecated - must use IModelProviders
	 */
	public static WebArtifactEdit getWebArtifactEditForRead(
			final IProject project) {
		return WebArtifactEdit.getWebArtifactEditForRead(project);
	}
	@SuppressWarnings("unchecked")
	private static Servlet findJAXRSServletUsingLibraryProviderID(final Object webApp, String selectedLibraryProviderID) {
		Iterator<Servlet> it = null;
		Servlet toReturn = null;
		if (webApp == null)
			return null;
		else if (webApp instanceof WebApp)
			it = ((WebApp) webApp).getServlets().iterator();
// See Bug 293103		
//		else if (webApp instanceof org.eclipse.jst.javaee.web.WebApp)
//			it = ((org.eclipse.jst.javaee.web.WebApp) webApp).getServlets()
//					.iterator();
		else
			return null;

		while (it.hasNext()) {
			Servlet servlet = it.next();
			
		    Iterator <Description> descIter = servlet.getDescriptions().iterator();
		    while (descIter.hasNext())
		    {
		       Description desc = descIter.next();
		       String value = desc.getValue();
		       if (value != null && value.trim().startsWith(JAXRS_SERVLET_IDENTIFIER))
		       {
		    	   return servlet;
		       }
		    }
			
			if (servlet != null && servlet.getWebType() != null) {

				if (servlet.getWebType().isServletType()) {
					if (servlet.getServletName() != null
							&& (servlet.getServletName().trim().equals(
									getSavedservletName()) || servlet
									.getServletName().trim().equals(
											JAXRS_DEFAULT_SERVLET_NAME))) {
						return servlet;
					}

					if (((ServletType) servlet.getWebType()).getClassName() != null
							&& ((ServletType) servlet.getWebType()).getClassName().trim().equals(
									getSavedServletClassName(selectedLibraryProviderID))) {
						return servlet;
					}
					
					if (((ServletType) servlet.getWebType()).getClassName() != null
							&& facetKnowsServletClassName(((ServletType) servlet.getWebType()).getClassName().trim()))
						return servlet;
					if (((ServletType) servlet.getWebType()).getClassName() != null
							&& ((ServletType) servlet.getWebType()).getClassName().trim().equals(JAXRS_SERVLET_CLASS)) {
						if (toReturn == null)
							//found a servlet with empty servlet class, return that if find nothing else 
							toReturn = servlet;

					}

				} else if (servlet.getWebType().isJspType()) {
					if (((JSPType) servlet.getWebType()).getJspFile() != null
							&& ( ((JSPType) servlet.getWebType()).getJspFile()
									.trim().equals(JAXRS_SERVLET_CLASS) || ((JSPType) servlet.getWebType()).getJspFile()
									.trim().equals(getSavedservletName())) ) {
						return servlet;
					}
				}
			}
		}

		// if we get to here then we have finished the loop
		// without finding the servlet we're looking for
		return toReturn;

	}
	public static Servlet findJAXRSServlet(final Object webApp, String selectedLibraryProviderID) {
		return findJAXRSServletUsingLibraryProviderID(webApp, selectedLibraryProviderID);
	}
	/**
	 * @param webApp
	 *            as Object
	 * @return Servlet - the JAXRS Servlet for the specified WebApp or null if
	 *         not present
	 */
	@SuppressWarnings("unchecked")
	public static Servlet findJAXRSServlet(final Object webApp) {
		return findJAXRSServletUsingLibraryProviderID(webApp, null);
	}

	/**
	 * Creates servlet reference in WebApp if not present or updates servlet
	 * name if found using the passed configuration.
	 * 
	 * @param webApp
	 * @param config
	 * @param servlet
	 * @return Servlet servlet - if passed servlet was null, will return created
	 *         servlet
	 */
	public static Servlet createOrUpdateServletRef(final WebApp webApp,  final IDataModel config, Servlet servlet) {

		String displayName = getDisplayName(config);
		String className = getServletClassname(config);

		// For 2.3 and 2.4, return empty element rather than null
		if(className == null || className.trim().length() == 0 ) {
			className = JAXRSUtils.JAXRS_SERVLET_CLASS; // The default name of the JAX-RS servlet class
		}
		
		return createOrUpdateServletRef(webApp, displayName, className, servlet);
	}
	
	public static Servlet createOrUpdateServletRef(final WebApp webApp,
			String displayName, String className, Servlet servlet) {

		if (servlet == null) {
			// Create the servlet instance and set up the parameters from data
			// model
			servlet = WebapplicationFactory.eINSTANCE.createServlet();
			servlet.setServletName(displayName);
			servlet.setDescription(JAXRS_SERVLET_IDENTIFIER_DESCRIPTION);

			ServletType servletType = WebapplicationFactory.eINSTANCE
					.createServletType();
			servletType.setClassName(className);
			servlet.setWebType(servletType);
			servlet.setLoadOnStartup(Integer.valueOf(1));
			// Add the servlet to the web application model
			webApp.getServlets().add(servlet);
		} else {
			// update
			updateServletMappings(webApp, servlet, servlet.getServletName()
					.trim(), displayName);
			servlet.setServletName(displayName);
			ServletType servletType = WebapplicationFactory.eINSTANCE
			.createServletType();
			servletType.setClassName(className);
			servlet.setWebType(servletType);
			servlet.setLoadOnStartup(Integer.valueOf(1));
			Description jaxrsToolsDescription = null;
			Description next = null;
			List<Description> descriptions = servlet.getDescriptions();
			if (descriptions != null) {
				Iterator<Description> it = descriptions.iterator();
				while (it.hasNext()) {
					next = it.next();
					if (JAXRS_SERVLET_IDENTIFIER_DESCRIPTION.equals(next
							.getValue())) {
						jaxrsToolsDescription = next;
						break;
					}
				}

			}
			if (jaxrsToolsDescription == null) {
				// add the description for our JAX-RS tools
				servlet.setDescription(JAXRS_SERVLET_IDENTIFIER_DESCRIPTION);
			}

		}
		return servlet;
	}

	private static void updateServletMappings(final WebApp webApp,
			final Servlet servlet, final String servletName,
			final String newServletName) {
		List<ServletMapping> mappings = findServletMappings(webApp, servlet,
				servletName);
		for (ServletMapping map : mappings) {
			map.setName(newServletName);
		}

	}

	@SuppressWarnings("unchecked")
	private static List<ServletMapping> findServletMappings(
			final WebApp webApp, final Servlet servlet, final String servletName) {
		List<ServletMapping> mappings = new ArrayList<ServletMapping>();
		List<ServletMapping> allMappings = webApp.getServletMappings();
		for (int i = allMappings.size() - 1; i >= 0; --i) {
			ServletMapping mapping = allMappings.get(i);
			if (mapping != null
					&& mapping.getServlet() != null
					&& mapping.getServlet().getServletName() != null
					&& mapping.getServlet().getServletName().trim().equals(
							servletName))
				mappings.add(mapping);
		}
		return mappings;
	}

	/**
	 * @param webApp
	 *            as Object
	 * @return true if webApp instanceof org.eclipse.jst.javaee.web.WebApp
	 */
	public static boolean isWebApp25(final Object webApp) {
		if (webApp instanceof org.eclipse.jst.javaee.web.WebApp)
			return true;
		return false;
	}

	/**
	 * Creates servlet-mappings for the servlet
	 * 
	 * @param webApp
	 * @param urlMappingList
	 *            - list of string values to be used in url-pattern for
	 *            servlet-mapping
	 * @param servlet
	 */
	@SuppressWarnings("unchecked")
	public static void setUpURLMappings(final WebApp webApp,
			final List urlMappingList, final Servlet servlet) {
		// Add mappings
		Iterator it = urlMappingList.iterator();
		while (it.hasNext()) {
			String pattern = (String) it.next();
			if (!(doesServletMappingExist(webApp, servlet, pattern))) {
				ServletMapping mapping = WebapplicationFactory.eINSTANCE
						.createServletMapping();
				mapping.setServlet(servlet);
				mapping.setName(servlet.getServletName());
				mapping.setUrlPattern(pattern);
				webApp.getServletMappings().add(mapping);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updateURLMappings(final WebApp webApp,
			final List urlMappingList, final Servlet servlet) {
		// TODO
		if (urlMappingList != null) {
			List<ServletMapping> mappings = findServletMappings(webApp,
					servlet, servlet.getServletName());
			Iterator it = urlMappingList.iterator();
			while (it.hasNext()) {
				String pattern = (String) it.next();
				if (!(doesServletMappingExist(webApp, servlet, pattern))) {
					ServletMapping mapping = WebapplicationFactory.eINSTANCE
							.createServletMapping();
					mapping.setServlet(servlet);
					mapping.setName(servlet.getServletName());
					mapping.setUrlPattern(pattern);
					webApp.getServletMappings().add(mapping);
				}

			}
			// Now gather up the patterns that aren't defined in the UI...
			List<ServletMapping> patternsToRemove = new ArrayList<ServletMapping>();
			for (Iterator<ServletMapping> iter = mappings.iterator(); iter.hasNext();)
			{
			   ServletMapping next = iter.next();
			   String patternTypeValue = next.getUrlPattern();
			   if (patternTypeValue != null && !urlMappingList.contains(patternTypeValue))
			   {
			     patternsToRemove.add(next);
			   }
		    }
			//...and remove them from the model
			for (Iterator<ServletMapping> iter = patternsToRemove.iterator(); iter.hasNext(); )
			{
			   webApp.getServletMappings().remove(iter.next());
			}
			


		}
	}

	@SuppressWarnings("unchecked")
	private static boolean doesServletMappingExist(final WebApp webApp,
			final Servlet servlet, final String pattern) {

		List<ServletMapping> mappings = webApp.getServletMappings();
		String servletName = servlet.getServletName();
		if (servletName != null) {
			for (int i = mappings.size() - 1; i >= 0; --i) {
				ServletMapping mapping = mappings.get(i);
				if (mapping != null
						&& mapping.getServlet() != null
						&& mapping.getServlet().getServletName() != null
						&& mapping.getServlet().getServletName().trim().equals(
								servletName) && mapping.getUrlPattern() != null
						&& mapping.getUrlPattern().trim().equals(pattern)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes servlet-mappings for servlet using servlet-name.
	 * 
	 * @param webApp
	 * @param servlet
	 */
	@SuppressWarnings("unchecked")
	public static void removeURLMappings(final WebApp webApp,
			final Servlet servlet) {
		List<ServletMapping> mappings = webApp.getServletMappings();
		String servletName = servlet.getServletName();
		if (servletName != null) {
			for (int i = mappings.size() - 1; i >= 0; --i) {
				ServletMapping mapping = mappings.get(i);
				if (mapping != null
						&& mapping.getServlet() != null
						&& mapping.getServlet().getServletName() != null
						&& mapping.getServlet().getServletName().trim().equals(
								servletName)) {
					mappings.remove(mapping);
				}
			}
		}
	}

	/**
	 * @param webAppObj
	 *            as Object
	 * @return true if webApp instanceof org.eclipse.jst.j2ee.web.WebApp and
	 *         versionID == 24
	 */
	public static boolean isWebApp24(final Object webAppObj) {
		if (webAppObj instanceof WebApp
				&& ((WebApp) webAppObj).getVersionID() == 24)
			return true;
		return false;
	}

	/**
	 * @param webAppObj
	 *            as Object
	 * @return true if webApp instanceof org.eclipse.jst.j2ee.web.WebApp and
	 *         versionID == 23
	 */
	public static boolean isWebApp23(final Object webAppObj) {
		if (webAppObj instanceof WebApp
				&& ((WebApp) webAppObj).getVersionID() == 23)
			return true;
		return false;
	}

}
