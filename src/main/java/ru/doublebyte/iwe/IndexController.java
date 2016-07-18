package ru.doublebyte.iwe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private DocumentService documentService;

    @Autowired
    public IndexController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadDocument(
            @RequestParam("document") MultipartFile document,
            RedirectAttributes redirectAttributes
    ) {
        try {
            documentService.upload(document);
            redirectAttributes.addFlashAttribute("upload_message", "Document uploaded successfully");
        } catch(Exception e) {
            logger.error("File upload error", e);
            redirectAttributes.addFlashAttribute("upload_message", e.getMessage());
        }
        return "redirect:/";
    }

}
