package net.ronmichael.trial.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import net.ronmichael.trial.R;
import net.ronmichael.trial.util.AssortedUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class NewMessageActivity extends Activity {

    public static final int REQUEST_CODE_OPEN_GALLERY = 1001;


    public ImageView mPhotoView;

    /**
     * Launches a new instances of the activity.
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, NewMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        AssortedUtil.lockScreenOrientation(this);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssortedUtil.openGallery(NewMessageActivity.this,
                        REQUEST_CODE_OPEN_GALLERY, getString(R.string.chooser_select_gallery_item));
            }
        });

        findViewById(R.id.open_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssortedUtil.openGallery(NewMessageActivity.this,
                        REQUEST_CODE_OPEN_GALLERY, getString(R.string.chooser_select_gallery_item));
            }
        });

        mPhotoView = (ImageView) findViewById(R.id.message_image);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("TRACE", "onBackPressed()");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            protected Activity mActivity;

            public DialogInterface.OnClickListener init(Activity activity){
                mActivity = activity;
                return this;
            }

            public void onClick(DialogInterface dialog, int id) {
                mActivity.finish();
            }
        }.init(this);

        AssortedUtil.showConfirmationDialog(this, R.string.confirmation_leave_new_post_message,
                listener);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.d("TRACE", "NewMessageActivity onActivityResult()");

        switch (requestCode) {

            case REQUEST_CODE_OPEN_GALLERY:
                Log.d("TRACE", "1");

                if (resultCode != Activity.RESULT_OK) {
                    Log.d("TRACE", "2");
                    break;
                }

                Uri imageUri = imageReturnedIntent.getData();

                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    break;
                }

                Bitmap original = BitmapFactory.decodeStream(stream);
                mPhotoView.setImageBitmap(original);
        }
    }
}
