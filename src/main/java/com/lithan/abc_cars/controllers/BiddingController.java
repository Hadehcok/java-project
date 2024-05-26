package com.lithan.abc_cars.controllers;

import com.lithan.abc_cars.dtos.BiddingDto;
import com.lithan.abc_cars.models.Bidding;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.BiddingService;
import com.lithan.abc_cars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BiddingController {
    private UserService userService;
    private BiddingService biddingService;

    @Autowired
    public BiddingController(UserService userService, BiddingService biddingService) {
        this.userService = userService;
        this.biddingService = biddingService;
    }

    @GetMapping("/biddingHistory")
    public String biddingHistory(Model model) {
        // Retrieve the list of biddings from the service
        List<Bidding> biddings = biddingService.getAllBiddings();

        // Add the list of biddings to the model
        model.addAttribute("biddings", biddings);

        // Return the name of the Thymeleaf template for the bidding history page
        return "bidding/history";
    }

    @GetMapping("addBidding/{carId}")
    public String addBiddingForm(@PathVariable("carId") Long carId, Model model){
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("carId",carId);
        model.addAttribute("bidding", new Bidding());

        return "bidding/biddingForm";
    }

    @PostMapping("/placeBid/{carId}")
    public String placeBid(@PathVariable("carId") Long carId, @ModelAttribute("bidding") BiddingDto bidding, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("carId",carId);
            model.addAttribute("bidding", new Bidding());
            // Handle validation errors if necessary
            return "bidding/biddingForm";
        }

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }

        // Save the bidding
        biddingService.addBid(carId, bidding);

        return "redirect:/carDetail/" + carId +"?bidding"; // Redirect to car detail page
    }

    @PostMapping("/acceptBidding")
    public String acceptBidding(@RequestParam("biddingId") Long biddingId) {
        biddingService.acceptBidding(biddingId);

        // Redirect to a confirmation page after accepting the bidding
        return "redirect:/biddingHistory?success";
    }

}
