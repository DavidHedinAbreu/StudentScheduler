-- select concat(s1.studentno, ": ", s1.lastname, ", ", s1.firstname, " ", s1.middlename) as Student
-- 	, s1.coursetitle as Course
--     , s1.percent as Percent
-- from students as s1
-- left join students as s2
-- 	on s2.studentno = s1.studentno
--     and s2.percent > s1.percent
-- where s2.coursetitle is null
-- 	and s1.room not like "%Gym%"
--     and s1.period < 7
-- order by s1.lastname asc

select concat(s1.studentno, ": ", s1.lastname, ", ", s1.firstname, " ", s1.middlename) as Student
	, s1.coursetitle as Course
    , s1.percent as Percent
from (
	select studentno, lastname, firstname, middlename, coursetitle, percent, room, period
	from students
    where percent < 70
    and room not like "%Gym%"
    and period < 7) as s1
left join (
	select studentno, lastname, firstname, middlename, coursetitle, percent, room, period
	from students
    where percent < 70
    and room not like "%Gym%"
    and period < 7) as s2
		on s2.studentno = s1.studentno
		and s2.percent > s1.percent
where s2.coursetitle is null
order by s1.lastname asc