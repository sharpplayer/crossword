@echo off
cd "c:\users\raymond\workspace\crossword"

rem *
rem * Check if gradle wrapper is present and keep repeating until done!
rem *
:repeat
if EXIST "gradle" goto menu
echo * 
echo * Run from the GRADLE_HOME\bin folder in a separate cmd prompt:
echo *     gradle.bat -p "%CD%" wrapper
echo * 
echo * Perform above instruction and then press a key when complete
pause
goto repeat

:menu
echo * ====================================================
echo * Default Gradle tasks
echo * ====================================================
echo * clean                # deletes the build directory
echo * clean test           # runs the unit tests (and compile before if needed)
echo * clean build          # assembles and tests this project
echo *
echo * cleanEclipse         # cleans all Eclipse files
echo * eclipse              # generates all Eclipse files
echo * build                # builds everything and runs unit tests
echo * build -x test        # builds everything and excludes tests
echo * clean checkstyleMain # checkstyle only src folder
echo * clean checkstyleTest # checkstyle only test folder
echo *
echo * ====================================================
echo * CAV tasks
echo * ====================================================
echo * showEnvironmentalVariables                         # shows key environment variables
echo * executeSpring -Pconfig=FILE -Psubsystem=NAME       # execute Spring XML federate 
echo * 
SET GRADLE_OPTIONS=
SET /P GRADLE_OPTIONS=Gradle Options (blank line to quit)?
if "%GRADLE_OPTIONS%" == "" exit /b 0
echo Running...
echo gradle %GRADLE_OPTIONS%
if /I "%GRADLE_OPTIONS:build=%" NEQ "%GRADLE_OPTIONS%" goto newwindow
if /I "%GRADLE_OPTIONS:~0,7%"=="execute" goto newwindow
if /I "%GRADLE_OPTIONS:~0,2%"=="eS" goto newwindow
if /I "%GRADLE_OPTIONS:~0,3%"=="eCS" goto newwindow
rem * 
rem * Default to executing in the same process
rem * 
call gradlew.bat %GRADLE_OPTIONS%
if /I "%GRADLE_OPTIONS%" == "eclipse" echo Refresh Eclipse Project View to see new dependencies
if /I "%GRADLE_OPTIONS%" == "cleanEclipse" echo Refresh Eclipse Project View to see new dependencies
pause
goto repeat
:newwindow
rem * 
rem * Run in new window
rem * 
echo ...in new window
start gradlew.bat %GRADLE_OPTIONS%
goto repeat
