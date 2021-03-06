SELECT DISTINCT
    room, coursetitle
FROM
    students
WHERE
    coursetitle NOT LIKE '%AcademicMentor%'
        AND coursetitle NOT LIKE '&Supervention%'
        AND coursetitle NOT LIKE '&Cond%'
        AND coursetitle NOT LIKE '&CT%'
        AND coursetitle NOT LIKE '&GCC%'
        AND coursetitle NOT LIKE '&LAVC%'
        AND coursetitle NOT LIKE '&TA%'
        AND coursetitle NOT LIKE '&TA%'
        AND coursetitle NOT LIKE '&Tm%'
        AND period < 7
        AND room NOT LIKE '%2329%'
        AND room NOT LIKE '%2104%'
        AND room NOT LIKE '%2115%'
        AND room NOT LIKE '%2204%'
        AND room NOT LIKE '%8105%'
        AND room NOT LIKE '%4118%'
        AND room NOT LIKE '%2225%'
        AND room NOT LIKE '%2107%'
        AND room NOT LIKE '%2121%'
        AND room NOT LIKE '%1205%'
        AND room NOT LIKE '%2317%'
        AND room NOT LIKE '%2108%'
        AND room NOT LIKE '%Office%'
        AND room NOT LIKE ''
        AND room NOT LIKE '%7003%'
        AND room NOT LIKE '%2217%'
        AND room NOT LIKE '%2113%'
        AND room NOT LIKE '%2202%'
        AND room NOT LIKE '%9104%'
        AND room NOT LIKE '%9102%'
        AND room NOT LIKE '%1153%'
        AND room NOT LIKE '%2309%'
        AND room NOT LIKE '%1211%'
        AND room NOT LIKE '%1154%'
        AND room NOT LIKE '%1235%'
        AND room NOT LIKE '%7114%'
        AND room NOT LIKE '%2307%'
        AND room NOT LIKE '%1150%'
        AND room NOT LIKE '%2313%'
        AND room NOT LIKE '%8106%'
        AND room NOT LIKE '%2302%'
        AND room NOT LIKE '%6101%'
        AND room NOT LIKE '%2315%'
        AND room NOT LIKE '%1201%'
        AND room NOT LIKE '%1216%'
        AND room NOT LIKE '%6203%'
        AND room NOT LIKE '%1213%'
        AND room NOT LIKE '%2206%'
        AND room NOT LIKE '%1224%'
        AND room NOT LIKE '%1220%'
        AND room NOT LIKE '%1229%'
        AND room NOT LIKE '%1217%'
        AND room NOT LIKE '%6107%'
        AND room NOT LIKE '%2228%'
        AND room NOT LIKE '%1149"%'
ORDER BY room ASC , coursetitle ASC