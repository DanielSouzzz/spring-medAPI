package app.springmedapi.controller;

import app.springmedapi.entity.Usuario;
import app.springmedapi.entity.usuarioDTO.DadosAutenticacao;
import app.springmedapi.infra.security.DadosTokenJwtDTO;
import app.springmedapi.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @RequestMapping
    public ResponseEntity autenticar(@RequestBody @Valid DadosAutenticacao dadosAutenticacao) {
        var authenticationTokentoken = new UsernamePasswordAuthenticationToken(dadosAutenticacao.login(), dadosAutenticacao.senha());
        var authenticaon = authenticationManager.authenticate(authenticationTokentoken);

        var tokenJWT = tokenService.gerarToken((Usuario) authenticaon.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJwtDTO(tokenJWT));
    }
}
