package com.generation.blogpessoal.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

@RestController // indica que é uma classe controladora do tipo rest
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*") // sem essa anotaçao o front nao vai conseguir se comunicar com o back
													// end, em produçao coloca o endereço, sem *
public class PostagemController {

	@Autowired
	private PostagemRepository postagemRepository;

	// listagem de posts
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll() { // resposta http do tipo 200
		return ResponseEntity.ok(postagemRepository.findAll()); // findAll = equivalente ao select * from tb_postagem;
	}

	// consultar por ID
	@GetMapping("/{id}") // será uma variavel de caminho. ex: /postagens/'id'
	public ResponseEntity<Postagem> getById(@PathVariable Long id) { // optional
		return postagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta)) // lambda
				.orElse(ResponseEntity.notFound().build());
	} // nesse caso optional + lambda substitui if

	// listar por titulo
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}

	// cadastrar postagem
	@PostMapping
	public ResponseEntity<Postagem> postPostagem(@Valid @RequestBody Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
	}

	// editar postagem
	@PutMapping
	public ResponseEntity<Postagem> putPostagem(@Valid @RequestBody Postagem postagem) {
		return postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.ok(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.notFound().build());
	}

	// deletar postagem
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePostagem(@PathVariable Long id) {
		return postagemRepository.findById(id).map(resposta -> {
			postagemRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}).orElse(ResponseEntity.notFound().build());

	}
	

}
