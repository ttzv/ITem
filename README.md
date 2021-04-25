<h1> ITem </h1>
This application was made mainly for learning purposes and, to some extent, make some tasks of IT technician less tedious and faster.
Maybe it can also be of some use to someone else.
Functionality of this app includes:
<ol>
<li>Parsing LDAP </li>
<li>Integration with PostgreSQL database</li>
<li>Synchronization of data between LDAP and database</li>
<li>Sending email templates</li>
<li>Generating random passwords</li>
<li>Generating mail signatures</li>
<li>CommandBox - a way to store useful strings, e.g. console commands </li>
<li>Sending SMS - using <a href="https://www.smsapi.com/">smsapi.com</a></li>
<li>Automatic updates from FTP server using <a href="https://github.com/update4j/update4j">update4j</a> framework</li>
</ol>

<h2> Application functionality </h2>
<h3>1. Synchronize ADUsers from LDAP to database</h3>

<img src="https://thumbs.gfycat.com/OrdinaryDependentCornsnake-size_restricted.gif"></img>

Main idea behind this app is to allow quick access to data of all Users. 

To achieve this the application tries to synchronize every User that exists in selected Organizational Unit (configurable in Settings).

If connection to LDAP could not be estabilished the app will still launch, because all data is stored in database. 
Application does not use any Object-Relational Mapping framework, so only currently supported DBMS is Postgres, however it is possible to add different DBMS drivers with some changes in code.
Along with data from LDAP it is possible to store additional information unique or shared between several ADUsers, such as City where they
reside (each Users' City is parsed from their parent container in LDAP, so for example, if User DN is 
"dn=Jane Doe,ou=New City,ou=Employees,dc=ttzv,dc=local" application will assume "Jane Doe" resides in City named "New City")

This brings us to the next point:

<h3>2. Change or add Users' information</h3>

<img src="https://thumbs.gfycat.com/AdolescentSelfishDingo-size_restricted.gif"></img>

Each User can have additional data associated with them. Database updates are performed by clicking "Save changes" button.
Every String can also be quickly copied to Clipboard using little copy icons on each TextField.

<h3>3. Send Mail templates with variable strings</h3>

<img src="https://thumbs.gfycat.com/SilentVacantBarracuda-size_restricted.gif"></img>

This functionality allows to send message templates with variable text fragments. 
For example, sending a welcome message to new Employee greeting them by name - in big organization scenario this can get tedious very fast, so why not just load a template, set a flag that tells the program where this variable text should be, and pull that info from database in one click.

Preparing message templates is very straightforward.

We have to use flags to tell the parser which areas interests us:

<img src="https://i.imgur.com/CHnJ1eO.png"></img>

Prepared message template can look like this:

<img src="https://i.imgur.com/iyCeQoM.png"></img>

E-Mail account credentials and server details can be changed in Settings.

<h3>4. Send SMS</h3>

<img src="https://thumbs.gfycat.com/ReliableAlienatedAntbear-size_restricted.gif"></img>

This is quite self-explanatory. This module allows sending SMS using existing <a href="https://www.smsapi.com/">smsapi.com</a> account.
If recipient has an assigned phone number it will be automatically pulled from database. 
Message templates are also supported.

<h3>5. Save commonly used strings</h3>

<img src="https://thumbs.gfycat.com/SoreShoddyHapuku-size_restricted.gif"></img>

If we already use database to store all our Users' info that we need everyday, then why not also store our most commonly used strings?

<h3>6. Dark theme</h3>

<img src="https://thumbs.gfycat.com/SardonicHastyCero-size_restricted.gif"></img>

Dark theme. That's all there is to it! Every app needs to have one.

<h3>7. Automatic updates</h3>

Being able to update application with minimal ADUser interaction is incredibly convenient.
By using <a href="https://github.com/update4j/update4j">update4j</a> framework, and it's compatibility with Java 9 and above, we can
create runtime image using <a href="https://docs.oracle.com/javase/9/tools/jlink.htm#JSWOR-GUID-CECAC52B-CFEE-46CB-8166-F17A8E9280E9">jlink</a> and update our application modules that run on custom java runtime. No more problems with installing and updating Java. Furthermore, we can auto-update Java runtime itself!
This application integrates with <a href="https://github.com/update4j/update4j">update4j</a> on very superficial level, using only a <a href="https://github.com/update4j/update4j/wiki/Documentation#lifecycle">default Bootstrap</a>, for instance.

<h3>Disclaimer</h3>

This application was built mainly for learning purposes as an effort to bring functionality of several systems to one place. 
There <i>will</i> be bugs, there <i>will</i> be crashes, and it probably <i>will</i> use more memory than it should.
