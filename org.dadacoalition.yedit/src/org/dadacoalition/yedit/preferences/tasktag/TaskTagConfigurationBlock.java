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
 *     Øystein Idema Torget - Adaption from CDT to YEdit.
 *******************************************************************************/
package org.dadacoalition.yedit.preferences.tasktag;

import java.util.List;

import org.dadacoalition.yedit.YEditLog;
import org.dadacoalition.yedit.editor.TaskTagPreference;
import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.core.runtime.IStatus;
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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


/**
 * UI for editing task tags.
 */
public class TaskTagConfigurationBlock {

    private IStatusChangeListener fContext;

    private Shell fShell;

    private IPreferenceStore fPreferenceStore;    

    private static final String[] ALL_KEYS = new String[] {
    		PreferenceConstants.TODO_TASK_TAGS,
    		PreferenceConstants.TODO_TASK_PRIORITIES,
    		PreferenceConstants.TODO_TASK_CASE_SENSITIVE
    };

    private static final String TASK_PRIORITY_HIGH = PreferenceConstants.TASK_PRIORITY_HIGH;
    private static final String TASK_PRIORITY_NORMAL = PreferenceConstants.TASK_PRIORITY_NORMAL;
    private static final String TASK_PRIORITY_LOW = PreferenceConstants.TASK_PRIORITY_LOW;
    
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
            TaskTagPreference task = (TaskTagPreference) element;
            if (columnIndex == 0) {
                String name = task.tag;
                if (isDefaultTask(task)) {
                    name = name + " (default)"; 
                }
                return name;
            }
            if (TASK_PRIORITY_HIGH.equals(task.severity)) {
                return "High"; 
            } else if (TASK_PRIORITY_NORMAL.equals(task.severity)) {
                return "Normal"; 
            } else if (TASK_PRIORITY_LOW.equals(task.severity)) {
                return "Low"; 
            }
            return ""; //$NON-NLS-1$    
        }

        public Font getFont(Object element) {
            if (isDefaultTask((TaskTagPreference) element)) {
                return JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT);
            }
            return null;
        }
    }
    
    private static class TodoTaskSorter extends ViewerComparator {
        @SuppressWarnings("unchecked")
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            return getComparator().compare(((TaskTagPreference) e1).tag, ((TaskTagPreference) e2).tag);
        }
    }

    private static final int IDX_ADD = 0;
    private static final int IDX_EDIT = 1;
    private static final int IDX_REMOVE = 2;
    private static final int IDX_DEFAULT = 4;
    
    private IStatus fTaskTagsStatus;
    private final ListDialogField<TaskTagPreference> fTodoTasksList;
    private final SelectionButtonDialogField fCaseSensitiveCheckBox;


    public TaskTagConfigurationBlock(IStatusChangeListener context, IPreferenceStore preferenceStore) {

        fPreferenceStore = preferenceStore;
        fContext= context;

        checkIfOptionsComplete(ALL_KEYS);

        settingsUpdated();       
        
        TaskTagAdapter adapter = new TaskTagAdapter();
        String[] buttons = new String[] {
            "Add", 
            "Edit", 
            "Remove", 
            null,
            "Set default"  
        };
        fTodoTasksList = new ListDialogField<TaskTagPreference>(adapter, buttons, new TodoTaskLabelProvider());
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
    
    final boolean isDefaultTask(TaskTagPreference task) {
        return fTodoTasksList.getIndexOfElement(task) == 0;
    }
    
    private void setToDefaultTask(TaskTagPreference task) {
        List<TaskTagPreference> elements = fTodoTasksList.getElements();
        elements.remove(task);
        elements.add(0, task);
        fTodoTasksList.setElements(elements);
        fTodoTasksList.enableButton(IDX_DEFAULT, false);
    }
    
    public class TaskTagAdapter implements IListAdapter<TaskTagPreference>, IDialogFieldListener {
        private boolean canEdit(List<TaskTagPreference> selectedElements) {
            return selectedElements.size() == 1;
        }
        
        private boolean canSetToDefault(List<TaskTagPreference> selectedElements) {
            return selectedElements.size() == 1 && !isDefaultTask(selectedElements.get(0));
        }

        public void customButtonPressed(ListDialogField<TaskTagPreference> field, int index) {
            doTodoButtonPressed(index);
        }

        public void selectionChanged(ListDialogField<TaskTagPreference> field) {
            List<TaskTagPreference> selectedElements = field.getSelectedElements();
            field.enableButton(IDX_EDIT, canEdit(selectedElements));
            field.enableButton(IDX_DEFAULT, canSetToDefault(selectedElements));
        }
            
        public void doubleClicked(ListDialogField<TaskTagPreference> field) {
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
        
        validateSettings(null);
    
        return composite;
    }

    private void validateSettings(String changedKey) {
        
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
            List<TaskTagPreference> list = fTodoTasksList.getElements();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    tags.append(',');
                    prios.append(',');
                }
                TaskTagPreference elem = list.get(i);
                tags.append(elem.tag);
                prios.append(elem.severity);
            }
            setValue(PreferenceConstants.TODO_TASK_TAGS, tags.toString());
            setValue(PreferenceConstants.TODO_TASK_PRIORITIES, prios.toString());
            validateSettings(PreferenceConstants.TODO_TASK_TAGS);
        } else if (field == fCaseSensitiveCheckBox) {
            String state = String.valueOf(fCaseSensitiveCheckBox.isSelected());
            setValue(PreferenceConstants.TODO_TASK_CASE_SENSITIVE, state);
        }
    }
    
    private void unpackTodoTasks() {
        List<TaskTagPreference> elements = TaskTagPreference.getTaskTagPreferences(fPreferenceStore);
        fTodoTasksList.setElements(elements);
        
        boolean isCaseSensitive = getBooleanValue(PreferenceConstants.TODO_TASK_CASE_SENSITIVE);
        fCaseSensitiveCheckBox.setSelection(isCaseSensitive);
    }
    
    private void doTodoButtonPressed(int index) {
        TaskTagPreference edited = null;
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
    

    private void checkIfOptionsComplete(String[] allKeys) {
        for (int i= 0; i < allKeys.length; i++) {
            if (fPreferenceStore.getString(allKeys[i]) == null) {
                YEditLog.logError("Preference option missing: " + allKeys[i] + " (" + this.getClass().getName() +')');  //$NON-NLS-1$//$NON-NLS-2$
            }
        }
    }

    protected void settingsUpdated() {
    }



    private Shell getShell() {
        return fShell;
    }

    private void setShell(Shell shell) {
        fShell= shell;
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

    public void dispose() {
    }

   
}
