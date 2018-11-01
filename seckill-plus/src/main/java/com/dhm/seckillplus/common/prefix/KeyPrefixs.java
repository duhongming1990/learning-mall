package com.dhm.seckillplus.common.prefix;

/**
 * @Author duhongming
 * @Email 19919902414@189.cn
 * @Date 2018/11/1 11:24
 */
public class KeyPrefixs {
    private static final String USER = "user-center:user:";
    private static final String SECKILL_USER = "user-center:seckill-user:";
    private static final String GOODS = "goods-center:goods:";
    private static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public enum UserKey {
        ID(new BasePrefix("id")),
        NAME(new BasePrefix("name")),
        TOKEN(new BasePrefix(TOKEN_EXPIRE, "token"));

        private BasePrefix basePrefix;

        UserKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(USER + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }

    public enum SeckillUserKey {
        TOKEN(new BasePrefix(TOKEN_EXPIRE, "token")),
        ID(new BasePrefix("id"));

        private BasePrefix basePrefix;

        SeckillUserKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(SECKILL_USER + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }


    public enum GoodsKey {
        GOODS_LIST(new BasePrefix(60, "goods-list")),
        GOODS_DETIAL(new BasePrefix(60, "goods-detial")),
        SECKILL_GOODS_STOCK(new BasePrefix("seckill-goods-stock"));

        private BasePrefix basePrefix;

        GoodsKey(BasePrefix basePrefix) {
            basePrefix.setPrefix(GOODS + basePrefix.getPrefix());
            this.basePrefix = basePrefix;
        }

        public BasePrefix getBasePrefix() {
            return basePrefix;
        }

        public void setBasePrefix(BasePrefix basePrefix) {
            this.basePrefix = basePrefix;
        }
    }

}