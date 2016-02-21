package com.mukherjee.sankar.dailyreportgenerator.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mukherjee.sankar.dailyreportgenerator.data.DailyReportData;
import com.mukherjee.sankar.dailyreportgenerator.data.MonitorData;
import com.mukherjee.sankar.dailyreportgenerator.data.TabData;
import com.mukherjee.sankar.dailyreportgenerator.security.SecurityWorker;
import com.mukherjee.sankar.dailyreportgenerator.worker.Worker;

/*
	Usage : This is the major class which when ran with a property file can check status of set of URL and generate a human friendly excel report and mail
	        the same to recipient. Property file is of specific format and if you do not have the documentation about the detail mail to author for the same.
	Author: Sankar Mukherjee (sanmuk21@gmail.com)
	Dated : 20 February, 2016
	
 */
public class DailyReportGenerator {

	public static void main(String[] args) {
		System.out.println("******************************************");
		System.out.println("***  Daily Report Generator Execution  ***");
		Date sysDate = new Date();
		System.out.println("*** Date: "+ sysDate +" ***\n");

		if (args.length != 1){
			System.out.println("ERROR: No configuration file passed. Usage: Pass configuration file.");
		} else {
			System.out.println("INFO: Configuration file - "+ args[0]);
			try {
				DailyReportData dailyRepData = loadConfigFile(args[0]);
				validateTabwiseDate(dailyRepData);
				Calendar cal = Calendar.getInstance();
				cal.setTime(sysDate);
				String attachFile = "Report_"+cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH) + 1)+"_"+cal.get(Calendar.YEAR)+".xls";
				createAttachment(attachFile, dailyRepData);
				sendMail(attachFile, dailyRepData);
			} catch (Exception e) {
				System.out.println("ERROR: Cannot continue report generation because of issue.");
				e.printStackTrace();
			}
		}

		sysDate = new Date();
		System.out.println("\n*** End:  "+ sysDate +" ***");
		System.out.println("******************************************");
	}

	private static void sendMail(String attachFile, DailyReportData dailyRepData) throws Exception {
		System.out.println("INFO: In methord sendMail, parameter passed - "+ attachFile);
		String bodyContent = "<html> <body><b>"+ dailyRepData.getEmailHeader() +"</b><br><br><br>";
		bodyContent = bodyContent +"<b>Below is the highlevel overview of the monitoring satus: </b><br><br>";
		bodyContent = bodyContent +"<table border=\"1\" style=\"width:100%\"><tr bgcolor=\"#E0ECF8\"><th>Component</th><th>Status</th></tr>";
		for (TabData tab : dailyRepData.getDailyRepData()) {
			bodyContent = bodyContent +"<tr bgcolor=\"#E0ECF8\"><td>"+ tab.getTabName() +"</td>";
			if (tab.isTabStatus()){
				bodyContent = bodyContent +"<td bgcolor=\"#CEF6CE\">OK</td></tr>";
			} else {
				bodyContent = bodyContent +"<td bgcolor=\"#F5A9A9\">ERROR</td></tr>";
			}
		}
		bodyContent = bodyContent +"</table><br><br><b>"+ dailyRepData.getEmailSignature() +"</b><br>Note: This is a automated mail please do not reply.</body>";
		sendMailAdvanceAttachment(attachFile, bodyContent, dailyRepData);
	}

	public synchronized static void sendMailAdvanceAttachment(String attachFile, String body, DailyReportData dailyRepData) throws Exception {
		System.out.println("INFO: In methord sendMailAdvanceAttachment, parameter passed - "+ attachFile +". Body: "+ body);
		String host = dailyRepData.getSmtpHost();
		String port = dailyRepData.getSmtpPort();
		String userName = dailyRepData.getEmailFrom();
		String password = dailyRepData.getEmailFromPasswd();
		String starttls = dailyRepData.getSslEnable();
		String emailTo = dailyRepData.getEmailTo();
		String emailCC=dailyRepData.getEmailCC();
		String subject = dailyRepData.getEmailSubject();
		if (dailyRepData.isOverallStatus()){
			subject = "INFO "+ dailyRepData.getEmailSubject() +" Dated: "+ new Date();
		} else {
			subject = "ALERT "+ dailyRepData.getEmailSubject() +" Dated: "+ new Date();
		}
		String fallback = "true";                         
		try
		{
			java.util.Properties props = null;
			props = System.getProperties();
			props.put("mail.smtp.user", userName);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.debug", "true");
			if(!"".equals(port))
			{
				props.put("mail.smtp.port", port);
				props.put("mail.smtp.socketFactory.port", port);
			}
			if(!"".equals(starttls))
				props.put("mail.smtp.starttls.enable",starttls);
			if(!"".equals(fallback))
				props.put("mail.smtp.socketFactory.fallback", fallback);
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userName));
			msg.setSubject(subject);                
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			if ( ! emailCC.equalsIgnoreCase("")){
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCC));
			}
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(attachFile);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("Report.xls");
			multipart.addBodyPart(messageBodyPart);
			msg.setContent(multipart);
			msg.setSentDate(new Date());
			msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("ERROR: Error sending Email.");
		}
	}

	private static void createAttachment(String attachFile, DailyReportData dailyRepData) throws Exception {
		System.out.println("INFO: In methord createAttachment, parameter passed - "+ attachFile);	
		Workbook workbook = new XSSFWorkbook();
		XSSFCellStyle styleH = (XSSFCellStyle) workbook.createCellStyle();
		styleH.setBorderBottom((short) 6); 
		XSSFFont fontH = (XSSFFont) workbook.createFont();
		fontH.setFontHeightInPoints((short) 15);
		fontH.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontH.setColor(HSSFColor.GREY_50_PERCENT.index);
		styleH.setFont(fontH);
		XSSFCellStyle styleS = (XSSFCellStyle) workbook.createCellStyle();
		styleS.setBorderBottom((short) 1); 
		styleS.setBorderLeft((short) 1); 
		styleS.setBorderRight((short) 1); 
		XSSFFont fontS = (XSSFFont) workbook.createFont();
		fontS.setFontHeightInPoints((short) 15);
		fontS.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontS.setColor(HSSFColor.GREEN.index);
		styleS.setFont(fontS);
		XSSFCellStyle styleE = (XSSFCellStyle) workbook.createCellStyle();
		styleE.setBorderBottom((short) 1); 
		styleE.setBorderLeft((short) 1); 
		styleE.setBorderRight((short) 1); 
		XSSFFont fontE = (XSSFFont) workbook.createFont();
		fontE.setFontHeightInPoints((short) 15);
		fontE.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fontE.setColor(HSSFColor.RED.index);
		styleE.setFont(fontE);

		XSSFCellStyle styleD = (XSSFCellStyle) workbook.createCellStyle();
		styleD.setBorderBottom((short) 1); 
		styleD.setBorderLeft((short) 1); 
		styleD.setBorderRight((short) 1);
		XSSFFont fontD = (XSSFFont) workbook.createFont();
		fontD.setFontHeightInPoints((short) 15);
		fontD.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		styleD.setFont(fontD);
		for (TabData tab : dailyRepData.getDailyRepData()) {
			Sheet monitorsSheet = workbook.createSheet(tab.getTabName());
			int rowIndex = 1;
			int cellIndex = 1;
			Cell cell = null;
			Row row = monitorsSheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue("No.");
			cell.setCellStyle(styleH);
			cell = row.createCell(cellIndex++);
			cell.setCellValue("Monitored Component");
			cell.setCellStyle(styleH);
			cell = row.createCell(cellIndex++);
			cell.setCellValue("Status");
			cell.setCellStyle(styleH);
			for (MonitorData monD : tab.getMonitorData()) {
				cellIndex = 1;
				row = monitorsSheet.createRow(rowIndex++);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(rowIndex - 2);
				cell.setCellStyle(styleD);
				cell = row.createCell(cellIndex++);
				cell.setCellValue(monD.getMonitorData());
				cell.setCellStyle(styleD);
				if (monD.isMonitorStatus()){
					cell = row.createCell(cellIndex++);
					cell.setCellValue("OK");
					cell.setCellStyle(styleS);
				} else {
					cell = row.createCell(cellIndex++);
					cell.setCellValue("ERROR");
					cell.setCellStyle(styleE);
				}
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(attachFile);
			workbook.write(fos);
			fos.close();
			System.out.println("INFO: File "+ attachFile + " is successfully written");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("ERROR: Error in writting data to excel.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("ERROR: Error in writting data to excel.");
		}
	}

	private static DailyReportData loadConfigFile(String confFile) throws Exception {
		System.out.println("INFO: In methord loadConfigFile, parameter passed - "+ confFile);
		DailyReportData result = new DailyReportData();
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(confFile));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println("INFO: Line read "+ sCurrentLine);
				if (sCurrentLine.split("@@@")[0].equalsIgnoreCase("MONITOR")){
					System.out.println("INFO: Recieved monitoring data of type "+ sCurrentLine.split("@@@")[1] +" and will be entered in Tab "+ sCurrentLine.split("@@@")[2]);
					addMonitorData(result, sCurrentLine.split("@@@")[1], sCurrentLine.split("@@@")[2], sCurrentLine.split("@@@")[3]);
				} else if (sCurrentLine.split("@@@")[0].equalsIgnoreCase("GENERAL")){
					if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("SMTP_HOST")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setSmtpHost(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("SMTP_PORT")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setSmtpPort(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_TO")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailTo(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_CC")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailCC(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_FROM")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailFrom(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EFROM_PASSWORD")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						SecurityWorker sec = new SecurityWorker();
						result.setEmailFromPasswd(sec.decrypt(sCurrentLine.split("@@@")[2]));
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("SSL_ENABLED")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setSslEnable(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_SUBJECT")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailSubject(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_SIGNATURE")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailSignature(sCurrentLine.split("@@@")[2]);
					} else if (sCurrentLine.split("@@@")[1].equalsIgnoreCase("EMAIL_HEADER")){
						System.out.println("INFO: Recieved property - "+ sCurrentLine.split("@@@")[1] +" with value "+ sCurrentLine.split("@@@")[2]);
						result.setEmailHeader(sCurrentLine.split("@@@")[2]);
					} 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("ERROR: Error in loading configuration file. Check if file present or in correct format.");
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new Exception("ERROR: Error in loading configuration file. Check if file present or in correct format.");
			}
		}
		return result;
	}

	private static void validateTabwiseDate(DailyReportData result) throws Exception {
		System.out.println("INFO: In methord validateTabwiseDate, parameter passed - "+ result);
		boolean overallMonStatus = true;
		for (TabData tab : result.getDailyRepData()) {
			System.out.println("INFO: Starting monitoring for Tab "+ tab.getTabName());
			boolean tabStatus = true;
			for (MonitorData monD : tab.getMonitorData()) {
				System.out.println("INFO: Monitoring "+ monD.getMonitorData() +" of type "+ monD.getMonitorType());
				if (monD.getMonitorType().equalsIgnoreCase("URL")){
					try {
						boolean monStat = Worker.checkHTTPStatus(monD.getMonitorData());
						monD.setMonitorStatus(monStat);
						if (monStat == false){
							System.out.println("INFO: Setting overall Tab status and overall Monitoring status to false as monitor validation not success.");
							tabStatus = false ;
							overallMonStatus = false;
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new Exception("ERROR: Error in collecting HTTP Status.");
					}
				} else if (monD.getMonitorType().equalsIgnoreCase("URLS")){
					try {
						boolean monStat = Worker.checkHTTPSStatus(monD.getMonitorData());
						monD.setMonitorStatus(monStat);
						if (monStat == false){
							System.out.println("INFO: Setting overall Tab status to false as monitor validation not success.");
							tabStatus = false ;
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new Exception("ERROR: Error in collecting HTTPS Status.");
					}
				}
			}
			tab.setTabStatus(tabStatus);
		}
		result.setOverallStatus(overallMonStatus);
	}

	private static void addMonitorData(DailyReportData repData, String monType, String tabName, String monData) {
		System.out.println("INFO: In methord addMonitorData, parameter passed - "+ monType +", "+ tabName +", "+ monData);
		boolean foundtab=false;
		for (TabData tab : repData.getDailyRepData()) {
			if (tab.getTabName().equalsIgnoreCase(tabName)) {
				foundtab=true;
				tab.getMonitorData().add(new MonitorData(monType, monData));
				System.out.println("INFO: Added one record to Tab named "+ tab.getTabName());
				break;
			}
		}
		if (foundtab == false){
			TabData newTab = new TabData(tabName);
			newTab.getMonitorData().add(new MonitorData(monType, monData));
			repData.getDailyRepData().add(newTab);
			System.out.println("INFO: New Tab added with name "+ newTab.getTabName());

		}
	}

}
