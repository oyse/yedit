/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Anton Leherbauer (Wind River Systems)
 *     Sergey Prigogin (Google)
 *******************************************************************************/
package org.dadacoalition.yedit.preferences.tasktag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.eclipse.ui.preferences.IWorkingCopyManager;
import org.eclipse.ui.preferences.WorkingCopyManager;

import org.osgi.service.prefs.BackingStoreException;

/**
 * Abstract options configuration block providing a general implementation for setting up
 * an options configuration page.
 */
public abstract class OptionsConfigurationBlock {
    protected static final String[] FALSE_TRUE = new String[] { "false", "true" };  //$NON-NLS-1$//$NON-NLS-2$
    protected static final String[] TRUE_FALSE = new String[] { "true", "false" };  //$NON-NLS-1$//$NON-NLS-2$


    protected static class ControlData {
        private final String fKey;
        private final String[] fValues;

        public ControlData(String key, String[] values) {
            fKey= key;
            fValues= values;
        }

        public String getKey() {
            return fKey;
        }

        public String getValue(boolean selection) {
            int index= selection ? 0 : 1;
            return fValues[index];
        }

        public String getValue(int index) {
            return fValues[index];
        }

        public int getSelection(String value) {
            if (value != null) {
                for (int i= 0; i < fValues.length; i++) {
                    if (value.equals(fValues[i])) {
                        return i;
                    }
                }
            }
            return fValues.length - 1; // assume the last option is the least severe
        }
    }

    private static final String REBUILD_COUNT_KEY= "preferences_build_requested"; //$NON-NLS-1$

    private static final String SETTINGS_EXPANDED= "expanded"; //$NON-NLS-1$

    protected final ArrayList<Button> fCheckBoxes;
    protected final ArrayList<Combo> fComboBoxes;
    protected final ArrayList<Text> fTextBoxes;
    protected final HashMap<Control, Label> fLabels;
    protected final ArrayList<ExpandableComposite> fExpandedComposites;

    private SelectionListener fSelectionListener;
    private ModifyListener fTextModifyListener;

    protected IStatusChangeListener fContext;
    protected final IProject fProject; // project or null
    protected final String[] fAllKeys;

    private IScopeContext[] fLookupOrder;

    private Shell fShell;

    private final IWorkingCopyManager fManager;
    protected final IWorkbenchPreferenceContainer fContainer;

    private int fRebuildCount; // used to prevent multiple dialogs that ask for a rebuild
    
    private IPreferenceStore fPreferenceStore;

    public OptionsConfigurationBlock(IStatusChangeListener context, IProject project, String[] allKeys,
            IWorkbenchPreferenceContainer container, IPreferenceStore preferenceStore) {
        fPreferenceStore = preferenceStore;
        fContext= context;
        fProject= project;
        fAllKeys= allKeys;
        fContainer= container;
        if (container == null) {
            fManager= new WorkingCopyManager();
        } else {
            fManager= container.getWorkingCopyManager();
        }

        if (fProject != null) {
            fLookupOrder= new IScopeContext[] {
                new ProjectScope(fProject),
                InstanceScope.INSTANCE,
                DefaultScope.INSTANCE
            };
        } else {
            fLookupOrder= new IScopeContext[] {
                InstanceScope.INSTANCE,
                DefaultScope.INSTANCE
            };
        }

        checkIfOptionsComplete(allKeys);

        settingsUpdated();

        fCheckBoxes= new ArrayList<Button>();
        fComboBoxes= new ArrayList<Combo>();
        fTextBoxes= new ArrayList<Text>(2);
        fLabels= new HashMap<Control, Label>();
        fExpandedComposites= new ArrayList<ExpandableComposite>();

        fRebuildCount= getRebuildCount();
    }

    protected final IWorkbenchPreferenceContainer getPreferenceContainer() {
        return fContainer;
    }

    private void checkIfOptionsComplete(String[] allKeys) {
        for (int i= 0; i < allKeys.length; i++) {
            if (fPreferenceStore.getString(allKeys[i]) == null) {
                YEditLog.logError("Preference option missing: " + allKeys[i] + " (" + this.getClass().getName() +')');  //$NON-NLS-1$//$NON-NLS-2$
            }
        }
    }

    private int getRebuildCount() {
        return fManager.getWorkingCopy(DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID)).getInt(REBUILD_COUNT_KEY, 0);
    }

    private void incrementRebuildCount() {
        fRebuildCount++;
        fManager.getWorkingCopy(DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID)).putInt(REBUILD_COUNT_KEY, fRebuildCount);
    }

    protected void settingsUpdated() {
    }


    public void selectOption(String key) {
        Control control= findControl(key);
        if (control != null) {
            if (!fExpandedComposites.isEmpty()) {
                ExpandableComposite expandable= getParentExpandableComposite(control);
                if (expandable != null) {
                    for (int i= 0; i < fExpandedComposites.size(); i++) {
                        ExpandableComposite curr= fExpandedComposites.get(i);
                        curr.setExpanded(curr == expandable);
                    }
                    expandedStateChanged(expandable);
                }
            }
            control.setFocus();
        }
    }

    public boolean hasProjectSpecificOptions(IProject project) {
        if (project != null) {
            IScopeContext projectContext= new ProjectScope(project);
            String[] allKeys= fAllKeys;
            for (int i= 0; i < allKeys.length; i++) {
//                if (allKeys[i].getStoredValue(projectContext, fManager) != null) {
//                    return true;
//                }
            }
        }
        return false;
    }

    protected Shell getShell() {
        return fShell;
    }

    protected void setShell(Shell shell) {
        fShell= shell;
    }

    protected abstract Control createContents(Composite parent);

    protected Button addCheckBox(Composite parent, String label, String key, String[] values, int indent) {
        ControlData data= new ControlData(key, values);

        GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gd.horizontalSpan= 3;
        gd.horizontalIndent= indent;

        Button checkBox= new Button(parent, SWT.CHECK);
        checkBox.setFont(JFaceResources.getDialogFont());
        checkBox.setText(label);
        checkBox.setData(data);
        checkBox.setLayoutData(gd);
        checkBox.addSelectionListener(getSelectionListener());

        makeScrollableCompositeAware(checkBox);

        String currValue= getValue(key);
        checkBox.setSelection(data.getSelection(currValue) == 0);

        fCheckBoxes.add(checkBox);

        return checkBox;
    }

    protected Button addCheckBoxWithLink(Composite parent, String label, String key, String[] values,
            int indent, int widthHint, SelectionListener listener) {
        ControlData data= new ControlData(key, values);

        GridData gd= new GridData(GridData.FILL, GridData.FILL, true, false);
        gd.horizontalSpan= 3;
        gd.horizontalIndent= indent;

        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        layout.numColumns= 2;
        composite.setLayout(layout);
        composite.setLayoutData(gd);

        Button checkBox= new Button(composite, SWT.CHECK);
        checkBox.setFont(JFaceResources.getDialogFont());
        checkBox.setData(data);
        checkBox.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, false, false));
        checkBox.addSelectionListener(getSelectionListener());

        gd= new GridData(GridData.FILL, GridData.CENTER, true, false);
        gd.widthHint= widthHint;

        Link link= new Link(composite, SWT.NONE);
        link.setText(label);
        link.setLayoutData(gd);
        if (listener != null) {
            link.addSelectionListener(listener);
        }

        makeScrollableCompositeAware(link);
        makeScrollableCompositeAware(checkBox);

        String currValue= getValue(key);
        checkBox.setSelection(data.getSelection(currValue) == 0);

        fCheckBoxes.add(checkBox);

        return checkBox;
    }

    protected Button addRadioButton(Composite parent, String label, String key, String[] values, int indent) {
        ControlData data= new ControlData(key, values);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan= 4;
//      gd.horizontalIndent= indent;

        Button radioButton= new Button(parent, SWT.RADIO);
        radioButton.setFont(JFaceResources.getDialogFont());
        radioButton.setText(label);
        radioButton.setData(data);
        radioButton.setLayoutData(gd);
        radioButton.addSelectionListener(getSelectionListener());

//      makeScrollableCompositeAware(radioButton);

        String currValue= getValue(key);
        radioButton.setSelection(data.getSelection(currValue) == 0);

        fCheckBoxes.add(radioButton);

        return radioButton;
    }
    
    protected Combo addComboBox(Composite parent, String label, String key, String[] values,
            String[] valueLabels, int indent) {
        GridData gd= new GridData(GridData.FILL, GridData.CENTER, false, false, 2, 1);
        gd.horizontalIndent= indent;

        Label labelControl= new Label(parent, SWT.LEFT);
        labelControl.setFont(JFaceResources.getDialogFont());
        labelControl.setText(label);
        labelControl.setLayoutData(gd);

        Combo comboBox= newComboControl(parent, key, values, valueLabels);
        comboBox.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        fLabels.put(comboBox, labelControl);

        return comboBox;
    }

    protected Combo addInversedComboBox(Composite parent, String label, String key, String[] values,
            String[] valueLabels, int indent) {
        GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gd.horizontalIndent= indent;
        gd.horizontalSpan= 3;

        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        layout.numColumns= 2;
        composite.setLayout(layout);
        composite.setLayoutData(gd);

        Combo comboBox= newComboControl(composite, key, values, valueLabels);
        comboBox.setFont(JFaceResources.getDialogFont());
        comboBox.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        Label labelControl= new Label(composite, SWT.LEFT | SWT.WRAP);
        labelControl.setText(label);
        labelControl.setLayoutData(new GridData());

        fLabels.put(comboBox, labelControl);
        return comboBox;
    }

    protected Combo newComboControl(Composite composite, String key, String[] values, String[] valueLabels) {
        ControlData data= new ControlData(key, values);

        Combo comboBox= new Combo(composite, SWT.READ_ONLY);
        comboBox.setItems(valueLabels);
        comboBox.setData(data);
        comboBox.addSelectionListener(getSelectionListener());
        comboBox.setFont(JFaceResources.getDialogFont());

        makeScrollableCompositeAware(comboBox);

        String currValue= getValue(key);
        comboBox.select(data.getSelection(currValue));

        fComboBoxes.add(comboBox);
        return comboBox;
    }

    protected Text addTextField(Composite parent, String label, String key, int indent, int widthHint) {
        return addTextField(parent, label, key, indent, widthHint, SWT.NONE);
    }

    protected Text addTextField(Composite parent, String label, String key, int indent, int widthHint,
            int extraStyle) {
        Label labelControl= new Label(parent, SWT.WRAP);
        labelControl.setText(label);
        labelControl.setFont(JFaceResources.getDialogFont());
        GridData data= new GridData();
        data.horizontalIndent= indent;
        labelControl.setLayoutData(data);

        Text textBox= new Text(parent, SWT.BORDER | SWT.SINGLE | extraStyle);
        textBox.setData(key);

        makeScrollableCompositeAware(textBox);

        fLabels.put(textBox, labelControl);

        if (key != null) {
            String currValue= getValue(key);
            if (currValue != null) {
                textBox.setText(currValue);
            }
            textBox.addModifyListener(getTextModifyListener());
        }

        data= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        if (widthHint != 0) {
            data.widthHint= widthHint;
        }
        data.horizontalSpan= 2;
        textBox.setLayoutData(data);

        fTextBoxes.add(textBox);
        return textBox;
    }

    protected Composite addSubsection(Composite parent, String label) {
        Group group= new Group(parent, SWT.SHADOW_NONE);
        group.setText(label);
        GridData data= new GridData(SWT.FILL, SWT.CENTER, true, false);
        group.setLayoutData(data);
        return group;
    }

    protected ScrolledPageContent getParentScrolledComposite(Control control) {
        Control parent= control.getParent();
        while (!(parent instanceof ScrolledPageContent) && parent != null) {
            parent= parent.getParent();
        }
        if (parent instanceof ScrolledPageContent) {
            return (ScrolledPageContent) parent;
        }
        return null;
    }

    protected ExpandableComposite getParentExpandableComposite(Control control) {
        Control parent= control.getParent();
        while (!(parent instanceof ExpandableComposite) && parent != null) {
            parent= parent.getParent();
        }
        if (parent instanceof ExpandableComposite) {
            return (ExpandableComposite) parent;
        }
        return null;
    }

    protected void makeScrollableCompositeAware(Control control) {
        ScrolledPageContent parentScrolledComposite= getParentScrolledComposite(control);
        if (parentScrolledComposite != null) {
            parentScrolledComposite.adaptChild(control);
        }
    }

    protected ExpandableComposite createStyleSection(Composite parent, String label, int nColumns) {
        ExpandableComposite excomposite= new ExpandableComposite(parent, SWT.NONE,
                ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
        excomposite.setText(label);
        excomposite.setExpanded(false);
        excomposite.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
        excomposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, nColumns, 1));
        excomposite.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                expandedStateChanged((ExpandableComposite) e.getSource());
            }
        });
        fExpandedComposites.add(excomposite);
        makeScrollableCompositeAware(excomposite);
        return excomposite;
    }

    protected final void expandedStateChanged(ExpandableComposite expandable) {
        ScrolledPageContent parentScrolledComposite= getParentScrolledComposite(expandable);
        if (parentScrolledComposite != null) {
            parentScrolledComposite.reflow(true);
        }
    }

    protected void restoreSectionExpansionStates(IDialogSettings settings) {
        for (int i= 0; i < fExpandedComposites.size(); i++) {
            ExpandableComposite excomposite= fExpandedComposites.get(i);
            if (settings == null) {
                excomposite.setExpanded(i == 0); // only expand the first node by default
            } else {
                excomposite.setExpanded(settings.getBoolean(SETTINGS_EXPANDED + String.valueOf(i)));
            }
        }
    }

    protected void storeSectionExpansionStates(IDialogSettings settings) {
        for (int i= 0; i < fExpandedComposites.size(); i++) {
            ExpandableComposite curr= fExpandedComposites.get(i);
            settings.put(SETTINGS_EXPANDED + String.valueOf(i), curr.isExpanded());
        }
    }

    protected SelectionListener getSelectionListener() {
        if (fSelectionListener == null) {
            fSelectionListener= new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {}

                public void widgetSelected(SelectionEvent e) {
                    controlChanged(e.widget);
                }
            };
        }
        return fSelectionListener;
    }

    protected ModifyListener getTextModifyListener() {
        if (fTextModifyListener == null) {
            fTextModifyListener= new ModifyListener() {
                
                public void modifyText(ModifyEvent e) {
                    textChanged((Text) e.widget);
                }
            };
        }
        return fTextModifyListener;
    }

    protected void controlChanged(Widget widget) {
        ControlData data= (ControlData) widget.getData();
        String newValue= null;
        if (widget instanceof Button) {
            newValue= data.getValue(((Button) widget).getSelection());
        } else if (widget instanceof Combo) {
            newValue= data.getValue(((Combo) widget).getSelectionIndex());
        } else {
            return;
        }
        String oldValue= setValue(data.getKey(), newValue);
        validateSettings(data.getKey(), oldValue, newValue);
    }

    protected void textChanged(Text textControl) {
        String key= (String) textControl.getData();
        if (key != null) {
            String newValue= textControl.getText();
            String oldValue= setValue(key, newValue);
            validateSettings(key, oldValue, newValue);
        }
    }

    protected boolean checkValue(String key, String value) {
        return value.equals(getValue(key));
    }

    protected String getValue(String key) {
        return fPreferenceStore.getString(key);
    }


    protected boolean getBooleanValue(String key) {
        return Boolean.valueOf(getValue(key)).booleanValue();
    }

    protected String setValue(String key, String value) {
        String oldValue= getValue(key);        
        fPreferenceStore.setValue(key, value);
        return oldValue;
    }

    protected String setValue(String key, boolean value) {
        return setValue(key, String.valueOf(value));
    }

    /**
     * Returns the value as actually stored in the preference store.
     * @param key
     * @return the value as actually stored in the preference store.
     */
    protected String getStoredValue(String key) {
        return fPreferenceStore.getString(key);
    }

    /**
     * Update fields and validate.
     * @param changedKey Key that changed, or null, if all changed.
     */
    protected abstract void validateSettings(String changedKey, String oldValue, String newValue);

    protected String[] getTokens(String text, String separator) {
        StringTokenizer tok= new StringTokenizer(text, separator); 
        int nTokens= tok.countTokens();
        String[] res= new String[nTokens];
        for (int i= 0; i < res.length; i++) {
            res[i]= tok.nextToken().trim();
        }
        return res;
    }




    public boolean performOk() {
        return processChanges(fContainer);
    }

    public boolean performApply() {
        return processChanges(null); // apply directly
    }

    protected boolean processChanges(IWorkbenchPreferenceContainer container) {
        IScopeContext currContext= fLookupOrder[0];

        List<String> changedOptions= new ArrayList<>();
        if (changedOptions.isEmpty()) {
            return true;
        }

        boolean doBuild= false;
        if (container != null) {
            // no need to apply the changes to the original store: will be done by the page container
            if (doBuild) { // post build
                incrementRebuildCount();
                // do a re-index?
//              container.registerUpdateJob(CoreUtility.getBuildJob(fProject));
            }
        } else {
            // apply changes right away
            try {
                fManager.applyChanges();
            } catch (BackingStoreException e) {
                YEditLog.logException(e);
                return false;
            }
            if (doBuild) {
                // do a re-index?
//              CoreUtility.getBuildJob(fProject).schedule();
            }

        }
        return true;
    }


    public void dispose() {
    }

    protected void updateControls() {
        // update the UI
        for (int i= fCheckBoxes.size() - 1; i >= 0; i--) {
            updateCheckBox(fCheckBoxes.get(i));
        }
        for (int i= fComboBoxes.size() - 1; i >= 0; i--) {
            updateCombo(fComboBoxes.get(i));
        }
        for (int i= fTextBoxes.size() - 1; i >= 0; i--) {
            updateText(fTextBoxes.get(i));
        }
    }

    protected void updateCombo(Combo curr) {
        ControlData data= (ControlData) curr.getData();

        String currValue= getValue(data.getKey());
        curr.select(data.getSelection(currValue));
    }

    protected void updateCheckBox(Button curr) {
        ControlData data= (ControlData) curr.getData();

        String currValue= getValue(data.getKey());
        curr.setSelection(data.getSelection(currValue) == 0);
    }

    protected void updateText(Text curr) {
        String key= (String) curr.getData();
        if (key != null) {
            String currValue= getValue(key);
            if (currValue != null) {
                curr.setText(currValue);
            }
        }
    }

    protected Button getCheckBox(String key) {
        for (int i= fCheckBoxes.size() - 1; i >= 0; i--) {
            Button curr= fCheckBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    protected Combo getComboBox(String key) {
        for (int i= fComboBoxes.size() - 1; i >= 0; i--) {
            Combo curr= fComboBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    protected Text getTextControl(String key) {
        for (int i= fTextBoxes.size() - 1; i >= 0; i--) {
            Text curr= fTextBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    protected Control findControl(String key) {
        Combo comboBox= getComboBox(key);
        if (comboBox != null) {
            return comboBox;
        }
        Button checkBox= getCheckBox(key);
        if (checkBox != null) {
            return checkBox;
        }
        Text text= getTextControl(key);
        if (text != null) {
            return text;
        }
        return null;
    }

    protected Control getLabel(Control control) {
        return fLabels.get(control);
    }

    protected void setComboEnabled(String key, boolean enabled) {
        Combo combo= getComboBox(key);
        Label label= fLabels.get(combo);
        combo.setEnabled(enabled);
        label.setEnabled(enabled);
    }
}

