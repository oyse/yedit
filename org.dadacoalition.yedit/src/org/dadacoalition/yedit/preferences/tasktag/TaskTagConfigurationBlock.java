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
 *     Øystein Idema Torget - Adaption from CDT to YEdit
 *******************************************************************************/
package org.dadacoalition.yedit.preferences.tasktag;

import java.util.ArrayList;
import java.util.List;

import org.dadacoalition.yedit.preferences.PreferenceConstants;
import org.eclipse.core.resources.IProject;
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
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;


/**
 * UI for editing task tags.
 */
public class TaskTagConfigurationBlock extends OptionsConfigurationBlock {
    private static final String PREF_TODO_TASK_TAGS = PreferenceConstants.TODO_TASK_TAGS;
    private static final String PREF_TODO_TASK_PRIORITIES = PreferenceConstants.TODO_TASK_PRIORITIES;
    private static final String PREF_TODO_TASK_CASE_SENSITIVE = PreferenceConstants.TODO_TASK_CASE_SENSITIVE;  

    private static final String[] ALL_KEYS = new String[] {
        PREF_TODO_TASK_TAGS, PREF_TODO_TASK_PRIORITIES, PREF_TODO_TASK_CASE_SENSITIVE
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
        super(context, project, ALL_KEYS, container, preferenceStore);
                        
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

    protected void validateSettings(String changedKey, String oldValue, String newValue) {
        
        if (changedKey != null) {
            if (PREF_TODO_TASK_TAGS.equals(changedKey)) {
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
            setValue(PREF_TODO_TASK_TAGS, tags.toString());
            setValue(PREF_TODO_TASK_PRIORITIES, prios.toString());
            validateSettings(PREF_TODO_TASK_TAGS, null, null);
        } else if (field == fCaseSensitiveCheckBox) {
            String state = String.valueOf(fCaseSensitiveCheckBox.isSelected());
            setValue(PREF_TODO_TASK_CASE_SENSITIVE, state);
        }
    }
    
    protected void updateControls() {
        unpackTodoTasks();
    }
    
    private void unpackTodoTasks() {
        String currTags = getValue(PREF_TODO_TASK_TAGS);    
        String currPrios = getValue(PREF_TODO_TASK_PRIORITIES);
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
        
        boolean isCaseSensitive = getBooleanValue(PREF_TODO_TASK_CASE_SENSITIVE);
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
}
