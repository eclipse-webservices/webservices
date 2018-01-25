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
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUtils;
import org.eclipse.osgi.util.NLS;

/**
 * To construct implementation library from persistent project properties as
 * saved libraries.
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */

public class JAXRSLibraryConfigProjectData implements
		JAXRSLibraryConfiglModelSource {
	final static String QUALIFIEDNAME = "org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryConfigProjectData";
	/**
	 * Parsing delimiter for elements in a tuple.
	 */
	final protected static String SPTR_TUPLE = ":"; //$NON-NLS-1$
	/**
	 * Parsing delimiter for tuples in a persistent property string.
	 */
	final protected static String EO_TUPLE = ";"; //$NON-NLS-1$

	final private IProject project;
	final private JAXRSLibraryRegistryUtil jaxrsLibReg;
	private JAXRSLibraryInternalReference selJAXRSLibImpl;

	/**
	 * Constructor
	 * 
	 * @param project
	 */
	public JAXRSLibraryConfigProjectData(IProject project) {
		this.project = project;
		this.jaxrsLibReg = JAXRSLibraryRegistryUtil.getInstance();

		/*
		 * logging message when object is instantiated instead of at method call
		 * to reduce log entries.
		 */
		if (!isProjectFirstCreated()) {
			verifySavedLibAvailability();
		}
	}

	/**
	 * Return the previously selected JAXRS implementation library from project
	 * persistent properties. Return null if none exists.
	 * 
	 * @return selJAXRSLibImpl JAXRSLibraryDecorator
	 */
	public JAXRSLibraryInternalReference getJAXRSImplementationLibrary() {
		try {
			if (!isProjectFirstCreated() && selJAXRSLibImpl == null) {
				String strImplLibs = ((IResource) project)
						.getPersistentProperty(new QualifiedName(QUALIFIEDNAME,
								JAXRSUtils.PP_JAXRS_IMPLEMENTATION_LIBRARIES));
				selJAXRSLibImpl = getJAXRSImplLibfromPersistentProperties(getTuples(strImplLibs));
			}
		} catch (CoreException e) {
			JAXRSCorePlugin
					.log(
							e,
							"Exception occured while returning reference to the JAXRS implementation library");
		}
		return selJAXRSLibImpl;
	}

	/**
	 * To save configuration data as a project persistent properties.
	 * 
	 * @param implementation
	 * @param component
	 */
	@SuppressWarnings("unchecked")
	void saveData(final List implementation) {
		try {
			((IResource) project).setPersistentProperty(
					new QualifiedName(QUALIFIEDNAME,
							JAXRSUtils.PP_JAXRS_IMPLEMENTATION_LIBRARIES),
					generatePersistString(implementation));

			/*
			 * Flush the selection so that they can be reconstructed from
			 * persistent properties when getSavedJAXRSImplLib and
			 * getSavedJAXRSCompLibs called next time.
			 */
			selJAXRSLibImpl = null;

		} catch (CoreException e) {
			JAXRSCorePlugin
					.log(e,
							"Exception occured while persisting the JAXRS Library preferences");
		}
	}

	/**
	 * Check if a project is just created by inspecting persistent properties if
	 * there is any. ?
	 */
	private boolean isProjectFirstCreated() {
		boolean isNew = false;
		try {
			((IResource) project)
					.getPersistentProperty(new QualifiedName(QUALIFIEDNAME,
							JAXRSUtils.PP_JAXRS_IMPLEMENTATION_LIBRARIES));
		} catch (CoreException e) {
			isNew = true;
		}
		return isNew;
	}

	private void verifySavedLibAvailability() {
		try {
			String strImplLibs = ((IResource) project)
					.getPersistentProperty(new QualifiedName(QUALIFIEDNAME,
							JAXRSUtils.PP_JAXRS_IMPLEMENTATION_LIBRARIES));

			logMissingLib(getTuples(strImplLibs), true);

		} catch (CoreException e) {
			JAXRSCorePlugin
					.log(e,
							"Exception occured while verifying saved JAXRS Library preferences");
		}
	}

	private void logMissingLib(final List<Tuple> jaxrsLibTuples,
			final boolean isVerifyImpl) {
		if (jaxrsLibReg != null) {
			Iterator<Tuple> itTuple = jaxrsLibTuples.iterator();
			while (itTuple.hasNext()) {
				Tuple tuple = itTuple.next();
				JAXRSLibraryInternalReference jaxrsLib = jaxrsLibReg
						.getJAXRSLibraryReferencebyID(tuple.getID());
				/*
				 * Information logged when saved JAXRS lib is removed from
				 * registry. One log entry is created for each missing library.
				 */
				if (jaxrsLib == null) {
					String prjName = project.getName();
					String msg = (isVerifyImpl) ? Messages.JAXRSLibraryConfigPersistData_SAVED_IMPLLIB_NOT_FOUND
							: Messages.JAXRSLibraryConfigPersistData_SAVED_COMPLIB_NOT_FOUND;
					JAXRSCorePlugin.log(IStatus.INFO, NLS.bind(msg, prjName));
				}
			}
		}
	}

	private JAXRSLibraryInternalReference getJAXRSImplLibfromPersistentProperties(
			final List<Tuple> jaxrsLibTuples) {
		if (jaxrsLibReg != null) {
			Tuple tuple = null;
			JAXRSLibraryInternalReference lib = null;
			Iterator<Tuple> itTuple = jaxrsLibTuples.iterator();
			while (itTuple.hasNext()) {
				tuple = itTuple.next();
				lib = jaxrsLibReg.getJAXRSLibraryReferencebyID(tuple.id);
				if (lib != null) {
					return new JAXRSLibraryInternalReference(lib.getLibrary(),
							tuple.selected, tuple.deploy, tuple.sharedLib);
				} /*
				 * else { // already logged a message for a missing library }
				 */
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String generatePersistString(List list) {
		JAXRSLibraryInternalReference jaxrsLibItem;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			jaxrsLibItem = (JAXRSLibraryInternalReference) list.get(i);
			sb = sb.append(jaxrsLibItem.generatePersistString());
			sb.append(JAXRSLibraryConfigProjectData.EO_TUPLE);
		}
		return sb.toString();
	}

	private List<Tuple> getTuples(String strJAXRSLibs) {
		List<Tuple> list = new ArrayList<Tuple>();

		if (strJAXRSLibs != null) {
			String patternStr = JAXRSLibraryConfigProjectData.EO_TUPLE;
			String[] fields = strJAXRSLibs.split(patternStr);
			if (strJAXRSLibs.length() > 0) {
				Tuple tuple;
				for (int i = 0; i < fields.length; i++) {
					tuple = new Tuple(fields[i]);
					list.add(tuple);
				}
			}
		}
		return list;
	}

	/**
	 * Inner class for parsing project persistent properties.
	 * 
	 * To Do: Take out selected attribute since it is not needed. Add the
	 * library name as an attribute. Provide code path to migrate earlier
	 * project.
	 * 
	 * NOTE: this class should no longer be used except to support legacy
 	 * library registries
	 */
	static class Tuple {
		final private String id;
		final private boolean selected;
		final private boolean deploy;
		final private boolean sharedLib;

		Tuple(String id, boolean selected, boolean deploy, boolean sharedLib) {
			this.id = id;
			this.selected = selected;
			this.deploy = deploy;
			this.sharedLib = sharedLib;
		}

		// parse tuple = ID:selected:deploy
		Tuple(String tuple) {
			String[] fields = tuple
					.split(JAXRSLibraryConfigProjectData.SPTR_TUPLE);

			if (fields.length >= 4) {
				this.id = fields[0];
				this.selected = Boolean.valueOf(fields[1]).booleanValue();
				this.deploy = Boolean.valueOf(fields[2]).booleanValue();
				this.sharedLib = Boolean.valueOf(fields[3]).booleanValue();
			} else {
				throw new IllegalStateException("Library registry is corrupt");
			}
		}

		String getID() {
			return id;
		}

		boolean isSelected() {
			return selected;
		}

		boolean needDeploy() {
			return deploy;
		}
		boolean needSharedLib() {
			return sharedLib;
		}
	}

}
