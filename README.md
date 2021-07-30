# ITem
This application was made mainly for learning purposes and, to some extent, make some tasks of IT technician less tedious and faster.
Maybe it can also be of some use to someone else.
Functionality of this app includes:
1. Parsing LDAP
2. Integration with PostgreSQL database or embedded database
3. Synchronization of data between LDAP and database
4. Sending email templates
5. Generating random passwords
6. Generating mail signatures
7. CommandBox - a way to store useful strings, e.g. console commands
8. Sending SMS - using [smsapi](https://www.smsapi.com)


## Application functionality
### 1. Synchronize ADUsers from LDAP to database

![Sync preview](https://thumbs.gfycat.com/OrdinaryDependentCornsnake-size_restricted.gif)

This application allows to retrieve users from LDAP and quickly generate and/or send personalized templates (mails, sms, signatures).

Along with data from LDAP it is possible to store additional information unique or shared between several ADUsers, such as City where they
reside (each Users' City is parsed from their parent container in LDAP, so for example, if User DN is 
"dn=Jane Doe,ou=New City,ou=Employees,dc=ttzv,dc=local" application will assume "Jane Doe" resides in City which location was "New City").

This brings us to the next point:

<h3> 2. Change or add Users' information

<img src="https://thumbs.gfycat.com/AdolescentSelfishDingo-size_restricted.gif"></img>

Each User can have additional data associated with them. Database updates are performed by clicking "Save changes" button.
Every String can also be quickly copied to Clipboard using little copy icons on each TextField.
I created a custom control specifically for this purpose.

### 3. Send Mail templates with variable strings

<img src="https://thumbs.gfycat.com/SilentVacantBarracuda-size_restricted.gif"></img>

This functionality allows to send message templates with variable text fragments. 
For example, sending a welcome message to new Employee greeting them by name - in big organization scenario this can get tedious very fast, so why not just load a template, set a flag that tells the program where this variable text should be, and retrieve that info from database in one click.

Preparing message templates is very straightforward.

We have to use flags to tell the parser which fields should be replaced with attributes:

<img src="https://i.imgur.com/CHnJ1eO.png"></img>

Prepared message template can look like this:

<img src="https://i.imgur.com/iyCeQoM.png"></img>

E-Mail account credentials and server details can be changed in Settings.

### 4. Send SMS

<img src="https://thumbs.gfycat.com/ReliableAlienatedAntbear-size_restricted.gif"></img>

This is quite self-explanatory. This module allows sending SMS using existing <a href="https://www.smsapi.com/">smsapi.com</a> account. 
Message templates are also supported.

### 5. Save commonly used strings</h3>

<img src="https://thumbs.gfycat.com/SoreShoddyHapuku-size_restricted.gif"></img>

If we already use database to store all our Users' info that we need everyday, then why not also store our most commonly used strings?

### 6. Dark theme</h3>

<img src="https://thumbs.gfycat.com/SardonicHastyCero-size_restricted.gif"></img>

Dark theme. That's all there is to it! Every app needs to have one.

### 7. Java Platform Module System and creating custom runtime image
This application uses JPMS which combined with JavaFX, multitude of libraries and Maven proved to be a challenge.  
The main advantage of using JPMS is an ability to use jlink - a tool that can create custom runtime image which means Java no longer has to be installed on client computer.  
Unfortunately not every library out there is modular yet, so I had to resort to other methods.  
To create image for this app follow these steps:
1. Run 

       mvn clean package
2. Separate jars that are not modular (those without module-info) - I plan to write a tool that does it automatically.
3. Put those jars in separate directory, in my case it's "auto"
4. Run jlink:  
        
       jlink --add-modules java.base,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.web,java.logging,java.naming,java.sql,java.desktop,jdk.charsets,com.fasterxml.classmate,com.fasterxml.jackson.core,com.fasterxml.jackson.databind,ttzv.uiUtils,java.management,jdk.crypto.ec,java.compiler --module-path files/ --output image/ --compress 2
5. Go to the root directory and run the app using custom runtime image that you just created: 

       "image/bin/java.exe" -p "auto;files" -m com.ttzv.item/com.ttzv.item.Main
8. In short - we package all jars that are modular in our jlink image. Unmodular jars are loaded in old-fashioned way using classpath.





