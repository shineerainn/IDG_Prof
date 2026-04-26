package com.example.controller;

import com.example.pojo.PgProfile;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.PgProfileDto;
import com.example.service.IPgProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/pgProfile")
public class PgProfileController {

    @Autowired
    private IPgProfileService pgProfileService;

    @PostMapping
    public ResponseMessage<PgProfile> create(@Validated @RequestBody PgProfileDto pgProfileDto) {
        PgProfile pgProfile = pgProfileService.create(pgProfileDto);
        return ResponseMessage.success(pgProfile);
    }

    @GetMapping("/userId/{userId}")
    public ResponseMessage<List<PgProfile>> getByUserId(@PathVariable String userId) {
        List<PgProfile> pgProfiles = pgProfileService.getByUserId(userId);
        return ResponseMessage.success(pgProfiles);
    }

    @GetMapping("/profileId/{profileId}")
    public ResponseMessage<PgProfile> getByProfileId(@PathVariable String profileId) {
        PgProfile pgProfile = pgProfileService.getByProfileId(profileId);
        return ResponseMessage.success(pgProfile);
    }
}