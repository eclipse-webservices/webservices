/*******************************************************************************
 * Copyright (c) 2007, 2019 IBM Corporation and others.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;

public class GenPluginPropertiesFromPluginXml 
{
  public static void main( String[] args )
  {
    File     startPath = new File( args[0] );
    String[] pluginFiles = Utils.readLines( args[1] );
    
    for( int index = 0; index < pluginFiles.length; index++ )
    {
      String filename = pluginFiles[index];
      
      HashSet keys = new HashSet();
      File    dir  = Utils.getFile( startPath, filename );
      File    file = Utils.getFile( dir, "plugin.xml" );

      if( file == null ) continue;
      
      Utils.getPluginXMLKeys( file, keys );

      File propfile = Utils.getFile( dir, "plugin.properties" );
      
      if( keys.size() > 0 && propfile == null )
      {
        try
        {
          System.out.println( "No plugin.properties file for:" + file );
          propfile = File.createTempFile( "prefix", "suffix" );
        }
        catch( Exception exc )
        {
          exc.printStackTrace();
        }
      }
      else if( keys.size() == 0 )
      {
        continue;
      }
            
      File           outputFile = new File( dir.getAbsolutePath() + File.separator + "plugin.properties.new" );
      File           delFile = new File( dir.getAbsolutePath() + File.separator + "plugin.properties.del" );
      PrintWriter writer     = null;
      PrintWriter delWriter  = null;
      
      try
      {
        writer = new PrintWriter( new FileWriter( outputFile ) );
        delWriter = new PrintWriter( new FileWriter( delFile ));
        System.out.println( "Generating output to:" + outputFile );
        Utils.createOutputFile( keys, propfile, writer, delWriter );
      }
      catch( Exception exc )
      {
        exc.printStackTrace(); 
      }
      finally
      {
        try
        {
          if( writer != null ) writer.close();
          if( delWriter != null ) delWriter.close();
        }
        catch( Exception exc )
        {
          exc.printStackTrace();
        }
      }      
    }
  }  
}
