package com.lyc.service;

import com.lyc.pojo.Bgm;

import java.util.List;

public interface BgmService {

    /**
     * @Description:用于查询背景音乐列表
     */
    public List<Bgm> bgmList();

    /**
     *
     * @Description:用于按照id查找bgm
     */
    public Bgm findBgm(String bgmId);
}
