package org.voiddog.android.lib.base.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

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
         * 解析 T
         * @param value
         * @param clazz
         * @param <T>
         * @return
         */
        @Nullable
        <T> T parseObject(@Nullable String value, @NonNull Class<T> clazz);

        /**
         * 转成 json string
         * @param value
         * @return
         */
        @Nullable
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

        public SpConfig build() {
            return new SpConfig(this);
        }
    }

    @NonNull
    private final String defaultSpName;
    @NonNull
    private final JSONParseSupplier jsonParseSupplier;

    private SpConfig(Builder builder) {
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
