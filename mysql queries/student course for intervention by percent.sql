select concat(s1.studentno, ": ", s1.lastname, ", ", s1.firstname, " ", s1.middlename) as Student
	, s1.coursetitle as "Intervention Course"
    , s1.percent as "Percent Grade in Course"
from (
	select studentno, lastname, firstname, middlename, coursetitle, percent, room, period
	from students
    where percent < 70
		and coursetitle not like '%AcademicMentor%' 
		and coursetitle not like '%Cond %' 
		and coursetitle not like '%CT %' 
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %' 
		and coursetitle not like '%TA %' 
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %' 
		and period < 7) as s1
left join (
	select studentno, lastname, firstname, middlename, coursetitle, percent, room, period
	from students
    where percent < 70
		and coursetitle not like '%AcademicMentor%' 
		and coursetitle not like '%Cond %' 
		and coursetitle not like '%CT %' 
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %' 
		and coursetitle not like '%TA %' 
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %' 
		and period < 7) as s2
		on s2.studentno = s1.studentno
		and s2.percent > s1.percent
where s2.coursetitle is null
order by s1.lastname asc