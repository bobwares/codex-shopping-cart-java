/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.config
 * File: AppProperties.java
 * Version: 0.1.0
 * Turns: [1]
 * Author: Codex Agent <agent@local>
 * Date: 2025-10-02T10:47:14Z
 * Exports: AppProperties
 * Description: Binds strongly typed configuration properties that describe
 *              application-level behaviour such as default pricing values
 *              and supported currencies for shopping cart operations.
 */
package com.bobwares.shoppingcart.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal defaultTaxRate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal defaultShippingCost;

    @NotEmpty
    private List<String> supportedCurrencies = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDefaultTaxRate() {
        return defaultTaxRate;
    }

    public void setDefaultTaxRate(BigDecimal defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }

    public BigDecimal getDefaultShippingCost() {
        return defaultShippingCost;
    }

    public void setDefaultShippingCost(BigDecimal defaultShippingCost) {
        this.defaultShippingCost = defaultShippingCost;
    }

    public List<String> getSupportedCurrencies() {
        return Collections.unmodifiableList(supportedCurrencies);
    }

    public void setSupportedCurrencies(List<String> supportedCurrencies) {
        this.supportedCurrencies = new ArrayList<>();
        if (supportedCurrencies != null) {
            supportedCurrencies.stream()
                .filter(Objects::nonNull)
                .map(currency -> currency.toUpperCase(Locale.ROOT))
                .forEach(this.supportedCurrencies::add);
        }
    }

    public boolean isCurrencySupported(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) {
            return false;
        }
        return supportedCurrencies.contains(currencyCode.toUpperCase(Locale.ROOT));
    }
}
