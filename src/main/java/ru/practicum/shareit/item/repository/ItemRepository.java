package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(long ownerId, PageRequest pageRequest);

    @Query(" select item from Item item where item.available = true and (upper(item.name) like upper(concat('%', ?1, '%')) "
           + "or upper(item.description) like upper(concat('%', ?1, '%')) )")
    List<Item> searchItem(String text, PageRequest pageRequest);

    List<Item> findAllByRequestId(long requestId);
}
