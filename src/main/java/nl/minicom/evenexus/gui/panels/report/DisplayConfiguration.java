package nl.minicom.evenexus.gui.panels.report;

import java.util.List;

import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import com.google.common.collect.Lists;

public class DisplayConfiguration {

	private final int numberOfGroups;

	private int offset;
	private int scale;
	private List<Expression> groupExpressions;
	
	public DisplayConfiguration(int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
		this.offset = 0;
		this.scale = numberOfGroups < 50 ? numberOfGroups : 50;
		this.groupExpressions = Lists.newArrayList();
	}

	public int getOffset() {
		return offset;
	}
	
	public int getScale() {
		return scale;
	}
	
	public Expression[] getExpressions() {
		return groupExpressions.toArray(new Expression[0]);
	}

	public void modifyOffset(int i) {
		boolean allowModification = false;
		if (i < 0) {
			allowModification = offset + i >= 0;
		}
		else if (i > 0) {
			allowModification = offset + i + scale <= numberOfGroups;
		}
		
		if (allowModification) {
			offset += i;
		}
	}

	public void modifyScale(int i) {
		boolean allowModification = false;
		if (i < 0) {
			allowModification = scale + i >= Math.min(numberOfGroups, 5);
		}
		else if (i > 0) {
			allowModification = offset + scale + i <= numberOfGroups;
			if (!allowModification && offset - i >= 0) {
				offset -= i;
				allowModification = true;
			}
		}
		
		if (allowModification) {
			scale += i;
		}
	}
	
}
