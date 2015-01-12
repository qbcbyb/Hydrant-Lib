package cn.qbcbyb.library.http;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TaskBasic<Params, Progress, Result> extends AsyncTask<Params, Progress, TaskResult<Result>> {

    public static ExecutorService threadService;
    private static Object lock = new Object();

    public static ExecutorService getThreadService() {
        synchronized (lock) {
            if (threadService == null) {
                threadService = Executors.newCachedThreadPool();
            }
        }
        return threadService;
    }

    protected Dialog dialog;
    protected ProgressBar progressBar;

    public TaskBasic() {
        super();
    }

    public TaskBasic(Dialog dialog, ProgressBar progressBar) {
        super();
        this.dialog = dialog;
        this.progressBar = progressBar;
    }

    public TaskBasic(Dialog dialog) {
        super();
        this.dialog = dialog;
    }

    protected abstract void onDealException(Exception e);

    protected abstract void onPostResult(Result result);

    protected abstract Result doInBack(Params... params) throws Exception;

    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    protected TaskResult<Result> doInBackground(Params... params) {
        try {
            Result result = doInBack(params);
            return new TaskResult<Result>(true, result);
        } catch (Exception ex) {
            return new TaskResult<Result>(ex);
        }
    }

    @Override
    protected final void onPostExecute(TaskResult<Result> result) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        if (result.isSuccess()) {
            onPostResult(result.getData());
        } else {
            onDealException(result.getException());
        }
    }

    public final AsyncTask<Params, Progress, TaskResult<Result>> executeTask(Params... params) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return executeOnExecutor(getThreadService(), params);
        } else {
            return execute(params);
        }
    }

}