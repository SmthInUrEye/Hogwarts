
1. ALTER TABLE student
    ADD CONSTRAINT age_constraing CHECK (age>16)

2. ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT name UNIQUE (name)

3. ALTER TABLE faculty
    ADD CONSTRAINT name_color_unique UNIQUE(name, color)

4. ALTER TABLE student
   ALTER COLUMN age SET DEFAULT 20;