select lastname, min(percent)
from students
group by lastname
having min(percent) >= 70
order by lastname, coursetitle