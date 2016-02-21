package com.mukherjee.sankar.dailyreportgenerator.data;

public class MonitorData {
	String monitorType;
	String monitorData;
	boolean monitorStatus;
	
	public MonitorData(String t, String d) {
		this.monitorType=t;
		this.monitorData=d;
		this.monitorStatus=false;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public String getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(String monitorData) {
		this.monitorData = monitorData;
	}

	public boolean isMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(boolean monitorStatus) {
		this.monitorStatus = monitorStatus;
	}
	
	@Override
	public String toString() {
		return "Monitor type "+ monitorType + ", Monitoring "+ monitorData +", Status "+ monitorStatus;
	}

}
