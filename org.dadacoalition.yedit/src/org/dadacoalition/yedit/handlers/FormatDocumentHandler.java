package org.dadacoalition.yedit.handlers;

import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.editor.YEdit;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;


public class FormatDocumentHandler extends AbstractHandler {


    public Object execute(ExecutionEvent event) throws ExecutionException {
        YEditLog.logger.fine("FormatDocumentHandler executed");
        
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
        if( editorPart instanceof YEdit ){            
            YEdit yedit = (YEdit) editorPart;
            yedit.formatDocument();            
        } else {
            YEditLog.logger.warning("Expected the active editor to be YEdit, but it wasn't");
        }
        return null;        
    }

}
