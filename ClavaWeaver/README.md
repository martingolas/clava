
## Building Clava

Download [eclipse-build](http://specs.fe.up.pt/tools/eclipse-build.jar) and run the following command:

`java -jar eclipse-build.jar --config https://raw.githubusercontent.com/specs-feup/clava/master/ClavaWeaver/eclipse.build`

This should create the file ClavaWeaver.jar. 

## Downloading Clava

Clava can be downloaded from this [link](http://specs.fe.up.pt/tools/clava.jar).


## Clava Online Demo Version

There is a demo online version of Clava available [here](http://specs.fe.up.pt/tools/clava).



## Running Clava


Clava has two modes, command-line and GUI.


## GUI


Run the JAR with passing parameters, e.g.:

	java -jar Clava.jar



## Command Line


There are two main modes in command line, either passing all arguments (LARA file, parameters, etc...), or passing a configuration file that was built with the graphical user interface.



### Manually:

	java -jar Clava.jar <aspect.lara> -p <source_folder>

where <aspect.lara> is the LARA aspect you want to execute, and <source_folder> is the folder where the source code is.


There are more command-line options available, which can be consulted by running:

	java -jar Clava.jar --help


		
### Configuration file:

To pass a configuration file, use the flag -c:

	java -jar Clava.jar -c <config.clava>

where <config.clava> is the configuration file created with the GUI.

