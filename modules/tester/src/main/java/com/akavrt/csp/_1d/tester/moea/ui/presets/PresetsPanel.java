package com.akavrt.csp._1d.tester.moea.ui.presets;

import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;

import javax.swing.*;
import java.awt.*;

/**
 * User: akavrt
 * Date: 18.07.13
 * Time: 03:34
 */
public class PresetsPanel extends JPanel {
    private PatternPresetsPanel patternPanel;
    private MoeaPresetsPanel moeaPanel;
    private ProblemPresetsPanel problemPanel;
    private GeneratorPresetsPanel generatorPanel;

    public PresetsPanel() {
        setLayout(new GridBagLayout());

        patternPanel = new PatternPresetsPanel();
        moeaPanel = new MoeaPresetsPanel();
        problemPanel = new ProblemPresetsPanel();
        generatorPanel = new GeneratorPresetsPanel();

        add(patternPanel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(moeaPanel, new GBC(0, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(problemPanel, new GBC(0, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(generatorPanel, new GBC(0, 3).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(new JPanel(), new GBC(0, 4).setFill(GBC.BOTH).setWeight(100, 100));

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.GRAY));
    }

    public PatternGeneratorParameters getPatternGeneratorParameters() {
        return patternPanel.getParameters();
    }

    public void setPatternGeneratorParameters(PatternGeneratorParameters parameters) {
        patternPanel.setParameters(parameters);
    }

    public EvolutionaryAlgorithmParameters getMoeaParameters() {
        return moeaPanel.getParameters();
    }

    public void setMoeaParameters(EvolutionaryAlgorithmParameters parameters) {
        moeaPanel.setParameters(parameters);
    }

    public ProblemDescriptors getProblemDescriptors() {
        return problemPanel.getParameters();
    }

    public void setProblemDescriptors(ProblemDescriptors descriptors) {
        problemPanel.setParameters(descriptors);
    }

    public int getRandomSeed() {
        return generatorPanel.getRandomSeed();
    }

    public void setRandomSeed(int seed) {
        generatorPanel.setRandomSeed(seed);
    }

    public int getProblemIndex() {
        return generatorPanel.getProblemIndex();
    }

    public void setProblemIndex(int index) {
        generatorPanel.setProblemIndex(index);
    }

}
