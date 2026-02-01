package com.rodini.ballotcomparator.model;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rodini.ballotcomparator.LoadDocxView;
import com.rodini.ballotcomparator.view.InitializeUI;
import com.rodini.ballotcomparator.view.PaginationBar;
import static com.rodini.ballotcomparator.VIEWS.*;
/**
 * BallotDocx acts as the container for all of the BallotGen
 * docx files. Upon creation it reads all of the .docx files
 * in the folderPath folder (typically .../chester-ouput) and 
 * loads the current docx file into the LoadDocxView object.
 * 
 * Notes:
 * - The count and the index values must coordinate with the pagination bar.
 */
public class BallotDocx {
	private Logger logger = LogManager.getLogger(BallotDocx.class);
	private String folderPath;
	private List<String> files;  // all docx files in folderPath
	private int count; // count == files.size()
	private int index; // index of current file
	public String fileName; // current docx file
	// constructor.
	BallotDocx(String folderPath) {
		this.folderPath = folderPath;
		loadFiles();
		logger.debug("BallotDocx object creatd on path: " + folderPath);
	}
	/**
	 * loadFiles loads the .docx files in the folderPath.
	 */
	private void loadFiles() {
		File path = new File(folderPath);
		files = Stream.of(path.listFiles())
				.filter(file -> !file.isDirectory() && file.getName().endsWith(".docx"))
				.map(File::getName)
				// Remove annoying MacOS shadow files.
				.filter(name -> !name.startsWith("."))
				.sorted()  // alphabetic order by file name
				.collect(toList());
		index = 1;
		count = files.size();
		PaginationBar bar = InitializeUI.getPaginationBar();
		bar.setTotalPages(count);
	}
	
	/**
	 * setIndex sets the index into the file list and 
	 * triggers the loading of a new docx file into view.
	 * 
	 * Notes:
	 * - Called upon reading properties file.
	 * - Called from pagination bar interaction.
	 * @param index of file to display.
	 */
	public void setIndex(int index) {
		this.index = index;
		docxChange();
	}
	/**
	 * getIndex gets the current index into the file list.
	 * @param index of current file.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * setCount sets the count of the files in the file list.
	 * @param count number of files in the file list.
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * getFilePath gets the absolute file path of the current file.
	 * @return abs. file path.
	 */
	public String getFilePath() {
		return folderPath + File.separator + fileName;
	}
	
	// called from PaginationBar
	/** 
	 * docxChange is called to display a new .docx file. 
	 * It creates a new LoadDocxView object and updates other
	 * widgets.
	 */
	public void docxChange() {
		LoadDocxView ldv = new LoadDocxView(InitializeUI.getCompareView(), RIGHT_VIEW);
		fileName = files.get(index - 1);
		logger.info("Loading: " + fileName);
		String docxFilePath = folderPath +  File.separator + fileName;
		ldv.load(docxFilePath);
		PaginationBar bar = InitializeUI.getPaginationBar();
		bar.setCurrentPage(index);
		InitializeUI.updateTitleBar();
	}
}
