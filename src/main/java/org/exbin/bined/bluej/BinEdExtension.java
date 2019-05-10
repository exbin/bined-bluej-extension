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

import bluej.extensions.*;
import bluej.extensions.event.*;
import bluej.extensions.editor.*;

import java.net.URL;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.event.*;

/**
 * Extended code area component default painter.
 *
 * @version 0.2.0 2019/05/10
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinEdExtension extends Extension implements PackageListener {

    /*
     * When this method is called, the extension may start its work.
     */
    public void startup(BlueJ bluej) {
        // Register a generator for menu items
        bluej.setMenuGenerator(new MenuBuilder());

        // Register a "preferences" panel generator
        Preferences myPreferences = new Preferences(bluej);
        bluej.setPreferenceGenerator(myPreferences);

        // Listen for BlueJ events at the "package" level
        bluej.addPackageListener(this);
    }

    /*
     * A package has been opened. Print the name of the project it is part of.
     * System.out is redirected to the BlueJ debug log file.
     * The location of this file is given in the Help/About BlueJ dialog box.
     */
    @Override
    public void packageOpened(PackageEvent ev) {
        try {
            System.out.println("Project " + ev.getPackage().getProject().getName() + " opened.");
        } catch (ExtensionException e) {
            System.out.println("Project closed by BlueJ");
        }
    }

    /*
     * A package is closing.
     */
    @Override
    public void packageClosing(PackageEvent ev) {
    }

    /*
     * This method must decide if this Extension is compatible with the 
     * current release of the BlueJ Extensions API
     */
    @Override
    public boolean isCompatible() {
        return true;
    }

    /*
     * Returns the version number of this extension
     */
    @Override
    public String getVersion() {
        return ("0.2.0");
    }

    /*
     * Returns the user-visible name of this extension
     */
    @Override
    public String getName() {
        return ("BinEd Binary/Hexadecimal Editor Extension");
    }

    @Override
    public void terminate() {
        System.out.println("Simple extension terminates");
    }

    @Override
    public String getDescription() {
        return "Binary/hexadecimal editor based on BinEd library. Use \"Open as Binary\" action in main menu or in files context menu.\n"
                + "\n"
                + "Features\n"
                + "* Visualize data as numerical (hexadecimal) codes and text representation\n"
                + "* Codes can be also binary, octal or decimal\n"
                + "* Support for Unicode, UTF-8 and other charsets\n"
                + "* Insert and overwrite edit modes\n"
                + "* Searching for text / hexadecimal code with matching highlighting\n"
                + "* Support for undo/redo\n"
                + "* Support for files with size up to exabytes\n"
                + "\n"
                + "License\n"
                + "Apache License, Version 2.0";
    }

    /*
     * Returns a URL where you can find info on this extension.
     */
    @Override
    public URL getURL() {
        try {
            return new URL("https://bined.exbin.org/bluej-extension");
        } catch (Exception ex) {
            System.out.println("Broken URL: Exception=" + ex.getMessage());
            return null;
        }
    }
}

/*
 * This class implements the preference panel behaviour for a BlueJ extension
 */
class Preferences implements PreferenceGenerator {

    private JPanel myPanel;
    private JTextField color;
    private BlueJ bluej;
    public static final String PROFILE_LABEL = "Favorite-Colour";

    // Construct the panel, and initialise it from any stored values
    public Preferences(BlueJ bluej) {
        this.bluej = bluej;
        myPanel = new JPanel();
        myPanel.add(new JLabel("Favorite Colour"));
        color = new JTextField(40);
        myPanel.add(color);
        // Load the default value
        loadValues();
    }

    @Override
    public JPanel getPanel() {
        return myPanel;
    }

    @Override
    public void saveValues() {
        // Save the preference value in the BlueJ properties file
        bluej.setExtensionPropertyString(PROFILE_LABEL, color.getText());
    }

    @Override
    public void loadValues() {
        // Load the property value from the BlueJ properties file, default to an empty string
        color.setText(bluej.getExtensionPropertyString(PROFILE_LABEL, ""));
    }
}

/* This class shows how you can bind different menus to different parts of BlueJ
 * Remember:
 * - getToolsMenuItem, getClassMenuItem and getObjectMenuItem may be called by BlueJ at any time.
 * - They must generate a new JMenuItem each time they are called.
 * - No reference to the JMenuItem should be stored in the extension.
 * - You must be quick in generating your menu.
 */
class MenuBuilder extends MenuGenerator {

    private BPackage curPackage;
    private BClass curClass;
    private BObject curObject;

    @Override
    public JMenuItem getToolsMenuItem(BPackage aPackage) {
        return new JMenuItem(new SimpleAction("Click Tools", "Tools menu:"));
    }

    @Override
    public JMenuItem getClassMenuItem(BClass aClass) {
        JMenu jm = new JMenu("Simple Extension");
        jm.add(new JMenuItem(new SimpleAction("Click Class", "Class menu:")));
        jm.add(new JMenuItem(new EditAction()));
        return jm;
    }

    @Override
    public JMenuItem getObjectMenuItem(BObject anObject) {
        return new JMenuItem(new SimpleAction("Click Object", "Object menu:"));
    }

    // These methods will be called when
    // each of the different menus are about to be invoked.
    @Override
    public void notifyPostToolsMenu(BPackage bp, JMenuItem jmi) {
        System.out.println("Post on Tools menu");
        curPackage = bp;
        curClass = null;
        curObject = null;
    }

    @Override
    public void notifyPostClassMenu(BClass bc, JMenuItem jmi) {
        System.out.println("Post on Class menu");
        curPackage = null;
        curClass = bc;
        curObject = null;
    }

    @Override
    public void notifyPostObjectMenu(BObject bo, JMenuItem jmi) {
        System.out.println("Post on Object menu");
        curPackage = null;
        curClass = null;
        curObject = bo;
    }

    // A utility method which pops up a dialog detailing the objects 
    // involved in the current (SimpleAction) menu invocation.
    private void showCurrentStatus(String header) {
        try {
            if (curObject != null) {
                curClass = curObject.getBClass();
            }
            if (curClass != null) {
                curPackage = curClass.getPackage();
            }

            String msg = header;
            if (curPackage != null) {
                msg += "\nCurrent Package = " + curPackage;
            }
            if (curClass != null) {
                msg += "\nCurrent Class = " + curClass;
            }
            if (curObject != null) {
                msg += "\nCurrent Object = " + curObject;
            }
            JOptionPane.showMessageDialog(null, msg);
        } catch (Exception exc) {
        }
    }

    // A method to add a comment at the end of the current class, using the Editor API
    private void addComment() {
        Editor classEditor = null;
        try {
            classEditor = curClass.getEditor();
        } catch (Exception e) {
        }
        if (classEditor == null) {
            System.out.println("Can't create Editor for " + curClass);
            return;
        }

        int textLen = classEditor.getTextLength();
        TextLocation lastLine = classEditor.getTextLocationFromOffset(textLen);
        lastLine.setColumn(0);
        // The TextLocation now points before the first character of the last line of the current text
        // which we'll assume contains the closing } bracket for the class
        classEditor.setText(lastLine, lastLine, "// Comment added by SimpleExtension\n");
    }

    // The nested class that instantiates the different (simple) menus.
    class SimpleAction extends AbstractAction {

        private String msgHeader;

        public SimpleAction(String menuName, String msg) {
            putValue(AbstractAction.NAME, menuName);
            msgHeader = msg;
        }

        @Override
        public void actionPerformed(ActionEvent anEvent) {
            showCurrentStatus(msgHeader);
        }
    }

    // And the nested class which implements the editor interaction menu
    class EditAction extends AbstractAction {

        public EditAction() {
            putValue(AbstractAction.NAME, "Add comment");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            addComment();
        }
    }
}
