package org.voiddog.android.lib.base.config;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.List;

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
 * @since 2018-07-23 19:30
 */
public class SpConfig {
    public interface JSONParseSupplier {
        /**
         * 解析 T 成一个 List<T>
         * @param value
         * @param clazz
         * @param <T>
         * @return
         */
        <T> List<T> parseList(String value, Class<T> clazz);

        /**
         * 解析 T
         * @param value
         * @param clazz
         * @param <T>
         * @return
         */
        <T> T parseObject(String value, Class<T> clazz);

        /**
         * 转成 json string
         * @param value
         * @return
         */
        String toJSONString(Object value);
    }
    public static class Builder {
        private String defaultSpName;
        private JSONParseSupplier jsonParseSupplier;

        public String getDefaultSpName() {
            return defaultSpName;
        }

        public Builder setDefaultSpName(String defaultSpName) {
            this.defaultSpName = defaultSpName;
            return this;
        }

        public JSONParseSupplier getJsonParseSupplier() {
            return jsonParseSupplier;
        }

        public Builder setJsonParseSupplier(JSONParseSupplier jsonParseSupplier) {
            this.jsonParseSupplier = jsonParseSupplier;
            return this;
        }
    }

    @NonNull
    private final String defaultSpName;
    @NonNull
    private final JSONParseSupplier jsonParseSupplier;

    public SpConfig(Builder builder) {
        if (TextUtils.isEmpty(builder.defaultSpName)) {
            throw new IllegalArgumentException("default sp name should not be empty");
        }
        if (builder.jsonParseSupplier == null) {
            throw new IllegalArgumentException("json parse supplier should not be null");
        }
        defaultSpName = builder.defaultSpName;
        jsonParseSupplier = builder.jsonParseSupplier;
    }

    @NonNull
    public String getDefaultSpName() {
        return defaultSpName;
    }

    @NonNull
    public JSONParseSupplier getJsonParseSupplier() {
        return jsonParseSupplier;
    }
}
