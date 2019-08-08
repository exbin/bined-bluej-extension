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
package org.exbin.framework.bined.options.panel;

import java.awt.Font;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.JPanel;
import org.exbin.bined.CodeAreaViewMode;
import org.exbin.bined.CodeCharactersCase;
import org.exbin.bined.CodeType;
import org.exbin.bined.PositionCodeType;
import org.exbin.bined.capability.RowWrappingCapable;
import org.exbin.framework.bined.options.impl.CodeAreaOptionsImpl;
import org.exbin.framework.editor.text.panel.TextFontPanel;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.utils.WindowUtils.DialogWrapper;
import org.exbin.framework.gui.utils.handler.DefaultControlHandler;
import org.exbin.framework.gui.utils.panel.DefaultControlPanel;
import org.exbin.framework.gui.options.api.OptionsCapable;
import org.exbin.framework.gui.options.api.OptionsModifiedListener;

/**
 * Code area preference parameters panel.
 *
 * @version 0.2.1 2019/07/20
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class CodeAreaOptionsPanel extends javax.swing.JPanel implements OptionsCapable<CodeAreaOptionsImpl> {

    private Font codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    private final java.util.ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(CodeAreaOptionsPanel.class);

    public CodeAreaOptionsPanel() {
        initComponents();
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public void saveToOptions(CodeAreaOptionsImpl options) {
        options.setCodeFont(codeFont);
        options.setCodeType(CodeType.valueOf((String) codeTypeComboBox.getSelectedItem()));
        options.setShowUnprintables(showNonprintableCharactersCheckBox.isSelected());
        options.setCodeCharactersCase(CodeCharactersCase.valueOf((String) codeCharactersModeComboBox.getSelectedItem()));
        options.setPositionCodeType(PositionCodeType.valueOf((String) positionCodeTypeComboBox.getSelectedItem()));
        options.setViewMode(CodeAreaViewMode.valueOf((String) viewModeComboBox.getSelectedItem()));
        options.setCodeColorization(codeColorizationCheckBox.isSelected());
        options.setUseDefaultFont(useDefaultFontCheckBox.isSelected());
        options.setRowWrappingMode(rowWrappingModeCheckBox.isSelected() ? RowWrappingCapable.RowWrappingMode.WRAPPING : RowWrappingCapable.RowWrappingMode.NO_WRAPPING);
        options.setMaxBytesPerRow((Integer) maxBytesPerRowSpinner.getValue());
        options.setMinRowPositionLength((Integer) minRowPositionLengthSpinner.getValue());
        options.setMaxRowPositionLength((Integer) maxRowPositionLengthSpinner.getValue());
    }

    @Override
    public void loadFromOptions(CodeAreaOptionsImpl options) {
        codeFont = options.getCodeFont();
        updateFontTextField();
        codeTypeComboBox.setSelectedItem(options.getCodeType().name());
        showNonprintableCharactersCheckBox.setSelected(options.isShowUnprintables());
        codeCharactersModeComboBox.setSelectedItem(options.getCodeCharactersCase().name());
        positionCodeTypeComboBox.setSelectedItem(options.getPositionCodeType().name());
        viewModeComboBox.setSelectedItem(options.getViewMode().name());
        codeColorizationCheckBox.setSelected(options.isCodeColorization());
        useDefaultFontCheckBox.setSelected(options.isUseDefaultFont());
        rowWrappingModeCheckBox.setSelected(options.getRowWrappingMode() == RowWrappingCapable.RowWrappingMode.WRAPPING);
        maxBytesPerRowSpinner.setValue(options.getMaxBytesPerRow());
        minRowPositionLengthSpinner.setValue(options.getMinRowPositionLength());
        maxRowPositionLengthSpinner.setValue(options.getMaxRowPositionLength());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codeCharactersModeComboBox = new javax.swing.JComboBox<>();
        viewModeComboBox = new javax.swing.JComboBox<>();
        showNonprintableCharactersCheckBox = new javax.swing.JCheckBox();
        viewModeScrollModeLabel = new javax.swing.JLabel();
        codeColorizationCheckBox = new javax.swing.JCheckBox();
        positionCodeTypeLabel = new javax.swing.JLabel();
        positionCodeTypeComboBox = new javax.swing.JComboBox<>();
        codeTypeScrollModeLabel = new javax.swing.JLabel();
        hexCharactersModeLabel = new javax.swing.JLabel();
        codeTypeComboBox = new javax.swing.JComboBox<>();
        fontPanel = new javax.swing.JPanel();
        useDefaultFontCheckBox = new javax.swing.JCheckBox();
        fontLabel = new javax.swing.JLabel();
        fontTextField = new javax.swing.JTextField();
        selectFontButton = new javax.swing.JButton();
        maxBytesPerRowLabel = new javax.swing.JLabel();
        maxBytesPerRowSpinner = new javax.swing.JSpinner();
        minRowPositionLengthLabel = new javax.swing.JLabel();
        minRowPositionLengthSpinner = new javax.swing.JSpinner();
        maxRowPositionLengthLabel = new javax.swing.JLabel();
        maxRowPositionLengthSpinner = new javax.swing.JSpinner();
        rowWrappingModeCheckBox = new javax.swing.JCheckBox();

        codeCharactersModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LOWER", "UPPER" }));

        viewModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DUAL", "HEXADECIMAL", "PREVIEW" }));

        showNonprintableCharactersCheckBox.setText(resourceBundle.getString("showNonprintableCharactersCheckBox.text")); // NOI18N

        viewModeScrollModeLabel.setText(resourceBundle.getString("viewModeScrollModeLabel.text")); // NOI18N

        codeColorizationCheckBox.setText(resourceBundle.getString("codeColorizationCheckBox.text")); // NOI18N

        positionCodeTypeLabel.setText(resourceBundle.getString("positionCodeTypeLabel.text")); // NOI18N

        positionCodeTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "OCTAL", "DECIMAL", "HEXADECIMAL" }));
        positionCodeTypeComboBox.setSelectedIndex(2);

        codeTypeScrollModeLabel.setText(resourceBundle.getString("codeTypeScrollModeLabel.text")); // NOI18N

        hexCharactersModeLabel.setText(resourceBundle.getString("hexCharactersModeLabel.text")); // NOI18N

        codeTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BINARY", "OCTAL", "DECIMAL", "HEXADECIMAL" }));
        codeTypeComboBox.setSelectedIndex(3);

        fontPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceBundle.getString("fontPanel.border.title"))); // NOI18N

        useDefaultFontCheckBox.setText(resourceBundle.getString("useDefaultFontCheckBox.text")); // NOI18N

        fontLabel.setText(resourceBundle.getString("fontLabel.text")); // NOI18N

        fontTextField.setEditable(false);

        selectFontButton.setText(resourceBundle.getString("selectFontButton.text")); // NOI18N
        selectFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFontButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fontPanelLayout = new javax.swing.GroupLayout(fontPanel);
        fontPanel.setLayout(fontPanelLayout);
        fontPanelLayout.setHorizontalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fontPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fontPanelLayout.createSequentialGroup()
                        .addComponent(fontTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectFontButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(fontPanelLayout.createSequentialGroup()
                        .addGroup(fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(useDefaultFontCheckBox)
                            .addComponent(fontLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        fontPanelLayout.setVerticalGroup(
            fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fontPanelLayout.createSequentialGroup()
                .addComponent(useDefaultFontCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fontTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectFontButton)))
        );

        maxBytesPerRowLabel.setText(resourceBundle.getString("maxBytesPerRowLabel.text")); // NOI18N

        maxBytesPerRowSpinner.setModel(new javax.swing.SpinnerNumberModel(16, 0, null, 1));

        minRowPositionLengthLabel.setText(resourceBundle.getString("minRowPositionLengthLabel.text")); // NOI18N

        minRowPositionLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        minRowPositionLengthSpinner.setValue(8);

        maxRowPositionLengthLabel.setText(resourceBundle.getString("maxRowPositionLengthLabel.text")); // NOI18N

        maxRowPositionLengthSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        maxRowPositionLengthSpinner.setValue(8);

        rowWrappingModeCheckBox.setText(resourceBundle.getString("wrapLineModeCheckBox.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codeColorizationCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showNonprintableCharactersCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codeCharactersModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(positionCodeTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewModeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codeTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fontPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maxBytesPerRowSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(minRowPositionLengthSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(maxRowPositionLengthSpinner, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hexCharactersModeLabel)
                            .addComponent(positionCodeTypeLabel)
                            .addComponent(viewModeScrollModeLabel)
                            .addComponent(codeTypeScrollModeLabel)
                            .addComponent(rowWrappingModeCheckBox)
                            .addComponent(maxBytesPerRowLabel)
                            .addComponent(minRowPositionLengthLabel)
                            .addComponent(maxRowPositionLengthLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(viewModeScrollModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewModeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeTypeScrollModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(positionCodeTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(positionCodeTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hexCharactersModeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeCharactersModeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showNonprintableCharactersCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeColorizationCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rowWrappingModeCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxBytesPerRowLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxBytesPerRowSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minRowPositionLengthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minRowPositionLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxRowPositionLengthLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxRowPositionLengthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFontButtonActionPerformed
        final TextFontPanel textFontPanel = new TextFontPanel();

        DefaultControlPanel textFontControlPanel = new DefaultControlPanel();
        textFontPanel.setStoredFont(codeFont);
        textFontPanel.setVisible(true);
        JPanel dialogPanel = WindowUtils.createDialogPanel(textFontPanel, textFontControlPanel);
        final DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, WindowUtils.getWindow(this), "Select Font", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        textFontControlPanel.setHandler((DefaultControlHandler.ControlActionType actionType) -> {
            if (actionType == DefaultControlHandler.ControlActionType.OK) {
                codeFont = textFontPanel.getStoredFont();
                updateFontTextField();
                useDefaultFontCheckBox.setSelected(false);
            }

            dialog.close();
        });
        dialog.showCentered(this);
        dialog.dispose();
    }//GEN-LAST:event_selectFontButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new CodeAreaOptionsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> codeCharactersModeComboBox;
    private javax.swing.JCheckBox codeColorizationCheckBox;
    private javax.swing.JComboBox<String> codeTypeComboBox;
    private javax.swing.JLabel codeTypeScrollModeLabel;
    private javax.swing.JLabel fontLabel;
    private javax.swing.JPanel fontPanel;
    private javax.swing.JTextField fontTextField;
    private javax.swing.JLabel hexCharactersModeLabel;
    private javax.swing.JLabel maxBytesPerRowLabel;
    private javax.swing.JSpinner maxBytesPerRowSpinner;
    private javax.swing.JLabel maxRowPositionLengthLabel;
    private javax.swing.JSpinner maxRowPositionLengthSpinner;
    private javax.swing.JLabel minRowPositionLengthLabel;
    private javax.swing.JSpinner minRowPositionLengthSpinner;
    private javax.swing.JComboBox<String> positionCodeTypeComboBox;
    private javax.swing.JLabel positionCodeTypeLabel;
    private javax.swing.JCheckBox rowWrappingModeCheckBox;
    private javax.swing.JButton selectFontButton;
    private javax.swing.JCheckBox showNonprintableCharactersCheckBox;
    private javax.swing.JCheckBox useDefaultFontCheckBox;
    private javax.swing.JComboBox<String> viewModeComboBox;
    private javax.swing.JLabel viewModeScrollModeLabel;
    // End of variables declaration//GEN-END:variables

    private void updateFontTextField() {
        int fontStyle = codeFont.getStyle();
        String fontStyleName;
        if ((fontStyle & (Font.BOLD + Font.ITALIC)) == Font.BOLD + Font.ITALIC) {
            fontStyleName = "Bold Italic";
        } else if ((fontStyle & Font.BOLD) > 0) {
            fontStyleName = "Bold";
        } else if ((fontStyle & Font.ITALIC) > 0) {
            fontStyleName = "Italic";
        } else {
            fontStyleName = "Plain";
        }
        fontTextField.setText(codeFont.getFamily() + " " + String.valueOf(codeFont.getSize()) + " " + fontStyleName);
    }

    @Override
    public void setOptionsModifiedListener(OptionsModifiedListener listener) {
    }
}
