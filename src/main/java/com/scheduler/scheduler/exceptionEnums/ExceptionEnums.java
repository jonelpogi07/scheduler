package com.scheduler.scheduler.exceptionEnums;

public class ExceptionEnums {
    public enum ErrorCode {
        TASK_NAME_EXIST("Task name already exist"),
        TASK_CANNOT_FIND("Task not found");

        private final String message;

        ErrorCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
