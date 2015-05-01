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
import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.graphics.Point;

/**
 * Double click strategy that emulates the double click strategy for strings in
 * the Eclipse Java editor. If either the previous or current char is a quote character
 * it will search forward or backward for a quote of the same time and then select
 * the entire contents of the quoted scalar instead of just a single word.
 */
public class YEditDoubleClickStrategy extends DefaultTextDoubleClickStrategy {

    protected enum SelectDirection {
        FORWARD_SINGLE, BACKWARD_SINGLE, FORWARD_DOUBLE, BACKWARD_DOUBLE, NONE;
        
        protected char quoteChar(){
            if(this.equals(BACKWARD_SINGLE) || this.equals(FORWARD_SINGLE)){
                return '\'';
            }
            return '"';
        }
        
        protected boolean isForward(){
            if(this.equals(FORWARD_DOUBLE) || this.equals(FORWARD_SINGLE)){
                return true;
            }
            return false;
        }
        
    }
    
    @Override
    public void doubleClicked(ITextViewer part){
        
        IDocument doc = part.getDocument();
        
        Point selectionRange = part.getSelectedRange();
        int offset = selectionRange.x;
        boolean actionCompleted = false;
        
        try {

            SelectDirection selectDirection = checkCharAtSelection(doc, offset);
            if( SelectDirection.NONE != selectDirection){
                YEditLog.logger.fine("Found quoted scalar at position " + offset + " on line" + EditorUtils.getLine(doc, offset));
                Point selection = calculateSelectionRange(selectDirection, doc, offset);
                if( null != selection ){
                    part.setSelectedRange(selection.x, selection.y);
                    actionCompleted = true;
                }
            } else {
                YEditLog.logger.fine("Found not quoted scalar at position " + offset + " on line" + EditorUtils.getLine(doc, offset));
            }
            
        } catch (BadLocationException e) {
            YEditLog.logException(e, "Failure during doubleclick action");
        }
        
        // if we have not performed any action here, then delegate to the super class
        if(!actionCompleted){
            super.doubleClicked(part);
        }

        
    }

    /**
     * Calculate the range that should be select for the scalar
     * @param selectDirection The direction to search and the type of scalar to look for.
     * @param document The document to search in
     * @param offset The offset to start from
     * @return A point where the x coordinate is the offset and the y coordinate is the length of the selection.
     *         This is similar to what the Eclipse APIs return
     * @throws BadLocationException If a bad location is requested.
     */
    protected Point calculateSelectionRange(SelectDirection selectDirection, IDocument document, int offset) throws BadLocationException{
        
        char quoteChar = selectDirection.quoteChar();
        
        int x = -1;
        int y = -1;
        int docLength = document.getLength();
        if( selectDirection.isForward()){

            for(int i = offset; i < docLength; i++){
                char currChar = document.getChar(i);
                if( currChar == quoteChar ){
                    x = offset;
                    y = i - offset;
                    break;
                }
            }

        } else {
            for(int i = offset - 1; i >= 0; i--){
                char currChar = document.getChar(i);
                if( currChar == quoteChar ){
                    x = i + 1;
                    y = offset - x;
                    break;
                }
            }            
        }
        
        if( -1 == x || -1 == y ){
            return null;
        }
        return new Point(x, y);
    }   
    
    /**
     * Check if we have quoted scalar at the selection point according to Eclipse double click rules. 
     * I.e. we have a quoted scalar
     * if the character immediately before or after the first character in the selection is a single
     * or double quote
     * @param document The document to check
     * @param offset The offset of the first character
     * @return true if the selection
     * @throws BadLocationException 
     */
    protected SelectDirection checkCharAtSelection(IDocument document, int offset) throws BadLocationException{
        
        if( 0 == offset ){
            return SelectDirection.NONE;
        }
        
        if( document.getLength() == offset ) {
            return SelectDirection.NONE;
        }
        
        String prevChar = document.get(offset -1, 1);
        String nextChar = document.get(offset, 1);
        
        if( "'".equals(prevChar) ){
            return SelectDirection.FORWARD_SINGLE;
        } else if("\"".equals(prevChar)){
            return SelectDirection.FORWARD_DOUBLE;
        } else if("'".equals(nextChar) ){
            return SelectDirection.BACKWARD_SINGLE;
        } else if( "\"".equals(nextChar)){
            return SelectDirection.BACKWARD_DOUBLE;
        }
        
        return SelectDirection.NONE;
        
        
    }
    
}
