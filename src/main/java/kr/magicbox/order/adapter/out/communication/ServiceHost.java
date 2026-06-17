package kr.magicbox.order.adapter.out.communication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceHost {
    WAITING("waiting-service"),
    CREATOR("creator-service");

    private final String hostName;
}
