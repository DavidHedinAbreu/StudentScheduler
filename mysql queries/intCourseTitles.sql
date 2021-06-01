select s1.coursetitle as 'Intervention Course Title'
from (
	select studentno, coursetitle, mark, percent, period
    , case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
        end as FakePercent
	from students
    where (case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
		end) < 70
		and coursetitle not like '%AcademicMentor%'
		and coursetitle not like '%Cond %'
		and coursetitle not like '%CT %'
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %'
		and coursetitle not like '%TA %'
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %'
		and period < 7
        and grade < 12 -- option for first grading period. turn off for other grading periods.
        ) as s1
left join (
	select studentno, coursetitle, mark, percent, period
    , case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
        end as FakePercent
	from students
    where (case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
		end) < 70
		and coursetitle not like '%AcademicMentor%'
		and coursetitle not like '%Cond %'
		and coursetitle not like '%CT %'
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %'
		and coursetitle not like '%TA %'
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %'
		and period < 7
        and grade < 12
		) as s2
on s2.studentno = s1.studentno
	and s2.FakePercent > s1.FakePercent
where s2.coursetitle is null
	and s1.coursetitle not in (
    
    select s3.coursetitle
from (
	select studentno, coursetitle, mark, percent, period
    , case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
        end as FakePercent
	from students
    where (case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
		end) < 70
		and coursetitle not like '%AcademicMentor%'
		and coursetitle not like '%Cond %'
		and coursetitle not like '%CT %'
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %'
		and coursetitle not like '%TA %'
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %'
		and period < 7
        and grade < 12 -- option for first grading period. turn off for other grading periods.
        ) as s3
left join (
	select studentno, coursetitle, mark, percent, period
    , case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
        end as FakePercent
	from students
    where (case
		when mark like 'D+' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 68.00
		when mark like 'D+' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 69.00
		when mark like 'D' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 64.00
		when mark like 'D' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 65.00
		when mark like 'D-' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 61.00
		when mark like 'D-' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 62.00
		when mark like 'F' and coursetitle not in ('Algebra 1', 'Geometry', 'Algebra 2') then 58.00
		when mark like 'F' and coursetitle in ('Algebra 1', 'Geometry', 'Algebra 2') then 59.00
        else 71.00
		end) < 70
		and coursetitle not like '%AcademicMentor%'
		and coursetitle not like '%Cond %'
		and coursetitle not like '%CT %'
		and coursetitle not like '%GCC %'
		and coursetitle not like '%LAVC %'
		and coursetitle not like '%TA %'
		and coursetitle not like '%TA/%'
		and coursetitle not like '%Tm %'
		and period < 7
        and grade < 12
		) as s4
on s4.studentno = s3.studentno
	and s4.FakePercent > s3.FakePercent
where s4.coursetitle is null
group by s3.coursetitle
having count(s3.studentno) < 4
    
    )
group by s1.coursetitle
order by s1.coursetitle asc
