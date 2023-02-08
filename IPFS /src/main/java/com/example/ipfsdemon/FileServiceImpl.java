package com.example.ipfsdemon;

import org.springframework.web.multipart.MultipartFile;

public interface FileServiceImpl {
	//Azim T.A
    String saveFile(String filePath);
    String saveFile(MultipartFile file);

    byte[] loadFile(String hash);
	
	
	
	
}
