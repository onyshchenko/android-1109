package com.example.onyshchenkov.homework_lesson12;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SAVE_STUDENT = "com.example.onyshchenkov.homework_lesson12.action.SAVE_STUDENT";
    private static final String ACTION_GET_STUDENTS = "com.example.onyshchenkov.homework_lesson12.action.GET_STUDENTS";
    private static final String ACTION_DELETE_STUDENTS = "com.example.onyshchenkov.homework_lesson12.action.DELETE_STUDENT";
    private static final String ACTION_UPDATE_STUDENTS = "com.example.onyshchenkov.homework_lesson12.action.UPDATE_STUDENT";

    // TODO: Rename parameters
    private static final String EXTRA_PENDING_INTENT = "com.example.onyshchenkov.homework_lesson12.action.PENDING_INTENT";
    public static final String EXTRA_STUDENTS = "com.example.onyshchenkov.homework_lesson12.action.STUDENTS";
    public static final String EXTRA_STUDENT = "com.example.onyshchenkov.homework_lesson12.action.STUDENT";
    public static final String EXTRA_STUDENT_UUID = "com.example.onyshchenkov.homework_lesson12.action.STUDENT_UUID";

    //public static final String EXTRA_GROUP = "com.example.onyshchenkov.homework_lesson12.action.GROUP";

    //private static final String EXTRA_PARAM1 = "com.example.onyshchenkov.homework_lesson12.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.example.onyshchenkov.homework_lesson12.extra.PARAM2";

    public static final int REQUEST_CODE_SAVE_STUDENT = 10;
    public static final int REQUEST_CODE_GET_STUDENTS = 11;
    public static final int REQUEST_CODE_DELETE_STUDENT = 12;
    public static final int REQUEST_CODE_UPDATE_STUDENT = 13;

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void getStudents(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);

        PendingIntent pendingIntent = ((AppCompatActivity) context).createPendingResult(REQUEST_CODE_GET_STUDENTS, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);

        intent.setAction(ACTION_GET_STUDENTS);

        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void saveStudent(Context context, Student student) {
        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent pendingIntent = ((AppCompatActivity) context).createPendingResult(REQUEST_CODE_SAVE_STUDENT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_STUDENT, student);
        intent.setAction(ACTION_SAVE_STUDENT);

        context.startService(intent);
    }

    public static void deleteStudent(Context context, String student_uuid) {
        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent pendingIntent = ((AppCompatActivity) context).createPendingResult(REQUEST_CODE_DELETE_STUDENT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_STUDENT_UUID, student_uuid);
        intent.setAction(ACTION_DELETE_STUDENTS);

        context.startService(intent);
    }

    public static void updateStudent(Context context, Student student) {
        Intent intent = new Intent(context, MyIntentService.class);
        PendingIntent pendingIntent = ((AppCompatActivity) context).createPendingResult(REQUEST_CODE_UPDATE_STUDENT, intent, 0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);
        intent.putExtra(EXTRA_STUDENT, student);
        intent.setAction(ACTION_UPDATE_STUDENTS);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            DataBaseHelper helper = new DataBaseHelper(this);
            Intent result = new Intent();

            final String action = intent.getAction();
            if (ACTION_GET_STUDENTS.equals(action)) {
                ArrayList<Student> students = helper.getStudents();
                result.putExtra(EXTRA_STUDENTS, students);

            } else if (ACTION_SAVE_STUDENT.equals(action)) {
                Student student = intent.getParcelableExtra(EXTRA_STUDENT);

                student.id = helper.insert_student(student, student.GroupName, "");

                result.putExtra(EXTRA_STUDENT, student);

            } else if (ACTION_DELETE_STUDENTS.equals(action)) {
                String student_uuid = intent.getStringExtra(EXTRA_STUDENT_UUID);
                int cnt = helper.deleteStudent(student_uuid);
                result.putExtra(EXTRA_STUDENT, cnt);

            } else if (ACTION_UPDATE_STUDENTS.equals(action)) {
                Student student = intent.getParcelableExtra(EXTRA_STUDENT);
                int cnt = helper.update_student(student);
                result.putExtra(EXTRA_STUDENT, student);

            }



            PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
            try {
                pendingIntent.send(this, MainActivity.RESULT_OK, result);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
