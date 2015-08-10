package org.dadacoalition.yedit.test.tasktagparser;

import java.util.List;

import org.dadacoalition.yedit.editor.TaskTag;
import org.dadacoalition.yedit.test.YEditTestCase;

public class TaskTagTestCase extends YEditTestCase {

    private List<TaskTag> expectedTags;

    public List<TaskTag> getExpectedTags() {
        return expectedTags;
    }

    public void setExpectedTags(List<TaskTag> expectedTags) {
        this.expectedTags = expectedTags;
    }
    
    
    
}
