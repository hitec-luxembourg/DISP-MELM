DISP-MELM
=========

DISP Map Element Library Manager

Development:

<ul>
<li>Install NodeJS (from http://nodejs.org/) for its npm (node package modules)</li>
<li>Install Grunt command line with "npm install -g grunt-cli"</li>
<li>With a command prompt go to the project directory and launch "npm install"</li>
<li>Then launch "grunt" (http://gruntjs.com/getting-started)</li>
<li>Finally use "bower install 'package'" in order to add new JS dependency</li>
<li>Copy the needed files to /css/vendor and js/vendor with configuring the copy plugin in Gruntfile.js</li>
</ul>

Installation:

<ul>
<li>Install database PostgreSQL version >= 9.1</li>
<li>Install application server Tomcat version >= 7.0.52</li>
<li>Put PostgreSQL JDBC driver postgresql-9.3-1100.jdbc41.jar in Tomcat lib folder</li>
<li>Run 1_db.sql in order to create user and database</li>
<li>Run schema.sql in order to create the tables</li>
<li>Deploy DISP-MELM.war in Tomcat</li>
</ul>