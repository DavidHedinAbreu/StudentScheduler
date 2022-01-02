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

FOR FURTHER DEVELOPMENT:

1. Review the suitability of the "student" object, with fields representing intervention-eligible courses prioritized by likelihood of pass and need for graduation, and caseworker. Does the "student" object represent everything that can be used for scheduling? Are there more sensible ways to translate raw data into an array of student objects?
2. Do the same for the "classroom" object, with fields representing subjects taught, capacity, availability for intervention, and caseworker status.
3. Consider making static those statistics calculated from the entire population that may be improved by multiple iterations of microservices, like classroom overcrowding/balancing which may be improved by changing intervention course, and appropriateness of intervention course which may be improved by allowing overcrowding/imbalance.  Also consider making static the array of student objects and intervention classroom objects.
4. Look for ways to use linear algebra optimization methods (simplex method?) to find optimal composition and size of intervention classes for each teacher.
5. Use a functional-programming approach -- streams, collections, and lambda expressions -- when feasible.
6. Rewrite the optimization strategy as services that are as stateless as possible.  For example,
    Raw student data -> student object array
    Raw teacher data -> classroom object array
    SOA + COA -> balance (minimal overcrowding) statistics
    SOA + COA -> appropriate placement statistics
    SOA + COA + OS + APS -> COA optimized for BS, SOA optimized for APS
7. A GUI allow ad-hoc changes to the finalized schedule, for example to honor teacher's parent's or student's special request for placement.
8. An attendance service to verify presence/absence, and to allow ad-hoc changes in attendance roster initiated and confirmed in advance by teachers (for example to honor teachers' agreement to swap a student for one or more periods, so Johnny can finish his English exam during intervention assigned as math.)

Next steps: can the process of creation of a master schedule be automated via AI?

