package org.tinygroup.flowcomponent.exception;

import org.tinygroup.exception.BaseRuntimeException;

@SuppressWarnings("serial")
public class FlowComponentException extends BaseRuntimeException {

    public FlowComponentException(Throwable e) {
        super(e);
    }

    public FlowComponentException(String code, Object... args) {
        super(code, args);
    }
}
