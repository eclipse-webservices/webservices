/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100310   304405 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Facet : support JAX-RS 1.1
 * 20100319   306594 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet install fails for Web 2.3 & 2.4
 * 20100325   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.javaee.core.JavaeeFactory;
import org.eclipse.jst.javaee.core.UrlPatternType;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;
import org.eclipse.jst.javaee.web.WebFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * Utility file for Web 2.5 or 3.0 model
 */
public class JAXRSJEEUtils extends JAXRSUtils {

	/**
	 * @param webApp
	 * @return Servlet - the JAXRS Servlet for the specified WebApp or null if
	 *         not present
	 */
	@SuppressWarnings("unchecked")
	public static Servlet findJAXRSServlet(final WebApp webApp) {

		Iterator<Servlet> it = webApp.getServlets().iterator();

		while (it.hasNext()) {
			Servlet servlet = it.next();
			if (servlet.getServletClass() != null
					&& (servlet.getServletClass().trim().equals(
							getSavedServletClassName()) || servlet
							.getServletClass().trim().equals(
									JAXRS_SERVLET_CLASS))) {
				return servlet;
			}
		}

		// if we get to here then we have finished the loop
		// without finding the servlet we're looking for
		return null;
	}

	private static String getSavedServletClassName() {
		IDialogSettings jaxrsUISettings = JAXRSCorePlugin.getDefault()
				.getJaxrsUISettings();
		if (jaxrsUISettings != null) {
			String JAXRSUISettingsRoot = "org.eclipse.jst.ws.jaxrs.ui" + ".jaxrsFacetInstall"; //$NON-NLS-1$
			IDialogSettings root = jaxrsUISettings
					.getSection(JAXRSUISettingsRoot);

			if (root != null)
				return root.get("servletClassname");
		}
		return null;
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
	@SuppressWarnings("unchecked")
	public static Servlet createOrUpdateServletRef(final WebApp webApp,
			final IDataModel config, org.eclipse.jst.javaee.web.Servlet servlet) {

		String displayName = getDisplayName(config);
		String className = getServletClassname(config);

		return createOrUpdateServletRef(webApp, displayName, className, servlet);
	}
	
	@SuppressWarnings("unchecked")
	public static Servlet createOrUpdateServletRef(final WebApp webApp,
			String displayName, String className, org.eclipse.jst.javaee.web.Servlet servlet) {

		if (servlet == null) {
			// Create the servlet instance and set up the parameters from data
			// model
			servlet = WebFactory.eINSTANCE.createServlet();
			servlet.setServletName(displayName);
			servlet.setServletClass(className);
			servlet.setLoadOnStartup(Integer.valueOf(1));
			// Add the servlet to the web application model
			webApp.getServlets().add(servlet);

		} else {
			updateServletMappings(webApp, servlet, displayName);
			servlet.setServletName(displayName);
			servlet.setServletClass(className);
			servlet.setLoadOnStartup(Integer.valueOf(1));
		}
		return servlet;
	}

	private static void updateServletMappings(final WebApp webApp,
			final Servlet servlet, final String displayName) {
		// update mappings for new name
		ServletMapping mapping = findServletMapping(webApp, servlet);
		if (mapping != null) {
			mapping.setServletName(displayName);
		}

	}

	/**
	 * @param webAppObj
	 *            as Object
	 * @return true if webApp instanceof org.eclipse.jst.javaee.web.WebApp and
	 *         WebAppVersionType._25
	 */
	public static boolean isWebApp25(final Object webAppObj) {
		if (webAppObj instanceof WebApp
				&& ((WebApp) webAppObj).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}

	/**
	 * @param webAppObj
	 *            as Object
	 * @return true if webApp instanceof org.eclipse.jst.javaee.web.WebApp and
	 *         WebAppVersionType._25 or WebAppVersionType._30
	 */
	public static boolean isWebApp25or30(final Object webAppObj) {
		if (webAppObj instanceof WebApp
				&& (((WebApp) webAppObj).getVersion() == WebAppVersionType._25_LITERAL || ((WebApp) webAppObj).getVersion() == WebAppVersionType._30_LITERAL))
			return true;
		return false;
	}
	
	/**
	 * Creates servlet-mappings for the servlet for 2.5 WebModules or greater
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

		if (urlMappingList.size() > 0) {
			ServletMapping mapping = findServletMapping(webApp, servlet);
			if (mapping == null) {
				mapping = WebFactory.eINSTANCE.createServletMapping();
				mapping.setServletName(servlet.getServletName());
				webApp.getServletMappings().add(mapping);
			}
			// Add patterns
			Iterator it = urlMappingList.iterator();
			while (it.hasNext()) {
				String pattern = (String) it.next();
				if (!(doesServletMappingPatternExist(webApp, mapping, pattern))) {
					UrlPatternType urlPattern = JavaeeFactory.eINSTANCE
							.createUrlPatternType();
					urlPattern.setValue(pattern);
					mapping.getUrlPatterns().add(urlPattern);
				}
				else
				{
					mapping.getUrlPatterns().remove(pattern);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updateURLMappings(final WebApp webApp,
			final List urlMappingList, final Servlet servlet) {

		if (urlMappingList != null) {
			ServletMapping mapping = findServletMapping(webApp, servlet);
			if (mapping == null) {
				mapping = WebFactory.eINSTANCE.createServletMapping();
				mapping.setServletName(servlet.getServletName());
				webApp.getServletMappings().add(mapping);
			}
			// Add patterns
			Iterator it = urlMappingList.iterator();
			while (it.hasNext()) {
				String pattern = (String) it.next();
				if (!(doesServletMappingPatternExist(webApp, mapping, pattern))) {
					UrlPatternType urlPattern = JavaeeFactory.eINSTANCE
							.createUrlPatternType();
					urlPattern.setValue(pattern);
					mapping.getUrlPatterns().add(urlPattern);
				}
			}
			// Now gather up the patterns that aren't defined in the UI...
			List<UrlPatternType> patternsToRemove = new ArrayList<UrlPatternType>();
			for (Iterator<UrlPatternType> iter = mapping.getUrlPatterns().iterator(); iter.hasNext();)
			{
			   UrlPatternType aPatternType = iter.next();
			   String patternTypeValue = aPatternType.getValue();
			   if (patternTypeValue != null && !urlMappingList.contains(patternTypeValue))
			   {
			     patternsToRemove.add(aPatternType);
			   }
		    }
			//...and remove them from the model
			for (Iterator<UrlPatternType> iter = patternsToRemove.iterator(); iter.hasNext(); )
			{
			   UrlPatternType aPatternType = iter.next();
			   mapping.getUrlPatterns().remove(aPatternType);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static ServletMapping findServletMapping(final WebApp webApp,
			final Servlet servlet) {
		for (Iterator<ServletMapping> it = webApp.getServletMappings()
				.iterator(); it.hasNext();) {
			ServletMapping mapping = it.next();
			if (mapping.getServletName() != null
					&& servlet.getServletName() != null
					&& mapping.getServletName().trim().equals(
							servlet.getServletName().trim()))
				return mapping;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static boolean doesServletMappingPatternExist(final WebApp webApp,
			final ServletMapping mapping, final String pattern) {
		for (Iterator<UrlPatternType> it = mapping.getUrlPatterns().iterator(); it
				.hasNext();) {
			String patternTypeValue = it.next().getValue();
			if (patternTypeValue != null
					&& pattern.equals(patternTypeValue.trim()))
				return true;
		}
		return false;
	}

	/**
	 * Removes servlet-mappings for servlet using servlet-name for >= 2.5
	 * WebModules.
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
			servletName = servletName.trim();
			for (int i = mappings.size() - 1; i >= 0; --i) {
				ServletMapping mapping = mappings.get(i);
				if (mapping != null && mapping.getServletName() != null
						&& mapping.getServletName().trim().equals(servletName)) {
					mappings.remove(mapping);
				}
			}
		}
	}
}
