package list;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.administrator.opencvlsndemo.MainActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ProcessImageUtils {
    private final static String TAG = ProcessImageUtils.class.getSimpleName();
    private Context context;

   //图片的灰度化
    public static Bitmap convert2Gray(Bitmap bitmap)
    {
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.cvtColor(src,dst,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(dst,bitmap);
        //一定要手动释放,矩阵消耗大量内存
        src.release();
        dst.release();
        return bitmap;
    }
  //像素取反方法一,Core
    public static Bitmap invert(Bitmap bitmap)
    {
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);
        long startTime = System.currentTimeMillis();
        Core.bitwise_not(src, src);
        long end = System.currentTimeMillis() - startTime;
        Log.i("Mat-TIME", "\t" + end);
        Utils.matToBitmap(src, bitmap);
        src.release();
        return bitmap;
    }
    //bitmap操作像素,建立缓冲区
    public static void local_invert(Bitmap bitmap)
    {
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int[] pixels=new int[width*height];
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        int index=0;
        int a=0,r=0,g=0,b=0;
        long startTime = System.currentTimeMillis();
        for (int row=0;row<height;row++)
        {
            index=row*width;
            for(int col=0;col<width;col++)
            {
                int pixel=pixels[index];
                a=(pixel>>24)&0xff;
                r=(pixel>>16)&0xff;
                g=(pixel>>8)&0xff;
                b=(pixel&0xff);
                r=255-r;
                g=255-g;
                b=255-b;
                pixel=((a&0xff)<<24)|((r&0xff)<<16)|((g&0xff)<<8)|(b&0xff);
                pixels[index]=pixel;
                index++;
            }

        }
        long end = System.currentTimeMillis() - startTime;
        Log.i("TIME", "\t" + end);
        bitmap.setPixels(pixels,0,width,0,0,width,height);

    }
    //两张图片像素相减
    public static Bitmap substract(Bitmap bitmap)
    {
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //新建一个白底照片
        Mat whiteImg=new Mat(src.size(),src.type(), Scalar.all(255));
        long StartTime=System.currentTimeMillis();
        Core.subtract(whiteImg,src,src);
        long end=System.currentTimeMillis()-StartTime;
        Log.i("Mat-Time","\t"+end);
        Utils.matToBitmap(src,bitmap);
        src.release();
        whiteImg.release();
        return bitmap;
        }
    //两张图片像素相加
    public static Bitmap add(Bitmap bitmap)
    {   Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //新建一个白底照片
        Mat whiteImg=new Mat(src.size(),src.type(), Scalar.all(100));
        long StartTime=System.currentTimeMillis();
        //按照权重进行像素相加
        Core.addWeighted(whiteImg,0.5,src,0.5,0.0,src);
        long end=System.currentTimeMillis()-StartTime;
        Log.i("Mat-Time","\t"+end);
        Utils.matToBitmap(src,bitmap);
        src.release();
        whiteImg.release();
        return bitmap;

    }
    public ProcessImageUtils(Context context){
        this.context = context;
    }


    //对比和亮度
    public static void adjustContrast(Bitmap bitmap) {
        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        src.convertTo(src, CvType.CV_32F);
        //新建一个白底照片
        Mat whiteImg=new Mat(src.size(),src.type(), Scalar.all(1.25));
        Mat bwImg=new Mat(src.size(),src.type(),Scalar.all(30));
        Core.multiply(whiteImg,src,src);
        Core.add(bwImg,src,src);
        src.convertTo(src,CvType.CV_8U);
        long StartTime=System.currentTimeMillis();
        Utils.matToBitmap(src,bitmap);
        src.release();
        whiteImg.release();
        bwImg.release();
    }
    //图像容器-Mat演示,用一块灰色替代原来的图片
    public static Bitmap demoMatUsage() {
        //创建bitmap
        Bitmap bitmap=Bitmap.createBitmap(400,600,Bitmap.Config.ARGB_8888);
        Mat dst=new Mat(bitmap.getHeight(),bitmap.getWidth(),CvType.CV_8UC1,new Scalar(100));
        Utils.matToBitmap(dst,bitmap);
        dst.release();
        return bitmap;
    }
    //图像容器-获取子图(截取子图)
    public static Bitmap getROIArea(Bitmap bitmap)
    {   //新建一个reac矩形,用来选择子图
        Rect roi=new Rect(200,150,200,300);
        Bitmap roimap=Bitmap.createBitmap(roi.width,roi.height,Bitmap.Config.ARGB_8888);

        Mat src=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //截取子图的mat数据
        Mat roiMat=src.submat(roi);
        Mat roiDstMat=new Mat();
        //
        Imgproc.cvtColor(roiMat,roiDstMat,Imgproc.COLOR_BGRA2GRAY);
        Utils.matToBitmap(roiDstMat,roimap);

        src.release();
        roiMat.release();
        roiDstMat.release();
        return roimap;
    }
    //均值模糊
    public static void meanBlur(Bitmap bitmap)
    {
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        Imgproc.blur(src, dst, new Size(1, 111), new Point(-1, -1), Imgproc.BORDER_DEFAULT);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
    }
    //高斯模糊
    public static void gaussianBlur(Bitmap bitmap)
    {Mat src=new Mat();
     Mat dst=new Mat();
     Utils.bitmapToMat(bitmap,src);
     Imgproc.GaussianBlur(src,dst,new Size(31,31),0,0,4);
     Utils.matToBitmap(dst,bitmap);
     src.release();
     dst.release();
    }
    //双边模糊加自定义算子,实现图片的锐化
    public static void biBlur(Bitmap bitmap) {
        Mat src=new Mat();
        Mat dst=new Mat();
        Utils.bitmapToMat(bitmap,src);
        //Mat类型，图像必须是8位或浮点型单通道、三通道的图像。
        // 四通道转换成三通道
        Imgproc.cvtColor(src,src,Imgproc.COLOR_BGRA2BGR);
        //双边模糊,sigmaColor 是颜色的差,sigmaSpace, 空间
        Imgproc.bilateralFilter(src,dst,15,150,15,Imgproc.BORDER_DEFAULT);
        //自定义mask
        Mat kernel=new Mat(3,3,CvType.CV_16S);
        kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0);
        Imgproc.filter2D(dst,dst,-1,kernel,new Point(-1,-1),0.0,4);
        Utils.matToBitmap(dst,bitmap);
        src.release();
        dst.release();
        kernel.release();
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }



    /**
     * get path by Uri
     * @param uri uri
     * @return path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor imageCursor = ((Activity)context).managedQuery(uri, projection, null, null, null);
        int actual_image_column_index = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        imageCursor.moveToFirst();
        String img_path = imageCursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * get bitmap from the given path
     * @param path path
     * @return Bitmap
     */
    public Bitmap decodeImage(String path) {
        Bitmap res;
        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 1;
            op.inJustDecodeBounds = false;
            res = BitmapFactory.decodeFile(path, op);
            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }

            Bitmap temp = Bitmap.createBitmap(res, 0, 0, res.getWidth(), res.getHeight(), matrix, true);
            Log.i(TAG, "check target Image:" + temp.getWidth() + "*" + temp.getHeight());

            if (!temp.equals(res)) {
                res.recycle();
            }
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
