package cn.qbcbyb.library.http;

/**
 * Created by 秋云 on 2014/10/20.
 */
class TaskResult<T> {
    private boolean success = false;
    private T data;
    private Exception exception;

    public TaskResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public TaskResult(Exception exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }

}
