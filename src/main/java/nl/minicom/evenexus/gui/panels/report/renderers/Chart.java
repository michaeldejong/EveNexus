package nl.minicom.evenexus.gui.panels.report.renderers;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import nl.minicom.evenexus.core.report.definition.components.ReportGroup;
import nl.minicom.evenexus.core.report.engine.ReportModel;
import nl.minicom.evenexus.core.report.persistence.expressions.Expression;

import com.google.common.collect.Lists;

public class Chart {
	
	protected ReportGroup getCurrentGroup(ReportModel reportModel, Expression... groupExpressions) {
		int index = 0;
		for (Expression expression : groupExpressions) {
			if (expression == null) {
				continue;
			}
			index++;
		}
		return reportModel.getReportGroups().get(index);
	}
	
	protected List<Color> createColorSequence(int length) {
		List<Color> colors = Lists.newArrayList();
		double count = 0.0;
		while (count < length) {
			int r = (int) (127.5 * (Math.cos(count) + 1.00));
			int g = (int) (127.5 * (Math.cos(count * 9.1 + Math.PI) + 1.00));
			int b = (int) (127.5 * (Math.sin(count + (1.5 * Math.PI)) + 1.00));
			colors.add(new Color(r, g, b));
			count = count + 1.0;
		}
		return Collections.unmodifiableList(colors);
	}

}
