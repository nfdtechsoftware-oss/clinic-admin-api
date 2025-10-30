package com.nfdtech.clinic_admin_api.mapper;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.dto.especialidade.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Especialidade toEntity(EspecialidadeRequestDTO dto);

    EspecialidadeResponseDTO toResponseDTO(Especialidade entity);

    List<EspecialidadeResponseDTO> toResponseDTOList(List<Especialidade> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(EspecialidadeUpdateDTO dto, @MappingTarget Especialidade entity);

}
