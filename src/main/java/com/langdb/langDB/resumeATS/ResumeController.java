package com.langdb.langDB.resumeATS;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final JobService jobService;

    public ResumeController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            String resumeText = extractTextFromPDF(file);
            List<String> response = jobService.parseResumeAndFindJobOpenings(resumeText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing resume: " + e.getMessage());
        }
    }


    @PostMapping("/upload/chatgpt")
    public ResponseEntity<?> uploadResumeChatGpt(@RequestParam("file") MultipartFile file) {
        try {
            String resumeText = extractTextFromPDF(file);
            List<String> response = jobService.parseResumeWithChatGpt(resumeText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing resume: " + e.getMessage());
        }
    }

    @PostMapping("/upload/deepseek")
    public ResponseEntity<?> uploadResumeDeepseek(@RequestParam("file") MultipartFile file) {
        try {
            String resumeText = extractTextFromPDF(file);
            List<String> response = jobService.parseResumeWithDeepseek(resumeText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing resume: " + e.getMessage());
        }
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("resume", ".pdf");
        Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try (PDDocument document = PDDocument.load(tempFile)) {
            return new PDFTextStripper().getText(document);
        }
    }

    @PostMapping("/extract")
    public ResponseEntity<?> extractTextFromResume(@RequestParam("file") MultipartFile file) {
        try {
            String resumeText = extractTextFromPDF(file);
            return ResponseEntity.ok(resumeText);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error extracting text: " + e.getMessage());
        }
    }

}
