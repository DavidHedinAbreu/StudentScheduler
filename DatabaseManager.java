import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseManager 
{
	private int numIntCourses, numberIntCourses, studentPeriod, theCourseCapacity;
	private ArrayList<String> intCourseTitlesTEMP = new ArrayList<String>(), intCourseTitles = new ArrayList<String>();
	private String JDBC_DRIVER, DB_URL, USER, PASS, CSV_URL, sql, studentCourseTitle, studentRoom, theRoom, theCourseTitle, previousRoom;
	private boolean notIntervention = false;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet res = null;
	private ArrayList<StudentRecord> workingAssignRecords = new ArrayList<StudentRecord>(), assignRecords = new ArrayList<StudentRecord>(), studentRecords = new ArrayList<StudentRecord>();
	private StudentRecord previousStudent = null, studentRecordLineTEMP = null, studentRecordLine = null;
	private ArrayList<ScheduleRecord> bogus = new ArrayList<ScheduleRecord>(), studentScheduleTEMP = new ArrayList<ScheduleRecord>(), studentSchedule = new ArrayList<ScheduleRecord>();
	private ScheduleRecord scheduleRecordLine = null;
	private RoomRecord roomRecordLine = null;
	private ArrayList<UseRecord> useRecords = new ArrayList<UseRecord>();
	private UseRecord useRecordLine = null;
	private ArrayList<IntCourseRecord> intCourseRecords = new ArrayList<IntCourseRecord>();
	private ArrayList<RoomRecord> roomRecords = new ArrayList<RoomRecord>();
	private String distributionRoom = "";
	
	// Constructor.  Creates INTERVENTION MySql database, populates it with the student information from the csv file.
	protected DatabaseManager(String JDBC_DRIVER, String DB_URL, String USER, String PASS, String CSV_URL, 
		String createColumns_sql, String loadColumns_sql)  
	{
		
		try{
			//Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//Open a connection
			System.out.println("Connecting to MySQL...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Instantiate statement with default navigation type and concurrency:
			// ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.CONCUR_READ_ONLY
			stmt = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		
			//Drop database 'INTERVENTION' if it exists
			System.out.println("Deleting database INTERVENTION...");
			sql = "DROP DATABASE IF EXISTS INTERVENTION";
			stmt.executeUpdate(sql);

			//Create database 'INTERVENTION'
			System.out.println("Creating database INTERVENTION...");
			sql = "CREATE DATABASE IF NOT EXISTS INTERVENTION";
			stmt.executeUpdate(sql);
		
			//Select database 'INTERVENTION'
			System.out.println("Selecting database INTERVENTION...");
			sql = "USE INTERVENTION";
			stmt.executeUpdate(sql);

			//Create table 'students' in database 'INTERVENTION'
			System.out.println("Creating table 'Students' in db INTERVENTION...");
			sql = createColumns_sql;
			stmt.executeUpdate(sql);
		
			// Import student.csv into database 'intervention', table 'Students'
			System.out.println("Importing CSV file into table 'Students'...");
			sql = "LOAD DATA INFILE '" + CSV_URL + "'"
				+ " INTO TABLE Students "
				+ " FIELDS TERMINATED BY ',' "
				+ " LINES TERMINATED BY '\\n' "
				+ " IGNORE 1 ROWS "
				+ " (" + loadColumns_sql + ")";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block
	}

	// UNFINISHED: loads the completed roomRecords information into the INTERVENTION database
	private void addSchedule(String JDBC_DRIVER, String DB_URL, String USER, String PASS, String CSV_URL, ArrayList<RoomRecord> roomRecords) 
	{
		String room = "";
		int studentNo, id;
		StudentRecord assignRecordLine = null;
		ArrayList<StudentRecord> workingAssignRecords = new ArrayList<StudentRecord>();
		
		try{
			//Register JDBC driver
			Class.forName(JDBC_DRIVER);

			//Open a connection
			System.out.println("Connecting to MySQL...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Instantiate statement with default navigation type and concurrency:
			// ResultSet.TYPE_FORWARD_ONLY,
			// ResultSet.CONCUR_READ_ONLY
			stmt = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
				
			//Select database 'INTERVENTION'
			System.out.println("Selecting database INTERVENTION...");
			sql = "USE INTERVENTION";
			stmt.executeUpdate(sql);

			//Create table 'Schedule' in database 'INTERVENTION'
			System.out.println("Creating table 'Schedule' in db INTERVENTION...");
			sql = "CREATE TABLE IF NOT EXISTS Schedule " +
				" (room varchar(255), " +
				" studentno INTEGER not NULL, " +  // 'not NULL' needed?
 				" primary key (task_id) )";  // does it need a primary key?

			stmt.executeUpdate(sql);
		
			// Export roomRecords into database 'INTERVENTION', table 'Schedule'
			System.out.println("Exporting roomRecords into table 'Schedule'...");
						
			for(RoomRecord r : roomRecords)
			{
				room = (String)r.room;
				workingAssignRecords = roomRecordLine.assignRecords;
				for(StudentRecord a : workingAssignRecords)
				{
					studentNo = (int)a.studentID;
					sql = "INSERT INTO Schedule (room, studentno)"
						+ " VALUES ('" + room + "', " + studentNo + ")";  // what about the key, is it auto-generated?
					
					stmt.executeUpdate(sql);
					
					// unfinished
				}
			}

		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block
	}

	// Prepares studentRecords
	protected ArrayList<StudentRecord> getStudentRecords(int studentsPerMentor, boolean firstSemesterFirstGradingPeriod, boolean useMarkNotPercent, 
		int distributionPeriod, ArrayList<String> priorityCourses, ArrayList<String> noAssignCourses, ArrayList<CaseManager> caseManagerRecords) 
	{		
		int theStudentNo;
		String theDistributionRoom, caseManagerName, caseManagerRoom = "";
		StudentRecord s = null;
		CaseManager studentCaseManager = null;  // bogus

		System.out.println("\nBeginning getStudentRecords()");
		try 
		{
			// Get the intervention courses for every student (highest courses < 70%), if the total number of students needing 
			// intervention is >= studentsPerMentor.  Ordered by asc ID, asc percent/fakepercent.
			// as ResultsSet object.
			sql = "SELECT" 
				+ " studentno,"
				+ " firstname,"
				+ " middlename,"
				+ " lastname,"
				+ " grade,"
				+ " room,"
				+ " period,"
				+ " coursetitle,";
				if(useMarkNotPercent)
				{
					sql += " mark,";
				} else
				{
					sql += " percent,";
				}
				if(useMarkNotPercent)
				{
					sql += " CASE"
					+ "     WHEN"
					+ "         mark LIKE BINARY '%D+%'"
					+ "             AND coursetitle NOT IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         68.00"
					+ "     WHEN"
					+ "         mark LIKE BINARY '%D+%'"
					+ "             AND coursetitle IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         69.00"

					+ "     WHEN"
					+ "         mark LIKE BINARY '%D%'"
					+ "             AND coursetitle NOT IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         64.00"
					+ "     WHEN"
					+ "         mark LIKE BINARY '%D%'"
					+ "             AND coursetitle IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         65.00"

					+ "     WHEN"
					+ "         mark LIKE BINARY '%D-%'"
					+ "             AND coursetitle NOT IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         61.00"
					+ "     WHEN"
					+ "         mark LIKE BINARY '%D-%'"
					+ "             AND coursetitle IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         62.00"

					+ "     WHEN"
					+ "         mark LIKE BINARY '%F%'"
					+ "             AND coursetitle NOT IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         58.00"
					+ "     WHEN"
					+ "         mark LIKE BINARY '%F%'"
					+ "             AND coursetitle IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         59.00"

					+ "     ELSE 71.00"
					+ " END AS 'fakepercent'";
				}
				else
				{
					sql += "percent,"
					+ " CASE"
					+ "     WHEN"
					+ "         coursetitle IN ('%" ;
					for(int i = 0; i < priorityCourses.size() - 1; i++)
					{
						sql += priorityCourses.get(i) + "%', '%";   
					}
					sql += priorityCourses.get(priorityCourses.size() - 1)
					+ "%')"
					+ "     THEN"
					+ "         TRUE"
					+ "		ELSE"
					+ "			FALSE"
					+ "	END AS 'ispriority'";
				}
				sql += " FROM"
				+ "     students"
				+ " WHERE"
				// + "     coursetitle NOT LIKE BINARY ('" ;
				+ "     coursetitle NOT LIKE BINARY '" ;
				for(int i = 0; i < noAssignCourses.size() - 1; i++)
				{
					sql += noAssignCourses.get(i) + "' AND coursetitle NOT LIKE BINARY '";   
				}
				sql += noAssignCourses.get(noAssignCourses.size() - 1)
				// + "')"				
				+ "'"				
				+ "         AND period < 7";  // may depend on customer schedule"

				if(firstSemesterFirstGradingPeriod)	
					sql += "         AND grade < 12";	
					
				if(useMarkNotPercent)  // when student changes teacher in middle of semester, and one record line for the course has no mark or percent 
					sql += " AND mark IS NOT NULL";
				else
					sql += " AND percent IS NOT NULL";

				sql += " GROUP BY studentno, coursetitle";
				
				if(useMarkNotPercent)
					sql += " HAVING fakepercent < 70";
				else
					sql += " HAVING percent < 70";
					
				sql += " ORDER BY studentno ASC ";
				
				if(useMarkNotPercent)
					sql += ", fakepercent DESC";
				else
					sql += ", ispriority ASC, percent DESC";  // in mysql, true = 0, false = 1

			
			// See note above about navigation and concurrency types for ResultSet object created by instantiation of Statement object.  
			res = stmt.executeQuery(sql);	
		
			// Move cursor to before the first row.  May not be necessary, default may be before first row.
			res.beforeFirst();

			// Extract data from result set
			System.out.println("Transferring result set records to ArrayList studentRecords...");
			previousStudent = new StudentRecord(0, "", "", "", 0, "", 0, 0, bogus, studentCaseManager);  // studentCaseManager is bogus at this time.
			numIntCourses = 0;
 			while(res.next())
 			{
				// record the data from the next line in interventionCoursesByStudent
				studentRecordLineTEMP = new StudentRecord(res.getInt("studentno"), 
					res.getString("firstname"), res.getString("middlename"), 
					res.getString("lastname"), res.getInt("grade"), "", 0, 0, bogus, studentCaseManager);  // studentCaseManager is bogus at this time.
				studentCourseTitle = verifyChars(res.getString("coursetitle") );
				studentRoom = verifyChars(res.getString("room") );
				studentPeriod = res.getInt("period");
				
			// upon next student, save student record with studentSchedule and caseManager
				if(studentRecordLineTEMP.studentID != previousStudent.studentID && previousStudent.studentID != 0)  
				{
					studentSchedule = new ArrayList<ScheduleRecord>(studentScheduleTEMP);  // need to deep clone here.
					studentRecordLine = new StudentRecord(previousStudent.studentID, previousStudent.firstName, previousStudent.middleName, 
						previousStudent.lastName, previousStudent.grade, distributionRoom, 0, numIntCourses, studentSchedule, studentCaseManager);
					studentRecords.add(studentRecordLine);

					// reset
					numIntCourses = 0;
					distributionRoom = "";
					studentScheduleTEMP.clear();

					// add to studentSchedule until next student
					scheduleRecordLine = new ScheduleRecord(studentCourseTitle, studentRoom, studentPeriod);
					studentScheduleTEMP.add(scheduleRecordLine);
					numIntCourses++;

					// reset
					previousStudent = new StudentRecord(studentRecordLineTEMP.studentID, studentRecordLineTEMP.firstName, studentRecordLineTEMP.middleName, 
						studentRecordLineTEMP.lastName, studentRecordLineTEMP.grade, "", 0, numIntCourses, studentScheduleTEMP, studentCaseManager);
				}
				else
				{
					// add to studentSchedule until next student
					scheduleRecordLine = new ScheduleRecord(studentCourseTitle, studentRoom, studentPeriod);
					studentScheduleTEMP.add(scheduleRecordLine);
					numIntCourses++;
					
					// reset
					previousStudent = new StudentRecord(studentRecordLineTEMP.studentID, studentRecordLineTEMP.firstName, studentRecordLineTEMP.middleName, 
						studentRecordLineTEMP.lastName, studentRecordLineTEMP.grade, "", 0, numIntCourses, studentScheduleTEMP, studentCaseManager);
				}
			}  // end while(res.next() ) loop

		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block


		try 
		{
			// Get studentNo and distributionRoom.  Parse studentRecords, if studentNo matches, save distributionRoom.
			sql = "SELECT\n	studentno,\n	room\nFROM students\nWHERE period = " + distributionPeriod + "\nORDER BY studentNo";
			
			// See note above about navigation and concurrency types for ResultSet object created by instantiation of Statement object.  
			res = stmt.executeQuery(sql);	
		
			// Move cursor to before the first row.  May not be necessary, default may be before first row.
			res.beforeFirst();

			// Extract data from result set
			System.out.println("Adding distribution room to student records...");
 			while(res.next())
 			{
				theStudentNo = res.getInt("studentno");
				theDistributionRoom = verifyChars(res.getString("room") );
				for(int i = 0; i < studentRecords.size(); i++)
				{
					s = (StudentRecord)studentRecords.get(i);
					if(s.studentID == theStudentNo)
					{
						s = new StudentRecord(s.studentID, s.firstName, s.middleName, s.lastName, s.grade, theDistributionRoom, 0, 
							s.numIntCourses, s.studentSchedule, s.caseManager);
						studentRecords.set(i, s);
					}  
				}  // End studentRecords loop.

			}  // End while(res.next() ) loop
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block
		
		try 
		{
			// Get studentNo and casemanager.  Parse studentRecords, if studentNo matches, save casemanager.
			sql = "SELECT\n	studentno,\n	casemanager\nFROM students\nWHERE casemanager NOT IN (null, '') OR coursetitle LIKE BINARY '%Unsched Per 0%'\nORDER BY studentNo";
			
			// See note above about navigation and concurrency types for ResultSet object created by instantiation of Statement object.  
			res = stmt.executeQuery(sql);	
		
			// Move cursor to before the first row.  May not be necessary, default may be before first row.
			res.beforeFirst();

			// Extract data from result set
			System.out.println("Adding case manager name and room to student records...");
 			while(res.next())
 			{
				theStudentNo = res.getInt("studentno");
				caseManagerName = verifyChars(res.getString("casemanager") );
				for(CaseManager c : caseManagerRecords)
				{
					if(c.name.equals(caseManagerName) )
					{
						caseManagerRoom = c.room;
					}
				}
				studentCaseManager = new CaseManager(caseManagerName, caseManagerRoom);
				for(int i = 0; i < studentRecords.size(); i++)
				{
					s = (StudentRecord)studentRecords.get(i);
					if(s.studentID == theStudentNo)
					{
						s = new StudentRecord(s.studentID, s.firstName, s.middleName, s.lastName, s.grade, s.distributionRoom, 0, 
							s.numIntCourses, s.studentSchedule, studentCaseManager);
						studentRecords.set(i, s);
					}  
				}  // End studentRecords loop.

			}  // End while(res.next() ) loop
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block




		// Return ArrayList
		return studentRecords;

	}  // End of the getStudentRecords method

	// Prepares roomRecords, using studentRecords to prepare useRecords.  At first, assignRecords and intCourseRecords are empty. 
	// These will be completed by Assigner methods.
	protected ArrayList<RoomRecord> getRoomRecords(int studentsPerMentor, int maxCapacity, ArrayList<StudentRecord> studentRecords, 
		ArrayList<String> noAssignRooms, ArrayList<String> noAssignCourses, ArrayList<CaseManager> caseManagerRecords) 
	{
		System.out.println("Beginning getRoomRecords()...");

		// Create intCourseTitles from studentRecords
		for(StudentRecord stu : studentRecords)
		{
			for(ScheduleRecord sched : stu.studentSchedule)
			{
				intCourseTitlesTEMP.add(sched.intCourseTitle);
			}
		}
		
		for(String title : intCourseTitlesTEMP)
		{
			if(!intCourseTitles.contains(title) )
			{
				intCourseTitles.add(title);
			}
		}
		
		try
		{
			// Get the course titles taught by room
			sql = "SELECT DISTINCT"
			+ " room,"
			+ "    coursetitle"
			+ " FROM students"
			+ " WHERE"
			+ "     coursetitle NOT LIKE BINARY '%" ;
			for(int i = 0; i < noAssignCourses.size() - 1; i++)
			{
				sql += noAssignCourses.get(i) + "%'   AND coursetitle NOT LIKE BINARY '%";   
			}
			sql += noAssignCourses.get(noAssignCourses.size() - 1)
			+ "%'"				
			+ "			AND period < 7";  // may depend on customer schedule"
			sql += "			AND room NOT LIKE BINARY '%" ;
			// Some teachers may have other duties for intervention and cannot take intervention students,
			// or may need to be with a particular group of students (assigned outside of this app).
			for(int i = 0; i < noAssignRooms.size() - 1; i++)
			{
				sql += noAssignRooms.get(i) + "%'			AND room NOT LIKE BINARY '%";   
			}
			sql += noAssignRooms.get(noAssignRooms.size() - 1)
			+ "%'           AND room NOT LIKE BINARY ''"
			+ " order by room asc, coursetitle asc";
			
			res = stmt.executeQuery(sql);
				
			// Move cursor to before the first row.  May not be necessary, default may be before first row.
			res.beforeFirst();
			
			// Extract data from result set
			System.out.println("Transferring result set records to ArrayList roomRecords...");	

			//Prepare room, useRecords & notIntervention to be placed into roomRecords
			while( res.next() )
			{
				theRoom = verifyChars(res.getString("room") ); // get the next room and courseTitle
				theCourseTitle = verifyChars(res.getString("coursetitle") );
				// if we have finished the courses in a room...
				if( !theRoom.equals(previousRoom) && theRoom != null && !theRoom.equals("") ) 		
				{
					// ... then calculate courseCapacity for each intCourse that the room has, and put this number in useRecords...
					numberIntCourses = useRecords.size();
					if(numberIntCourses != 0)
						theCourseCapacity = (int)((double)maxCapacity/numberIntCourses);  // ex. if maxCap = 14 and numberIntCourses = 3, theCourseCapacity = 4
					for( int i = 0; i < numberIntCourses; i++ )
					{
						useRecordLine = (UseRecord)useRecords.get(i);
						useRecordLine = new UseRecord( useRecordLine.courseTitle, theCourseCapacity );
						useRecords.set(i, useRecordLine);
					}
					
					// ... and determine whether the room has any intervention courses...
					if(numberIntCourses > 0)
						notIntervention = false;
					else
						notIntervention = true;
					
					// ... and record what we found for the previous room in roomRecords(assignRecords & intCourseRecords are empty now.)
					
					roomRecordLine = new RoomRecord(previousRoom, useRecords, notIntervention, assignRecords, intCourseRecords, 0);
					roomRecords.add(roomRecordLine);  
					// System.out.println("Added room record " + roomRecords.get(roomRecords.size() - 1).room + ", useRecords size "
					// 	+ roomRecords.get(roomRecords.size() - 1).useRecords.size() );
					// reset
					useRecords.clear();
					notIntervention = true;
					previousRoom = theRoom;
				}
				
				// put all the intervention courses taught in the room, into useRecords.  Will calculate and add courseCapacity at next room.
				if(intCourseTitles.contains( theCourseTitle ) )
				{
					useRecordLine = new UseRecord(theCourseTitle, 0);
					useRecords.add(useRecordLine);
				}

			}

			// Finish up the record for the last room.
			if(!res.next() )
			{
				// ... then calculate courseCapacity for each intCourse that the room has, and put this number in useRecords...
				numberIntCourses = useRecords.size();
				if(numberIntCourses != 0)
					theCourseCapacity = (int)((double)maxCapacity/numberIntCourses);  // ex. if maxCap = 14 and numberIntCourses = 3, theCourseCapacity = 4
				for( int i = 0; i < numberIntCourses; i++ )
				{
					useRecordLine = (UseRecord)useRecords.get(i);
					useRecordLine = new UseRecord( useRecordLine.courseTitle, theCourseCapacity );
					useRecords.set(i, useRecordLine);
				}
					
				// ... and determine whether the room has any intervention courses...
				if(numberIntCourses > 0)
					notIntervention = false;
				else
					notIntervention = true;
					
				// ... and record what we found for the previous room in roomRecords(assignRecords & intCourseRecords are empty now.)
					
				roomRecordLine = new RoomRecord(theRoom, useRecords, notIntervention, assignRecords, intCourseRecords, 0);
				roomRecords.add(roomRecordLine);  
			}

			// remove null rooms
			for(int i = 0; i < roomRecords.size(); i++)
			{
				if(roomRecords.get(i).room == null || roomRecords.get(i).room.equals("") )
				{
					roomRecords.remove(i);
					i--;
				}
			}

			System.out.println("Completed roomRecords.  roomRecords.size() = " + roomRecords.size() );
					
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} // end try-catch block
		
		// Return an arraylist
		return roomRecords;

	}  // End of the getRoomRecords method
	
	// Closes all resources
	public void closeResources() {
		try{
		if(res!=null)
			res.close();
		}catch(SQLException se2){
		}// nothing we can do
		try{
			if(stmt!=null)
				stmt.close();
		}catch(SQLException se3){
		}// nothing we can do
		try{
			if(conn!=null)
				conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		} 
	}

	public String verifyChars(String input) {
		// String to be scanned to find the pattern.
		String pattern = "([-A-Za-z0-9 ()/]*)";
		String output = "";
		// Create a Pattern object
		Pattern r = Pattern.compile(pattern);

		// Now create matcher object.
		Matcher m = r.matcher(input);
		if (m.find( ) ) {
			output = m.group(0);
			if(output.length() != input.length() )
				System.out.println("Illegal characters found in field value " + output + ", other than legal characters " + pattern);
		}
		
		return output;
	}
	
} // end of DatabaseManager class

// Comparator object that allows sorting the assignRecords or studentRecords ArrayList by intCourseTitle.
class intCourseTitleComparatorForStudentRecords implements Comparator{  
	public int compare(Object o1,Object o2){  
		StudentRecord A=(StudentRecord)o1;  
		StudentRecord B=(StudentRecord)o2;  

		return A.studentSchedule.get(A.theIntCourse).intCourseTitle.compareTo(B.studentSchedule.get(B.theIntCourse).intCourseTitle );  
	}  
}  



