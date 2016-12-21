package com.im.server.util;

import com.im.server.core.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 协议加载器
 *
 * @author xieyang
 */
public class ProtocolHandllerLoader {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolHandllerLoader.class);

    private static Map<Integer, ProtocolHandler> handlers = new HashMap<Integer, ProtocolHandler>();

    static {
        logger.debug("加载protocol handlers...");
        try {
            Class cls = ProtocolHandler.class;
            List<String> packages = new ArrayList<String>();
            packages.add("com.im.protocol.handler");
            List<Class<?>> tocalClasses = new ArrayList<Class<?>>();
            for (String pk : packages) {
                tocalClasses.addAll(ClassUtil.getClasses(pk));
            }
            List<Class<?>> classes = new ArrayList<Class<?>>();
            for (Class<?> c : tocalClasses) {
                if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                    logger.debug("handler[ " + c.getName() + " ]");
                    ProtocolHandler instance = (ProtocolHandler) c.newInstance();
                    handlers.put(instance.getCmd(), instance);
                }
            }
            logger.debug("handler size[ " + handlers.size() + " ]");
            for (Class<?> c : classes) {
                System.out.println(c.getName());
            }

        } catch (Exception e) {
            logger.error("加载protocol handlers 失败:{}", e.toString());
        }
    }

    public static ProtocolHandler getProtocolHandler(Integer cmd) {
        return handlers.get(cmd);
    }


}
