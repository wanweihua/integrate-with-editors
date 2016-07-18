package ru.doublebyte.iwe.types;

import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testGetByName() throws Exception {
        assertEquals(DocumentType.doc, DocumentType.getByName("file1.doc"));
        assertEquals(DocumentType.doc, DocumentType.getByName("file1.Doc"));
        assertEquals(DocumentType.doc, DocumentType.getByName("file1.DOC"));

        assertEquals(DocumentType.docx, DocumentType.getByName("file2.DOCX"));

        assertEquals(DocumentType.unknown, DocumentType.getByName("file3"));
        assertEquals(DocumentType.unknown, DocumentType.getByName("file3.java"));
        assertEquals(DocumentType.unknown, DocumentType.getByName("file3."));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("docx", DocumentType.docx.toString());
        assertEquals("pdf", DocumentType.pdf.toString());
    }

}