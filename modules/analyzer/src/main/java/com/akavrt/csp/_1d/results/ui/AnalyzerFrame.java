package com.akavrt.csp._1d.results.ui;

import com.akavrt.csp._1d.results.*;
import com.akavrt.csp._1d.tester.moea.ui.utils.GBC;
import com.google.common.collect.Lists;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 20:09
 */
public class AnalyzerFrame extends JFrame implements LoadDataToolBar.OnActionPerformedListener {
    private static final String APP_NAME = "Analyzer";
    private static final double SCREEN_DIV = 2;
    private static final String DEFAULT_CURRENT_DIRECTORY_PATH =
            "/Users/akavrt/Development/source/evocut/data";
    private final ResultsProcessor processor;
    // views
    private DataPanel dataPanel;
    private JFileChooser chooser;
    // data
    private Map<CutgenClass, java.util.List<PaperResult>> existingData;

    public AnalyzerFrame() {
        prepareFileChooser();

        setupFrame();
        setupViews();

        pack();
        setVisible(true);

        processor = new ResultsProcessor();
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
        new AnalyzerFrame();
    }

    private void setupFrame() {
        setTitle(APP_NAME);
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

        LoadDataToolBar toolBar = new LoadDataToolBar(this);
        dataPanel = new DataPanel(this);

        add(toolBar, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0));
        add(dataPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(100, 100));
    }

    private void prepareFileChooser() {
        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
        chooser.addChoosableFileFilter(filter);
    }

    private void resetFileChooser() {
        File currentDirectory = new File(DEFAULT_CURRENT_DIRECTORY_PATH);
        chooser.setCurrentDirectory(currentDirectory.exists() ? currentDirectory : new File("."));
        chooser.setSelectedFile(new File(""));
        chooser.setMultiSelectionEnabled(false);
    }

    @Override
    public void loadPapersData() {
        resetFileChooser();

        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }

        dataPanel.clearPapersData();

        PaperResultsParser parser = new PaperResultsParser();
        parser.parse(chooser.getSelectedFile());

        existingData = parser.getQuantityResults();

        java.util.List<com.akavrt.csp._1d.results.ui.table.ClassData> classes = Lists.newArrayList();
        if (existingData != null) {
            // prepare papers' data
            for (Map.Entry<CutgenClass, List<PaperResult>> each : existingData.entrySet()) {
                processor.process(each.getValue());

                int nonDominated = 0;
                for (PaperResult classResult : each.getValue()) {
                    nonDominated += classResult.isDominated() ? 0 : 1;
                }

                com.akavrt.csp._1d.results.ui.table.ClassData classData = new com.akavrt.csp._1d.results.ui.table.ClassData(each.getKey(), each.getValue().size(),
                                                    nonDominated);
                classes.add(classData);
            }
        }

        dataPanel.setPapersData(classes);
    }

    @Override
    public void loadEvoCutData() {
        resetFileChooser();

        int r = chooser.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) {
            return;
        }

        dataPanel.clearEvoCutData();

        EvoResultsParser parser = new EvoResultsParser();
        parser.parse(chooser.getSelectedFile());

        // prepare evo data
        for (MethodMod mod : parser.getMods()) {
            for (List<EvoResult> results : mod.getData().values()) {
                processor.process(results);
            }
        }

        dataPanel.setEvoCutData(parser.getMods());
    }

    public Map<CutgenClass, java.util.List<PaperResult>> getPapersData() {
        return existingData;
    }
}
