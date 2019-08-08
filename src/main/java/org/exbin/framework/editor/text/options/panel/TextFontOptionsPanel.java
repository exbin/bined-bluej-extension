/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.framework.editor.text.options.panel;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.framework.editor.text.options.impl.TextFontOptionsImpl;
import org.exbin.framework.gui.utils.LanguageUtils;
import org.exbin.framework.gui.utils.WindowUtils;
import org.exbin.framework.gui.options.api.OptionsCapable;
import org.exbin.framework.gui.options.api.OptionsModifiedListener;
import org.exbin.framework.editor.text.service.TextFontService;

/**
 * Text font options panel.
 *
 * @version 0.2.1 2019/07/19
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TextFontOptionsPanel extends javax.swing.JPanel implements OptionsCapable<TextFontOptionsImpl> {

    private OptionsModifiedListener optionsModifiedListener;
    private FontChangeAction fontChangeAction;
    private final ResourceBundle resourceBundle = LanguageUtils.getResourceBundleByClass(TextFontOptionsPanel.class);
    private TextFontService textFontService;
    private Font font;

    public TextFontOptionsPanel() {
        initComponents();
    }

    @Nonnull
    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setTextFontService(TextFontService textFontService) {
        this.textFontService = textFontService;
    }

    @Override
    public void saveToOptions(TextFontOptionsImpl options) {
        options.setUseDefaultFont(defaultFontCheckBox.isSelected());
        options.setFontAttributes(font != null ? (Map<TextAttribute, Object>) font.getAttributes() : null);
    }

    @Override
    public void loadFromOptions(TextFontOptionsImpl options) {
        boolean useDefaultFont = options.isUseDefaultFont();
        defaultFontCheckBox.setSelected(useDefaultFont);
        setEnabled(!useDefaultFont);

        font = textFontService.getDefaultFont().deriveFont(options.getFontAttributes());
        fontPreviewLabel.setFont(font);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        fontPreviewLabel.setEnabled(enabled);
        fillDefaultFontButton.setEnabled(enabled);
        fillCurrentFontButton.setEnabled(enabled);
        changeFontButton.setEnabled(enabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        colorChooser = new javax.swing.JColorChooser();
        defaultFontCheckBox = new javax.swing.JCheckBox();
        fillDefaultFontButton = new javax.swing.JButton();
        changeFontButton = new javax.swing.JButton();
        fontPreviewLabel = new javax.swing.JLabel();
        fillCurrentFontButton = new javax.swing.JButton();

        colorChooser.setName("colorChooser"); // NOI18N

        setName("Form"); // NOI18N

        defaultFontCheckBox.setSelected(true);
        defaultFontCheckBox.setText(resourceBundle.getString("defaultFontCheckBox.text")); // NOI18N
        defaultFontCheckBox.setName("defaultFontCheckBox"); // NOI18N
        defaultFontCheckBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                defaultFontCheckBoxItemStateChanged(evt);
            }
        });

        fillDefaultFontButton.setText(resourceBundle.getString("fillDefaultFontButton.text")); // NOI18N
        fillDefaultFontButton.setEnabled(false);
        fillDefaultFontButton.setName("fillDefaultFontButton"); // NOI18N
        fillDefaultFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillDefaultFontButtonActionPerformed(evt);
            }
        });

        changeFontButton.setText(resourceBundle.getString("changeFontButton.text")); // NOI18N
        changeFontButton.setEnabled(false);
        changeFontButton.setName("changeFontButton"); // NOI18N
        changeFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFontButtonActionPerformed(evt);
            }
        });

        fontPreviewLabel.setBackground(java.awt.Color.white);
        fontPreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fontPreviewLabel.setText(resourceBundle.getString("fontPreviewLabel.text")); // NOI18N
        fontPreviewLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        fontPreviewLabel.setEnabled(false);
        fontPreviewLabel.setName("fontPreviewLabel"); // NOI18N
        fontPreviewLabel.setOpaque(true);

        fillCurrentFontButton.setText(resourceBundle.getString("fillCurrentFontButton.text")); // NOI18N
        fillCurrentFontButton.setEnabled(false);
        fillCurrentFontButton.setName("fillCurrentFontButton"); // NOI18N
        fillCurrentFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillCurrentFontButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fontPreviewLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(defaultFontCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(changeFontButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fillDefaultFontButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fillCurrentFontButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(defaultFontCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fontPreviewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeFontButton)
                    .addComponent(fillDefaultFontButton)
                    .addComponent(fillCurrentFontButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void defaultFontCheckBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_defaultFontCheckBoxItemStateChanged
        boolean selected = evt.getStateChange() != ItemEvent.SELECTED;
        fontPreviewLabel.setEnabled(selected);
        fillDefaultFontButton.setEnabled(selected);
        fillCurrentFontButton.setEnabled(selected);
        changeFontButton.setEnabled(selected);
        setModified(true);
    }//GEN-LAST:event_defaultFontCheckBoxItemStateChanged

    private void fillDefaultFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillDefaultFontButtonActionPerformed
        fontPreviewLabel.setFont(textFontService.getDefaultFont());
        setModified(true);
    }//GEN-LAST:event_fillDefaultFontButtonActionPerformed

    private void changeFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeFontButtonActionPerformed
        if (fontChangeAction != null) {
            Font resultFont = fontChangeAction.changeFont(fontPreviewLabel.getFont());
            if (resultFont != null) {
                fontPreviewLabel.setFont(resultFont);
                setModified(true);
            }
        }
    }//GEN-LAST:event_changeFontButtonActionPerformed

    private void fillCurrentFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillCurrentFontButtonActionPerformed
        fontPreviewLabel.setFont(textFontService.getCurrentFont());
        setModified(true);
    }//GEN-LAST:event_fillCurrentFontButtonActionPerformed

    /**
     * Test method for this panel.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        WindowUtils.invokeDialog(new TextFontOptionsPanel());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changeFontButton;
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JCheckBox defaultFontCheckBox;
    private javax.swing.JButton fillCurrentFontButton;
    private javax.swing.JButton fillDefaultFontButton;
    private javax.swing.JLabel fontPreviewLabel;
    // End of variables declaration//GEN-END:variables

    private void setModified(boolean b) {
        if (optionsModifiedListener != null) {
            optionsModifiedListener.wasModified();
        }
    }

    @Override
    public void setOptionsModifiedListener(OptionsModifiedListener listener) {
        optionsModifiedListener = listener;
    }

    public void setFontChangeAction(FontChangeAction fontChangeAction) {
        this.fontChangeAction = fontChangeAction;
    }

    public static interface FontChangeAction {

        Font changeFont(Font currentFont);
    }
}
