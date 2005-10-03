/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.ws.internal.explorer.platform.wsdl.transport;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends FilterInputStream
{
  protected long chunkSize = 0l;
  protected volatile boolean closed = false;
  private static final int maxCharLong = (Long.toHexString(Long.MAX_VALUE)).toString().length();

  private ChunkedInputStream()
  {
    super(null);
  }

  public ChunkedInputStream(InputStream is)
  {
    super(is);
  }

  public int read() throws IOException
  {
    byte[] d = new byte[1];
    int rc = read(d, 0, 1);
    return rc > 0 ? (d[0] & 0xFF) : rc;
  }

  public int read(byte[] b) throws IOException
  {
    return read(b, 0, b.length);
  }

  public synchronized int read(byte[] b, int off, int len) throws IOException
  {
    if (closed)
      return -1;

    int totalread = 0;
    int bytesread = 0;
    try
    {
      do
      {
        if (chunkSize < 1L)
        {
          if (0l == getChunked())
          {
            if (totalread == 0)
              return -1;
            else
              return totalread;
          }
        }
        bytesread = in.read(b, off + totalread, Math.min(len - totalread, (int) Math.min(chunkSize, Integer.MAX_VALUE)));
        if (bytesread > 0)
        {
          totalread += bytesread;
          chunkSize -= bytesread;
        }
      }
      while (len - totalread > 0 && bytesread > -1);
    }
    catch (IOException e)
    {
      closed = true;
      throw e;
    }
    return totalread;
  }

  public long skip(final long n) throws IOException
  {
    if (closed)
      return 0;
    long skipped = 0l;
    byte[] b = new byte[1024];
    int bread = -1;
    do
    {
      bread = read(b, 0, b.length);
      if (bread > 0)
        skipped += bread;
    }
    while (bread != -1 && skipped < n);
    return skipped;
  }

  public int available() throws IOException
  {
    if (closed)
      return 0;
    int rc = (int) Math.min(chunkSize, Integer.MAX_VALUE);
    return Math.min(rc, in.available());
  }

  protected long getChunked() throws IOException
  {
    //StringBuffer buf= new StringBuffer(1024);
    byte[] buf = new byte[maxCharLong + 2];
    int bufsz = 0;
    chunkSize = -1L;
    int c = -1;
    do
    {
      c = in.read();
      if (c > -1)
      {
        if (c != '\r' && c != '\n' && c != ' ' && c != '\t')
        {
          buf[bufsz++] = ((byte) c);
        }
      }
    }
    while (c > -1 && (c != '\n' || bufsz == 0) && bufsz < buf.length);
    if (c < 0)
    {
      closed = true;
    }
    String sbuf = new String(buf, 0, bufsz);
    if (bufsz > maxCharLong)
    {
      closed = true;
      throw new IOException("Chunked input stream failed to receive valid chunk size:" + sbuf);
    }
    try
    {
      chunkSize = Long.parseLong(sbuf, 16);
    }
    catch (NumberFormatException ne)
    {
      closed = true;
      throw new IOException("'" + sbuf + "' " + ne.getMessage());
    }
    if (chunkSize < 1L)
      closed = true;
    if (chunkSize != 0L && c < 0)
    {
      //If chunk size is zero try and be tolerant that there maybe no cr or lf
      // at the end.
      throw new IOException("HTTP Chunked stream closed in middle of chunk.");
    }
    if (chunkSize < 0L)
      throw new IOException("HTTP Chunk size received " + chunkSize + " is less than zero.");
    return chunkSize;
  }

  public void close() throws IOException
  {
    synchronized (this)
    {
      if (closed)
        return;
      closed = true;
    }
    byte[] b = new byte[1024];
    int bread = -1;
    do
    {
      bread = read(b, 0, b.length);
    }
    while (bread != -1);
  }

  /*
   * public void mark(int readlimit)
   * {
   * }
   */

  public void reset() throws IOException
  {
    throw new IOException("Don't support marked streams");
  }

  public boolean markSupported()
  {
    return false;
  }
}
