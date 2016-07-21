package ru.doublebyte.iwe;

import org.junit.Test;
import ru.doublebyte.iwe.types.DocumentType;

import static org.junit.Assert.*;

public class DocumentServiceTest {
    @Test
    public void getResultDocumentType() throws Exception {
        DocumentService documentService = new DocumentService(null);

        assertEquals(DocumentType.docx, documentService.getResultDocumentType("http://localhost/edit?a=b&ooname=test.docx"));
        assertEquals(DocumentType.docx, documentService.getResultDocumentType("http://localhost/edit?a=b&ooname=test.docx&c=d"));

        assertEquals(DocumentType.unknown, documentService.getResultDocumentType("http://localhost"));
        assertEquals(DocumentType.unknown, documentService.getResultDocumentType("testtesttest"));
        assertEquals(DocumentType.unknown, documentService.getResultDocumentType("http://localho%%$#$%^st"));
        assertEquals(DocumentType.unknown, documentService.getResultDocumentType("http://localhost?a=b&c=d"));
    }

}