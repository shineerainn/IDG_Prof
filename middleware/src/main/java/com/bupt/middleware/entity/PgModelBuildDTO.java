package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiao
 * @date 2025/4/10 00:24
 * @description: /
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgModelBuildDTO {
    String userId;

    PgWideTableAttributes pgWideTableAttributes;
}
