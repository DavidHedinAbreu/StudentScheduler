import java.util.ArrayList;

public class StudentRecord
{  
	int studentID;
	String firstName;
	String middleName;
	String lastName;
	int grade;
	String distributionRoom;
	int theIntCourse;
	int numIntCourses;
	ArrayList<ScheduleRecord> studentSchedule;
	CaseManager caseManager;
	
	StudentRecord(int studentID, String firstName, String middleName, String lastName, int grade, String distributionRoom, int theIntCourse, 
		int numIntCourses, ArrayList<ScheduleRecord> studentSchedule, CaseManager caseManager) 
	{  
		this.studentID=studentID;  
		this.firstName=firstName;
		this.middleName=middleName;
		this.lastName=lastName;
		this.grade=grade;
		this.distributionRoom=distributionRoom;
		this.theIntCourse=theIntCourse;
		this.numIntCourses=numIntCourses;
		this.studentSchedule=studentSchedule;
		this.caseManager=caseManager;
	}
	
}	
