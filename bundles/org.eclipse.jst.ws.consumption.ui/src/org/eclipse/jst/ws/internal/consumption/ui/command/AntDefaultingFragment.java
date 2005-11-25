package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.wst.command.internal.env.core.fragment.SequenceFragment;

public class AntDefaultingFragment extends SequenceFragment{

	
	public boolean getServiceIdsFixed()
	{
		return true;
	}

	public boolean getClientIdsFixed()
	{
		return true;
	}
}
