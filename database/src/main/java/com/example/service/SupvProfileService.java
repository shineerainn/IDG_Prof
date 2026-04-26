package com.example.service;

import com.example.pojo.SupvProfile;
import com.example.pojo.dto.SupvProfileDto;
import com.example.repository.SupvProfileRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupvProfileService implements ISupvProfileService {

    @Autowired
    private SupvProfileRepository supvProfileRepository;

    @Override
    public SupvProfile create(SupvProfileDto SupvProfileDto) {

        SupvProfile supvProfile = new SupvProfile();
        BeanUtils.copyProperties(SupvProfileDto, supvProfile);

        return supvProfileRepository.save(supvProfile);
    }

    @Override
    public List<SupvProfile> getByUserId(String userId) {
        return supvProfileRepository.findByUserId(userId);
    }

    @Override
    public SupvProfile getByProfileId(String profileId) {
        return supvProfileRepository.findByProfileId(profileId);
    }
}
