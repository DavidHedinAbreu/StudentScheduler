# StudentScheduler
Java app for scheduling students with Ds and Fs into "intervention periods" for the class they're struggling in.
Last updated October 26, 2019.  
Receives as input a CSV file containing records for 2700 students, with the following fields:
studentno	lastname	middlename	firstname	grade	room	period	courseno	coursetitle	percent	mark	gpa	credits1	credits2	id
Also receives CSV with teachers available for intervention (i.e., not assigned other duties).
The CSV information populates a new MySQL database, and queries retrieve information about the individual students.  
The information is analyzed to find statistics related to overcrowding in certain courses or classrooms, and other information like SPED, ELL, or RSP status.
The app attempts to find an ideal schedule for the students, based on these criteria:
* If a student has multiple D-F courses, the student is placed into intervention for the course they are most likely to pass.
* If there are not enough intervention teachers for a course, overcrowding is reduced by assigning students with multiple D-F courses to a less-crowded course in which they may have received a lower mark. 
* Students should be placed with their own teacher if possible. (This can be modified so that they should NEVER be placed with their own teacher if possible.)
* Teachers of a particular course should have approximately the same number of students during intervention period.
* Students who need a class for graduation or A-G college requirements are assigned to that class with priority over others.
* Students who should receive intervention with a casemanager because of special needs (SPED, ELL, RSP) are placed with that teacher instead of with the general population.
The ideal schedule is saved to database and exported as CSV.

Comments: I'm looking forward to refactoring the code from a set of interfaces.  The project suffered from several uncommunicated but essential criteria for scheduling, which had to be added later.  Therefore I suspect the object-oriented design isn't as sleek as it might have been.  Also, I wasn't too familiar with streams/lambda expressions/functional programming when I wrote the app, and now that I'm more aware of the believe a functional approach might be used here and there to make the code more concise.  With the interface approach I hope development can be more test-driven and ultimately more flexible.

To-do: A GUI needs to allow the admin to make ad-hoc changes to the completed schedule, for example, when a teacher has a special request to have a particular student in his/her intervention classroom, or when two students should not be placed together in the same room.  Next steps: to create a web-app attendance-taking component.  It might be modified to allow admin to alter the schedule.  The GUI might even allow teachers to "trade" students during intervention when they mutually decide it would be beneficial for the student, for example, when Johnny needs to finish his English paper to pass English, although he has Algebra intervention.

Do you have any comments?
