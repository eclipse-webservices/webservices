/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100507   312131 mahutch@ca.ibm.com - Mark Hutchinson, ws performance test JUnits can have inconsistent results due to background jobs running
 *******************************************************************************/

package org.eclipse.jst.ws.tests.performance.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.jobs.Job;

public final class PerformanceJobUtil {
	
	private static List<Job> getRunningJobs() {
		List<Job> jobs = new ArrayList<Job>();
		jobs.addAll(Arrays.asList(Job.getJobManager().find(null)));			
		return jobs;
	}

	public static void waitForStartupJobs() {				
		if (System.getProperty("ws.perf.startup.nowait") != null ) {
			System.out.println("ws.perf.startup.nowait property was set.  Not waiting for start up jobs");
			return;
		}		
		System.out.println("waiting up jobs to complete.  Set the ws.perf.startup.nowait vm arg to skip this wait");
		try {
			waitForJobs();
			Thread.sleep(2000);//sleep for approx 2 more seconds
		}
		catch (InterruptedException e) {
			return;
		}		
	}

	/* If there are jobs running, then sleep 2s.
	 * Give up waiting if still jobs running after 60 iterations (approx 2 min)
	 */
	private static void waitForJobs() throws InterruptedException {
		List<Job> jobs = getRunningJobs();
		int counter = 0;
		while (!jobs.isEmpty()) {			
			
			String[] jobNames = new String[jobs.size()];
			for (int i = 0; i < jobs.size(); i++) {
				jobNames[i] = jobs.get(i).getName();
			}			
			System.out.println("Waiting for " + jobs.size() + " jobs: " + Arrays.toString(jobNames));
			Thread.sleep(2000);
			jobs = getRunningJobs();
			counter++;
			if (counter == 60) {
				System.out.println("Giving up waiting after 2 minutes.  Still  "  + jobs.size() + " jobs running: " + Arrays.toString(jobNames));
				break;
			}
		}
	}


}
