package apsh.backend.service;

import apsh.backend.dto.HumanDto;

import java.util.List;

public interface HumanService {

    void update(HumanDto humanDto);

    void delete(Integer id);

    void add(Integer id, HumanDto humanDto);

    List<HumanDto> getAll(Integer pageSize, Integer pageNum);
}
