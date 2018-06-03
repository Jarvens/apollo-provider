package com.apollo.provider.apolloprovider.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.apollo.api.RemoteIndexService;
import org.springframework.stereotype.Component;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午12:30
 */
@Service(interfaceClass = RemoteIndexService.class, version = "1.0")
@Component
public class RemoteIndexServiceImpl implements RemoteIndexService {

    @Override
    public String hello() {
        return "Hello Dubbo";
    }

}
