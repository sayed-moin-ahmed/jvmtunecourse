#H2 Database

SELECT SCHEMA();
SELECT CURRENT_USER();

CREATE TABLE employee (id int auto_increment , name text, age int, designation text, department text, country text, PRIMARY KEY(id));

SELECT * FROM employee;

INSERT INTO employee (name, age, designation, department, country) VALUES ('Anand', 25, 'Developer', 'IT', 'India'); 
INSERT INTO employee (name, age, designation, department, country) VALUES ('Prem', 28, 'Developer', 'IT', 'India'); 
INSERT INTO employee (name, age, designation, department, country) VALUES ('Jawahar', 30, 'Executive', 'Admin', 'India'); 
INSERT INTO employee (name, age, designation, department, country) VALUES ('Axar', 23, 'Associate', 'Finance', 'India');
UPDATE employee SET designation = 'Manager' WHERE id = 1;
DELETE FROM employee WHERE id = 1;

TRUNCATE employee;
DROP TABLE employee;