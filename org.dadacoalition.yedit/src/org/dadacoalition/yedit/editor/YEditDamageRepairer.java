/*******************************************************************************
 * Copyright (c) 2015 Øystein Idema Torget 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Øystein Idema Torget
 *******************************************************************************/
package org.dadacoalition.yedit.editor;

import org.dadacoalition.yedit.YEditLog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;

/**
 * Damage and repairer for YAML files. The only difference from
 * DefaultDamageRepairer is that it does not damage whole line
 * if a line is long. This is for performance reasons since
 * damaging long lines would lead to the editor hanging several
 * seconds for each key press.
 *
 */
public class YEditDamageRepairer extends DefaultDamagerRepairer {

	/**
	 * The maximum length of a line to damage completely.
	 */
	public static final int MAX_COMPLETE_LINE_DAMAGE = 200;
	
	/**
	 * The number of chars to damage in front of and at the end of
	 * the offset.
	 */
	public static final int CHARS_TO_DAMAGE = 25;
	
	public YEditDamageRepairer(ITokenScanner scanner) {
		super(scanner);	
	}
	
	@Override
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent e, boolean documentPartitioningChanged){
		
		
		IRegion damagedRegion = null;
		try {
			IDocument doc = e.getDocument();
			IRegion damagedLine = doc.getLineInformationOfOffset(e.getOffset());
			
			// the damaged line is too long so calculate a smaller region to damage instead of the
			// whole line.
			if(damagedLine.getLength() > MAX_COMPLETE_LINE_DAMAGE ){
				damagedRegion = calculateRegionToDamage(e.getOffset(), doc);
			} else {
				damagedRegion = super.getDamageRegion(partition, e, documentPartitioningChanged);
			}
			
			
		} catch (BadLocationException ex) {
			YEditLog.logException(ex, "Failed to get the region for a document change.");
		}
		
		return damagedRegion;
	}
	
	private IRegion calculateRegionToDamage(int offset, IDocument doc) throws BadLocationException{
		
		int damagedLineNum = doc.getLineOfOffset(offset);
		int lineOffset = doc.getLineOffset(damagedLineNum); 
		int damagedLineLength = doc.getLineLength(damagedLineNum);
		
		int startDamageOffset = offset - CHARS_TO_DAMAGE;
		if( startDamageOffset < lineOffset ){
			startDamageOffset = lineOffset;
		}
		
		int endDamageOffset = offset + CHARS_TO_DAMAGE;
		if( endDamageOffset > lineOffset + damagedLineLength ){
			endDamageOffset = lineOffset + damagedLineLength;
		}
		
		IRegion regionToDamage = new Region(startDamageOffset, endDamageOffset - startDamageOffset);
		return regionToDamage;
		
	}

}
