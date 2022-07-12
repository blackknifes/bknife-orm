package com.bknife.orm.assemble;

import java.lang.reflect.Field;

import com.bknife.orm.annotion.Column;

/**
 * 列信息
 * 列信息具有属性：名称,类型,长度,小数点,符号,唯一,主键,是否允许空，注释,自增长,默认值,字符集,排序规则,来源表类,来源表名,sql语句,check约束
 */
public interface SqlColumnInfo extends SqlNamed {
    public static interface Builder {
        public SqlColumnInfo build();
    }

    public static class ExistColumnBuilder implements Builder {
        private SqlColumnInfo columnInfo;

        public ExistColumnBuilder(SqlColumnInfo columnInfo) {
            this.columnInfo = columnInfo;
        }

        @Override
        public SqlColumnInfo build() {
            return columnInfo;
        }
    }

    /**
     * 获取来源表名
     * 
     * @return
     */
    public String getTableName();

    /**
     * 获取成员
     * 
     * @return
     */
    public Field getField();

    /**
     * 获取列注解
     * 
     * @return
     */
    public Column getAnnotation();
}
