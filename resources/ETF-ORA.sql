BEGIN

FOR c IN (SELECT table_name FROM user_tables) LOOP
EXECUTE IMMEDIATE ('DROP TABLE "' || c.table_name || '" CASCADE CONSTRAINTS');
END LOOP;

END;
/

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
    name   VARCHAR(60) NOT NULL
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

DROP SEQUENCE login_seq;
CREATE SEQUENCE login_seq;

DROP SEQUENCE subject_seq;
CREATE SEQUENCE subject_seq;

DROP SEQUENCE person_seq;
CREATE SEQUENCE person_seq;

DROP SEQUENCE grade_seq;
CREATE SEQUENCE grade_seq;

DROP SEQUENCE course_seq;
CREATE SEQUENCE course_seq;

DROP SEQUENCE semester_seq;
CREATE SEQUENCE semester_seq;

DROP SEQUENCE curriculum_seq;
CREATE SEQUENCE curriculum_seq;

INSERT INTO login VALUES(login_seq.nextval, 'aaa', 'rr1jcb53jgb8oqne5iowy9a9g4j5kip9ujt1wt156j12gug0b1', SYSDATE, 'Administrator', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'fpoljcic', 'wb9r3ue9it5mspc9wyinetkgfj6ufdmo59mcu7rep8pgrbrph8', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'fsisic', 'wbgrpup99p77v6q2r9yh0tfoys0u2bk5w2fe4krbdhhexgmuhx', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'epita', 'vbtrxuu96049vkps10dvxe86tt0tmr3j64x2pfqliupmi1fcbj', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'fmusic', 'wbnr1ug9152yusdf504kmjwq46oebep20tv665qsul1ap6mshf', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'aturkusic', 'rberku49e8xmbaajp0y81t30i64jm67mva0cd5emftpi5cvshg', SYSDATE, 'Student', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'vljubovic', 'dbwr1ud9dxsws9ap4eyxvw0i1clp6qiqakd31o3p6o6d3beehu', SYSDATE, 'Profesor', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'zjuric', 'hbtr4uy9s08wxrwr7bjay60kbuolbaguyu3r7b8rue6rask1hf', SYSDATE, 'Profesor', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'sribic', 'ab6rvut91n134c4v5r3jq033k50cib5yrxqgud7tgit0ut4phr', SYSDATE, 'Profesor', NULL);
INSERT INTO login VALUES(login_seq.nextval, 'zsabanac', 'hb0rmu49ufll9k752nn19sluu1l4q3gwvugsr6yomgm58op8hu', SYSDATE, 'Profesor', NULL);

INSERT INTO person VALUES(person_seq.nextval, 'Admin', 'Test', '2107998170067', 'Adresa', 'test@mail.ba', 1);
INSERT INTO person VALUES(person_seq.nextval, 'Faris', 'Poljcic', '2207998170067', 'Tome Medje 3', 'fpoljcic@etf.unsa.ba', 2);
INSERT INTO person VALUES(person_seq.nextval, 'Faris', 'Sisic', '1009998170067', 'Grbavica 12', 'fsisic@etf.unsa.ba', 3);
INSERT INTO person VALUES(person_seq.nextval, 'Emir', 'Pita', '0102998170067', 'Bistrik 99', 'epita@etf.unsa.ba', 4);
INSERT INTO person VALUES(person_seq.nextval, 'Faris', 'Music', '2211998170067', 'Nedjarici 14', 'fmusic@etf.unsa.ba', 5);
INSERT INTO person VALUES(person_seq.nextval, 'Arlsan', 'Turkusic', '0302998170067', 'Dobrinja 64', 'aturkusic@etf.unsa.ba', 6);
INSERT INTO person VALUES(person_seq.nextval, 'Vedran', 'Ljubovic', '1107992170067', 'Adresa 42', 'vljubovic@etf.unsa.ba', 7);
INSERT INTO person VALUES(person_seq.nextval, 'Zeljko', 'Juric', '3003989170067', 'Adresa 37', 'zjuric@etf.unsa.ba', 8);
INSERT INTO person VALUES(person_seq.nextval, 'Samir', 'Ribic', '1210988170067', 'Adresa 74', 'sribic@etf.unsa.ba', 9);
INSERT INTO person VALUES(person_seq.nextval, 'Zenan', 'Sabanac', '1605990170067', 'Adresa 22', 'zsabanac@etf.unsa.ba', 10);

INSERT INTO semester VALUES(semester_seq.nextval, 1, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 2, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 3, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 4, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 5, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 6, 1, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 1, 2, 15);
INSERT INTO semester VALUES(semester_seq.nextval, 2, 2, 15);

INSERT INTO course VALUES(course_seq.nextval, 'Racunarstvo i informatika');
INSERT INTO course VALUES(course_seq.nextval, 'Automatika i elektronika');
INSERT INTO course VALUES(course_seq.nextval, 'Telekomunikacije');

INSERT INTO administrator VALUES(1);
INSERT INTO student VALUES(2, TO_DATE('22-07-1998', 'dd-mm-yyyy'), 1, 1, NULL);
INSERT INTO student VALUES(3, TO_DATE('10-09-1998', 'dd-mm-yyyy'), 1, 1, NULL);
INSERT INTO student VALUES(4, TO_DATE('01-02-1998', 'dd-mm-yyyy'), 1, 1, SYSDATE);
INSERT INTO student VALUES(5, TO_DATE('22-11-1998', 'dd-mm-yyyy'), 2, 1, NULL);
INSERT INTO student VALUES(6, TO_DATE('03-02-1998', 'dd-mm-yyyy'), 7, 2, NULL);
INSERT INTO professor VALUES(7, 'Doc. dr, dipl.ing.el.');
INSERT INTO professor VALUES(8, 'R. prof. dr, dipl.ing.el.');
INSERT INTO professor VALUES(9, 'V. prof. dr, dipl.ing.el.');
INSERT INTO professor VALUES(10, 'V. prof. dr, dipl.mat.');

INSERT INTO subject VALUES(subject_seq.nextval, 'Inzenjerska fizika 1', 'IF1', 10, 7, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Inzenjerska matematika 1', 'IM1', 8, 8, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Linearna algebra i geometrija', 'LAG', 8, 10, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Osnove elektrotehnike', 'OE', 7, 9, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Osnove racunarstva', 'OR', 9, 10, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Inzenjerska matematika 2', 'IM2', 6, 7, 1);
INSERT INTO subject VALUES(subject_seq.nextval, 'Matematicka logika i teorija izracunljivosti', 'MLTI', 8, 8, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Operativni sistemi', 'OS', 10, 8, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Tehnike programiranja', 'TP', 9, 9, 5);
INSERT INTO subject VALUES(subject_seq.nextval, 'Vjerovatnoca i statistika', 'VIS', 7, 10, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Algoritmi i strukture podataka', 'ASP', 10, 7, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Diskretna matematika', 'DM', 8, 8, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Logicki dizajn', 'LD', 8, 9, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Osnove baza podataka', 'OBP', 9, 7, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Razvoj programskih rjesenja', 'RPR', 7, 10, 9);
INSERT INTO subject VALUES(subject_seq.nextval, 'Sistemsko programiranje', 'SP', 6, 10, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Numericki algoritmi', 'NA', 6, 9, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Digitalna obrada signala', 'DOS', 10, 9, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Polja i prostiranje', 'PIP', 10, 8, NULL);
INSERT INTO subject VALUES(subject_seq.nextval, 'Mikroelektronicke komponente i modeliranje', 'MKIM', 10, 10, NULL);

INSERT INTO grade VALUES(grade_seq.nextval, 2, 1, 26, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 2, 2, 24.54, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 2, 3, 36, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 2, 4, 40.2, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 2, 5, 27, NULL, NULL, NULL);

INSERT INTO grade VALUES(grade_seq.nextval, 3, 1, 45, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 2, 78, 8, SYSDATE, 8);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 3, 54.2, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 4, 99.23, 10, SYSDATE, 9);
INSERT INTO grade VALUES(grade_seq.nextval, 3, 5, 55, 6, SYSDATE, 10);

INSERT INTO grade VALUES(grade_seq.nextval, 4, 1, 55.3, 6, SYSDATE, 7);
INSERT INTO grade VALUES(grade_seq.nextval, 4, 2, 60.2, 6, SYSDATE, 8);
INSERT INTO grade VALUES(grade_seq.nextval, 4, 3, 63, 6, SYSDATE, 10);
INSERT INTO grade VALUES(grade_seq.nextval, 4, 4, 69.2, 7, SYSDATE, 9);
INSERT INTO grade VALUES(grade_seq.nextval, 4, 5, 54.2, NULL, NULL, NULL);

INSERT INTO grade VALUES(grade_seq.nextval, 5, 1, 100, 10, SYSDATE, 7);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 2, 96.1, 10, SYSDATE, 8);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 3, 90.2, 9, SYSDATE, 10);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 4, 82.03, 8, SYSDATE, 9);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 5, 96.9, 10, SYSDATE, 10);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 6, 89.9, 9, SYSDATE, 7);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 7, 76, 8, SYSDATE, 8);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 8, 65, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 9, 41.2, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 5, 10, 83, NULL, NULL, NULL);

INSERT INTO grade VALUES(grade_seq.nextval, 6, 18, 55, NULL, NULL, NULL);
INSERT INTO grade VALUES(grade_seq.nextval, 6, 19, 68.9, 7, SYSDATE, 8);

INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 1, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 2, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 3, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 4, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 1, 5, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 6, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 7, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 8, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 9, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 2, 10, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 11, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 12, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 13, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 14, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 15, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 16, 'Ne');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 1, 3, 17, 'Ne');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 2, 7, 18, 'Da');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 2, 7, 19, 'Ne');
INSERT INTO curriculum VALUES(curriculum_seq.nextval, 2, 7, 20, 'Ne');

COMMIT;