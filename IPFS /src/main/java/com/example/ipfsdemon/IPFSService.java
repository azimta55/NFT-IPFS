package com.example.ipfsdemon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ipfs.payload.UploadFileResponse;
import com.ipfs.property.FileStorageProperties;


import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;




@Service
public class IPFSService implements FileServiceImpl {
	//Azim T.A	
	
	
	@Autowired
    private FileStorageProperties properties;
    private IPFSService fileStorageService;
    private boolean createDir = true;
    
	  
    @Autowired
    IPFSConfig ipfsConfig;
    
    private Path fileStorageLocation;
    private String packageName;

    @Override
    public String saveFile(String filePath) {
        try {
            IPFS ipfs = ipfsConfig.ipfs;
            File _file = new File(filePath);
            NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(_file);
            MerkleNode response = ipfs.add(file).get(0);
            System.out.println("Hash (base 58): " + response.hash.toBase58());
            return response.hash.toBase58();
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }
    
    
    
    
    public String saveFiles(MultipartFile[] files) throws Exception {
    	// TODO Auto-generated method stub
    	this.properties.setUploadDir(properties.getUploadDir()+"/"+packageName);
    	this.fileStorageService = new IPFSService(properties);
    	setCreateDir(false);
    	
    	uploadFile("files", "packageName");
		return packageName;
                
    	                
    		}

    
    @Override
    public String saveFile(MultipartFile file) {


        try {

            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            IPFS ipfs = ipfsConfig.ipfs;

            NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
            MerkleNode response = ipfs.add(is).get(0);
            System.out.println("Hash (base 58): " + response.name.get() + " - " + response.hash.toBase58());
            return response.hash.toBase58();

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }

    }
    
    

	
    
    

    @Override
    public byte[] loadFile(String hash) {
        try {

            IPFS ipfs = ipfsConfig.ipfs;
            Multihash filePointer = Multihash.fromBase58(hash);

            return ipfs.cat(filePointer);
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }
    
    



	
	public   UploadFileResponse uploadFile(String file, String packageName) throws Exception {
		
		if(isCreateDir()) {
    		properties.setUploadDir(properties.getUploadDir()+"/"+packageName);
        	fileStorageService = new IPFSService(properties);
    	}
    	
    	    	    	
    	String fileName = fileStorageService.saveFile(file);
                

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        
        return new UploadFileResponse(fileName, fileDownloadUri, fileDownloadUri, 0);
               
        
	}
	


public boolean isCreateDir() {
	return createDir;
}
@Autowired
public  IPFSService(FileStorageProperties fileStorageProperties) throws Exception {
	
	
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
            .toAbsolutePath().normalize();

    try {
        Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
        throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
    }
}




public void setCreateDir(boolean createDir) {
	this.createDir = createDir;
}





}	



