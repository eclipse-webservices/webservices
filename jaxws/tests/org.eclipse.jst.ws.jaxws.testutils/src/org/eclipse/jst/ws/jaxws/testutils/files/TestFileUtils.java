/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class TestFileUtils
{
	// Hide default constructor
	private TestFileUtils()
	{
	};

	/** Get new instance of the FileUtilities object */
	public static TestFileUtils getInstance()
	{
		return new TestFileUtils();
	}

	/**
	 * Utility method. Copies the contents of the source folder to the destination folder.
	 */
	public boolean copyFiles(final File dest, final File src) throws IOException
	{

		if (src.isDirectory())
		{
			dest.mkdirs();
			final String list[] = src.list();

			for (int i = 0; i < list.length; i++)
			{
				final String dest1 = dest.getAbsolutePath() + "\\" + list[i];
				final String src1 = src.getAbsolutePath() + "\\" + list[i];
				copyFiles(new File(dest1), new File(src1));
			}
		} else
		{
			return copy(dest, src);
		}

		return false;
	}

	/**
	 * Utility method for copying a file! - not a directory
	 * 
	 * @param target
	 * @param source
	 * @throws IOException
	 */
	public boolean copy(final File target, final File source) throws IOException
	{
		if (!source.isFile())
			return false;

		final FileChannel sourceChannel = new FileInputStream(source).getChannel();
		final FileChannel targetChannel = new FileInputStream(target).getChannel();
		sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
		sourceChannel.close();
		targetChannel.close();

		return true;
	}

	/**
	 * 
	 * @param destDir
	 * @param srcRelativePath -
	 *            the relative path of the resource or <code>null</code> if it is in the same package
	 * @param resourceName
	 * @param resourceClass
	 * @return
	 * @throws IOException
	 */
	public File copyResourceToDir(File destDir, InputStream srcStream, String resourceName) throws IOException
	{
		File destFile = new File(destDir.getAbsolutePath(), resourceName);
		copyFile(srcStream, new FileOutputStream(destFile));
		return destFile;
	}
	/**
	 * 
	 * @param destDir
	 * @param srcRelativePath -
	 *            the relative path of the resource or <code>null</code> if it is in the same package
	 * @param srcFileName
	 * @param resourceClass
	 * @return
	 * @throws IOException
	 */
	public File copyResourceToDir(File destDir, String srcRelativePath, String srcFileName, Class<?> resourceClass) throws IOException
	{
		File destFile = new File(destDir.getAbsolutePath(), srcFileName);
		copyFile(createInputStreamForResource(srcRelativePath, srcFileName, resourceClass), new FileOutputStream(destFile));
		return destFile;
	}

	private InputStream createInputStreamForResource(String srcRelativePath, String srcFileName, Class<?> resourceClass)
	{
		return resourceClass.getResourceAsStream(createResourcePath(srcRelativePath) + srcFileName);
	}

	private String createResourcePath(String srcRelativePath)
	{
		return (srcRelativePath == null) ? "" : srcRelativePath + "/";
	}

	/** Closes the streams after completion */
	public void copyFile(InputStream in, OutputStream out) throws IOException
	{
		if (in == null || out == null)
			throw new IllegalArgumentException("Stream not initialized");
		try
		{
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
		} finally
		{
			in.close();
			out.close();
		}
	}

	/**
	 * Utility method for deleting the contents of a given folder path
	 * 
	 * @param folderPath
	 * @return <code>true</code> if the process was completed successfully.
	 * @throws IOException
	 */
	public boolean deleteFolderAndContents(final String folderPath) throws IOException
	{
		final File path = new File(folderPath);
		return deleteFolderAndContents(path);
	}

	/**
	 * Deletes the specified directory and all its contents including all sub directories.
	 * 
	 * @param tempDir
	 *            the directory that is being deleted.
	 */
	public boolean deleteFolderAndContents(File tempDir)
	{
		boolean status = true;
		if (tempDir != null)
		{
			File[] files = tempDir.listFiles();

			if (files != null)
			{
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].isDirectory())
					{
						status = deleteFolderAndContents(files[i]) && status;
					} else
					{
						status = files[i].delete() && status;
					}
				}
			}
			status = tempDir.delete() && status;
		} else
		{
			status = false;
		}
		return status;
	}

	/**
	 * Extracts the contents of the given input stream file to the given directory.
	 * 
	 * @param istr
	 *            The input stream of the jar file
	 * @param srcFileName
	 *            The name of the archive - where the contents of the input stream will be copied
	 * @param destDir
	 *            The destination directory object
	 * @return <code>true</code> if the process was completed successfully
	 * @throws IOException
	 */
	public boolean unzipArchiveToDir(final InputStream istr, final String srcFileName, final File destDir) throws IOException
	{

		if (!destDir.exists())
			throw new IOException("Directory does not exist");
		if (!destDir.canWrite())
			throw new IOException("Could not write to directory");

		final File sourceFile = new File(destDir, srcFileName);

		final FileOutputStream fos = new FileOutputStream(sourceFile);
		final BufferedOutputStream bufOutStream = new BufferedOutputStream(fos);

		try
		{
			Unzipper.INSTANCE.copyInputStream(istr, bufOutStream);
		} finally
		{
			bufOutStream.close();
			fos.close();
		}

		// check if the archive was copied successfully
		if (!sourceFile.exists())
		{
			throw new FileNotFoundException("Archive could not be found");
		}

		Unzipper.INSTANCE.unzip(sourceFile, destDir, false);

		return true;
	}

	/** Utility method for getting the contents of a file through URL */
	public String readURLResource(URL url) throws IOException
	{
		String readInf = null;
		final InputStream in = url.openStream();

		try
		{
			int inRead = in.read();

			readInf = "";
			while (inRead != -1)
			{
				readInf += String.valueOf((char) inRead);
				inRead = in.read();
			}
		} finally
		{
			in.close();
		}

		return readInf;
	}

	/**
	 * Method for replacing the text occurrence in a file with the provided replacement.
	 * 
	 * @param file
	 * @param find
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public boolean replaceContentsInFile(File file, String find, String replacement) throws IOException
	{
		if (!file.exists())
			return false;

		String fileContents = readFileContents(file);

		fileContents = fileContents.replaceAll(find, replacement);

		file.delete();
		file.createNewFile();

		writeContentsToFile(file, fileContents);

		return true;
	}

	/**
	 * Writes the string to the given file.
	 * 
	 * @param file -
	 *            the file to write to
	 * @param fileContents -
	 *            the contents to be written to the file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeContentsToFile(File file, String fileContents) throws IOException, FileNotFoundException
	{

		PrintWriter pout = new PrintWriter(file);

		try
		{
			pout.println(fileContents);
			pout.flush();
		} finally
		{
			pout.close();
		}
	}

	/**
	 * Reads the contents of a file and returns it as String.
	 * 
	 * @param file -
	 *            the file to read from
	 * @return the contents of the file
	 * @throws IOException
	 */
	public String readFileContents(File file) throws IOException
	{
		String readInf = null;
		final InputStream in = new FileInputStream(file);

		try
		{
			int inRead = in.read();

			readInf = "";
			while (inRead != -1)
			{
				readInf += String.valueOf((char) inRead);
				inRead = in.read();
			}
		} catch (IOException e)
		{
			throw e;
		} finally
		{
			in.close();
		}

		return readInf;
	}

	/** @return the resource input stream of the projects archive */
	@SuppressWarnings("unchecked")
	public InputStream getResourceInputStream(final String archiveName, final String projectSourceFolder, Class relativeResourceBaseClass)
	{
		return relativeResourceBaseClass.getResourceAsStream(projectSourceFolder + "/" + archiveName);
	}

	/** Method for handling the unzipping of the test projects */
	public void unzipTestProjects(final InputStream archiveStream, final String archiveName, final File destDir) throws IOException
	{

		if (archiveStream == null)
			throw new IOException("The archive resource stream is not initialized! archive: " + archiveName + " - dir: " + destDir);

		try
		{
			unzipArchiveToDir(archiveStream, archiveName, destDir);
		} finally
		{
			archiveStream.close();
		}
	}

	/**
	 * Unzips the content of <tt>zipFileLocation</tt> into <tt>outFolder</tt>.
	 * 
	 * @param zipFileLocation
	 * @param outFolder
	 * @throws IOException
	 */
	public static void addZippedContent(File zipFileLocation, File outFolder) throws IOException
	{
		FileInputStream fis = new FileInputStream(zipFileLocation);
		addZippedContent(fis, outFolder);
	}

	/**
	 * Unzips the content of <tt>is</tt> into <tt>outFolder</tt>.
	 * 
	 * @param zipFileLocation
	 * @param outFolder
	 * @throws IOException
	 */
	public static void addZippedContent(InputStream is, File outFolder) throws IOException
	{
		ZipInputStream zis = new ZipInputStream(is);
		ZipEntry entry = null;
		while ((entry = zis.getNextEntry()) != null)
		{
			File outFile = new File(outFolder, entry.getName());

			if (entry.isDirectory())
			{
				outFile.mkdirs();
			} else
			{
				OutputStream out = new FileOutputStream(outFile);
				byte[] buff = new byte[2048];
				int read;
				while ((read = zis.read(buff)) > 0)
				{
					out.write(buff, 0, read);
				}
				out.close();
			}

			zis.closeEntry();
		}

		zis.close();
	}

	/**
	 * Deletes directory with its content
	 * 
	 * @param dir
	 */
	public static void deleteDirectory(File dir)
	{
		if (!dir.isDirectory())
		{
			throw new IllegalArgumentException("Not a directory" + dir.getName());
		}

		for (String s : dir.list())
		{
			File f = new File(dir.getAbsolutePath() + File.separator + s);
			if (f.isDirectory())
			{
				deleteDirectory(f);
			} else
			{
				f.delete();
			}
		}

		dir.delete();
	}

	/**
	 * Create a directory named via the pattern System.getProperty("java.io.tmpdir") + File.separator + dirName
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createTempDirectory(String dirName)
	{
		File tempDir = new File(System.getProperty("java.io.tmpdir"), dirName);
		if (tempDir.exists())
		{
			deleteDirectory(tempDir);
		}

		if (!tempDir.mkdir())
		{
			throw new IllegalStateException("Temp direcory " + tempDir.getAbsolutePath() + " could not be created");
		}
		// tempDir.deleteOnExit();

		return tempDir;
	}

	/**
	 * Copy the content of <code>filePath</code> and copies it to temporary file in system temp directory.
	 * 
	 * @param classContext
	 * @param filePath
	 * @return created temporary file
	 * @throws IOException
	 */
	public static File copyToTempLocation(Class<?> classContext, String filePath) throws IOException
	{
		final InputStream is = classContext.getResourceAsStream(filePath);
		final File dir = new File(System.getProperty("java.io.tmpdir"));
		final File tempFile = new File(dir, System.currentTimeMillis() + ".java");
		tempFile.deleteOnExit();

		final FileOutputStream fos = new FileOutputStream(tempFile);
		try
		{
			byte[] buff = new byte[1024];
			for (int cnt = 0; (cnt = is.read(buff)) > -1;)
			{
				fos.write(buff, 0, cnt);
			}
		} finally
		{
			is.close();
			fos.close();
		}

		return tempFile;
	}

	public static void setFileContent(File file, String content) throws IOException
	{
		final FileWriter fw = new FileWriter(file);
		try
		{
			fw.write(content);
		} finally
		{
			fw.close();
		}
	}
}
