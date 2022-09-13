package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, PageRequest pageRequest);

    @Query("select b from Booking b " +
    " where b.bookerId = ?1 and b.end < ?2" +
    " order by b.start desc ")
    List<Booking> getAllBookingPast(long bookerId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingCurrent(long bookerId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingFuture(long bookerId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByStatus(long bookerId, Status status, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserPast(long userId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserCurrent(long userId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserFuture(long userId, LocalDateTime now, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserByStatus(long userId, Status status, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserId(long userId, PageRequest pageRequest);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.id = ?1 and i.owner = ?2" +
            " order by b.start ")
    List<Booking> getAllBookingByUserCurrentByItem(long itemId, long userId);

    List<Booking> findAllByItemId(long itemId);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.id = ?1 and i.owner = ?2 and b.end < ?3" +
            " order by b.end desc ")
    Booking getLastBooking(long itemId, long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.id = ?1 and i.owner = ?2 and b.start > ?3" +
            " order by b.end ")
    Booking getNextBooking(long itemId, long userId, LocalDateTime now);
}
