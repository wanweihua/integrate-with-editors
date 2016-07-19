package ru.doublebyte.iwe.types;

import java.io.InputStream;

public class DocumentWithData {

    private final Document document;
    private final InputStream inputStream;

    public DocumentWithData(Document document, InputStream inputStream) {
        this.document = document;
        this.inputStream = inputStream;
    }

    public Document getDocument() {
        return document;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
