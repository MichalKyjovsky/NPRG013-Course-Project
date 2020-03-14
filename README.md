# NPRG013-Course-Project
Application for maintaing overview of personal budget in .xlsx files. 

## Purpose 
If you ever tried to track your expenses, you might get ot hte situation when still opening excel files 
and manualy selects particular cell, but accidently missed or used some unwanted functionality, which excel
provides in vast range. This idea is to make this tracking simplier. Due to the limitation of current Java
development knowledge I was limited to desktop window application. But in following course of advanced Java I 
tend to develop upgraded version, which would be web or mobile based and handling the tracking activities will
be smoother.


## Instalation step by step
**Prerequisities** 
- Installed **Java 11+**
- Installed **Git**
- Installed **Maven**

Download the repository on your local machine via command:
  ```
  git clone (https://github.com/MichalKyjovsky/NPRG013-Course-Project.git)
  ```
Once you have repository downloaded and prerequisities are fulfilled
you can move into the project root directory where ***pom.xml*** is located
and run the project via commands:
  ```
  mvn process-resources
  mvn compile
  mvn install
  mvn javafx:compile
  mvn javafx:run
  ```
  
After commands above were executed, landing page of the application will display.  
![Landing page](./Documentation/LandingPage.PNG)

In current version of application are only features *Create new workbook* and *Open from local device* fully operational. 
Feature *Open from OneDrive* is partly included in *Open from local device* if you have your hard drive synchronized with 
your OneDrive account, but original intention, to grab your file from cloud directly was not finished and it is planned for 
web-based version of the application. 

If you press *Create new workbook*, new **.xlsx** will be created. As shown on images below you need to set name of the document
and initial tracking month. After this procedure patterned workbook will be created and scene view will change to the core layer 
of the application. Plese see attachment bellow. 
 
Name of the document must starts with **character and than numbers, underscores and dashes** are allowed
![NameSetup](./Documentation/NameSetup.PNG)

Initial Tracking months are indexed with **integers 1-12*
![MonthSetup](./Documentation/InitialMonth.PNG)
  
  
