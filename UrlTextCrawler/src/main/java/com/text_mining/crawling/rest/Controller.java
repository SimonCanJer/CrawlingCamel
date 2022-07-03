package com.text_mining.crawling.rest;

import com.txt_mining.common.asyncUtills.api.IAsyncTaskExecutionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/crawling")
public class Controller {
    @Autowired
    IAsyncTaskExecutionController<String> controller;

    @GetMapping(path="/url")
    public ResponseEntity<Mono<List<String>>> getText(@RequestParam(name="url") String text){
        ResponseEntity entity=null;
        List<String> request= new ArrayList<>(){{add(text);}};

        try{
            entity= ResponseEntity.ok().body(controller.acceptList(request));

        }
        catch(Exception e){
            entity= ResponseEntity.internalServerError().build();

        }
        return entity;
    }

    @CrossOrigin
    @PostMapping(path="/batch")
    public ResponseEntity<Mono<List<String>>> batch(@RequestBody List<String> request){
        ResponseEntity entity=null;


        try{
            entity= ResponseEntity.ok().body(controller.acceptList(request));
        }
        catch(Exception e){
            entity= ResponseEntity.internalServerError().build();

        }
        return entity;
    }


}
