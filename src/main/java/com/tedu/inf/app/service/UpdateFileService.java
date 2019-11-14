package com.tedu.inf.app.service;

import com.tedu.inf.app.vo.UpdateFileVo;

public interface UpdateFileService {

	UpdateFileVo selectAppStartPage(Long versionCode, UpdateFileVo vo);

	UpdateFileVo selectTeachingGuidePage(Long versionCode, UpdateFileVo vo);

}
