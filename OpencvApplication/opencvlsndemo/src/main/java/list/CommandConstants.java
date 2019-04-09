package list;

public interface CommandConstants {
    public static final String TEST_ENV_COMMAND = "环境测试-灰度";
    public static final String MAT_PIXEL_INVERT_COMMAND = "像素操作-取反";
    public static final String BITMAP_PIXEL_INVERT_COMMAND = "Bitmap像素操作-取反";
    public static final String PIXEL_SUBSTRACT_COMMAND = "像素操作-减法";
    public static final String PIXEL_ADD_COMMAND = "像素操作-加法";
    public static final String ADJUST_CONTRAST_COMMAND = "调整对比度与亮度";
    public static final String IMAGE_CONTAINER_COMMAND = "图像容器-Mat演示";
    public static final String SUB_IMAGE_COMMAND = "图像容器-获取子图";
    public static final String BLUR_IMAGE_COMMAND = "均值模糊";
    public static final String GAUSSIAN_BLUR_COMMAND = "高斯模糊";
    public static final String BI_BLUR_COMMAND = "双边模糊";
    public static final String CUSTOM_BLUR_COMMAND = "自定义算子-模糊";
    public static final String CUSTOM_EDGE_COMMAND = "自定义算子-边缘";
    public static final String CUSTOM_SHARPEN_COMMAND = "自定义算子-锐化";
    public static final String ERODE_COMMAND = "腐蚀/最小值滤波";
    public static final String DILATE_COMMAND = "膨胀/最大值滤波";
    public static final String OPEN_COMMAND = "开操作";
    public static final String CLOSE_COMMAND = "闭操作";
    public static final String MORPH_LINE_COMMAND = "形态学直线监测";

}
