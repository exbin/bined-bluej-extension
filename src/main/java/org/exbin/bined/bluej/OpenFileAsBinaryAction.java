/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.bined.bluej;

import bluej.extensions.BlueJ;
import java.awt.Dialog;
import java.awt.Frame;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.bined.bluej.panel.BinaryEditorPanel;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.utils.panel.CloseControlPanel;

/**
 * Open file as binary action.
 *
 * @version 0.2.0 2019/05/12
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class OpenFileAsBinaryAction extends AbstractAction {

    private final BlueJMenuHandler menuHandler;
    private File previousPath = null;

    public OpenFileAsBinaryAction(BlueJMenuHandler menuHandler) {
        this.menuHandler = menuHandler;
        putValue(AbstractAction.NAME, "Open File As Binary...");
    }

    @Override
    public void actionPerformed(ActionEvent anEvent) {
        BlueJ bluej = menuHandler.getBlueJ();
        Frame frame = bluej.getCurrentFrame();

        JFileChooser fileChooser = new JFileChooser();
        if (previousPath != null) {
            fileChooser.setCurrentDirectory(previousPath);
        }
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File openedFile = fileChooser.getSelectedFile();
            previousPath = fileChooser.getCurrentDirectory();
            BinaryEditorPanel editorPanel = new BinaryEditorPanel(bluej);
            CloseControlPanel closeControlPanel = new CloseControlPanel();
            JPanel dialogPanel = WindowUtils.createDialogPanel(editorPanel, closeControlPanel);
            WindowUtils.DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, frame, "Binary Editor", Dialog.ModalityType.APPLICATION_MODAL);
            closeControlPanel.setHandler(() -> {
                dialog.close();
            });
            ((JDialog) dialog.getWindow()).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            ((JDialog) dialog.getWindow()).addWindowListener(new WindowAdapter() {
                boolean termination = false;

                @Override
                public void windowClosing(WindowEvent e) {
                    if (!termination && editorPanel.releaseFile()) {
                        termination = true;
                        ((JDialog) dialog.getWindow()).setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                        dialog.close();
                    }
                }
            });
            //            dialog.setSize(650, 460);

            try {
                editorPanel.openFile(openedFile);
            } catch (IOException ex) {
                Logger.getLogger(OpenFileAsBinaryAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            dialog.showCentered(frame);
            dialog.dispose();
        }
    }
}
