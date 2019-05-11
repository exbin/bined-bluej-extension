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

    private final BlueJ bluej;

    public OpenAsBinaryAction(BlueJ bluej) {
        this.bluej = bluej;
        putValue(AbstractAction.NAME, "Open As Binary...");
    }

    @Override
    public void actionPerformed(ActionEvent anEvent) {
        Frame frame = bluej.getCurrentFrame();
        BinaryEditorPanel editorPanel = new BinaryEditorPanel(bluej);
        CloseControlPanel closeControlPanel = new CloseControlPanel();
        JPanel dialogPanel = WindowUtils.createDialogPanel(editorPanel, closeControlPanel);
        WindowUtils.DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, null, "Binary Editor", Dialog.ModalityType.APPLICATION_MODAL);
        closeControlPanel.setHandler(() -> {
            dialog.close();
        });
        WindowUtils.assignGlobalKeyListener(dialog.getWindow(), closeControlPanel.createOkCancelListener());
        //            dialog.setSize(650, 460);
        dialog.show();
    }
}
