package com.bank.loan.service.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepaymentStrategyRegistry {

    private final Map<String, RepaymentAllocationStrategy> strategyMap = new HashMap<>();

    @Autowired
    public RepaymentStrategyRegistry(List<RepaymentAllocationStrategy> strategies) {
        for (RepaymentAllocationStrategy strategy : strategies) {
            strategyMap.put(strategy.getName().toLowerCase(), strategy);
        }
    }

    public RepaymentAllocationStrategy getStrategy(String name) {
        if (name == null) {
            // Default strategy
            return strategyMap.get("chargesinterestprincipal");
        }
        RepaymentAllocationStrategy strategy = strategyMap.get(name.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported repayment allocation strategy: " + name);
        }
        return strategy;
    }
}
