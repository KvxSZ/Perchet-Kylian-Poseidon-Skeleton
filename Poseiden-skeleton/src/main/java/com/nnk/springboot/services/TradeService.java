package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> getTrades() {
        return tradeRepository.findAll();
    }

    public Optional<Trade> getTradeById(Integer id) {
        return tradeRepository.findById(id);
    }

    @Transactional
    public Trade addTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Transactional
    public void updateTrade(Integer id, Trade tradeDetails) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));

        trade.setAccount(tradeDetails.getAccount());
        trade.setType(tradeDetails.getType());
        trade.setBuyQuantity(tradeDetails.getBuyQuantity());
        trade.setSellQuantity(tradeDetails.getSellQuantity());
        trade.setBuyPrice(tradeDetails.getBuyPrice());
        trade.setSellPrice(tradeDetails.getSellPrice());
        trade.setTradeDate(tradeDetails.getTradeDate());
        trade.setSecurity(tradeDetails.getSecurity());
        trade.setStatus(tradeDetails.getStatus());
        trade.setTrader(tradeDetails.getTrader());
        trade.setBenchmark(tradeDetails.getBenchmark());
        trade.setBook(tradeDetails.getBook());
        trade.setCreationName(tradeDetails.getCreationName());
        trade.setCreationDate(tradeDetails.getCreationDate());
        trade.setRevisionName(tradeDetails.getRevisionName());
        trade.setRevisionDate(tradeDetails.getRevisionDate());
        trade.setDealName(tradeDetails.getDealName());
        trade.setDealType(tradeDetails.getDealType());
        trade.setSourceListId(tradeDetails.getSourceListId());
        trade.setSide(tradeDetails.getSide());

        tradeRepository.save(trade);
    }
    @Transactional
    public void deleteTrade(Integer id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeRepository.delete(trade);
    }
}
