CREATE TABLE Student_Is_In (
	perm_number CHAR(7),
    sname CHAR(20),
    address CHAR(20),
    PIN CHAR(4),
	mname CHAR(20),
	dname CHAR(20),
	PRIMARY KEY (perm_number),
	FOREIGN KEY (mname) REFERENCES Major,
	FOREIGN KEY (dname) REFERENCES Department
);
CREATE TABLE Courses (
	course_number CHAR(7),
	enrollment_code INTEGER,
	title CHAR(20),
	pname CHAR(15),
	location CHAR(9),
	time_slot CHAR(20),
	max_enrollment INTEGER,
	PRIMARY KEY (course_number),
	UNIQUE (enrollment_code)
);
CREATE TABLE Quarter_Year (
	quarter CHAR(6),
	year INTEGER,
	PRIMARY KEY (quarter)
);
CREATE TABLE Department (
	dname CHAR(20),
	PRIMARY KEY(dname)
);
CREATE TABLE Major (
	mname CHAR(20),
	elective_number INTEGER,
	PRIMARY KEY (mname)
);
CREATE TABLE Current_Courses (
	perm_number CHAR(7),
	course_number CHAR(7),
	PRIMARY KEY (perm_number, course_number),
	FOREIGN KEY (perm_number) REFERENCES Student_Is_In,
	FOREIGN KEY (course_number) REFERENCES Courses	
);
CREATE TABLE Past_Courses (
	perm_number CHAR(7),
	course_number CHAR(7),
	grade CHAR(2),
	PRIMARY KEY (perm_number, course_number),
	FOREIGN KEY (perm_number) REFERENCES Student_Is_In,
	FOREIGN KEY (course_number) REFERENCES Courses	
);
CREATE TABLE Prerequisites (
	course_number CHAR(7),
	prereq_course CHAR(7),
	PRIMARY KEY (course_number, prereq_course),
	FOREIGN KEY (course_number) References Courses,
	FOREIGN KEY (prereq_course) References Courses(course_number)
);
CREATE TABLE Offered (
	course_number CHAR(7),
	quarter CHAR(6),
	PRIMARY KEY (course_number, quarter),
	FOREIGN KEY (course_number) REFERENCES Courses,
	FOREIGN KEY (quarter) REFERENCES Quarter_Year
);
CREATE TABLE Mandatory_Courses (
	mname CHAR(20),
	course_number CHAR(7),
	PRIMARY KEY (mname, course_number),
	FOREIGN KEY (mname) REFERENCES Major,
	FOREIGN KEY (course_number) REFERENCES Courses
);
CREATE TABLE Major_Electives (
	mname CHAR(20),
	course_number CHAR(7),
	PRIMARY KEY (mname, course_number),
	FOREIGN KEY (mname) REFERENCES Major,
	FOREIGN KEY (course_number) REFERENCES Courses
);
CREATE TABLE Manages (
	dname CHAR(20),
	mname CHAR(20),
	PRIMARY KEY (dname, mname),
	FOREIGN KEY (dname) REFERENCES Department,
	FOREIGN KEY (mname) REFERENCES Major
);
