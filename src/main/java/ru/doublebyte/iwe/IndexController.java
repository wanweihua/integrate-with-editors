package ru.doublebyte.iwe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Value("${editors.url}")
    private String editorsUrl;

    @Autowired
    public IndexController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("documents", documentService.list());
        return "index";
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String upload(
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

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(
        @RequestParam("id") Long id,
        RedirectAttributes redirectAttributes
    ) {
        try {
            documentService.delete(id);
            redirectAttributes.addFlashAttribute("delete_message", "Document deleted successfully");
        } catch(Exception e) {
            logger.error("Document delete error", e);
            redirectAttributes.addFlashAttribute("delete_message", e.getMessage());
        }
        return "redirect:/";
    }

}
