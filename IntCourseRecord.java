public class IntCourseRecord
{  
	String intCourseTitle;  // intervention course title  
	int intCourseNumAssigned;  // how many students with that intervention course title are assigned to the room (s.b. < useRecord.courseCapacity)

	IntCourseRecord(String intCourseTitle, int intCourseNumAssigned) 
	{  
		this.intCourseTitle=intCourseTitle;  
		this.intCourseNumAssigned=intCourseNumAssigned;
	}
	
}	
