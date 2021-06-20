package com.video.demo.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class VideoConfiguration {

	private String videoLocation = "videos";

	private Map<String, File> videos = new HashMap<>();

	public Map<String, File> getFileList() {
		File dir = new File(videoLocation);
		videos.clear();
		videos.putAll(Arrays.asList(dir.listFiles()).stream().collect(Collectors.toMap(f -> {return f.getName();}, f -> f)));
		return videos;
	}
}
