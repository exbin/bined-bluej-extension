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

import bluej.extensions.BClass;
import bluej.extensions.BObject;
import bluej.extensions.BlueJ;
import bluej.extensions.ClassNotFoundException;
import bluej.extensions.PackageNotFoundException;
import bluej.extensions.ProjectNotOpenException;
import java.awt.Dialog;
import java.awt.Frame;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.bined.bluej.panel.BinaryEditorPanel;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.utils.panel.CloseControlPanel;

/**
 * Open as binary action.
 *
 * @version 0.2.0 2019/05/11
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class OpenAsBinaryAction extends AbstractAction {

    private final BlueJMenuHandler menuHandler;

    public OpenAsBinaryAction(BlueJMenuHandler menuHandler) {
        this.menuHandler = menuHandler;
        putValue(AbstractAction.NAME, "Open As Binary...");
    }

    @Override
    public void actionPerformed(ActionEvent anEvent) {
        BlueJ bluej = menuHandler.getBlueJ();
        Frame frame = bluej.getCurrentFrame();
        BinaryEditorPanel editorPanel = new BinaryEditorPanel(bluej);
        CloseControlPanel closeControlPanel = new CloseControlPanel();
        JPanel dialogPanel = WindowUtils.createDialogPanel(editorPanel, closeControlPanel);
        WindowUtils.DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, frame, "Binary Editor", Dialog.ModalityType.APPLICATION_MODAL);
        closeControlPanel.setHandler(() -> {
            dialog.close();
        });
        //            dialog.setSize(650, 460);

        try {
            BClass curClass = menuHandler.getCurClass();
            if (curClass != null) {
                editorPanel.openFile(curClass.getJavaFile());
            } else {
                BObject curObject = menuHandler.getCurObject();
                if (curObject != null) {
                    editorPanel.openFile(curObject.getBClass().getJavaFile());
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(OpenAsBinaryAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProjectNotOpenException ex) {
            Logger.getLogger(OpenAsBinaryAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PackageNotFoundException ex) {
            Logger.getLogger(OpenAsBinaryAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OpenAsBinaryAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        dialog.showCentered(frame);
    }
}
