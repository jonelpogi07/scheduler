CREATE TABLE public.task (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status INT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_date TIMESTAMP NULL,
    end_date TIMESTAMP NULL,
    dependencies VARCHAR(255) NULL,
    duration VARCHAR(255)
);