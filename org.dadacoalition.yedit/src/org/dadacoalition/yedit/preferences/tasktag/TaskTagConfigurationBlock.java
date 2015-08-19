/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sergey Prigogin (Google)
 *     ��ystein Idema Torget - Adaption from CDT to YEdit
 *******************************************************************************/
package org.dadacoalition.yedit.preferences.tasktag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.dadacoalition.yedit.Activator;
import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
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
 * UI for editing task tags.
 */
public class TaskTagConfigurationBlock {

    private static final String[] FALSE_TRUE = new String[] { "false", "true" };  //$NON-NLS-1$//$NON-NLS-2$
    private static final String[] TRUE_FALSE = new String[] { "true", "false" };  //$NON-NLS-1$//$NON-NLS-2$

    private static final String REBUILD_COUNT_KEY= "preferences_build_requested"; //$NON-NLS-1$

    private static final String SETTINGS_EXPANDED= "expanded"; //$NON-NLS-1$

    private final ArrayList<Button> fCheckBoxes;
    private final ArrayList<Combo> fComboBoxes;
    private final ArrayList<Text> fTextBoxes;
    private final HashMap<Control, Label> fLabels;
    private final ArrayList<ExpandableComposite> fExpandedComposites;

    private SelectionListener fSelectionListener;
    private ModifyListener fTextModifyListener;

    private IStatusChangeListener fContext;
    private final IProject fProject; // project or null
    private final String[] fAllKeys;

    private Shell fShell;

    private final IWorkingCopyManager fManager;
    private final IWorkbenchPreferenceContainer fContainer;

    private int fRebuildCount; // used to prevent multiple dialogs that ask for a rebuild
    
    private IPreferenceStore fPreferenceStore;    

    private static class ControlData {
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
    
    
    private static final String[] ALL_KEYS = new String[] {
    		PreferenceConstants.TODO_TASK_TAGS,
    		PreferenceConstants.TODO_TASK_PRIORITIES,
    		PreferenceConstants.TODO_TASK_CASE_SENSITIVE
    };

    private static final String TASK_PRIORITY_HIGH = PreferenceConstants.TASK_PRIORITY_HIGH;
    private static final String TASK_PRIORITY_NORMAL = PreferenceConstants.TASK_PRIORITY_NORMAL;
    private static final String TASK_PRIORITY_LOW = PreferenceConstants.TASK_PRIORITY_LOW;
    
    public static class TodoTask {
        public String name;
        public String priority;
    }
    
    private class TodoTaskLabelProvider extends LabelProvider implements ITableLabelProvider, IFontProvider {

        public TodoTaskLabelProvider() {
        }
        
        @Override
        public Image getImage(Object element) {
            return null;
        }

        @Override
        public String getText(Object element) {
            return getColumnText(element, 0);
        }
        
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            TodoTask task = (TodoTask) element;
            if (columnIndex == 0) {
                String name = task.name;
                if (isDefaultTask(task)) {
                    name = name + " (default)"; 
                }
                return name;
            }
            if (TASK_PRIORITY_HIGH.equals(task.priority)) {
                return "High"; 
            } else if (TASK_PRIORITY_NORMAL.equals(task.priority)) {
                return "Normal"; 
            } else if (TASK_PRIORITY_LOW.equals(task.priority)) {
                return "Low"; 
            }
            return ""; //$NON-NLS-1$    
        }

        public Font getFont(Object element) {
            if (isDefaultTask((TodoTask) element)) {
                return JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT);
            }
            return null;
        }
    }
    
    private static class TodoTaskSorter extends ViewerComparator {
        @SuppressWarnings("unchecked")
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            return getComparator().compare(((TodoTask) e1).name, ((TodoTask) e2).name);
        }
    }

    private static final int IDX_ADD = 0;
    private static final int IDX_EDIT = 1;
    private static final int IDX_REMOVE = 2;
    private static final int IDX_DEFAULT = 4;
    
    private IStatus fTaskTagsStatus;
    private final ListDialogField<TodoTask> fTodoTasksList;
    private final SelectionButtonDialogField fCaseSensitiveCheckBox;


    public TaskTagConfigurationBlock(IStatusChangeListener context, IProject project, IWorkbenchPreferenceContainer container, IPreferenceStore preferenceStore) {

        fPreferenceStore = preferenceStore;
        fContext= context;
        fProject= project;
        fAllKeys= ALL_KEYS;
        fContainer= container;
        if (container == null) {
            fManager= new WorkingCopyManager();
        } else {
            fManager= container.getWorkingCopyManager();
        }

        checkIfOptionsComplete(ALL_KEYS);

        settingsUpdated();

        fCheckBoxes= new ArrayList<Button>();
        fComboBoxes= new ArrayList<Combo>();
        fTextBoxes= new ArrayList<Text>(2);
        fLabels= new HashMap<Control, Label>();
        fExpandedComposites= new ArrayList<ExpandableComposite>();

        fRebuildCount= getRebuildCount();        
        
        TaskTagAdapter adapter = new TaskTagAdapter();
        String[] buttons = new String[] {
            "Add", 
            "Edit", 
            "Remove", 
            null,
            "Set default" 
        };
        fTodoTasksList = new ListDialogField<TodoTask>(adapter, buttons, new TodoTaskLabelProvider());
        fTodoTasksList.setDialogFieldListener(adapter);
        fTodoTasksList.setRemoveButtonIndex(IDX_REMOVE);
        
        String[] columnsHeaders = new String[] {
            "Name", 
            "Priority" 
        };
        
        fTodoTasksList.setTableColumns(new ListDialogField.ColumnsDescription(columnsHeaders, true));
        fTodoTasksList.setViewerComparator(new TodoTaskSorter());
        
        fCaseSensitiveCheckBox = new SelectionButtonDialogField(SWT.CHECK);
        fCaseSensitiveCheckBox.setLabelText("Case sensitive task tag names"); 
        fCaseSensitiveCheckBox.setDialogFieldListener(adapter);
        
        unpackTodoTasks();
        if (fTodoTasksList.getSize() > 0) {
            fTodoTasksList.selectFirstElement();
        } else {
            fTodoTasksList.enableButton(IDX_EDIT, false);
            fTodoTasksList.enableButton(IDX_DEFAULT, false);
        }
        
        fTaskTagsStatus = new StatusInfo();     
    }
    
    public void setEnabled(boolean isEnabled) {
        fTodoTasksList.setEnabled(isEnabled);
        fCaseSensitiveCheckBox.setEnabled(isEnabled);
    }
    
    final boolean isDefaultTask(TodoTask task) {
        return fTodoTasksList.getIndexOfElement(task) == 0;
    }
    
    private void setToDefaultTask(TodoTask task) {
        List<TodoTask> elements = fTodoTasksList.getElements();
        elements.remove(task);
        elements.add(0, task);
        fTodoTasksList.setElements(elements);
        fTodoTasksList.enableButton(IDX_DEFAULT, false);
    }
    
    public class TaskTagAdapter implements IListAdapter<TodoTask>, IDialogFieldListener {
        private boolean canEdit(List<TodoTask> selectedElements) {
            return selectedElements.size() == 1;
        }
        
        private boolean canSetToDefault(List<TodoTask> selectedElements) {
            return selectedElements.size() == 1 && !isDefaultTask(selectedElements.get(0));
        }

        public void customButtonPressed(ListDialogField<TodoTask> field, int index) {
            doTodoButtonPressed(index);
        }

        public void selectionChanged(ListDialogField<TodoTask> field) {
            List<TodoTask> selectedElements = field.getSelectedElements();
            field.enableButton(IDX_EDIT, canEdit(selectedElements));
            field.enableButton(IDX_DEFAULT, canSetToDefault(selectedElements));
        }
            
        public void doubleClicked(ListDialogField<TodoTask> field) {
            if (canEdit(field.getSelectedElements())) {
                doTodoButtonPressed(IDX_EDIT);
            }
        }

        public void dialogFieldChanged(DialogField field) {
            updateModel(field);
        }           
    }
        
    public Control createContents(Composite parent) {
        setShell(parent.getShell());
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        
        PixelConverter conv = new PixelConverter(parent);
        
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(layout);
        composite.setFont(parent.getFont());
        
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = conv.convertWidthInCharsToPixels(50);
        Control listControl = fTodoTasksList.getListControl(composite);
        listControl.setLayoutData(data);
        
        Control buttonsControl = fTodoTasksList.getButtonBox(composite);
        buttonsControl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING));
        
        fCaseSensitiveCheckBox.doFillIntoGrid(composite, 2);
        
        validateSettings(null, null, null);
    
        return composite;
    }

    private void validateSettings(String changedKey, String oldValue, String newValue) {
        
        if (changedKey != null) {
            if (PreferenceConstants.TODO_TASK_TAGS.equals(changedKey)) {
                fTaskTagsStatus = validateTaskTags();
            } else {
                return;
            }
        } else {
            fTaskTagsStatus = validateTaskTags();
        }       
        IStatus status = fTaskTagsStatus; //StatusUtil.getMostSevere(new IStatus[] { fTaskTagsStatus });
        fContext.statusChanged(status);
    }
    
    private IStatus validateTaskTags() {
        return new StatusInfo();
    }
    
    private void updateModel(DialogField field) {
        if (field == fTodoTasksList) {
            StringBuffer tags = new StringBuffer();
            StringBuffer prios = new StringBuffer();
            List<TodoTask> list = fTodoTasksList.getElements();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    tags.append(',');
                    prios.append(',');
                }
                TodoTask elem = list.get(i);
                tags.append(elem.name);
                prios.append(elem.priority);
            }
            setValue(PreferenceConstants.TODO_TASK_TAGS, tags.toString());
            setValue(PreferenceConstants.TODO_TASK_PRIORITIES, prios.toString());
            validateSettings(PreferenceConstants.TODO_TASK_TAGS, null, null);
        } else if (field == fCaseSensitiveCheckBox) {
            String state = String.valueOf(fCaseSensitiveCheckBox.isSelected());
            setValue(PreferenceConstants.TODO_TASK_CASE_SENSITIVE, state);
        }
    }
    
    private void updateControls() {
        unpackTodoTasks();
    }
    
    private void unpackTodoTasks() {
        String currTags = getValue(PreferenceConstants.TODO_TASK_TAGS);    
        String currPrios = getValue(PreferenceConstants.TODO_TASK_PRIORITIES);
        String[] tags = getTokens(currTags, ","); //$NON-NLS-1$
        String[] prios = getTokens(currPrios, ","); //$NON-NLS-1$
        ArrayList<TodoTask> elements = new ArrayList<TodoTask>(tags.length);
        for (int i = 0; i < tags.length; i++) {
            TodoTask task = new TodoTask();
            task.name = tags[i].trim();
            task.priority = (i < prios.length) ? prios[i] : TASK_PRIORITY_NORMAL;
            elements.add(task);
        }
        fTodoTasksList.setElements(elements);
        
        boolean isCaseSensitive = getBooleanValue(PreferenceConstants.TODO_TASK_CASE_SENSITIVE);
        fCaseSensitiveCheckBox.setSelection(isCaseSensitive);
    }
    
    private void doTodoButtonPressed(int index) {
        TodoTask edited = null;
        if (index != IDX_ADD) {
            edited = fTodoTasksList.getSelectedElements().get(0);
        }
        if (index == IDX_ADD || index == IDX_EDIT) {
            TodoTaskInputDialog dialog = new TodoTaskInputDialog(getShell(), edited, fTodoTasksList.getElements());
            if (dialog.open() == Window.OK) {
                if (edited != null) {
                    fTodoTasksList.replaceElement(edited, dialog.getResult());
                } else {
                    fTodoTasksList.addElement(dialog.getResult());
                }
            }
        } else if (index == IDX_DEFAULT) {
            setToDefaultTask(edited);
        }
    }
    
    private final IWorkbenchPreferenceContainer getPreferenceContainer() {
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


    private void selectOption(String key) {
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

    private Shell getShell() {
        return fShell;
    }

    private void setShell(Shell shell) {
        fShell= shell;
    }

    private Button addCheckBox(Composite parent, String label, String key, String[] values, int indent) {
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

    private Button addCheckBoxWithLink(Composite parent, String label, String key, String[] values,
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

    private Button addRadioButton(Composite parent, String label, String key, String[] values, int indent) {
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
    
    private Combo addComboBox(Composite parent, String label, String key, String[] values,
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

    private Combo addInversedComboBox(Composite parent, String label, String key, String[] values,
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

    private Combo newComboControl(Composite composite, String key, String[] values, String[] valueLabels) {
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

    private Text addTextField(Composite parent, String label, String key, int indent, int widthHint) {
        return addTextField(parent, label, key, indent, widthHint, SWT.NONE);
    }

    private Text addTextField(Composite parent, String label, String key, int indent, int widthHint,
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

    private Composite addSubsection(Composite parent, String label) {
        Group group= new Group(parent, SWT.SHADOW_NONE);
        group.setText(label);
        GridData data= new GridData(SWT.FILL, SWT.CENTER, true, false);
        group.setLayoutData(data);
        return group;
    }

    private ScrolledPageContent getParentScrolledComposite(Control control) {
        Control parent= control.getParent();
        while (!(parent instanceof ScrolledPageContent) && parent != null) {
            parent= parent.getParent();
        }
        if (parent instanceof ScrolledPageContent) {
            return (ScrolledPageContent) parent;
        }
        return null;
    }

    private ExpandableComposite getParentExpandableComposite(Control control) {
        Control parent= control.getParent();
        while (!(parent instanceof ExpandableComposite) && parent != null) {
            parent= parent.getParent();
        }
        if (parent instanceof ExpandableComposite) {
            return (ExpandableComposite) parent;
        }
        return null;
    }

    private void makeScrollableCompositeAware(Control control) {
        ScrolledPageContent parentScrolledComposite= getParentScrolledComposite(control);
        if (parentScrolledComposite != null) {
            parentScrolledComposite.adaptChild(control);
        }
    }

    private ExpandableComposite createStyleSection(Composite parent, String label, int nColumns) {
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

    private final void expandedStateChanged(ExpandableComposite expandable) {
        ScrolledPageContent parentScrolledComposite= getParentScrolledComposite(expandable);
        if (parentScrolledComposite != null) {
            parentScrolledComposite.reflow(true);
        }
    }

    private void restoreSectionExpansionStates(IDialogSettings settings) {
        for (int i= 0; i < fExpandedComposites.size(); i++) {
            ExpandableComposite excomposite= fExpandedComposites.get(i);
            if (settings == null) {
                excomposite.setExpanded(i == 0); // only expand the first node by default
            } else {
                excomposite.setExpanded(settings.getBoolean(SETTINGS_EXPANDED + String.valueOf(i)));
            }
        }
    }

    private void storeSectionExpansionStates(IDialogSettings settings) {
        for (int i= 0; i < fExpandedComposites.size(); i++) {
            ExpandableComposite curr= fExpandedComposites.get(i);
            settings.put(SETTINGS_EXPANDED + String.valueOf(i), curr.isExpanded());
        }
    }

    private SelectionListener getSelectionListener() {
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

    private ModifyListener getTextModifyListener() {
        if (fTextModifyListener == null) {
            fTextModifyListener= new ModifyListener() {
                
                public void modifyText(ModifyEvent e) {
                    textChanged((Text) e.widget);
                }
            };
        }
        return fTextModifyListener;
    }

    private void controlChanged(Widget widget) {
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

    private void textChanged(Text textControl) {
        String key= (String) textControl.getData();
        if (key != null) {
            String newValue= textControl.getText();
            String oldValue= setValue(key, newValue);
            validateSettings(key, oldValue, newValue);
        }
    }

    private boolean checkValue(String key, String value) {
        return value.equals(getValue(key));
    }

    private String getValue(String key) {
        return fPreferenceStore.getString(key);
    }


    private boolean getBooleanValue(String key) {
        return Boolean.valueOf(getValue(key)).booleanValue();
    }

    private String setValue(String key, String value) {
        String oldValue= getValue(key);        
        fPreferenceStore.setValue(key, value);
        return oldValue;
    }

    private String setValue(String key, boolean value) {
        return setValue(key, String.valueOf(value));
    }

    /**
     * Returns the value as actually stored in the preference store.
     * @param key
     * @return the value as actually stored in the preference store.
     */
    private String getStoredValue(String key) {
        return fPreferenceStore.getString(key);
    }

    private String[] getTokens(String text, String separator) {
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

    private boolean performApply() {
        return processChanges(null); // apply directly
    }

    private boolean processChanges(IWorkbenchPreferenceContainer container) {
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

//    private void updateControls() {
//        // update the UI
//        for (int i= fCheckBoxes.size() - 1; i >= 0; i--) {
//            updateCheckBox(fCheckBoxes.get(i));
//        }
//        for (int i= fComboBoxes.size() - 1; i >= 0; i--) {
//            updateCombo(fComboBoxes.get(i));
//        }
//        for (int i= fTextBoxes.size() - 1; i >= 0; i--) {
//            updateText(fTextBoxes.get(i));
//        }
//    }

    private void updateCombo(Combo curr) {
        ControlData data= (ControlData) curr.getData();

        String currValue= getValue(data.getKey());
        curr.select(data.getSelection(currValue));
    }

    private void updateCheckBox(Button curr) {
        ControlData data= (ControlData) curr.getData();

        String currValue= getValue(data.getKey());
        curr.setSelection(data.getSelection(currValue) == 0);
    }

    private void updateText(Text curr) {
        String key= (String) curr.getData();
        if (key != null) {
            String currValue= getValue(key);
            if (currValue != null) {
                curr.setText(currValue);
            }
        }
    }

    private Button getCheckBox(String key) {
        for (int i= fCheckBoxes.size() - 1; i >= 0; i--) {
            Button curr= fCheckBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    private Combo getComboBox(String key) {
        for (int i= fComboBoxes.size() - 1; i >= 0; i--) {
            Combo curr= fComboBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    private Text getTextControl(String key) {
        for (int i= fTextBoxes.size() - 1; i >= 0; i--) {
            Text curr= fTextBoxes.get(i);
            ControlData data= (ControlData) curr.getData();
            if (key.equals(data.getKey())) {
                return curr;
            }
        }
        return null;
    }

    private Control findControl(String key) {
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

    private Control getLabel(Control control) {
        return fLabels.get(control);
    }

    private void setComboEnabled(String key, boolean enabled) {
        Combo combo= getComboBox(key);
        Label label= fLabels.get(combo);
        combo.setEnabled(enabled);
        label.setEnabled(enabled);
    }    
}
