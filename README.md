# Vehicle Routing
This project is an experimental environment for testing different optimisation libraries on the vehicle routing problem. 
The following libraries are used in this project
* [Optaplanner](https://www.optaplanner.org/)
* [Google's or-tools](https://developers.google.com/optimization/)

The project was historically set up to only run Optaplanner and after some experimentation Or-tools was set up as an alternative.
As such, the modelling is primarily done using the Optaplanner models and Or-tools is accessed via these models. The adapters
`OptaplannerToOrToolsAdapter` and `OrtoolsToOptaplannerAdapter` translate the model in and out. 

## Dependencies
### Optaplanner
Optaplanner is loaded as a dependency using maven. 

### Or-tools
Or-tools is not available in the maven repository. Therefore to run or-tools one needs to download the library from 
[here](https://developers.google.com/optimization/install). Place the google-ortools jar in the following folder: 
`/src/main/resources/lib/com.google.ortools.jar` (or adjust the `pom.xml` accordingly.) 

Note that on Windows the Visual Studio C++ redistributable needs to be installed for it to work. 

## Program arguments
The program supports the following program arguments:

* `-run-mode` or `-m`. The mode to run the solver in. Accepts `optaplanner`, `or-tools` or `benchmark`.
* `-problem-type` or `-p`. The problem type to solve. Currently only supports the `SINTEF` problem. See [here](https://www.sintef.no/projectweb/top/vrptw/homberger-benchmark/)
* `-file-path` or `-f`. The path for the problem to solve. 
* `-runtimeInMinutes` or `-rt`. The runtime in minutes for the problem. 

## How to run a single problem
First run `mvn pacakge` to create a jar. 
### Optaplanner
`mvn clean exec:java -Dexec.args="-m optaplanner -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/C1_10_2.TXT"`

### Or-tools
`mvn clean exec:java -Dexec.args="-m or-tools -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/C1_10_2.TXT"`

## How to run the benchmark
### Benchmarker
The functionality to run the same problem set against different libraries can be accessed via the following command. 
`mvn clean exec:java -Dexec.args="-m benchmark -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/ -o output.csv"`
