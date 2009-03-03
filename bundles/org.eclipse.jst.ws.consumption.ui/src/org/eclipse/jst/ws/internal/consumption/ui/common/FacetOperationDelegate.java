/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20090303   242635 mahutch@ca.ibm.com - Mark Hutchinson, Remove unnecessary UI dependencies from org.eclipse.jst.ws.consumption
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.jst.ws.internal.consumption.common.IFacetOperationDelegate;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.osgi.framework.Bundle;

public class FacetOperationDelegate implements IFacetOperationDelegate {

	public IStatus addFacetsToProject(final IFacetedProject fproject, final Set projectFacetVersions) {
		final IStatus[] status = new IStatus[1];
		status[0] = Status.OK_STATUS;
		final Set actions = FacetUtils.getInstallActions(projectFacetVersions);

		// Create a runnable that applies the install actions to the faceted project
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException {
				try {
					fproject.modify(actions, shellMonitor);
				} catch (CoreException e) {
					status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, e);
				}
			}
		};

		// Run the runnable in another thread unless there is no UI thread (Ant scenarios)
		if (displayPresent()) {
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
			} catch (InvocationTargetException ite) {
				status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, ite);
			} catch (InterruptedException ie) {
				status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, ie);
			}
		} else {
			try {
				fproject.modify(actions, null);
			} catch (CoreException e) {
				status[0] = getErrorStatusForAddingFacets(fproject.getProject().getName(), projectFacetVersions, e);
			}
		}

		return status[0];
	}

	public IStatus createNewFacetedProject(final String projectName) {
		final IStatus[] status = new IStatus[1];
		status[0] = Status.OK_STATUS;
		IProject project = ProjectUtilities.getProject(projectName);
		if (!project.exists()) {
			// Create a runnable that creates a new faceted project.
			IRunnableWithProgress runnable = new IRunnableWithProgress() {
				public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException {
					try {
						IFacetedProject fProject = ProjectFacetsManager.create(projectName, null, shellMonitor);
						if (fProject == null) {
							status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
						}
					} catch (CoreException e) {
						status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), e);
					}
				}
			};

			// Run the runnable in another thread unless there is no UI thread (Ant scenarios)
			try {
				if (displayPresent()) {
					PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
				} else {
					try {
						IFacetedProject fProject = ProjectFacetsManager.create(projectName, null, null);
						if (fProject == null) {
							status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }));
						}
					} catch (CoreException e) {
						status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), e);
					}
				}
			} catch (InvocationTargetException ite) {
				status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ite);
			} catch (InterruptedException ie) {
				status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_PROJECT_CREATION, new String[] { projectName }), ie);
			}
		}

		return status[0];
	}

	public IStatus setFacetRuntimeOnProject(final IFacetedProject fProject, final IRuntime fRuntime) {
		final IStatus[] status = new IStatus[1];
		status[0] = Status.OK_STATUS;

		// Create a runnable that sets the facet runtime on the faceted project
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException {
				try {
					fProject.setTargetedRuntimes(Collections.singleton(fRuntime), shellMonitor);
				} catch (CoreException e) {
					status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), e);
				}
			}
		};

		// Run the runnable in another thread unless there is no UI thread (Ant scenarios)
		if (displayPresent()) {
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
			} catch (InvocationTargetException ite) {
				status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), ite);
			} catch (InterruptedException ie) {
				status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), ie);
			}
		} else {
			try {
				fProject.setTargetedRuntimes(Collections.singleton(fRuntime), null);

			} catch (CoreException e) {
				status[0] = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_SETTING_RUNTIME, new String[] { fProject.getProject().getName(), fRuntime.getName() }), e);
			}
		}

		return status[0];
	}

	public IStatus setFixedFacetsOnProject(final IFacetedProject fProject, final Set fixedFacets) {
		final IStatus[] status = new IStatus[1];
		status[0] = Status.OK_STATUS;

		// Create a runnable that sets the fixed facets on the faceted project
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException {
				try {
					fProject.setFixedProjectFacets(fixedFacets);
				} catch (CoreException e) {
					status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, e);
				}
			}
		};

		// Run the runnable in another thread unless there is no UI thread (Ant scenarios)
		if (displayPresent()) {
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, false, runnable);
			} catch (InvocationTargetException ite) {
				status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, ite);
			} catch (InterruptedException ie) {
				status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, ie);
			}
		} else {
			try {
				fProject.setFixedProjectFacets(fixedFacets);
			} catch (CoreException e) {
				status[0] = getErrorStatusForSettingFixedFacets(fProject.getProject().getName(), fixedFacets, e);
			}
		}

		return status[0];
	}

	// Check to see if SWT is active and the Display is present or not
	private static boolean displayPresent() {
		Bundle b = Platform.getBundle("org.eclipse.swt");
		if (b == null) {
			return false;
		}
		if ((b.getState() != Bundle.RESOLVED && b.getState() != Bundle.ACTIVE)) {
			return false;
		}
		try {
			if (Display.getCurrent() == null) {
				return false;
			} else {
				return true;
			}
		} catch (NoClassDefFoundError e1) {
			return false;
		} catch (Exception e) { 
			// if the Display class cannot be loaded for whatever reason
			return false;

		}
	}

	// the following private methods had to be copied from FacetUtils
	/**
	 * Returns a translatable delimited list of facet labels derived from the
	 * provided set of facets
	 * 
	 * @param facets
	 *            a set containing elements of type {@link IProjectFacet}
	 * @return String a delimited list of facet labels
	 */
	private static String getFacetListMessageString(Set facets) {
		String facetListMessage = "";
		int size = facets.size();
		if (size > 0) {
			Iterator itr = facets.iterator();
			IProjectFacet firstProjectFacet = (IProjectFacet) itr.next();
			facetListMessage = firstProjectFacet.getLabel();

			// Continue appending to facetListMessage until all the facet labels
			// are in the list.
			while (itr.hasNext()) {
				IProjectFacet projectFacet = (IProjectFacet) itr.next();
				String pfLabel = projectFacet.getLabel();
				facetListMessage = NLS.bind(ConsumptionMessages.MSG_FACETS, new String[] { facetListMessage, pfLabel });
			}
		}

		return facetListMessage;
	}

	/**
	 * Returns an error status indicating that the facets could not be set as
	 * fixed facets on the faceted project
	 * 
	 * @param projectName
	 *            a project name to insert in the error message in the IStatus
	 * @param facets
	 *            a set containing elements of type {@link IProjectFacet}. The
	 *            facets in this set will be listed in the error message in the
	 *            IStatus.
	 * @param t
	 *            a Throwable which will be inserted in the IStatus
	 * @return an IStatus with severity IStatus.ERROR
	 */
	private static IStatus getErrorStatusForSettingFixedFacets(String projectName, Set facets, Throwable t) {
		IStatus status = Status.OK_STATUS;
		int size = facets.size();
		if (size > 0) {
			String facetList = getFacetListMessageString(facets);
			status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_FIXED_FACETS, new String[] { projectName, facetList }), t);
		}

		return status;
	}

	/**
	 * Returns an error status indicating that the facet versions could not be
	 * added to the faceted project
	 * 
	 * @param projectName
	 *            a project name to insert in the error message in the IStatus
	 * @param projectFacetVersions
	 *            a set containing elements of type {@link IProjectFacetVersion}
	 *            . The facets in this set will be listed in the error message
	 *            in the IStatus.
	 * @param t
	 *            a Throwable which will be inserted in the IStatus
	 * @return an IStatus with severity IStatus.ERROR
	 */
	private static IStatus getErrorStatusForAddingFacets(String projectName, Set projectFacetVersions, Throwable t) {
		IStatus status = Status.OK_STATUS;
		int size = projectFacetVersions.size();
		if (size > 0) {
			Set facets = new HashSet();
			// Iterate over projectFacetVersions to form a set of IProjectFacets
			Iterator itr = projectFacetVersions.iterator();
			while (itr.hasNext()) {
				IProjectFacetVersion projectFacet = (IProjectFacetVersion) itr.next();
				IProjectFacet facet = projectFacet.getProjectFacet();
				facets.add(facet);
			}
			String facetList = getFacetListMessageString(facets);
			status = StatusUtils.errorStatus(NLS.bind(ConsumptionMessages.MSG_ERROR_ADDING_FACETS_TO_PROJECT, new String[] { projectName, facetList }), t);
		}

		return status;
	}

}
