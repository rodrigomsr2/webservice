package br.inf.ufrgs.tecmides.controllers;

import br.inf.ufrgs.tecmides.entities.rule.RuleModelInstance;
import br.inf.ufrgs.tecmides.exception.BusinessException;
import br.inf.ufrgs.tecmides.services.models.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = "RuleModel")
@RequestMapping("/rule_model")
@RestController
public class RuleModelController {

    @Autowired
    ModelService<RuleModelInstance> service;

    @ApiOperation("<descrição_do_método>")
    @PostMapping("/classify")
    // List<RuleModelInstance>
    public ResponseEntity classify( @ApiParam(name = "request body", value = "<descrição_parâmetro>")
    									@RequestBody List<RuleModelInstance> instances ) {
        try {
            log.info("Classify by rule model called.\nInstances: {}", instances.toString());

            return new ResponseEntity<>(service.classify(instances), HttpStatus.OK);
        } catch( Exception ex ) {
            String error = "Unabled to classify the instances!";
            throw new BusinessException(error);
        }
    }

    @ApiOperation("<descrição_do_método>")
    @PostMapping("/contribute")
    public ResponseEntity contribute( //@ApiParam(name = "request body", value = "<descrição_parâmetro>") 
										@RequestBody List<RuleModelInstance> instances ) {
        try {
        	log.info("Contribute by rule model called.\nInstances: {}", instances.toString());
            service.updateTrainingData(instances);
            log.info("Instances saved");
            service.updateModel();
            log.info("Model updated");

            return new ResponseEntity<>(HttpStatus.OK);
        } catch( Exception ex ) {
            String error = "Unabled to save the provided instances and update the model!";
            throw new BusinessException(error);
        }
    }

}
