package apsh.backend.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLoadVo {
    Double deviceLoad;
    Double manpowerLoad;
    List<ResourceInResourceLoadVo> deviceLoadlist;
    List<ResourceInResourceLoadVo> manpowerLoadlist;
}