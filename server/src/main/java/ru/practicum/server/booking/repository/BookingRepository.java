package ru.practicum.server.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(long bookerId, Pageable pageable);

    @Query("select b from Booking b " +
    " where b.bookerId = ?1 and b.end < ?2" +
    " order by b.start desc ")
    Page<Booking> getAllBookingPast(long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingCurrent(long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingFuture(long bookerId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.status = ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByStatus(long bookerId, String status, Pageable pageable);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.end < ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByUserPast(long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByUserCurrent(long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start > ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByUserFuture(long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = ?2" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByUserByStatus(long userId, Status status, Pageable pageable);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1" +
            " order by b.start desc ")
    Page<Booking> getAllBookingByUserId(long userId, Pageable pageable);

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
