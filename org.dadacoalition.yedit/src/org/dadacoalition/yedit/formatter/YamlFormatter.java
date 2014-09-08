package org.dadacoalition.yedit.formatter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

public class YamlFormatter {

    private final DumperOptions options;
    
    private YamlFormatter(Builder builder) {
        options = new DumperOptions();
        options.setExplicitStart(builder.explicitStart);
        options.setExplicitEnd(builder.explicitEnd);
        options.setIndent(builder.indent);
        options.setWidth(builder.lineLength);
        options.setDefaultFlowStyle(builder.flowStyle);     
        options.setPrettyFlow(builder.prettyFlow);
        options.setDefaultScalarStyle(builder.scalarStyle);
    }
    
    public static class Builder {
        private boolean explicitStart = false;
        private boolean explicitEnd = false;
        private int indent = 2;
        private int lineLength = 80;
        private FlowStyle flowStyle = FlowStyle.BLOCK;
        private boolean prettyFlow = false;
        private ScalarStyle scalarStyle = ScalarStyle.PLAIN;
        
        public Builder(){
            
        }
        
        public Builder explicitStart(boolean explicitStart){
            this.explicitStart = explicitStart; 
            return this;
        }
        
        public Builder explicitEnd(boolean explicitEnd){
            this.explicitEnd = explicitEnd; 
            return this;
        }

        public Builder indent(int indent){
            this.indent = indent; 
            return this;
        }

        public Builder lineLength(int lineLength){
            this.lineLength = lineLength; 
            return this;
        }
        
        public Builder flowStyle(FlowStyle flowStyle){
            this.flowStyle = flowStyle;
            return this;
        }
        
        public Builder prettyFlow(boolean prettyFlow){
            this.prettyFlow = prettyFlow;
            return this;
        }
        
        public Builder scalarStyle(ScalarStyle scalarStyle){
            this.scalarStyle = scalarStyle;
            return this;
        }
        
        
        public YamlFormatter build() {
            return new YamlFormatter(this);
        }
    }
    
    public String formatDocuments(Iterable<Object> documents){
        List<String> formattedDocuments = new ArrayList<String>();
        for(Object document : documents ){
            formattedDocuments.add(formatDocument(document));
        }
        if( formattedDocuments.isEmpty()){
            return formatDocument(null);
        } else {        
            return StringUtils.join(formattedDocuments, System.lineSeparator());
        }
    }
    
    private String formatDocument(Object document) {
                
        Yaml yaml = new Yaml(options);
        
        if( null == document ){
            StringBuilder sb = new StringBuilder();
            if(options.isExplicitStart()){
                sb.append("---");
            }
            
            sb.append(System.lineSeparator());
            
            if(options.isExplicitEnd()){
                sb.append("...");
            }
            
            return sb.toString();
        }
        return yaml.dump(document).trim();
        
    }
    
}
