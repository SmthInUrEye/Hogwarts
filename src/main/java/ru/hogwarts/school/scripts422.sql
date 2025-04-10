
1. "Создаём таблицу с людьми"
create table humans (
human_id serial primary key,
name varchar(100),
age smallint,
have_licence boolean
)

2. "Создаём таблицу с машинами"
create table cars (
car_id serial primary key,
label varchar(100),
model varchar(100),
price decimal(10,2)
)

3. "Создаём таблицу для обеспечения связи многие ко многим"
CREATE TABLE human_car (
human_id INTEGER REFERENCES humans(human_id) ON DELETE CASCADE,
car_id INTEGER REFERENCES cars(car_id) ON DELETE CASCADE,
PRIMARY KEY (human_id, car_id)
)

4. "Наполняем данными"
INSERT INTO humans (name) VALUES
    ('Иван'), ('Мария'), ('Алексей')
INSERT INTO cars (label,model) VALUES
    ('Toyota', 'Camry'),
    ('Tesla', 'Model 3')
INSERT INTO human_car (human_id, car_id) VALUES
    (1, 1)

5. "Тестовый запрос для проверки связи"
select h.name, h.age, c.label, c.model, c.price
from humans h
join human_car hc on h.human_id=hc.human_id
join cars c on hc.car_id = c.car_id
where c.model = 'Camry'