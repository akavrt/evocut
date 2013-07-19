package com.akavrt.csp._1d.tester.moea.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * User: akavrt
 * Date: 09.04.13
 * Time: 14:46
 */
public class MainToolBar extends JToolBar {
    private final OnActionPerformedListener listener;
    private JProgressBar progressBar;
    private AbstractAction clearAction;
    private AbstractAction startAction;
    private AbstractAction stopAction;

    public MainToolBar(OnActionPerformedListener listener) {
        this.listener = listener;
        setFloatable(false);
        setRollover(true);

        setupActions();
        setupContent();

        setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
    }

    private void setupActions() {
        clearAction = new BaseAction("Clear", "Stop calculations", KeyEvent.VK_L,
                                     KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (listener != null) {
                    listener.clearTrace();
                }
            }
        };

        startAction = new BaseAction("Start", "Start calculations", KeyEvent.VK_R,
                                     KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (listener != null && listener.startCalculations()) {
                    startAction.setEnabled(false);
                    stopAction.setEnabled(true);
                    progressBar.setValue(0);
                    progressBar.setString("");
                    progressBar.setVisible(true);
                }
            }
        };

        stopAction = new BaseAction("Stop", "Stop calculations", KeyEvent.VK_T,
                                    KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK)) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (listener != null) {
                    listener.stopCalculations();
                }

                startAction.setEnabled(true);
                stopAction.setEnabled(false);
                progressBar.setVisible(false);
                progressBar.setValue(0);
                progressBar.setString("");
            }
        };
        stopAction.setEnabled(false);
    }

    private void setupContent() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        JButton clearButton = new JButton(clearAction);
        clearButton.setMargin(new Insets(5, 5, 5, 5));
        clearButton.setFocusable(false);

        JButton startButton = new JButton(startAction);
        startButton.setMargin(new Insets(5, 5, 5, 5));
        startButton.setFocusable(false);

        JButton stopButton = new JButton(stopAction);
        stopButton.setMargin(new Insets(5, 5, 5, 5));
        stopButton.setFocusable(false);

        add(clearButton);
        addSeparator();
        add(startButton);
        add(stopButton);
        addSeparator();

        add(progressBar);
    }

    public void setStartActionEnabled(boolean enabled) {
        startAction.setEnabled(enabled);
    }

    public void setStopActionEnabled(boolean enabled) {
        stopAction.setEnabled(enabled);
    }

    public void setProgressBarVisible(boolean visible) {
        progressBar.setVisible(visible);
    }

    public void onEvolutionProgressChanged(MoeaProgressUpdate update) {
        progressBar.setValue(update.progress);
        progressBar.setString(String.format("%s %d%%",
                                            update.phase.getDescription(), update.progress));
    }

    public interface OnActionPerformedListener {
        void clearTrace();

        boolean startCalculations();

        void stopCalculations();
    }

    private static abstract class BaseAction extends AbstractAction {
        public BaseAction(String text, String desc, Integer mnemonic, KeyStroke accelerator) {
            super(text);

            putValue(SHORT_DESCRIPTION, desc);
            putValue(MNEMONIC_KEY, mnemonic);
            putValue(ACCELERATOR_KEY, accelerator);
        }
    }

}
