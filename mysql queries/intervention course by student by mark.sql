SELECT 
    s1.studentno AS 'Student ID',
    s1.coursetitle AS 'Intervention Course Title'
FROM
    (SELECT 
        studentno,
            coursetitle,
            mark,
            percent,
            period,
            CASE
                WHEN
                    mark LIKE 'D+'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    68.00
                WHEN
                    mark LIKE 'D+'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    69.00
                WHEN
                    mark LIKE 'D'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    64.00
                WHEN
                    mark LIKE 'D'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    65.00
                WHEN
                    mark LIKE 'D-'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    61.00
                WHEN
                    mark LIKE 'D-'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    62.00
                WHEN
                    mark LIKE 'F'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    58.00
                WHEN
                    mark LIKE 'F'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    59.00
                ELSE 71.00
            END AS FakePercent
    FROM
        students
    WHERE
        CASE
            WHEN
                mark LIKE 'D+'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                68.00
            WHEN
                mark LIKE 'D+'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                69.00
            WHEN
                mark LIKE 'D'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                64.00
            WHEN
                mark LIKE 'D'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                65.00
            WHEN
                mark LIKE 'D-'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                61.00
            WHEN
                mark LIKE 'D-'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                62.00
            WHEN
                mark LIKE 'F'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                58.00
            WHEN
                mark LIKE 'F'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                59.00
            ELSE 71.00
        END < 70
            AND coursetitle NOT LIKE '%AcademicMentor%'
            AND coursetitle NOT LIKE '%Supervention%'
            AND coursetitle NOT LIKE '%Cond %'
            AND coursetitle NOT LIKE '%CT %'
            AND coursetitle NOT LIKE '%GCC %'
            AND coursetitle NOT LIKE '%LAVC %'
            AND coursetitle NOT LIKE '%TA %'
            AND coursetitle NOT LIKE '%TA/%'
            AND coursetitle NOT LIKE '%Tm %'
            AND period < 7
            AND grade < 12) AS s1
        LEFT JOIN
    (SELECT 
        studentno,
            coursetitle,
            mark,
            percent,
            period,
            CASE
                WHEN
                    mark LIKE 'D+'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    68.00
                WHEN
                    mark LIKE 'D+'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    69.00
                WHEN
                    mark LIKE 'D'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    64.00
                WHEN
                    mark LIKE 'D'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    65.00
                WHEN
                    mark LIKE 'D-'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    61.00
                WHEN
                    mark LIKE 'D-'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    62.00
                WHEN
                    mark LIKE 'F'
                        AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    58.00
                WHEN
                    mark LIKE 'F'
                        AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                THEN
                    59.00
                ELSE 71.00
            END AS FakePercent
    FROM
        students
    WHERE
        CASE
            WHEN
                mark LIKE 'D+'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                68.00
            WHEN
                mark LIKE 'D+'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                69.00
            WHEN
                mark LIKE 'D'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                64.00
            WHEN
                mark LIKE 'D'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                65.00
            WHEN
                mark LIKE 'D-'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                61.00
            WHEN
                mark LIKE 'D-'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                62.00
            WHEN
                mark LIKE 'F'
                    AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                58.00
            WHEN
                mark LIKE 'F'
                    AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
            THEN
                59.00
            ELSE 71.00
        END < 70
            AND coursetitle NOT LIKE '%AcademicMentor%'
            AND coursetitle NOT LIKE '%Supervention%'
            AND coursetitle NOT LIKE '%Cond %'
            AND coursetitle NOT LIKE '%CT %'
            AND coursetitle NOT LIKE '%GCC %'
            AND coursetitle NOT LIKE '%LAVC %'
            AND coursetitle NOT LIKE '%TA %'
            AND coursetitle NOT LIKE '%TA/%'
            AND coursetitle NOT LIKE '%Tm %'
            AND period < 7
            AND grade < 12) AS s2 ON s2.studentno = s1.studentno
        AND s2.FakePercent > s1.FakePercent
WHERE
    s2.coursetitle IS NULL
        AND s1.coursetitle NOT IN (SELECT 
            s3.coursetitle
        FROM
            (SELECT 
                studentno,
                    coursetitle,
                    mark,
                    percent,
                    period,
                    CASE
                        WHEN
                            mark LIKE 'D+'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            68.00
                        WHEN
                            mark LIKE 'D+'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            69.00
                        WHEN
                            mark LIKE 'D'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            64.00
                        WHEN
                            mark LIKE 'D'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            65.00
                        WHEN
                            mark LIKE 'D-'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            61.00
                        WHEN
                            mark LIKE 'D-'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            62.00
                        WHEN
                            mark LIKE 'F'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            58.00
                        WHEN
                            mark LIKE 'F'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            59.00
                        ELSE 71.00
                    END AS FakePercent
            FROM
                students
            WHERE
                CASE
                    WHEN
                        mark LIKE 'D+'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        68.00
                    WHEN
                        mark LIKE 'D+'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        69.00
                    WHEN
                        mark LIKE 'D'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        64.00
                    WHEN
                        mark LIKE 'D'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        65.00
                    WHEN
                        mark LIKE 'D-'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        61.00
                    WHEN
                        mark LIKE 'D-'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        62.00
                    WHEN
                        mark LIKE 'F'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        58.00
                    WHEN
                        mark LIKE 'F'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        59.00
                    ELSE 71.00
                END < 70
                    AND coursetitle NOT LIKE '%AcademicMentor%'
                    AND coursetitle NOT LIKE '%Supervention%'
                    AND coursetitle NOT LIKE '%Cond %'
                    AND coursetitle NOT LIKE '%CT %'
                    AND coursetitle NOT LIKE '%GCC %'
                    AND coursetitle NOT LIKE '%LAVC %'
                    AND coursetitle NOT LIKE '%TA %'
                    AND coursetitle NOT LIKE '%TA/%'
                    AND coursetitle NOT LIKE '%Tm %'
                    AND period < 7
                    AND grade < 12) AS s3
                LEFT JOIN
            (SELECT 
                studentno,
                    coursetitle,
                    mark,
                    percent,
                    period,
                    CASE
                        WHEN
                            mark LIKE 'D+'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            68.00
                        WHEN
                            mark LIKE 'D+'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            69.00
                        WHEN
                            mark LIKE 'D'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            64.00
                        WHEN
                            mark LIKE 'D'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            65.00
                        WHEN
                            mark LIKE 'D-'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            61.00
                        WHEN
                            mark LIKE 'D-'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            62.00
                        WHEN
                            mark LIKE 'F'
                                AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            58.00
                        WHEN
                            mark LIKE 'F'
                                AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                        THEN
                            59.00
                        ELSE 71.00
                    END AS FakePercent
            FROM
                students
            WHERE
                CASE
                    WHEN
                        mark LIKE 'D+'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        68.00
                    WHEN
                        mark LIKE 'D+'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        69.00
                    WHEN
                        mark LIKE 'D'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        64.00
                    WHEN
                        mark LIKE 'D'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        65.00
                    WHEN
                        mark LIKE 'D-'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        61.00
                    WHEN
                        mark LIKE 'D-'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        62.00
                    WHEN
                        mark LIKE 'F'
                            AND coursetitle NOT IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        58.00
                    WHEN
                        mark LIKE 'F'
                            AND coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
                    THEN
                        59.00
                    ELSE 71.00
                END < 70
                    AND coursetitle NOT LIKE '%AcademicMentor%'
                    AND coursetitle NOT LIKE '%Supervention%'
                    AND coursetitle NOT LIKE '%Cond %'
                    AND coursetitle NOT LIKE '%CT %'
                    AND coursetitle NOT LIKE '%GCC %'
                    AND coursetitle NOT LIKE '%LAVC %'
                    AND coursetitle NOT LIKE '%TA %'
                    AND coursetitle NOT LIKE '%TA/%'
                    AND coursetitle NOT LIKE '%Tm %'
                    AND period < 7
                    AND grade < 12) AS s4 ON s4.studentno = s3.studentno
                AND s4.FakePercent > s3.FakePercent
        WHERE
            s4.coursetitle IS NULL
        GROUP BY s3.coursetitle
        HAVING COUNT(s3.studentno) < 4)
-- GROUP BY s1.studentno
ORDER BY s1.studentno ASC