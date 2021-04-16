package org.voiddog.android.lib.base.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.voiddog.android.lib.base.utils.ParseUtil;
import org.voiddog.android.lib.base.utils.TypeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ┏┛ ┻━━━━━┛ ┻┓
 * ┃　　　　　　 ┃
 * ┃　　　━　　　┃
 * ┃　┳┛　  ┗┳　┃
 * ┃　　　　　　 ┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　 ┃
 * ┗━┓　　　┏━━━┛
 * * ┃　　　┃   神兽保佑
 * * ┃　　　┃   代码无BUG！
 * * ┃　　　┗━━━━━━━━━┓
 * * ┃　　　　　　　    ┣┓
 * * ┃　　　　         ┏┛
 * * ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
 * * * ┃ ┫ ┫   ┃ ┫ ┫
 * * * ┗━┻━┛   ┗━┻━┛
 *
 * @author qigengxin
 * @since 2017-11-12 15:00
 */

public enum SPManager {
    INSTANCE;

    private Map<Class<?>, Object> servicesMap = new ConcurrentHashMap<>();
    private SharedPreferences sp;
    private SpConfig spConfig;
    private Context appContext;
    private boolean isInit = false;

    public synchronized void init(@NonNull Context appContext, SpConfig spConfig) {
        if (isInit) {
            return;
        }
        appContext = appContext instanceof Application ? appContext : appContext.getApplicationContext();
        isInit = true;
        sp = appContext.getSharedPreferences(spConfig.getDefaultSpName(), Context.MODE_PRIVATE);
        this.appContext = appContext;
        this.spConfig = spConfig;
    }

    /**
     * 清理 sp 数据
     */
    public void clean() {
        clean(null);
    }

    /**
     * 清理指定 spName 的 shared preference
     * @param spName 需要清理的 shared preference 的名字
     */
    public void clean(@Nullable String spName) {
        SharedPreferences sp;
        if (TextUtils.isEmpty(spName)) {
            sp = this.sp;
        } else {
            sp = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
        sp.edit().clear().apply();
    }

    public <T extends ISPService> T getService(Class<T> clazz){
        checkInit();
        Object o = servicesMap.get(clazz);
        if (o == null){
            // create new
            o = createNewService(clazz);
            servicesMap.put(clazz, o);
        }
        return (T) o;
    }

    private <T extends ISPService> T createNewService(final Class<T> clazz){
        checkInit();
        Object res = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                final SPApi api = method.getAnnotation(SPApi.class);

                if (api == null){
                    return null;
                }
                final String spName = api.spName();
                final String spKey = api.spKey();

                // 校验数据
                if (TextUtils.isEmpty(spKey)){
                    Log.e(SPManager.class.getSimpleName(), "lose key from: " + o.getClass().getSimpleName() + "." + method.getName());
                    return null;
                }

                Type returnType = method.getGenericReturnType();
                Class<?> returnRawType = TypeUtil.getRawType(returnType);
                Object ret = null;

                do {
                    // get data
                    if (returnRawType != void.class && !clazz.equals(returnRawType)) {
                        Object defaultValue = null;
                        if (args != null && args.length > 0){
                            defaultValue = args[0];
                        }
                        if (!TextUtils.isEmpty(spKey)) {
                            // 有 sp 配置
                            SharedPreferences sp = TextUtils.isEmpty(spName) ? SPManager.this.sp :
                                    appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);

                            if (returnRawType == String.class) {
                                ret = sp.getString(spKey, null);
                            } else if (returnRawType == Integer.class || returnRawType == int.class) {
                                ret = sp.getInt(spKey, (defaultValue instanceof Integer) ? (Integer)defaultValue : 0);
                            } else if (returnRawType == Long.class || returnRawType == long.class) {
                                ret = sp.getLong(spKey, (defaultValue instanceof Long) ? (Long)defaultValue : 0);
                            } else if (returnRawType == Float.class || returnRawType == float.class
                                    || returnRawType == Double.class ||returnRawType == double.class) {
                                // Double a = 1; Float b = (Float) a;
                                ret = sp.getFloat(spKey, (defaultValue instanceof Float || defaultValue instanceof Double) ? (Float)defaultValue : 0);
                            } else if (returnRawType == Boolean.class || returnRawType == boolean.class) {
                                ret = sp.getBoolean(spKey, (defaultValue instanceof Boolean) ? (Boolean)defaultValue : false);
                            } else {
                                String value = sp.getString(spKey, null);
                                if (!TextUtils.isEmpty(value)) {
                                    ret = createNewInstance(returnType, returnRawType, value);
                                }
                            }
                        }
                        if (ret == null && defaultValue != null){
                            ret = defaultValue;
                        }
                    } else if (!TextUtils.isEmpty(spKey)){
                        // 写入数据
                        if (args != null && args.length > 0) {
                            // 有 sp 配置
                            SharedPreferences sp = TextUtils.isEmpty(spName) ? SPManager.this.sp :
                                    appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);

                            Class<?> paramRawType = TypeUtil.getRawType(args[0].getClass());
                            SharedPreferences.Editor editor = sp.edit();
                            Object param = args[0];
                            if (paramRawType == String.class) {
                                editor.putString(spKey, (String) param);
                            } else if (paramRawType == Integer.class || paramRawType == int.class) {
                                editor.putInt(spKey, param == null ? 0 : (int) param);
                            } else if (paramRawType == Long.class || paramRawType == long.class) {
                                editor.putLong(spKey, param == null ? 0 : (long) param);
                            } else if (paramRawType == Float.class || paramRawType == float.class) {
                                editor.putFloat(spKey, param == null ? 0 : (float) param);
                            } else if (paramRawType == Double.class || paramRawType == double.class) {
                                editor.putFloat(spKey, param == null ? 0 : (float) param);
                            } else if (paramRawType == Boolean.class || paramRawType == boolean.class) {
                                editor.putBoolean(spKey, param != null && (boolean) param);
                            } else {
                                editor.putString(spKey, param == null ? null : spConfig.getJsonParseSupplier().toJSONString(param));
                            }
                            editor.apply();
                        }
                        if (clazz.equals(returnRawType)) {
                            ret = o;
                        }
                    }
                } while (false);

                if (!api.nullable() && returnRawType != void.class && ret == null){
                    if (returnRawType == List.class) {
                        return new ArrayList<>();
                    }
                    // 反射
                    try {
                        return returnRawType.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return ret;
            }
        });
        return (T) res;
    }

    private <T> T createNewInstance(Type type, Class<T> clazz, String value) {
        if (clazz == String.class){
            return (T) value;
        } else if (clazz == Integer.class || clazz == int.class){
            Integer ret = ParseUtil.parseInt(value, 0);
            return (T) ret;
        } else if(clazz == Long.class || clazz == long.class){
            Long ret = ParseUtil.parseLong(value, 0);
            return (T) ret;
        } else if (clazz == Float.class || clazz == float.class){
            Float ret = ParseUtil.parseFloat(value, 0);
            return (T) ret;
        } else if (clazz == Double.class || clazz == double.class){
            Double ret = ParseUtil.parseDouble(value, 0);
            return (T) ret;
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            Boolean ret = ParseUtil.parseBoolean(value, false);
            return (T) ret;
        } else if (value == null){
            return null;
        }
        try {
            return spConfig.getJsonParseSupplier().parseObject(value, clazz);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private synchronized void checkInit() {
        if (isInit) {
            return;
        }
        throw new IllegalStateException("SPManager must init first");
    }
}
