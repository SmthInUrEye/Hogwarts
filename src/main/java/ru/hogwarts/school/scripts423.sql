1. "Получем список студентов с факультетами(студентов без факультета не показывает)"
select s.name, s.age, f.name
from student s
join faculty f on f.id=s.faculty_id

2. "Получаем студентов с аватарками (аналогично первому join не показывает студентов без аватара)"
select s.name, s.age, a.file_path
from student s
join avatar a on a.id=s.avatar_id
