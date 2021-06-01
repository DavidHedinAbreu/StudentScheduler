select s1.coursetitle as "Intervention Course"
	, count(s1.studentno) as "No. Students for Intervention"
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
group by s1.coursetitle
having count(s1.studentno) > 12
order by count(s1.studentno) desc