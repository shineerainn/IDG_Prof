package com.example.service;

import com.example.pojo.PgDetct;
import com.example.pojo.dto.PgDetctDto;
import com.example.repository.PgDetctRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgDetctService implements IPgDetctService {

    @Autowired
    private PgDetctRepository pgDetctRepository;

    @Override
    public PgDetct create(PgDetctDto pgDetctDto) {

        PgDetct pgDetct = new PgDetct();
        BeanUtils.copyProperties(pgDetctDto, pgDetct);

        return pgDetctRepository.save(pgDetct);
    }

    @Override
    public List<PgDetct> getByUserId(String userId) {
        return pgDetctRepository.getByUserId(userId);
    }

    @Override
    public PgDetct getByDetctId(String detctId) {
        return pgDetctRepository.getByDetctId(detctId);
    }
}
