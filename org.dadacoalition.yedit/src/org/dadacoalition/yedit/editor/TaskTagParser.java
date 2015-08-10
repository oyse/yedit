package org.dadacoalition.yedit.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.editor.scanner.YAMLScanner;
import org.dadacoalition.yedit.editor.scanner.YAMLToken;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Simpler parser for searching for task tags in Yaml comments.
 */
public class TaskTagParser {

    
    private final List<TaskTagPreference> tagsToFind;
    private final YAMLScanner scanner;
    
    private final Pattern tagPattern;
    
    public TaskTagParser(List<TaskTagPreference> tagsToFind, YAMLScanner scanner){
        this.tagsToFind = tagsToFind;
        this.scanner = scanner;
        
        tagPattern = Pattern.compile(constructTagPattern(tagsToFind));
        
    }
    
    /**
     * Parse the comment of the entire document looking for task tags according
     * list of registered task tags.
     * @param document The document to parse
     * @return A list of all found task tags.
     */
    public List<TaskTag> parseTags(IDocument document){
        
        if(tagsToFind.isEmpty()){
            return new ArrayList<TaskTag>();
        }
        
        List<TaskTag> foundTags = new ArrayList<TaskTag>();
        int rangeLength = document.getLength();
        scanner.setRange(document, 0, rangeLength);

        // Scan for tokens. It is crucial to update the range of the
        // scanner after each read token to prevent and infinite loop
        IToken token = scanner.nextToken();
        while (token != Token.EOF) {

            if(tokenHasTag(token, document)){
                YAMLToken yToken = (YAMLToken) token;
                TaskTag tag = parseToken(document, yToken);
                foundTags.add(tag);
            }

            //update the range of the scanner
            int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
            rangeLength = rangeLength - scanner.getTokenLength();
            scanner.setRange(document, newOffset, rangeLength);

            token = scanner.nextToken();
        }
        
        return foundTags;

    }
    
    private boolean tokenHasTag(IToken token, IDocument document){
        if(!(token instanceof YAMLToken) || ((YAMLToken) token).getTokenType() != YAMLToken.COMMENT){
            return false;
        }
        
        String comment = getTokenString(document);
        return hasTag(comment);
    }


    private TaskTag parseToken(IDocument document, YAMLToken token) {

        String tokenString = getTokenString(document);
        TaskTag todoTag = constructTodoTag(tokenString, document);

        return todoTag;
    }


    private String getTokenString(IDocument document){
        try {
            return document.get(scanner.getTokenOffset(), scanner.getTokenLength());
        } catch (BadLocationException e) {
            YEditLog.logException(e, "Finding todo tag failed" );
            return "";
        }
    }
    
    private TaskTag constructTodoTag(String tokenString, IDocument document) {
        
        int lineNumber = 1;
        try {
            lineNumber = document.getLineOfOffset(scanner.getTokenOffset()) + 1;
        } catch (BadLocationException e) {
            YEditLog.logException(e, "Failed to get the correct line number" );
        }
        
        TaskTagPreference ttp = getFoundTagType(tokenString);
        String message = getTodoTagMessage(tokenString, ttp.tag);
        return new TaskTag(ttp.tag, ttp.severity, lineNumber, message);
    }
    
    private String getTodoTagMessage(String comment, String tag){
        
        String message = comment.substring(comment.indexOf(tag) + tag.length()); 
        return message.trim();
        
    }

    private TaskTagPreference getFoundTagType(String comment){
        
        for(TaskTagPreference ttp : tagsToFind){
            if(comment.indexOf(ttp.tag) > 0){
                return ttp;
            }
        }
        throw new IllegalArgumentException("Did not find the correct tag preference for : '" + comment + "' Should not be possible.");
    }

    private String constructTagPattern(List<TaskTagPreference> tagsToFind){
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(TaskTagPreference ttp : tagsToFind){
            if(!first){
                sb.append("|");
            }
            
            sb.append(ttp.tag);
            first = false;
        }
        return sb.toString();
        
    }
    
    private boolean hasTag(String comment){
        
        Matcher m = tagPattern.matcher(comment);
        return m.find();        
    }
}
