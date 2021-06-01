public class CourseRecord
{  
	String intCourseTitle;  // intervention course title  
	int numStudents;  // how many students with that intervention course title are assigned in roomRecords, assignRecords
	int capacity;  // how many students with that intervention course CAN be taught, in roomRecords useRecords

	CourseRecord(String intCourseTitle, int numStudents, int capacity) 
	{  
		this.intCourseTitle=intCourseTitle;  
		this.numStudents=numStudents;
		this.capacity=capacity;
	}
	
}	
