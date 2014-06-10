package com.marklogic.performanceExamples.util;

import java.io.File;
import java.io.InputStream;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

public class DocumentInsertRunnable implements Runnable {
	private final File file;
	private long enque;
	private long queueDuration;
	private long runtimeDuration;
	private XMLDocumentManager docMgr = null;
	
	public DocumentInsertRunnable(final File file, final XMLDocumentManager docMgr) {
		this.file = file;
		this.docMgr = docMgr;
		//collect queue start time
		this.enque = System.currentTimeMillis();
	}

	public void run() {
		try {
			//collect method start time
			long begin = System.currentTimeMillis();
					
			//create docStream on individual file
			InputStream docStream = DocumentInsertRunnable.class.getClassLoader().getResourceAsStream(
					"data" + File.separator + "medline" + File.separator + file.getName());

			//create a handle on the content - note that instances of Handle CANNOT be shared across threads, so local to this thread
			InputStreamHandle handle = new InputStreamHandle(docStream);
			
			// write the document content
			docMgr.write("/example/" + file.getName(), handle);
			
			docStream.close();

			//collect method end time
			long end = System.currentTimeMillis();
			queueDuration = begin - enque;
			runtimeDuration = end - begin;
		} catch (Exception e) {
			//handle exception
			e.printStackTrace();
		}
	}

	public long getQueueDuration(){
		return queueDuration;
	}
	
	public long getRuntimeDuration(){
		return runtimeDuration;
	}
}
