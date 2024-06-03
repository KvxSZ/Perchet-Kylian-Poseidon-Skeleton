package com.nnk.springboot.servicesTest;

import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTrades() {
        Trade trade1 = new Trade("Account1", "Type1", 100.0);
        Trade trade2 = new Trade("Account2", "Type2", 200.0);
        List<Trade> trades = Arrays.asList(trade1, trade2);

        when(tradeRepository.findAll()).thenReturn(trades);

        List<Trade> result = tradeService.getTrades();

        assertEquals(2, result.size());
        verify(tradeRepository, times(1)).findAll();
    }

    @Test
    void getTradeById() {
        Trade trade = new Trade();
        trade.setAccount("Account1");
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Optional<Trade> result = tradeService.getTradeById(1);

        assertTrue(result.isPresent());
        assertEquals("Account1", result.get().getAccount());
        verify(tradeRepository, times(1)).findById(1);
    }

    @Test
    void addTrade() {
        Trade trade = new Trade("Account1", "Type1", 100.0);

        tradeService.addTrade(trade);

        verify(tradeRepository, times(1)).save(trade);
    }

    @Test
    void updateTrade() {
        Trade existingTrade = new Trade();
        existingTrade.setTradeId(1);
        existingTrade.setAccount("Account1");
        existingTrade.setType("Type1");
        existingTrade.setBuyQuantity(100.0);
        Trade updatedTrade = new Trade("UpdatedAccount", "UpdatedType", 200.0);
        updatedTrade.setAccount("UpdatedAccount");
        updatedTrade.setType("UpdatedType");
        updatedTrade.setBuyQuantity(200.0);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(existingTrade));

        tradeService.updateTrade(1, updatedTrade);

        verify(tradeRepository, times(1)).save(existingTrade);
        assertEquals("UpdatedAccount", existingTrade.getAccount());
        assertEquals("UpdatedType", existingTrade.getType());
        assertEquals(200.0, existingTrade.getBuyQuantity());
    }

    @Test
    void deleteTrade() {
        Trade trade = new Trade("Account1", "Type1", 100.0);
        trade.setTradeId(1);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        tradeService.deleteTrade(1);

        verify(tradeRepository, times(1)).delete(trade);
    }

    @Test
    void updateTradeNotFound() {
        Trade updatedTrade = new Trade("UpdatedAccount", "UpdatedType", 200.0);

        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.updateTrade(1, updatedTrade);
        });

        String expectedMessage = "Invalid trade Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteTradeNotFound() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tradeService.deleteTrade(1);
        });

        String expectedMessage = "Invalid trade Id:1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
