package com.scheduler.scheduler.enums;

public class Enums {
    public enum Code {
        NOT_STARTED(0),
        IN_PROGRESS(1),
        DONE(2);

        private final int status;

        Code(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
