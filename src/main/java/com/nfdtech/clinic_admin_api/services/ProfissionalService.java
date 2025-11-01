package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.domain.profissional.Profissional;
import com.nfdtech.clinic_admin_api.dto.profissional.*;
import com.nfdtech.clinic_admin_api.exception.custom.BusinessException;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.mapper.ProfissionalMapper;
import com.nfdtech.clinic_admin_api.repositories.EspecialidadeRepository;
import com.nfdtech.clinic_admin_api.repositories.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ProfissionalMapper profissionalMapper;

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> listarTodos() {
        List<Profissional> profissionais = profissionalRepository.findByAtivoTrue();
        return profissionalMapper.toResponseDTOList(profissionais);
    }

    @Transactional(readOnly = true)
    public ProfissionalResponseDTO buscarPorId(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "id", id));
        return profissionalMapper.toResponseDTO(profissional);
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> buscarPorEspecialidade(Long especialidadeId) {
        List<Profissional> profissionais = profissionalRepository.findByEspecialidadeId(especialidadeId);
        return profissionalMapper.toResponseDTOList(profissionais);
    }

    @Transactional(readOnly = true)
    public List<ProfissionalResponseDTO> buscarPorNome(String nome) {
        List<Profissional> profissionais = profissionalRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
        return profissionalMapper.toResponseDTOList(profissionais);
    }

    @Transactional
    public ProfissionalResponseDTO criar(ProfissionalRequestDTO requestDTO) {
        // Validar se já existe profissional com o mesmo registro de classe
        if (profissionalRepository.existsByRegistroClasse(requestDTO.getRegistroClasse())) {
            throw new BusinessException("Já existe um profissional com o registro de classe: " +
                    requestDTO.getRegistroClasse());
        }

        Profissional profissional = profissionalMapper.toEntity(requestDTO);

        // Adicionar especialidades
        Set<Especialidade> especialidades = buscarEspecialidades(requestDTO.getEspecialidadesIds());
        profissional.setEspecialidades(especialidades);

        Profissional saved = profissionalRepository.save(profissional);
        return profissionalMapper.toResponseDTO(saved);
    }

    @Transactional
    public ProfissionalResponseDTO atualizar(Long id, ProfissionalUpdateDTO updateDTO) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "id", id));

        // Validar se o novo registro de classe já existe
        if (updateDTO.getRegistroClasse() != null &&
                !updateDTO.getRegistroClasse().equals(profissional.getRegistroClasse())) {
            if (profissionalRepository.existsByRegistroClasse(updateDTO.getRegistroClasse())) {
                throw new BusinessException("Já existe um profissional com o registro de classe: " +
                        updateDTO.getRegistroClasse());
            }
        }

        profissionalMapper.updateEntityFromDTO(updateDTO, profissional);

        // Atualizar especialidades se fornecido
        if (updateDTO.getEspecialidadesIds() != null && !updateDTO.getEspecialidadesIds().isEmpty()) {
            Set<Especialidade> especialidades = buscarEspecialidades(updateDTO.getEspecialidadesIds());
            profissional.setEspecialidades(especialidades);
        }

        Profissional updated = profissionalRepository.save(profissional);
        return profissionalMapper.toResponseDTO(updated);
    }

    @Transactional
    public void desativar(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profissional", "id", id));
        profissional.setAtivo(false);
        profissionalRepository.save(profissional);
    }

    private Set<Especialidade> buscarEspecialidades(Set<Long> especialidadesIds) {
        Set<Especialidade> especialidades = new HashSet<>();
        for (Long especialidadeId : especialidadesIds) {
            Especialidade especialidade = especialidadeRepository.findById(especialidadeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidade", "id", especialidadeId));
            especialidades.add(especialidade);
        }
        return especialidades;
    }

}
