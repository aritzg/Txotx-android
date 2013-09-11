package net.sareweb.android.txotx.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sareweb.android.txotx.model.Gertaera;
import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.android.txotx.model.SagardoEgun;
import net.sareweb.android.txotx.model.Sagardotegi;
import net.sareweb.lifedroid.model.DLFileEntry;
import net.sareweb.lifedroid.model.User;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

public class ImageUtils {

	
	public static void resizeFile(File f) {

		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			
			final int REQUIRED_SIZE = 400;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			
			Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);
			
			
			
			ExifInterface exif = new ExifInterface(f.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL); 
			
			orientation = orientation % 10;
			Log.d(TAG, "Orientation " + orientation);
			int rotate=0;
			switch(orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				Log.d(TAG, "ORIENTATION_ROTATE_270");
				rotate=90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				Log.d(TAG, "ORIENTATION_ROTATE_180");
				rotate=90;
			case ExifInterface.ORIENTATION_ROTATE_90:
				Log.d(TAG, "ORIENTATION_ROTATE_90");
				rotate=90;
			}
			
			Log.d(TAG, "rotate " + rotate);
			
			 Matrix m = new Matrix();

			 m.setRotate(rotate, (float) bmp.getWidth() / 2, (float) bmp.getHeight() / 2);

	            Bitmap b2 = Bitmap.createBitmap(
	                    bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
	            if (bmp != b2) {
	                bmp.recycle();
	                bmp = b2;
	            }
		            
		       

			
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		    
		    b2.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
			
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			return;

		} catch (Exception e) {
			Log.e(TAG, "Error scaling image", e);
		}

		return;
	}

	public static File getOutputMediaFile(String prefix) {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Txotx");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e(TAG, "failed to create directory");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ prefix + "_" + timeStamp + ".jpg");

		return mediaFile;
	}

	public static File getOutputTmpJpgFile() {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Txotx");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e(TAG, "failed to create directory");
				return null;
			}
		}

		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "txootx_tmp.jpg");

		return mediaFile;
	}

	public static String getMediaStorageDir() {

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Barazkide");

		return mediaStorageDir.getAbsolutePath();
	}

	public static void copyImage(File from, File to) throws IOException {
		FileInputStream inStream = new FileInputStream(from);
		FileOutputStream outStream = new FileOutputStream(to);

		byte[] buffer = new byte[1024];

		int length;
		// copy the file content in bytes
		while ((length = inStream.read(buffer)) > 0) {

			outStream.write(buffer, 0, length);

		}

		inStream.close();
		outStream.close();
	}

	public static void copyInputStreamToFile(InputStream from, File to)
			throws IOException {
		FileInputStream inStream = (FileInputStream) from;
		FileOutputStream outStream = new FileOutputStream(to);

		byte[] buffer = new byte[1024];

		int length;
		// copy the file content in bytes
		while ((length = inStream.read(buffer)) > 0) {

			outStream.write(buffer, 0, length);

		}

		inStream.close();
		outStream.close();
	}

	/*
	 * public static DLFileEntry composeDLFileEntry(Picking p){ DLFileEntry file
	 * = new DLFileEntry(); file.setFolderId(OnddoConstants.IMAGE_FOLDER);
	 * file.setRepositoryId(OnddoConstants.IMAGE_REPOSITORY);
	 * file.setSourceFileName(p.getImgName());
	 * 
	 * //TODO:This is a fucking mess!! File f = new
	 * File(file.getSourceFileName()); Uri fileUri = Uri.fromFile(f); String
	 * fileExtension = MimeTypeMap.getSingleton()
	 * .getFileExtensionFromUrl(fileUri.toString()); String mimeType =
	 * MimeTypeMap.getSingleton().getMimeTypeFromExtension( fileExtension);
	 * 
	 * file.setMimeType(mimeType); return file; }
	 */

	public static DLFileEntry composeDLFileEntry(long folderId, File f) {
		DLFileEntry dlFile = new DLFileEntry();
		dlFile.setGroupId(Constants.GROUP);
		dlFile.setFolderId(folderId);
		dlFile.setRepositoryId(Constants.GROUP);
		dlFile.setSourceFileName(f.getName());
		dlFile.setTitle(f.getName());

		Uri fileUri = Uri.fromFile(f);
		String fileExtension = MimeTypeMap.getSingleton()
				.getFileExtensionFromUrl(fileUri.toString());
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				fileExtension);

		dlFile.setMimeType(mimeType);
		return dlFile;
	}

	public static String getSagardotegiImageUrl(Sagardotegi sagardotegi) {
		if (sagardotegi.getIrudia() == null
				|| sagardotegi.getIrudia().equals("")) {
			return "";
		} else {
			String imageUrl = "http://" + Constants.SERVICE_URL + ":"
					+ Constants.SERVICE_PORT + "/documents/" + Constants.GROUP
					+ "/" + sagardotegi.getIrudiKarpetaId() + "/"
					+ sagardotegi.getIrudia();
			return imageUrl;
		}
	}
	
	public static String getSagardoEgunImageUrl(SagardoEgun sagardoEgun) {
		if (sagardoEgun.getIrudia() == null
				|| sagardoEgun.getIrudia().equals("")) {
			return "";
		} else {
			String imageUrl = "http://" + Constants.SERVICE_URL + ":"
					+ Constants.SERVICE_PORT + "/documents/" + Constants.GROUP
					+ "/" + sagardoEgun.getIrudiKarpetaId() + "/"
					+ sagardoEgun.getIrudia();
			return imageUrl;
		}
	}

	public static String getGertaeraImageUrl(Gertaera gertaera) {
		if (gertaera.getIrudia() == null || gertaera.getIrudia().equals("")) {
			return "";
		} else {
			String imageUrl = "http://" + Constants.SERVICE_URL + ":"
					+ Constants.SERVICE_PORT + "/documents/" + Constants.GROUP
					+ "/" + gertaera.getIrudiKarpetaId() + "/"
					+ gertaera.getIrudia();
			return imageUrl;
		}
	}
	
	public static String getOharraImageUrl(Oharra oharra) {
		if (oharra.getIrudia() == null || oharra.getIrudia().equals("")) {
			return "";
		} else {
			String imageUrl = "http://" + Constants.SERVICE_URL + ":"
					+ Constants.SERVICE_PORT + "/documents/" + Constants.GROUP
					+ "/" + oharra.getIrudiKarpetaId() + "/"
					+ oharra.getIrudia();
			return imageUrl;
		}
	}
	
	public static String getPortraitImageUrl(User user) {
		if (user == null || user.getPortraitId()==null || user.getPortraitId()==0 ) {
			return "";
		} else {
			String imageUrl = "http://" + Constants.SERVICE_URL + ":"
					+ Constants.SERVICE_PORT + "/image/user_portrait?img_id=" + user.getPortraitId();
			return imageUrl;
		}
	}

	private static String TAG = "ImageUtil";

}