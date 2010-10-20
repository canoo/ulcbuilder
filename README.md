ULCBuilder
==========

ULCBuilder is a Groovy based builder for ULC powered applications.

ULCBuilder and Groovy makes possible the use of a *DSL-like* language
to defining the UI of an application, in the same vein as SwingBuilder
does for Swing.

ULCBuilder is extensible via builder plugins. There are two officially
supported plugins at the moment: miglayout and formlayout. These plugins
allow the usage of two powerful layout managers. Builder plugins can
be used by simply adding their respective jars to the application's
classpath. If you rely on Maven or Ivy then you can configure the following
repository to pull ULCBuilder and its dependencies

  https://ci.canoo.com/nexus/content/repositories/public-releases

group:    **com.canoo.ulc**
artifact: **ulcbuilder-core**
version:  **1.0**

group:    **com.canoo.ulc**
artifact: **ulcbuilder-miglayout**
version:  **1.0**

group:    **com.canoo.ulc**
artifact: **ulcbuilder-formlayout**
version:  **1.0**

Usage
=====

ULCBuilder works in the same as SwingBuilder does. This means you typically
create an instance of the builder and use Groovy's closures and Map notation
to define which components should be created and with which properties. The
following example demonstrates how a simple frame with a form-like view can be
created using the builder:

    package com.canoo.groovy.ulc

    import com.ulcjava.applicationframework.application.SingleFrameApplication
    import com.ulcjava.base.application.ULCComponent
    import com.ulcjava.base.application.ULCFrame

    class SampleApplication extends SingleFrameApplication {
        private final ULCBuilder builder = new ULCBuilder()
    
        @Override
        protected ULCComponent createStartupMainContent</th></tr>
            builder.migLayoutPane {
                label 'Label1'
                textField constraints: 'span 2, growx, wrap'
                label 'Label2'
                textField constraints: 'wrap'
                label 'Label3'
                textField()
                button 'Click!', constraints: 'right'
            }
        }
    
        @Override
        protected void initFrame(ULCFrame frame) {
            super.initFrame(frame)
            frame.setLocationRelativeTo(null)
        }
    }

Let's review the code part by part. If you're familiar with ULC you probably
know that it ships with an Application Framework that provides a set of classes
that help writing applications in a more efficient way. `SingleFrameApplication`
is one of such classes. You're only required to provide an implementation for the
`createStartupMainComntent()` method. This is where the builder will be used to
create the components that conform the application. This example relies on one
of the builder plugins to be available in the classpath, we're refering to the
ulcbuilder-miglayout plugin.

Granted the example is quite trivial but compare what would be the alternate code
if the example were written in plain java

    ULCMigLayoutPane p = new ULCMigLayoutPane();
    p.add(new ULCLabel("Label1"));
    p.add(new ULCTextField(), "span 2, growx, wrap");
    p.add(new ULCLabel("Label2"));
    p.add(new ULCTextField(), "wrap");
    p.add(new ULCLabel("Label3"));
    p.add(new ULCTextField());
    p.add(new ULCButton("Click!"), "right");
    return p;

With the builder you save a few characters (parenthesis and semi-colons), but you
also gain cleaner code. It's easier to determine the hierarchy of components at
first glance whereas in the Java version you'll quickly get container adds all
over the place; not counting code factorizations into private methods to hide that
complexity.

Builder Nodes
=============

The builder should allow you to build any component available in ULC Core. Each node
follows a naming convention that is easy to remember:

1. Take the name of an ULC component and drop the 'ULC' prefix
   example: ULCButton -> Button
2. Uncapitalize the first character
   example: Button -> button

And that's all! There are additional nodes that map to classes that do not follow this
pattern, but the list is very short. The following section spells out all nodes available
in the latest version of the builder. They are shown grouped by behavior but the grouping
has no real impact on how they are used.

<table>
  <tr>
    <th>Node</th>
    <th>Class</th>
  </tr>
  <tr><th span="2">SupportNodes</th></tr>
  <tr><td>action</td><td>DefaultAction</td></tr>
  <tr><td>actions</td><td>List</td></tr>
  <tr><td>map</td><td>Map</td></tr>
  <tr><td>icon</td><td>ULCIcon</td></tr>
  <tr><td>buttonGroup</td><td>ULCButtonGroup</td></tr>
  <tr><th colspan="2">Binding</th></tr>
  <tr><td>bind</td><td>FullBinding</td></tr>
  <tr><th colspan="2">PassThruNodes</th></tr>
  <tr><td>widget</td><td>accepts any ULCComponent/td></tr>
  <tr><td>container</td><td>accepts any ULCComponent/td></tr>
  <tr><td>bean</td><td>any class</td></tr>
  <tr><th colspan="2">Windows</th></tr>
  <tr><td>dialog</td><td>ULCDialog</td></tr>
  <tr><td>frame</td><td>ULCFrame</td></tr>
  <tr><td>window</td><td>ULCWindow</td></tr>
  <tr><th colspan="2">ActionButtonWidgets</th></tr>
  <tr><td>button</td><td>ULCButton</td></tr>
  <tr><td>checkBox</td><td>ULCCheckBox</td></tr>
  <tr><td>checkBoxMenuItem</td><td>ULCCheckBoxMenuItem</td></tr>
  <tr><td>menuItem</td><td>ULCMenuItem</td></tr>
  <tr><td>radioButton</td><td>ULCRadioButton</td></tr>
  <tr><td>radioButtonMenuItem</td><td>ULCRadioButtonMenuItem</td></tr>
  <tr><td>toggleButton</td><td>ULCToggleButton</td></tr>
  <tr><th colspan="2">TextWidgets</th></tr>
  <tr><td>label</td><td>ULCLabel</td></tr>
  <tr><td>passwordField</td><td>ULCPasswordField</td></tr>
  <tr><td>textArea</td><td>ULCTextArea</td></tr>
  <tr><td>textField</td><td>ULCTextField</td></tr>
  <tr><th colspan="2">MDIWidgets</th></tr>
  <tr><td>desktopPane</td><td>ULCDesktopPane</td></tr>
  <tr><td>internalFrame</td><td>ULCInternalFrame</td></tr>
  <tr><th colspan="2">BasicWidgets</th></tr>
  <tr><td>comboBox</td><td>ULCComboBox</td></tr>
  <tr><td>list</td><td>ULCList</tr>
  <tr><td>progressBar</td><td>ULCProgressBar</td></tr>
  <tr><td>separator</td><td>ULCSeparator</td></tr>
  <tr><td>scrollBar</td><td>ULCScrollBar</td></tr>
  <tr><td>slider</td><td>ULCSlider</td></tr>
  <tr><td>spinner</td><td>ULCSpinner</td></tr>
  <tr><td>tree</td><td>ULCTree</td></tr>
  <tr><th colspan="2">MenuWidgets</th></tr>
  <tr><td>menu</td><td>ULCMenu</td></tr>
  <tr><td>menuBar</td><td>ULCMenuBar</td></tr>
  <tr><td>popupMenu</td><td>ULCPopupMenu</td></tr>
  <tr><th colspan="2">Containers</th></tr>
  <tr><td>boxPane</td><td>ULCBoxPane</td></tr>
  <tr><td>scrollPane</td><td>ULCScrollPane</td></tr>
  <tr><td>splitPane</td><td>ULCSplitPane</td></tr>
  <tr><td>cardPane</td><td>ULCCardPane</td></tr>
  <tr><td>tabbedPane</td><td>ULCTabbedPane</td></tr>
  <tr><td>toolBar</td><td>ULCToolBar</td></tr>
  <tr><td>layeredPane</td><td>ULCLayeredPane</td></tr>
  <tr><th colspan="2">DataModels</th></tr> 
  <tr><td>spinnerDateModel</td><td>ULCSpinnerDateModel</td></tr>
  <tr><td>spinnerListModel</td><td>ULCSpinnerListModel</td></tr>
  <tr><td>spinnerNumberModel</td><td>ULCSpinnerNumberModel</td></tr>
  <tr><th colspan="2">TableComponents</th></tr>
  <tr><td>table</td><td>ULCTable</td></tr>
  <tr><td>tableColumn</td><td>ULCTableColumn</td></tr>
  <tr><td>tableModel</td><td>ULCTableModel</td></tr>
  <tr><td>propertyColumn</td><td>ULCTableColumn</td></tr>
  <tr><td>closureColumn</td><td>ULCTableColumn</td></tr>
  <tr><td>tableTree</td><td>ULCTableTree</td></tr>
  <tr><th colspan="2">BasicLayouts</th></tr>
  <tr><td>borderLayoutPane</td><td>ULCBorderLayoutPane</td></tr>
  <tr><td>flowLayoutPane</td><td>ULCFlowLayoutPane</td></tr>
  <tr><td>gridLayoutPane</td><td>ULCGridLayoutPane</td></tr>
  <tr><td>gridBagLayoutPane</td><td>ULCGridBagLayoutPane</td></tr>
  <tr><th colspan="2">BoxLayout</th></tr>
  <tr><td>boxLayoutPane</td><td>ULCBoxLayoutPane</td></tr>
  <tr><td>filler</td><td>ULCFiller</td></tr>
  <tr><td>hglue</td><td>ULCFiller</td></tr>
  <tr><td>hstrut</td><td>ULCFiller</td></tr>
  <tr><td>rigidarea</td><td><ULCFiller/td></tr>
  <tr><td>vglue</td><td>ULCFiller</td></tr>
  <tr><td>vstrut</td><td>ULCFiller</td></tr>
  <tr><th colspan="2">Borders</th></tr>
  <tr><td>lineBorder</td><td>ULCLineBorder</td></tr>
  <tr><td>loweredBevelBorder</td><td>ULCBevelBorderFactory (ULCBevelBorder.LOWERED)</td></tr>
  <tr><td>raisedBevelBorder</td><td>ULCBevelBorderFactory (ULCBevelBorder.RAISED)</td></tr>
  <tr><td>etchedBorder</td><td>ULCEtchedBorderFactory (ULCEtchedBorder.LOWERED)</td></tr>
  <tr><td>loweredEtchedBorder</td><td>ULCEtchedBorderFactory (ULCEtchedBorder.LOWERED)</td></tr>
  <tr><td>raisedEtchedBorder</td><td>ULCEtchedBorderFactory (ULCEtchedBorder.RAISED)</td></tr>
  <tr><td>titledBorder</td><td>ULCTitledBorder</td></tr>
  <tr><td>emptyBorder</td><td>ULCEmptyBorder</td></tr>
  <tr><td>compoundBorder</td><td>ULCCompoundBorder</td></tr>
  <tr><td>matteBorder</td><td>ULCMatteBorder</td></tr>
  <tr><th colspan="2">RenderersAndEditors</th></tr>
  <tr><td>tableCellRenderer</td><td>DefaultTableCellRenderer</td></tr>
  <tr><td>treeCellRenderer</td><td>DefaultTreeCellRenderer</td></tr>
  <tr><td>tableTreeCellRenderer</td><td>DefaultTableTreeCellRenderer</td></tr>
  <tr><td>cellEditor</td><td>DefaultCellEditor</td></tr>
</table>
