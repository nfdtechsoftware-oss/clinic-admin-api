package com.nfdtech.clinic_admin_api.mapper;

import com.nfdtech.clinic_admin_api.domain.embeddable.Contato;
import com.nfdtech.clinic_admin_api.domain.embeddable.Endereco;
import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import com.nfdtech.clinic_admin_api.domain.valueobject.CPF;
import com.nfdtech.clinic_admin_api.domain.valueobject.Telefone;
import com.nfdtech.clinic_admin_api.dto.profissional.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {EspecialidadeMapper.class})
public interface ProfissionalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "stringToCPF")
    @Mapping(target = "endereco", source = "dto", qualifiedByName = "dtoToEndereco")
    @Mapping(target = "contato", source = "dto", qualifiedByName = "dtoToContato")
    @Mapping(target = "especialidades", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Profissional toEntity(ProfissionalRequestDTO dto);

    @Mapping(target = "cpf", source = "cpf.numero")
    @Mapping(target = "logradouro", source = "endereco.logradouro")
    @Mapping(target = "numero", source = "endereco.numero")
    @Mapping(target = "complemento", source = "endereco.complemento")
    @Mapping(target = "bairro", source = "endereco.bairro")
    @Mapping(target = "cidade", source = "endereco.cidade")
    @Mapping(target = "estado", source = "endereco.estado")
    @Mapping(target = "cep", source = "endereco.cep")
    @Mapping(target = "telefone", source = "contato.telefone.numero")
    @Mapping(target = "celular", source = "contato.celular.numero")
    @Mapping(target = "email", source = "contato.email")
    ProfissionalResponseDTO toResponseDTO(Profissional entity);

    List<ProfissionalResponseDTO> toResponseDTOList(List<Profissional> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "endereco", source = "dto", qualifiedByName = "updateDtoToEndereco")
    @Mapping(target = "contato", source = "dto", qualifiedByName = "updateDtoToContato")
    @Mapping(target = "especialidades", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ProfissionalUpdateDTO dto, @MappingTarget Profissional entity);

    // Métodos auxiliares customizados
    @Named("stringToCPF")
    default CPF stringToCPF(String cpf) {
        return CPF.of(cpf);
    }

    @Named("dtoToEndereco")
    default Endereco dtoToEndereco(ProfissionalRequestDTO dto) {
        return Endereco.builder()
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .build();
    }

    @Named("dtoToContato")
    default Contato dtoToContato(ProfissionalRequestDTO dto) {
        return Contato.builder()
                .telefone(dto.getTelefone() != null ? Telefone.of(dto.getTelefone()) : null)
                .celular(dto.getCelular() != null ? Telefone.of(dto.getCelular()) : null)
                .email(dto.getEmail())
                .build();
    }

    @Named("updateDtoToEndereco")
    default Endereco updateDtoToEndereco(ProfissionalUpdateDTO dto) {
        if (dto.getLogradouro() == null && dto.getNumero() == null &&
                dto.getBairro() == null && dto.getCidade() == null &&
                dto.getEstado() == null && dto.getCep() == null) {
            return null;
        }
        return Endereco.builder()
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .build();
    }

    @Named("updateDtoToContato")
    default Contato updateDtoToContato(ProfissionalUpdateDTO dto) {
        if (dto.getTelefone() == null && dto.getCelular() == null && dto.getEmail() == null) {
            return null;
        }
        return Contato.builder()
                .telefone(dto.getTelefone() != null ? Telefone.of(dto.getTelefone()) : null)
                .celular(dto.getCelular() != null ? Telefone.of(dto.getCelular()) : null)
                .email(dto.getEmail())
                .build();
    }

}
