package org.tinygroup.service;

import org.tinygroup.event.Event;
import org.tinygroup.exceptionhandler.ExceptionHandler;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.exception.ServiceRunException;

import java.lang.reflect.InvocationTargetException;

public class ServiceRunExceptionHandler implements ExceptionHandler<ServiceRunException>{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRunExceptionHandler.class);
    @Override
    public void handle(ServiceRunException e, Event event) {
        if(e==null){
            LOGGER.errorMessage("ServiceRunException Param is NULL");
        }
        if(e.getCause() instanceof InvocationTargetException){
            InvocationTargetException invocationTargetException = (InvocationTargetException) e.getCause();
            Throwable t = invocationTargetException.getTargetException();
            if(t!=null && t instanceof  RuntimeException){
                throw (RuntimeException)t;
            }
        }
    }
}
