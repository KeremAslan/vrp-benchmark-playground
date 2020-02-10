# Vehicle Routing
This project is an experimental environment for testing different optimisation libraries on the vehicle routing problem. 
The following libraries are used in this project
* Optaplanner
* Google's or-tools

## Dependencies
### Optaplanner
Optaplanner is loaded as a dependency using maven. 

### Or-tools
Or-tools is not available in the maven repository. Therefore to run or-tools one needs to download the library from 
[here](https://developers.google.com/optimization/install). 

For both Windows and MacOS one needs to add the path where the `jniortools` dll is located to the classpath. In IntelliJ
that can be done by right clicking on the library and then clickin on `Add to library`. 

Note that on Windows the Visual Studio C++ redistributable needs to be installed for it to work. 

## Program arguments
