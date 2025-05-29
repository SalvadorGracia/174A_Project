DROP TABLE Course_Catalog CASCADE CONSTRAINT;
DROP TABLE Course_Offering CASCADE CONSTRAINT;
DROP TABLE Department CASCADE CONSTRAINT;
DROP TABLE Enrollment CASCADE CONSTRAINT;
DROP TABLE Major CASCADE CONSTRAINT;
DROP TABLE Major_Requirement CASCADE CONSTRAINT;
DROP TABLE Manages CASCADE CONSTRAINT;
DROP TABLE Prerequisite CASCADE CONSTRAINT;
DROP TABLE Student_Is_In CASCADE CONSTRAINT;

CREATE TABLE Course_Catalog (
	course_code CHAR(7) PRIMARY KEY
);
CREATE TABLE Department (
	d_name CHAR(50) PRIMARY KEY
);
CREATE TABLE Major (
	m_name CHAR(50) PRIMARY KEY,
	elective_number INTEGER
);
CREATE TABLE Student_Is_In (
	perm_number CHAR(5) PRIMARY KEY,
    s_name CHAR(50),
	address CHAR(50),
	d_name CHAR(50),
    m_name CHAR(50),
    pin CHAR(5),
	FOREIGN KEY (d_name) REFERENCES Department,
    FOREIGN KEY (m_name) REFERENCES Major
);
CREATE TABLE Course_Offering (
    course_code CHAR(7),
	enrollment_code INTEGER,
	year_quarter CHAR(10),
	p_name CHAR(20),
    max_enrollment INTEGER,
    time_location CHAR(25),
	title CHAR(50),
    PRIMARY KEY (enrollment_code, year_quarter),
	FOREIGN KEY (course_code) REFERENCES Course_Catalog
);
CREATE TABLE Enrollment (
	perm_number CHAR(7),
	enrollment_code INTEGER,
    year_quarter CHAR(10),
    grade CHAR(2),
	PRIMARY KEY (perm_number, enrollment_code, year_quarter),
	FOREIGN KEY (perm_number) REFERENCES Student_Is_In,
	FOREIGN KEY (enrollment_code, year_quarter) REFERENCES Course_Offering	
);
CREATE TABLE Prerequisite (
	course_code CHAR(7),
	prereq_course CHAR(7),
	PRIMARY KEY (course_code, prereq_course),
	FOREIGN KEY (course_code) References Course_Catalog,
	FOREIGN KEY (prereq_course) References Course_Catalog(course_code)
);
CREATE TABLE Major_Requirement (
	m_name CHAR(50),
	course_code CHAR(7),
    requirement_type CHAR(10),
	PRIMARY KEY (m_name, course_code),
	FOREIGN KEY (m_name) REFERENCES Major,
	FOREIGN KEY (course_code) REFERENCES Course_Catalog
);
CREATE TABLE Manages (
	d_name CHAR(50),
	m_name CHAR(50),
	PRIMARY KEY (d_name, m_name),
	FOREIGN KEY (d_name) REFERENCES Department,
	FOREIGN KEY (m_name) REFERENCES Major
);

-- Insert Departments
INSERT INTO department (d_name) VALUES ('CS');
INSERT INTO department (d_name) VALUES ('ECE');

SELECT *
FROM department;

-- Insert Students
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('12345', 'Alfred Hitchcock', '6667 El Colegio #40', 'CS', '12345');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('14682', 'Billy Clinton', '5777 Hollister', 'ECE', '14682');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('37642', 'Cindy Laugher', '7000 Hollister', 'CS', '37642');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('85821', 'David Copperfill', '1357 State St', 'CS', '85821');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('38567', 'Elizabeth Sailor', '4321 State St', 'ECE', '38567');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('81934', 'Fatal Castro', '3756 La Cumbre Plaza', 'CS', '81934');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('98246', 'George Brush', '5346 Foothill Av', 'CS', '98246');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('35328', 'Hurryson Ford', '678 State St', 'ECE', '35328');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('84713', 'Ivan Lendme', '1235 Johnson Dr', 'ECE', '84713');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('36912', 'Joe Pepsi', '3210 State St', 'CS', '36912');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('46590', 'Kelvin Coster', 'Santa Cruz #3579', 'CS', '46590');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('91734', 'Li Kung', '2 People''s Rd Beijing', 'ECE', '91734');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('73521', 'Magic Jordon', '3852 Court Rd', 'CS', '73521');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('53540', 'Nam-hoi Chung', '1997 People''s St HK', 'CS', '53540');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('82452', 'Olive Stoner', '6689 El Colegio #151', 'ECE', '82452');
INSERT INTO student_is_in (perm_number, s_name, address, d_name, pin) VALUES ('18221', 'Pit Wilson', '911 State St', 'ECE', '18221');

SELECT *
FROM student_is_in;

-- Insert Courses Into Catalog
INSERT INTO course_catalog (course_code) VALUES ('CS174');
INSERT INTO course_catalog (course_code) VALUES ('CS170');
INSERT INTO course_catalog (course_code) VALUES ('CS160');
INSERT INTO course_catalog (course_code) VALUES ('CS154');
INSERT INTO course_catalog (course_code) VALUES ('CS130');
INSERT INTO course_catalog (course_code) VALUES ('CS026');
INSERT INTO course_catalog (course_code) VALUES ('CS010');
INSERT INTO course_catalog (course_code) VALUES ('EC154');
INSERT INTO course_catalog (course_code) VALUES ('EC152');
INSERT INTO course_catalog (course_code) VALUES ('EC140');
INSERT INTO course_catalog (course_code) VALUES ('EC015');
INSERT INTO course_catalog (course_code) VALUES ('EC010');

SELECT *
FROM course_catalog;

-- Insert Prerequisites
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('CS174', 'CS130');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('CS174', 'CS026');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('CS170', 'CS130');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('CS170', 'CS154');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('CS160', 'CS026');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('EC154', 'CS026');
INSERT INTO prerequisite (course_code, prereq_course) VALUES ('EC154', 'EC152');

SELECT *
FROM prerequisite;

-- 25 Spring (25 S) Offerings
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS174', '12345', '25 S', 'Venus', 5, 'TR10-12 Psycho 1132');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS170', '54321', '25 S', 'Jupiter', 8, 'MWF10-11 English 1124');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS160', '41725', '25 S', 'Mercury', 5, 'MWF2-3 Engr 1132');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS026', '76543', '25 S', 'Mars', 5, 'MWF2-3 Bio 2222');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC154', '93156', '25 S', 'Saturn', 7, 'T3-5 Maths 3333');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC140', '19023', '25 S', 'Gold', 10, 'TR1-3 Chem 1234');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC015', '71631', '25 S', 'Silver', 5, 'MW11-1 Engr 2116');

-- 25 Winter (25 W) Offerings
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS170', '54321', '25 W', 'Copper', 18, 'MWF10-11 English 1124');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS160', '41725', '25 W', 'Iron', 15, 'MWF2-3 Engr 1132');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS154', '32165', '25 W', 'Tin', 10, 'MF8-9 Engr 2116');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS130', '56789', '25 W', 'Star', 15, 'TR2-4 Chem 1111');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS026', '76543', '25 W', 'Tin', 15, 'MWF2-3 Bio 2222');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC154', '93156', '25 W', 'Saturn', 18, 'T3-5 Maths 3333');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC152', '91823', '25 W', 'Gold', 10, 'MW11-1 Engr 3163');

-- 24 Fall (24 F) Offerings
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS170', '54321', '24 F', 'Copper', 15, 'MWF10-11 English 1124');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS160', '41725', '24 F', 'Mercury', 10, 'MWF2-3 Engr 1132');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS154', '32165', '24 F', 'Mars', 10, 'MWF8-9 Engr 2116');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS130', '56789', '24 F', 'Jupiter', 15, 'TR2-4 Chem 1111');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS026', '76543', '24 F', 'Tin', 15, 'MWF2-3 Bio 2222');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS010', '81623', '24 F', 'Gold', 10, 'MWR3-4 Chem 3333');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC154', '93156', '24 F', 'Silver', 10, 'T3-5 Maths 3333');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC152', '91823', '24 F', 'Sun', 10, 'MW11-1 Engr 3163');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC015', '71631', '24 F', 'Moon', 15, 'TR2-4 Engr 1124');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC010', '82612', '24 F', 'Earth', 15, 'MWF8-9 Physics 4004');

-- 24 Spring (24 S) Offerings
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS130', '56789', '24 S', 'Mercury', 15, 'TR2-4 Chem 1111');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS026', '76543', '24 S', 'Mars', 15, 'MWF2-3 Bio 2222');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('CS010', '81623', '24 S', 'Gold', 10, 'MWR3-4 Chem 3333');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC152', '91823', '24 S', 'Iron', 12, 'MW11-1 Engr 3163');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC015', '71631', '24 S', 'Moon', 15, 'TR2-4 Engr 1124');
INSERT INTO course_offering (course_code, enrollment_code, year_quarter, p_name, max_enrollment, time_location) VALUES ('EC010', '82612', '24 S', 'Star', 15, 'MWF8-9 Physics 4004');

SELECT *
FROM course_offering;
