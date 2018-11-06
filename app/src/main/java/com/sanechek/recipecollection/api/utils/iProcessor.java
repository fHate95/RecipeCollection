package com.sanechek.recipecollection.api.utils;

import com.sanechek.recipecollection.api.data.BaseRequest;
import com.sanechek.recipecollection.api.data.BaseResponse;

public interface iProcessor {

    <Rsp extends BaseResponse, Req extends BaseRequest<Rsp>> Rsp process(Req request) throws Exception;
}
