package com.dga.services.waa.Utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Configurations {
    public static final String PREF_NAME = "oparadeals";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 2, NETWORK_STAUS_WIFI = 1, NETWORK_STATUS_MOBILE = 0;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 0;
    public static int TYPE_NOT_CONNECTED = 2;
    private static Dialog dialog1;
    private static Dialog dialogShimmer;
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    public String languagecode = "";
    // Constructor
    @SuppressLint("CommitPrefEdits")
    public Configurations(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public static void hideDilog() {
        dialog1.dismiss();
    }
    public static String getMainColor() {
        try {
            return pref.getString("mainColor", "#2abef4");
        }catch (Exception e){
            e.printStackTrace();
            return "#0073ac";
        }
    }
   /* public static void showDilog(Context context) {
        Configurations settingsMain = new Configurations(context);

        dialog1 = new Dialog(context, R.style.Theme_Opara);
        dialog1.setContentView(R.layout.dilog_progressbar);
        dialog1.setCancelable(false);

        TextView textView = dialog1.findViewById(R.id.id_title);
        ProgressBar progressBar = dialog1.findViewById(R.id.id_pbar);
        textView.setText(settingsMain.getAlertDialogMessage("waitMessage"));
        dialog1.show();
    }*/
    public void setMainColor(String mainColor) {
        editor.putString("mainColor", mainColor);
        editor.commit();
    }

    /* Checking Internet Connection */
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
        }
        return false;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
                activeNetwork = cm.getActiveNetworkInfo();
                Log.d("info d ", activeNetwork.getType() + "" + activeNetwork.getTypeName());
            }
        }


        if (null != activeNetwork) {
            Log.d("info adssad", "adasd");
            if (activeNetwork.getType() == TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        Log.d("info d", getConnectivityStatus(context) + "");
        int conn = getConnectivityStatus(context);
        int status = 0;
        if (conn == TYPE_WIFI) {
            status = NETWORK_STAUS_WIFI;
        } else if (conn == TYPE_MOBILE) {
            status = NETWORK_STATUS_MOBILE;
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
/*
    public static void enableInternetReceiver(Context context) {
        ComponentName component = new ComponentName(context, NetwordStateManager.class);

        context.getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void disableInternetReceiver(Context context) {
        Log.d("info check net", "disable net");
        ComponentName component = new ComponentName(context, NetwordStateManager.class);
        context.getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

    public static boolean isInternetReceiverEnabled(Context context) {
        ComponentName component = new ComponentName(context, NetwordStateManager.class);
        int status = context.getPackageManager().getComponentEnabledSetting(component);
        if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return true;
        } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            return false;
        }
        return false;

    }*/
    public static String getLanguageCode() {
        return pref.getString("code_lang", "fr");
    }


    public static Uri decodeFile(Context context, File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//
//       File destFile = new File(file, "img_"
//                + dateFormatter.format(new Date()).toString() + ".png");
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), b, "Title", null);
        String path = saveImage(b, context);
        return Uri.parse(path);
    }

    public void setLanguageCode(String languageCode) {
        editor.putString("code_lang", languageCode);
        editor.commit();
    }
    public boolean isLanguageChanged() {
        return pref.getBoolean("isLanguageChanged", false);
    }

    public void setLanguageChanged(boolean b) {
        editor.putBoolean("isLanguageChanged", b);
        editor.commit();
    }

    public boolean isLocationChanged() {
        return pref.getBoolean("isLocationChanged", false);
    }

    public void setLocationChanged(boolean b) {
        editor.putBoolean("isLocationChanged", b);
        editor.commit();
    }
    public String getAlertDialogTitle(String type) {
        return pref.getString(type, "Error");
    }

    public void setAlertDialogTitle(String type, String title) {
        editor.putString(type, title);
        editor.commit();
    }


    public String getAlertDialogMessage(String type) {
        return pref.getString(type, "Vous n'êtes pas connecté à Internet. S'il vous plaît, vérifiez votre connexion à internet et réessayez");
    }


    public String getExitApp(String type) {
        return pref.getString(type, "Are You Sure You Want Exit ?");
    }

    public String getAlertOkText() {
        return pref.getString("AlertOkText", "OK");
    }

    public void setAlertOkText(String msg) {
        editor.putString("AlertOkText", msg);
        editor.commit();
    }
    public void setAlertDialogMessage(String type, String msg) {
        editor.putString(type, msg);
        editor.commit();
    }
    public String getAlertCancelText() {
        return pref.getString("AlertCancelText", "CANCEL");
    }

    public void setAlertCancelText(String msg) {
        editor.putString("AlertCancelText", msg);
        editor.commit();
    }

    public String getGenericAlertTitle() {
        return pref.getString("title", "Confirm!");
    }

    public void setGenericAlertTitle(String title) {
        editor.putString("title", title);
        editor.commit();
    }

    public String getGenericAlertMessage() {
        return pref.getString("text", "Are You Sure You Want To Do This!");
    }


    public void setGenericAlertMessage(String title) {
        editor.putString("text", title);
        editor.commit();
    }

    public String getGenericAlertOkText() {
        return pref.getString("btn_ok", "OK");
    }

    public void setGenericAlertOkText(String title) {
        editor.putString("btn_ok", title);
        editor.commit();
    }

    public String getGenericAlertCancelText() {
        return pref.getString("btn_no", "Cancel");
    }

    public void setGenericAlertCancelText(String title) {
        editor.putString("btn_no", title);
        editor.commit();
    }

    public void isAppOpen(boolean appOpen) {
        editor.putBoolean("app_open", appOpen);
        editor.commit();
    }

    public boolean getAppOpen() {
        return pref.getBoolean("app_open", false);
    }

    public void checkOpen(boolean appOpen) {
        editor.putBoolean("checkOpen", appOpen);
        editor.commit();
    }

    public boolean isAdShowOrNot() {
        return pref.getBoolean("setShowAd", true);
    }

    public void setAdShowOrNot(boolean b) {
        editor.putBoolean("setShowAd", b);
        editor.commit();
    }
    public void setUserImage(String userImage) {
        editor.putString("image", userImage);
        editor.commit();
    }

    public String getUserLogin() {
        return pref.getString("login", "0");
    }

    public void setUserLogin(String userLogin) {
        editor.putString("login", userLogin);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString("username", "UserName");
    }

    public void setUserName(String userName) {
        editor.putString("username", userName);
        editor.commit();

    }

    public String getUserEmail() {
        return pref.getString("useremail", "UserEmail");
    }

    public void setUserEmail(String userEmail) {
        editor.putString("useremail", userEmail);
        editor.commit();

    }

    public String getUserPassword() {
        return pref.getString("userPassword", "0");
    }

    public void setUserPassword(String UserPassword) {
        editor.putString("userPassword", UserPassword);
        editor.commit();
    }

    public boolean getAdsShow() {
        return pref.getBoolean("showAd", false);
    }

    public void setAdsShow(boolean value) {
        editor.putBoolean("showAd", value);
        editor.commit();
    }
    public String getNoLoginMessage() {
        return pref.getString("noLoginmessage", "Vous n'etes autoirise a eeffecuter cet action.");
    }

    public void setNoLoginMessage(String message) {
        editor.putString("noLoginmessage", message);
        editor.commit();
    }
   /* public static void setProgressModel(ProgressModel progressModel) {
        Gson gson = new Gson();
        String toJson = gson.toJson(progressModel);
        editor.putString("progressModel", toJson);
        editor.apply();

    }

    public static ProgressModel getProgressSettings(Context context) {


        Gson gson = new Gson();
        String progressModel = pref.getString("progressModel", null);
        ProgressModel model = gson.fromJson(progressModel, ProgressModel.class);
        return model;
    }
*/

/*
    public void setAdPostImageModel(AdPostImageModel AdPostImageModel) {
        Gson gson = new Gson();
        String toJson = gson.toJson(AdPostImageModel);
        editor.putString("AdPostImageModel", toJson);
        editor.apply();

    }

    public AdPostImageModel getAdPostImageModel(Context context) {
        Gson gson = new Gson();
        String progressModel = pref.getString("AdPostImageModel", null);
        AdPostImageModel model = gson.fromJson(progressModel, AdPostImageModel.class);
        return model;
    }*/
    public void setLocationId(String locationId) {
        editor.putString("code_pays", locationId);
        editor.commit();
    }

    public static String getAnnonceId() {
        return pref.getString("annonce_id", "");
    }

    public void setAnnonceId(String locationId) {
        editor.putString("annonce_id", locationId);
        editor.commit();
    }

    public static String getlocationId() {
        return pref.getString("code_pays", "");
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        String path = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                path = saveBitmap(inContext, inImage, Bitmap.CompressFormat.JPEG, "image/jpeg", "Title");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        }
        return Uri.parse(path);
    }

    private static String saveBitmap(Context context, @NonNull final Bitmap bitmap,
                                     @NonNull final Bitmap.CompressFormat format, @NonNull final String mimeType,
                                     @NonNull final String displayName) throws IOException {
        final String relativeLocation = Environment.DIRECTORY_PICTURES;

        final ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);

        final ContentResolver resolver = context.getContentResolver();

        OutputStream stream = null;
        Uri uri = null;

        try {
            final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            uri = resolver.insert(contentUri, contentValues);

            if (uri == null) {
                throw new IOException("Failed to create new MediaStore record.");
            }

            stream = resolver.openOutputStream(uri);

            if (stream == null) {
                throw new IOException("Failed to get output stream.");
            }

            if (bitmap.compress(format, 95, stream) == false) {
                throw new IOException("Failed to save bitmap.");
            }
        } catch (IOException e) {
            if (uri != null) {
                // Don't leave an orphan entry in the MediaStore
                resolver.delete(uri, null, null);
            }

            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return relativeLocation;
    }

   /* public static String getRealPathFromURI(Context inContext, Uri uri) {

        String filePath = "";

        Pattern p = Pattern.compile("(\\d+)$");
        Matcher m = p.matcher(uri.toString());
        if (!m.find()) {
            Log.e(AddNewAdPost.class.getSimpleName(), "ID for requested image not found: " + uri.toString());
            return filePath;
        }
        String imgId = m.group();

        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = inContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, null, null, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }*/

    public void setGoogleButn(boolean value) {
        editor.putBoolean("googleButton", value);
        editor.commit();
    }

    public boolean getGooglButn() {
        return pref.getBoolean("googleButton", false);
    }

    public void setfbButn(boolean value) {
        editor.putBoolean("fbButton", value);
        editor.commit();
    }
    public boolean getfbButn() {
        return pref.getBoolean("fbButton", false);
    }

    private static String saveImage(Bitmap b, Context context) {

        Bitmap bitmap = b;

        String state = Environment.getExternalStorageState();
        String root = "";
        String fileName = "/MyImage" + System.currentTimeMillis() + "Image" + ".jpg";
        String parent = "App_Name";
        File mFile;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            root = Environment.getExternalStorageDirectory().toString();
            mFile = new File(root, parent);
            if (!mFile.isDirectory())
                mFile.mkdirs();

        } else {
            root = context.getFilesDir().toString();
            mFile = new File(root, parent);
            if (!mFile.isDirectory())
                mFile.mkdirs();
        }

        String strCaptured_FileName = root + "/App_Name" + fileName;

        File f = new File(strCaptured_FileName);

        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bytes);

            FileOutputStream fo;

            fo = new FileOutputStream(f);

            fo.write(bytes.toByteArray());
            fo.close();
            bitmap.recycle();

            System.gc();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strCaptured_FileName;
    }
    public String getLinkednText() {
        return pref.getString("setLinkednText", "CANCEL");
    }

    public void setLinkednText(String msg) {
        editor.putString("setLinkednText", msg);
        editor.commit();
    }

    public void setLinkedinButn(boolean value) {
        editor.putBoolean("linkedin", value);
        editor.commit();
    }

    public boolean getLinkedinButn() {
        return pref.getBoolean("linkedin", false);
    }

    public void setLinkedinLogin(boolean value) {
        editor.putBoolean("linkedin", value);
        editor.commit();
    }

    public boolean getLinkedinLogin() {
        return pref.getBoolean("linkedin", false);
    }


    public void setUserPhone(String userPhone) {
        editor.putString("phone", userPhone);
        editor.commit();
    }

    public void setUserIdApp(String userID) {
        editor.putString("userID", userID);
        editor.commit();
    }
    public String getUserIdApp() {
        return pref.getString("userID", "0");
    }

    public String getUserImage() {
        return pref.getString("image", "0");
    }
    public static String getAppLogo() {
        return pref.getString("appLogo", "");

    }

    public void setAppLogo(String appLogo) {
        editor.putString("appLogo", appLogo);
        editor.commit();
    }
  /*  public static permissionsModel getPermissionsModel() {


        Gson gson = new Gson();
        String permissionsModel = pref.getString("permissionsModel", null);
        permissionsModel model = gson.fromJson(permissionsModel, permissionsModel.class);
        return model;
    }

    public static void setPermissionsModel(permissionsModel permissionsModel) {
        Gson gson = new Gson();
        String toJson = gson.toJson(permissionsModel);
        editor.putString("permissionsModel", toJson);
        editor.apply();

    }*/
}
