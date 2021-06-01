select distinct coursetitle as "Intervention Course"
	, count(distinct room) as "No. Rooms for Intervention"
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
group by coursetitle
order by coursetitle asc
