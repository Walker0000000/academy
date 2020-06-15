package com.academy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * cn.com.doone.csb.system.util
 *
 * @author CKZ
 * @time 2017-03-14 09:16
 */
public class RandomNbr {
    /**
     *  登录信息 key 生成方法
     * @return key
     */
    public static  String getOlNbrByRandom(){
        String time=new SimpleDateFormat("MMddyymmssSSSHH") .format(new Date() );
        String olNbr=generateShortUuid()+time+generateShortUuid();
        return olNbr;
    }
    private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };
    private static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
}
