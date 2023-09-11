package com.android.liba.util;

import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflexUtil {

    /**
     * 获取某类中的泛型
     *
     * @param c      类
     * @param pIndex 第几个泛型下标
     * @return 泛型类
     */
    public static <T> Class<T> getParadigmClass(Class<?> c, int pIndex) {
        try {
            Type type = c.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                return (Class<T>) actualTypeArguments[pIndex];
            } else {
                Class<?> superclass = c.getSuperclass();
                if (superclass == null) return null;
                return getParadigmClass(superclass, pIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射调用一个参数的静态方法
     *
     * @param c             拥有静态方法的类对象
     * @param methodName    静态方法方法名
     * @param parameterType 参数类名，如View.class
     * @param param         参数值 如 View v
     * @return 返回静态方法返回值
     */
    public static <T> T doStaticMethod(Class<?> c, String methodName, Class<?> parameterType, Object param) {
        try {
            Method method = c.getMethod(methodName, parameterType);
            return (T) method.invoke(null, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 反射调用 ViewBinding 继承类的 bind(View view) 方法
     *
     * @return 返回 ViewBinding 实际对象
     */
    public static <T> T doViewBindingBind(Class<? extends ViewBinding> c, View v) {
        return doStaticMethod(c, "bind", View.class, v);
    }

    /**
     * 反射调用 ViewBinding 继承类的 inflate(@NonNull LayoutInflater inflater) 方法
     *
     * @return 返回 ViewBinding 实际对象
     */
    public static <T> T doViewBindingInflate(Class<? extends ViewBinding> c, LayoutInflater inflater) {
        return doStaticMethod(c, "inflate", LayoutInflater.class, inflater);
    }
} 
