package com.getdropdowndata.dropdown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.getdropdowndata.dropdown.service.DropDownService;

@RestController
public class DropDownDataController {

    @Autowired
    private DropDownService dropDownService;

   
    public DropDownDataController(DropDownService dropDownService) {
        this.dropDownService = dropDownService;
    }

    @GetMapping("/getDropDownData")
    public ResponseEntity<?> getDropDownData(@RequestParam String organisationId, @RequestParam String entityID) {
        ResponseEntity<?> dropDownData = dropDownService.getDropDownData(organisationId, entityID);
        
        return dropDownData;
    }
    
}
