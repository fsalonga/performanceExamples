package com.marklogic.performanceExamples.util;

public class StatsResult {
	protected long queueAverage = 0;
	protected long queueSD = 0;
	protected long runtimeAverage = 0;
	protected long runtimeSD = 0;
	protected long totalTime = 0;

	public StatsResult(long queueAverage, long queueSD, long runtimeAverage, long runtimeSD, long totalTime){
		this.queueAverage = queueAverage;
		this.queueSD = queueSD;
		this.runtimeAverage = runtimeAverage;
		this.runtimeSD = runtimeSD;
		this.totalTime = totalTime;
	}
	
	public long getQueueAverage() {
		return queueAverage;
	}
	public void setQueueAverage(long queueAverage) {
		this.queueAverage = queueAverage;
	}
	public long getQueueSD() {
		return queueSD;
	}
	public void setQueueSD(long queueSD) {
		this.queueSD = queueSD;
	}
	public long getRuntimeAverage() {
		return runtimeAverage;
	}
	public void setRuntimeAverage(long runtimeAverage) {
		this.runtimeAverage = runtimeAverage;
	}
	public long getRuntimeSD() {
		return runtimeSD;
	}
	public void setRuntimeSD(long runtimeSD) {
		this.runtimeSD = runtimeSD;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

}
