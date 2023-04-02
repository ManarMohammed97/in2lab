package com.haw.srs.customerservice;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// test 2
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Reservation {

    @Column(name = "reservation_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")

        Movie movie;

    public Reservation(Movie movie, int auditoriumNum, int seatNum) {
        this.movie = movie;
        this.auditoriumNum = auditoriumNum;
        this.seatNum = seatNum;
    }

    private int auditoriumNum;
    private int seatNum;

}
