package com.akavrt.csp._1d.tester.moea.ui.presets;

import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import com.akavrt.csp._1d.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * User: akavrt
 * Date: 18.07.13
 * Time: 03:54
 */
public class GeneratorPresetsPanel extends BasePresetsPanel {
    private JFormattedTextField seedEdit;
    private JFormattedTextField indexEdit;

    public GeneratorPresetsPanel() {
        seedEdit = createIntValueEdit();
        indexEdit = createIntValueEdit();

        setLayout(new GridBagLayout());
        add(new JLabel("Problem generator"),
            new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(10, 10, 10, 10));

        add(new JLabel("random seed:", SwingConstants.RIGHT),
            new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(5, 10, 3, 5));
        add(seedEdit, new GBC(1, 1).setInsets(5, 0, 3, 10));

        add(new JLabel("problem index:", SwingConstants.RIGHT),
            new GBC(0, 2).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 10, 5));
        add(indexEdit, new GBC(1, 2).setInsets(3, 0, 10, 10));
    }

    public int getRandomSeed() {
        return Utils.isEmpty(seedEdit.getText()) ? 0 : ((Number) seedEdit.getValue()).intValue();
    }

    public void setRandomSeed(int seed) {
        if (seed == 0) {
            seedEdit.setText("");
        } else {
            seedEdit.setValue(seed);
        }
    }

    public int getProblemIndex() {
        return Utils.isEmpty(indexEdit.getText()) ? 0 : ((Number) indexEdit.getValue()).intValue();
    }

    public void setProblemIndex(int index) {
        if (index == 0) {
            indexEdit.setText("");
        } else {
            indexEdit.setValue(index);
        }
    }

}

