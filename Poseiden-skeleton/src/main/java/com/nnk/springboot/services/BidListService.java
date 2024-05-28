package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> getAllBids() {
        return bidListRepository.findAll();
    }

    public Optional<BidList> getBidById(Integer id) {
        return bidListRepository.findById(id);
    }

    public void addBid(BidList bid) {
        bidListRepository.save(bid);
    }

    public void updateBid(Integer id, BidList bidDetails) {
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id: " + id));

        bid.setAccount(bidDetails.getAccount());
        bid.setType(bidDetails.getType());
        bid.setBidQuantity(bidDetails.getBidQuantity());
        bid.setAskQuantity(bidDetails.getAskQuantity());
        bid.setBid(bidDetails.getBid());
        bid.setAsk(bidDetails.getAsk());
        bid.setBenchmark(bidDetails.getBenchmark());
        bid.setBidListDate(bidDetails.getBidListDate());
        bid.setCommentary(bidDetails.getCommentary());
        bid.setSecurity(bidDetails.getSecurity());
        bid.setStatus(bidDetails.getStatus());
        bid.setTrader(bidDetails.getTrader());
        bid.setBook(bidDetails.getBook());
        bid.setCreationName(bidDetails.getCreationName());
        bid.setCreationDate(bidDetails.getCreationDate());
        bid.setRevisionName(bidDetails.getRevisionName());
        bid.setRevisionDate(bidDetails.getRevisionDate());
        bid.setDealName(bidDetails.getDealName());
        bid.setDealType(bidDetails.getDealType());
        bid.setSourceListId(bidDetails.getSourceListId());
        bid.setSide(bidDetails.getSide());

        bidListRepository.save(bid);
    }

    public void deleteBid(Integer id) {
        BidList bid = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Bid Id: " + id));
        bidListRepository.delete(bid);
    }

}
