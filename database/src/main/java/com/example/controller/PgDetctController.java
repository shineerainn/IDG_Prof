package com.example.controller;

import com.example.pojo.PgDetct;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.PgDetctDto;
import com.example.service.IPgDetctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/pgDetct")
public class PgDetctController {

    @Autowired
    private IPgDetctService pgDetctService;

    @PostMapping
    public ResponseMessage<PgDetct> create(@Validated @RequestBody PgDetctDto pgDetctDto) {
        PgDetct pgDetct = pgDetctService.create(pgDetctDto);
        return ResponseMessage.success(pgDetct);
    }

    @GetMapping("/userId/{userId}")
    public ResponseMessage<List<PgDetct>> getByUserId(@PathVariable String userId) {
        List<PgDetct> pgDetcts = pgDetctService.getByUserId(userId);
        return ResponseMessage.success(pgDetcts);
    }

    @GetMapping("/detctId/{detctId}")
    public ResponseMessage<PgDetct> getByDetctId(@PathVariable String detctId) {
        PgDetct pgDetct = pgDetctService.getByDetctId(detctId);
        return ResponseMessage.success(pgDetct);
    }
}