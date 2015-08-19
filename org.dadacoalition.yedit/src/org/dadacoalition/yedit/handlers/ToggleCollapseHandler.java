/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget and others
 *******************************************************************************/
package org.dadacoalition.yedit.handlers;

import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.editor.YEdit;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handler for toggling the collapse/expand of the outline view elements.
 */
public class ToggleCollapseHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if( editorPart instanceof YEdit ){
			YEdit yedit = (YEdit) editorPart;
			yedit.getContentOutlinePage().toggleCollapse();
		} else {
			YEditLog.logger.warning("Expected the active editor to be YEdit, but it wasn't");
		}
		return null;
	}

}
