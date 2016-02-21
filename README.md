Introduction
It is often a regular requirement in almost every infrastructure support team to perform regular health check and send status mail for the activity and most often this activity is bounded by SLA/KPI. This activity is at the same time repetitive, time consuming and often prone to human error. Report Automation Tool helps in solving this problem by automating the complete process end to end without compromising in any of the aspect of the task thus could replace the human activity completely.
Report Automation Tool is a simple Java based tool which tries to automate the reporting activity end to end thus saves large man hour everyday leading to a more efficient and productive environment. Before going any further let us look at the tools key features
1.	Complete Java based tool thus require only JRE for execution and no other requirement.
2.	Platform independent fit to run in UNIX, Windows or practically any environment with JRE support.
3.	Rich report generation which is very human friendly and matching human generated quality.
4.	Tool can be executed with just one property file which in minimum form is fully functional (very easy to get going) and also can be used to customise the tool drastically (customise as per requirement).
Next we will pick a use case where there is a regular reporting requirement which we will try to solve using the tool.



Example Use Case
This section we will pick one sample requirement and describe the scenario and in later section will see how the tool can easily be use to get rid of human effort.
A team of Infra Support engineer ABC work for a client XYZ Company. It is very critical for XYZ Company’s business that three of the major service is available round the clock. 
All the services are Web based and below are the same.
Web Search Engine
https://www.google.co.in/
Web Mail
https://mail.google.com
Company Web Page
http://www.cucse.org/

Thus to avoid prompt identification of issue XYZ Company has added a regular task for ABC team to check the important service thrice a day and send Status email to all concerned members. As of now team is performing this validation thrice per day and send a status mail with over all status with an attached excel with three Tab for Search Engine, Web Mail and Company Web Page containing status of the links within them. In later section we will see how easily this regular task can be automated with Report Automation Tool.
Tool Usage Guide
Report Automation Tool is extremely simple to configure and use, within minute you can set this tool up and your reporting requirements will get automated from that point of time. Now i will guide you through the configuration and usage of this tool.
Set up your platform 
The only basic prerequisite of this tool is Java (Runtime Edition) available on the platform where you want to use this tool. You can validate the presence of the same by typing the below command in a terminal.
 
In case you don’t get a java version returned you can install Java from Oracle site.
NOTE: The tool is compiled with Java version 7, thus please make sure you install and make Java version 7 or later available for the execution. In case you want to execute this tool with older Java version please contact support (support contact provided later).
Once Java is available you’re ready to run the tool download the tool which is a executable jar from the following link.

Configuration
Create the configuration file required to execute the jar, detail of the configuration file parameter is given in next section but for completion of this section let us assume you have already created a configuration file i.e. Report.txt as shown below.
 
NOTE: The name of the file is immaterial here as we will pass the file as a parameter in the jar execution.
Execution
Once we are done we start running the tool as 
java –jar <jar home>/dailyreport.jar <configuration file home>/< configuration file>
Below is a sample execution.
 
Configuration File
The configuration file tells the tool what to monitor and also helps you to customise and control the reporting. Other than the monitoring lines all report customisation and control line has some default thus will help you avoid unnecessary configuration if not required.
Monitor Section
Line 
MONITOR@@@URL@@@[TAB]@@@[MONITOR URL]
Meaning
Monitor HTTP URL "[MONITOR URL]" and group it under "[TAB]" name. It's status will come under "[TAB]" in overall report and under "[TAB]" in attached excel.
Example
MONITOR@@@URL@@@HTTP@@@http://www.cucse.org/

Line 
MONITOR@@@URLS@@@[TAB]@@@[MONITOR URL]
Meaning
Monitor secured HTTPS  URL "[MONITOR URL]" and group it under "[TAB]" name. It's status will come under "[TAB]" in overall report and under "[TAB]" in attached excel.
Example
MONITOR@@@URLS@@@HTTPS@@@https://www.google.co.in/

Note: Currently the tool is only capable of monitoring URL but it is developed to accommodate new monitoring capability to be added for any future requirement.
Report Control and Customization
Line 
GENERAL@@@SMTP_HOST@@@[SMTP SERVER HOST]
Meaning
Email SMPT Server hostname. Default: Gmail SMTP Server
Example
GENERAL@@@SMTP_HOST@@@smtp.gmail.com

Line 
GENERAL@@@SMTP_PORT@@@[SMTP SERVER PORT]
Meaning
Email SMPT Server port. Default: Gmail SMTP Server port
Example
GENERAL@@@SMTP_PORT@@@587


Line 
GENERAL@@@EMAIL_TO@@@[EMAIL ID]
Meaning
Email Id to with report to be send. Default: sanmuk21@gmail.com
Example
GENERAL@@@EMAIL_TO@@@sanmuk21@gmail.com

Line 
GENERAL@@@EMAIL_CC@@@[EMAIL ID]
Meaning
Email Id to be kept in CC in report mail. Default: 
Example
GENERAL@@@EMAIL_TO@@@sanmuk21@gmail.com

Line 
GENERAL@@@EMAIL_FROM@@@[EMAIL ID]
Meaning
Email Id from which report mail will be send. Default: sanmuk.testing.work@gmail.com
Example
GENERAL@@@EMAIL_FROM@@@sanmuk.testing.work

Line 
GENERAL@@@EFROM_PASSWORD@@@[ENCRYPTED PASSWORD]
Meaning
Password for the email Id from which report mail will be send. Default: ******
Example
GENERAL@@@ EFROM_PASSWORD @@@ZM4FI7WARG8Or7Tz97PYNw==

NOTE: In above field the password provided should be an encrypted text your original password. To get an encrypted text for your password use the same tool jar as below.
java -cp dailyreport.jar com.mukherjee.sankar.dailyreportgenerator.security.SecurityWorker
It will ask for password and once provided it will provide you the encrypted text for your password which then will go in the above mentioned EFROM_PASSWORD field.
 

Line 
GENERAL@@@SSL_ENABLED@@@[STATUS]
Meaning
Status of the SMTP Server SSL Enabled. Default: true
Example
GENERAL@@@SSL_ENABLED@@@true

Line 
GENERAL@@@EMAIL_SUBJECT@@@[SUBJECT STRING]
Meaning
Subject string of the report mail. Default: Daily Report
Example
GENERAL@@@EMAIL_SUBJECT@@@Daily Report

Line 
GENERAL@@@EMAIL_HEADER@@@[HEADER STRING]
Meaning
Header line of the report mail. Default: Hi, Below is the status of Daily Report.
Example
GENERAL@@@EMAIL_HEADER@@@Hi, <BR>Below is the status of Daily Report.

Line 
GENERAL@@@EMAIL_SIGNATURE@@@[SIGNATURE STRING]
Meaning
Signature line of the report mail. Default: Thanks, Daily Report Administrator
Example
GENERAL@@@EMAIL_SIGNATURE@@@Thanks, <BR>Daily Report Administrator

NOTE: For both text fields for EMAIL_SIGNATURE and EMAIL_HEADER you can give multiline data using HTML next line string notation <BR>.
Example Execution
Now that we are all set let us run the tool for our imaginary use case discussed in previous section. In the configuration file we will put 3 monitor lines to monitor 3 Services that was required to be validated and we will mail it to concerned peoples group email id in our case is sanmuk21@gmail.com.
Thus the configuration file looks like below.
 
Now we are all set to execute so let’s execute.
 
You should receive a mail in your inbox as shown below.
 
 
