package com.nnk.springboot.servicesTest;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @InjectMocks
    private BidListService bidListService;

    @Mock
    private BidListRepository bidListRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBids() {
        BidList bid1 = new BidList("Account Test 1", "Type Test 1", 10.0);
        BidList bid2 = new BidList("Account Test 2", "Type Test 2", 20.0);
        List<BidList> bidLists = Arrays.asList(bid1, bid2);

        when(bidListRepository.findAll()).thenReturn(bidLists);

        List<BidList> result = bidListService.getAllBids();

        assertEquals(2, result.size());
        verify(bidListRepository, times(1)).findAll();
    }

    @Test
    void getBidById() {
        BidList bid = new BidList();
        bid.setAccount("Account Test");
        bid.setType("Type Test");
        bid.setBidQuantity(10.0);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        Optional<BidList> result = bidListService.getBidById(1);

        assertTrue(result.isPresent());
        assertEquals("Account Test", result.get().getAccount());
        verify(bidListRepository, times(1)).findById(1);
    }

    @Test
    void addBid() {
        BidList bid = new BidList("Account Test", "Type Test", 10.0);

        bidListService.addBid(bid);

        verify(bidListRepository, times(1)).save(bid);
    }

    @Test
    void updateBid() {
        BidList existingBid = new BidList();
        existingBid.setId(1);
        existingBid.setAccount("Account Test");
        existingBid.setType("Type Test");
        existingBid.setBidQuantity(10.0);
        BidList updatedBid = new BidList();
        updatedBid.setAccount("Updated Account");
        updatedBid.setType("Updated Type");
        updatedBid.setBidQuantity(20.0);

        when(bidListRepository.findById(1)).thenReturn(Optional.of(existingBid));

        bidListService.updateBid(1, updatedBid);

        verify(bidListRepository, times(1)).save(existingBid);
        assertEquals("Updated Account", existingBid.getAccount());
        assertEquals("Updated Type", existingBid.getType());
        assertEquals(20.0, existingBid.getBidQuantity());
    }

    @Test
    void deleteBid() {
        BidList bid = new BidList("Account Test", "Type Test", 10.0);
        bid.setId(1);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        bidListService.deleteBid(1);

        verify(bidListRepository, times(1)).delete(bid);
    }

    @Test
    void updateBidNotFound() {
        BidList updatedBid = new BidList();
        updatedBid.setAccount("Updated Account");
        updatedBid.setType("Updated Type");
        updatedBid.setBidQuantity(20.0);

        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bidListService.updateBid(1, updatedBid);
        });

        String expectedMessage = "Invalid Bid Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteBidNotFound() {
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bidListService.deleteBid(1);
        });

        String expectedMessage = "Invalid Bid Id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
