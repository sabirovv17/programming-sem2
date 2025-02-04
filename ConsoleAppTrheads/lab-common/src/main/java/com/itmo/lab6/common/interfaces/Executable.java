package com.itmo.lab6.common.interfaces;

import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.Request;

public interface Executable {
    Response execute(Request request);
}
