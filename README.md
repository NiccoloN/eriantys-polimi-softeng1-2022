# Eriantys Board Game

<img src="https://img.dungeondice.it/49229-large_default/eriantys.jpg" width=192px height=192 align="right"  alt=""/>

Eriantys Board Game is the final test of **"Software Engineering"**, course of **"Computer Science Engineering"** held at Politecnico di Milano (2021/2022).

**Teacher** San Pietro Pierluigi

## The Team
* Niccolò Nicolosi
* Emanuele Musto
* Francesco Melegati Maccari

## Project specification
The project consists of a Java version of the board game *Eriantys*, made by Cranio Creations.

You can find the full game [here](https://www.craniocreations.it/prodotto/eriantys/).

The final version includes:
* initial UML diagram;
* final UML diagram, generated from the code by automated tools;
* protocol documentation;
* every peer review, received and sent;
* working game implementation, which has to be rules compliant;
* source code of the implementation.

## Implemented Functionalities
| Functionality                |         Status         |
|:-----------------------------|:----------------------:|
| Basic rules                  |           ✅            |
| Complete rules               |           ✅            |
| Socket                       |           ✅            |
| GUI                          |           ✅            |
| CLI                          |           ✅            |
| Persistence                  |           ✅            |
| All twelve characters        |           ✅            |
| Four player mode             |           ✅            |
| Multiple games               |           ⛔            |
| Resilience to disconnections |           ⛔            |

#### Legend
[⛔]() Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;[⚠️]() Implementing&nbsp;&nbsp;&nbsp;&nbsp;[✅]() Implemented


<!--
[![RED](http://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](http://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](http://placehold.it/15/44bb44/44bb44)](#)
-->

## Instructions

*Note: launching the CLI could cause an error to occur, due to the limited
space of the command line. To prevent this, please decrease the font size of the command line until it works*

*Note 2: files .bat, .command, .sh can be also found in deliverables, and can be downloaded and used
in the same way as described later on.*

*Note 3: It is recommended to have installed one of the latest JDK versions*

### Windows

Download file jar from path `deliverables/jar/Eriantys.jar` ,
and place it in a folder.

Now prepare two files batch (.bat), one to start the server, 
the other one to start the CLI, like following.

In the server file write `java -jar Eriantys.jar -server`.
In the CLI file write `java -jar Eriantys.jar -nogui`.

Place these two files in the same folder as the jar downloaded before.

To start the server, launch the server bat file.
To start the CLI, launch the CLI bat file.
To start the GUI, launch the jar file.

### Mac

Same procedure as for windows, except for using a .command file instead of a .bat file,
and the lines for the .command file are:

`#!/bin/bash`  
`cd "$(dirname "$BASH_SOURCE")"`  
`java -jar Eriantys.jar -server`

and

`#!/bin/bash`  
`cd "$(dirname "$BASH_SOURCE")"`  
`java -jar Eriantys.jar -nogui`

*Note: Mac with aarch64 architecture (M1 processor) needs a different jar, 
that can be found in `deliverables/jar/M1`*

### Linux

Download file jar from path `deliverables/jar/Eriantys.jar`.
Open the terminal in the directory of the downloaded file,
and write

`java -jar Eriantys.jar -server` to start the server, or
`java -jar Eriantys.jar -nogui` to start the CLI.

