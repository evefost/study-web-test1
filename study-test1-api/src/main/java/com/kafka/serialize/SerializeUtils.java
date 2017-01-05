//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kafka.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SerializeUtils {
    private static final Logger logger = LoggerFactory.getLogger(SerializeUtils.class);

    public SerializeUtils() {
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = null;

        try {
            objectOut = new ObjectOutputStream(output);
            objectOut.writeObject(obj);
        } catch (IOException var7) {
            logger.error("[kafka client-->序列化出现异常！]", var7);
        } finally {
            close(output, objectOut);
        }

        return output.toByteArray();
    }

    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ObjectInputStream objectIn = null;
        Object obj = null;

        try {
            objectIn = new ObjectInputStream(input);
            obj = objectIn.readObject();
        } catch (IOException var9) {
            logger.error("[kafka client-->反序列化出现异常！]", var9);
        } catch (ClassNotFoundException var10) {
            logger.error("[kafka client-->序列化出现异常-->找不到该类！]", var10);
        } finally {
            close(input, objectIn);
        }

        return obj;
    }

    private static void close(ByteArrayInputStream input, ObjectInputStream objectIn) {
        try {
            if(objectIn != null) {
                objectIn.close();
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            }

        }

    }

    private static void close(ByteArrayOutputStream output, ObjectOutputStream objectOut) {
        try {
            if(objectOut != null) {
                objectOut.close();
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        } finally {
            try {
                if(output != null) {
                    output.close();
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            }

        }

    }
}
