package apsh.backend.service;

import apsh.backend.dto.ResourceDto;
import apsh.backend.vo.ResourceLoadVo;
;

import java.util.Date;
import java.util.List;

public interface ResourceService {
    ResourceLoadVo getResourceLoad(Date date, Integer pageSize, Integer pageNum);

    List<ResourceDto> getResourceUse(Date date, Integer pageSize, Integer pageNum);
}
