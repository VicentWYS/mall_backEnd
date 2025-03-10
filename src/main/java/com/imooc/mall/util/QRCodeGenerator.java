package com.imooc.mall.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.imooc.mall.common.Constant;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 生成指定文本的二维码图片并保存
 */
public class QRCodeGenerator {

    /**
     * 生成二维码图片并保存到指定路径
     *
     * @param text     二维码中要编码的内容（例如 URL 或文本）
     * @param width    二维码图片的宽度（单位：像素）
     * @param height   二维码图片的高度（单位：像素）
     * @param filePath 生成的二维码图片保存的完整文件路径
     * @throws WriterException 二维码编码过程异常
     * @throws IOException     文件写入异常
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        // 创建 QRCodeWriter 对象，用于生成二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // 使用 QRCodeWriter 将文本编码为二维码的 BitMatrix（二维矩阵）
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // 通过 FileSystems 获取指定文件路径的 Path 对象
        Path path = FileSystems.getDefault().getPath(filePath);

        // 将 BitMatrix 转换为 PNG 格式的图片并写入到指定路径
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        try {
            generateQRCodeImage("asuka", 350, 350, "D:\\Me\\Project\\imooc_mall\\code\\images\\QRTest.png");
        } catch (WriterException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
