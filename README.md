
 Initial Setup

 - 1. Importing buildSrc:
  
 If you build for the first time the app on the machine you need to create a symbolic link to the
 buildSrc directory in the common code. Also the symbolic link is added to the .gitignore of the android project.
 
 For mac:
	cd EbolaApp/android
        ln -s ../common/buildSrc buildSrc
	./gradlew build
	
 For Windows:
	cd EbolaApp/android
	mklink /D buildSrc ..\common\buildSrc


