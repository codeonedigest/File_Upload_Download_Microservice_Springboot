package cod.multipart.springbootfileuploaddownloadmicroservice.controller;

import cod.multipart.springbootfileuploaddownloadmicroservice.business.FileUploadDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;

@RestController
@Slf4j
public class FileController {

    @Autowired
    FileUploadDownloadService fileUploadDownloadService;

    @Autowired
    ServletContext context;

    @GetMapping("/test")
    public String addCustomer() {
        log.info("Test Controller");
        return "Welcome Test";
    }


    @PostMapping("/fileupload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Upload File {} ", file.getOriginalFilename());
        return fileUploadDownloadService.fileUpload(file);
    }

    @GetMapping("/filedownload/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("filename") String filename) {
       log.info("Downloading file {}", filename);
        InputStreamResource resource = fileUploadDownloadService.downloadFile(filename);
        return ResponseEntity.ok()
                // Content-Disposition
                .contentType(getMediaTypeForFileName(context, filename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .body(resource);
    }

    private MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
