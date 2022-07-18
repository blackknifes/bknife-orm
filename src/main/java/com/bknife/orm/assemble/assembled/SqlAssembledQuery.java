package com.bknife.orm.assemble.assembled;

import java.sql.ResultSet;

public interface SqlAssembledQuery<T> extends SqlAssembled<T> {

        /**
         * 获取对应类
         * 
         * @return
         */
        public Class<T> getType();

        /**
         * 从结果集创建对象
         * 
         * @param resultSet
         * @return
         */
        public T createFromResultSet(ResultSet resultSet) throws Exception;
}
