import java.util.ArrayList;

public class RoomRecord
{  
	String room; // room identifier/number  
	ArrayList<UseRecord> useRecords;  // containing courseTitle, courseCapacity
	Boolean notIntervention;
	ArrayList<StudentRecord> assignRecords;  // containing studentID, intCourseTitle
	ArrayList<IntCourseRecord> intCourseRecords;  // containing intCourseTitle, intCourseNumAssigned
	int numAssigned;
	
	RoomRecord(String room, ArrayList<UseRecord> u, Boolean notIntervention, ArrayList<StudentRecord> a, 
		ArrayList<IntCourseRecord> i, int numAssigned) 
	{  
		this.room=room;  
		this.useRecords=new ArrayList<UseRecord>(u);  // to deep copy, or copy by value
		this.notIntervention=notIntervention;
		this.assignRecords=new ArrayList<StudentRecord>(a);  // to deep copy, or copy by value
		this.intCourseRecords=new ArrayList<IntCourseRecord>(i);  // to deep copy, or copy by value  
		this.numAssigned = numAssigned;
	}
	
}	
