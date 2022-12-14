package ru.practicum.server.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.server.exeption.BadRequestException;
import ru.practicum.server.exeption.ObjectNotFoundException;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.mapper.ItemMapper;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.requests.ItemRequest;
import ru.practicum.server.requests.dto.ItemRequestDto;
import ru.practicum.server.requests.dto.ItemRequestReturnDto;
import ru.practicum.server.requests.repository.ItemRequestRepository;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.requests.ItemRequestMapper.*;

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
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).get();
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
            log.warn("???????????????????????? ID: " + userId + ", ???? ????????????!");
            throw new ObjectNotFoundException("???????????????????????? ID: " + userId + ", ???? ????????????!");
        }
    }

    private void checkItemRequest(ItemRequestDto itemRequestDto) {
        if ((itemRequestDto.getDescription() == null) || (itemRequestDto.getDescription().isBlank())) {
            log.warn("???????????????? ???? ?????????? ???????? ????????????");
            throw new BadRequestException("???????????????? ???? ?????????? ???????? ????????????");
        }
    }

    private List<ItemDto> getItemDtoList(long userId) {
        return itemRepository.findAllByRequestId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkRequest(long requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            log.warn("???????????? ID: " + requestId + ", ???? ????????????!");
            throw new ObjectNotFoundException("???????????? ID: " + requestId + ", ???? ????????????!");
        }
    }
}
