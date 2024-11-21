CREATE TABLE public.task (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status INT,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    start_date TIMESTAMP NULL,
    end_date TIMESTAMP NULL,
    dependencies VARCHAR(255) NULL,
    duration INT
);

-- Update a task by id
UPDATE public.task
SET
    name = :name,
    status = :status,
    start_date = :start_date,
    end_date = :end_date,
    dependencies = :dependencies,
    duration = :duration
WHERE
    id = :id;

DELETE FROM public.task
WHERE id = :id;

SELECT
    t.id,
    t.name,
    t.status,
    t.creation_date,
    t.start_date,
    t.end_date,
    t.dependencies,
    t.duration,
    td.dependency_name
FROM
    public.task t
LEFT JOIN
    public.task_dependencies td
ON
    t.id = td.task_id;