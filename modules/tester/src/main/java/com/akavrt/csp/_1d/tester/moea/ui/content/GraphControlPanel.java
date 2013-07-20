package com.akavrt.csp._1d.tester.moea.ui.content;

import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * User: akavrt
 * Date: 20.07.13
 * Time: 18:13
 */
public class GraphControlPanel extends JPanel {
    private final JCheckBox showRejectedCheck;

    public GraphControlPanel(final GraphTracePanel graphTracePanel) {
        showRejectedCheck = new JCheckBox("Show rejected solutions");
        showRejectedCheck.setFocusPainted(false);
        showRejectedCheck.setSelected(true);

        showRejectedCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                graphTracePanel.showRejected(itemEvent.getStateChange() == ItemEvent.SELECTED);
            }
        });

        setLayout(new GridBagLayout());
        add(showRejectedCheck, new GBC(0, 0).setAnchor(GBC.WEST).setWeight(100, 0)
                                            .setInsets(5, 5, 5, 5));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
    }

}
