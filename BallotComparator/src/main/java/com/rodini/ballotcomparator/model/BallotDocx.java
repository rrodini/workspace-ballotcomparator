package com.rodini.ballotcomparator.model;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

import com.rodini.ballotcomparator.LoadDocxView;
import com.rodini.ballotcomparator.view.InitializeUI;
import com.rodini.ballotcomparator.view.PaginationBar;
import static com.rodini.ballotcomparator.Views.*;

public class BallotDocx {
	
	private String folderPath;
	private List<String> files;  // all docx files in folderPath
	private int count; // count == files.size()
	private int index; // index of current file
	public String fileName; // current docx file
	
	BallotDocx(String folderPath) {
		this.folderPath = folderPath;
		loadFiles();
	}
	
	public void loadFiles() {
		System.out.println("BallotDocx:load() called.");
		File path = new File(folderPath);
		files = Stream.of(path.listFiles())
				.filter(file -> !file.isDirectory() && file.getName().endsWith(".docx"))
				.map(File::getName)
				.sorted()  // alphabetic order by file name
				.collect(toList());
		// debug
//		System.out.println("Docx files:");
//		for (String name: files) {
//			System.out.println(name);
//		}
		index = 1;
		count = files.size();
		System.out.printf("BallotDocx: index: %s count: %d%n", index, count);
		
		PaginationBar bar = InitializeUI.getPaginationBar();
//		bar.setCurrentPage(1);
		bar.setTotalPages(count);
		// Show the first file.
//		docxChange(1);
	}
	
	
	public String getNextFile() {
		return "";
	}
	
	public void setIndex(int index) {
		this.index = index;
		docxChange();
	}

	public int getIndex() {
		return index;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFilePath() {
		return folderPath + File.separator + fileName;
	}
	
	// called from PaginationBar
	public void docxChange() {
		LoadDocxView ldv = new LoadDocxView(InitializeUI.getCompareView(), RIGHT_VIEW);
		fileName = files.get(index - 1);
		String docxFilePath = folderPath +  File.separator + fileName;
		ldv.load(docxFilePath);
		PaginationBar bar = InitializeUI.getPaginationBar();
		bar.setCurrentPage(index);
		InitializeUI.updateTitleBar();
	}
}
