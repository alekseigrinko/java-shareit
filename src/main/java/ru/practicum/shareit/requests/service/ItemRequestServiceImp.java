package ru.practicum.shareit.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.BadRequestException;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestReturnDto;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.requests.ItemRequestMapper.*;

@Service
@Slf4j
public class ItemRequestServiceImp implements ItemRequestService {

    ItemRequestRepository itemRequestRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;

    public ItemRequestServiceImp(ItemRequestRepository itemRequestRepository, UserRepository userRepository,
                                 ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestDto create(long userId, ItemRequestDto itemRequestDto) {
        checkUser(userId);
        checkItemRequest(itemRequestDto);
        itemRequestDto.setRequester(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return toItemRequestDto(itemRequestRepository.save(toItemRequest(itemRequestDto)));
    }

    @Override
    public ItemRequestReturnDto getItemRequestById(long requestId, long userId) {
        checkRequest(requestId);
        checkUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId);
        List<ItemDto> itemDtoList = getItemDtoList(itemRequest.getRequester());
        return toItemRequestReturnDto(itemRequest, itemDtoList);
    }

    @Override
    public List<ItemRequestReturnDto> getAllUserRequests(long userId) {
        checkUser(userId);
        List<ItemRequestReturnDto> requestReturnList = new ArrayList<>();
        for (ItemRequest itemRequest: itemRequestRepository.findAllByRequesterOrderByCreatedDesc(userId)) {
            List<ItemDto> itemDtoList = getItemDtoList(userId);
            requestReturnList.add(toItemRequestReturnDto(itemRequest, itemDtoList));
        }
        return requestReturnList;
    }

    @Override
    public List<ItemRequestReturnDto> getAllRequests(PageRequest pageRequest, long userId) {
        List<ItemRequestReturnDto> requestReturnList = new ArrayList<>();
        for (ItemRequest itemRequest: itemRequestRepository.findAllRequests(userId, pageRequest)) {
            List<ItemDto> itemDtoList = getItemDtoList(itemRequest.getRequester());
            requestReturnList.add(toItemRequestReturnDto(itemRequest, itemDtoList));
        }
        return requestReturnList;
    }

    private void checkUser(long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь ID: " + userId + ", не найден!");
            throw new ObjectNotFoundException("Пользователь ID: " + userId + ", не найден!");
        }
    }

    private void checkItemRequest(ItemRequestDto itemRequestDto) {
        if ((itemRequestDto.getDescription() == null) || (itemRequestDto.getDescription().isBlank())) {
            log.warn("Описание не может быть пустым");
            throw new BadRequestException("Описание не может быть пустым");
        }
    }

    private List<ItemDto> getItemDtoList(long userId) {
        return itemRepository.findAllByRequestId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkRequest(long requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            log.warn("Запрос ID: " + requestId + ", не найден!");
            throw new ObjectNotFoundException("Запрос ID: " + requestId + ", не найден!");
        }
    }
}
