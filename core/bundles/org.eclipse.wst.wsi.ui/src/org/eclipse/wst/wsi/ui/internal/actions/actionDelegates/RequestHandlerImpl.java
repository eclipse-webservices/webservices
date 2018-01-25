package org.eclipse.wst.wsi.ui.internal.actions.actionDelegates;

import java.util.Date;

import org.eclipse.wst.internet.monitor.core.internal.provisional.Request;
import org.eclipse.wst.wsi.internal.core.log.RequestHandler;

/**
 * Implements the RequestHandler interface which represents a TCP/IP
 * request made between the client and the server. Each request 
 * represents a request-response pair, where the request is from 
 * client -> server, and the response is from server -> client.
 * 
 * @author lauzond
 */
public class RequestHandlerImpl implements RequestHandler 
{
	/**
	 * The HTTP header of the request portion of this request.
	 */
	protected byte[] requestHeader;
	
	/**
	 * The HTTP header of the response portion of this request.
	 */
	protected byte[] responseHeader;

	/**
	 * The HTTP body of the request portion of this request.
	 */
	protected byte[] requestContent;

	/**
	 * The HTTP body of the response portion of this request.
	 */
	protected byte[] responseContent;

	/**
	 * The time this request was made.
	 */
	protected Date date = null;
	
	/**
	 * The local (client) port.
	 */
	protected int localPort = 0;

	/**
	 * The remote (server) port.
	 */
	protected int remotePort = 0;
	
	/**
	 * The remote (server) host.
	 */
	protected String remoteHost = null;

	/**
	 * The server's response time in milliseconds. 
	 */
	protected long responseTime = 0;
	
	/**
	 * Constructor.
	 * @param request a TCP/IP request
	 */
	RequestHandlerImpl(Request request)
	{
	  if (request != null)
	  {
		this.requestHeader = request.getRequest(Request.TRANSPORT);
		this.requestContent = request.getRequest(Request.CONTENT);
		this.responseHeader = request.getResponse(Request.TRANSPORT);
		this.responseContent = request.getResponse(Request.CONTENT);
		this.date = request.getDate();
		this.localPort = request.getLocalPort();
		this.remotePort = request.getRemotePort();
		this.remoteHost = request.getRemoteHost();
		this.responseTime = request.getResponseTime();
	  }
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getRequestHeader()
	 */
	public byte[] getRequestHeader() 
	{
		return this.requestHeader;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setRequestHeader(byte[])
	 */
	public void setRequestHeader(byte[] requestHeader) 
	{
		this.requestHeader = requestHeader;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getResponseHeader()
	 */
	public byte[] getResponseHeader() 
	{
		return this.responseHeader;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setResponseHeader(byte[])
	 */
	public void setResponseHeader(byte[] responseHeader) 
	{
		this.responseHeader = responseHeader;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getRequestContent()
	 */
	public byte[] getRequestContent() 
	{
		return this.requestContent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setResponseContent(byte[])
	 */
	public void setResponseContent(byte[] responseContent) 
	{
		this.responseContent = responseContent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getResponseContent()
	 */
	public byte[] getResponseContent() 
	{
		return this.responseContent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setRequestContent(byte[])
	 */
	public void setRequestContent(byte[] requestContent) 
	{
		this.requestContent = requestContent;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getDate()
	 */
	public Date getDate() 
	{
		return this.date;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setDate(java.util.Date)
	 */
	public void setDate(Date date) 
	{
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getLocalPort()
	 */
	public int getLocalPort() 
	{
		return this.localPort;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setLocalPort(int)
	 */
	public void setLocalPort(int localPort) 
	{
		this.localPort = localPort;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getRemotePort()
	 */
	public int getRemotePort() 
	{
		return this.remotePort;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setRemotePort(int)
	 */
	public void setRemotePort(int remotePort) 
	{
		this.remotePort = remotePort;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getRemoteHost()
	 */
	public String getRemoteHost() 
	{
		return this.remoteHost;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setRemoteHost(java.lang.String)
	 */
	public void setRemoteHost(String remoteHost) 
	{
		this.remoteHost = remoteHost;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#getResponseTime()
	 */
	public long getResponseTime() 
	{
		return this.responseTime;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.wsi.internal.core.log.RequestHandler#setResponseTime(long)
	 */
	public void setResponseTime(long responseTime) 
	{
		this.responseTime = responseTime;
	}
}
