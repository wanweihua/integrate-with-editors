package ru.doublebyte.iwe;

import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.doublebyte.iwe.types.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final DocumentService documentService;

    @Value("${editors.url}")
    private String editorsUrl;

    @Value("${editors.self_url}")
    private String selfUrl;

    @Autowired
    public IndexController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("documents", documentService.list());
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
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

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(
            @PathVariable("id") Long id,
            Model model
    ) {
        model.addAttribute("editors_url", editorsUrl);
        model.addAttribute("self_url", selfUrl);
        model.addAttribute("document", documentService.get(id));
        return "edit";
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public void get(
            @PathVariable("id") Long id,
            HttpServletResponse response
    ) {
        try {
            Document document = documentService.get(id);
            File file = documentService.getFile(id); //TODO make better

            response.setHeader("Content-Disposition", "attachment; filename=" + document.getName());
            ByteStreams.copy(new FileInputStream(file), response.getOutputStream());
            response.flushBuffer();
        } catch(Exception e) {
            throw new RuntimeException("Foo");
        }
    }

}
