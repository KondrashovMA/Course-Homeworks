package ru.event.manager.events.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events")
public class EventController {

    @PostMapping
    public void createEvent() {

    }

    @GetMapping
    public void getAllEvents() {

    }


    @DeleteMapping("/{id}")
    public void deleteEventById() {

    }

    @GetMapping("/{$id}")
    public void getEventById(@PathVariable("id") Long id) {

    }

    @PutMapping("/{id}")
    public void updateEventById() {

    }
}
