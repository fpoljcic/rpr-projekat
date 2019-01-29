--BEGIN

--FOR c IN (SELECT table_name FROM user_tables) LOOP
--EXECUTE IMMEDIATE ('DROP TABLE "' || c.table_name || '" CASCADE CONSTRAINTS');
--END LOOP;

--END;

CREATE TABLE login (
    id                INTEGER PRIMARY KEY,
    username          VARCHAR(50) NOT NULL UNIQUE,
    password          VARCHAR(100) NOT NULL,
    date_created      DATE,
    user_type         VARCHAR(30),
    last_login_date   DATE
);

CREATE TABLE person (
    id           INTEGER PRIMARY KEY,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(55) NOT NULL,
    jmbg         VARCHAR(30) NOT NULL UNIQUE,
    address      VARCHAR(60),
    email        VARCHAR(100),
    login_id     INTEGER
        REFERENCES login ( id )
);

CREATE TABLE semester (
    id         INTEGER PRIMARY KEY,
    no         INTEGER NOT NULL,
    cycle_no   INTEGER NOT NULL,
    ects       INTEGER NOT NULL
);

CREATE TABLE course (
    id     INTEGER PRIMARY KEY,
    name   VARCHAR(50) NOT NULL
);

CREATE TABLE student (
    id            INTEGER PRIMARY KEY
        REFERENCES person ( id )
            ON DELETE CASCADE,
    birth_date    DATE NOT NULL,
    semester_id   INTEGER
        REFERENCES semester ( id ),
    course_id     INTEGER
        REFERENCES course ( id ),
    pause_date    DATE
);

CREATE TABLE professor (
    id      INTEGER PRIMARY KEY
        REFERENCES person ( id )
            ON DELETE CASCADE,
    title   VARCHAR(60) NOT NULL
);

CREATE TABLE administrator (
    id   INTEGER PRIMARY KEY
        REFERENCES person ( id )
);

CREATE TABLE subject (
    id               INTEGER PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    code             VARCHAR(20) NOT NULL,
    ects             INTEGER NOT NULL,
    professor_id     INTEGER
        REFERENCES professor ( id ),
    req_subject_id   INTEGER
        REFERENCES subject ( id )
);

CREATE TABLE grade (
    id             INTEGER PRIMARY KEY,
    student_id     INTEGER
        REFERENCES student ( id ),
    subject_id     INTEGER
        REFERENCES subject ( id ),
    points         FLOAT NOT NULL,
    score          INTEGER,
    grade_date     DATE,
    professor_id   INTEGER
        REFERENCES professor ( id )
);

CREATE TABLE curriculum (
    id                 INTEGER PRIMARY KEY,
    course_id          INTEGER
        REFERENCES course ( id ),
    semester_id        INTEGER
        REFERENCES semester ( id ),
    subject_id         INTEGER
        REFERENCES subject ( id ),
    required_subject   CHAR(2) NOT NULL
);

CREATE SEQUENCE login_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE subject_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE person_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE grade_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE course_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE semester_seq
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE curriculum_seq
START WITH 1
INCREMENT BY 1;

INSERT INTO login VALUES(login_seq.nextval, 'a', 'a', SYSDATE, 'Administrator', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'fpoljcic1', 'test', SYSDATE, 'Administrator', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'fsisic1', 'test', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'vljubovic2', 'test', SYSDATE, 'Profesor', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'zjuric', 'test', SYSDATE, 'Profesor', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'bpoljcic', 'test', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'apoljcic', 'test', SYSDATE, 'Student', NULL);

INSERT INTO person VALUES(person_seq.nextval, 'Test', 'Test', '2107998170067', 'Adresa', 'test@mail.ba', 1);
INSERT INTO person VALUES(person_seq.nextval, 'Faris', 'Poljcic', '2207995170057', 'Tome Medje 3', 'fpoljcic1@etf.unsa.ba', 2);
INSERT INTO person VALUES(person_seq.nextval, 'Faris', 'Sisic', '1007988170047', 'Grbavica 12', 'fsisic1@etf.unsa.ba', 3);
INSERT INTO person VALUES(person_seq.nextval, 'Vedran', 'Ljubovic', '1007008170052', 'Adresa 23', 'vljubovic2@etf.unsa.ba', 4);
INSERT INTO person VALUES(person_seq.nextval, 'Zeljko', 'Juric', '1007002170658', 'Adresa 14', 'zjuric@etf.unsa.ba', 5);
INSERT INTO person VALUES(person_seq.nextval, 'Bakir', 'Poljcic', '1012988173057', 'Tome Medje 20', 'bpoljcic@etf.unsa.ba', 6);
INSERT INTO person VALUES(person_seq.nextval, 'Adnan', 'Poljcic', '1407988170557', 'Tome Medje 33', 'apoljcic@etf.unsa.ba', 7);

INSERT INTO semester VALUES(semester_seq.nextval, 1, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 2, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 3, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 4, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 5, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 6, 1, 30);
INSERT INTO semester VALUES(semester_seq.nextval, 1, 2, 35);
INSERT INTO semester VALUES(semester_seq.nextval, 2, 2, 35);

INSERT INTO course VALUES(course_seq.nextval, 'Racunarstvo i informatika');
INSERT INTO course VALUES(course_seq.nextval, 'Automatika i elektronika');
INSERT INTO course VALUES(course_seq.nextval, 'Telekomunikacije');

INSERT INTO administrator VALUES(1);
INSERT INTO administrator VALUES(2);
INSERT INTO student VALUES(3, TO_DATE('01-07-1998', 'dd-mm-yyyy'), 1, 1, NULL);   
INSERT INTO professor VALUES(4, 'Vanredni prof');
INSERT INTO professor VALUES(5, 'Redovni prof');
INSERT INTO student VALUES(6, TO_DATE('21-01-1997', 'dd-mm-yyyy'), 2, 1, NULL); 
INSERT INTO student VALUES(7, TO_DATE('10-01-1999', 'dd-mm-yyyy'), 1, 1, SYSDATE);  

INSERT INTO subject VALUES(subject_seq.nextval, 'Razvoj programskih rjesenja', 'RPR', 10, 4, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Diskretna matematika', 'DM', 8, 5, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Inzenjerska matematika 1', 'IM1', 8, 4, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Operativni sistemi', 'OS', 7, 5, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Osnove elektrotehnike', 'OE', 9, 5, 1);

INSERT INTO grade VALUES(grade_seq.nextval, 3, 1, 83.34, 9, SYSDATE, 4);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 2, 92.3, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 5, 35, 9, SYSDATE, 5);
INSERT INTO grade VALUES(grade_seq.nextval, 6, 1, 52.5, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 6, 4, 60, 6, SYSDATE, 5);
INSERT INTO grade VALUES(grade_seq.nextval, 7, 1, 10, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 7, 5, 99, 10, SYSDATE, 5);
INSERT INTO grade VALUES(grade_seq.nextval, 7, 3, 63.1, NULL, NULL, NULL);

INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 1, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 2, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 3, 'Ne');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 4, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 5, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 3, 'Ne');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 2, 7, 5, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 2, 7, 4, 'Ne');

COMMIT;