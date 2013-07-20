package com.akavrt.csp._1d.tester.moea.ui;

import com.akavrt.csp._1d.core.Problem;
import com.akavrt.csp._1d.cutgen.ProblemDescriptors;
import com.akavrt.csp._1d.cutgen.ProblemGenerator;
import com.akavrt.csp._1d.cutgen.PseudoRandom;
import com.akavrt.csp._1d.solver.evo.EvolutionaryAlgorithmParameters;
import com.akavrt.csp._1d.solver.moea.Chromosome;
import com.akavrt.csp._1d.solver.moea.MoeaAlgorithm;
import com.akavrt.csp._1d.solver.pattern.PatternGeneratorParameters;
import com.akavrt.csp._1d.tester.moea.ui.content.ContentPanel;
import com.akavrt.csp._1d.tester.moea.ui.presets.PresetsPanel;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: akavrt
 * Date: 18.07.13
 * Time: 03:40
 */
public class MainFrame extends JFrame implements MainToolBar.OnActionPerformedListener,
        AsyncSolver.OnProblemSolvedListener {
    private static final double SCREEN_DIV = 2;
    private static final String APP_NAME = "moo csp";
    // views
    private MainToolBar toolBar;
    private PresetsPanel presetsPanel;
    private ContentPanel contentPanel;
    // core
    private AsyncSolver solver;

    public MainFrame() {
        setupFrame();
        setupViews();
        setupParameters();

        pack();
        setVisible(true);

        contentPanel.setFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        new MainFrame();
    }

    private void setupFrame() {
        resetTitle();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // calc window size and position
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int windowWidth = (int) (screenWidth / SCREEN_DIV);
        int windowHeight = (int) (screenHeight / SCREEN_DIV);

        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setLocation((screenWidth - windowWidth) / 2, (screenHeight - windowHeight) / 2);
    }

    private void setupViews() {
        setLayout(new GridBagLayout());

        toolBar = new MainToolBar(this);
        presetsPanel = new PresetsPanel();
        contentPanel = new ContentPanel();

        add(toolBar, new GBC(0, 0, 2, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(presetsPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(0, 100));
        add(contentPanel, new GBC(1, 1).setFill(GBC.BOTH).setWeight(100, 100));
    }

    private void setupParameters() {
        PatternGeneratorParameters patternParams = new PatternGeneratorParameters();
        patternParams.setGenerationTrialsLimit(5);
        presetsPanel.setPatternGeneratorParameters(patternParams);

        EvolutionaryAlgorithmParameters moeaParams = new EvolutionaryAlgorithmParameters();
        moeaParams.setPopulationSize(50);
        moeaParams.setRunSteps(2000);
        presetsPanel.setMoeaParameters(moeaParams);

        ProblemDescriptors descriptors = new ProblemDescriptors(40, 1000, 0.2, 0.8, 100);
        presetsPanel.setProblemDescriptors(descriptors);

        presetsPanel.setRandomSeed(1994);
        presetsPanel.setProblemIndex(1);
    }

    public void resetTitle() {
        setTitle(APP_NAME);
    }

    @Override
    public void clearTrace() {
        contentPanel.clearTextArea();
        contentPanel.clearGraph();
    }

    @Override
    public boolean startCalculations() {
        Problem problem = generateProblem();
        if (problem == null) {
            JOptionPane.showMessageDialog(this, "Can't generate problem.", "Warning",
                                          JOptionPane.WARNING_MESSAGE);
            return false;
        }


        contentPanel.setProblem(problem);

        contentPanel.appendText("\nExecuting MOEA.\n");
        contentPanel.clearGraph();
        contentPanel.setSelectedIndex(1);

        MoeaAlgorithm algorithm = new AlgorithmCreator(presetsPanel).create();

        solver = new AsyncSolver(problem, algorithm, this);
        solver.execute();

        return true;
    }

    @Override
    public void stopCalculations() {
        if (solver != null && !solver.isDone()) {
            solver.cancel(true);

            contentPanel.appendText("\nExecution was canceled.\n");
        }
    }

    @Override
    public void onEvolutionProgressChanged(MoeaProgressUpdate update) {
        toolBar.onEvolutionProgressChanged(update);
        contentPanel.updateGraph(update);
    }

    @Override
    public void onProblemSolved(List<Chromosome> obtained) {
        toolBar.setProgressBarVisible(false);
        toolBar.setStartActionEnabled(true);
        toolBar.setStopActionEnabled(false);

        if (obtained == null || obtained.isEmpty()) {
            // notify user with update in text trace
            contentPanel.appendText("\nNo solution was found in run.");
        } else {
            contentPanel.setSolutions(obtained);
        }
    }

    private Problem generateProblem() {
        ProblemDescriptors descriptors = presetsPanel.getProblemDescriptors();
        if (descriptors == null) {
            return null;
        }

        int seed = presetsPanel.getRandomSeed();
        int index = presetsPanel.getProblemIndex();
        if (seed == 0 || index == 0) {
            return null;
        }

        PseudoRandom random = new PseudoRandom(seed);
        ProblemGenerator generator = new ProblemGenerator(random, descriptors);
        Problem problem = null;
        for (int i = 0; i < index; i++) {
            problem = generator.nextProblem();
        }

        return problem;
    }

}
