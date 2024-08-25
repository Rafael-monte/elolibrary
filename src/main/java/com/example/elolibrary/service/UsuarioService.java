package com.example.elolibrary.service;

import com.example.elolibrary.dto.output.UsuarioOutputDto;
import com.example.elolibrary.interfaces.CRUDService;
import com.example.elolibrary.interfaces.OutputDto;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.repository.UsuarioRepository;
import com.example.elolibrary.util.DateUtils;
import com.example.elolibrary.util.MessageTemplate;
import com.example.elolibrary.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService implements CRUDService<Usuario, UsuarioOutputDto> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String ENTIDADE_SERVICE="Usuario";

    @Override
    public Page<OutputDto<Usuario>> findAllByPage(Pageable pageable) {
        List<OutputDto<Usuario>> pageUser = usuarioRepository.findAllByAtivoTrue(pageable)
                .stream()
                .map(usr -> new UsuarioOutputDto().wrap(usr))
                .toList();
        return new PageImpl<>(pageUser, pageable, pageUser.size());
    }

    @Override
    public OutputDto<Usuario> findById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND, ENTIDADE_SERVICE, id.toString()
                    )
            );
        }
        return new UsuarioOutputDto().wrap(optUsuario.get());
    }

    @Override
    public void save(Usuario usuario) throws HttpClientErrorException.BadRequest {
        String telefoneFormatado = formatTelefone(usuario.getTelefone());
        this.checkIfEmailIsBeingUsed(usuario.getEmail());
        this.checkIfTelefoneIsBeingUsed(telefoneFormatado);
        usuario.setDataCadastro(LocalDate.now());
        try {
            usuario.setSenha(
                    ServiceUtils.encryptPassword(usuario.getSenha())
            );
        } catch (NoSuchAlgorithmException e) {
            throw new HttpClientErrorException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    ServiceUtils.createExceptionMessage(MessageTemplate.COULD_NOT_ENCRYPT_PASSWORD)
            );
        }
        usuario.setRole(Role.USER);
        usuario.setAtivo(true);
        usuario.setTelefone(telefoneFormatado);
        this.usuarioRepository.saveAndFlush(usuario);
    }

    @Override
    public OutputDto<Usuario> update(Usuario usuario, Long usuarioId) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound {
        String telefoneFormatado = formatTelefone(usuario.getTelefone());
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(usuarioId);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                ServiceUtils.createExceptionMessage(
                        MessageTemplate.ENTITY_NOT_FOUND,
                        ENTIDADE_SERVICE,
                        usuarioId.toString()
                )
            );
        }

        if (usuario.getDataCadastro() != null) {
            this.checkDataCadastro(usuario.getDataCadastro());
        }
        try {
            usuario.setSenha(
                    ServiceUtils.encryptPassword(usuario.getSenha())
            );
        } catch (NoSuchAlgorithmException e) {
            throw new HttpClientErrorException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    ServiceUtils.createExceptionMessage(MessageTemplate.COULD_NOT_ENCRYPT_PASSWORD)
            );
        }
        this.checkIfNewTelefoneIsBeingUsed(telefoneFormatado, usuarioId);
        Usuario usuarioBase = optUsuario.get();
        usuarioBase.setSenha(usuario.getSenha());
        usuarioBase.setNome(usuario.getNome());
        usuarioBase.setTelefone(telefoneFormatado);
        this.usuarioRepository.saveAndFlush(usuarioBase);
        return new UsuarioOutputDto().wrap(usuarioBase);
    }

    @Override
    public void deleteById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND,
                            ENTIDADE_SERVICE,
                            id.toString()
                    )
            );
        }
        Usuario usuario = optUsuario.get();
        usuario.setAtivo(false);
        usuario.setEmail(null);
        usuario.setTelefone(null);
        this.usuarioRepository.saveAndFlush(usuario);
    }

    private void checkDataCadastro(LocalDate dataCadastro) throws HttpClientErrorException.BadRequest {
        if (dataCadastro.isAfter(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.REGISTER_DATE_AHEAD_OF_CURRENT_DATE,
                            DateUtils.dateToDDMMYYYY(dataCadastro)
                    )
            );
        }
    }

    private void checkIfEmailIsBeingUsed(String email) throws HttpClientErrorException.BadRequest {
        Optional<Usuario> emailUser = this.usuarioRepository.findByEmailAndAtivoTrue(email);
        if (emailUser.isPresent()) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.EMAIL_ALREADY_IN_USE,
                            email
                    )
            );
        }
    }

    private void checkIfTelefoneIsBeingUsed(String telefone) throws HttpClientErrorException.BadRequest {
        Optional<Usuario> optUsuarioTelefone = this.usuarioRepository.findByTelefoneAndAtivoTrue(telefone);
        if (optUsuarioTelefone.isPresent()) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.PHONE_ALREADY_IN_USE,
                            telefone
                    )
            );
        }
    }

    private void checkIfNewTelefoneIsBeingUsed(String newTelefone, Long userId) throws HttpClientErrorException.BadRequest {
        Optional<Usuario> optUsuarioTelefone = this.usuarioRepository.findByTelefoneAndAtivoTrue(newTelefone);
        if (optUsuarioTelefone.isPresent() && !Objects.equals(optUsuarioTelefone.get().getId(), userId)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.PHONE_ALREADY_IN_USE,
                            newTelefone
                    )
            );
        }
    }

    private String formatTelefone(String telefone) {
        return telefone.replace("(", "")
                .replace(")", "")
                .replace("+55", "")
                .replace("-", "")
                .replace(" ", "");
    }

}
