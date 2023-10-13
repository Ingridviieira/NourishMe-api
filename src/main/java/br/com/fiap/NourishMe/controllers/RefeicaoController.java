package br.com.fiap.NourishMe.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.NourishMe.exceptions.RestNotFoundException;
import br.com.fiap.NourishMe.models.Refeicao;
import br.com.fiap.NourishMe.repository.MotivoRepository;
import br.com.fiap.NourishMe.repository.RefeicaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/refeicoes")
@Slf4j
// @SecurityRequirement(name = "bearer-key")
// @Tag(name = "refeicoes")
public class RefeicaoController {
    
    @Autowired
    RefeicaoRepository refeicaoRepository;

    @Autowired
    MotivoRepository motivoRepository;

    @Autowired
    PagedResourcesAssembler<Object> assembler;

    @GetMapping
    public PagedModel<EntityModel<Object>> index(@RequestParam(required = false) String busca, @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
        var refeicoes = (busca == null) ? 
            refeicaoRepository.findAll(pageable): 
            refeicaoRepository.findByNomeContaining(busca, pageable);

        return assembler.toModel(refeicoes.map(Refeicao::toEntityModel)); //HAL
    }

    @PostMapping
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "a refeicao foi cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "os dados enviados são inválidos")
    })
    public ResponseEntity<EntityModel<Refeicao>> create(
            @RequestBody @Valid Refeicao refeicao,
            BindingResult result) {
        log.info("cadastrando refeicao: " + refeicao);
        refeicaoRepository.save(refeicao);
        refeicao.setMotivo(motivoRepository.findById(refeicao.getMotivo().getId()).get());
        return ResponseEntity
            .created(refeicao.toEntityModel().getRequiredLink("self").toUri())
            .body(refeicao.toEntityModel());
    }

    @GetMapping("{id}")
    @Operation(
        summary = "Detalhes da refeicao",
        description = "Retornar os dados da refeicao de acordo com o id informado no path"
    )
    public EntityModel<Refeicao> show(@PathVariable Long id) {
        log.info("buscando a refeicao: " + id);
        return getRefeicao(id).toEntityModel();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Refeicao> destroy(@PathVariable Long id) {
        log.info("apagando refeicao: " + id);
        refeicaoRepository.delete(getRefeicao(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Refeicao>> update(
            @PathVariable Long id,
            @RequestBody @Valid Refeicao refeicao) {
        log.info("atualizando refeicao: " + id);
        getRefeicao(id);
        refeicao.setId(id);
        refeicaoRepository.save(refeicao);
        return ResponseEntity.ok(refeicao.toEntityModel());
    }

    private Refeicao getRefeicao(Long id) {
        return refeicaoRepository.findById(id).orElseThrow(
                () -> new RestNotFoundException("refeicao não encontrada"));
    }

}
