package com.lyc.mapper;

import com.lyc.pojo.SearchRecords;
import com.lyc.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

	//查询热词
	public List<String> getHotwords();
}