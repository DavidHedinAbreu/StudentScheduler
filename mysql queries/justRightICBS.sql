select s1.studentno as "ID"
	, s1.coursetitle as "Intervention Course"
    , s1.percent as "Percent Grade in Course"
from (
	select studentno, coursetitle, percent, period
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
	select studentno, coursetitle, percent, period
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
	and s1.coursetitle not in (
    -- not in the same query as above, grouped by s3.coursetitle, where count(s3.studentno) < studentsPerMentor
		select s3.coursetitle
		from (
			select studentno, coursetitle, percent, period
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
				and period < 7) as s3
		left join (
			select studentno, coursetitle, percent, period
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
				and period < 7) as s4
				on s4.studentno = s3.studentno
					and s4.percent > s3.percent
		where s4.coursetitle is null
		group by s3.coursetitle
		having count(s3.studentno) < 3
	)
order by s1.coursetitle asc
		-- select distinct s3.coursetitle
		-- from (
			-- select studentno, coursetitle, percent, period
			-- from students
			-- where percent < 70 
				-- and coursetitle not like '%AcademicMentor%' 
				-- and coursetitle not like '%Cond %' 
				-- and coursetitle not like '%CT %' 
				-- and coursetitle not like '%GCC %'
				-- and coursetitle not like '%LAVC %' 
				-- and coursetitle not like '%TA %' 
				-- and coursetitle not like '%TA/%'
				-- and coursetitle not like '%Tm %' 
				-- and period < 7) as s3


