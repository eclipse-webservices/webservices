package org.eclipse.wst.ws.service.policy.test.internal.properties;


import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class FOOFilePropertyTester extends PropertyTester
{
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue)
  {
    boolean result = false;
    
    if( receiver instanceof IContainer)
    {
      result = hasFOOfile( (IContainer) receiver );
    }
    
    return result;
  }
  
  private boolean hasFOOfile( IContainer container )
  {
    boolean result   = false;
    
    try
    {
      IResource[] children = container.members();
      
      for( IResource child : children )
      {
        if( child instanceof IContainer )
        {
          result = hasFOOfile( (IContainer) child );
          
        }
        else if( child instanceof IFile )
        {
          result = child.getFileExtension().equalsIgnoreCase( "foo" );
        }
        
        // If we found a single foo file we will break out of the loop.
        if( result ) break;
      }    
    }
    catch( CoreException exc )
    {
      // Ignore exception and return false.  
    }
    
    return result;
  }
}
