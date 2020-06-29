package com.hiberus.client.service.imp;

import java.util.List;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hiberus.client.repository.ClientRepository;
import com.hiberus.client.service.ClientService;
import com.hiberus.commons.dto.ClientDTO;
import com.hiberus.commons.dto.ClientResponseDTO;
import com.hiberus.commons.error.Errors;
import com.hiberus.commons.expection.CustomException;
import com.hiberus.commons.model.Client;

import lombok.extern.java.Log;

@Log
@Service
public class ClientServiceImp implements ClientService {

	@Value("${client.post}")
	String saveClientMessage = "";

	@Value("${client.put}")
	String updateClientMessage = "";

	@Value("${client.delete}")
	String deleteClientMessage = "";

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	ClientRepository clientRepository;

	@Override
	public ClientResponseDTO saveClient(ClientDTO clientDTO) throws CustomException {
		Optional<Client> clientCheck = clientRepository.findByIdentificationNumber(clientDTO.getIdentificationNumber());
		if (clientCheck.isPresent()) {
			throw new CustomException(HttpStatus.BAD_REQUEST.value(),
					String.format(Errors.CLIENT_WITH_IDENTIFICATION_NUMBER_ALREADY_EXIST.description,
							clientDTO.getIdentificationNumber()));
		}

		List<Client> clientsRegistered = clientRepository.findByEmailOrUsername(clientDTO.getEmail(),
				clientDTO.getUsername());

		if (!clientsRegistered.isEmpty()) {
			for (Client clientByEmailOrUsername : clientsRegistered) {
				if (clientByEmailOrUsername.getUsername().equals(clientDTO.getUsername())
						|| clientByEmailOrUsername.getEmail().equals(clientDTO.getEmail())) {
					throw new CustomException(HttpStatus.BAD_REQUEST.value(),
							String.format(Errors.EMAIL_OR_USERNAME_ARE_ALREADY_REGISTERED_FOR_OTHER_CLIENT.description,
									clientDTO.getIdentificationNumber()));
				}
			}
		}
		Client client = new Client();
		modelMapper.map(clientDTO, client);
		client.setEnabled(true);
		clientRepository.save(client);
		log.info(String.format(saveClientMessage, client.getIdentificationNumber()));
		return new ClientResponseDTO(String.format(saveClientMessage, client.getIdentificationNumber()),
				client.getClientId());
	}

	@Override
	public ClientResponseDTO updateClient(ClientDTO clientDTO) throws CustomException {
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		if (clientDTO.getIdentificationNumber() == null) {
			throw new CustomException(HttpStatus.BAD_REQUEST.value(),
					Errors.IDENTIFICATION_NUMBER_IS_REQUIRED.description);
		}
		Optional<Client> clientCheck = clientRepository.findByIdentificationNumber(clientDTO.getIdentificationNumber());
		if (!clientCheck.isPresent()) {
			throw new CustomException(HttpStatus.NOT_FOUND.value(), Errors.CLIENT_DOESNT_EXIST.description);
		}
		if (clientDTO.getUsername() == null) {
			clientDTO.setUsername(clientCheck.get().getUsername());
		}
		if (clientDTO.getEmail() == null) {
			clientDTO.setEmail(clientCheck.get().getEmail());
		}
		List<Client> clientsRegistered = clientRepository.findByEmailOrUsername(clientDTO.getEmail(),
				clientDTO.getUsername());

		if (!clientsRegistered.isEmpty()) {
			for (Client clientByEmailOrUsername : clientsRegistered) {
				if (clientByEmailOrUsername.getIdentificationNumber() != clientDTO.getIdentificationNumber()
						&& (clientByEmailOrUsername.getUsername().equals(clientDTO.getUsername())
								|| clientByEmailOrUsername.getEmail().equals(clientDTO.getEmail()))) {
					throw new CustomException(HttpStatus.BAD_REQUEST.value(),
							Errors.EMAIL_OR_USERNAME_ARE_ALREADY_REGISTERED_FOR_OTHER_CLIENT.description);
				}
			}
		}
		modelMapper.map(clientDTO, clientCheck.get());
		clientRepository.save(clientCheck.get());
		log.info(String.format(updateClientMessage, clientCheck.get().getIdentificationNumber()));
		return new ClientResponseDTO(String.format(updateClientMessage, clientCheck.get().getIdentificationNumber()),
				clientCheck.get().getClientId());
	}

	@Override
	public ClientDTO getClient(Long clientId) throws CustomException {
		ClientDTO clientDTO = new ClientDTO();
		Optional<Client> clientCheck = clientRepository.findById(clientId);
		if (!clientCheck.isPresent()) {
			throw new CustomException(HttpStatus.NOT_FOUND.value(), Errors.CLIENT_NOT_FOUND.description);
		}
		modelMapper.map(clientCheck.get(), clientDTO);
		return clientDTO;
	}

	@Override
	public ClientResponseDTO disableClient(String identificationNumber) throws CustomException {
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		Optional<Client> clientCheck = clientRepository.findByIdentificationNumber(identificationNumber);
		if (!clientCheck.isPresent()) {
			throw new CustomException(HttpStatus.BAD_REQUEST.value(), Errors.CLIENT_DOESNT_EXIST.description);
		}
		clientCheck.get().setEnabled(false);
		clientRepository.save(clientCheck.get());
		log.info(String.format(updateClientMessage, clientCheck.get().getIdentificationNumber()));
		return new ClientResponseDTO(String.format(updateClientMessage, clientCheck.get().getIdentificationNumber()),
				clientCheck.get().getClientId());
	}

}
