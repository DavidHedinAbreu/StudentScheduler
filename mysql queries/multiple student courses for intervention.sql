select studentno as 'Student ID'
	, lastname as 'Last Name'
    , middlename as 'Middle Name'
    , firstname as 'First Name'
	, coursetitle as 'Intervention Course'
    , percent as 'Percent Grade in Course'
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
		and period < 7
order by lastname asc, percent desc