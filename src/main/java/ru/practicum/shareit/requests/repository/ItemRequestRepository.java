package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterOrderByCreatedDesc(long requester);

    ItemRequest findById(long requestId);

    @Query(" select itemRequest from ItemRequest itemRequest where itemRequest.requester <> ?1 ")
    List<ItemRequest> findAllRequests(long userId, PageRequest pageRequest);
}
