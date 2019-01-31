package com.jiuqi.bi.bizview.util.erparse;

import java.io.InputStream;
import java.util.List;

import com.jiuqi.bi.bizview.util.erparse.entity.Root;

/**
 * 读取er图转换接口
 */
public interface IUmlParser {
    //读取表内容
    Root readER(InputStream fis) throws Exception;
}
