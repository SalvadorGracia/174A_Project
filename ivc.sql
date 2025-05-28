CREATE TABLE Student_Is_In (
	perm_number CHAR(7) PRIMARY KEY,
    	s_name CHAR(250),
    	address CHAR(250),
    	pin CHAR(4),
	m_name CHAR(250),
	d_name CHAR(250),
	FOREIGN KEY (m_name) REFERENCES Major,
	FOREIGN KEY (d_name) REFERENCES Department
);
CREATE TABLE Course_Offering (
	enrollment_code INTEGER PRIMARY KEY,
	course_number CHAR(7),
	location CHAR(250),
	title CHAR(250),
	max_enrollment INTEGER,
	p_name CHAR(250),
	quarter CHAR(250),
	year INTEGER,
	time_slot CHAR(250),
	FOREIGN KEY (course_number) REFERENCES Course_Catalog
);
CREATE TABLE Course_Catalog (
	course_number CHAR(7) PRIMARY KEY
);
CREATE TABLE Department (
	d_name CHAR(250) PRIMARY KEY
);
CREATE TABLE Major (
	m_name CHAR(250) PRIMARY KEY,
	elective_number INTEGER
);
CREATE TABLE Current_Courses (
	perm_number CHAR(7),
	enrollment_code INTEGER,
	PRIMARY KEY (perm_number, enrollment_code),
	FOREIGN KEY (perm_number) REFERENCES Student_Is_In,
	FOREIGN KEY (enrollment_code) REFERENCES Course_Offering	
);
CREATE TABLE Past_Courses (
	perm_number CHAR(7),
	enrollment_code CHAR(7),
	grade CHAR(2),
	PRIMARY KEY (perm_number, enrollment_code),
	FOREIGN KEY (perm_number) REFERENCES Student_Is_In,
	FOREIGN KEY (enrollment_code) REFERENCES Course_Offering	
);
CREATE TABLE Prerequisites (
	course_number CHAR(7),
	prereq_course CHAR(7),
	PRIMARY KEY (course_number, prereq_course),
	FOREIGN KEY (course_number) References Course_Catalog,
	FOREIGN KEY (prereq_course) References Course_Catalog(course_number)
);
CREATE TABLE Mandatory_Courses (
	m_name CHAR(250),
	course_number CHAR(7),
	PRIMARY KEY (m_name, course_number),
	FOREIGN KEY (m_name) REFERENCES Major,
	FOREIGN KEY (course_number) REFERENCES Course_Catalog
);
CREATE TABLE Elective_Courses (
	m_name CHAR(250),
	course_number CHAR(7),
	PRIMARY KEY (m_name, course_number),
	FOREIGN KEY (m_name) REFERENCES Major,
	FOREIGN KEY (course_number) REFERENCES Course_Catalog
);
CREATE TABLE Manages (
	d_name CHAR(250),
	m_name CHAR(250),
	PRIMARY KEY (d_name, m_name),
	FOREIGN KEY (d_name) REFERENCES Department,
	FOREIGN KEY (m_name) REFERENCES Major
);
