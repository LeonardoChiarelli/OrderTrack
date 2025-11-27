package br.com.OrderTrack.Track.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("orderTrack/admin/track")
public class TrackController {

    @PostMapping("/registry")
    @Transactional
    public ResponseEntity<?> registerTrack(/* AMQP */) {



        return ResponseEntity.ok().build();
    }
}
