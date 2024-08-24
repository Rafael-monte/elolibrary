package com.example.elolibrary.service;

import com.example.elolibrary.interfaces.CRUDService;
import com.example.elolibrary.model.Usuario;
import com.example.elolibrary.model.enumeration.Role;
import com.example.elolibrary.repository.UsuarioRepository;
import com.example.elolibrary.util.MessageTemplate;
import com.example.elolibrary.util.ServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService implements CRUDService<Usuario> {

    @Autowired
    private UsuarioRepository usuarioRepository;


    private static final String ENTIDADE_SERVICE="Usuario";


    @Override
    public List<Usuario> findAllByPage(Pageable pageable) {
        return this.usuarioRepository.findAllByAtivoTrue(pageable);
    }

    @Override
    public Usuario findById(Long id) throws HttpClientErrorException.NotFound {
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(id);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                    HttpStatus.NOT_FOUND,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.ENTITY_NOT_FOUND, ENTIDADE_SERVICE, id
                    )
            );
        }
        return optUsuario.get();
    }

    @Override
    public void save(Usuario usuario) throws HttpClientErrorException.BadRequest {
        this.checkIfFieldsAreNull(usuario);
        this.checkEmailPattern(usuario.getEmail());
        this.checkTelefonePattern(usuario.getTelefone());
        usuario.setDataCadastro(LocalDate.now());
        usuario.setRole(Role.USER);
        usuario.setAtivo(true);
        this.usuarioRepository.saveAndFlush(usuario);
    }

    @Override
    public Usuario update(Usuario usuario, Long usuarioId) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound {
        checkIfFieldsAreNull(usuario);
        this.checkIfFieldsAreNull(usuario);
        this.checkEmailPattern(usuario.getEmail());
        this.checkTelefonePattern(usuario.getTelefone());
        Optional<Usuario> optUsuario = this.usuarioRepository.findById(usuarioId);
        if (optUsuario.isEmpty()) {
            throw new HttpClientErrorException(
                HttpStatus.NOT_FOUND,
                ServiceUtils.createExceptionMessage(
                        MessageTemplate.ENTITY_NOT_FOUND,
                        ENTIDADE_SERVICE,
                        usuario.getId()
                )
            );
        }
        if (usuario.getDataCadastro() != null) {
            this.checkDataCadastro(usuario.getDataCadastro());
        }
        usuario.setId(usuarioId);
        this.usuarioRepository.saveAndFlush(usuario);
        return usuario;
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
                            id
                    )
            );
        }
        Usuario usuario = optUsuario.get();
        usuario.setAtivo(false);
        this.usuarioRepository.saveAndFlush(usuario);
    }


    private void checkIfFieldsAreNull(Usuario usuario)  {
        this.checkIfFieldIsEmpty(usuario.getNome(), "Nome");
        this.checkIfFieldIsEmpty(usuario.getSenha(), "Senha");
        this.checkIfFieldIsEmpty(usuario.getEmail(), "Email");
        this.checkIfFieldIsEmpty(usuario.getTelefone(), "Telefone");
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
        if (dataCadastro.isAfter(LocalDate.now())) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    ServiceUtils.createExceptionMessage(
                            MessageTemplate.REGISTER_DATE_AHEAD_OF_CURRENT_DATE,
                            ServiceUtils.dateToDDMMYYYY(dataCadastro)
                    )
            );
        }
    }

}
