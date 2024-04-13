package com.qingyu.mo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 签名
 * </p>
 *
 * @author qingyu-mo
 * @since 1.0.7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InnerCheckSign implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    private String key;

    /**
     * secret
     */
    private String secret;

    /**
     * version
     */
    private String version;

    /**
     * second
     */
    private Long second;
}