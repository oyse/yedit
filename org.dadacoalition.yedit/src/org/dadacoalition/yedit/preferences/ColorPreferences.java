package org.dadacoalition.yedit.preferences;

import java.util.ArrayList;
import java.util.Iterator;

import org.dadacoalition.yedit.Activator;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

/**
 * The UI for setting the YEdit color preferences.
 * 
 * This code borrows heavily from the FieldEditorPreferencePage, but does not
 * directly inherit from it as I could not find a way to get FieldEditorPreferencePage
 * to layout field editors correctly.
 * 
 * @author oysteto
 * 
 */
public class ColorPreferences extends PreferencePage implements
        IWorkbenchPreferencePage, IPropertyChangeListener {

    private ArrayList<FieldEditor> fieldEditors;

    /** 
     * The first invalid field editor, or <code>null</code>
     * if all field editors are valid.
     * 
     * Taken from FieldEditorPreferencePage
     */    
    private FieldEditor invalidFieldEditor = null;
    
    public ColorPreferences() {
        fieldEditors = new ArrayList<FieldEditor>();
        
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("YEdit Color Preferences for syntax highlighting");

    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    protected void createFieldEditors(Composite parent) {
       
        addSyntaxCategory(
                PreferenceConstants.COLOR_COMMENT,
                PreferenceConstants.BOLD_COMMENT,
                PreferenceConstants.ITALIC_COMMENT,
                PreferenceConstants.UNDERLINE_COMMENT, "Comments", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_KEY,
                PreferenceConstants.BOLD_KEY,
                PreferenceConstants.ITALIC_KEY,
                PreferenceConstants.UNDERLINE_KEY, "Keys", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_SCALAR,
                PreferenceConstants.BOLD_SCALAR,
                PreferenceConstants.ITALIC_SCALAR,
                PreferenceConstants.UNDERLINE_SCALAR, "Scalars", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_CONSTANT,
                PreferenceConstants.BOLD_CONSTANT,
                PreferenceConstants.ITALIC_CONSTANT,
                PreferenceConstants.UNDERLINE_CONSTANT, "Constants", parent);
        
        addSyntaxCategory(
                PreferenceConstants.COLOR_ANCHOR,
                PreferenceConstants.BOLD_ANCHOR,
                PreferenceConstants.ITALIC_ANCHOR,
                PreferenceConstants.UNDERLINE_ANCHOR, "Anchors", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_ALIAS,
                PreferenceConstants.BOLD_ALIAS,
                PreferenceConstants.ITALIC_ALIAS,
                PreferenceConstants.UNDERLINE_ALIAS, "Alias", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_TAG_PROPERTY,
                PreferenceConstants.BOLD_TAG_PROPERTY,
                PreferenceConstants.ITALIC_TAG_PROPERTY,
                PreferenceConstants.UNDERLINE_TAG_PROPERTY, "Tags", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_DOCUMENT,
                PreferenceConstants.BOLD_DOCUMENT,
                PreferenceConstants.ITALIC_DOCUMENT,
                PreferenceConstants.UNDERLINE_DOCUMENT, "Documents", parent);

        addSyntaxCategory(
                PreferenceConstants.COLOR_DEFAULT,
                PreferenceConstants.BOLD_DEFAULT,
                PreferenceConstants.ITALIC_DEFAULT,
                PreferenceConstants.UNDERLINE_DEFAULT, "Default", parent);

    }

    private void addSyntaxCategory(String colorConstant, String boldConstant,
            String italicConstant, String underlineConstant, String labelText,
            Composite parent) {

        Label categoryLabel = new Label( parent, SWT.LEFT );
        GridData gdLabel = new GridData();
        gdLabel.horizontalAlignment = SWT.BEGINNING;
        gdLabel.minimumWidth = 150;
        categoryLabel.setText( labelText );
        categoryLabel.setLayoutData( gdLabel );
        
        Composite colorC = new Composite(parent, SWT.LEFT);
        GridData gdColor = new GridData();
        gdColor.horizontalAlignment = SWT.BEGINNING;
        colorC.setLayoutData(gdColor);
        ColorFieldEditor colorEditor = new ColorFieldEditor(colorConstant, "",
                colorC);       
        fieldEditors.add(colorEditor);
        
        Composite boldC = new Composite(parent, SWT.LEFT);
        GridData gdBold = new GridData();
        boldC.setLayoutData(gdBold);
        BooleanFieldEditor boldEditor = new BooleanFieldEditor(boldConstant, "Bold",
                boldC);
        fieldEditors.add(boldEditor);
        
        Composite italicC = new Composite(parent, SWT.LEFT);
        GridData gdItalic = new GridData();
        italicC.setLayoutData(gdItalic);

        BooleanFieldEditor italicEditor = new BooleanFieldEditor(italicConstant,
                "Italic", italicC);
        fieldEditors.add(italicEditor);

        Composite underlineC = new Composite(parent, SWT.LEFT);
        GridData gdUnderline = new GridData();
        underlineC.setLayoutData(gdUnderline);
        BooleanFieldEditor underlineEditor = new BooleanFieldEditor(
                underlineConstant, "Underline", underlineC);
        fieldEditors.add(underlineEditor);
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }


    protected Control createContents(Composite parent) {

        Composite c = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout();
        gl.numColumns = 5;
        c.setLayout(gl);

        createFieldEditors(c);

        initialize();
        checkState();
        
        return c;
    }
    
    /**
     * Initializes all field editors.
     */
    protected void initialize() {

        Iterator<FieldEditor> i = fieldEditors.iterator();
        while (i.hasNext()) {
            FieldEditor pe = i.next();
            pe.setPage(this);
            pe.setPropertyChangeListener(this);
            pe.setPreferenceStore(getPreferenceStore());
            pe.load();
        }

    }

    /** 
     * The field editor preference page implementation of a <code>PreferencePage</code>
     * method loads all the field editors with their default values.
     */
    protected void performDefaults() {

        Iterator<FieldEditor> i = fieldEditors.iterator();
        while (i.hasNext()) {
            FieldEditor pe = i.next();
            pe.loadDefault();
        }

        // Force a recalculation of my error state.
        checkState();
        super.performDefaults();
    }

    /** 
     * The field editor preference page implementation of this 
     * <code>PreferencePage</code> method saves all field editors by
     * calling <code>FieldEditor.store</code>. Note that this method
     * does not save the preference store itself; it just stores the
     * values back into the preference store.
     *
     * @see FieldEditor#store()
     */
    public boolean performOk() {

        Iterator<FieldEditor> i = fieldEditors.iterator();
        while (i.hasNext()) {
            FieldEditor pe = i.next();
            pe.store();
            
            //this method is protected so have no access to it in this package
            //pe.setPresentsDefaultValue(false);
        }

        return true;
    }

    /**
     * The field editor preference page implementation of this <code>IPreferencePage</code>
     * (and <code>IPropertyChangeListener</code>) method intercepts <code>IS_VALID</code> 
     * events but passes other events on to its superclass.
     */
    public void propertyChange(PropertyChangeEvent event) {

        if (event.getProperty().equals(FieldEditor.IS_VALID)) {
            boolean newValue = ((Boolean) event.getNewValue()).booleanValue();
            // If the new value is true then we must check all field editors.
            // If it is false, then the page is invalid in any case.
            if (newValue) {
                checkState();
            } else {
                invalidFieldEditor = (FieldEditor) event.getSource();
                setValid(newValue);
            }
        }
    }

    /* (non-Javadoc)
     * Method declared on IDialog.
     */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && invalidFieldEditor != null) {
            invalidFieldEditor.setFocus();
        }
    }
    
    /** 
     * The field editor preference page implementation of an <code>IDialogPage</code>
     * method disposes of this page's controls and images.
     * Subclasses may override to release their own allocated SWT
     * resources, but must call <code>super.dispose</code>.
     */
    public void dispose() {
        super.dispose();

        Iterator<FieldEditor> i = fieldEditors.iterator();
        while (i.hasNext()) {
            FieldEditor pe = i.next();
            pe.setPage(null);
            pe.setPropertyChangeListener(null);
            pe.setPreferenceStore(null);
        }
    }
    
    /**
     * Recomputes the page's error state by calling <code>isValid</code> for
     * every field editor.
     */
    protected void checkState() {
        boolean valid = true;
        invalidFieldEditor = null;
        
        // The state can only be set to true if all
        // field editors contain a valid value. So we must check them all
        Iterator<FieldEditor> i = fieldEditors.iterator();
        while( i.hasNext() ){
            FieldEditor editor = i.next();
            valid = valid && editor.isValid();            
            if (!valid) {
                invalidFieldEditor = editor;
                break;
            }            
        }
        setValid(valid);
    }    

}
