package com.hiberus.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hiberus.client.service.ClientService;
import com.hiberus.commons.dto.ClientDTO;
import com.hiberus.commons.dto.ClientResponseDTO;
import com.hiberus.commons.expection.CustomException;

@RestController
public class ClientController {

	@Autowired
	ClientService clientService;

	@PostMapping("/clients")
	public ResponseEntity<ClientResponseDTO> createClient(@Validated @RequestBody ClientDTO clientDTO)
			throws CustomException {
		return new ResponseEntity<>(clientService.saveClient(clientDTO), HttpStatus.OK);
	}

	@PatchMapping("/clients")
	public ResponseEntity<ClientResponseDTO> updateClient(@RequestBody ClientDTO clientDto) throws CustomException {
		return new ResponseEntity<>(clientService.updateClient(clientDto), HttpStatus.OK);
	}

	@GetMapping("/clients/{clientId}")
	public ResponseEntity<ClientDTO> findClient(@PathVariable Long clientId) throws CustomException {
		return new ResponseEntity<>(clientService.getClient(clientId), HttpStatus.OK);
	}

	@DeleteMapping("/clients/{identificationNumber}")
	public ResponseEntity<ClientResponseDTO> disableClient(@PathVariable String identificationNumber)
			throws CustomException {
		return new ResponseEntity<>(clientService.disableClient(identificationNumber), HttpStatus.OK);
	}
}
