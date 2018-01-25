package org.eclipse.wst.ws.tests.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

/*
 * This class is used to find Context IDs in source files.
 * args[0] = The root directory where plugins source directories will be searched.
 * args[1] = A file containing plugin source directories.  Each line should contain
 *           a path to a source directory relative to the args[0] parameter.
 * args[2] = The output file name.
 */
public class FindContextIds 
{
  public void findIds( String rootDirectory, String sourceDirectoryName, String outputFileName )
  {
	File        sourceDirectoryFile = new File( sourceDirectoryName );
	String[]    srcDirLines         = Utils.readLines( sourceDirectoryFile );
	PrintWriter outputFile          = null;
		
	try
	{
	  outputFile = new PrintWriter( new FileWriter( outputFileName ) );
	
	  for( int index = 0; index < srcDirLines.length; index++ )
	  {
		String sourceDirName   = srcDirLines[index];
	    File   sourceDirectory = new File( rootDirectory, sourceDirName );
	  
	    outputFile.println( "======>Context IDs for plugin directory: " + sourceDirName );
	    
	    findIdsForaDirectory( sourceDirectory, outputFile );
	  }
	}
	catch( Exception exc )
	{
	  exc.printStackTrace();
	}
	finally
	{
	  if( outputFile != null )
	  {
	    outputFile.close();
	  }
	}
  }
  
  static public void main( String[] args )
  {
    FindContextIds findIds = new FindContextIds();
    
    if( args.length != 3 )
    {
      System.out.println( "Error: expecting a root directory, the source directory file name, and an output file as parameters." );
      return;
    }
    
    findIds.findIds( args[0], args[1], args[2] );
  }
  
  private boolean findIdsForaDirectory( File sourceDirectory, PrintWriter outputFile )
  {
	Vector  files = new Vector();
	boolean found = false;
	
    Utils.getFiles( sourceDirectory, files, "java", new String[0] );
    
    // Loop through each file.
    for( int fileIndex = 0; fileIndex < files.size(); fileIndex++ )
    {
      String[] lines = Utils.readLines( (File)files.elementAt(fileIndex) );	
      
      // Loop through each line in the file.
      for( int lineIndex = 0; lineIndex < lines.length; lineIndex++ )
      {
        String line = lines[lineIndex];
        
        if( line.indexOf("CONTEXT_ID") != -1 )
        {
          outputFile.println( line );
          found = true;
        }
      }
    }
    
    return found;
  }
}
