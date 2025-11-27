package br.com.OrderTrack.Track.domain.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orderTrack/admin/track")
public class TrackController {

    @PostMapping("/registry")
    @Transactional
    public ResponseEntity<Void> registerTrack(@RequestBody /* AMQP */) {



        return ResponseEntity.ok().build();
    }
}
