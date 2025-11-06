package org.gragon.system.dubbo;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.gragon.common.core.utils.MapstructUtils;
import org.gragon.system.api.RemoteClientService;
import org.gragon.system.api.domain.vo.RemoteClientVo;
import org.gragon.system.domain.vo.SysClientVo;
import org.gragon.system.service.SysClientService;
import org.springframework.stereotype.Service;

/**
 * 客户端服务
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteClientServiceImpl implements RemoteClientService {

    private final SysClientService sysClientService;

    /**
     * 根据客户端id获取客户端详情
     */
    @Override
    public RemoteClientVo queryByClientId(String clientId) {
        SysClientVo vo = sysClientService.queryByClientId(clientId);
        return MapstructUtils.convert(vo, RemoteClientVo.class);
    }

}
