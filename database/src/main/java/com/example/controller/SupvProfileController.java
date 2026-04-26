package com.example.controller;

import com.example.pojo.SupvProfile;
import com.example.pojo.ResponseMessage;
import com.example.pojo.dto.SupvProfileDto;
import com.example.service.ISupvProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database/supvProfile")
public class SupvProfileController {

    @Autowired
    private ISupvProfileService supvProfileService;

    @PostMapping
    public ResponseMessage<SupvProfile> create(@Validated @RequestBody SupvProfileDto supvProfileDto) {
        SupvProfile supvProfile = supvProfileService.create(supvProfileDto);
        return ResponseMessage.success(supvProfile);
    }

    @GetMapping("/userId/{userId}")
    public ResponseMessage<List<SupvProfile>> getByUserId(@PathVariable String userId) {
        List<SupvProfile> supvProfiles = supvProfileService.getByUserId(userId);
        return ResponseMessage.success(supvProfiles);
    }

    @GetMapping("/profileId/{profileId}")
    public ResponseMessage<SupvProfile> getByProfileId(@PathVariable String profileId) {
        SupvProfile supvProfile = supvProfileService.getByProfileId(profileId);
        return ResponseMessage.success(supvProfile);
    }
}