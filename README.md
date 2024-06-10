Task Manager
Project Description
Task Manager is a desktop application written in Java that allows users to manage tasks efficiently. The application provides a user-friendly interface for adding, viewing, editing, and deleting tasks. Additionally, the application allows for importing, exporting, and generating PDF reports of tasks.

Features
1. Adding Tasks
Users can add new tasks by entering the title, description, status, and due date.
The due date can be selected using a date picker component.
2. Viewing and Editing Task Details
Tasks are displayed in a table with columns: title, due date, status, and description.
Users can select a task from the table to view and edit its details in a dialog box.
The dialog box allows updating the title, description, status, and due date.
3. Deleting Tasks
Users can delete tasks by selecting them from the table and clicking the "Delete" button.
4. Importing and Exporting Tasks
Tasks can be imported from and exported to CSV files.
This feature allows for easy backup and transfer of task data.
5. Generating PDF Reports
Users can generate PDF reports containing the list of tasks.
The report includes all tasks displayed in the table, making it easy to share and print.
6. Dark Theme Interface
The application features a dark theme with a black background and white text for better readability and aesthetics.
Technical Requirements
Java SE Development Kit (JDK) version 8 or newer

External Libraries:
iText (for generating PDFs)
JDatePicker (for date selection)
Installation and Running

Step 1: Cloning the Repository

git clone https://github.com/patricck277/Task-Manager-Application.git


Step 2: Adding External Libraries
Download the libraries and add them to the lib directory in your project:
iText
JDatePicker

Step 3: Compiling and Running

Compile the project:

javac -cp "lib/*" -d out/production/TaskManager src/*.java

Run the application:

java -cp "out/production/TaskManager;lib/*" TaskManager


File Descriptions

TaskManager.java
The main class of the application responsible for the user interface and task management.

Task.java
The class representing a task, containing fields such as title, description, status, and due date.

TaskDetailsDialog.java
The class responsible for the dialog box used for viewing and editing task details.

TaskPDFGenerator.java
The class responsible for generating PDF reports with the task list.

TaskIO.java
The class responsible for importing and exporting tasks to/from CSV files.

DateLabelFormatter.java
The class responsible for formatting the date in the JDatePicker component.

How to Use
Running the Application
Run the application:

java -cp "out/production/TaskManager;lib/*" TaskManager
Adding a Task:

Enter the task details in the input fields and click the "Add" button.
Viewing and Editing a Task:

Select a task from the table and click the "Details" button.
Update the task details in the dialog box and click "Save" to apply the changes.
Deleting a Task:

Select a task from the table and click the "Delete" button.
Importing Tasks:

Click on the "File" menu and select "Import Tasks".
Choose a CSV file to import tasks from.
Exporting Tasks:

Click on the "File" menu and select "Export Tasks".
Choose a location to save the CSV file.
Generating PDF Report:

Click on the "File" menu and select "Generate PDF".
Choose a location to save the PDF report.
