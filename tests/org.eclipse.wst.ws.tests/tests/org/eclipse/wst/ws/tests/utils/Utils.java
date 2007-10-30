package org.eclipse.wst.ws.tests.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.Vector;

public class Utils 
{
  public static void createOutputFile( Collection keys, File propsFile, PrintWriter outputFile, PrintWriter deleteFile )
  {
    String[] lines        = readLines( propsFile );
    boolean  continueLine = false;
    boolean  deleteKey    = false;
     
    for( int index = 0; index < lines.length; index++ )
    {
      String   line = lines[index].trim();
      
      if( continueLine )
      {
        continueLine = line.endsWith( "\\" );
        
        if( !deleteKey )
        {
          outputFile.println( line );
        }
        else
        {
          deleteFile.println( line );
        }
        
        continue;
      }
      
      continueLine = line.endsWith( "\\" );
      
      boolean  validKey     = isLineKey( keys, line );
      boolean  hasKey       = line.indexOf( '=' ) != -1;
      
      if( validKey )
      {
        outputFile.println( line );
        deleteKey = false;
      }
      else if( hasKey )
      {
        deleteKey = true;
        deleteFile.println( line );
      }
      else
      {
        outputFile.println( line );
      }
    }
  }
  
  public static boolean isLineKey( Collection keys, String line )
  {
    boolean  result = false;
    Iterator iter   = keys.iterator();
    int      equalIndex = line.indexOf( '=' );
    String   key        = null;
    
    if( equalIndex != -1 && !line.startsWith( "#" ))
    {
      key = line.substring( 0, equalIndex );
    }
    
    while( iter.hasNext() && key != null )
    {
      String collectionKey = (String)iter.next();
      if( key.equals( collectionKey ) || collectionKey.equals( '%' + key ) )
      {
        result = true;
        break;
      }
    }
    
    return result;
  }
  
  public static void getPluginXMLKeys( File file, HashSet keys )
  {
    BufferedReader reader = null;
    
    try
    {
      reader = new BufferedReader( new FileReader( file ) );
      
      String line = reader.readLine();
      
      while( line != null )
      {
        Vector linekeys = findKeys( line );
        keys.addAll( linekeys );
        line = reader.readLine();
      }
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
    finally
    {
      try
      {
        if( reader != null ) reader.close();
      }
      catch( Exception exc )
      {
        exc.printStackTrace();
      }
    }
  }
  
  public static File getFile( File base, final String child )
  {
    File[] file = base.listFiles( new FilenameFilter()
                                  {
                                    public boolean accept( File file, String name )
                                    {
                                      int index = name.indexOf( '_' );
                                      
                                      if( index != -1 )
                                      {
                                        name = name.substring( 0, index );  
                                      }
                                      
                                      return name.endsWith( child );
                                    }
                                  } );
    return file.length == 0 ? null : file[0];
  }
  
  public static Vector findKeys( String line )
  {
    Vector keys       = new Vector();
    int    quotestart = line.indexOf( '"' );
    int    quoteend   = line.indexOf( '"', quotestart + 1 );
    
    while( quotestart != -1 && quoteend != -1 && quotestart < quoteend )
    {
      String key = line.substring( quotestart+1, quoteend );
      
      if( key.startsWith( "%") )
      {
        keys.add( key.substring(1, key.length() ) );
      }
      
      quotestart = line.indexOf( '"', quoteend+1 );
      quoteend   = line.indexOf( '"', quotestart+1 );
    }
    
    return keys;
  }
  
  public static void findJavaStrings( String line, Vector keys )
  {
    int    quotestart = line.indexOf( '"' );
    int    quoteend   = line.indexOf( '"', quotestart + 1 );
    
    while( quotestart != -1 && quoteend != -1 && quotestart < quoteend )
    {
      // Find last real ending quote.
      while( quotestart != -1 && quoteend != -1 && line.charAt( quoteend-1 ) == '\\' )
      {
        quoteend = line.indexOf( '"', quoteend + 1 );
      }
      
      if( quotestart == -1 || quoteend == -1 || quotestart > quoteend ) break;
      
      String key = line.substring( quotestart+1, quoteend );
      
      keys.add( key );
      
      quotestart = line.indexOf( '"', quoteend+1 );
      quoteend   = line.indexOf( '"', quotestart+1 );
    }
  }
  
  public static String[] readLines( File file )
  {
    Vector files = new Vector();
    BufferedReader reader = null;
    
    try
    {
      reader = new BufferedReader( new FileReader( file ) );
      
      String line = reader.readLine();
      
      while( line != null )
      {
        files.add( line );
        line = reader.readLine();
      }
    }
    catch( Exception exc )
    {
      System.out.println( "Problem opening file:" + file );
      exc.printStackTrace();
    }
    finally
    {
      try
      {
        if( reader != null ) reader.close();
      }
      catch( Exception exc )
      {
        exc.printStackTrace();
      }
    }
    
    return (String[])files.toArray( new String[0] );
    
  }
  
  public static String[] readLines( String file )
  {
    return readLines( new File( file ) );
  }
    
  public static void getFiles( File file, Vector files, String fileSuffix, String[] excludeFiles )
  {
    if( stringInArray( file.getName(), excludeFiles ) )
    {
      // do nothing
    }
    else if( file.isDirectory() )
    {
      File[] dirfiles = file.listFiles();
      
      for( int index = 0; index < dirfiles.length; index++ )
      {
        getFiles( dirfiles[index], files, fileSuffix, excludeFiles );
      }
    }
    else if( file.getName().endsWith( fileSuffix ) )
    {
      files.add( file );                              
    }
  }
  
  public static boolean stringInArray( String name, String[] names )
  {
    boolean found = false;
    
    for( int index = 0; index < names.length; index++ )
    {
      if( name.equals( names[index] ))
      {
        found = true;
        break;
      }
    }
    
    return found;
  }
  public static Vector getPropertyKeys( File file )
  {
    Vector      keys = new Vector();
    InputStream stream = null;
    
    try
    {
      stream = new FileInputStream( file );
      
      PropertyResourceBundle bundle  = new PropertyResourceBundle( stream );
      Enumeration            keyEnum = bundle.getKeys();
      
      while( keyEnum.hasMoreElements() )
      {
        keys.add( keyEnum.nextElement() );
      }
    }
    catch( Exception exc )
    {
      exc.printStackTrace();
    }
    finally
    {
      try
      {
        if( stream != null ) stream.close();
      }
      catch( Exception exc )
      {
        exc.printStackTrace();
      }
    }
    
    return keys;
  }
}
