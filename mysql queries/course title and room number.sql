SELECT DISTINCT
    room AS 'Room Number',
    coursetitle AS 'Intervention Course'
FROM
    students
WHERE
    coursetitle NOT LIKE '%AcademicMentor%'
        AND coursetitle NOT LIKE '%Cond %'
        AND coursetitle NOT LIKE '%CT %'
        AND coursetitle NOT LIKE '%GCC %'
        AND coursetitle NOT LIKE '%LAVC %'
        AND coursetitle NOT LIKE '%TA %'
        AND coursetitle NOT LIKE '%TA/%'
        AND coursetitle NOT LIKE '%Tm %'
        AND period < 7
        AND room NOT LIKE ''
ORDER BY room ASC, coursetitle ASC
