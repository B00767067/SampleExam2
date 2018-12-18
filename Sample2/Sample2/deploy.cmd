@echo off 

echo ---Rename app to ROOT.war 
rename d:\home\site\repository\AppDB\target\*.war ROOT.war
echo ---File is moved to webapp folder
copy d:\home\site\repository\AppDB\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
