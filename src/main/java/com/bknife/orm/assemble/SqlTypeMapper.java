package com.bknife.orm.assemble;

import com.bknife.orm.annotion.Column.Type;

public interface SqlTypeMapper {
    /**
     * 从type获取类型
     * 
     * @param type
     * @return
     */
    public String toString(Type type, int length, int dot);
}
