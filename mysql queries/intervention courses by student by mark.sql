			SELECT
				studentno,
				firstname,
				middlename,
				lastname,
				coursetitle,
				room,
				period,
				mark,  -- if mark is to be used. 
				-- percent,
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
					END AS 'fakepercent'
					-- percent,
					-- CASE
					--    WHEN
					--        coursetitle IN ('Algebra 1' , 'Geometry', 'Algebra 2')
					--    THEN
					--        TRUE
					--	ELSE
					-- 			FALSE
					-- 	END AS 'ispriority'
				 FROM
				    students
				WHERE
				    coursetitle NOT LIKE ('%Cond %')				
				        AND period < 7  -- may depend on customer schedule
				HAVING fakepercent < 70  -- if mark is used
				-- HAVING percent < 70
						AND grade < 12	-- if first semester of the year
					
				 GROUP BY studentno, coursetitle
				
				if(useMarkNotPercent)
					 HAVING fakepercent < 70
				else
					 HAVING percent < 70
					
				 ORDER BY studentno ASC 
