package com.mukherjee.sankar.dailyreportgenerator.data;

import java.util.ArrayList;

public class TabData {
	
	String tabName;
	boolean tabStatus;
	ArrayList<MonitorData> monitorData;
	
	public TabData(String tN) {
		this.tabName = tN;
		this.tabStatus = false ;
		this.monitorData = new ArrayList<MonitorData>();
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public boolean isTabStatus() {
		return tabStatus;
	}

	public void setTabStatus(boolean tabStatus) {
		this.tabStatus = tabStatus;
	}

	public ArrayList<MonitorData> getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(ArrayList<MonitorData> monitorData) {
		this.monitorData = monitorData;
	}

	@Override
	public String toString() {
		return "TabData [tabName=" + tabName + ", tabStatus=" + tabStatus
				+ ", monitorData=" + monitorData + "]";
	}
	
	

}
