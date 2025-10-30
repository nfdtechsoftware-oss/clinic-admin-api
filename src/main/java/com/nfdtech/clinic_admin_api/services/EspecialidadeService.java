package com.nfdtech.clinic_admin_api.services;

import com.nfdtech.clinic_admin_api.domain.especialidade.Especialidade;
import com.nfdtech.clinic_admin_api.dto.especialidade.*;
import com.nfdtech.clinic_admin_api.exception.custom.BusinessException;
import com.nfdtech.clinic_admin_api.exception.custom.ResourceNotFoundException;
import com.nfdtech.clinic_admin_api.mapper.EspecialidadeMapper;
import com.nfdtech.clinic_admin_api.repositories.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final EspecialidadeMapper especialidadeMapper;

    @Transactional(readOnly = true)
    public List<EspecialidadeResponseDTO> listarTodas() {
        List<Especialidade> especialidades = especialidadeRepository.findByAtivoTrue();
        return especialidadeMapper.toResponseDTOList(especialidades);
    }

    @Transactional(readOnly = true)
    public EspecialidadeResponseDTO buscarPorId(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade", "id", id));
        return especialidadeMapper.toResponseDTO(especialidade);
    }

    @Transactional
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO requestDTO) {
        // Validar se já existe especialidade com o mesmo nome
        if (especialidadeRepository.existsByNome(requestDTO.getNome())) {
            throw new BusinessException("Já existe uma especialidade com o nome: " + requestDTO.getNome());
        }

        Especialidade especialidade = especialidadeMapper.toEntity(requestDTO);
        Especialidade saved = especialidadeRepository.save(especialidade);
        return especialidadeMapper.toResponseDTO(saved);
    }

    @Transactional
    public EspecialidadeResponseDTO atualizar(Long id, EspecialidadeUpdateDTO updateDTO) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade", "id", id));

        // Validar se o novo nome já existe (se o nome foi alterado)
        if (updateDTO.getNome() != null && !updateDTO.getNome().equals(especialidade.getNome())) {
            if (especialidadeRepository.existsByNome(updateDTO.getNome())) {
                throw new BusinessException("Já existe uma especialidade com o nome: " + updateDTO.getNome());
            }
        }

        especialidadeMapper.updateEntityFromDTO(updateDTO, especialidade);
        Especialidade updated = especialidadeRepository.save(especialidade);
        return especialidadeMapper.toResponseDTO(updated);
    }

    @Transactional
    public void desativar(Long id) {
        Especialidade especialidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade", "id", id));
        especialidade.setAtivo(false);
        especialidadeRepository.save(especialidade);
    }

    @Transactional
    public void deletar(Long id) {
        if (!especialidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Especialidade", "id", id);
        }
        especialidadeRepository.deleteById(id);
    }

}
