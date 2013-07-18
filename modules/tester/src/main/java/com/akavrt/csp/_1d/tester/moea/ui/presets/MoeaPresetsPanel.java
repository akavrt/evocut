package com.akavrt.csp._1d.tester.moea.ui.presets;

import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import com.akavrt.csp._1d.utils.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * User: akavrt
 * Date: 09.04.13
 * Time: 15:50
 */
public class MoeaPresetsPanel extends BasePresetsPanel {
    private JFormattedTextField populationSizeEdit;
    private JFormattedTextField runStepsEdit;

    public MoeaPresetsPanel() {
        populationSizeEdit = createIntValueEdit();
        runStepsEdit = createIntValueEdit();

        setLayout(new GridBagLayout());
        add(new JLabel("Evolutionary algorithm"),
            new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(10, 10, 10, 10));

        add(new JLabel("population size:", SwingConstants.RIGHT),
            new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 0).setInsets(5, 10, 3, 5));
        add(populationSizeEdit, new GBC(1, 1).setInsets(5, 0, 3, 10));

        add(new JLabel("generations:", SwingConstants.RIGHT),
            new GBC(0, 2).setFill(GBC.BOTH).setWeight(100, 0).setInsets(3, 10, 10, 5));
        add(runStepsEdit, new GBC(1, 2).setInsets(3, 0, 10, 10));

        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    }

    public EvolutionaryAlgorithmParameters getParameters() {
        if (Utils.isEmpty(populationSizeEdit.getText())
                || Utils.isEmpty(runStepsEdit.getText())) {
            return null;
        }

        int populationSize = ((Number) populationSizeEdit.getValue()).intValue();
        int runSteps = ((Number) runStepsEdit.getValue()).intValue();

        EvolutionaryAlgorithmParameters params = new EvolutionaryAlgorithmParameters();
        params.setPopulationSize(populationSize);
        params.setRunSteps(runSteps);

        return params;
    }

    public void setParameters(EvolutionaryAlgorithmParameters params) {
        if (params == null) {
            populationSizeEdit.setText("");
            runStepsEdit.setText("");
        } else {
            populationSizeEdit.setValue(params.getPopulationSize());
            runStepsEdit.setValue(params.getRunSteps());
        }
    }

}
