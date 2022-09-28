package ru.practicum.server.requests.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.requests.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterOrderByCreatedDesc(long requester);

    @Query(" select itemRequest from ItemRequest itemRequest where itemRequest.requester <> ?1 ")
    Page<ItemRequest> findAllRequests(long userId, Pageable pageable);
}
