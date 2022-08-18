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
package org.exbin.bined.bluej.gui;

import bluej.extensions.BlueJ;
import org.exbin.framework.preferences.PreferencesWrapper;
import org.exbin.framework.editor.text.EncodingsHandler;
import org.exbin.framework.bined.preferences.BinaryEditorPreferences;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import org.exbin.bined.basic.BasicCodeAreaZone;
import org.exbin.bined.CodeAreaCaretPosition;
import org.exbin.bined.EditMode;
import org.exbin.bined.EditOperation;
import org.exbin.bined.PositionCodeType;
import org.exbin.bined.bluej.BinEdApplyOptions;
import org.exbin.bined.bluej.GoToPositionAction;
import org.exbin.bined.bluej.SearchAction;
import org.exbin.bined.capability.CharsetCapable;
import org.exbin.auxiliary.paged_data.delta.DeltaDocument;
import org.exbin.auxiliary.paged_data.delta.FileDataSource;
import org.exbin.auxiliary.paged_data.delta.SegmentsRepository;
import org.exbin.bined.extended.layout.ExtendedCodeAreaLayoutProfile;
import org.exbin.bined.highlight.swing.extended.ExtendedHighlightNonAsciiCodeAreaPainter;
import org.exbin.framework.bined.gui.ValuesPanel;
import org.exbin.bined.operation.BinaryDataCommand;
import org.exbin.bined.operation.swing.CodeAreaOperationCommandHandler;
import org.exbin.bined.operation.swing.CodeAreaUndoHandler;
import org.exbin.bined.operation.undo.BinaryDataUndoUpdateListener;
import org.exbin.bined.swing.basic.color.CodeAreaColorsProfile;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.bined.swing.extended.theme.ExtendedCodeAreaThemeProfile;
import org.exbin.framework.bined.gui.BinaryStatusPanel;
import org.exbin.framework.editor.text.TextEncodingStatusApi;
import org.exbin.framework.utils.WindowUtils;
import org.exbin.framework.utils.handler.OptionsControlHandler;
import org.exbin.framework.utils.gui.OptionsControlPanel;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.auxiliary.paged_data.ByteArrayData;
import org.exbin.auxiliary.paged_data.PagedData;
import org.exbin.framework.bined.BinaryStatusApi;
import org.exbin.framework.bined.FileHandlingMode;
import org.exbin.framework.bined.options.CodeAreaColorOptions;
import org.exbin.framework.bined.options.CodeAreaLayoutOptions;
import org.exbin.framework.bined.options.CodeAreaOptions;
import org.exbin.framework.bined.options.CodeAreaThemeOptions;
import org.exbin.framework.bined.options.EditorOptions;
import org.exbin.framework.bined.options.StatusOptions;
import org.exbin.framework.bined.options.impl.CodeAreaOptionsImpl;
import org.exbin.framework.editor.text.options.TextEncodingOptions;
import org.exbin.framework.editor.text.options.TextFontOptions;
import org.exbin.framework.about.gui.AboutPanel;
import org.exbin.framework.utils.ActionUtils;
import org.exbin.framework.utils.WindowUtils.DialogWrapper;
import org.exbin.framework.utils.gui.CloseControlPanel;
import org.exbin.auxiliary.paged_data.ByteArrayEditableData;
import org.exbin.auxiliary.paged_data.EditableBinaryData;
import org.exbin.bined.bluej.BlueJPreferencesWrapper;

/**
 * Binary editor main panel.
 *
 * @version 0.2.0 2019/08/08
 * @author ExBin Project (http://exbin.org)
 */
public final class BinEdComponentPanel extends JPanel {

    public static final String ACTION_CLIPBOARD_CUT = "cut-to-clipboard";
    public static final String ACTION_CLIPBOARD_COPY = "copy-to-clipboard";
    public static final String ACTION_CLIPBOARD_PASTE = "paste-from-clipboard";
    private static final FileHandlingMode DEFAULT_FILE_HANDLING_MODE = FileHandlingMode.DELTA;

    private final BinaryEditorPreferences preferences;
    private static SegmentsRepository segmentsRepository = null;
    private final ExtCodeArea codeArea;
    private final CodeAreaUndoHandler undoHandler;
    private final ExtendedCodeAreaLayoutProfile defaultLayoutProfile;
    private final ExtendedCodeAreaThemeProfile defaultThemeProfile;
    private final CodeAreaColorsProfile defaultColorProfile;
//    private final InstanceContent content = new InstanceContent();

    private BinEdToolbarPanel toolbarPanel;
    private BinaryStatusPanel statusPanel;
    private BinaryStatusApi binaryStatus;
    private TextEncodingStatusApi encodingStatus;
    private CharsetChangeListener charsetChangeListener = null;
    private GoToPositionAction goToPositionAction;
    private AbstractAction showHeaderAction;
    private AbstractAction showRowNumbersAction;
    private EncodingsHandler encodingsHandler;
    private ValuesPanel valuesPanel = null;
    private JScrollPane valuesPanelScrollPane = null;
    private boolean valuesPanelVisible = false;
    private final SearchAction searchAction;

    private boolean opened = false;
    private boolean modified = false;
    private FileHandlingMode fileHandlingMode = DEFAULT_FILE_HANDLING_MODE;
    protected String displayName;
    private long documentOriginalSize;
    private String fileName;

    public BinEdComponentPanel(BlueJ bluej) {
        initComponents();

        preferences = new BinaryEditorPreferences(new BlueJPreferencesWrapper(bluej));

        codeArea = new ExtCodeArea();
        codeArea.setPainter(new ExtendedHighlightNonAsciiCodeAreaPainter(codeArea));
        codeArea.setCodeFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        codeArea.getCaret().setBlinkRate(300);
        defaultLayoutProfile = codeArea.getLayoutProfile();
        defaultThemeProfile = codeArea.getThemeProfile();
        defaultColorProfile = codeArea.getColorsProfile();

        undoHandler = new CodeAreaUndoHandler(codeArea);
        toolbarPanel = new BinEdToolbarPanel(preferences, codeArea, undoHandler);
        toolbarPanel.setSaveAction(this::saveFileButtonActionPerformed);
        statusPanel = new BinaryStatusPanel();
        codeAreaPanel.add(toolbarPanel, BorderLayout.NORTH);
        registerEncodingStatus(statusPanel);
        encodingsHandler = new EncodingsHandler();
        encodingsHandler.init();
        encodingsHandler.setParentComponent(this);
        encodingsHandler.setTextEncodingStatus(new TextEncodingStatusApi() {
            @Override
            public String getEncoding() {
                return encodingStatus.getEncoding();
            }

            @Override
            public void setEncoding(String encodingName) {
                codeArea.setCharset(Charset.forName(encodingName));
                encodingStatus.setEncoding(encodingName);
                preferences.getEncodingPreferences().setSelectedEncoding(encodingName);
                charsetChangeListener.charsetChanged();
            }
        });

        getSegmentsRepository();
        setNewData();
        CodeAreaOperationCommandHandler commandHandler = new CodeAreaOperationCommandHandler(codeArea, undoHandler);
        codeArea.setCommandHandler(commandHandler);

        initialLoadFromPreferences();

        codeAreaPanel.add(codeArea, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        registerBinaryStatus(statusPanel);
        goToPositionAction = new GoToPositionAction(codeArea);
        goToPositionAction.setParentComponent(this);
        showHeaderAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtendedCodeAreaLayoutProfile layoutProfile = codeArea.getLayoutProfile();
                if (layoutProfile == null) {
                    throw new IllegalStateException();
                }
                boolean showHeader = layoutProfile.isShowHeader();
                layoutProfile.setShowHeader(!showHeader);
                codeArea.setLayoutProfile(layoutProfile);
            }
        };
        showRowNumbersAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ExtendedCodeAreaLayoutProfile layoutProfile = codeArea.getLayoutProfile();
                if (layoutProfile == null) {
                    throw new IllegalStateException();
                }
                boolean showRowPosition = layoutProfile.isShowRowPosition();
                layoutProfile.setShowRowPosition(!showRowPosition);
                codeArea.setLayoutProfile(layoutProfile);
            }
        };

        codeArea.setComponentPopupMenu(new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                JPopupMenu popupMenu = createContextMenu(x, y);
                popupMenu.show(invoker, x, y);
            }
        });

        toolbarPanel.applyFromCodeArea();

        undoHandler.addUndoUpdateListener(new BinaryDataUndoUpdateListener() {
            @Override
            public void undoCommandPositionChanged() {
                codeArea.repaint();
                toolbarPanel.updateUndoState();
                updateCurrentDocumentSize();
                updateModified();
            }

            @Override
            public void undoCommandAdded(final BinaryDataCommand command) {
                toolbarPanel.updateUndoState();
                updateCurrentDocumentSize();
                updateModified();
            }
        });
        toolbarPanel.updateUndoState();

        searchAction = new SearchAction(codeArea, codeAreaPanel);
        codeArea.addDataChangedListener(() -> {
            searchAction.codeAreaDataChanged();
            updateCurrentDocumentSize();
        });

        toolbarPanel.applyFromCodeArea();

        getActionMap().put(ACTION_CLIPBOARD_COPY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copy();
            }
        });
        getActionMap().put(ACTION_CLIPBOARD_CUT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.cut();
            }
        });
        getActionMap().put(ACTION_CLIPBOARD_PASTE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.paste();
            }
        });

        codeArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getModifiersEx() == ActionUtils.getMetaMask()) {
                    int keyCode = keyEvent.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_F: {
                            searchAction.actionPerformed(new ActionEvent(this, 0, ""));
                            searchAction.switchReplaceMode(BinarySearchPanel.SearchOperation.FIND);
                            break;
                        }
                        case KeyEvent.VK_G: {
                            goToPositionAction.actionPerformed(new ActionEvent(this, 0, ""));
                            break;
                        }
                    }
                }
            }
        });
    }

    public void registerBinaryStatus(BinaryStatusApi binaryStatusApi) {
        this.binaryStatus = binaryStatusApi;
        codeArea.addCaretMovedListener((CodeAreaCaretPosition caretPosition) -> {
            binaryStatus.setCursorPosition(caretPosition);
        });
        codeArea.addSelectionChangedListener(() -> {
            binaryStatus.setSelectionRange(codeArea.getSelection());
        });

        codeArea.addEditModeChangedListener(binaryStatus::setEditMode);
        binaryStatus.setEditMode(codeArea.getEditMode(), codeArea.getActiveOperation());

        ((BinaryStatusPanel) binaryStatus).setController(new BinaryStatusPanel.Controller() {
            @Override
            public void changeEditOperation(EditOperation editOperation) {
                codeArea.setEditOperation(editOperation);
            }

            @Override
            public void changeCursorPosition() {
                goToPositionAction.actionPerformed(new ActionEvent(BinEdComponentPanel.this, 0, ""));
            }

            @Override
            public void cycleEncodings() {
                if (encodingsHandler != null) {
                    encodingsHandler.cycleEncodings();
                }
            }

            @Override
            public void encodingsPopupEncodingsMenu(MouseEvent mouseEvent) {
                if (encodingsHandler != null) {
                    encodingsHandler.popupEncodingsMenu(mouseEvent);
                }
            }

            @Override
            public void changeMemoryMode(BinaryStatusApi.MemoryMode memoryMode) {
                FileHandlingMode newHandlingMode = memoryMode == BinaryStatusApi.MemoryMode.DELTA_MODE ? FileHandlingMode.DELTA : FileHandlingMode.MEMORY;
                if (newHandlingMode != fileHandlingMode) {
                    // TODO
                    // fileApi.switchFileHandlingMode(newHandlingMode);
                    switchFileHandlingMode(newHandlingMode);
                    preferences.getEditorPreferences().setFileHandlingMode(newHandlingMode);
                }
            }
        });
    }

    private void switchShowValuesPanel(boolean showValuesPanel) {
        if (showValuesPanel) {
            showValuesPanel();
        } else {
            hideValuesPanel();
        }
    }

    private void switchFileHandlingMode(FileHandlingMode newHandlingMode) {
        if (newHandlingMode != fileHandlingMode) {
            // Switch memory mode
            Object dataObject = null;
            if (dataObject != null) {
                // If document is connected to file, attempt to release first if modified and then simply reload
                if (isModified()) {
                    if (releaseFile()) {
                        fileHandlingMode = newHandlingMode;
//                        openDataObject(dataObject);
                        codeArea.clearSelection();
                        codeArea.setCaretPosition(0);
                    }
                } else {
                    fileHandlingMode = newHandlingMode;
//                    openDataObject(dataObject);
                }
            } else {
                // If document unsaved in memory, switch data in code area
                if (codeArea.getContentData() instanceof DeltaDocument) {
                    BinaryData oldData = codeArea.getContentData();
                    PagedData data = new PagedData();
                    data.insert(0, codeArea.getContentData());
                    codeArea.setContentData(data);
                    oldData.dispose();
                } else {
                    BinaryData oldData = codeArea.getContentData();
                    DeltaDocument document = segmentsRepository.createDocument();
                    document.insert(0, oldData);
                    codeArea.setContentData(document);
                    oldData.dispose();
                }
                undoHandler.clear();
                codeArea.notifyDataChanged();
                updateCurrentMemoryMode();
                fileHandlingMode = newHandlingMode;
            }
            fileHandlingMode = newHandlingMode;
        }
    }

    public void registerEncodingStatus(TextEncodingStatusApi encodingStatusApi) {
        this.encodingStatus = encodingStatusApi;
        setCharsetChangeListener(() -> {
            String selectedEncoding = codeArea.getCharset().name();
            encodingStatus.setEncoding(selectedEncoding);
        });
    }

    public void setCharsetChangeListener(CharsetChangeListener charsetChangeListener) {
        this.charsetChangeListener = charsetChangeListener;
    }

    public boolean canClose() {
        if (!modified) {
            return true;
        }

        final Component parent = null; //WindowManager.getDefault().getMainWindow();
        final Object[] options = new Object[]{"Save", "Discard", "Cancel"};
        final String message = "File " + displayName + " is modified. Save?";
        final int choice = JOptionPane.showOptionDialog(parent, message, "Question", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, JOptionPane.YES_OPTION);
        if (choice == JOptionPane.CANCEL_OPTION) {
            return false;
        }

        if (choice == JOptionPane.YES_OPTION) {
            try {
                saveFile();
            } catch (IOException ex) {
                Logger.getLogger(BinEdComponentPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    public boolean isModified() {
        return undoHandler.getCommandPosition() != undoHandler.getSyncPoint();
    }

    void setModified(boolean modified) {
        this.modified = modified;
        toolbarPanel.updateUndoState();
        toolbarPanel.updateModified(modified);
    }

    private void setNewData() {
        if (fileHandlingMode == FileHandlingMode.DELTA) {
            codeArea.setContentData(segmentsRepository.createDocument());
        } else {
            codeArea.setContentData(new PagedData());
        }
    }

    /**
     * Attempts to release current file and warn if document was modified.
     *
     * @return true if successful
     */
    public boolean releaseFile() {

        if (fileName == null) {
            return true;
        }
        while (isModified()) {
            Object[] options = {
                "Save",
                "Discard",
                "Cancel"
            };
            int result = JOptionPane.showOptionDialog(this,
                    "Document was modified! Do you wish to save it?",
                    "Save File?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            if (result == JOptionPane.NO_OPTION) {
                return true;
            }
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                return false;
            }

            try {
                saveFile();
            } catch (IOException ex) {
                Logger.getLogger(BinEdComponentPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public void openFile(String fileName) throws IOException {
        this.fileName = fileName;
        openFileInt(new File(fileName));
    }

    public void openFile(File file) throws IOException {
        this.fileName = file.getName();
        openFileInt(file);
    }

    private void openFileInt(File file) throws IOException {
        boolean editable = file.canWrite();
        BinaryData oldData = codeArea.getContentData();
        if (fileHandlingMode == FileHandlingMode.MEMORY) {
            EditableBinaryData data = new ByteArrayEditableData();
            data.loadFromStream(new FileInputStream(file));
            codeArea.setContentData(data);
            oldData.dispose();
        } else {
            FileDataSource fileSource = segmentsRepository.openFileSource(file, editable ? FileDataSource.EditMode.READ_WRITE : FileDataSource.EditMode.READ_ONLY);
            DeltaDocument document = segmentsRepository.createDocument(fileSource);
            codeArea.setContentData(document);
            oldData.dispose();
        }
        codeArea.setEditMode(editable ? EditMode.EXPANDING : EditMode.READ_ONLY);
        opened = true;
        documentOriginalSize = codeArea.getDataSize();
        updateCurrentDocumentSize();
        updateCurrentMemoryMode();
    }

    public void saveFile(String fileName) throws IOException {
        this.fileName = fileName;
        saveFile();
    }

    public void saveFile() throws IOException {
        BinaryData data = codeArea.getContentData();
        if (fileHandlingMode == FileHandlingMode.MEMORY) {
            segmentsRepository.saveDocument((DeltaDocument) data);
        } else {
            File file = new File(fileName);
            data.saveToStream(new FileOutputStream(file));
        }
        undoHandler.setSyncPoint();
        setModified(false);
        documentOriginalSize = codeArea.getDataSize();
        updateCurrentDocumentSize();
        updateCurrentMemoryMode();
    }

    private void updateCurrentDocumentSize() {
        long dataSize = codeArea.getDataSize();
        binaryStatus.setCurrentDocumentSize(dataSize, documentOriginalSize);
    }

    @Nonnull
    public FileHandlingMode getFileHandlingMode() {
        return fileHandlingMode;
    }

    public void setFileHandlingMode(FileHandlingMode fileHandlingMode) {
        this.fileHandlingMode = fileHandlingMode;
    }

    private void updateCurrentMemoryMode() {
        BinaryStatusApi.MemoryMode memoryMode = BinaryStatusApi.MemoryMode.RAM_MEMORY;
        if (codeArea.getEditMode() == EditMode.READ_ONLY) {
            memoryMode = BinaryStatusApi.MemoryMode.READ_ONLY;
        } else if (codeArea.getContentData() instanceof DeltaDocument) {
            memoryMode = BinaryStatusApi.MemoryMode.DELTA_MODE;
        }

        if (binaryStatus != null) {
            binaryStatus.setMemoryMode(memoryMode);
        }
    }

    private void updateModified() {
        setModified(undoHandler.getSyncPoint() != undoHandler.getCommandPosition());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codeAreaPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        codeAreaPanel.setLayout(new java.awt.BorderLayout());
        add(codeAreaPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel codeAreaPanel;
    // End of variables declaration//GEN-END:variables

    public void componentOpened() {
        codeArea.requestFocus();
    }

    public void componentClosed() {
        closeData();
    }

    private void closeData() {
        BinaryData data = codeArea.getContentData();
        codeArea.setContentData(new ByteArrayData());
        if (data instanceof DeltaDocument) {
            FileDataSource fileSource = ((DeltaDocument) data).getFileSource();
            data.dispose();
            segmentsRepository.detachFileSource(fileSource);
            segmentsRepository.closeFileSource(fileSource);
        } else {
            if (data != null) {
                data.dispose();
            }
        }
    }

    public static synchronized SegmentsRepository getSegmentsRepository() {
        if (segmentsRepository == null) {
            segmentsRepository = new SegmentsRepository();
        }

        return segmentsRepository;
    }

    @Nonnull
    private JPopupMenu createContextMenu(int x, int y) {
        final JPopupMenu result = new JPopupMenu();

        BasicCodeAreaZone positionZone = codeArea.getPainter().getPositionZone(x, y);

        switch (positionZone) {
            case TOP_LEFT_CORNER:
            case HEADER: {
                result.add(createShowHeaderMenuItem());
                result.add(createPositionCodeTypeMenuItem());
                break;
            }
            case ROW_POSITIONS: {
                result.add(createShowRowPositionMenuItem());
                result.add(createPositionCodeTypeMenuItem());
                result.add(new JSeparator());
                result.add(createGoToMenuItem());

                break;
            }
            default: {
                final JMenuItem cutMenuItem = new JMenuItem("Cut");
                cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionUtils.getMetaMask()));
                cutMenuItem.setEnabled(codeArea.hasSelection() && codeArea.isEditable());
                cutMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.cut();
                    result.setVisible(false);
                });
                result.add(cutMenuItem);

                final JMenuItem copyMenuItem = new JMenuItem("Copy");
                copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionUtils.getMetaMask()));
                copyMenuItem.setEnabled(codeArea.hasSelection());
                copyMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.copy();
                    result.setVisible(false);
                });
                result.add(copyMenuItem);

                final JMenuItem copyAsCodeMenuItem = new JMenuItem("Copy as Code");
                copyAsCodeMenuItem.setEnabled(codeArea.hasSelection());
                copyAsCodeMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.copyAsCode();
                    result.setVisible(false);
                });
                result.add(copyAsCodeMenuItem);

                final JMenuItem pasteMenuItem = new JMenuItem("Paste");
                pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionUtils.getMetaMask()));
                pasteMenuItem.setEnabled(codeArea.canPaste() && codeArea.isEditable());
                pasteMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.paste();
                    result.setVisible(false);
                });
                result.add(pasteMenuItem);

                final JMenuItem pasteFromCodeMenuItem = new JMenuItem("Paste from Code");
                pasteFromCodeMenuItem.setEnabled(codeArea.canPaste() && codeArea.isEditable());
                pasteFromCodeMenuItem.addActionListener((ActionEvent e) -> {
                    try {
                        codeArea.pasteFromCode();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(codeArea, ex.getMessage(), "Unable to Paste Code", JOptionPane.ERROR_MESSAGE);
                    }
                    result.setVisible(false);
                });
                result.add(pasteFromCodeMenuItem);

                final JMenuItem deleteMenuItem = new JMenuItem("Delete");
                deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                deleteMenuItem.setEnabled(codeArea.hasSelection() && codeArea.isEditable());
                deleteMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.delete();
                    result.setVisible(false);
                });
                result.add(deleteMenuItem);
                result.addSeparator();

                final JMenuItem selectAllMenuItem = new JMenuItem("Select All");
                selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionUtils.getMetaMask()));
                selectAllMenuItem.addActionListener((ActionEvent e) -> {
                    codeArea.selectAll();
                    result.setVisible(false);
                });
                result.add(selectAllMenuItem);
                result.addSeparator();

                JMenuItem goToMenuItem = createGoToMenuItem();
                result.add(goToMenuItem);

                final JMenuItem findMenuItem = new JMenuItem("Find...");
                findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionUtils.getMetaMask()));
                findMenuItem.addActionListener((ActionEvent e) -> {
                    searchAction.actionPerformed(e);
                    searchAction.switchReplaceMode(BinarySearchPanel.SearchOperation.FIND);
                });
                result.add(findMenuItem);

                final JMenuItem replaceMenuItem = new JMenuItem("Replace...");
                replaceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionUtils.getMetaMask()));
                replaceMenuItem.setEnabled(codeArea.isEditable());
                replaceMenuItem.addActionListener((ActionEvent e) -> {
                    searchAction.actionPerformed(e);
                    searchAction.switchReplaceMode(BinarySearchPanel.SearchOperation.REPLACE);
                });
                result.add(replaceMenuItem);
            }
        }

        result.addSeparator();

        switch (positionZone) {
            case TOP_LEFT_CORNER:
            case HEADER:
            case ROW_POSITIONS: {
                break;
            }
            default: {
                JMenu showMenu = new JMenu("Show");
                JMenuItem showHeader = createShowHeaderMenuItem();
                showMenu.add(showHeader);
                JMenuItem showRowPosition = createShowRowPositionMenuItem();
                showMenu.add(showRowPosition);
                result.add(showMenu);
            }
        }

        final JMenuItem optionsMenuItem = new JMenuItem("Options...");
        optionsMenuItem.addActionListener((ActionEvent e) -> {
            final BinEdOptionsPanelBorder optionsPanelWrapper = new BinEdOptionsPanelBorder();
            BinEdOptionsPanel optionsPanel = optionsPanelWrapper.getOptionsPanel();
            optionsPanel.setPreferences(preferences);
            optionsPanel.loadFromPreferences();
            updateApplyOptions(optionsPanel);
            OptionsControlPanel optionsControlPanel = new OptionsControlPanel();
            JPanel dialogPanel = WindowUtils.createDialogPanel(optionsPanelWrapper, optionsControlPanel);
            DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, WindowUtils.getWindow(this), "Options", Dialog.ModalityType.APPLICATION_MODAL);
            optionsControlPanel.setHandler((OptionsControlHandler.ControlActionType actionType) -> {
                if (actionType != OptionsControlHandler.ControlActionType.CANCEL) {
                    try {
                        optionsPanel.applyToOptions();
                        if (actionType == OptionsControlHandler.ControlActionType.SAVE) {
                            optionsPanel.saveToPreferences();
                        }
                        applyOptions(optionsPanel);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialogPanel, ex.getMessage());
                    }
                    codeArea.repaint();
                }

                dialog.close();
            });
            dialog.getWindow().setSize(650, 460);
            dialog.showCentered(this);
            dialog.dispose();
        });
        result.add(optionsMenuItem);

        switch (positionZone) {
            case TOP_LEFT_CORNER:
            case HEADER:
            case ROW_POSITIONS: {
                break;
            }
            default: {
                result.addSeparator();
                final JMenuItem aboutMenuItem = new JMenuItem("About...");
                aboutMenuItem.addActionListener((ActionEvent e) -> {
                    AboutPanel aboutPanel = new AboutPanel();
                    aboutPanel.setupFields();
                    CloseControlPanel closeControlPanel = new CloseControlPanel();
                    JPanel dialogPanel = WindowUtils.createDialogPanel(aboutPanel, closeControlPanel);
                    DialogWrapper dialog = WindowUtils.createDialog(dialogPanel, WindowUtils.getWindow(this), "About Plugin", Dialog.ModalityType.APPLICATION_MODAL);
                    closeControlPanel.setHandler(() -> {
                        dialog.close();
                    });
                    //            dialog.setSize(650, 460);
                    dialog.showCentered(WindowUtils.getWindow(this));
                });
                result.add(aboutMenuItem);
            }
        }

        return result;
    }

    @Nonnull
    private JMenuItem createGoToMenuItem() {
        final JMenuItem goToMenuItem = new JMenuItem("Go To...");
        goToMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtils.getMetaMask()));
        goToMenuItem.addActionListener(goToPositionAction);
        return goToMenuItem;
    }

    @Nonnull
    private JMenuItem createShowHeaderMenuItem() {
        final JCheckBoxMenuItem showHeader = new JCheckBoxMenuItem("Show Header");
        showHeader.setSelected(codeArea.getLayoutProfile().isShowHeader());
        showHeader.addActionListener(showHeaderAction);
        return showHeader;
    }

    @Nonnull
    private JMenuItem createShowRowPositionMenuItem() {
        final JCheckBoxMenuItem showRowPosition = new JCheckBoxMenuItem("Show Row Position");
        showRowPosition.setSelected(codeArea.getLayoutProfile().isShowRowPosition());
        showRowPosition.addActionListener(showRowNumbersAction);
        return showRowPosition;
    }

    @Nonnull
    private JMenuItem createPositionCodeTypeMenuItem() {
        JMenu menu = new JMenu("Position Code Type");
        PositionCodeType codeType = codeArea.getPositionCodeType();

        final JRadioButtonMenuItem octalCodeTypeMenuItem = new JRadioButtonMenuItem("Octal");
        octalCodeTypeMenuItem.setSelected(codeType == PositionCodeType.OCTAL);
        octalCodeTypeMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setPositionCodeType(PositionCodeType.OCTAL);
                preferences.getCodeAreaPreferences().setPositionCodeType(PositionCodeType.OCTAL);
            }
        });
        menu.add(octalCodeTypeMenuItem);

        final JRadioButtonMenuItem decimalCodeTypeMenuItem = new JRadioButtonMenuItem("Decimal");
        decimalCodeTypeMenuItem.setSelected(codeType == PositionCodeType.DECIMAL);
        decimalCodeTypeMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setPositionCodeType(PositionCodeType.DECIMAL);
                preferences.getCodeAreaPreferences().setPositionCodeType(PositionCodeType.DECIMAL);
            }
        });
        menu.add(decimalCodeTypeMenuItem);

        final JRadioButtonMenuItem hexadecimalCodeTypeMenuItem = new JRadioButtonMenuItem("Hexadecimal");
        hexadecimalCodeTypeMenuItem.setSelected(codeType == PositionCodeType.HEXADECIMAL);
        hexadecimalCodeTypeMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.setPositionCodeType(PositionCodeType.HEXADECIMAL);
                preferences.getCodeAreaPreferences().setPositionCodeType(PositionCodeType.HEXADECIMAL);
            }
        });
        menu.add(hexadecimalCodeTypeMenuItem);

        return menu;
    }

    private void updateApplyOptions(BinEdApplyOptions applyOptions) {
        CodeAreaOptionsImpl.applyFromCodeArea(applyOptions.getCodeAreaOptions(), codeArea);
        applyOptions.getEncodingOptions().setSelectedEncoding(((CharsetCapable) codeArea).getCharset().name());

        EditorOptions editorOptions = applyOptions.getEditorOptions();
        editorOptions.setShowValuesPanel(valuesPanelVisible);
        editorOptions.setFileHandlingMode(fileHandlingMode);
        editorOptions.setEnterKeyHandlingMode(((CodeAreaOperationCommandHandler) codeArea.getCommandHandler()).getEnterKeyHandlingMode());

        // TODO applyOptions.getStatusOptions().loadFromPreferences(preferences.getStatusPreferences());
    }

    private void applyOptions(BinEdApplyOptions applyOptions) {
        CodeAreaOptionsImpl.applyToCodeArea(applyOptions.getCodeAreaOptions(), codeArea);

        ((CharsetCapable) codeArea).setCharset(Charset.forName(applyOptions.getEncodingOptions().getSelectedEncoding()));
        encodingsHandler.setEncodings(applyOptions.getEncodingOptions().getEncodings());

        EditorOptions editorOptions = applyOptions.getEditorOptions();
        switchShowValuesPanel(editorOptions.isShowValuesPanel());
        ((CodeAreaOperationCommandHandler) codeArea.getCommandHandler()).setEnterKeyHandlingMode(editorOptions.getEnterKeyHandlingMode());
        switchFileHandlingMode(editorOptions.getFileHandlingMode());

        StatusOptions statusOptions = applyOptions.getStatusOptions();
        statusPanel.setStatusOptions(statusOptions);
        toolbarPanel.applyFromCodeArea();

        CodeAreaLayoutOptions layoutOptions = applyOptions.getLayoutOptions();
        int selectedLayoutProfile = layoutOptions.getSelectedProfile();
        if (selectedLayoutProfile >= 0) {
            codeArea.setLayoutProfile(layoutOptions.getLayoutProfile(selectedLayoutProfile));
        } else {
            codeArea.setLayoutProfile(defaultLayoutProfile);
        }

        CodeAreaThemeOptions themeOptions = applyOptions.getThemeOptions();
        int selectedThemeProfile = themeOptions.getSelectedProfile();
        if (selectedThemeProfile >= 0) {
            codeArea.setThemeProfile(themeOptions.getThemeProfile(selectedThemeProfile));
        } else {
            codeArea.setThemeProfile(defaultThemeProfile);
        }

        CodeAreaColorOptions colorOptions = applyOptions.getColorOptions();
        int selectedColorProfile = colorOptions.getSelectedProfile();
        if (selectedColorProfile >= 0) {
            codeArea.setColorsProfile(colorOptions.getColorsProfile(selectedColorProfile));
        } else {
            codeArea.setColorsProfile(defaultColorProfile);
        }
    }

    public void showValuesPanel() {
        if (!valuesPanelVisible) {
            valuesPanelVisible = true;
            if (valuesPanel == null) {
                valuesPanel = new ValuesPanel();
                valuesPanel.setCodeArea(codeArea, undoHandler);
                valuesPanelScrollPane = new JScrollPane(valuesPanel);
                valuesPanelScrollPane.setBorder(null);
            }
            codeAreaPanel.add(valuesPanelScrollPane, BorderLayout.EAST);
            valuesPanel.enableUpdate();
            valuesPanel.updateValues();
            codeAreaPanel.revalidate();
            revalidate();
        }
    }

    public void hideValuesPanel() {
        if (valuesPanelVisible) {
            valuesPanelVisible = false;
            valuesPanel.disableUpdate();
            codeAreaPanel.remove(valuesPanelScrollPane);
            codeAreaPanel.revalidate();
            revalidate();
        }
    }

    public ExtCodeArea getCodeArea() {
        return codeArea;
    }

    private void saveFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            saveFile();
        } catch (IOException ex) {
            Logger.getLogger(BinEdComponentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initialLoadFromPreferences() {
        applyOptions(new BinEdApplyOptions() {
            @Override
            public CodeAreaOptions getCodeAreaOptions() {
                return preferences.getCodeAreaPreferences();
            }

            @Override
            public TextEncodingOptions getEncodingOptions() {
                return preferences.getEncodingPreferences();
            }

            @Override
            public EditorOptions getEditorOptions() {
                return preferences.getEditorPreferences();
            }

            @Override
            public TextFontOptions getFontOptions() {
                return preferences.getFontPreferences();
            }

            @Override
            public StatusOptions getStatusOptions() {
                return preferences.getStatusPreferences();
            }

            @Override
            public CodeAreaLayoutOptions getLayoutOptions() {
                return preferences.getLayoutPreferences();
            }

            @Override
            public CodeAreaColorOptions getColorOptions() {
                return preferences.getColorPreferences();
            }

            @Override
            public CodeAreaThemeOptions getThemeOptions() {
                return preferences.getThemePreferences();
            }
        });

        encodingsHandler.loadFromPreferences(preferences.getEncodingPreferences());
        statusPanel.loadFromPreferences(preferences.getStatusPreferences());
        toolbarPanel.loadFromPreferences();

        fileHandlingMode = preferences.getEditorPreferences().getFileHandlingMode();
    }

    public static interface CharsetChangeListener {

        public void charsetChanged();
    }
}