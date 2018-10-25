package br.inf.ufrgs.tecmides.services.models.rule;

import br.inf.ufrgs.tecmides.entities.rule.Rule;
import br.inf.ufrgs.tecmides.entities.rule.RuleOperand;
import br.inf.ufrgs.tecmides.services.models.ModelDataset;
import br.inf.ufrgs.tecmides.services.tool.association.AprioriAssociation;
import br.inf.ufrgs.tecmides.services.tool.association.AssociationTool;
import br.inf.ufrgs.tecmides.services.tool.filter.RuleFilter;
import br.inf.ufrgs.tecmides.services.tool.filter.RuleFilterImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import weka.core.Instances;

@Service
public class RuleServiceImpl implements RuleService {

    @Override
    public List<Rule> generateRules( ModelDataset dataset, int numRules, double minSupport, double minConfidence ) throws Exception {
        List<Rule> rules = new ArrayList<>();

        Instances instances = dataset.getInstances();
        AssociationTool associator = new AprioriAssociation();

        rules.addAll(associator.associate(instances, numRules, minSupport, minConfidence));

        return rules;
    }

    @Override
    public List<Rule> filter( List<Rule> rules, double minLift, double minConviction ) {
        RuleFilter filter = new RuleFilterImpl();

        List<RuleOperand> operands = new ArrayList<>();
        operands.add(new RuleOperand("st_indiv_assign_ltsubmit", "discouraged"));
        operands.add(new RuleOperand("st_indiv_subject_diff", "discouraged"));
        operands.add(new RuleOperand("st_group_assign_ltsubmit", "discouraged"));

        List<Rule> filteredRules = filter.filterByOperands(filter.filterByMinLift(filter.filterByMinConviction(rules, minLift), minConviction), operands);

        return filteredRules.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Map<String, String> mappedFields(Rule toMap) {
        Map<String, String> map = new HashMap<>();

        for( RuleOperand operand : toMap.getAntecedent() ) {
            map.put(operand.getName(), operand.getValue());
        }

        for( RuleOperand operand : toMap.getConsequent() ) {
            map.put(operand.getName(), operand.getValue());
        }

        return map;
    }

}
