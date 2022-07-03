package com.wallet.walletservice.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletServiceException extends RuntimeException {

    private String message;
}
