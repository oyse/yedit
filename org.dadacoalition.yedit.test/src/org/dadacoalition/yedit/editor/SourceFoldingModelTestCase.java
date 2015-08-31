package org.dadacoalition.yedit.editor;

import java.util.List;

import org.dadacoalition.yedit.test.YEditTestCase;
import org.eclipse.jface.text.Position;

public class SourceFoldingModelTestCase extends YEditTestCase {
	
	private List<Position> expectedPositions;

	public List<Position> getExpectedPositions() {
		return expectedPositions;
	}

	public void setExpectedPositions(List<Position> expectedPositions) {
		this.expectedPositions = expectedPositions;
	}
	
	

}
