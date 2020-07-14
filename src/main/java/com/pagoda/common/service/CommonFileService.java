package com.pagoda.common.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service("CommonFileService")
public class CommonFileService {
	
	private static final Logger log = LoggerFactory.getLogger(CommonFileService.class);
	
	@Value("#{properties['Globals.uploadFileFullPath']}")
	private String uploadFileFullPath;
	
	@Value("#{properties['Globals.uploadFileFullPathUrl']}")
	private String uploadFileFullPathUrl;
	
	@Value("#{properties['Globals.uploadFileMp3Dir']}")
	private String uploadFileMp3Dir;
	
	@Value("#{properties['Globals.domain']}")
	private String domain;
	
	@Value("#{properties['encode.source.path']}")
	private String encodeSourcePath;

	/**
	 * 파일 DB정보 DELETE / 파일 삭제
	 * 
	 * @param fileNum
	 * @return
	 * @throws Exception
	 */
	public int fileDelete(Map paramMap, String[] fileNum) throws Exception {
		int fileDeleteCnt = 0;
		int i = 0;
		Map map = new HashMap();
		Map fileInfo = null;
		List fileList = new ArrayList();
		File file = null;

		// 파일 삭제
		for (i=0; i<fileList.size(); i++) {
			file = (File) fileList.get(i);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}

		return fileDeleteCnt;
	}


	
	/**
	 * 파일 업로드 : 파일 을 원하는 새이름으로 저장한다
	 * 
	 * @param paramMap
	 * @param files
	 * @return
	 * @throws Exception
	 */
	public Map fileUploadSimple(Map paramMap, MultipartFile[] files) throws Exception {
		int fileUploadCnt = 0;

		String uploadFileContextPath = paramMap.get("saveDir").toString();
		String originalFilename = "";
		String fileExtSn = "";
		String filePath = "";
		String fileName = "";
		File fileDir = null;

		log.debug("files.length : " + files.length);

		if (files.length > 0) {
			
			filePath = uploadFileFullPath + uploadFileContextPath;
			fileDir = new File(filePath);

			// 디렉토리 생성
			if (!fileDir.exists()) {
				Boolean mkdir = fileDir.mkdirs();
				log.debug("direct make : " + mkdir);
			}

			for (MultipartFile file : files) {
				if(file.getSize() > 0){
					originalFilename = file.getOriginalFilename();
					fileExtSn = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
					paramMap.put("fileExtSn", fileExtSn);
					paramMap.put("fileSize", file.getSize());
					paramMap.put("fileOriName", originalFilename);
					
					System.out.println("fileExtSn:"+fileExtSn);
					System.out.println("fileSize:"+file.getSize());
					System.out.println("fileOriName:"+originalFilename);
					
					if(paramMap.containsKey("newFileNm")){					
						fileName = paramMap.get("newFileNm").toString() + "." + fileExtSn;	
					}else{
						fileName = file.getOriginalFilename();						
					}
					
					file.transferTo(new File(filePath + fileName));	
					paramMap.put("fileName", fileName);
					//paramMap.put("fileUrl", uploadFileContextPath + fileName);
				}
			}
		}

		return paramMap;
	}
	
	public String upload( HttpServletRequest request,String apply_no, String target_name ) throws IllegalStateException, IOException{
		
		log.info("target_name = " + target_name);
		
		//폴더명
		String path = uploadFileFullPath + apply_no;
		
		MultipartHttpServletRequest multi_request = (MultipartHttpServletRequest) request;
		MultipartFile file_part = multi_request.getFile( target_name );
		
		//String newFileNm = "";
		String filename = "";
		
		log.info("file_part = " + file_part);
		
		if(file_part != null && file_part.getSize() > 0){
		
			filename = file_part.getOriginalFilename();
			
			
			//String fileExtSn = filename.substring(filename.lastIndexOf(".")+1);
			
			// 신규파일네임 규약
			//String todate = (new SimpleDateFormat("yyyyMMddHHmmss")).format( new java.util.Date());
			
			//if(apply_no.equals("")) apply_no = filename.substring(0,filename.lastIndexOf(".")-1);
			
			//newFileNm = apply_no+"_"+ todate +  "." +  fileExtSn;
			
			File file_io = new File( path );		
			if( !file_io.exists() ){
				
				file_io.mkdirs();
				
			}
			
			file_io = new File( path + File.separator + filename );
			
			log.debug("upload file full path : " + path + File.separator + filename );
			
			//업로드
			file_part.transferTo( file_io );
			
		}
		else{
			return "";
		}
	
		return domain + uploadFileFullPathUrl + apply_no + "/" + filename;
		
	}
	
	public String uploadExcel( HttpServletRequest request,String apply_no, String target_name ) throws IllegalStateException, IOException{
		
		log.info("target_name = " + target_name);
		
		//폴더명
		String path = uploadFileFullPath + target_name;
		
		MultipartHttpServletRequest multi_request = (MultipartHttpServletRequest) request;
		MultipartFile file_part = multi_request.getFile( target_name );
		
		String newFileNm = "";
		
		log.info("file_part = " + file_part);
		
		if(file_part != null && file_part.getSize() > 0){
		
			String filename = file_part.getOriginalFilename();
			
			
			String fileExtSn = filename.substring(filename.lastIndexOf(".")+1);
			
			// 신규파일네임 규약
			String todate = (new SimpleDateFormat("yyyyMMddHHmmss")).format( new java.util.Date());
			
			if(apply_no.equals("")) apply_no = filename.substring(0,filename.lastIndexOf(".")-1);
			
			newFileNm = apply_no+"_"+ todate +  "." +  fileExtSn;
			
			File file_io = new File( path );		
			if( !file_io.exists() ){
				
				file_io.mkdirs();
				
			}
			
			file_io = new File( path + File.separator + newFileNm );
			
			log.debug("upload file full path : " + path + File.separator + newFileNm );
			
			//업로드
			file_part.transferTo( file_io );
			
		}
		else{
			return "";
		}
	
		return path + File.separator + newFileNm;
		
	}
	
	public String uploadMedia( HttpServletRequest request,String apply_no, String target_name ) throws IllegalStateException, IOException{
		
		//폴더명
		String path = encodeSourcePath + "/" + target_name;
		
		MultipartHttpServletRequest multi_request = (MultipartHttpServletRequest) request;
		MultipartFile file_part = multi_request.getFile( target_name );
		
		String newFileNm = "";
		
		if(file_part != null && file_part.getSize() > 0){
		
			String filename = file_part.getOriginalFilename();
			
			
			String fileExtSn = filename.substring(filename.lastIndexOf(".")+1);
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			// 신규파일네임 규약
			String todate = (new SimpleDateFormat("yyyyMMddHHmmss")).format( new java.util.Date());
			
			if(apply_no.equals("")) apply_no = filename.substring(0,filename.lastIndexOf(".")-1);
			
			newFileNm = apply_no+"_"+ todate +"_" + uuid +  "." +  fileExtSn;
			
			File file_io = new File( path );		
			if( !file_io.exists() ){
				
				file_io.mkdirs();
				
			}
			
			file_io = new File( path + "/" + newFileNm );
			
			//log.debug("upload file full path : " + path + "/" + newFileNm );
			
			//업로드
			file_part.transferTo( file_io );
			
			
		}
		else{
			return "";
		}
	
		return path + "/" + newFileNm;
		
	}

}
