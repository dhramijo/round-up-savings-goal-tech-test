package com.starlingbank.roundup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.starlingbank.roundup.services.SavingsGoalAmountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class SavingsGoalControllerTest {

  @Autowired private MockMvc mvc;
  @MockBean private SavingsGoalAmountService savingsGoalAmountService;

  @Test
  void test_update_savings_goal_amount_updated_successfully_without_dates_specified() throws Exception {

    doNothing().when(savingsGoalAmountService).updateSavingsGoalAmount(any(),any(),anyString());

    mvc.perform(get("/api/v1/savings/update-goal-amount")
            .accept(MediaType.APPLICATION_JSON)
            .param("savingsGoalId", "savingsGoalId"))
        .andExpect(status().isOk());

    verify(savingsGoalAmountService, times(1)).updateSavingsGoalAmount(isNull(),isNull(),isA(String.class));
  }

  @Test
  void test_update_savings_goal_amount_updated_successfully_with_dates_specified() throws Exception {

    doNothing().when(savingsGoalAmountService).updateSavingsGoalAmount(any(),any(),anyString());

    mvc.perform(get("/api/v1/savings/update-goal-amount")
            .accept(MediaType.APPLICATION_JSON)
            .param("savingsGoalId", "savingsGoalId")
            .param("dateFrom", "2022-11-04T15:35:22.801Z")
            .param("dateTo", "2022-11-11T15:35:22.802Z")
        ).andExpect(status().isOk());

    verify(savingsGoalAmountService, times(1)).updateSavingsGoalAmount(isA(String.class),isA(String.class),isA(String.class));
  }

  @Test
  void test_savings_goal_amount_updated_fails_due_to_missing_query_param() throws Exception {

    doNothing().when(savingsGoalAmountService).updateSavingsGoalAmount(any(), any(), anyString());

    mvc.perform(get("/api/v1/savings/update-goal-amount")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError());

    verifyNoInteractions(savingsGoalAmountService);
  }
}