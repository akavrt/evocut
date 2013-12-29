package com.akavrt.csp._1d.results.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * User: akavrt
 * Date: 26.12.13
 * Time: 20:34
 */
public class LoadDataToolBar extends JToolBar {
    private final OnActionPerformedListener listener;
    private AbstractAction loadExistingDataAction;
    private AbstractAction loadMyDataAction;

    public LoadDataToolBar(OnActionPerformedListener listener) {
        this.listener = listener;
        setFloatable(false);
        setRollover(true);

        setupActions();
        setupContent();

        setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
    }

    private void setupActions() {
        loadExistingDataAction = new BaseAction("Papers", "Load data from existing papers",
                                                KeyEvent.VK_E,
                                                KeyStroke.getKeyStroke(KeyEvent.VK_E,
                                                                       InputEvent.CTRL_MASK)) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (listener != null) {
                    listener.loadPapersData();
                }
            }
        };

        loadMyDataAction = new BaseAction("EvoCut", "Load data obtained with EvoCut",
                                          KeyEvent.VK_R,
                                          KeyStroke.getKeyStroke(KeyEvent.VK_R,
                                                                 InputEvent.CTRL_MASK)) {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (listener != null) {
                    listener.loadEvoCutData();
                }
            }
        };
    }

    private void setupContent() {
        JButton existingDataButton = new JButton(loadExistingDataAction);
        existingDataButton.setMargin(new Insets(5, 5, 5, 5));
        existingDataButton.setFocusable(false);

        JButton myDataButton = new JButton(loadMyDataAction);
        myDataButton.setMargin(new Insets(5, 5, 5, 5));
        myDataButton.setFocusable(false);

        add(existingDataButton);
        addSeparator();
        add(myDataButton);
    }

    public interface OnActionPerformedListener {
        void loadPapersData();

        void loadEvoCutData();
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
