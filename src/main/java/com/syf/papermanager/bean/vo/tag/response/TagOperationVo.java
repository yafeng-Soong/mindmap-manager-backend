package com.syf.papermanager.bean.vo.tag.response;

import com.syf.papermanager.bean.dto.TagOperationDTO;
import com.syf.papermanager.bean.enums.OperationType;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag.response
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/3 16:05
 */
@Data
public class TagOperationVo {
    private String operatorName;
    private String avatar;
    private String operation;
    private String tagName;
    private Date createTime;
    public TagOperationVo(TagOperationDTO operationDTO) {
        BeanUtils.copyProperties(operationDTO, this);
        this.operatorName = operationDTO.getUsername();
        this.tagName = operationDTO.getName();
        this.operation = OperationType.of(operationDTO.getType()).getDescription();
    }
}
