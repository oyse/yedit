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

import org.eclipse.jface.text.BadLocationException;
import org.dadacoalition.yedit.editor.YEditDoubleClickStrategy;
import org.dadacoalition.yedit.editor.YEditDoubleClickStrategy.SelectDirection;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.graphics.Point;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class YEditDoubleClickStrategyTest {

    private static YEditDoubleClickStrategy doubleClickStrategy;
    private static Document doc;
    
    @BeforeClass
    public static void setup(){
        doubleClickStrategy = new YEditDoubleClickStrategy();
        doc = new Document();
    }
    
    @Test
    public void checkCharAtSelection_emptyContent() throws BadLocationException {
        doc.set("");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 0));       
    }
    
    @Test
    public void checkCharAtSelection_notQuotedScalar() throws BadLocationException{
        doc.set("some text that is not quoted");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 1));
    }
    
    @Test(expected = BadLocationException.class)
    public void checkCharAtSelection_incorrectOffset() throws BadLocationException{
        doc.set("some");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 5));        
    }
    
    @Test
    public void checkCharAtSelection_singleQuotedButIncorrectSelection() throws BadLocationException{
        doc.set("some 'text that is' quoted");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 3));
        
    }
    
    @Test
    public void checkCharAtSelection_singleQuotedCorrectForwardSelection() throws BadLocationException{
        doc.set("some 'text that is quoted'");
        assertEquals(SelectDirection.FORWARD_SINGLE, doubleClickStrategy.checkCharAtSelection(doc, 6));
        
    }    

    @Test
    public void checkCharAtSelection_singleQuotedCorrectBackwardSelection() throws BadLocationException{
        doc.set("some 'text that is quoted'");
        assertEquals(SelectDirection.BACKWARD_SINGLE, doubleClickStrategy.checkCharAtSelection(doc, 25));
        
    }    
    
    @Test
    public void checkCharAtSelection_doubleQuotedCorrectForwardSelection() throws BadLocationException{
        doc.set("some \"text that is\" not quoted");
        assertEquals(SelectDirection.FORWARD_DOUBLE, doubleClickStrategy.checkCharAtSelection(doc, 6));
        
    }    

    @Test
    public void checkCharAtSelection_doubleQuotedCorrectBackwardSelection() throws BadLocationException{
        doc.set("some \"text that is\" not quoted");
        assertEquals(SelectDirection.BACKWARD_DOUBLE, doubleClickStrategy.checkCharAtSelection(doc, 18));
        
    }
    
    @Test
    public void checkCharAtSelection_completeDocumentQuotedAtEndOfDoc() throws BadLocationException{
        doc.set("'text'");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 6));
        
    }
    
    @Test
    public void checkCharAtSelection_completeDocumentQuotedAtEnd() throws BadLocationException{
        doc.set("'text'");
        assertEquals(SelectDirection.BACKWARD_SINGLE, doubleClickStrategy.checkCharAtSelection(doc, 5));
        
    }
    
    @Test
    public void checkCharAtSelection_completeDocumentQuotedAtStart() throws BadLocationException{
        doc.set("'text'");
        assertEquals(SelectDirection.FORWARD_SINGLE, doubleClickStrategy.checkCharAtSelection(doc, 1));
        
    }    
    
    @Test
    public void checkCharAtSelection_completeDocumentQuotedAtBegginingOfDoc() throws BadLocationException{
        doc.set("'text'");
        assertEquals(SelectDirection.NONE, doubleClickStrategy.checkCharAtSelection(doc, 0));
        
    }        
    
    
    @Test
    public void checkCharAtSelection_emptyStringAtStart() throws BadLocationException{
        doc.set("''");
        assertEquals(SelectDirection.FORWARD_SINGLE, doubleClickStrategy.checkCharAtSelection(doc, 1));
        
    }    
    
    @Test
    public void calculateSelectionRange_emptySelectionForward() throws BadLocationException{
        doc.set("''");
        assertEquals(new Point(1,0), doubleClickStrategy.calculateSelectionRange(SelectDirection.FORWARD_SINGLE, doc, 1));
    }
        
    @Test
    public void calculateSelectionRange_emptySelectionBackward() throws BadLocationException{
        doc.set("''");
        assertEquals(new Point(1,0), doubleClickStrategy.calculateSelectionRange(SelectDirection.BACKWARD_SINGLE, doc, 1));
    }
    
    @Test
    public void calculateSelectionRange_emptySelectionNonEmptyDocForward() throws BadLocationException{
        doc.set("some here '' and here");
        assertEquals(new Point(11,0), doubleClickStrategy.calculateSelectionRange(SelectDirection.FORWARD_SINGLE, doc, 11));
    }
    
    @Test
    public void calculateSelectionRange_emptySelectionNonEmptyDocBackward() throws BadLocationException{
        doc.set("some here '' and here");
        assertEquals(new Point(11,0), doubleClickStrategy.calculateSelectionRange(SelectDirection.BACKWARD_SINGLE, doc, 11));
    }        
    
    @Test
    public void calculateSelectionRange_nonEmptySelectionBackward() throws BadLocationException{
        doc.set("before \"testing non empty\" after");
        assertEquals(new Point(8,17), doubleClickStrategy.calculateSelectionRange(SelectDirection.BACKWARD_DOUBLE, doc, 25));
    }       

    @Test
    public void calculateSelectionRange_nonEmptySelectionForward() throws BadLocationException{
        doc.set("before \"testing non empty\" after");
        assertEquals(new Point(8,17), doubleClickStrategy.calculateSelectionRange(SelectDirection.FORWARD_DOUBLE, doc, 8));
    }
    
    @Test
    public void calculateSelectionRange_missingEndQuoteForward() throws BadLocationException{
        doc.set("before 'testing non empty\" after");
        assertNull(doubleClickStrategy.calculateSelectionRange(SelectDirection.FORWARD_SINGLE, doc, 8));
    }      
    
    @Test
    public void calculateSelectionRange_missingEndQuoteBackward() throws BadLocationException{
        doc.set("before testing non empty' after");
        assertNull(doubleClickStrategy.calculateSelectionRange(SelectDirection.BACKWARD_SINGLE, doc, 24));
    }
    
    @Test
    public void calculateSelectionRange_multiQuotesForward() throws BadLocationException{
        doc.set("this is 'multi' 'quotes'");
        assertEquals(new Point(9,5), doubleClickStrategy.calculateSelectionRange(SelectDirection.FORWARD_SINGLE, doc, 9));
    }
    
    @Test
    public void calculateSelectionRange_multiQuotesBackward() throws BadLocationException{
        doc.set("this is 'multi' 'quotes'");
        assertEquals(new Point(17,6), doubleClickStrategy.calculateSelectionRange(SelectDirection.BACKWARD_SINGLE, doc, 23));
    }    
        
}
