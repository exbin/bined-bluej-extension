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
package org.exbin.bined.bluej.panel;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.bined.bluej.BinEdApplyOptions;

/**
 * Hexadecimal editor options panel with border.
 *
 * @version 0.2.0 2019/03/16
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinEdOptionsPanelBorder extends javax.swing.JPanel {

    public BinEdOptionsPanelBorder() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionsPanel = new org.exbin.bined.bluej.panel.BinEdOptionsPanel();

        setLayout(new java.awt.BorderLayout());

        optionsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(optionsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.exbin.bined.bluej.panel.BinEdOptionsPanel optionsPanel;
    // End of variables declaration//GEN-END:variables

    @Nonnull
    public BinEdApplyOptions getApplyOptions() {
        return optionsPanel.getApplyOptions();
    }

    public void setApplyOptions(BinEdApplyOptions applyOptions) {
        optionsPanel.setApplyOptions(applyOptions);;
    }

    public void load() {
        optionsPanel.load();
    }

    public void store() {
        optionsPanel.store();
    }
}
