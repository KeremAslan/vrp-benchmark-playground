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
[here](https://developers.google.com/optimization/install). The dependency is included in the `lib` folder.  

For both Windows and MacOS one needs to add the path where the `jniortools` dll is located to the classpath. In IntelliJ
that can be done by right clicking on the library and then clickin on `Add to library`. 

Note that on Windows the Visual Studio C++ redistributable needs to be installed for it to work. 

## Program arguments
The program supports the following program arguments:

* `run-mode` or `m`. The mode to run the solver in. Accepts `optaplanner` or `or-tools`.
* `problem-type` or `p`. The problem type to solve. Currently only supports the `SINTEF` problem. See [here](https://www.sintef.no/projectweb/top/vrptw/homberger-benchmark/)
* `file-path` or `f`. The path for the problem to solve. 
* `route-output-path` or `o`. File path to save the solution to. (Not implemented)
* `runtimeInMinutes` or `rt`. The runtime in minutes for the problem. 

## How to run a single problem
First run `mvn pacakge` to create a jar. 
### Optaplanner
`mvn exec:java -D"exec.mainClass"="app.MainApp" -Dexec.args="-m optaplanner -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/C1_10_2.TXT"`

### Or-tools
`mvn exec:java -D"exec.mainClass"="app.MainApp" -Dexec.args="-m or-tools -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/C1_10_2.TXT"`

### Benchmarker
This project is set up to compare different libraries against each other. That functionality can be run by
`mvn exec:java -D"exec.mainClass"="app.MainApp" -Dexec.args="-m benchmarker -p SINTEF -runtimeInMinutes 1 -f input/sintef/homberger_1000_customer_instances/ -o output.csv"`
