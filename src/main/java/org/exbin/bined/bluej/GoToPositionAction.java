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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPanel;
import org.exbin.bined.basic.PositionScrollVisibility;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.bined.gui.GoToBinaryPanel;
import org.exbin.framework.utils.LanguageUtils;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.WindowUtils.DialogWrapper;
import org.exbin.framework.utils.handler.DefaultControlHandler;
import org.exbin.framework.utils.gui.DefaultControlPanel;

/**
 * Go to handler.
 *
 * @version 0.2.0 2019/04/13
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class GoToPositionAction implements ActionListener {

    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(GoToBinaryPanel.class);
    private final ExtCodeArea codeArea;
    private Component parentComponent;

    public GoToPositionAction(ExtCodeArea codeArea) {
        this.codeArea = Objects.requireNonNull(codeArea);
    }
    
    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        final GoToBinaryPanel goToPanel = new GoToBinaryPanel();
        DefaultControlPanel goToControlPanel = new DefaultControlPanel(goToPanel.getResourceBundle());
        goToPanel.setCursorPosition(codeArea.getCaretPosition().getDataPosition());
        goToPanel.setMaxPosition(codeArea.getDataSize());
        JPanel dialogPanel = WindowUtils.createDialogPanel(goToPanel, goToControlPanel);
        final DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, parentComponent, resourceBundle.getString("dialog.title"), Dialog.ModalityType.APPLICATION_MODAL);

        goToPanel.initFocus();
        goToControlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                goToPanel.acceptInput();
                codeArea.setCaretPosition(goToPanel.getTargetPosition());
                PositionScrollVisibility visibility = codeArea.getPainter().computePositionScrollVisibility(codeArea.getCaretPosition());
                if (visibility != PositionScrollVisibility.VISIBLE) {
                    codeArea.centerOnCursor();
                }
            }

            dialog.close();
        });
        dialog.showCentered(parentComponent);
    }
}
