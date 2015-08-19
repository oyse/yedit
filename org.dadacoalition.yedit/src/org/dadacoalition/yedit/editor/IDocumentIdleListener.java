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
/*
 * Created on 05.12.2003
 *
 */
package org.dadacoalition.yedit.editor;

import org.eclipse.jface.text.source.ISourceViewer;

/**
 * @author luelljoc
 * 
 * From EPIC Perl editor
 *
 */
public interface IDocumentIdleListener
{
    /**
     * This notification occurs on the Display thread within a configured
     * time period after the viewed document has changed, provided that
     * the viewer is visible at that time. If the document has not changed
     * or the viewer is not visible, the notification may, but does not have
     * to occur.
     */
	public void editorIdle(ISourceViewer viewer);
}
