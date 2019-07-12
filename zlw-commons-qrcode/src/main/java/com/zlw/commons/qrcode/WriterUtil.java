package com.zlw.commons.qrcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangliewei on 2017/7/14.
 */
public class WriterUtil {

    /**
     * 生成二维码2Dmatrix of bits
     * @param content 二维码信息
     * @param size 二维码大小
     * @return
     */
    public static BitMatrix encode(String content, Integer size) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            return new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
