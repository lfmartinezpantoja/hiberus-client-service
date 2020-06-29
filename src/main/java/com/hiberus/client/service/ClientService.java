package com.hiberus.client.service;

import org.springframework.stereotype.Repository;

import com.hiberus.commons.dto.ClientDTO;
import com.hiberus.commons.dto.ClientResponseDTO;
import com.hiberus.commons.expection.CustomException;

@Repository
public interface ClientService {

	public ClientResponseDTO saveClient(ClientDTO clientDTO) throws CustomException;

	public ClientResponseDTO updateClient(ClientDTO clientDTO) throws CustomException;

	public ClientDTO getClient(Long clientId) throws CustomException;

	public ClientResponseDTO disableClient(String identificationNumber) throws CustomException;

}
