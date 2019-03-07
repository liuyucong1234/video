package com.lyc.service.impl;

import com.lyc.mapper.BgmMapper;
import com.lyc.pojo.Bgm;
import com.lyc.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> bgmList() {
        return bgmMapper.selectAll();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm findBgm(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }
}
