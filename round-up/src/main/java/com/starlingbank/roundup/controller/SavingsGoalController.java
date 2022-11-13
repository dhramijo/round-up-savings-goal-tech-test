package com.starlingbank.roundup.controller;

import com.starlingbank.roundup.services.SavingsGoalAmountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class SavingsGoalController {
  private final SavingsGoalAmountService savingsGoalAmountService;

  @GetMapping(path = "/savings/update-goal-amount")
  public ResponseEntity<Object> updateSavingsGoalAmount(
      @RequestParam(name = "dateFrom", required = false) String dateFrom,
      @RequestParam(name = "dateTo", required = false) String dateTo,
      @RequestParam(name = "savingsGoalId", required = true) String savingsGoalId) {
    log.info("Savings Goal Update Amount process started.");
    savingsGoalAmountService.updateSavingsGoalAmount(dateFrom, dateTo, savingsGoalId);
    return ResponseEntity.ok().build();
  }
}
