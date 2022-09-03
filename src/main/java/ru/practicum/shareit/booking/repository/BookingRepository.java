package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    @Query("select b from Booking b " +
    " where b.bookerId = ?1 and b.end < ?2" +
    " order by b.start desc ")
    List<Booking> getAllBookingPast(long bookerId, LocalDateTime now);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingCurrent(long bookerId, LocalDateTime now);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingFuture(long bookerId, LocalDateTime now);

    @Query("select b from Booking b " +
            " where b.bookerId = ?1 and b.status = UPPER(?2)" +
            " order by b.start desc ")
    List<Booking> getAllBookingByStatus(long bookerId, String status);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.end < ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserPast(long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start < ?2 and b.end > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserCurrent(long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.start > ?2" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserFuture(long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1 and b.status = UPPER(?2)" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserByStatus(long userId, String status);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.owner = ?1" +
            " order by b.start desc ")
    List<Booking> getAllBookingByUserId(long userId);

    @Query("select b from Booking b " +
            "inner join Item i on b.itemId = i.id" +
            " where i.id = ?1 and i.owner = ?2 and b.start < ?3" +
            " order by b.start ")
    List<Booking> getAllBookingByUserCurrentByItem(long itemId, long userId, LocalDateTime now);

    Booking findByItemId(long itemId);
}