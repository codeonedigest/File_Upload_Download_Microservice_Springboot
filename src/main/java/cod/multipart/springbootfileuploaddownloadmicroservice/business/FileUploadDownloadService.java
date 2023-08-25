package cod.multipart.springbootfileuploaddownloadmicroservice.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileUploadDownloadService {

    @Value("${cod.file.storage.location}")
    String fileStorageLocation;

    public String fileUpload(MultipartFile file) {
        String messageResponse = null;

        if (file.isEmpty()) {
            messageResponse = "Please select a valid file to upload";
            return messageResponse;
        }
        log.info("Upload File {} ", file.getOriginalFilename());
        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(fileStorageLocation + file.getOriginalFilename());
            Files.write(path, bytes);

            messageResponse = "You successfully uploaded '" + file.getOriginalFilename() + "'";

        } catch (IOException e) {
            e.printStackTrace();
        }

        return messageResponse;
    }


    public InputStreamResource downloadFile( String fileName){
        log.info("Downloading file {} ", fileName);
        InputStreamResource resource = null;
        try {
            File file = new File(fileStorageLocation +  fileName);
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return resource;
    }

}
