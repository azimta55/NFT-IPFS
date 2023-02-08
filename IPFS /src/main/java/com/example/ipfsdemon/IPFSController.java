package com.example.ipfsdemon;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class IPFSController {
	//Azim T.A

    @Autowired
    private IPFSService ipfsService;

    @GetMapping(value = "")
    public String saveText(@RequestParam("filepath") String filepath) {
        return ipfsService.saveFile(filepath);
    }
    
    
 
    
    @PostMapping("/uploadMultipleFiles")
    public  String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files)  {
    	try {
			return ipfsService.saveFiles(files);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		
			e.printStackTrace();
		}
		return null;
    }
    	
    @PostMapping(value = "/uploaddir")
    public String savefiles(@RequestParam("filepath") String filepath) {
        return ipfsService.saveFile(filepath);
    }	
    	

    @PostMapping(value = "upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return ipfsService.saveFile(file);
    }

    @GetMapping(value = "file/{hash}")
    public ResponseEntity<byte[]> getFile(@PathVariable("hash") String hash) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.ALL_VALUE);
        byte[] bytes = ipfsService.loadFile(hash);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytes);

    }

}
