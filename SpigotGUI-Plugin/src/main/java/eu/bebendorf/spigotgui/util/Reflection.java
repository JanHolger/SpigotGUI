package eu.bebendorf.spigotgui.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static Reflection getCBClass(String name) {
        try {
            return new Reflection(Class.forName("org.bukkit.craftbukkit."+VERSION+"."+name));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static Reflection getNMSClass(String name) {
        try {
            return new Reflection(Class.forName("net.minecraft.server."+VERSION+"."+name));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    final Object value;

    public Reflection(Object value){
        this.value = value;
    }

    public Object get(){
        return value;
    }

    public Class<?> getAsClass(){
        return (Class<?>) get();
    }

    public Method getAsMethod(){
        return (Method) get();
    }

    public Constructor<?> getAsConstructor(){
        return (Constructor<?>) get();
    }

    public Field getAsField(){
        return (Field) get();
    }

    public String getAsString(){
        return (String) get();
    }

    public Reflection getField(String name) {
        try {
            Field field = getAsClass().getDeclaredField(name);
            field.setAccessible(true);
            return new Reflection(field);
        } catch (NoSuchFieldException e) {
            throw new ReflectionException(e);
        }
    }

    public Reflection getMethod(String name, Class<?>... parameterTypes) {
        try {
            Method method = getAsClass().getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return new Reflection(method);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    public Reflection getConstructor(Class<?>... parameterTypes) {
        try {
            Constructor<?> constructor = getAsClass().getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return new Reflection(constructor);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException(e);
        }
    }

    public Reflection call(Object instance, Object... parameters) {
        try {
            return new Reflection(getAsMethod().invoke(instance, parameters));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionException(e);
        }
    }

    public Reflection construct(Object... parameters){
        try {
            return new Reflection(getAsConstructor().newInstance(parameters));
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ReflectionException(e);
        }
    }

    public Reflection getValue(Object instance){
        try {
            return new Reflection(getAsField().get(instance));
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public void setValue(Object instance, Object value){
        try {
            getAsField().set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public static class ReflectionException extends RuntimeException {
        public ReflectionException(Exception parent){
            super(parent);
        }
    }

}
