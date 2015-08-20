package org.dadacoalition.yedit.editor;

/**
 * Class for representing task tags found by the task tag parser.
 */
public class TaskTag {
    private String tag;
    private String severity;
    private int lineNumber;
    private String message;
    
    public TaskTag(){
        
    }
    
    public TaskTag(String tag, String severity, int lineNumber, String message){
        this.setTag(tag);
        this.setSeverity(severity);
        this.setLineNumber(lineNumber);
        this.setMessage(message);
    }
    
    @Override
    public boolean equals(Object a){
                
        if( !(a instanceof TaskTag) ){
            return false;
        }
        
        TaskTag that = (TaskTag) a;
        
        if( !this.tag.equals(that.tag) 
                || !this.severity.equals(that.severity) 
                || this.lineNumber != that.lineNumber
                || !this.message.equals(that.message)){
            return false;
        }
        return true;
        
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("<");
        sb.append("tag: ");
        sb.append(tag);
        sb.append(", severity: ");
        sb.append(severity);
        sb.append(", lineNumber: ");
        sb.append(lineNumber);
        sb.append(", message: ");
        sb.append(message);
        sb.append(">");
        
        return sb.toString();
        
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}