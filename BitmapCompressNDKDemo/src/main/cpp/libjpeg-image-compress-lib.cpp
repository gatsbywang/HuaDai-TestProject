#include "libjpeg-image-compress-lib.h"
#include <android/bitmap.h>
#include <android/log.h>
#include <stdio.h>
#include <setjmp.h>

//统一编译方式
extern "C" {
#include "jpeg/jpeglib.h"
#include "jpeg/cdjpeg.h"        /* Common decls for cjpeg/djpeg applications */
}
// log打印
#define LOG_TAG "jni"
#define LOGW(...)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define true 1
#define false 0

typedef uint8_t BYTE;

// error 结构体
char *error;
struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};

typedef struct my_error_mgr *my_error_ptr;

METHODDEF(void)
my_error_exit(j_common_ptr cinfo) {
    my_error_ptr myerr = (my_error_ptr) cinfo->err;
    (*cinfo->err->output_message)(cinfo);
    error = (char *) myerr->pub.jpeg_message_table[myerr->pub.msg_code];
    LOGE("jpeg_message_table[%d]:%s", myerr->pub.msg_code,
         myerr->pub.jpeg_message_table[myerr->pub.msg_code]);
    longjmp(myerr->setjmp_buffer, 1);
}


//extern "C"
//jstring
//Java_com_demo_ndk_MainActivity_stringFromJNI(
//        JNIEnv *env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}

extern "C"
int generateJPEG(BYTE *data, int w, int h, int quality,
                 const char *outfilename, jboolean optimize) {

    // 结构体相当于Java类
    struct jpeg_compress_struct jcs;

    //当读完整个文件的时候就会回调my_error_exit这个退出方法。
    struct my_error_mgr jem;
    jcs.err = jpeg_std_error(&jem.pub);
    jem.pub.error_exit = my_error_exit;
    // setjmp是一个系统级函数，是一个回调。
    if (setjmp(jem.setjmp_buffer)) {
        return 0;
    }

    //初始化jsc结构体
    jpeg_create_compress(&jcs);
    //打开输出文件 wb 可写  rb 可读
    FILE *f = fopen(outfilename, "wb");
    if (f == NULL) {
        return 0;
    }
    //设置结构体的文件路径，以及宽高
    jpeg_stdio_dest(&jcs, f);
    jcs.image_width = w;
    jcs.image_height = h;

    // /* TRUE=arithmetic coding, FALSE=Huffman */
    jcs.arith_code = false;
    int nComponent = 3;
    /* 颜色的组成 rgb，三个 # of color components in input image */
    jcs.input_components = nComponent;
    //设置颜色空间为rgb
    jcs.in_color_space = JCS_RGB;
    ///* Default parameter setup for compression */
    jpeg_set_defaults(&jcs);
    //是否采用哈弗曼
    jcs.optimize_coding = optimize;
    //设置质量
    jpeg_set_quality(&jcs, quality, true);
    //开始压缩
    jpeg_start_compress(&jcs, TRUE);

    JSAMPROW row_pointer[1];
    int row_stride;
    row_stride = jcs.image_width * nComponent;
    while (jcs.next_scanline < jcs.image_height) {
        //得到一行的首地址
        row_pointer[0] = &data[jcs.next_scanline * row_stride];
        jpeg_write_scanlines(&jcs, row_pointer, 1);
    }
    // 压缩结束
    jpeg_finish_compress(&jcs);
    // 销毁回收内存
    jpeg_destroy_compress(&jcs);
    //关闭文件
    fclose(f);
    return 1;
}


extern "C"
jint Java_com_demo_ndk_MainActivity_compressBitmap(JNIEnv *env, jclass thiz, jobject bitmap,
                                                   int quality, jstring fileNameStr) {


    //1、解析RGB

    //1.1 获取bitmap信息 宽 高  格式format
    AndroidBitmapInfo info;
    //java调用完方法往往返回的是对象，而C往往是参数，就是说调用AndroidBitmap_getInfo方法后
    //info里的信息发生改变
    AndroidBitmap_getInfo(env, bitmap, &info);

    //从地址获取值
    int bitmap_height = info.height;
    int bitmap_width = info.width;
    int bitmap_format = info.format;
    if (bitmap_format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        //
        return -1;
    }

    //1.2 解析保存到数据,数组中保存的是rgb -> YCbCr
    //1.2.1 锁定画布
    BYTE *pixel_color;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &pixel_color);

    //1.2.2 解析数据
    BYTE *data;
    BYTE r, g, b;
    // 申请一块内存 = 宽 * 高 *3    (rgb 为3，argb为4)
    // 数组指针指向的是数组首地址，因为这块内存要释放，所以需要保存。
    data = (BYTE *) malloc(bitmap_width * bitmap_height * 3);
    //声明一个指针BYTE 指向data的首地址
    BYTE *tempData;
    tempData = data;

    int i = 0;
    int j = 0;
    int color;
    for (i = 0; i < bitmap_height; ++i) {
        for (j = 0; j < bitmap_width; ++j) {
            //获取二位数组的每一个像素信息的首地址
            color = *((int *) pixel_color);
//            color = (int) *pixel_color;

            //把 rgb 取出来
            //右移，color假设是0x000f0000
            //转化为二进制，00000000 00001111 00000000 00000000 ，
            // 那么与之后得到的就是00000000 00001111 00000000 00000000
            //可用看出右移16位就说00000000 00000000 00000000 00001111 就把0f取出来了
            r = (color & 0x00FF0000) >> 16;
            g = (color & 0x0000FF00) >> 8;
            b = (color & 0x000000FF);

            //保存到data里面去
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;

            data = data + 3;
            //一个像素点包括argb 四个值，每+4就说取下一个像素点
            pixel_color += 4;
        }
    }

    //1.2.3 解锁画布
    AndroidBitmap_unlockPixels(env, bitmap);

    //1.2.4 获取图片地址
    char *file_name = (char *) env->GetStringUTFChars(fileNameStr, NULL);
    LOGE("file_name = %s",file_name);

    //2、 调用第三方提供好的方法
    //这里不能用data 而用的tempdata,由于data在for循环把地址改变了，
    //由于数据从data一开始的地址开始保存，那么data此时指向的地址已经跟数据无关了
    //那么应该传的是data一开始的地址，tempdata
    int result = generateJPEG(tempData, bitmap_width, bitmap_height, quality, file_name, true);

    //3、一定要回收内存
    free(tempData);
    env->ReleaseStringUTFChars(fileNameStr, file_name);

    //释放Bitmap,
    //3.1 获取bitmap的class
    jclass obj_clazz = env->GetObjectClass(bitmap);
    //3.2 通过bitmap的class  获取reycle方法的method_id
    jmethodID method_id = env->GetMethodID(obj_clazz,"recycle","()V");
    //3.3 调用方法释放Bitmap
    env->CallVoidMethod(obj_clazz,method_id);

    //4.返回结果
    if (result == 0) {
        return -1;
    }

    return 1;
}