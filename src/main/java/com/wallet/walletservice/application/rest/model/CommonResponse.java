package com.wallet.walletservice.application.rest.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class CommonResponse {
    private String message;

    public CommonResponse(com.wallet.walletservice.domain.model.CommonResponse domainCommonResponse) {
        this.message = domainCommonResponse.getMessage();
    }

    public com.wallet.walletservice.domain.model.CommonResponse toDomain() {
        return com.wallet.walletservice.domain.model.CommonResponse.builder()
                .message(this.getMessage()).build();
    }
}
