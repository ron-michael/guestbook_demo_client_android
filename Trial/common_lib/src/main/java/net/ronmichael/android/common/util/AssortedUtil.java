/*
 * Copyright (C) 2014
 * 
 */

/* REVISION HISTORY
 * 
 * DATE         NAME                      REMARKS
 * 2014/06/13   Ron Michael Khu           Created
 */

package net.ronmichael.android.common.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.text.DecimalFormat;

/**
 * Contains assorted utility or helper methods.
 */
public class AssortedUtil {

    /**
     * Gets a list of the files in the specified directory
     *
     * @param dir directory path to be read
     * @return list of filenames
     */
    public static String[] getFileListing(File dir) {
        return getFileListing(dir, null);
    }

    /**
     * Copy contents from the input stream to the output stream
     *
     * @param inputStream  input stream pointing to the input file
     * @param outputStream output stream pointing to the output file
     * @return true when there are no error; otherwise, false
     */
    public static boolean copyContents(InputStream inputStream, OutputStream outputStream) {
        //-- Copy contents --//
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Shows a confirmation dialog with the specified listener as the listener to invoke
     * when the user chooses to confirm.
     *
     * @param context    launching context
     * @param messageId   resource ID of the confirmation message string
     * @param yesListener listener to invoke when the user chooses to confirm
     */
    public static void showConfirmationDialog(Context context, int messageId,
                                              DialogInterface.OnClickListener yesListener) {

        showConfirmationDialog(context, context.getString(messageId), yesListener);
    }

    /**
     * Shows a confirmation dialog with the specified listener as the listener to invoke
     * when the user chooses to confirm.
     *
     * @param context    launching context
     * @param message     confirmation message string
     * @param yesListener listener to invoke when the user chooses to confirm
     */
    public static void showConfirmationDialog(Context context, String message,
                                              DialogInterface.OnClickListener yesListener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, yesListener)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(
                            final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    /**
     * Shows a message dialog with the specified listener as the listener to invoke
     * when the user chooses to close the dialog.
     *
     * @param context launching context
     * @param title    dialog title
     * @param message  message string to display
     * @param listener  listener to invoke when the user chooses to close the dialog
     */
    public static void showMessageDialog(Context context, String title, String message,
                                         DialogInterface.OnClickListener listener) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton(android.R.string.ok, listener);

        builder.show();
    }

    /**
     * Gets a list of the files in the specified directory
     *
     * @param dir       directory path to be read
     * @param extension filename extension to be used to filter files to find
     * @return list of filenames
     */
    public static String[] getFileListing(File dir, String extension) {

        if (dir.isDirectory() == false) {
            return null;
        }

        FilenameFilter filter = null;
        if (null != extension && extension.trim().length() > 0) {
            filter = new GenericExtensionFilter(extension);
        }

        String files[] = dir.list(filter);

        return files;
    }

    /**
     * Extension filename filter used in getFileListing()
     */
    public static class GenericExtensionFilter implements FilenameFilter {
        private String mExtension;

        public GenericExtensionFilter(String extension) {
            mExtension = extension;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(mExtension));
        }
    }

    /**
     * Sends an email to designated recipients with the specified subject, message body,
     * attachment. If there are multiple email applications, the chooseMessage will be
     * used as prompt message.
     *
     * @param context       calling context
     * @param subject       email subject
     * @param to            email recipients
     * @param body          message body
     * @param attachment    file attachment
     * @param chooseMessage mail chooser prompt message
     */
    public static void sendMail(Context context, String subject, String[] to, String body,
                                Uri attachment, String chooseMessage) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        context.startActivity(Intent.createChooser(intent, chooseMessage));
    }

    /**
     * Writes the specified contents to a file internal to the application.
     *
     * @param context  calling context
     * @param filename filename for the internal file
     * @param contents contents to write to the file
     * @return true on success; otherwise, false
     */
    public static boolean writeInternalFile(Context context, String filename, String contents) {

        FileOutputStream outputStream;

        // Open file output stream
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            return false;
        }

        boolean status = true;

        // Write contents
        try {
            outputStream.write(contents.getBytes());
        } catch (IOException e) {
            status = false;
        }

        // Close file
        try {
            outputStream.close();
        } catch (IOException e) {
        }

        return status;
    }

    /**
     * Read the contents of the specified internal file.
     *
     * @param context calling context
     * @param filename filename of the internal file
     *
     * @return data read on success; otherwise, null
     */
    public static String readInternalFile(Context context, String filename) {
        FileInputStream inputStream;

        // Open file for reading
        try {
            inputStream = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            return null;
        }

        // Storage and buffer for reading contents
        StringBuffer contents = new StringBuffer("");
        byte[] buffer = new byte[1024];

        // Read contents of file into storage
        int n;
        try {
            while ((n = inputStream.read(buffer)) != -1)
            {
                contents.append(new String(buffer, 0, n));
            }
        } catch (Throwable t) {
        }

        // Close the file
        try {
            inputStream.close();
        } catch (IOException e) {
        }

        return contents.toString();
    }

    /**
     * Format byte size into human-readable format
     *
     * @param size size to format
     *
     * @return human-readable format
     */
    public static String formatByteSize(long size){
        if (size <= 0) {
            return "0";
        }

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB", "EB" };
        int numGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(
                size/ Math.pow(1024, numGroups)) + " " + units[numGroups];
    }

    /**
     * Check if there is network connectivity
     *
     * @param context calling context
     *
     * @return true, when there is network connectivity; otherwise, false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Makes a new downloads request to the system download manager service.
     *
     * @param context calling context
     * @param uri URI to be accessed which points to the item to be downloaded
     * @param title title or label for the download request
     * @param description description for the download request
     * @param fileDirFilename filename of the destination file in external files directory
     * @param visibleFlag indicates whether the request should show on the DownloadManager's UI
     * @param notifyFlag indicates whether the DownloadManager should show notifications or not
     *
     * @return an ID for the download, unique across the system. This ID is used to make future
     * calls related to this download.
     *
     * @see android.app.DownloadManager#enqueue(android.app.DownloadManager.Request)
     */
    public synchronized static long requestDownload(Context context, String uri,
            String title, String description, String fileDirFilename,
            boolean visibleFlag, boolean notifyFlag) {
        Log.d("TRACE", "request download for: " + uri);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));

        request.setTitle(title);
        request.setDescription(description);

        // Set visibility on UI
        request.setVisibleInDownloadsUi(visibleFlag);
        request.setDestinationInExternalFilesDir(context, null, fileDirFilename);

        // Set notification status
        int visibility = DownloadManager.Request.VISIBILITY_HIDDEN;
        if (notifyFlag) {
            visibility = DownloadManager.Request.VISIBILITY_VISIBLE;
        }
        request.setNotificationVisibility(visibility);

        // Enqueue download request
        return downloadManager.enqueue(request);
    }

    /**
     * Creates an intent to any application that can view the specified file
     *
     * @param context calling context
     * @param file file to open
     */
    public static void openFile(Context context, File file) {
        Uri fileUri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, URLConnection.guessContentTypeFromName(fileUri.toString()));

        context.startActivity(intent);
    }


    /**
     * Launches the application settings associated with the specified package name.
     *
     * @param context calling context
     * @param packageName JAVA package name of the application
     */
    public static void launchApplicationSettings(Context context, String packageName) {

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Retrieves the last path component of the specified URI string
     *
     * @param uriString URI string
     *
     * @return last path component
     */
    public static String getLastPathComponent(String uriString) {
        URI uri = null;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return uriString;
        }

        String path = uri.getPath();
        String part = path.substring(path.lastIndexOf('/') + 1);

        return part;
    }

    /**
     * Locks the screen orientation. The effect is that app wont adjust to the sensor.
     *
     * @param activity calling activity
     */
    /*
     * http://stackoverflow.com/questions/1512045/how-to-disable-orientation-change-in-android
     */
    public static void lockScreenOrientation(Activity activity)
    {
        WindowManager windowManager =  (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Configuration configuration = activity.getResources().getConfiguration();
        int rotation = windowManager.getDefaultDisplay().getRotation();

        // Search for the natural position of the device
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) ||
                configuration.orientation == Configuration.ORIENTATION_PORTRAIT &&
                        (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270))
        {
            // Natural position is Landscape
            switch (rotation)
            {
                case Surface.ROTATION_0:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_90:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_180:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
                case Surface.ROTATION_270:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }
        }
        else
        {
            // Natural position is Portrait
            switch (rotation)
            {
                case Surface.ROTATION_0:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case Surface.ROTATION_90:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_180:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_270:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
            }
        }
    }

    /**
     * Unlocks the screen orientation. The effect is that app will use the previous setting before
     * lockScreenOrientation() was invoked.
     *
     * @param activity calling activity
     */
    public static void unlockScreenOrientation(Activity activity)
    {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Pushes the specified fragment unto the backstack and into the view associated with
     * contanerId
     *
     * @param fragment fragment to push
     * @param containerId ID of the container to hold the fragment
     * @param tagName tag name
     */
    public static void pushFragment(Activity activity, android.app.Fragment fragment,
                                    int containerId, String tagName) {

        android.app.FragmentManager fragmentManager = activity.getFragmentManager();

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(containerId, fragment)
                .commit();

        fragmentManager.executePendingTransactions();
    }

    /**
     * Pushes the specified fragment unto the backstack and into the view associated with
     * contanerId
     *
     * @param fragment fragment to push
     * @param containerId ID of the container to hold the fragment
     * @param tagName tag name
     */
    public static void pushFragment(FragmentActivity activity, Fragment fragment, int containerId, String tagName) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(tagName);
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    /**
     * Pushes the specified fragment unto the backstack and into the view associated with
     * contanerId
     *
     * @param fragment fragment to push
     * @param containerId ID of the container to hold the fragment
     * @param tagName tag name
     */
    public static void pushFragment(Fragment context, Fragment fragment, int containerId,
                                    boolean addToBackStack, String tagName) {

        FragmentManager fragmentManager = context.getChildFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(tagName);
        }

        transaction.replace(containerId, fragment, tagName);
        transaction.commit();

        //fragmentManager.executePendingTransactions();
    }

    /**
     * Pushes the specified fragment unto the backstack and into the view associated with
     * contanerId
     *
     * @param fragment fragment to push
     * @param containerId ID of the container to hold the fragment
     * @param tagName tag name
     */
    public static void pushFragment(FragmentTransaction transaction, Fragment fragment, int containerId, String tagName) {
        transaction.addToBackStack(tagName);
        transaction.replace(containerId, fragment);
        transaction.commit();
        fragment.getChildFragmentManager().executePendingTransactions();
    }

    /**
     * Pops the topmost fragment on the stack
     *
     * @param context calling context

     * @return true, if a fragment has been popped; otherwise, false
     */
    public static boolean popFragment(Fragment context) {
        boolean isPop = false;

        if (context.getChildFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            context.getChildFragmentManager().popBackStack();
        }

        return isPop;
    }

    /**
     * Pops all fragments on the stack
     *
     * @param context calling context

     * @return true, if at least one fragment has been popped; otherwise, false
     */
    public static boolean popAllFragments(Fragment context) {
        boolean isPop = false;

        int count = context.getChildFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            isPop = true;
            context.getChildFragmentManager().popBackStack();
        }

        return isPop;
    }


    /**
     * Pops all fragments on the stack
     *
     * @param context calling context

     * @return true, if at least one fragment has been popped; otherwise, false
     */
    public static boolean popAll(FragmentActivity context) {
        boolean isPop = false;

        int count = context.getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            isPop = true;
            context.getSupportFragmentManager().popBackStack();
        }

        return isPop;
    }
    /**
     * Pops the topmost fragment on the stack
     *
     * @param context calling context

     * @return true, if a fragment has been popped; otherwise, false
     */
    public static boolean popFragment(Activity context) {
        boolean isPop = false;

        if (context.getFragmentManager().getBackStackEntryCount() > 0) {
            isPop = true;
            context.getFragmentManager().popBackStack();
        }

        return isPop;
    }

}
