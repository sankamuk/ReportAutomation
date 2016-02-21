package com.mukherjee.sankar.dailyreportgenerator.data;

import java.util.ArrayList;

public class DailyReportData {

	String smtpHost;
	String smtpPort;
	String emailTo;
	String emailCC;
	String emailFrom;
	String emailFromPasswd;
	String sslEnable;
	String emailSignature;
	String emailSubject;
	String emailHeader;
	boolean overallStatus;
	ArrayList<TabData> dailyRepData;
	
	public DailyReportData() {
		this.smtpHost="smtp.gmail.com";
		this.smtpPort="587";
		this.emailTo="sanmuk21@gmail.com";
		this.emailCC="";
		this.emailFrom="sanmuk.testing.work";
		this.emailFromPasswd="somenath";
		this.sslEnable="true";
		this.emailSignature="Thanks, Daily Report Administrator";
		this.emailSubject=" : Daily Report : ";
		this.emailHeader="Hi, Below is the status of Daily Report.";
		this.overallStatus=false;
		this.dailyRepData = new ArrayList<TabData>();
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailCC() {
		return emailCC;
	}

	public void setEmailCC(String emailCC) {
		this.emailCC = emailCC;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailFromPasswd() {
		return emailFromPasswd;
	}

	public void setEmailFromPasswd(String emailFromPasswd) {
		this.emailFromPasswd = emailFromPasswd;
	}

	public String getSslEnable() {
		return sslEnable;
	}

	public void setSslEnable(String sslEnable) {
		this.sslEnable = sslEnable;
	}

	public String getEmailSignature() {
		return emailSignature;
	}

	public void setEmailSignature(String emailSignature) {
		this.emailSignature = emailSignature;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailHeader() {
		return emailHeader;
	}

	public void setEmailHeader(String emailHeader) {
		this.emailHeader = emailHeader;
	}

	public boolean isOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(boolean overallStatus) {
		this.overallStatus = overallStatus;
	}

	public ArrayList<TabData> getDailyRepData() {
		return dailyRepData;
	}

	public void setDailyRepData(ArrayList<TabData> dailyRepData) {
		this.dailyRepData = dailyRepData;
	}

	@Override
	public String toString() {
		return "DailyReportData [smtpHost=" + smtpHost + ", smtpPort="
				+ smtpPort + ", emailTo=" + emailTo + ", emailCC=" + emailCC
				+ ", emailFrom=" + emailFrom + ", emailFromPasswd="
				+ emailFromPasswd + ", sslEnable=" + sslEnable
				+ ", emailSignature=" + emailSignature + ", emailSubject="
				+ emailSubject + ", emailHeader=" + emailHeader
				+ ", overallStatus=" + overallStatus + ", dailyRepData="
				+ dailyRepData + "]";
	}

	
}
