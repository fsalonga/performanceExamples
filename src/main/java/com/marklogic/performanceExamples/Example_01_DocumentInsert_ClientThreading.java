/*
 * Copyright 2012 MarkLogic Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.performanceExamples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.performanceExamples.util.ClientManager;
import com.marklogic.performanceExamples.util.Config;
import com.marklogic.performanceExamples.util.Database;
import com.marklogic.performanceExamples.util.DocumentInsertRunnable;
import com.marklogic.performanceExamples.util.Forest;
import com.marklogic.performanceExamples.util.StatsResult;
import com.marklogic.performanceExamples.util.SessionManager;
import com.marklogic.performanceExamples.util.Stats;
import com.marklogic.xcc.Session;

public class Example_01_DocumentInsert_ClientThreading {
	//set the numThreads to use
	protected static final int TOO_FEW_CLIENT_THREADS = 1;
	protected static final int ENOUGH_CLIENT_THREADS = 8;
	protected static final int TOO_MANY_CLIENT_THREADS = 50;	
	
	private static Session SESSION = null;
	
	public static synchronized Session getSession(){
		if(SESSION == null){
			//SESSION references are used for XCC/MarkLogic admin level operations
			SESSION = SessionManager.createSession(Config.admin_user, Config.admin_password, Config.host, Config.xdbc_port);
		}
		return SESSION;
	}
	
	public static void main(String[] args){
		System.out.println("example: "+Example_01_DocumentInsert_ClientThreading.class.getName());
		System.out.println("ClientThreads\tTotalTime\tQueueAvg\tQueueSD\t\tRuntimeAvg\tRuntimeSD");
		
		try {
			int[] clientThreadCounts = {TOO_FEW_CLIENT_THREADS, ENOUGH_CLIENT_THREADS, TOO_MANY_CLIENT_THREADS};
			StatsResult result;
			for(int i = 0; i < clientThreadCounts.length; i++){
				result = doTest(clientThreadCounts[i]);
				System.out.println(clientThreadCounts[i] + "\t\t" +
						result.getTotalTime() + "\t\t" +
						result.getQueueAverage() + "\t\t" + result.getQueueSD() + "\t\t" +
						result.getRuntimeAverage() + "\t\t" + result.getRuntimeSD());
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static StatsResult doTest(int numThreads){
		StatsResult result = null;
		
		try{
			//initialize the XCC session
			getSession();
			
			String dbName = Example_01_DocumentInsert_ClientThreading.class.getSimpleName();
			String forestName = Example_01_DocumentInsert_ClientThreading.class.getSimpleName();
			
			//if forest exists, detach, then delete
			if(Forest.exists(forestName, SESSION)){
				Database.detachForest(dbName, forestName, SESSION);					
				Forest.delete(forestName, SESSION);
			}
			//create a forest
			Forest.create(forestName, SESSION);
			
			//if database exists, delete
			if(Database.exists(dbName, SESSION)){
				Database.delete(dbName, SESSION);
			}
			//create a database
			Database.create(dbName, SESSION);
			
			//attach the forest to database
			Database.attachForest(dbName, forestName, SESSION);

			//acquire all the content in a directory		
			File fileDir = new File((Example_01_DocumentInsert_ClientThreading.class.getClassLoader().getResource("data/medline")).getFile());
			File[] files = fileDir.listFiles();

			//create the Java API client
			DatabaseClient client = ClientManager.createClient();
			//create the Java API document manager
			XMLDocumentManager docMgr = client.newXMLDocumentManager();
			
			//create the thread pool
			ExecutorService executor = Executors.newFixedThreadPool(numThreads);

			ArrayList<DocumentInsertRunnable> l = new ArrayList<DocumentInsertRunnable>();

			long begin = System.currentTimeMillis();
			//for each file in directory, spawn a thread, share docMgr across all threads created
			for(int i = 0; i < files.length; i++){
				DocumentInsertRunnable worker = new DocumentInsertRunnable(files[i], docMgr);
				executor.execute(worker);
				l.add(worker);
			}
			// This will make the executor accept no new threads and finish all existing threads in the queue
			executor.shutdown();
			// Wait until all threads are finished
			executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
			long end = System.currentTimeMillis();

			if(client != null){
				// release the client
				client.release();
				client = null;
				docMgr = null;
			}			
			
			//after all threads complete, output average and stdev time taken			
			DocumentInsertRunnable thread;
			//int index = 1;
			Iterator<DocumentInsertRunnable> i = l.iterator(); 
			
			List<Long> queueList = new ArrayList<Long>();
			List<Long> runtimeList = new ArrayList<Long>();
			while(i.hasNext()){
				thread = i.next();
				//System.out.println(index++ + "\tclient queue (ms): " + thread.getQueueDuration() + "\truntime (ms): " + thread.getRuntimeDuration());
				queueList.add(thread.getQueueDuration());
				runtimeList.add(thread.getRuntimeDuration());
			}
			
			result = new StatsResult(Stats.average(queueList), Stats.stdev(queueList), Stats.average(runtimeList), Stats.stdev(runtimeList), end-begin);
			//System.out.println("client queue average +\\- stdev:\t" + Math.average(queueList) + " +\\- " + Math.stdev(queueList));
			//System.out.println("runtime average +\\- stdev:\t" + Math.average(runtimeList) + " +\\- " + Math.stdev(runtimeList));
			//System.out.println("total time:\t" + (end-begin));
			
			//detach forest
			Database.detachForest(dbName, forestName, SESSION);
			//delete forest
			Forest.delete(forestName, SESSION);
			//delete the database
			Database.delete(dbName, SESSION);

			//close the session
			if(SESSION != null){
				SESSION.close();
				SESSION = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
	}
}
