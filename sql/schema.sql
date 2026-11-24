CREATE TABLE learn_post (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE learn_course (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE learn_course_label (
    course_id VARCHAR(36) NOT NULL,
    label VARCHAR(50) NOT NULL,
    PRIMARY KEY (course_id, label),
    FOREIGN KEY (course_id) REFERENCES learn_course (id)
);

INSERT INTO learn_post (name) VALUES
    ('Java Developer'), ('QA Engineer'), ('Java Lead'), ('Product Manager');
