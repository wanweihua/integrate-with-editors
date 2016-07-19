package ru.doublebyte.iwe.types;

public enum EditorType {

    text,
    spreadsheet,
    presentation;

    @Override
    public String toString() {
        return name();
    }
}
