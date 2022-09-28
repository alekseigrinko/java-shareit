package ru.practicum.server.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwner(long ownerId, Pageable pageable);

    @Query(" select item from Item item where item.available = true and (upper(item.name) like upper(concat('%', ?1, '%')) "
           + "or upper(item.description) like upper(concat('%', ?1, '%')) )")
    Page<Item> searchItem(String text, Pageable pageable);

    List<Item> findAllByRequestId(long requestId);
}
