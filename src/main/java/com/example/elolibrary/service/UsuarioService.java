package com.example.elolibrary.service;

import com.example.elolibrary.dto.UsuarioDto;
import com.example.elolibrary.interfaces.CRUDService;
import com.example.elolibrary.interfaces.Dto;
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
import java.util.regex.Pattern;

@Service
public class UsuarioService implements CRUDService<Usuario, UsuarioDto> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String ENTIDADE_SERVICE="Usuario";

    @Override
    public Page<Dto<Usuario>> findAllByPage(Pageable pageable) {
        List<Dto<Usuario>> pageUser = usuarioRepository.findAllByAtivoTrue(pageable)
                .stream()
                .map(usr -> new UsuarioDto().wrap(usr))
                .toList();
        return new PageImpl<>(pageUser, pageable, pageUser.size());
    }

    @Override
    public Dto<Usuario> findById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND, ENTIDADE_SERVICE, id.toString()
                    )
            );
        }
        return new UsuarioDto().wrap(optUsuario.get());
    }

    @Override
    public void save(Usuario usuario) throws HttpClientErrorException.BadRequest {
        this.checkIfFieldIsEmpty(usuario.getNome(), "Nome");
        this.checkIfFieldIsEmpty(usuario.getSenha(), "Senha");
        this.checkIfFieldIsEmpty(usuario.getEmail(), "Email");
        this.checkIfFieldIsEmpty(usuario.getTelefone(), "Telefone");
        this.checkEmailPattern(usuario.getEmail());
        this.checkIfEmailIsBeingUsed(usuario.getEmail());
        this.checkTelefonePattern(usuario.getTelefone());
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
        this.usuarioRepository.saveAndFlush(usuario);
    }

    @Override
    public Dto<Usuario> update(Usuario usuario, Long usuarioId) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound {
        this.checkIfFieldIsEmpty(usuario.getNome(), "Nome");
        this.checkIfFieldIsEmpty(usuario.getSenha(), "Senha");
        this.checkEmailPattern(usuario.getEmail());
        this.checkTelefonePattern(usuario.getTelefone());
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
        this.checkIfNewTelefoneIsBeingUsed(usuario.getTelefone(), usuarioId);
        Usuario usuarioBase = optUsuario.get();
        usuarioBase.setSenha(usuario.getSenha());
        usuarioBase.setNome(usuario.getNome());
        usuarioBase.setTelefone(usuario.getTelefone());
        this.usuarioRepository.saveAndFlush(usuarioBase);
        return new UsuarioDto().wrap(usuarioBase);
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
        this.usuarioRepository.saveAndFlush(usuario);
    }

    private void checkIfFieldIsEmpty(String field, final String FIELD_NAME) throws HttpClientErrorException.BadRequest {
        if (ServiceUtils.isFalsyString(field)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.NULL_FIELD,
                            FIELD_NAME
                    )
            );
        }
    }


    private void checkEmailPattern(String emailUsuario) throws HttpClientErrorException.BadRequest {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        if (!emailPattern.matcher(emailUsuario).matches()) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.INVALID_EMAIL,
                            emailUsuario
                    )
            );
        }
    }


    //REFERENCIA: https://medium.com/@igorrozani/criando-uma-express%C3%A3o-regular-para-telefone-fef7a8f98828
    private void checkTelefonePattern(String telefoneUsuario) {
        final String TELEFONE_PATTERN = "(\\(?\\d{2}\\)?\\s)?(\\d{4,5}\\-\\d{4})";
        Pattern patternPhone = Pattern.compile(TELEFONE_PATTERN);
        if (!patternPhone.matcher(telefoneUsuario).matches()) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.INVALID_PHONE,
                            telefoneUsuario
                    )
            );
        }
    }

    private void checkDataCadastro(LocalDate dataCadastro) throws HttpClientErrorException.BadRequest {
        if (dataCadastro == null) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.NULL_FIELD,
                            "Data Cadastro"
                    )
            );
        }
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
        Optional<Usuario> emailUser = this.usuarioRepository.findByEmail(email);
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

    private void checkIfNewTelefoneIsBeingUsed(String newTelefone, Long userId) throws HttpClientErrorException.BadRequest {
        Optional<Usuario> optUsuarioTelefone = this.usuarioRepository.findByTelefone(newTelefone);
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

}
