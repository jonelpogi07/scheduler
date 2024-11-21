# scheduler
Task planner for projects activity

please use postgresql
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=123456

endpoints

1. http://localhost:8080/task/addTask
   {
   "name": "TASK-0001",
   "dependencies": "2"
   }
2. http://localhost:8080/task/updateTaskStatus
   {
   "id": 1,
   "status": 2
   }
3. http://localhost:8080/task/showTasks
4. http://localhost:8080/task/batchAddTask
   [
   {
   "name": "TASK-0003",
   "dependencies": "1,2",
   "duration": 1
   },
   {
   "name": "TASK-0004",
   "dependencies": "1,2",
   "duration": 2
   }
   ]
5. http://localhost:8080/task/findTask
   {
   "name": "TASK-0001",
   "status": 1
   }