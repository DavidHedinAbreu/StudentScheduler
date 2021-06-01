select distinct coursetitle as "Intervention Course"
	, room as "Room Number"
from students
where coursetitle not like '%AcademicMentor%' 
	and coursetitle not like '%Cond %' 
 	and coursetitle not like '%CT %' 
 	and coursetitle not like '%GCC %'
	and coursetitle not like '%LAVC %' 
	and coursetitle not like '%TA %' 
    and coursetitle not like '%TA/%'
    and coursetitle not like '%Tm %' 
    and period < 7
    and room not like ""
order by coursetitle asc, room asc
