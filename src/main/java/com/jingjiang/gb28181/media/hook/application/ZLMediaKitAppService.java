package com.jingjiang.gb28181.media.hook.application;

import com.jingjiang.gb28181.media.hook.domain.StreamChanged;

public class ZLMediaKitAppService {

    public void onStreamChanged(StreamChanged streamChanged) {

        if (streamChanged.getRegist()) {

        } else {

        }

    }

}
