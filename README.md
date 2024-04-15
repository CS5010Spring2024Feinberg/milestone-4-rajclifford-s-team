# CS 5010 Semester Project

This repo represents the coursework for CS 5010!

**Name:** Rajorshi Sarkar,
          Clifford Yin

**Email:** sarkar.r@northeastern.edu
yin.cl@northeastern.edu

**Preferred Name:** Raj,
Clifford


### About/Overview

The problem facing us is that we need to represent in code a clinic that has rooms of patients and the staff members assigned to those patients, as well as all of their various functions. We accomplish this by using an MVC design pattern where the clinic is the model, a GUI is the view, and the controller takes user input from the GUI and directs actions in the model.

### List of Features

The clinic has the ability to:

- Get and set its name
- Add a room
- Add a patient
- Add a staff member
- Register a new patient in the clinic, can be done through GUI
- Get a room by its number
- Get all the rooms
- Register a new clinical staff member in the clinic
- Send a patient home, can be done through GUI
- Assign a clinical staff member to a patient, can be done through GUI
- Assign a patient to a room, can be done through GUI
- Get the patients in a room
- Find a patient by name
- Find a clinical staff member by name
- Get a patient's room
- Display available rooms
- Get a list of clinical staff members
- Show a list of clinical staff members on a GUI
- Check if a room is occupied
- Check if a patient is in an exam or procedure room
- Display a seating chart of the rooms
- Check for duplicate patients
- Get the room of a patient
- List all the patients with their information
- Find a patient by serial number
- Find a staff by serial number
- Add a visit record on a GUI
- Deactivate a staff member on a GUI
- Show a patient's details on a GUI
- Unassign a staff member from a patient on a GUI
- List the clinical staff and patient counts on a GUI
- List inactive patients for a year on a GUI
- List clinical staff with incomplete visits on a GUI
- List patients with multiple visits in past year on a GUI


### How to Run
Jar file Milestone_4.jar is located in /res directory
Steps:

Open Command Line or Terminal: Navigate to the command line or terminal on your computer.

Navigate to the JAR File Location: Use the cd command to change directories to the location where your JAR file is stored. For example: cd path/to/your/jarfile

3.Run the JAR File: To run the program, use the following command:java -jar Milestone-4.jar
(no arguments needed)



### How to Use the Program

Run the GUIDriver.java file as a Java application, and go through the menu and options displayed on the GUI as you so wish. You can perform any function in the manner described in the milestone 4 description.

Step 1: Run the JAR and see the welcome screen:
Step2: Select the clinic file to load
Step 3: See the rendered map of your Clinic with your clinic name
Step 4: Use the Clinic Menu to interact and use the Clinic Management System
Menu 1 : Register New Patient
Menu 2 : Assign Patient to Room
Menu 3 : Add Visit Record
Menu 4 : Add Clinical Staff
Menu 5 : Assign Clinical Staff to Patient
Menu 6 : Send Patient Home
Menu 7 : Deactivate Staff
Menu 8 : Show Patient Detail
Menu 9 : Unassign Staff from Patient
Menu 10 : List Clinical Staff and Patient Count
Menu 11 : List Inactive Patient for Over an Year
Menu 12 : List Clinical Staff with Incomplete Visit
Mneu 13 : List Patients with Mutiple Visits in Last Year



### Example Runs

As the run is in GUI, it has been provided as a pdf file in /res file

File Name: View and Run of Milestone4.pdf

### Design/Model Changes

Numerous refactoring was needed to the Milestone 3 code to transform it, and meet the GUI requirement of Milestone 4.
The changes can be summarized as:
1. Refactored All the methods in Clinic,Patient and Room Class to work with GUI inputs(rather than scanner) and display any error or success message as JOption Message
2. Created New Driver to provide a GUI interface to let user choose a file (filechooser) from directory as Clinic File.
3. Refactored Controller to render a realtime map of the Clinic (from CinicMap class) and redraw it when ever the clinic is update throw user action.
4. Refactored Contoller to create GUI Menu to accomodate all the functionality required in Milestone 4, and linked them as command invokers (lambda) using the command interface to let the GUI inouts interact with the model in a decoupled manner.
5. Removed concreate command classes, and scanner input classes (UserInput and ClinicalStaffInput) from Milestone 3.
6. Streamline Codes to remove any artifacts from the promt based interaction , like message and list displayes and use JTable and GUI dialog boxed to display all neccessary Information.
7. Used mock model to test the implementation.



### Assumptions

List any assumptions that you made during program development and implementations. Be sure that these do not conflict with the requirements of the project.



### Limitations

1. The Clinic Managment System cannot add room or change the room layout
2. The Clinic Managment System cannot handle 


### Citations

https://stackoverflow.com/questions/48504303/getting-resources-outside-of-src-folder-in-a-jar-file
https://stackoverflow.com/questions/5819772/java-parsing-text-file Creating jars in Itellij (https://www.youtube.com/watch?v=3Xo6zSBgdgk) https://www.w3schools.com/java/java_user_input.asp
https://www.geeksforgeeks.org/java-swing-jpanel-with-examples/

Be sure to cite your sources. A good guideline is if you take more than three lines of code from some source, you must include the information on where it came from. Citations should use proper [IEEE citation guidelines](https://ieee-dataport.org/sites/default/files/analysis/27/IEEE Citation Guidelines.pdf) and should include references (websites, papers, books, or other) for ***any site that you used to research a solution***. For websites, this includes name of website, title of the article, the url, and the date of retrieval**.** Citations should also include a qualitative description of what you used, and what you changed/contributed.



