package com.demo.compass.data.repository.base.upload.remote;


import com.demo.compass.data.model.BaseResponse;

import java.io.File;


import io.reactivex.Observable;

/**
 * Created by uyminhduc on 10/16/16.
 */

public interface UploadRepository {
    Observable<BaseResponse<Integer>> uploadImage(File file, String appCode, String sessionCode,
                                                  int userTeamID, String dateTimeDevice,
                                                  double latitude, double longitude,
                                                  int imageType, String fileName);
}
