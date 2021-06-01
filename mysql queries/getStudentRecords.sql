SELECT 
    studentno,
    firstname,
    middlename,
    lastname,
    room,
    period,
    coursetitle,
    mark,
    CASE
        WHEN
            mark LIKE '%D+%'
                AND coursetitle NOT IN ('%AcademicMentor%' , '%Supervention%',
                '% Cond%',
                '%Cond %',
                '%CT %',
                '%GCC %',
                '%LAVC %',
                '%TA %',
                '%TA/%',
                '%Tm %',
                '% Fund%',
                '% Fun%',
                '% Basic%',
                '% Bas%')
        THEN
            68.00
        WHEN
            mark LIKE '%D%'
                AND coursetitle IN ('bogus')
        THEN
            69.00
        WHEN
            mark LIKE '%D%'
                AND coursetitle NOT IN ('%AcademicMentor%' , '%Supervention%',
                '% Cond%',
                '%Cond %',
                '%CT %',
                '%GCC %',
                '%LAVC %',
                '%TA %',
                '%TA/%',
                '%Tm %',
                '% Fund%',
                '% Fun%',
                '% Basic%',
                '% Bas%')
        THEN
            64.00
        WHEN
            mark LIKE '%D%'
                AND coursetitle IN ('bogus')
        THEN
            65.00
        WHEN
            mark LIKE '%D-%'
                AND coursetitle NOT IN ('%AcademicMentor%' , '%Supervention%',
                '% Cond%',
                '%Cond %',
                '%CT %',
                '%GCC %',
                '%LAVC %',
                '%TA %',
                '%TA/%',
                '%Tm %',
                '% Fund%',
                '% Fun%',
                '% Basic%',
                '% Bas%')
        THEN
            61.00
        WHEN
            mark LIKE '%D-%'
                AND coursetitle IN ('bogus')
        THEN
            62.00
        WHEN
            mark LIKE '%F%'
                AND coursetitle NOT IN ('%AcademicMentor%' , '%Supervention%',
                '% Cond%',
                '%Cond %',
                '%CT %',
                '%GCC %',
                '%LAVC %',
                '%TA %',
                '%TA/%',
                '%Tm %',
                '% Fund%',
                '% Fun%',
                '% Basic%',
                '% Bas%')
        THEN
            58.00
        WHEN
            mark LIKE '%F%'
                AND coursetitle IN ('bogus')
        THEN
            59.00
        ELSE 71.00
    END AS 'fakepercent'
FROM
    students
WHERE
    coursetitle NOT LIKE '%AcademicMentor%'
        AND coursetitle NOT LIKE '%Supervention%'
        AND coursetitle NOT LIKE '% Cond%'
        AND coursetitle NOT LIKE '%Cond %'
        AND coursetitle NOT LIKE '%CT %'
        AND coursetitle NOT LIKE '%GCC %'
        AND coursetitle NOT LIKE '%LAVC %'
        AND coursetitle NOT LIKE '%TA %'
        AND coursetitle NOT LIKE '%TA/%'
        AND coursetitle NOT LIKE '%Tm %'
        AND coursetitle NOT LIKE '% Fund%'
        AND coursetitle NOT LIKE '% Fun%'
        AND coursetitle NOT LIKE '% Basic%'
        AND coursetitle NOT LIKE '% Bas%'
        AND period < 7
        AND mark IS NOT NULL
GROUP BY studentno , coursetitle
HAVING fakepercent < 70
ORDER BY studentno ASC , fakepercent DESC