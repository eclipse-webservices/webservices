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
package org.eclipse.jst.ws.jaxws.testutils.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A simple utility for unzipping ZIP archives
 * 
 * @author Danail Branekov
 */
public class Unzipper {
	public static final Unzipper INSTANCE = new Unzipper();

	// Hide default constructor
	private Unzipper() {

	}

	/**
	 * Copy the content from the <code>sourceStream</code> to the
	 * <code>destinationStream</code>
	 * 
	 * @param sourceStream
	 * @param destStream
	 * @throws IOException
	 */
	public void copyInputStream(final InputStream sourceStream,
			final OutputStream destStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = sourceStream.read(buffer)) >= 0) {
			destStream.write(buffer, 0, len);
		}

		destStream.flush();
	}

	/**
	 * Unzip the zip file specified to the destination directory
	 * 
	 * @param sourceFile
	 *            the source zip file
	 * @param destinationDir
	 *            the destination directory
	 * @param toLowerCase
	 *            if true, all the unzipped entries would be lowercased (for *IX
	 *            compatibility) The background behind this parameter is the
	 *            case inconsistency between ABAP xml test data directory names
	 *            and the real directory names in the ABAP ZIP
	 * @throws IOException
	 * @throws NullPointerException
	 *             when any of the input parameters is null
	 * @throws IllegalArgumentException
	 *             when <code>sourceFile</code> does not exist or points to a
	 *             directory
	 * @throws IllegalArgumentException
	 *             when <code>destinationDir</code> does not exist or is not a
	 *             directory
	 */
	public void unzip(final File sourceFile, final File destinationDir,
			final boolean toLowerCase) throws IOException {
		checkInputParams(sourceFile, destinationDir);
		ZipFile zipFile = null;

		try {
			zipFile = new ZipFile(sourceFile);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			// Create dir structure
			while (entries.hasMoreElements()) {
				final ZipEntry entry = entries.nextElement();

				if (entry.isDirectory()) {
					// This is not robust, just for demonstration purposes.
					(new File(destinationDir, toLowerCase ? entry.getName()
							.toLowerCase() : entry.getName())).mkdirs();
				}
			}

			// Create files
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				final ZipEntry entry = entries.nextElement();

				if (entry.isDirectory()) {
					continue;
				}

				FileOutputStream fos = null;
				BufferedOutputStream bufOutStream = null;

				try {
					fos = new FileOutputStream(new File(destinationDir,
							toLowerCase ? entry.getName().toLowerCase() : entry
									.getName()));
					bufOutStream = new BufferedOutputStream(fos);
					copyInputStream(zipFile.getInputStream(entry), bufOutStream);
					fos.flush();
					bufOutStream.flush();
				} finally {
					if (fos != null) {
						fos.close();
					}
					if (bufOutStream != null) {
						bufOutStream.close();
					}
				}
			}
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	private void checkInputParams(final File zipFile, final File destination) {
		// ContractChecker.nullCheckParam(zipFile, "zipFile");
		// ContractChecker.nullCheckParam(destination, "destination");
		if (!zipFile.exists() || zipFile.isDirectory()) {
			throw new IllegalArgumentException(
					"ZIP file does not exist or is a directory");
		}
		if (!destination.exists() || !destination.isDirectory()) {
			throw new IllegalArgumentException(
					"Destination does not exist or is not a directory");
		}
	}
}