package com.dsousa.minhasfinancas.service;




import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dsousa.minhasfinancas.exception.ErroAutenticacao;
import com.dsousa.minhasfinancas.exception.RegraNegocioException;
import com.dsousa.minhasfinancas.model.entity.Usuario;
import com.dsousa.minhasfinancas.model.repository.UsuarioRepository;
import com.dsousa.minhasfinancas.service.impl.UsuarioServiceImpl;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	
	@Test
	public void deveSalvarUmUsuario() {
		
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1L)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		//acao
		
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	@Test
	public void naoDeveCadastrarUmUsuarioComEmailJaCadastrado() {
		//cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
		
		//verificação
		Mockito.verify( repository, Mockito.never() ).save(usuario);
		
	}
	
	
	@Test
	public void deveValidarEmail() {
			
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void deveLancarErroQuandoExistirEmailCadastrado() {
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		//ação
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
	}
	
	@Test
	public void deveAutenticarUsuario() {
		
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when( repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastrado() {
		
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		
		//org.junit.jupiter.api.Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "senha"));
		
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha"));
		
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado para o email informado.");
		
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).id(1L).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		//org.junit.jupiter.api.Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "123"));
		
		Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
		
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Inválida");
	}
	
	
	
	

}
