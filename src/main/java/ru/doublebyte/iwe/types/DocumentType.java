package ru.doublebyte.iwe.types;

/**
 * Document types suitable for document server
 */
public enum DocumentType {
    docx,
    xlsx,
    pptx,
    doc,
    xls,
    ppt,
    pps,
    odt,
    ods,
    odp,
    txt,
    rtf,
    csv,
    mht,
    html,
    htm,
    epub,
    pdf,
    djvu,
    xps,
    unknown; //special type

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

    @Override
    public String toString() {
        return this.name();
    }

}
