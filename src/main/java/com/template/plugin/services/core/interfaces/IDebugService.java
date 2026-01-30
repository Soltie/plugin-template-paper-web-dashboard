package com.template.plugin.services.core.interfaces;

import com.template.plugin.services.IService;

public interface IDebugService extends IService {
    void debug(String category, String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    void error(String message, Throwable throwable);
}
