/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.command.internal.env.common;

import java.io.File;

/**
 * 
 * The class provides and easy way to build classpaths.
 *
 */
public class ClassPath 
{
  private StringBuffer path = new StringBuffer();
  
  /**
   * 
   * @param newPath appends this new path to the end of the classpath.
   */
  public void appendPath( String newPath )
  {
  	// No value to put on path, so just return
  	if( newPath.equals( "" ) ) return;
  	
  	// If this is the first path in the string then we don't need a path
  	// separator.
  	if( path.length() == 0 )
  	{
  	  path.append( newPath );
  	}
  	else
  	{
  	  path.append( File.pathSeparatorChar + newPath );
  	}
  }
  
  /**
   * This method appends all jar and zip files in a particular directory to
   * the classpath.
   * 
   * @param directory specifies a directory contains jar and zip files.
   */
  public void appendDir( String directory )
  {
  	File dir = new File( directory );
  	
  	if( !dir.isDirectory() ) return;
  	
  	File[] files = dir.listFiles();
  	
  	for( int index = 0; index < files.length; index++ )
  	{
  	  File   file = files[index];
  	  String name = file.getName();
  	  
  	  if( file.isFile() &&
  	      ( name.endsWith( ".jar" ) ||
  	        name.endsWith( ".zip" ) ) )
  	  {
  	  	appendPath( file.getAbsolutePath() );
  	  }
  	        
  	}
  }
  
  /**
   * 
   * @param newPath appends a new classpath to the end of this classpath.
   */
  public void appendPath( ClassPath newPath )
  {
  	appendPath( newPath.path.toString() );
  }
  
  /**
   * @return returns the full string classpath with separators.
   */
  public String toString()
  {
  	return path.toString();
  }
}
