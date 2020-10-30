package apsh.backend.service;

import apsh.backend.vo.ResourceLoadVo;
import apsh.backend.vo.ResourceUseVo;

import java.util.Date;
import java.util.List;

public interface ResourceService {
    ResourceLoadVo getResourceLoad(Date date);

    List<ResourceUseVo> getResourceUse(Date date);
}
