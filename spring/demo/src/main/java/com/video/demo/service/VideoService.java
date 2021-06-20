package com.video.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

	@Autowired
	private VideoConfiguration videoConfiguration;

	private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

	private static final int CHUNK_SIZE = 1024 * 1024 * 1; //1MB Chunk

	public ResponseEntity<byte[]> stream(String range) {
		Map<String, File> videos = videoConfiguration.getFileList();
		File videoFile = videos.get("India.mp4");
		return getContent(videoFile, range);
	}

	private ResponseEntity<byte[]> getContent(File file, String range) {

		int start = 0;
		int end = 0;
		byte[] data;
		Long fileSize = file.length();
		String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		try {
			if (range == null) {
			    logger.info("Empty Range parameter!");
			    
				return ResponseEntity.status(HttpStatus.OK)
						.header("Content-Type", "video" + "/" + fileType)
						.header("Content-Length", String.valueOf(fileSize))
						.body(readByteRange(file, start, fileSize - 1));
			}

			logger.info("Request Range: {} ", range);

			String[] ranges = range.split("=")[1].split("-");
			start = Integer.parseInt(ranges[0]);
			
	        end = CHUNK_SIZE + start;
	        
	        if ( end >= fileSize) {
	            end = (int) ( fileSize - 1 );
	        }
	        final String responseRange = String.format( "bytes %d-%d/%d", start, end, fileSize);
	        logger.info( "Response Content-Range: {} \n", responseRange);
	        
			data = readByteRange(file, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		String contentLength = String.valueOf((end - start) + 1);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.header(HttpHeaders.CONTENT_TYPE, "video" + "/" + fileType)
				.header(HttpHeaders.ACCEPT_RANGES, "bytes")
				.header(HttpHeaders.CONTENT_LENGTH, contentLength)
				.header(HttpHeaders.CONTENT_RANGE, "bytes" + " " + start + "-" + end + "/" + fileSize).body(data);
	}

	
	public byte[] readByteRange(File file, long start, long end) throws IOException {

		try (InputStream inputStream = new FileInputStream(file);
		
			ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
		
			byte[] data = new byte[CHUNK_SIZE];
			int nRead;
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				bufferedOutputStream.write(data, 0, nRead);
			}
			bufferedOutputStream.flush();
			byte[] result = new byte[(int) (end - start) + 1];
			System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
			return result;
		}
	}
}
