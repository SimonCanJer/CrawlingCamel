package com.txt_mining.cathegory.rest;

import com.txt_mining.cathegory.flow.data.api.ICategorizedUrlStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/cathegories")
public class CathregoryController {
    @Autowired
    ICategorizedUrlStorage storage;
    CathregoryController(){
        log.debug("Constructed");

    }

    @GetMapping(path="/entries/{id}")
    ResponseEntity<Map<String,Integer>> findCathegoryEntries(@PathVariable("id") String cathegory){
        ResponseEntity<Map<String,Integer>> result=null;
        try {
            result = ResponseEntity.ok(storage.match(cathegory));
        }
        catch(Exception e){
            result = ResponseEntity.internalServerError().build();
        }
        return result;
    }

}
