package com.karthik.prettyruntime;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.karthik.pretty_annotation.Pretty;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import static com.karthik.prettyruntime.ConstantsKt.EXTRAHEADER;
import static com.karthik.prettyruntime.ConstantsKt.PRETTY_ASPECT_RULE;

@Aspect
public class PrettyAspect {

    @Pointcut(value = PRETTY_ASPECT_RULE)
    public void prettyPrintBundle() {
    }

    @Around("prettyPrintBundle()")
    public Object processBundleParams(ProceedingJoinPoint point) throws Throwable {
        Pretty pretty =  ((MethodSignature)point.getSignature()).getMethod().getAnnotation(Pretty.class);
        Bundle bundle = getBundleArgument(point.getArgs());
        if(pretty!=null && bundle!=null){
            PrettyPrinter.INSTANCE.printExtras(bundle,new String[]{EXTRAHEADER,pretty.headerName()});
        }
        return point.proceed();
    }

    @Nullable
    public Bundle getBundleArgument(Object[] args){
        for(Object arg:args){
            if(arg instanceof Bundle) return (Bundle) arg;
        }
        return null;
    }
}
