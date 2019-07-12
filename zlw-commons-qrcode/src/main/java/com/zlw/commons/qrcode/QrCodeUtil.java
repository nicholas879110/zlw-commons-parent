package com.zlw.commons.qrcode;

import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * 二维码工具
 * Created by zhangliewei on 2017/7/14.
 */
public class QrCodeUtil {

    private static final int BLACK = Color.black.getRGB();
    private static final int WHITE = Color.WHITE.getRGB();
    private static final int DEFAULT_QR_SIZE = 183;
    private static final String DEFAULT_QR_FORMAT = "png";


    public static BufferedImage toBufferedImage(String content, int size) {
        if (size <= 0) {
            size = DEFAULT_QR_SIZE;
        }
        BitMatrix bitMatrix = WriterUtil.encode(content, size);
        return MatrixToImageWriter.toBufferedImage(bitMatrix, new MatrixToImageConfig());
    }

    public static BufferedImage toBufferedImage(String content, int size,Image insertImg) throws IOException{
        if (size <= 0) {
            size = DEFAULT_QR_SIZE;
        }
        if (insertImg==null){
            return toBufferedImage(content,size);
        }
        BitMatrix bitMatrix = WriterUtil.encode(content, size);
        BufferedImage image=insertImage(bitMatrix, insertImg);
        return image;
    }

    public static byte[] toBytes(String content, int size) throws IOException {
        BufferedImage image=toBufferedImage(content,size);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, DEFAULT_QR_FORMAT, baos);
        return baos.toByteArray();
    }

    public static byte[] toBytes(String content, int size, Image insertImg) throws IOException {
        if (insertImg==null){
            return toBytes(content,size);
        }
        BitMatrix bitMatrix = WriterUtil.encode(content, size);
        BufferedImage image=insertImage(bitMatrix, insertImg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, DEFAULT_QR_FORMAT, baos);
        return baos.toByteArray();
    }


    public static void writeToStream(String content, int size, OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(content, size);
        if (!ImageIO.write(image, DEFAULT_QR_FORMAT, stream)) {
            throw new IOException("Could not write an image of format");
        }
    }

    public static void writeToStream(String content, int size, OutputStream stream,Image insertImg) throws IOException {
        if (insertImg==null){
            writeToStream(content,size,stream);
            return;
        }
        BitMatrix bitMatrix = WriterUtil.encode(content, size);
        BufferedImage image=insertImage(bitMatrix, insertImg);
        if (!ImageIO.write(image, DEFAULT_QR_FORMAT, stream)) {
            throw new IOException("Could not write an image of format");
        }
    }

    public static void writeToPath(String content, int size, Path file) throws IOException {
        BitMatrix bitMatrix = WriterUtil.encode(content, size);
        MatrixToImageWriter.writeToPath(bitMatrix, DEFAULT_QR_FORMAT, file, new MatrixToImageConfig());
    }


    private static BufferedImage insertImage(BitMatrix bitMatrix, Image insertImg) throws IOException {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, bitMatrix.get(i, j) ? BLACK : WHITE);
            }
        }

        int insertImgWidth = insertImg.getWidth(null);
        int insertImgHeight = insertImg.getHeight(null);

        insertImgWidth = insertImgWidth > width / 6 ? width / 6 : width; // logo设为二维码的六分之一大小
        insertImgHeight = insertImgHeight > height / 6 ? height / 6 : height;
        Graphics2D graph = image.createGraphics();
        int x = (width - insertImgWidth) / 2;
        int y = (height - insertImgHeight) / 2;
        graph.drawImage(insertImg, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
        return image;
    }

}
