package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository("ItemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(long ownerId);

    @Query("select item from Item item where item.available = true and item.name like ?1 and item.description like ?1")
    List<Item> searchItem(String text);
}
