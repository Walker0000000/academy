package com.academy.service.system;

import java.util.List;

/**
 *
 * @description 通用服务类 SeqCode 表数据服务层接口
 * @author: shuZhiMing
 */

public interface SysCodeService {
    /**
     * @param type 编码的类型：例如warehouseCode
     * @return outCode 返回的编码值
     * ***/
    String getSysCode(String type);

    List<String> getSysCodeList(String type, Integer size);


}