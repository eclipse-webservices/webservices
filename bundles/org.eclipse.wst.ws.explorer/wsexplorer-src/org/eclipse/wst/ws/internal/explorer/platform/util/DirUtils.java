package org.eclipse.wst.ws.internal.explorer.platform.util;

import java.util.Locale;
import org.eclipse.core.runtime.Platform;

public final class DirUtils
{
  public static boolean isRTL()
  {
    return "rtl".equals(getDir());
  }

  public static String getDir()
  {
    String rtl = "rtl";
    String ltr = "ltr";

    // from system property
    String orientation = System.getProperty("eclipse.orientation"); //$NON-NLS-1$
    if (rtl.equals(orientation)) //$NON-NLS-1$
      return rtl;
    else if (ltr.equals(orientation)) //$NON-NLS-1$
      return ltr;

    // from command line
    String[] args = Platform.getCommandLineArgs();
    for (int i = 0; i < args.length; i++)
    {
      if ("-dir".equalsIgnoreCase(args[i])) //$NON-NLS-1$
      {
        if ((i + 1) < args.length && "rtl".equalsIgnoreCase(args[i + 1])) //$NON-NLS-1$
        {
          return rtl;
        }
        return ltr;
      }
    }

    // Check if the user property is set. If not do not
    // rely on the vm.
    if (System.getProperty("osgi.nl.user") == null) //$NON-NLS-1$
      return ltr;

    // guess from default locale
    String locale = Platform.getNL();
    if (locale == null)
    {
      locale = Locale.getDefault().toString();
    }
    if (locale.startsWith("ar") || locale.startsWith("fa") //$NON-NLS-1$//$NON-NLS-2$
        || locale.startsWith("he") || locale.startsWith("iw") //$NON-NLS-1$//$NON-NLS-2$
        || locale.startsWith("ur")) //$NON-NLS-1$
    {
      return rtl;
    }
    return ltr;
  }
}
