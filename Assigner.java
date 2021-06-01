import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Assigner 
{	
	int studentsPerMentor, maxCapacity;

	// Constructor.
	protected Assigner(int studentsPerMentor, int maxCapacity)
 	{
		this.studentsPerMentor = studentsPerMentor;
		this.maxCapacity = maxCapacity;
		
	}

	// ASSIGNMENT 
	
	// Adds case manager rooms ro roomRecords.  Places all students with intervention courses who have a case manger, with their case manager
	protected StudentAndRoomRecords assignToCaseManager(int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<RoomRecord> roomRecords, ArrayList<CaseManager> caseManagerRecords)
	{
		System.out.println("Beginning assignCaseManager()...");
		StudentAndRoomRecords studentAndRoomRecords = null;
		RoomRecord r = null;
		ArrayList<UseRecord> useRecords = new ArrayList<UseRecord>();
		boolean notIntervention = false, there;
		ArrayList<StudentRecord> assignRecords = new ArrayList<StudentRecord>();
		StudentRecord s = null;
		ArrayList<IntCourseRecord> intCourseRecords = new ArrayList<IntCourseRecord>();
		CaseManager c = null;
		
		for(CaseManager cm : caseManagerRecords)  // add each case manager's room, if it doesn't already exist
		{
			there = false;
			for(RoomRecord rm : roomRecords)
			{
				if(rm.room != null && rm.room.equals(cm.room) )
					there = true;
			}
			if(!there)
			{
				r = new RoomRecord(cm.room, useRecords, notIntervention, assignRecords, intCourseRecords, 0);
				roomRecords.add(r);
				System.out.println("Adding case manager's room " + cm.room + " to roomRecords");
			}
		}  
		
		// Parse student records.  If student has casemanager, parse roomRecords to find a room match.  Assign the student to 
		// assignRecords for that room.
		for(int i = 0; i < studentRecords.size(); i++)
		{
			s = (StudentRecord)studentRecords.get(i);
			if(s.caseManager != null)
			{
				c = s.caseManager;
				System.out.println("Student " + s.studentID + " has casemanager " + c.name);
				for(int j = 0; j < roomRecords.size(); j++)
				{
					r = (RoomRecord)roomRecords.get(j);
					if(r.room.equals(c.room) )
					{
						r.assignRecords.add(s);
						System.out.println("Adding student " + s.studentID + " to casemanager's room " + r.room);
						studentRecords.remove(i);
						i--;  // correct for removal of student
						r.numAssigned++;
						r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
						r = verifyICRNA(r, false);
						roomRecords.set(j, r);
					}  // End if(r.room.equals(c.room) ) 
				}  // End roomRecords.size() loop.
			}  // End if(s.caseManger != null)
		}  // End studentRecords.size() loop.
		
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);		
		return studentAndRoomRecords;
	}
	
	// Assign by useRecords method, including overcapacity courses.  Returns studentRecords and roomRecords.
	protected StudentAndRoomRecords assignByUse(ArrayList<String> overCapCourses, ArrayList<StudentRecord> studentRecords, ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("Beginning assignByUse... " + studentRecords.size() + " students left to assign.");
		boolean overCap = false;
		RoomRecord r = null;
		StudentRecord s = null;
		StudentAndRoomRecords studentAndRoomRecords;
		int courseNumAssigned = 0;
		
		for(int i = 0; i < roomRecords.size(); i++)
		{
			r = (RoomRecord)roomRecords.get(i);
			for(UseRecord u : r.useRecords)
			{
				overCap = false;
				for(String o : overCapCourses)
				{
					if(u.courseTitle.equals(o) )
					{
						overCap = true;
					}
				}
				courseNumAssigned = 0;
				if(!overCap)  // if overcapacity is not allowed, add up to u.courseCapacity, until there are no more students
				{
					for(int j = 0; j < studentRecords.size() && courseNumAssigned < u.courseCapacity; j++)
					{
						s = (StudentRecord)studentRecords.get(j);
						if(s.studentSchedule.get(s.theIntCourse).intCourseTitle.equals(u.courseTitle) && s.caseManager == null)
						{ 
							r.assignRecords.add(s);
							r.numAssigned++;
							studentRecords.remove(j);
							j--;  // correct for removal of last student.
							courseNumAssigned++;
						}
					}
				}  else  // if over-capacity is allowed, add ALL the students in studentRecords with that course
				{
					for(int k = 0; k < studentRecords.size(); k++)
					{
						s = (StudentRecord)studentRecords.get(k);
						if(s.studentSchedule.get(s.theIntCourse).intCourseTitle.equals(u.courseTitle) && s.caseManager == null)
						{
							r.assignRecords.add(s);
							r.numAssigned++;
							studentRecords.remove(k);
							k--;  // correct for removal of last student.
							courseNumAssigned++;
						}
					}					
				}
			}  // End useRecords loop.
			r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
			r = verifyICRNA(r, false);
			roomRecords.set(i, r);
		}  // End roomRecords loop.
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);
		return studentAndRoomRecords;
	}  // End assignByUse() method.
	
		
	// Assign to correct intervention room to maximize room capacity.  Returns studentRecords and roomRecords.
	protected StudentAndRoomRecords assignByCorrectIntRoom(int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("Beginning assignByCorrectIntRoom... "  + studentRecords.size() + " students left to assign.");
		RoomRecord r = null;
		StudentRecord s = null;
		StudentAndRoomRecords studentAndRoomRecords = null;
		
		for(int i = 0; i < roomRecords.size(); i++)
		{
			r = (RoomRecord)roomRecords.get(i);
			for(UseRecord u : r.useRecords)
			{
				for(int j = 0; j < studentRecords.size() && r.numAssigned < maxCapacity; j++)
				{
					s = (StudentRecord)studentRecords.get(j);
					if(s.studentSchedule.get(s.theIntCourse).intCourseTitle.equals(u.courseTitle) && s.caseManager == null)
					{
						r.assignRecords.add(s);
						r.numAssigned++;
						studentRecords.remove(j);
						j--;  // correct for removal of last student.
					}
				}
			}  // End useRecords loop.
			r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
			r = verifyICRNA(r, false);
			roomRecords.set(i, r);
		}  // End roomRecords loop.
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);
		return studentAndRoomRecords;
	}  // End assignByCorrectIntRoom() method.
	
	// Assign to similar, whether or not correct intervention room.  Returns studentRecords and roomRecords.
	protected StudentAndRoomRecords assignBySimilar(int maxCapacity, ArrayList<ArrayList<String> > similarCourses, 
		ArrayList<StudentRecord> studentRecords, ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("\n\nBeginning assignBySimilar..." + studentRecords.size() + " students left to assign.");
		RoomRecord r = null;
		StudentRecord s = null;
		StudentAndRoomRecords studentAndRoomRecords = null;
		UseRecord u = null;
		ArrayList<String> sc = null;
		String theIntCourse = "";

		for(int i = 0; i < studentRecords.size(); i++)
		{
			s = (StudentRecord)studentRecords.get(i);
			theIntCourse = s.studentSchedule.get(s.theIntCourse).intCourseTitle;
			for(int j = 0; j < roomRecords.size(); j++)
			{
				r = roomRecords.get(j);
				for(int k = 0; k < r.useRecords.size(); k++)
				{
					u = r.useRecords.get(k);
					for(int m = 0; m < similarCourses.size(); m++)
					{
						sc = similarCourses.get(m);
						if(sc.contains(u.courseTitle) && sc.contains(theIntCourse) && r.numAssigned < maxCapacity && s.caseManager == null)  // if the room use and student schedule both are in the similarCourse group 
						{
							r.assignRecords.add(s);
							r.numAssigned++;
							studentRecords.remove(i);
							r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
							r = verifyICRNA(r, false);
							roomRecords.set(j, r);					
							i--;  // correction for removed student
							j = roomRecords.size();  // if stu assigned in this room, stop looking in roomRecords 
							k = r.useRecords.size();  // if stu assigned in this room, stop looking in useRecords 
							m = similarCourses.size();  // if stu assigned in this room, stop looking in similarCourses
							System.out.println("In room " + r.room + ", " + u.courseTitle + " is taught, and student " 
								+ s.studentID + " has " + theIntCourse + ". This student will be assigned to the room, "
								+ " and removed from studentRecords.");
						}
					}  // End similarCourses loop
				}  // End useRecord loop
			}  // End roomRecords loop
		}  // End studentRecords loop.
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);
		return studentAndRoomRecords;
	}  // End assignBySimilar() method.
	
	// Assign to non-intervention room.  Returns studentRecords and roomRecords.
	protected StudentAndRoomRecords assignByNonIntRoom(int studentsPerMentor, int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("\n\nBeginning assignByNonIntRoom() ..." + studentRecords.size() + " students left to assign.");
		RoomRecord r = null;
		StudentRecord s = null;
		StudentAndRoomRecords studentAndRoomRecords = null;

		for(int i = 0; i < roomRecords.size(); i++)
		{
			r = (RoomRecord)roomRecords.get(i);
			// Because of some flaky room data in the 2019 5-week grade file, added this fix...
			//  r.room != null && !r.room.equals("") && !r.room.isEmpty() && !r.room.equals("GYM") && 
			if(r.room != null && !r.room.equals("") && !r.room.isEmpty() && !r.room.equals("GYM") && r.notIntervention)   
			{
				for(int j = 0; j < studentRecords.size() && r.numAssigned < maxCapacity; j++)
				{
					s = (StudentRecord)studentRecords.get(j);
					if(s.caseManager == null)
					{
						r.assignRecords.add(s);
						r.numAssigned++;
						studentRecords.remove(j);
						j--;  // correct for removal of last student.
						System.out.println("Added student " + s.studentID + " to non-intervention room " + r.room);
					}
				}
			}
			r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
			r = verifyICRNA(r, false);
			roomRecords.set(i, r);					
		}  // End roomRecords loop.
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);
		return studentAndRoomRecords;
	}  // End assignByNonIntRoom() method.
	
	// Assign remaining to ANY room.  Returns studentRecords and roomRecords.
	protected StudentAndRoomRecords assignToAny(int studentsPerMentor, int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("\n\nBeginning assignToAny()... " + studentRecords.size() + " students left to assign.");
		RoomRecord r = null;
		StudentRecord s = null;
		StudentAndRoomRecords studentAndRoomRecords = null;
		
		// Assign to low numStudents rooms first
		Collections.sort(roomRecords, new numAssignedComparatorForRoomRecords() );
		
		for(int i = 0; i < roomRecords.size(); i++)
		{
			r = (RoomRecord)roomRecords.get(i);
			// Because of some flaky room data in the 2019 5-week grade file, added this fix...
			// && r.room != null && !r.room.equals("") && !r.room.isEmpty() && !r.room.equals("GYM") && 
			for(int j = 0; j < studentRecords.size() && r.numAssigned < maxCapacity && r.room != null && !r.room.equals("") 
				&& !r.room.isEmpty() && !r.room.equals("GYM"); j++)
			{
				s = (StudentRecord)studentRecords.get(j);
				if(s.caseManager == null)
				{
					r.assignRecords.add(s);
					r.numAssigned++;
					studentRecords.remove(j);
					j--;  // correct for removal of last student.
					System.out.println("Added student " + s.studentID + " to room " + r.room);
				}
			}
			r = new RoomRecord(r.room, r.useRecords, r.notIntervention, r.assignRecords, r.intCourseRecords, r.numAssigned);
			r = verifyICRNA(r, false);
			roomRecords.set(i, r);					
		}  // End roomRecords loop.
		studentAndRoomRecords = new StudentAndRoomRecords(studentRecords, roomRecords);
		return studentAndRoomRecords;
	}  // End assignToAny() method.
	
	// Swap students with incorrect teacher for correct teacher.  Returns roomRecords.
	protected ArrayList<RoomRecord> swapForCorrectTeacher(int studentsPerMentor, int maxCapacity, int periodBeforeIntervention, 
		ArrayList<StudentRecord> studentRecords, ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("\n\nBeginning swapForCorrectTeacher()..." + studentRecords.size() + " students left to assign.");
		RoomRecord r1 = null, r2 = null;
		StudentRecord s1 = null, s2 = null;
		String title1 = "", title2 = "", room1 = "", room2 = "";
		int period1 = 0, period2 = 0;
		
		for(int i = 0; i < roomRecords.size(); i++)
		{
			r1 = (RoomRecord)roomRecords.get(i);

			// Because of some flaky room data in the 2019 5-week grade file, added this fix...
			// r1.room == null || r1.room.equals("") || r1.room.isEmpty() || r1.room.equals("GYM") 
			while(r1.room == null || r1.room.equals("") || r1.room.isEmpty() || r1.room.equals("GYM") )
			{
				i++;
				r1 = (RoomRecord)roomRecords.get(i);
			}
			for(int j = 0; j < r1.assignRecords.size(); j++)
			{
				s1 = r1.assignRecords.get(j);
				title1 = s1.studentSchedule.get(s1.theIntCourse).intCourseTitle;
				room1 = s1.studentSchedule.get(s1.theIntCourse).room;
				period1 = s1.studentSchedule.get(s1.theIntCourse).period;
			
				if(!room1.equals(r1.room) )  // if this student doesn't have this teacher for their intervention course, look for one that does to swap them
				{
					for(int k = 0; k < roomRecords.size(); k++)
					{
						r2 = (RoomRecord)roomRecords.get(k);

						// Because of some flaky room data in the 2019 5-week grade file, added this fix...
						// r1.room == null || r1.room.equals("") || r1.room.isEmpty() || r1.room.equals("GYM") 
						
						// But KEEP check for k == i || k == (roomRecords.size() - 1), prevents swapping for same student, or different student in same room
						while( (r2.room == null || r2.room.equals("") || r2.room.isEmpty() || r2.room.equals("GYM") || k == i) && k <= (roomRecords.size() - 2 ) )
						{
							k++;
							r2 = (RoomRecord)roomRecords.get(k);
						}

						for(int m = 0; m < r2.assignRecords.size(); m++)
						{
							s2 = r2.assignRecords.get(m);
							title2 = s2.studentSchedule.get(s2.theIntCourse).intCourseTitle;
							room2 = s2.studentSchedule.get(s2.theIntCourse).room;
							period2 = s2.studentSchedule.get(s2.theIntCourse).period;
							// If the other student has this teacher for the same intervention course, 
							// and the first student has this student's teacher their intervention course switch them.
							// if(title2.equals(title1) && room2.equals(r1.room) && room1.equals(r2.room) )  
							
							// if the two students have the same intervention course but BOTH, or only ONE, could be placed in their own 
							// teacher's room with a swap, swap them, but only if both are not currently placed correctly.
							if(title2.equals(title1) && !(!room2.equals(r1.room) && !room1.equals(r2.room)) 
								&& !room1.equals(r1.room) && !room2.equals(r2.room) )  
							{
								r1.assignRecords.set(j, s2);
								r2.assignRecords.set(m, s1);
								r1 = new RoomRecord(r1.room, r1.useRecords, r1.notIntervention, r1.assignRecords, r1.intCourseRecords, 
									r1.numAssigned);
								r2 = new RoomRecord(r2.room, r2.useRecords, r2.notIntervention, r2.assignRecords, r2.intCourseRecords, 
									r2.numAssigned);
								roomRecords.set(i, r1);
								roomRecords.set(k, r2);	
								System.out.println("Swapping student " + s2.studentID + " with " + title2 + " per. " + period2 + " rm. " + room2
									+ " from room " + r2.room + " to room " + r1.room 
									+ ", and student " + s1.studentID + " with " + title1 + " per. " + period1 + " rm. " + room1
									+ " from room " + r1.room + " to room " + r2.room);
								m = r2.assignRecords.size();  // begin again with room1 if a swap is made, until no swaps need to be made
								k = roomRecords.size();
								// j = r1.assignRecords.size();
								// i = roomRecords.size();
							}
						}  // End assignRecords loop 2
					}  // End roomRecords loop 2
				}  // End if(!room1.equals(r1.room) )
			}  // End assignRecords loop 1
		}  // End roomRecords loop 1
		
		return roomRecords;
	}  // End swapForCorrectTeacher() method.
	
	// Reduce overcapacity courses.  Creates courseRecords using studentRecords and roomRecords.  Returns studentRecords.
	protected ArrayList<StudentRecord> reduceOverCapCourses(int studentsPerMentor, int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<RoomRecord> roomRecords)
	{
		System.out.println("\nBeginning reduceOverCapCourses() ");
		int nextIntCourse;
		ArrayList<CourseRecord> courseRecords = new ArrayList<CourseRecord>(); 
		StudentRecord s = null;
		CourseRecord c = null;
		ArrayList<String> overCapacityCourses = new ArrayList<String>();
		boolean flag = true;
		
		courseRecords = getCourseRecords(studentRecords, roomRecords);
												
		// Loop through studentRecords. Loop through that student's schedule.  If overCapacityCourses does NOT contain a course 
		// in the student's schedule, point theIntCourse to that course, so the under capacity course becomes the student's int course.  
		// If all of them are overcapacity, do nothing.

		for(int i = 0; i < studentRecords.size(); i++)
		{
			s = (StudentRecord)studentRecords.get(i);

			for(nextIntCourse = 0; nextIntCourse < s.studentSchedule.size(); nextIntCourse++)
			{
				flag = true;
				for(int j = 0; j < courseRecords.size(); j++)
				{
					c = (CourseRecord)courseRecords.get(j);
					if(flag && c.intCourseTitle.equals(s.studentSchedule.get(nextIntCourse).intCourseTitle) && c.numStudents <= c.capacity)
					{
						s.theIntCourse = nextIntCourse;
						s = new StudentRecord(s.studentID, s.firstName, s.middleName, s.lastName, s.grade, s.distributionRoom, s.theIntCourse, 
							s.numIntCourses, s.studentSchedule, s.caseManager);
						studentRecords.set(i, s);
						if(s.theIntCourse != 0)  // update courseRecords if intCourse changed.
						{
							c.numStudents++;
							c = new CourseRecord(c.intCourseTitle, c.numStudents, c.capacity);
							courseRecords.set(j, c);
							System.out.println("Student " + s.studentID + " " + s.studentSchedule.get(s.theIntCourse).intCourseTitle + " assigned, was " + s.studentSchedule.get(0).intCourseTitle); // debug
						}
						flag = false;
						nextIntCourse = s.studentSchedule.size();  // stop when a low-cap course is found in this student's schedule
						
					}
				}
			}
		}  // End studentRecords loop.

		return studentRecords;

	}  //  End reduceOverCapCourses() method.
	
	protected ArrayList<CourseRecord> getCourseRecords(ArrayList<StudentRecord> studentRecords, ArrayList<RoomRecord> roomRecords)
	{
		boolean containsCourseRecord = false;
		int numStudents = 0, capacity = 0, theIntCourse = 0;
		ArrayList<CourseRecord> courseRecords = new ArrayList<CourseRecord>(), courseRecordsTEMP = new ArrayList<CourseRecord>(); 
		CourseRecord courseRecordLine = null, courseRecordLine2 = null;
		RoomRecord roomRecordLine = null;
		UseRecord useRecordLine = null;

		courseRecordsTEMP.clear();
		// Create courseRecordsTEMP from studentRecords with bogus numStudents = 0 and capacity = 0
		for(StudentRecord stu : studentRecords)
		{
			courseRecordsTEMP.add(new CourseRecord(stu.studentSchedule.get(stu.theIntCourse).intCourseTitle, 0, 0));
		}
		
		// Find all distinct courses in courseRecordsTEMP, store in courseRecords with bogus numStudents = 0, bogus capacity = 0
		for(CourseRecord c1 : courseRecordsTEMP)
		{
			containsCourseRecord = false;
			for(int i = 0; i < courseRecords.size(); i++)  // if there are no courseRecords for the int course, add it to courseRecords
			{
				courseRecordLine = (CourseRecord)courseRecords.get(i);
				if(courseRecordLine.intCourseTitle.equals(c1.intCourseTitle) )
				{
					containsCourseRecord = true;
					i = courseRecords.size();  // stop looking once found.
				}
			}
			if(!containsCourseRecord)
			{
				courseRecords.add(new CourseRecord(c1.intCourseTitle, 0, 0) );
			}
		}
		
		Collections.sort(courseRecords, new numStudentsComparatorForCourseRecords() );
		Collections.reverse(courseRecords);  // sorts courseRecords highest number of students to lowest.
		
		// Assign correct numStudents to all courses in courseRecords, keeping bogus capacity = 0.;
		for(int i = 0; i < courseRecords.size(); i++)
		{
			courseRecordLine = (CourseRecord)courseRecords.get(i);
			numStudents = 0;
			for(int j = 0; j < courseRecordsTEMP.size(); j++)
			{
				courseRecordLine2 = (CourseRecord)courseRecordsTEMP.get(j);
				if(courseRecordLine2.intCourseTitle.equals(courseRecordLine.intCourseTitle) )
				{
					numStudents++;
				}
			}
			
			courseRecords.set(i, new CourseRecord(courseRecordLine.intCourseTitle, numStudents, 0) );
		}
		
		// Correct the capacity in courseRecords from roomRecords for each course.
		for(int i = 0; i < courseRecords.size(); i++)
		{
			courseRecordLine = courseRecords.get(i);
			capacity = 0;
			for(int j = 0; j < roomRecords.size(); j++)
			{
				roomRecordLine = (RoomRecord)roomRecords.get(j);
				for(int k = 0; k < roomRecordLine.useRecords.size(); k++)
				{
					useRecordLine = (UseRecord)roomRecordLine.useRecords.get(k);
					if(useRecordLine.courseTitle.equals(courseRecordLine.intCourseTitle) ) 
					{
						capacity += useRecordLine.courseCapacity;
					}
				}
			}
			courseRecordLine = new CourseRecord(courseRecordLine.intCourseTitle, courseRecordLine.numStudents, capacity);
			courseRecords.set(i, courseRecordLine);
		}
		
		return courseRecords;
	}  // End getCourseRecords

	
	// VERIFICATION AND INTEGRITY
	
	// Verify roomRecords (both intCourseRecords & numAssigned).  Returns RoomRecord.
	protected RoomRecord verifyICRNA(RoomRecord workingRoomRecords, boolean verbose)
	{		
		Boolean hasCourse = false;
		ArrayList<StudentRecord> workingAssignRecords = new ArrayList<StudentRecord>(workingRoomRecords.assignRecords);
		ArrayList<String> workingIntCourses = new ArrayList<String>();
		ArrayList<IntCourseRecord> workingIntCourseRecords = new ArrayList<IntCourseRecord>();
		IntCourseRecord intCourseRecordLine = null;
		int numAssignedForCourse = 0;
		
		if(verbose)
			System.out.println("Beginning verifyICRNA for room " + workingRoomRecords.room);

		// Find a list of all the intCourses assigned to RoomRecord workingRoomRecords, assign to workingIntCourses
		for(StudentRecord a : workingAssignRecords)
		{
			if(!workingIntCourses.contains(a.studentSchedule.get(a.theIntCourse).intCourseTitle) )  // If a student has a new int course that hasn't been recorded
			{
				workingIntCourses.add(a.studentSchedule.get(a.theIntCourse).intCourseTitle);
				if(verbose)
					System.out.println("Recorded " + a.studentSchedule.get(a.theIntCourse).intCourseTitle + " in workingIntCourses.");					
			}
		}
		
		// Loop through workingIntCourses, find number of students assigned for each course in workingAssignRecords.  
		// Add a new IntCourseRecord to the IntCourseRecords for each course.
		for(String theIntCourse : workingIntCourses)
		{
			numAssignedForCourse = 0;
			for(StudentRecord a : workingAssignRecords)
			{
				if(a.studentSchedule.get(a.theIntCourse).intCourseTitle.equals(theIntCourse) )
				{
					numAssignedForCourse++;
				}
			}
			if(verbose)
				System.out.println("For course " + theIntCourse + ", there were" + numAssignedForCourse + "assigned.");
									
			intCourseRecordLine = new IntCourseRecord(theIntCourse, numAssignedForCourse);
			workingIntCourseRecords.add(intCourseRecordLine);
			if(verbose)
				System.out.println("Recorded new intCourseRecord, " + theIntCourse + " " + numAssignedForCourse);
		}
		
		for(int i = 0; i < workingIntCourseRecords.size(); i++)
		{
			intCourseRecordLine = workingIntCourseRecords.get(i);
			if(intCourseRecordLine.intCourseNumAssigned < studentsPerMentor)
			{
				if(verbose)
				{
					System.out.println("OMG! Too few students assigned to one room for one course! "
					+ intCourseRecordLine.intCourseTitle + " has " + intCourseRecordLine.intCourseNumAssigned 
					+ " students assigned, in room " + workingRoomRecords.room + ".");
				}
			}
		}
		
		for(int i = 0; i < workingIntCourseRecords.size(); i++)
		{
			intCourseRecordLine = workingIntCourseRecords.get(i);
			if(verbose)
			{
				if(intCourseRecordLine.intCourseNumAssigned < studentsPerMentor)
				System.out.println("OMG! In room " + workingRoomRecords.room + ", int course " + intCourseRecordLine.intCourseTitle + " has " + intCourseRecordLine.intCourseNumAssigned + " students assigned.");
			}
		}
		
		if(workingRoomRecords.numAssigned > maxCapacity && verbose)
			System.out.println("OMG! In room " + workingRoomRecords.room + ", there are " + workingRoomRecords.numAssigned + " total students assigned.");

		Collections.sort(workingAssignRecords, new intCourseTitleComparatorForAssignRecords() );
		Collections.sort(workingIntCourseRecords, new intCourseTitleComparatorForIntCourseRecords() );

		workingRoomRecords = new RoomRecord(workingRoomRecords.room, workingRoomRecords.useRecords, workingRoomRecords.notIntervention, 
			workingAssignRecords, workingIntCourseRecords, workingAssignRecords.size() );
			
		return workingRoomRecords;
	} 
	
	// Verify student assignment numbers to prevent data leaks or duplicates.  compares original and assigned student records.
	// Nothing returned; just a report.
	protected void verifyStudentAssignments(ArrayList<StudentRecord> originalStudentRecords, ArrayList<RoomRecord> roomRecords, boolean verbose)
	{
		boolean studentsAssignedCorrectly = true, foundOneMatch = true;
		StudentRecord originalSRLine = null, assignedSRLine = null;  // assignRecordLine = null, 
		ScheduleRecord scheduleRecordLine1 = null, scheduleRecordLine2 = null;
		ArrayList<StudentRecord> assignedStudentRecords = new ArrayList<StudentRecord>();
		
		if(verbose)
			System.out.println("Beginning verifyStudentAssignments().");

		// Make assignedStudentRecords from roomRecords
		for(RoomRecord r : roomRecords)  
		{
			for(StudentRecord a : r.assignRecords)
			{
				assignedStudentRecords.add(new StudentRecord(a.studentID, a.firstName, a.middleName, a.lastName, a.grade, a.distributionRoom,
					a.theIntCourse, a.numIntCourses, a.studentSchedule, a.caseManager) );
			}
		}
		
		// Sort the two studentrecord lists so they should match 1-1
		Collections.sort(originalStudentRecords, new studentIDComparatorForStudentRecords() );
		Collections.sort(assignedStudentRecords, new studentIDComparatorForStudentRecords() );
		
		if(verbose)
			System.out.println("There were " + originalStudentRecords.size() + " students to assign, and " + assignedStudentRecords.size() + " were actually assigned.\n");
		
		// Check for mismatches
		if(verbose)
			System.out.println("The following is a list of mismatches between the original student records and the assigned student records.");
		for(int i = 0; i < originalStudentRecords.size(); i++)
		{
			originalSRLine = (StudentRecord)originalStudentRecords.get(i);
			assignedSRLine = (StudentRecord)assignedStudentRecords.get(i);
			studentsAssignedCorrectly = false;
			if(originalSRLine.studentID == assignedSRLine.studentID 
				&& originalSRLine.firstName.equals(assignedSRLine.firstName)
				&& originalSRLine.middleName.equals(assignedSRLine.middleName)
				&& originalSRLine.lastName.equals(assignedSRLine.lastName)
				// && originalSRLine.theIntCourse == assignedSRLine.theIntCourse   // theIntCourse may differ after adjustment
				&& originalSRLine.numIntCourses == assignedSRLine.numIntCourses)
			{
				// if every assigned studentSchedule ScheduleRecord is in the original student's studentSchedule
				for(int j = 0; j < assignedSRLine.studentSchedule.size(); j++)   
				{
					scheduleRecordLine1 =  (ScheduleRecord)assignedSRLine.studentSchedule.get(j);
					for(int k = 0; k < originalSRLine.studentSchedule.size(); k++)
					{
						scheduleRecordLine2 = (ScheduleRecord)originalSRLine.studentSchedule.get(j);
						if(scheduleRecordLine1.intCourseTitle.equals(scheduleRecordLine2.intCourseTitle) 
							&& scheduleRecordLine1.room.equals(scheduleRecordLine2.room)
							&& scheduleRecordLine1.period == scheduleRecordLine2.period )
						{
							studentsAssignedCorrectly = true;
							// stop looking through original student's schedule if found the assigned schedule record in the original
							k = originalSRLine.studentSchedule.size(); 
						} else
						{
							studentsAssignedCorrectly = false; // if it's never found, will print a mismatch 
						}
					}
				}
				if(!studentsAssignedCorrectly && verbose) // if the assigned student's schedule record was never found in the original student's schedule
					System.out.println("Original record " + originalSRLine.studentID + " does not match assigned record.");

			} else // if the assigned student and original student don't match IDs, names, or other fields
			{
				if(verbose)
					System.out.println("Original record " + originalSRLine.studentID + " does not match assigned record.");
			}
		}


		// check for duplicates
		if(verbose)
			System.out.println("The following is a list of students duplicated in assigned student records");
		for(StudentRecord assignedSRLine1 : assignedStudentRecords)
		{
			foundOneMatch = false;
			for(StudentRecord assignedSRLine2 : assignedStudentRecords)
			{
				if(foundOneMatch == false && assignedSRLine1.studentID == assignedSRLine2.studentID)
					foundOneMatch = true;
				else if(foundOneMatch == true && assignedSRLine1.studentID == assignedSRLine2.studentID)
				{
					if(verbose)
					{
						System.out.println("\n" + assignedSRLine1.studentID + ", " + assignedSRLine2.studentID);
					}
				}
			}
		}
		
		if(verbose)
			System.out.println("\nIf no records were printed, the original and assigned students match, with no duplicates in the assigned student records.");
	}
	
	
	// DIAGNOSTIC REPORTS
	
	// Print summary of all student records
	protected void allStudentRecordsSummary(ArrayList<StudentRecord> studentRecords)
	{
		ScheduleRecord scheduleRecordLine = null;
		Collections.sort(studentRecords, new studentIDComparatorForStudentRecords() );
		System.out.println("\nThis is a complete list of all students needing intervention, by ID number.  Student who do not need intervention in any class are not listed."); 
		System.out.println("There are " + studentRecords.size() + " students in the list.");
		System.out.println("The intervention course is listed, as well as the room number and period where the student takes the course.");
		System.out.println("Last, the possible intervention courses are listed, in order of mark, from highest to lowest (D+ or lower).");
		for(StudentRecord stu : studentRecords)
		{			
			System.out.println("\nStudent " + stu.studentID + " " + stu.firstName + " " 
				+ stu.middleName + " " + stu.lastName + "was assigned intervention course " + stu.studentSchedule.get(stu.theIntCourse).intCourseTitle 
				+ " in room " + stu.studentSchedule.get(stu.theIntCourse).room + " during period " + stu.studentSchedule.get(stu.theIntCourse).period + ".");
			System.out.println("The student has the following possible intervention courses:");
			for(int i = 0; i < stu.studentSchedule.size(); i++)
			{
				scheduleRecordLine = stu.studentSchedule.get(i);
				System.out.println("    " + scheduleRecordLine.intCourseTitle + " " + scheduleRecordLine.room + " " + scheduleRecordLine.period);
			}
		}
	}

	// Print all assignment records in CSV friendly format.
	protected void allStudentRecordsCSV(ArrayList<RoomRecord> roomRecords)
	{
		Collections.sort(roomRecords, new roomComparatorForRoomRecords() );
		System.out.println("\nThis is a CSV-ready list of the students above, sorted by room number then course title, "
			+ "showing the following fields:"
			+ "\nAssigned room, studentID, last name, first name, middle name, grade, distributionRoom, intervention course title");
		for(RoomRecord r : roomRecords)
		{
			for(StudentRecord stu : r.assignRecords)
			{
				System.out.println(r.room + "," + stu.studentID + "," + stu.lastName + "," + stu.firstName + "," + stu.middleName + "," 
					+ stu.grade + "," + stu.distributionRoom + "," + stu.studentSchedule.get(stu.theIntCourse).intCourseTitle);
			}
		}
	}

	// Print student records by course title
	protected void studentRecordsByCourseTitle(ArrayList<StudentRecord> studentRecords, String courseTitle)
	{
		System.out.println("\nThis is a complete list of all students needing intervention, by ID number and the course title " 
			+ courseTitle + " in which they need intervention.  Student who do not need intervention in any class are not listed."); 
		for(StudentRecord s : studentRecords)
		{			
			if(s.studentSchedule.get(s.theIntCourse).equals(courseTitle) )
			{
				System.out.println(s.studentID + ", " + s.firstName + " " + s.middleName + " " + s.lastName 
					+ s.studentSchedule.get(s.theIntCourse) + ", studentSchedule size " + s.studentSchedule.size() );
			}
		}
	}
	
	// Print all room records
	protected void allRoomRecordsSummary(ArrayList<RoomRecord> roomRecords)
	{
		ArrayList<UseRecord> workingUseRecords = new ArrayList<UseRecord>();
		ArrayList<StudentRecord> workingAssignRecords = new ArrayList<StudentRecord>();
		ArrayList<IntCourseRecord> workingIntCourseRecords = new ArrayList<IntCourseRecord>();
		Collections.sort(roomRecords, new roomComparatorForRoomRecords() );
		
		System.out.println("\n\nRoom Records.\nThere are " + roomRecords.size() + " rooms assigned.");
				
		for(RoomRecord r : roomRecords)
		{	
			System.out.print("\n\nIn room " + r.room + ", ");
			workingUseRecords = r.useRecords;				
			workingAssignRecords = r.assignRecords;
			
			if(r.notIntervention == true)
				System.out.println("no courses taught here during the school day have ANY students assigned for intervention to qualify as intervention courses.");
			else
			{
				System.out.println("the following " + workingUseRecords.size() + " intervention courses are taught during the school day:");
				for(UseRecord u : workingUseRecords)
				{
					System.out.println(u.courseTitle + "   ");
				}
			}
			
			if(workingAssignRecords.size() != 0)
			{
				System.out.println("The following " + workingAssignRecords.size() + " students are assigned to this room for the intervention period.");
				System.out.println("Freshmen:");
				for(StudentRecord s : workingAssignRecords)
				{
					if(s.grade == 9)
					{
						System.out.println(s.studentID + ": " + s.lastName + ", " + s.firstName + " " + s.middleName + ", " + s.studentSchedule.get(s.theIntCourse).intCourseTitle );
					}
				}
				System.out.println("Upperclassmen (Sophomores, Juniors, and Seniors):");
				for(StudentRecord s : workingAssignRecords)
				{
					if(s.grade != 9)
					{
						System.out.println(s.studentID + ": " + s.lastName + ", " + s.firstName + " " + s.middleName + ", " + s.studentSchedule.get(s.theIntCourse).intCourseTitle );
					}
				}
				workingIntCourseRecords = r.intCourseRecords; 
				System.out.println("This is a listing of the intervention period course titles and number of students assigned for each.");
				for(IntCourseRecord i : workingIntCourseRecords)
				{
					System.out.println(i.intCourseTitle + ", " + i.intCourseNumAssigned);
				}							
			} else
			{
				System.out.println("No students were assigned to this room.");				
			}
		}
	}		
	
	// Print room records by course title: first, rooms where course is taught; second, rooms where no intervention course is taught; 
	// third, rooms where other intervention courses are taught but the int course is not.  Uses method roomPrintFormatter().
	protected void roomRecordsSummaryByCourseTitle(ArrayList<RoomRecord> roomRecords, String courseTitle)
	{		
		boolean printFlag;
		
		System.out.println("\n\n1. These are the intervention rooms where " + courseTitle + " is taught:");
		for(RoomRecord r : roomRecords)
		{	
			for(UseRecord u : r.useRecords)
			{
				if(u.courseTitle.equals(courseTitle) )
					roomPrintFormatter(r);
			}
		}
		
		System.out.println("\n\n2. These are the NON-intervention rooms where students with " + courseTitle + " are placed:");
		for(RoomRecord r : roomRecords)
		{
			for(IntCourseRecord i : r.intCourseRecords)
			{
				if(i.intCourseTitle.equals(courseTitle) && r.notIntervention == true)
					roomPrintFormatter(r);
			}
		}

		System.out.println("\n\n3. These are the intervention rooms where students with " + courseTitle + " are placed, but the subject is NOT taught:");
		for(RoomRecord r : roomRecords)
		{	
			printFlag = true;
			if(r.notIntervention == false)  
			{
				for(UseRecord u : r.useRecords) // is the course NOT taught here?
				{
					if(u.courseTitle.equals(courseTitle) )
						printFlag = false;
				}
				if(printFlag == true)
				{
					for(IntCourseRecord i : r.intCourseRecords)
					{
						if(i.intCourseTitle.equals(courseTitle) )
							roomPrintFormatter(r);
					}					
				}
			}
		}
	}

	// This method is used by printSchedule().
	protected void roomPrintFormatter(RoomRecord roomRecordLine) 
	{
		ArrayList<UseRecord> workingUseRecords = new ArrayList<UseRecord>();
		ArrayList<StudentRecord> workingAssignRecords = new ArrayList<StudentRecord>();
		ArrayList<IntCourseRecord> workingIntCourseRecords = new ArrayList<IntCourseRecord>();
		
		System.out.print("\n\nIn room " + roomRecordLine.room + ", ");
			
		if(roomRecordLine.notIntervention == true)
			System.out.println("no courses taught here during the school day have (enough) students assigned for intervention to qualify as intervention courses.");
		else
		{
			workingUseRecords = roomRecordLine.useRecords;
			System.out.println("the following " + workingUseRecords.size() + " intervention courses are taught during the school day:");
			for(UseRecord u : workingUseRecords)
			{
				System.out.println(u.courseTitle + "   ");
			}
		}
		
		workingAssignRecords = roomRecordLine.assignRecords;
		if(workingAssignRecords.size() != 0)
		{
			System.out.println("The following " + workingAssignRecords.size() + " students are assigned to this room for the intervention period.");
			for(StudentRecord s : workingAssignRecords)
			{
				System.out.println(s.studentID + " " + s.studentSchedule.get(s.theIntCourse).intCourseTitle );
			}

			// intCourseRecords = roomRecordLine.intCourseRecords;
			workingIntCourseRecords = roomRecordLine.intCourseRecords; 
			System.out.println("This is a listing of the intervention period course titles and number of students assigned for each.");
			for(IntCourseRecord i : workingIntCourseRecords)
			{
				System.out.println(i.intCourseTitle + ", " + i.intCourseNumAssigned);
			}							
		}
		
		System.out.println("There are a total of " + roomRecordLine.numAssigned + " students assigned to this room during intervention period.");
	}


	// Print course records for all courses that are over capacity (for use in "Reduce overcapacity courses" method)
	protected void printCourseRecordsOverCapacity(ArrayList<StudentRecord> studentRecords, ArrayList<RoomRecord> roomRecords)
	{
		ArrayList<CourseRecord> courseRecords = new ArrayList<CourseRecord>();
		courseRecords = getCourseRecords(studentRecords, roomRecords);

		System.out.println("\nThese are the courses that are currently over capacity:");
		int totalNumStudents = 0, totalCapacity = 0;
		
		Collections.sort(courseRecords, new intCourseTitleComparatorForCourseRecords() );

		for(CourseRecord c : courseRecords)
		{
			if(c.numStudents > c.capacity)
			{
				System.out.println(c.intCourseTitle + ", assigned " + c.numStudents + ", capacity " + c.capacity);
			}
			totalNumStudents += c.numStudents;
			totalCapacity += c.capacity;
		}
		System.out.println("According to courseRecords, " + totalNumStudents + " students must be assigned, and for all available rooms, " 
		+ "the total capacity available is " + totalCapacity 
		+ ". According to studentRecords, " + studentRecords.size() + " students should be assigned (should match courseRecords).");
	}
	
}  // End of class Assigner

// COMPARATOR OBJECTS

/*
// Comparator object that allows sorting the studentRecords or assignRecords ArrayList by intCourseTitle.
class intCourseTitleComparatorForStudentRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		StudentRecord A=(StudentRecord)o1;  
		StudentRecord B=(StudentRecord)o2;  

		return A.studentSchedule.get(A.theIntCourse).intCourseTitle.compareTo(B.studentSchedule.get(B.theIntCourse).intCourseTitle );  
	}  
}  
*/

// Comparator object that allows sorting the intCourseRecords ArrayList by intCourseTitle.
class intCourseTitleComparatorForIntCourseRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		IntCourseRecord A=(IntCourseRecord)o1;  
		IntCourseRecord B=(IntCourseRecord)o2;  

		return A.intCourseTitle.compareTo(B.intCourseTitle);  
	}  
}  

// Comparator object that allows sorting the studentRecords or assignRecords ArrayList by studentID.
class studentIDComparatorForStudentRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		StudentRecord A=(StudentRecord)o1;  
		StudentRecord B=(StudentRecord)o2;  
		
		if(A.studentID < B.studentID)
			return -1;
		else if(A.studentID == B.studentID)
			return 0;
		else 
			return 1;  
	}  
}  

// Comparator object that allows sorting the courseRecords ArrayList by numStudents.
class numStudentsComparatorForCourseRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		CourseRecord A=(CourseRecord)o1;  
		CourseRecord B=(CourseRecord)o2;  
		
		if(A.numStudents < B.numStudents)
			return -1;
		else if(A.numStudents == B.numStudents)
			return 0;
		else 
			return 1;  
	}  
}  

// Comparator object that allows sorting the roomRecords ArrayList by room.
class roomComparatorForRoomRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		RoomRecord A=(RoomRecord)o1;  
		RoomRecord B=(RoomRecord)o2;  

		if(A.room != null && B.room != null )
			return A.room.compareTo(B.room);  
		else
			return -1;
	}  
}  

// Comparator object that allows sorting the roomRecords ArrayList by numAssigned.
class numAssignedComparatorForRoomRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		RoomRecord A=(RoomRecord)o1;  
		RoomRecord B=(RoomRecord)o2;  

		if(A.numAssigned < B.numAssigned)
			return -1;
		else if(A.numAssigned == B.numAssigned)
			return 0;
		else 
			return 1;  	}  
}  

