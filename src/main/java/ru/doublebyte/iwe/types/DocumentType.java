package ru.doublebyte.iwe.types;

/**
 * Document types suitable for document server
 */
public enum DocumentType {
    docx(EditorType.text),
    doc(EditorType.text),
    odt(EditorType.text),
    txt(EditorType.text),
    rtf(EditorType.text),
    mht(EditorType.text),
    html(EditorType.text),
    htm(EditorType.text),
    epub(EditorType.text),
    pdf(EditorType.text),
    djvu(EditorType.text),
    xps(EditorType.text),

    xlsx(EditorType.spreadsheet),
    xls(EditorType.spreadsheet),
    ods(EditorType.spreadsheet),
    csv(EditorType.spreadsheet),

    pptx(EditorType.presentation),
    ppt(EditorType.presentation),
    pps(EditorType.presentation),
    odp(EditorType.presentation),

    unknown(EditorType.text); //special type

    ///////////////////////////////////////////////////////////////////////////

    private EditorType editorType;

    DocumentType(EditorType editorType) {
        this.editorType = editorType;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get document type by it's extension
     * @param name Document file name
     * @return Document type
     */
    public static DocumentType getByName(String name) {
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            return unknown;
        }

        String extension = name.substring(dot + 1).toLowerCase();
        for(DocumentType type : values()) {
            if(type != unknown && type.toString().equals(extension)) {
                return type;
            }
        }

        return unknown;
    }

    ///////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return this.name();
    }

    public EditorType getEditorType() {
        return editorType;
    }
}
