#  Motorbike community (AMOS-WS18-19-Project-3)

The Motorbike Community App is a mobile app meant to bring to motorbike community together by offering them a platform for everything they would need before and after a tour. Every user has the possibility to contribute to the community by sharing beautiful routes and special spots. With a constantly growing user network, this app is the best place for local dealers to connect to the riders through special offers of services or events. The generated user engagement can give businesses even more insides on needs of this group of drivers and events creators.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See Prerequisites and Installing guidelines below.

### Prerequisites

Following are the software to download in your system in order to run the application:  

```
* [Android Studio](https://developer.android.com/studio/) - Used as IDE for Android application development.
* [Apache Tomcat](https://tomcat.apache.org/download-80.cgi) - Used for Web server
* [Eclipse IDE](https://www.eclipse.org/downloads/) -used as IDE for web server.
* [PostgreSQL](https://www.postgresql.org/download/)- Used for database.
```

### Installing

A step by step series of software's that tell you how to get a development environment running in local Machine.

Set up for Android Studio

```
Steps:
 1.  If you have downloaded an .exe file from above given link, double-click to launch it. If you downloaded a .zip file, unpack the ZIP, copy the android-studio folder into your Program Files folder, and then open the android-studio > bin folder and launch studio64.exe (for 64-bit machines) or studio.exe (for 32-bit machines).
 2.  Follow the setup wizard in Android Studio and install any SDK packages that it recommends.
 3.  Go to file ---> New --> Project from version control --> git
 4.  Put 'https://github.com/Paraffinum/AMOS-WS18-19-Project-3.git' in URL and set folder in Directory.
 5.  Now , see 'app' in the left side corner in 'project' tab and run it by right click on it.
 6.  Furthermore, To set 'virtual device' could be an optional because project can run in mobile device also.
 7.  If would like to set up 'virtual device' then go to Tools --> AVD Manager --> create virtual device.
```

Set up for PostgreSQL

```
Steps:
1.  If you have downloaded then Double click on the installer file.
2.  Specify installation folder, choose your own or keep the default folder suggested by PostgreSQL installer. Enter the password for the database superuser and service account.
3.  Enter the port for PostgreSQL. Make sure that no other applications are using this port. Leave it as default if you are unsure.
4.  Choose the default locale used by the database.
5.  You’ve completed providing information for the PostgreSQL installer. Click the Next button to install PostgreSQL. The installation may take few minutes to complete.
6. The quick way to verify the installation is through the pgAdmin application. First, click on pgAdmin 4 to launch it. The pgAdmin 4 GUI will display.
7. Second, double-click PostgreSQL on the object browser. It will ask you for the admin password. Just enter the password you’ve used in the installation step.
8.  Third, you will be able to see 'Database' there you can create new database for project by right click on it.
```

Set up for Web server application (Eclipse and Apache Tomcat)

```
Steps:
 1.  If you have downloaded a zip file from above given link then Unzip via right click on file and extract there.
 2.  Create a shortcut on your desktop to the eclipse.exe file in this eclipse folder (Could be optional).
 3.  Double-click the shortcut to Eclipse that you just created above. The splash screen will appear and then an Eclipse Launcher pop-up window will appear.
 4.  In the Workspace text box, your name should appear between C:\Users\ and \eclipse-workspace. Leave unchecked the 'Use this as the default and do not ask again box'. Although you will use this same workspace for the entire quarter (checking projects in and out of it), it is best to see this Workspace Launcher pop-up window each time you start Eclipse, to remind you where your workspace is located.
 5.  Click Launch. Progress bars will appear as Eclipse loads. Eventually the Eclipse workbench will appear with a   Welcome tab covering it.
 6.  Now , pull branch Master from git and Open project in eclipse which you have just installed.
 7.  Download Apache Tomcat v 8.5 and unzip it to install.
 8.  Add new server by right clicking on File --> New --> Other --> server.
 9.  Click on next and choose 'server type' --> tomcat v8 5 server and click on Finish. 
 10. Edit classpath and remove for Tomcat all Entries that are not located on your computer (Run -> Run Configurations -> Classpath)
 11. If necessary(could be optional as sometimes jdk is already installed if used in another project): Set the 'jdk' to the one installed on your computer (Right click on Project -> Properties -> Java Build Path) and used port 8080 (Else any free port on your Machine), then settled the port in the config file server.xml.
 12. So at the end, Need to check/set one important things: You can see Restfulwebserver in project explorer in left side corner. There you will be able to see Web content folder inside it. Then, Click on WEB-INF --> lib --> enivronment.ini. 
 13. Make sure following information should be corrected to connect server with database: 
     POSTGRES_HOST= Name of the host which you are using currently (e.g. localhost)
     POSTGRES_DB= Name of the database which ou have created in pgAdmin 4.
     POSTGRES_USERNAME= Name of username which you can find in pgAdmin4 below Database named 'Login/Group roles' --> the last tab (e.g 'postgres' by default usually)
     POSTGRES_PASSWORD= The password which you have kept while installing pgAdmin 4.
     POSTGRES_PORT= The port number (e.g 5432)  
```
At the end , The user should now be able to run project in their local machine successfully.
