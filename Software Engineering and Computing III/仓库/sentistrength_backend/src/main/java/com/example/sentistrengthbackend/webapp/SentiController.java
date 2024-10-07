package com.example.sentistrengthbackend.webapp;

import com.example.sentistrengthbackend.service.SentiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
;import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/sentistrength")
public class SentiController {
    SentiService sentiService = new SentiService();

    @GetMapping("/runWithText")
    public String runWithText(String args) {
        return sentiService.runWithText(args);
    }

    @PostMapping("/upload")
    public void fileUploads(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        sentiService.fileUploads(request, file);
    }

    @GetMapping("runWithFile")
    public String runWithFile(String args) {
        return sentiService.runWithFile(args);
    }
}
