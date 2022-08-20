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

import bluej.extensions2.BlueJ;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.auxiliary.paged_data.ByteArrayData;
import org.exbin.auxiliary.paged_data.EditableBinaryData;
import org.exbin.auxiliary.paged_data.PagedData;
import org.exbin.auxiliary.paged_data.delta.DeltaDocument;
import org.exbin.auxiliary.paged_data.delta.FileDataSource;
import org.exbin.auxiliary.paged_data.delta.SegmentsRepository;
import org.exbin.bined.CodeAreaUtils;
import org.exbin.bined.EditMode;
import org.exbin.bined.bluej.gui.BinEdComponentFileApi;
import org.exbin.bined.bluej.gui.BinEdComponentPanel;
import org.exbin.bined.operation.swing.CodeAreaUndoHandler;
import org.exbin.bined.operation.undo.BinaryDataUndoHandler;
import org.exbin.bined.swing.extended.ExtCodeArea;
import org.exbin.framework.bined.FileHandlingMode;

/**
 * File editor wrapper using BinEd editor component.
 *
 * @version 0.2.2 2022/08/19
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinEdFile implements BinEdComponentFileApi {

    public static final String ACTION_CLIPBOARD_CUT = "cut-to-clipboard";
    public static final String ACTION_CLIPBOARD_COPY = "copy-to-clipboard";
    public static final String ACTION_CLIPBOARD_PASTE = "paste-from-clipboard";

    private static SegmentsRepository segmentsRepository = null;

    private final BinEdComponentPanel componentPanel;
    private final BinaryDataUndoHandler undoHandlerWrapper;
    private URI fileUri = null;
    private long documentOriginalSize;

    public BinEdFile(BlueJ blueJ) {
        componentPanel = new BinEdComponentPanel(blueJ);
        ExtCodeArea codeArea = componentPanel.getCodeArea();
        undoHandlerWrapper = new CodeAreaUndoHandler(codeArea);
        componentPanel.setFileApi(this);
        componentPanel.setUndoHandler(undoHandlerWrapper);

        getSegmentsRepository();

        ActionMap actionMap = componentPanel.getActionMap();
        actionMap.put(ACTION_CLIPBOARD_COPY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.copy();
            }
        });
        actionMap.put(ACTION_CLIPBOARD_CUT, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.cut();
            }
        });
        actionMap.put(ACTION_CLIPBOARD_PASTE, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codeArea.paste();
            }
        });
    }

    @Nonnull
    public BinEdComponentPanel getComponentPanel() {
        return componentPanel;
    }

    public boolean isModified() {
        return componentPanel.isModified();
    }

    public boolean releaseFile() {
        return componentPanel.releaseFile();
    }

    private void fileSync() {
        documentOriginalSize = componentPanel.getCodeArea().getDataSize();
        undoHandlerWrapper.setSyncPoint();
    }

    public static synchronized SegmentsRepository getSegmentsRepository() {
        if (segmentsRepository == null) {
            segmentsRepository = new SegmentsRepository();
        }

        return segmentsRepository;
    }

    public void openDocument(File file) throws IOException {
        openDocument(file, file.canWrite());
    }
            
    public void openDocument(File file, boolean editable) throws IOException {
        if (!file.isFile()) {
            JOptionPane.showOptionDialog(componentPanel,
                    "File not found",
                    "Unable to load file",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null, null, null);
            return;
        }

        FileHandlingMode fileHandlingMode = componentPanel.getFileHandlingMode();

        BinaryData oldData = componentPanel.getContentData();
        if (fileHandlingMode == FileHandlingMode.DELTA) {
            FileDataSource fileSource = segmentsRepository.openFileSource(file, editable ? FileDataSource.EditMode.READ_WRITE : FileDataSource.EditMode.READ_ONLY);
            DeltaDocument document = segmentsRepository.createDocument(fileSource);
            componentPanel.setContentData(document);
            if (oldData != null) {
                oldData.dispose();
            }
        } else {
            try ( FileInputStream fileStream = new FileInputStream(file)) {
                BinaryData data = componentPanel.getContentData();
                if (!(data instanceof PagedData)) {
                    data = new PagedData();
                    if (oldData != null) {
                        oldData.dispose();
                    }
                }
                ((EditableBinaryData) data).loadFromStream(fileStream);
                componentPanel.setContentData(data);
            }
        }
        fileUri = file.toURI();
        ExtCodeArea codeArea = componentPanel.getCodeArea();
        codeArea.setEditMode(editable ? EditMode.EXPANDING : EditMode.READ_ONLY);
        undoHandlerWrapper.clear();
        fileSync();
    }

    public void openDocument(InputStream stream, boolean editable) throws IOException {
        setNewData();
        EditableBinaryData data = CodeAreaUtils.requireNonNull((EditableBinaryData) componentPanel.getContentData());
        data.loadFromStream(stream);
        ExtCodeArea codeArea = componentPanel.getCodeArea();
        codeArea.setEditMode(editable ? EditMode.EXPANDING : EditMode.READ_ONLY);
        componentPanel.setContentData(data);
    }

    public long getDocumentOriginalSize() {
        return documentOriginalSize;
    }

    public void saveFile() throws IOException {
        BinaryData data = componentPanel.getContentData();
        if (data instanceof DeltaDocument) {
            segmentsRepository.saveDocument((DeltaDocument) data);
        } else {
            File file = new File(fileUri);
            data.saveToStream(new FileOutputStream(file));
        }

        undoHandlerWrapper.getSyncPoint();
        fileSync();
    }

    @Override
    public void closeData() {
        BinaryData data = componentPanel.getContentData();
        componentPanel.setContentData(new ByteArrayData());
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

    @Override
    public void saveDocument() {
        try {
            saveFile();
        } catch (IOException ex) {
            Logger.getLogger(BinEdFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void switchFileHandlingMode(FileHandlingMode newHandlingMode) {
        FileHandlingMode fileHandlingMode = componentPanel.getFileHandlingMode();
        BinaryData contentData = componentPanel.getContentData();
        if (newHandlingMode != fileHandlingMode) {
            // If document unsaved in memory, switch data in code area
            if (contentData instanceof DeltaDocument) {
                BinaryData oldData = componentPanel.getContentData();
                PagedData data = new PagedData();
                data.insert(0, componentPanel.getContentData());
                componentPanel.setContentData(data);
                if (oldData != null) {
                    oldData.dispose();
                }
            } else {
                BinaryData oldData = componentPanel.getContentData();
                DeltaDocument document = segmentsRepository.createDocument();
                if (oldData != null) {
                    document.insert(0, oldData);
                    oldData.dispose();
                }
                componentPanel.setContentData(document);
            }

            componentPanel.getUndoHandler().clear();
            componentPanel.setFileHandlingMode(newHandlingMode);
        }
    }

    @Override
    public boolean isSaveSupported() {
        return true;
    }

    private void setNewData() {
        FileHandlingMode fileHandlingMode = componentPanel.getFileHandlingMode();
        if (fileHandlingMode == FileHandlingMode.DELTA) {
            componentPanel.setContentData(segmentsRepository.createDocument());
        } else {
            componentPanel.setContentData(new PagedData());
        }
        fileUri = null;
        undoHandlerWrapper.clear();
        fileSync();
    }

    public void setModifiedChangeListener(BinEdComponentPanel.ModifiedStateListener modifiedChangeListener) {
        componentPanel.setModifiedChangeListener(modifiedChangeListener);
    }

    public void requestFocus() {
        componentPanel.getCodeArea().requestFocus();
    }
}
