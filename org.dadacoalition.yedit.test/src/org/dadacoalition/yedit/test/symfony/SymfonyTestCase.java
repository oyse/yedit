package org.dadacoalition.yedit.test.symfony;

import org.dadacoalition.yedit.test.YEditTestCase;

public class SymfonyTestCase extends YEditTestCase {

    private String content;
    private String expectedContent;
        
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getExpectedContent() {
        return expectedContent;
    }
    public void setExpectedContent(String expectedContent) {
        this.expectedContent = expectedContent;
    }
    
}
