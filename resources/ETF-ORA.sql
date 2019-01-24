CREATE TABLE login (
    id               INTEGER PRIMARY KEY,
    username           VARCHAR(50) NOT NULL UNIQUE,
    password              VARCHAR(100) NOT NULL,
    date_created                 DATE,
    user_type                 VARCHAR(30)
);

CREATE TABLE person (
    id               INTEGER PRIMARY KEY,
    first_name       VARCHAR(50) NOT NULL,
    last_name              VARCHAR(50) NOT NULL,
    jmbg                 VARCHAR(30) NOT NULL UNIQUE,
    address                 VARCHAR(60),
    email                 VARCHAR(100),
    login_id        INTEGER REFERENCES login(id)
);

CREATE TABLE semester (
    id               INTEGER PRIMARY KEY,
    no      INTEGER NOT NULL,
    cycle_no          INTEGER NOT NULL,
    ects    INTEGER NOT NULL
);

CREATE TABLE course (
    id               INTEGER PRIMARY KEY,
    name       VARCHAR(50) NOT NULL
);

CREATE TABLE student (
    id               INTEGER PRIMARY KEY REFERENCES person(id),
    birth_date       DATE NOT NULL,
    semester_id     INTEGER REFERENCES semester(id),
    course_id     INTEGER REFERENCES course(id)
);

CREATE TABLE professor (
    id               INTEGER PRIMARY KEY REFERENCES person(id),
    title       VARCHAR(60) NOT NULL
);

CREATE TABLE administrator (
    id               INTEGER PRIMARY KEY REFERENCES person(id)
);

CREATE TABLE subject (
    id               INTEGER PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    code              VARCHAR(20) NOT NULL,
    ects                 INTEGER NOT NULL,
    professor_id      INTEGER REFERENCES professor(id)
);

CREATE TABLE grade (
    id               INTEGER PRIMARY KEY,
    student_id      INTEGER REFERENCES student(id),
    subject_id      INTEGER REFERENCES subject(id),
    points          FLOAT NOT NULL,
    score           INTEGER
);

CREATE TABLE curriculum (
    id               INTEGER PRIMARY KEY,
    course_id      INTEGER REFERENCES course(id),
    semester_id      INTEGER REFERENCES semester(id),
    main_subject_id      INTEGER REFERENCES subject(id),
    secondary_subject_id      INTEGER REFERENCES subject(id)
);

CREATE SEQUENCE subject_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE person_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE grade_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE course_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE semester_seq
INCREMENT BY 1
START WITH 1;

CREATE SEQUENCE curriculum_seq
INCREMENT BY 1
START WITH 1;

INSERT INTO login VALUES(1, 'test', 'test', SYSDATE, 'Administrator');
INSERT INTO login VALUES(2, 'test2', 'test', SYSDATE, 'Administrator');

INSERT INTO person VALUES(1, 'Faris', 'Poljcic', '2107998170067', 'Adresa 123', 'fpoljcic1@etf.unsa.ba', 1);
INSERT INTO person VALUES(2, 'Test', 'PlsWork', '2107998170057', 'Asssdresa 123', 'kkkkk@etf.unsa.ba', 2); 

INSERT INTO student VALUES(1, TO_DATE('21-07-1998', 'dd-mm-yyyy'), NULL, NULL);   
INSERT INTO professor VALUES(2, 'Vanredni prof');

INSERT INTO subject VALUES(1, 'Tjelesno', 'TJE', 5, 2);



SELECT ID FROM FP18120.LOGIN WHERE USERNAME='test' AND PASSWORD='test' AND USER_TYPE='Administrator';