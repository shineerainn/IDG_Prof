package com.bupt.middleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenxiao
 * @date 2025/4/13 14:58
 * @description: Supervisor Model Build DTO
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupvModelBuildDTO {
    String userId;

    SupvWideTableAttributes supvWideTableAttributes;
}
